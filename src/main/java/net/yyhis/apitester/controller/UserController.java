package net.yyhis.apitester.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.yyhis.apitester.dto.UserDto;

import static net.yyhis.apitester.service.UserService.toUserDto;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/info")
    public UserDto getUserInfo(Authentication authentication) {
        return toUserDto(authentication);
    }
}
