package org.katastrofi.cs5k;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import static com.google.common.collect.Sets.newHashSet;

public class JPAPersistedCodeSets implements CodeSets {

    private final EntityManager entityManager;


    @Inject
    public JPAPersistedCodeSets(EntityManager entityManager) {
        this.entityManager = entityManager;
    }


    @Override
    public Set<CodeSet> all() {
        CriteriaQuery<CodeSet> query =
                entityManager.getCriteriaBuilder().createQuery(CodeSet.class);
        Root<CodeSet> root = query.from(CodeSet.class);
        return newHashSet(
                entityManager.createQuery(query.select(root)).getResultList());
    }


    @Override
    public Optional<CodeSet> withName(String name) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<CodeSet> q = cb.createQuery(CodeSet.class);
        Root<CodeSet> root = q.from(CodeSet.class);
        Predicate nameIsEqual = cb.equal(root.get(CodeSet_.name), name);
        q.where(nameIsEqual);
        TypedQuery<CodeSet> tq = entityManager.createQuery(q);
        return optionalOf(tq.getResultList());
    }

    @Override
    public void removeAll() {
        all().stream().forEach(entityManager::remove);
    }

    @Override
    public void removeWithName(String name) {
        Optional<CodeSet> possibleCodeSet = withName(name);
        if (possibleCodeSet.isPresent()) {
            entityManager.remove(possibleCodeSet.get());
        }
    }

    @Override
    public boolean addOrUpdate(CodeSet codeSet) {
        Optional<CodeSet> possibleCodeSet = withName(codeSet.name());
        if (possibleCodeSet.isPresent()) {
            entityManager.merge(codeSet);
            return true;
        } else {
            entityManager.persist(codeSet);
            return false;
        }
    }


    private <T> Optional<T> optionalOf(Collection<T> resultList) {
        return resultList.isEmpty() ? Optional.empty() :
                Optional.of(resultList.iterator().next());
    }
}
