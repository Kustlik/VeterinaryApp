package pl.gr.veterinaryapp.service.impl.validator;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import pl.gr.veterinaryapp.exception.ResourceNotFoundException;
import pl.gr.veterinaryapp.model.entity.Client;

public class UserValidator {
    public boolean isUserAuthorized(User user, Client client) {
        boolean isClient = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch("ROLE_CLIENT"::equalsIgnoreCase);
        if (isClient) {
            if (client.getUser() == null) {
                return false;
            } else {
                return client.getUser().getUsername().equalsIgnoreCase(user.getUsername());
            }
        }
        return true;
    }

    public void validateUserAuthorization(User user, Client client) {
        if (!isUserAuthorized(user, client)) {
            throw new ResourceNotFoundException("Wrong id.");
        }
    }
}
