package service;

import dao.jpa.UserDao;
import models.Users;

import java.util.Map;

/**
 * Created by Alexander on 17.02.2016.
 */
public class AuthenticateService {

    /**
     * Checks user on correct username and password.
     * @param data - map.
     * @return true - if user with this username and password exists.
     */
    public boolean authenticate(Map<String, String> data) {
        Users user = new Users();
        user.setPassword(data.get("password"));
        user.setUsername(data.get("username"));
        boolean success = UserDao.check(user);
        return success;
    }
}
