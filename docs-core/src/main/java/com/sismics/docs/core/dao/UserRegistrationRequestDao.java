package com.sismics.docs.core.dao;

import com.sismics.docs.core.model.jpa.UserRegistrationRequest;
import com.sismics.util.context.ThreadLocalContext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.Query;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class UserRegistrationRequestDao {

    public String create(UserRegistrationRequest request) {
        request.setId(UUID.randomUUID().toString());
        request.setCreateTime(new Date());
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.persist(request);
        return request.getId();
    }

    public void update(UserRegistrationRequest request) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        em.merge(request);
    }

    public UserRegistrationRequest findById(String id) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        return em.find(UserRegistrationRequest.class, id);
    }

    public UserRegistrationRequest findByUsername(String username) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT r FROM UserRegistrationRequest r WHERE r.username = :username");
        query.setParameter("username", username);
        try {
            return (UserRegistrationRequest) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public UserRegistrationRequest findByEmail(String email) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT r FROM UserRegistrationRequest r WHERE r.email = :email");
        query.setParameter("email", email);
        try {
            return (UserRegistrationRequest) query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public List<UserRegistrationRequest> findByStatus(String status, int limit, int offset) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT r FROM UserRegistrationRequest r WHERE r.status = :status ORDER BY r.createTime DESC");
        query.setParameter("status", status);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<UserRegistrationRequest> findByStatusIn(List<String> statusList, int limit, int offset) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT r FROM UserRegistrationRequest r WHERE r.status IN :statusList ORDER BY r.createTime DESC");
        query.setParameter("statusList", statusList);
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public long countByStatus(String status) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT COUNT(r) FROM UserRegistrationRequest r WHERE r.status = :status");
        query.setParameter("status", status);
        return (Long) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public List<UserRegistrationRequest> findAll(int limit, int offset) {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT r FROM UserRegistrationRequest r ORDER BY r.createTime DESC");
        query.setFirstResult(offset);
        query.setMaxResults(limit);
        return query.getResultList();
    }

    public long countAll() {
        EntityManager em = ThreadLocalContext.get().getEntityManager();
        Query query = em.createQuery("SELECT COUNT(r) FROM UserRegistrationRequest r");
        return (Long) query.getSingleResult();
    }
} 