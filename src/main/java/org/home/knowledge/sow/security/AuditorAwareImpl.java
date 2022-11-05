package org.home.knowledge.sow.security;

import java.util.Optional;

import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        // @formatter:off
        return Optional.ofNullable(SecurityContextHolder.getContext())
                .map(context -> context.getAuthentication().getName());
                // .map(SecurityContext::getAuthentication)
                // .filter(Authentication::isAuthenticated)
                // .map(Authentication::getPrincipal)
                // .map(User.class::cast);
        // @formatter:on
    }

}
