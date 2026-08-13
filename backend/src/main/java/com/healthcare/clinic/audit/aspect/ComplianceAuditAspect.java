package com.healthcare.clinic.audit.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.healthcare.clinic.audit.annotation.AuditableAction;
import com.healthcare.clinic.audit.entity.AuditRecord;
import com.healthcare.clinic.audit.service.AuditTrailService;
import com.healthcare.clinic.identity.entity.User;
import com.healthcare.clinic.identity.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.ZonedDateTime;
import java.util.Optional;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class ComplianceAuditAspect {

    private final AuditTrailService auditTrailService;
    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;

    @Around("@annotation(auditableAction)")
    public Object auditAction(ProceedingJoinPoint joinPoint, AuditableAction auditableAction) throws Throwable {
        AuditRecord record = AuditRecord.builder()
                .createdAt(ZonedDateTime.now())
                .moduleName(auditableAction.module())
                .actionName(auditableAction.action())
                .resourceType(auditableAction.resourceType())
                .sensitivityLevel(auditableAction.sensitivityLevel())
                .sourceChannel("WEB") // Default, can be overridden if API
                .build();

        populateActorAndNetworkContext(record);

        // Capture arguments (basic attempt to find Patient ID or Resource ID)
        extractArguments(joinPoint, record);

        Object result;
        try {
            result = joinPoint.proceed();
            record.setOutcome("SUCCESS");
        } catch (org.springframework.security.access.AccessDeniedException e) {
            record.setOutcome("DENIED");
            record.setReason(e.getMessage());
            throw e;
        } catch (Exception e) {
            record.setOutcome("FAILED");
            record.setReason(e.getMessage());
            throw e;
        } finally {
            auditTrailService.logAuditAsync(record);
        }

        return result;
    }

    private void populateActorAndNetworkContext(AuditRecord record) {
        // Actor
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !"anonymousUser".equals(auth.getPrincipal())) {
            if (auth.getPrincipal() instanceof UserDetails) {
                String email = ((UserDetails) auth.getPrincipal()).getUsername();
                Optional<User> userOpt = userRepository.findByEmail(email);
                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    record.setActorId(user.getId());
                    record.setActorRole(user.getRoles().isEmpty() ? "UNKNOWN" : user.getRoles().iterator().next().getName());
                    record.setTenantId(user.getBranchId());
                    record.setActorType("HUMAN");
                }
            }
        } else {
            record.setActorType("SYSTEM");
        }

        // Network
        ServletRequestAttributes attrs = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attrs != null) {
            HttpServletRequest request = attrs.getRequest();
            record.setIpAddress(getClientIp(request));
            record.setUserAgent(request.getHeader("User-Agent"));
            record.setSessionId(request.getSession(false) != null ? request.getSession().getId() : null);
            
            String breakGlass = request.getHeader("X-Break-Glass");
            if ("true".equalsIgnoreCase(breakGlass)) {
                record.setBreakGlassUsed(true);
                record.setReason(request.getHeader("X-Break-Glass-Reason"));
            }
        }
    }

    private void extractArguments(ProceedingJoinPoint joinPoint, AuditRecord record) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String[] parameterNames = signature.getParameterNames();
        Object[] args = joinPoint.getArgs();
        
        try {
            for (int i = 0; i < parameterNames.length; i++) {
                String paramName = parameterNames[i];
                Object arg = args[i];
                if (arg == null) continue;
                
                if (paramName.toLowerCase().contains("patientid")) {
                    record.setPatientId(Long.parseLong(arg.toString()));
                } else if (paramName.toLowerCase().contains("id") && record.getResourceId() == null) {
                    record.setResourceId(arg.toString());
                } else if (arg.getClass().getName().startsWith("com.healthcare.clinic")) {
                    // Try to extract before/after values roughly by serializing the DTO
                    if (record.getAfterValues() == null) {
                        record.setAfterValues(objectMapper.writeValueAsString(arg));
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to extract audit arguments", e);
        }
    }

    private String getClientIp(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
