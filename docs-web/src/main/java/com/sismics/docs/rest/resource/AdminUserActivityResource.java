package com.sismics.docs.rest.resource;

import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.DocumentDao;
import com.sismics.docs.core.dao.FileDao;
import com.sismics.docs.core.dao.dto.UserDto;
import com.sismics.docs.rest.constant.BaseFunction;
import com.sismics.rest.exception.ForbiddenClientException;
import com.sismics.rest.exception.ServerException;
import com.sismics.rest.util.ValidationUtil;

import jakarta.json.Json;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObjectBuilder;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

/**
 * Admin user activity REST resource.
 */
@Path("/admin/user/activity")
public class AdminUserActivityResource extends BaseResource {
    
    /**
     * Get all users activity.
     *
     * @return Response
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get() {
        if (!authenticate()) {
            throw new ForbiddenClientException();
        }
        checkBaseFunction(BaseFunction.ADMIN);

        // Get all users
        UserDao userDao = new UserDao();
        List<UserDto> userList = userDao.findByCriteria(new com.sismics.docs.core.dao.criteria.UserCriteria(), null);
        
        // Build response
        JsonArrayBuilder users = Json.createArrayBuilder();
        for (UserDto user : userList) {
            // Get user's document count
            DocumentDao documentDao = new DocumentDao();
            long documentCount = documentDao.findByUserId(user.getId()).size();
            
            // Get user's file count and total size
            FileDao fileDao = new FileDao();
            long fileCount = fileDao.findByUserId(user.getId()).size();
            
            users.add(Json.createObjectBuilder()
                .add("id", user.getId())
                .add("username", user.getUsername())
                .add("email", user.getEmail())
                .add("storage_current", user.getStorageCurrent())
                .add("storage_quota", user.getStorageQuota())
                .add("document_count", documentCount)
                .add("file_count", fileCount)
                .add("create_date", user.getCreateTimestamp())
                .add("disabled", user.getDisableTimestamp() != null)
                .add("totp_enabled", user.getTotpKey() != null));
        }
        
        JsonObjectBuilder response = Json.createObjectBuilder()
            .add("users", users);
        
        return Response.ok().entity(response.build()).build();
    }
} 