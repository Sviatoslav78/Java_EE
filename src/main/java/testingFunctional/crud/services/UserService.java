package testingFunctional.crud.services;

import jakarta.ejb.Singleton;

@Singleton
public interface UserService {
    String getUserById(int parameter);

    String createUser(int userId);

    String updateUserName(int parseInt, String parseInt1);

    String deleteUserById(int parseInt);

    String getAllUsers();
}

