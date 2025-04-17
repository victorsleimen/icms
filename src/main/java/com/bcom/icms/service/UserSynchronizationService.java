package com.bcom.icms.service;

import com.bcom.icms.domain.User;
import com.bcom.icms.repos.UserRepository;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Service;


/**
 * Synchronize Keycloak users with the database after successful authentication.
 */
@Service
@Slf4j
public class UserSynchronizationService {

    private final UserRepository userRepository;

    public UserSynchronizationService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    private void syncWithDatabase(final Map<String, Object> claims) {
        final String subject = claims.get("sub").toString();
        User user = userRepository.findByLoggedUser(subject);
        if (user == null) {
            log.info("adding new user after successful authentication: {}", subject);
            user = new User();
            user.setLoggedUser(subject);
            // TODO provide data for new users
            user.setUsername("Consectetuer adipiscing.");
            user.setIsUtc(true);
        } else {
            log.debug("updating existing user after successful authentication: {}", subject);
        }
        user.setEmail(((String)claims.get("email")));
        user.setFirstName(((String)claims.get("given_name")));
        user.setLastName(((String)claims.get("family_name")));
        userRepository.save(user);
    }

    @EventListener(AuthenticationSuccessEvent.class)
    public void onAuthenticationSuccessEvent(final AuthenticationSuccessEvent event) {
        if (event.getSource() instanceof JwtAuthenticationToken jwtToken && 
        "BCom".equals(jwtToken.getTokenAttributes().get("azp"))) {
            syncWithDatabase(jwtToken.getTokenAttributes());
        }
    }

}
