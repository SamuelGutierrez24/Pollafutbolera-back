package co.edu.icesi.pollafutbolera.resolver;



public final class TenantContext {

    private static final ThreadLocal<Long> tenantId = new InheritableThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TenantContext.tenantId.set(tenantId);
    }
    public static Long getTenantId() {
        return tenantId.get();
    }
    public static void clear() {
        tenantId.remove();
    }

}
