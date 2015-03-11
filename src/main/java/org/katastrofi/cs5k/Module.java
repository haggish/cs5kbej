package org.katastrofi.cs5k;

import com.google.inject.Binder;
import com.google.inject.Singleton;

public class Module implements com.google.inject.Module {

    @Override
    public void configure(Binder binder) {
        binder.bind(RESTEndpoint.class).in(Singleton.class);
        binder.bind(Service.class).in(Singleton.class);
        binder.bind(CodeSets.class)
                .to(JPAPersistedCodeSets.class).in(Singleton.class);
    }

}
