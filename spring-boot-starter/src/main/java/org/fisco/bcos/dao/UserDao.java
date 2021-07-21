package org.fisco.bcos.dao;

import org.fisco.bcos.domain.User;

public interface UserDao {
    User getUser(String name);
    boolean setUser(User user);
}
