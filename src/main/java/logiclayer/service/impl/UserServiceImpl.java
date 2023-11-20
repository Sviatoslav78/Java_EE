package logiclayer.service.impl;

import dao.UserDao;
import entity.UserModel;
import jakarta.ejb.EJB;
import jakarta.ejb.Singleton;
import logiclayer.service.UserService;

@Singleton
public class UserServiceImpl implements UserService {
    @EJB
    private UserDao userDao;

    @Override
    public String getUserById(int parameter) {
        UserModel userModel = userDao.getUserById(parameter).get();
        return userModel.toString();
    }

    @Override
    public String createUser(int userId) {
        if (userDao.createUser(new UserModel(userId))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String updateUserName(int userId, String userName) {
        if (userDao.updateUser(new UserModel(userId, userName))) {
            return getUserById(userId);
        }
        return "";
    }

    @Override
    public String deleteUserById(int userId) {
        userDao.deleteUser(new UserModel(userId));
        return "";
    }

    @Override
    public String getAllUsers() {
        return userDao.getAllUsers().stream().map(UserModel::toString).reduce((s, s2) -> s + "," + s2).get();
    }
}
