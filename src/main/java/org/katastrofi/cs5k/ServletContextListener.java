package org.katastrofi.cs5k;

import com.google.inject.Injector;
import com.google.inject.persist.PersistService;
import com.google.inject.persist.jpa.JpaPersistModule;
import org.jboss.resteasy.plugins.guice.GuiceResteasyBootstrapServletContextListener;

import javax.servlet.ServletContext;
import java.util.List;

import static java.util.Arrays.asList;

public class ServletContextListener
        extends GuiceResteasyBootstrapServletContextListener {

    @Override
    protected List<com.google.inject.Module> getModules(
            ServletContext context) {
        return asList(new JpaPersistModule("codesets"), new Module());
    }

    @Override
    public void withInjector(Injector injector) {
        injector.getInstance(PersistService.class).start();
    }
}
