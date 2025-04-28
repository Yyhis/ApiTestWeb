package net.yyhis.apitester.service;

import static org.junit.jupiter.api.Assertions.*;
import java.util.Collections;

import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.Authentication;

import net.yyhis.apitester.dto.UserDto;
import static net.yyhis.apitester.config.TokenUtil.generateDummyKeycloakToken;

public class UserServiceTest {
    private Jwt mockJwt = generateDummyKeycloakToken();

    @Test
    @DisplayName("UserService의 toUserDto 테스트")
    void testToUserDto() {
        Authentication authentication = new JwtAuthenticationToken(mockJwt, Collections.emptyList());
       
        UserDto userDto = UserService.toUserDto(authentication);

        // Verify the result
        assertNotNull(userDto.username());
        assertNotNull(userDto.id());
    }
}
