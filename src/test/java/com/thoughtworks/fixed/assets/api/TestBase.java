package com.thoughtworks.fixed.assets.api;


import com.thoughtworks.tryout.api.UsersResourceImpl;
import com.thoughtworks.tryout.api.resource.UsersResource;
import com.thoughtworks.tryout.core.UsersRepository;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.glassfish.jersey.test.TestProperties;

import javax.ws.rs.core.Application;

import static org.mockito.Mockito.mock;

public class TestBase extends JerseyTest {
    protected UsersRepository usersRepository = mock(UsersRepository.class);

    @Override
    protected Application configure() {
        
//        enable(TestProperties.LOG_TRAFFIC);
        enable(TestProperties.DUMP_ENTITY);
//        enable(TestProperties.RECORD_LOG_LEVEL);
        ResourceConfig config = new ResourceConfig();
//        config.register(UsersResourceImpl.class);
        return config.register(new AbstractBinder() {


            @Override
            protected void configure() {
                bind(UsersResourceImpl.class).to(UsersResource.class);
                bind(usersRepository).to(UsersRepository.class);


            }
        }).packages("com.thoughtworks.tryout.api");
    }
}
