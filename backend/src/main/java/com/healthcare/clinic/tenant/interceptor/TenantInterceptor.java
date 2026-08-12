package com.healthcare.clinic.tenant.interceptor;

import com.healthcare.clinic.tenant.context.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class TenantInterceptor implements HandlerInterceptor {

    private static final String TENANT_HEADER = "X-Tenant-ID";
    private static final String BRANCH_HEADER = "X-Branch-ID";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String tenantIdStr = request.getHeader(TENANT_HEADER);
        String branchIdStr = request.getHeader(BRANCH_HEADER);

        if (tenantIdStr != null && !tenantIdStr.isEmpty()) {
            try {
                TenantContextHolder.setTenantId(Long.valueOf(tenantIdStr));
            } catch (NumberFormatException e) {
                // Ignore invalid format, could be logged or rejected
            }
        }
        
        if (branchIdStr != null && !branchIdStr.isEmpty()) {
            try {
                TenantContextHolder.setBranchId(Long.valueOf(branchIdStr));
            } catch (NumberFormatException e) {
                // Ignore
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        TenantContextHolder.clearContext();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        TenantContextHolder.clearContext();
    }
}
