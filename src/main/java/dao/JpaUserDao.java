package dao;

import entity.UserModel;
import jakarta.ejb.Singleton;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;

@Singleton
public class JpaUserDao extends AbstractJpaDao<UserModel> implements UserDao {

    private static final String FIND_ENABLED_USER_BY_EMAIL = "SELECT u FROM UserModel u WHERE u.id = :id";

    public JpaUserDao() {
        super(UserModel.class);
    }

    @Override
    public Optional<UserModel> getUserById(int parameter) {
        TypedQuery<UserModel> query = entityManager.createQuery(FIND_ENABLED_USER_BY_EMAIL, UserModel.class);
        query.setParameter("id", parameter);
        return Optional.ofNullable(query.getSingleResult());
    }

    @Override
    public boolean createUser(UserModel userModel) {
        super.create(userModel);
        return true;
    }

    @Override
    public boolean updateUser(UserModel userModel) {
        //SELECT * FROM user WHERE user.email = ? AND user.password = ? AND user.account_non_expired = true AND user.account_non_locked = true AND user.credentials_non_expired = true AND user.enabled = true
        TypedQuery<UserModel> query = entityManager.createQuery(FIND_ENABLED_USER_BY_EMAIL, UserModel.class);
        query.setParameter("id", userModel.getId());
        UserModel userModelFromDb = query.getSingleResult();
        userModelFromDb.setName(userModel.getName());
        super.update(userModelFromDb);
        return true;

    }

    @Override
    public boolean deleteUser(UserModel userModel) {
        super.delete(userModel);
        return true;
    }

    @Override
    public List<UserModel> getAllUsers() {
        return entityManager.createQuery("SELECT u FROM UserModel u", UserModel.class).getResultList();
    }

}
