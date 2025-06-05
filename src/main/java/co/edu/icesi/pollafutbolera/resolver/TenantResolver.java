package co.edu.icesi.pollafutbolera.resolver;

import jakarta.validation.constraints.NotNull;

@FunctionalInterface
public interface TenantResolver<T> {
    Long resolveTenantId (@NotNull T objetct);
}
