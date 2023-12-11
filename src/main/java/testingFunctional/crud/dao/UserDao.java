package testingFunctional.crud.dao;

import jakarta.ejb.Singleton;
import testingFunctional.crud.model.UserModel;

import java.util.List;
import java.util.Optional;

@Singleton
public interface UserDao {
    Optional<UserModel> getUserById(int parameter);

    boolean createUser(UserModel userModel);

    boolean updateUser(UserModel userModel);

    boolean deleteUser(UserModel userModel);

    List<UserModel> getAllUsers();
}
