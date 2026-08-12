package com.healthcare.clinic.tenant.context;

public class TenantContextHolder {

    private static final ThreadLocal<Long> TENANT_ID = new ThreadLocal<>();
    private static final ThreadLocal<Long> BRANCH_ID = new ThreadLocal<>();

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    public static void clearTenantId() {
        TENANT_ID.remove();
    }

    public static void setBranchId(Long branchId) {
        BRANCH_ID.set(branchId);
    }

    public static Long getBranchId() {
        return BRANCH_ID.get();
    }

    public static void clearBranchId() {
        BRANCH_ID.remove();
    }

    public static void clearContext() {
        TENANT_ID.remove();
        BRANCH_ID.remove();
    }
}
