package net.yyhis.apitester.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomJwtConverter implements Converter<Jwt, Collection<GrantedAuthority>> {
    private static final List<String> EXCLUDED_ROLES = List.of("offline_access", "default-roles-dev-realm", "uma_authorization");

    @Override
	public Collection<GrantedAuthority> convert(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        List<String> roles = (List<String>) realmAccess.get("roles");

        List<String> filteredRoles = roles.stream()
                .filter(role -> !EXCLUDED_ROLES.contains(role)) // 제외할 역할을 필터링
                .collect(Collectors.toList());

		Collection<GrantedAuthority> authorities = filteredRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toList());
                
		return authorities;
	}
}