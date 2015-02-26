package org.katastrofi.cs5k;

import com.google.inject.Provider;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.Optional;
import java.util.function.Function;

import static com.google.common.collect.Sets.newHashSet;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.core.IsCollectionContaining.hasItem;

public class JPAPersistedCodeSetsHibernateIntegrationTest {

    private static EntityManagerFactory emf;

    private static Provider<EntityManager> emp;

    private JPAPersistedCodeSets testedJPAPersistedCodeSets;

    private static Code c01, c02;

    private static CodeSet cs01, cs02;


    @BeforeClass
    public static void initPersistence() {
        emf = Persistence.createEntityManagerFactory("codesets");
    }

    @AfterClass
    public static void cleanUp() {
        emf.close();
    }

    @After
    public void closeEM() {
        emp.get().close();
    }

    @Before
    public void init() {
        emp = emf::createEntityManager;

        testedJPAPersistedCodeSets = new JPAPersistedCodeSets(emp);

        inTX(testedJPAPersistedCodeSets::removeAll);

        inTX(() -> {
            c01 = new Code("C01", "codesc", newHashSet("1"));
            c02 = new Code("C02", "codesc2", newHashSet("2", "3"));
            cs01 = new CodeSet("CS01", "desc", newHashSet(c01, c02));
            cs02 = new CodeSet("CS02", "desc2", newHashSet());

            EntityManager em = emp.get();
            em.persist(cs01);
            em.persist(cs02);
        });
    }


    @Test
    public void allReturnsAllCodeSets() {
        assertThat(inTX(aVoid -> testedJPAPersistedCodeSets.all()),
                containsInAnyOrder(cs01, cs02));
    }

    @Test
    public void existingCodeSetWithNameIsFound() {
        assertThat(inTX(testedJPAPersistedCodeSets::withName, "CS01"),
                is(Optional.of(cs01)));
    }

    @Test
    public void nonExistentCodeSetWithNameIsEmpty() {
        assertThat(inTX(testedJPAPersistedCodeSets::withName, "CSNonex"),
                is(Optional.empty()));
    }

    @Test
    public void removingAllWipesOutAllCodeSets() {
        inTX(testedJPAPersistedCodeSets::removeAll);
        assertThat(inTX(aVoid -> testedJPAPersistedCodeSets.all()),
                is(Matchers.empty()));
    }

    @Test
    public void removingNamedCodeSetWipesTheCodeSet() {
        inTXV((String name) -> {
            testedJPAPersistedCodeSets.removeWithName(name);
            return null;
        }, "CS01");
        assertThat(inTX(testedJPAPersistedCodeSets::withName, "CS01"),
                is(Optional.empty()));
    }

    @Test
    public void updatingMergesUpdatesToPersistentCodeSet() {
        CodeSet cs1 = inTX(testedJPAPersistedCodeSets::withName, "CS01").get();
        Code c03 = new Code("C03", "new", newHashSet());
        cs1.addOrUpdate(c03);
        inTX(testedJPAPersistedCodeSets::addOrUpdate, cs1);
        assertThat(inTX(testedJPAPersistedCodeSets::withName, "CS01").get()
                .codes(), hasItem(c03));
    }

    @Test
    public void addingAddsToPersistentCodeSet() {
        CodeSet cs3 = new CodeSet("CS03", "baba", newHashSet());
        inTX(testedJPAPersistedCodeSets::addOrUpdate, cs3);
        assertThat(inTX(testedJPAPersistedCodeSets::withName, "CS03").get(),
                is(cs3));
    }


    private static <I> void inTXV(Function<I, Void> action, I input) {
        inTX(action, input);
    }

    private static <O> O inTX(Function<Void, O> action) {
        return inTX(action, null);
    }

    private static void inTX(Action action) {
        inTX(aVoid -> {
            action.execute();
            return null;
        }, null);
    }

    private static <I, O> O inTX(Function<I, O> action, I input) {
        EntityTransaction et = null;
        try {
            et = emp.get().getTransaction();
            et.begin();
            O output = action.apply(input);
            et.commit();
            return output;
        } catch (Exception e) {
            e.printStackTrace();
            if (et != null && et.isActive() && !et.getRollbackOnly()) {
                et.setRollbackOnly();
            }
            return null;
        }
    }

    private static interface Action {
        void execute();
    }
}
