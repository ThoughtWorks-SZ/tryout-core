package com.thoughtworks.tryout;

import com.thoughtworks.tryout.api.UsersResourceImpl;
import com.thoughtworks.tryout.api.resource.UsersResource;
import com.thoughtworks.tryout.core.UsersRepository;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.session.SqlSessionManager;
import org.glassfish.grizzly.http.server.HttpServer;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;

import java.io.IOException;
import java.io.Reader;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;

public class App {

    private static final URI BASE_URI = URI.create("http://localhost:8080/admin/");
    public static final String ROOT_PATH = "published-templates";

    public static void main(String[] args) {
        try {
            System.out.println("\"Hello World\" Jersey Example App");

            final HttpServer server = GrizzlyHttpServerFactory.createHttpServer(BASE_URI, createSessionInViewConfig(), false);
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.shutdownNow();
                }
            }));
            server.start();

            System.out.println(String.format("Application started.%nTry out %s%s%nStop the application using CTRL+C",
                    BASE_URI, ROOT_PATH));

            Thread.currentThread().join();
        } catch (IOException | InterruptedException ex) {
            System.out.println("aa");
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }

    public static ResourceConfig createSessionInViewConfig() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig();
        String resource = "mybatis-config.xml";

        final Reader reader  = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "test");
        final SqlSessionManager sqlSessionManager = SqlSessionManager.newInstance(sqlSessionFactory);

        final UsersRepository usersRepository = sqlSessionManager.getMapper(UsersRepository.class);


        final ResourceConfig config = new ResourceConfig()
                .packages("com.thoughtworks.tryout")
                .register(new AbstractBinder() {
                    @Override
                    protected void configure() {
                        bind(UsersResourceImpl.class).to(UsersResource.class);
                        bind(usersRepository).to(UsersRepository.class);
                        bind(sqlSessionManager).to(SqlSessionManager.class);
                    }
                });

        return config;
    }


    public static ResourceConfig create() throws IOException {
        final ResourceConfig resourceConfig = new ResourceConfig();
        String resource = "mybatis-config.xml";

        final Reader reader  = Resources.getResourceAsReader(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader, "test");

        SqlSession session = sqlSessionFactory.openSession();
        final UsersRepository usersRepository = session.getMapper(UsersRepository.class);

        resourceConfig.register(new AbstractBinder() {
            @Override
            protected void configure() {
                bind(UsersResourceImpl.class).to(UsersResource.class);
                bind(usersRepository).to(UsersRepository.class);
            }
        }).packages("com.thoughtworks.tryout.api");

        return resourceConfig;
    }

}

