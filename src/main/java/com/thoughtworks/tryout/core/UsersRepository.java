package com.thoughtworks.tryout.core;

import com.thoughtworks.tryout.api.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by ctang on 4/21/16.
 */
public interface UsersRepository {
    List<User> findUsers();

    User newUser(String name);

    void createUser(Map newInstance);

    User getUserById(String publishedTemplateId);
}
