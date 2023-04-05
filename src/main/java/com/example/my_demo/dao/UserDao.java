package com.example.my_demo.dao;

import com.example.my_demo.entity.User;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

@Stateless
public class UserDao {

    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UserDao() {
        emf = Persistence.createEntityManagerFactory("default");
        em = emf.createEntityManager();
    }

    public boolean persistUser(User user) {
        try {
            em.getTransaction().begin();
            em.persist(user);
            em.getTransaction().commit();
            em.clear();
            return true;
        }
        catch(PersistenceException e){
            System.out.println(e.getMessage());
            em.clear();
            em.close();
            emf.close();
            return false;
        }
    }

}
