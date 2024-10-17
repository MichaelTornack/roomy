package com.nerdware.roomy.framework;

import com.nerdware.roomy.domain.entities.User;
import com.nerdware.roomy.domain.entities.UserRole;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUser.SecurityContextFactory.class)
public @interface WithMockCustomUser {
    String email() default "default_admin@gmail.com";

    class SecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser>
    {
        private Map<String, User> users = new HashMap<>();

        public SecurityContextFactory()
        {

            var defaultAdmin = new User();
            defaultAdmin.setId(1);
            defaultAdmin.setRole(UserRole.Admin);
            defaultAdmin.setFullName("Default Admin");
            defaultAdmin.setEmail("default_admin@gmail.com");

            var employee = new User();
            employee.setId(2);
            employee.setRole(UserRole.Employee);
            employee.setFullName("Normal Employee");
            employee.setEmail("normal.employee@gmail.com");

            users.put(defaultAdmin.getEmail(), defaultAdmin);
            users.put(employee.getEmail(), employee);
        }

        @Override
        public SecurityContext createSecurityContext(WithMockCustomUser withUser)
        {
            SecurityContext context = SecurityContextHolder.createEmptyContext();

            User user = users.get(withUser.email());
            if (user == null) {
                throw new UsernameNotFoundException("User not found: " + withUser.email());
            }

            Authentication auth =
                new UsernamePasswordAuthenticationToken(user, "password", new ArrayList<>());
            context.setAuthentication(auth);
            return context;
        }
    }
}