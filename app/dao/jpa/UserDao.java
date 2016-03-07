package dao.jpa;

import models.Users;
import play.db.jpa.JPA;

/**
 * Created by Alexander on 16.02.2016.
 */
public class UserDao {
    public static boolean check(Users user) {
        boolean result = JPA.em().createQuery("select r FROM Users r WHERE r.username = :username and r.password =:password")
                .setParameter("username", user.getUsername()).setParameter("password", user.getPassword()).getResultList().isEmpty();
        return !result;

    }
}
