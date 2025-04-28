package net.yyhis.apitester.dto;

import java.util.UUID;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserDto (
    @NotNull UUID id,
    @NotNull String username,
    @NotNull String name,
    @NotNull @Email String email
){

}
