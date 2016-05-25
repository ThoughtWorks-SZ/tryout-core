package com.thoughtworks.fixed.assets.api;

import com.thoughtworks.tryout.api.model.User;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Response;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by ctang on 4/21/16.
 */
public class UsersResourceImplTest extends TestBase {
    private String basePath = "/users";
    private User firstUser = new User();
    private User secondUser = new User();
    private User newUser = new User();


    @Override
    public void setUp() throws Exception {
        firstUser.setId("f7b1d300-086a-11e6-99fe-0b01e67fc86e");
        firstUser.setName("first user");

        secondUser.setId("f89747f0-086a-11e6-99fe-0b01e67fc86e");
        secondUser.setName("second user");

        newUser.setName("new user");

        when(usersRepository.findUsers()).thenReturn(Arrays.asList(firstUser, secondUser));

        when(usersRepository.getUserById(anyString())).thenReturn(firstUser);
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Object[] arguments = invocation.getArguments();
                ((Map)arguments[0]).put("id", "f8f19caa-086a-11e6-99fe-0b01e67fc86e");
                return null;
            }
        }).when(usersRepository).createUser(anyMap());
        super.setUp();
    }

    @Test
    public void should_list_all_users() throws Exception {
        Response response = target(basePath).request().get();

        assertThat(response.getStatus(), is(200));

        List<Map> result = response.readEntity(List.class);

        assertThat(result.size(), is(2));

        Map actualFirstUser = result.get(0);
        Map actualSecondUser = result.get(1);

        assertThat((String) actualFirstUser.get("id"), is(firstUser.getId()));
        assertThat((String) actualFirstUser.get("name"), is(firstUser.getName()));

        assertThat((String) actualSecondUser.get("id"), is(secondUser.getId()));
        assertThat((String) actualSecondUser.get("name"), is(secondUser.getName()));
    }

    @Test
    public void should_get_published_template_by_id() throws Exception {
        Response response = target(basePath+"/f7b1d300-086a-11e6-99fe-0b01e67fc86e").request().get();

        assertThat(response.getStatus(), is(200));

        Map actualUser = response.readEntity(Map.class);

        assertThat((String) actualUser.get("id"), is(firstUser.getId()));
        assertThat((String) actualUser.get("name"), is(firstUser.getName()));
    }

    @Test
    public void should_get_not_found_by_invalid_id() throws Exception {
        Response response = target(basePath+"/ffffffff-086a-11e6-99fe-0b01e67fc86e").request().get();

        assertThat(response.getStatus(), is(404));
    }

    @Test
    public void should_create_one_published_template() throws Exception {
        Response response = target(basePath).request().post(Entity.entity(newUser, "application/json"));

        assertThat(response.getStatus(), is(201));

        Map actualNewUser = response.readEntity(Map.class);
        assertThat(((String) actualNewUser.get("id")).length(), is(36));
        assertThat((String) actualNewUser.get("name"), is(firstUser.getName()));
    }
}
