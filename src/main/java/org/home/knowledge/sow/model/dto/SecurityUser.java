package org.home.knowledge.sow.model.dto;

import java.util.Collection;

import org.home.knowledge.sow.model.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.extern.slf4j.Slf4j;

/**
 * Security user wraps around persisted UserAccount to honor UserDetailService
 * interface
 */
@Slf4j
public class SecurityUser implements UserDetails {

    private final UserAccount userAccount;

    public SecurityUser(UserAccount userAccount) {
        this.userAccount = userAccount;
    }

    @Override
    public String getUsername() {
        return userAccount.getUsername();
    }

    @Override
    public String getPassword() {
        return userAccount.getPassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userAccount.getRoles().stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.getName()))
                .toList();
        // return Arrays.stream(user
        // .getRoles()
        // .split(","))
        // .map(SimpleGrantedAuthority::new)
        // .toList();
    }

    @Override
    public boolean isAccountNonExpired() {
        log.warn("Function hasn't been implemented, returning default value");
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        log.warn("Function hasn't been implemented, returning default value");
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        log.warn("Function hasn't been implemented, returning default value");
        return true;
    }

    @Override
    public boolean isEnabled() {
        log.warn("Function hasn't been implemented, returning default value");
        return true;
    }
}