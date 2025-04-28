package net.yyhis.apitester.service;

import java.util.UUID;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import net.yyhis.apitester.dto.UserDto;

@Service("UserService")
public class UserService {
    
    public boolean isOwner(Authentication authentication, Long userId) {
        return true;
    }

    public static UserDto toUserDto(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
      
        return new UserDto(
            UUID.fromString(jwt.getClaimAsString("sub")),
            jwt.getClaimAsString("preferred_username"),
            jwt.getClaimAsString("name"),
            jwt.getClaimAsString("email")
        );
    }
}
