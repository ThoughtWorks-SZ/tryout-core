package com.thoughtworks.fixed.assets.api;

import com.thoughtworks.tryout.api.model.User;
import com.thoughtworks.tryout.core.UsersRepository;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Reader;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertThat;

/**
 * Created by ctang on 4/21/16.
 */
public class UsersRepositoryTest {
    private SqlSessionFactory sqlSessionFactory;
    private UsersRepository usersRepository;
    private SqlSession session;

    @Before
    public void setUp() throws IOException, SQLException {
        String resource = "mybatis-config.xml";

        Reader reader  = Resources.getResourceAsReader(resource);

        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "test");

        session = sqlSessionFactory.openSession();
        session.getConnection().setAutoCommit(false);
        usersRepository = session.getMapper(UsersRepository.class);
    }

    @After
    public void tearDown(){
        session.rollback();
        session.close();

    }

    @Test
    public void should_find_all_users() throws Exception {
        List<User> users = usersRepository.findUsers();

        assertThat(users.size(), is(2));
        assertThat(users.get(0).getName(), is("first user"));
        assertThat(users.get(0).getRole(), is("people"));
        assertThat(users.get(1).getName(), is("second user"));
        assertThat(users.get(1).getRole(), is("buddy"));
    }

    @Test
    public void should_create_a_new_user() throws Exception {
        Map newInstance = new HashMap();
        newInstance.put("id", "cbd59eb2-086d-11e6-99fe-0b01e67fc86e");
        newInstance.put("name", "third user");
        newInstance.put("role", "people");
        usersRepository.createUser(newInstance);

        assertThat((String) newInstance.get("name"), is("third user"));
        assertThat(((String) newInstance.get("id")).length(), is(36));
    }

    @Test
    public void should_get_user_by_id() throws Exception {
        User theUser =
                usersRepository.getUserById("823efe78-0869-11e6-99fe-0b01e67fc86e");
        assertThat(theUser.getId(), is("823efe78-0869-11e6-99fe-0b01e67fc86e"));
        assertThat(theUser.getName(), is("first user"));
        assertThat(theUser.getRole(), is("people"));
    }
}
