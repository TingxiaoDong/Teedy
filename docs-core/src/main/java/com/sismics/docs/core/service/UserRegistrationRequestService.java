package com.sismics.docs.core.service;

import com.sismics.docs.core.dao.UserDao;
import com.sismics.docs.core.dao.UserRegistrationRequestDao;
import com.sismics.docs.core.model.jpa.User;
import com.sismics.docs.core.model.jpa.UserRegistrationRequest;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.PersistenceException;
import java.util.Date;
import java.util.List;
import java.util.Arrays;

public class UserRegistrationRequestService {
    private UserRegistrationRequestDao requestDao = new UserRegistrationRequestDao();
    private UserDao userDao = new UserDao();

    /**
     * 创建注册请求
     */
    public String createRequest(UserRegistrationRequest request) {
        // 检查邮箱是否已存在
        UserRegistrationRequest existingRequest = requestDao.findByEmail(request.getEmail());
        if (existingRequest != null) {
            throw new RuntimeException("该邮箱地址已被注册");
        }
        
        request.setStatus("PENDING");
        return requestDao.create(request);
    }

    /**
     * 获取所有注册请求
     */
    public List<UserRegistrationRequest> getAllRequests(int limit, int offset) {
        return requestDao.findAll(limit, offset);
    }

    /**
     * 获取待审核注册请求
     */
    public List<UserRegistrationRequest> getPendingRequests(int limit, int offset) {
        return requestDao.findByStatus("PENDING", limit, offset);
    }

    /**
     * 获取已处理的注册请求（包括accepted和rejected）
     */
    public List<UserRegistrationRequest> getProcessedRequests(int limit, int offset) {
        return requestDao.findByStatusIn(Arrays.asList("ACCEPTED", "REJECTED"), limit, offset);
    }

    /**
     * 管理员审核通过
     */
    public void acceptRequest(String requestId, String adminRemark) {
        System.out.println("开始处理接受注册请求: " + requestId);
        try {
            EntityManager em = ThreadLocalContext.get().getEntityManager();
            
            // 使用JPA查询获取请求
            UserRegistrationRequest request = requestDao.findById(requestId);
            System.out.println("找到注册请求: " + (request != null ? request.getUsername() : "null"));
            if (request == null || !"PENDING".equalsIgnoreCase(request.getStatus())) {
                System.out.println("请求状态不正确: " + (request != null ? request.getStatus() : "null"));
                throw new RuntimeException("请求不存在或已处理");
            }
            
            // 获取密码
            String password = request.getPassword();
            System.out.println("从请求对象获取密码: " + (password != null ? "不为空" : "为空"));
            
            // 检查密码
            if (password == null) {
                throw new RuntimeException("注册请求中的密码为空");
            }
            
            // 创建正式用户
            User user = new User();
            System.out.println("request.getUsername():"+request.getUsername());
            user.setId(null); // 由UserDao生成
            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setCreateDate(new Date());
            user.setPassword(password); // 设置密码
            user.setRoleId("user"); // 设置默认角色为普通用户
            user.setStorageQuota(1024L * 1024L * 1024L); // 设置默认存储配额为1GB
            user.setStorageCurrent(0L); // 设置当前存储使用量为0
            
            // 使用原始密码创建用户
            try {
                System.out.println("开始创建用户，密码长度: " + (password != null ? password.length() : 0));
                String userId = userDao.create(user, "system"); // 使用"system"作为审计日志的用户ID
                System.out.println("用户创建成功，用户ID: " + userId);
            } catch (Exception e) {
                System.out.println("创建用户失败: " + e.getMessage());
                throw new RuntimeException("创建用户失败: " + e.getMessage(), e);
            }
            
            // 更新请求状态
            request.setStatus("ACCEPTED");
            request.setAdminRemark(adminRemark);
            requestDao.update(request);
            System.out.println("更新请求状态成功");
        } catch (Exception e) {
            System.out.println("发生异常: " + e.getMessage());
            throw e;
        }
    }

    /**
     * 管理员拒绝
     */
    public void rejectRequest(String requestId, String adminRemark) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();
        try {
            UserRegistrationRequest request = requestDao.findById(requestId);
            if (request == null || !"PENDING".equals(request.getStatus())) {
                throw new RuntimeException("请求不存在或已处理");
            }
            request.setStatus("REJECTED");
            request.setAdminRemark(adminRemark);
            requestDao.update(request);
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            throw e;
        }
    }

    /**
     * 通过ID获取请求
     */
    public UserRegistrationRequest getRequestById(String id) {
        return requestDao.findById(id);
    }
} 