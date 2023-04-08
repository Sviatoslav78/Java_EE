package dao;

import entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.PersistenceException;

public class UserDaoTemplate {
//клас с конфиг шаблона от Святослава
    private final EntityManagerFactory emf;
    private final EntityManager em;

    public UserDaoTemplate() {
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
