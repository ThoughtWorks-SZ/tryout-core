package com.thoughtworks.tryout.api;

import com.thoughtworks.tryout.api.model.User;
import com.thoughtworks.tryout.api.resource.UsersResource;
import com.thoughtworks.tryout.core.UsersRepository;

import javax.inject.Inject;
import javax.ws.rs.PathParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ctang on 4/21/16.
 */
public class UsersResourceImpl implements UsersResource {

    @Inject
    private UsersRepository usersRepository;


    @Override
    public GetUsersResponse getUsers() throws Exception {
        return null;
    }

    @Override
    public PostUsersResponse postUsers(User entity) throws Exception {
        return null;
    }

    @Override
    public GetUsersByUserIdResponse getUsersByUserId(@PathParam("user-id") String userId) throws Exception {
        return null;
    }
}
