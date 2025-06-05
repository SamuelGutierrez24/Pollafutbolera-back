package co.edu.icesi.pollafutbolera.repository;

import co.edu.icesi.pollafutbolera.resolver.TenantContext;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.lang.NonNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

public class TenantAwareRepository<T, ID extends Serializable> extends SimpleJpaRepository<T, ID> {

    private final EntityManager entityManager;

    public TenantAwareRepository(JpaEntityInformation<T, ?> entityInformation, EntityManager entityManager) {
        super(entityInformation, entityManager);
        this.entityManager = entityManager;
    }

    @Override
    @NonNull
    public Optional<T> findById(@NonNull ID id) {
        activateTenantFilter();
        return super.findById(id);
    }

    @Override
    @NonNull
    public List<T> findAll() {
        activateTenantFilter();
        return super.findAll();
    }

    @Override
    @NonNull
    public List<T> findAll(@NonNull Sort sort) {
        activateTenantFilter();
        return super.findAll(sort);
    }

    @Override
    @NonNull
    public Page<T> findAll(@NonNull Pageable pageable) {
        activateTenantFilter();
        return super.findAll(pageable);
    }

    @Override
    @NonNull
    public List<T> findAllById(@NonNull Iterable<ID> ids) {
        activateTenantFilter();
        return super.findAllById(ids);
    }

    @Override
    public long count() {
        activateTenantFilter();
        return super.count();
    }

    @Override
    @NonNull
    public <S extends T> Optional<S> findOne(@NonNull Example<S> example) {
        activateTenantFilter();
        return super.findOne(example);
    }

    @Override
    @NonNull
    public <S extends T> List<S> findAll(@NonNull Example<S> example) {
        activateTenantFilter();
        return super.findAll(example);
    }

    @Override
    @NonNull
    public <S extends T> List<S> findAll(@NonNull Example<S> example, @NonNull Sort sort) {
        activateTenantFilter();
        return super.findAll(example, sort);
    }

    @Override
    @NonNull
    public <S extends T> Page<S> findAll(@NonNull Example<S> example, @NonNull Pageable pageable) {
        activateTenantFilter();
        return super.findAll(example, pageable);
    }

    @Override
    public <S extends T> long count(@NonNull Example<S> example) {
        activateTenantFilter();
        return super.count(example);
    }

    @Override
    @NonNull
    public Optional<T> findOne(Specification<T> spec) {
        activateTenantFilter();
        return super.findOne(spec);
    }

    @Override
    @NonNull
    public List<T> findAll(Specification<T> spec) {
        activateTenantFilter();
        return super.findAll(spec);
    }

    @Override
    @NonNull
    public Page<T> findAll(Specification<T> spec, @NonNull Pageable pageable) {
        activateTenantFilter();
        return super.findAll(spec, pageable);
    }

    @Override
    @NonNull
    public List<T> findAll(Specification<T> spec, @NonNull Sort sort) {
        activateTenantFilter();
        return super.findAll(spec, sort);
    }

    @Override
    public long count(Specification<T> spec) {
        activateTenantFilter();
        return super.count(spec);
    }

    private void activateTenantFilter() {
        if (TenantContext.getTenantId() != null) {
            Session session = entityManager.unwrap(Session.class);
            if (session.getEnabledFilter("tenantFilter") == null) {
                 session.enableFilter("tenantFilter")
                        .setParameter("tenantId", TenantContext.getTenantId());
            }
        }
    }
}