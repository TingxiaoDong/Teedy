package com.sismics.docs.rest.resource;

import com.sismics.docs.core.service.UserRegistrationRequestService;
import com.sismics.docs.core.model.jpa.UserRegistrationRequest;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.docs.rest.constant.BaseFunction;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.MediaType;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/register-request")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
public class UserRegistrationRequestResource extends BaseResource {
    private static final Logger log = LoggerFactory.getLogger(UserRegistrationRequestResource.class);
    private UserRegistrationRequestService service = new UserRegistrationRequestService();

    /**
     * 访客提交注册请求
     */
    @POST
    public Response createRequest(
            @FormParam("username") String username,
            @FormParam("email") String email,
            @FormParam("password") String password,
            @FormParam("requestMessage") String requestMessage) {
        UserRegistrationRequest request = new UserRegistrationRequest();
        request.setUsername(username);
        request.setEmail(email);
        request.setPassword(password);
        request.setRequestMessage(requestMessage);
        
        String id = service.createRequest(request);
        return Response.ok().entity("{\"id\":\"" + id + "\"}").build();
    }

    /**
     * 管理员获取所有注册请求
     */
    @GET
    public Response getAllRequests(@QueryParam("status") String status,
                                 @QueryParam("limit") @DefaultValue("50") int limit,
                                 @QueryParam("offset") @DefaultValue("0") int offset) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);
        
        log.info("Getting registration requests with status: {}, limit: {}, offset: {}", status, limit, offset);
        
        List<UserRegistrationRequest> list;
        if ("PENDING".equals(status)) {
            list = service.getPendingRequests(limit, offset);
        } else if ("PROCESSED".equals(status)) {
            list = service.getProcessedRequests(limit, offset);
        } else {
            list = service.getAllRequests(limit, offset);
        }
        
        log.info("Found {} registration requests", list.size());
        return Response.ok().entity(list).build();
    }

    /**
     * 管理员同意注册请求
     */
    @POST
    @Path("/{id}/accept")
    public Response acceptRequest(@PathParam("id") String id, 
                                @FormParam("adminRemark") String adminRemark) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);
        
        log.info("Accepting registration request: {}, adminRemark: {}", id, adminRemark);
        try {
            service.acceptRequest(id, adminRemark);
            log.info("Successfully accepted registration request: {}", id);
            return Response.ok().build();
        } catch (Exception e) {
            log.error("Error accepting registration request: " + id, e);
            throw e;
        }
    }

    /**
     * 管理员拒绝注册请求
     */
    @POST
    @Path("/{id}/reject")
    public Response rejectRequest(@PathParam("id") String id,
                                @FormParam("adminRemark") String adminRemark) {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);
        
        log.info("Rejecting registration request: {}", id);
        service.rejectRequest(id, adminRemark);
        return Response.ok().build();
    }
} 