package org.home.knowledge.sow.service;

import java.util.List;
import java.util.Optional;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.home.knowledge.sow.model.UserAccount;
import org.home.knowledge.sow.model.UserRole;
import org.home.knowledge.sow.repository.UserAccountRepository;
import org.home.knowledge.sow.repository.UserRoleRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UserAccountService {

        private final UserAccountRepository userAccountRepository;
        private final UserRoleRepository userRoleRepository;
        private final PasswordEncoder passwordEncoder;

        private final String ADMIN_ROLE = "ROLE_ADMIN";
        private final String USER_ROLE = "ROLE_USER";

        private final String SYSTEM_USER = "system";
        private final String ADMIN_USER = "admin";
        private final String GUEST_USER = "guest";

        public UserAccountService(UserAccountRepository userAccountRepository, UserRoleRepository userRoleRepository,
                        PasswordEncoder passwordEncoder) {
                this.userAccountRepository = userAccountRepository;
                this.userRoleRepository = userRoleRepository;
                this.passwordEncoder = passwordEncoder;
        }

        @PostConstruct
        @Transactional
        public void loadRequiredUserDetails() {
                log.debug("Loading require user details (roles and system accounts)");
                // TODO: verify that there is no better method of creating a system user context
                SecurityContext context = SecurityContextHolder.createEmptyContext();
                Authentication systemAuthentication = new PreAuthenticatedAuthenticationToken("system", null);
                systemAuthentication.setAuthenticated(true);
                context.setAuthentication(systemAuthentication);
                SecurityContextHolder.setContext(context);

                var userRoles = findUserRoles();
                var adminRole = userRoles.stream()
                                .filter(role -> role.getName().equals(
                                                ADMIN_ROLE))
                                .findFirst()
                                .orElse(saveUserRole(new UserRole(ADMIN_ROLE)));
                log.debug("Required admin role saved: {}", adminRole);

                var userRole = userRoles.stream()
                                .filter(role -> role.getName().equals(
                                                USER_ROLE))
                                .findFirst()
                                .orElse(saveUserRole(new UserRole(USER_ROLE)));
                log.debug("Required admin role saved: {}", userRole);

                var adminUser = findByUsername(ADMIN_USER).orElse(
                                saveUserAccount(new UserAccount(ADMIN_USER,
                                                passwordEncoder.encode("admin"), List.of(adminRole, userRole))));
                log.debug("Required admin user saved: {}", adminUser);

                var systemUser = findByUsername(SYSTEM_USER).orElse(
                                saveUserAccount(new UserAccount(SYSTEM_USER,
                                                passwordEncoder.encode("system"), List.of(adminRole))));
                log.debug("Required system user saved: {}", systemUser);

                var guestUser = findByUsername(GUEST_USER).orElse(
                                saveUserAccount(new UserAccount(GUEST_USER,
                                                passwordEncoder.encode("guest"), List.of(userRole))));

                // log system user back out
                SecurityContextHolder.setContext(SecurityContextHolder.createEmptyContext());
                log.debug("Required guest user saved: {}", guestUser);
        }

        public Optional<UserAccount> findByUsername(String username) {
                log.info("Finding user account by username: {}", username);
                return userAccountRepository.findByUsername(username);
        }

        public UserAccount saveUserAccount(UserAccount userAccount) {
                log.info("Saving userAccount: {}: ", userAccount);
                return userAccountRepository.save(userAccount);
        }

        public List<UserRole> findUserRoles() {
                log.debug("Retrieving all UserRoles");
                return userRoleRepository.findAll();
        }

        public UserRole saveUserRole(UserRole userRole) {
                log.info("Saving userRole: {}", userRole);
                return userRoleRepository.save(userRole);
        }

}
