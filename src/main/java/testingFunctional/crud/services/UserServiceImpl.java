package testingFunctional.crud.services;

import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import testingFunctional.crud.dao.UserDao;
import testingFunctional.crud.model.UserModel;

import javax.enterprise.inject.Instance;

@Singleton
public class UserServiceImpl implements UserService {
    @EJB
    private Instance<UserDao> userDao;

    @Override
    public String getUserById(int parameter) {
        UserModel userModel = userDao.get().getUserById(parameter).get();
        return userModel.toString();
    }

    @Override
    public String createUser(int userId) {
        if (userDao.get().createUser(new UserModel(userId))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String updateUserName(int userId, String userName) {
        if (userDao.get().updateUser(new UserModel(userId, userName))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String deleteUserById(int userId) {
        userDao.get().deleteUser(new UserModel(userId));
        return "";
    }

    @Override
    public String getAllUsers() {
        return userDao.get().getAllUsers().stream().map(UserModel::toString).reduce((s, s2) -> s + "," + s2).get();
    }
}
