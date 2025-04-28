package net.yyhis.apitester.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import net.yyhis.apitester.ApitesterApplication;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import static net.yyhis.apitester.config.TokenUtil.generateDummyKeycloakToken;

@AutoConfigureMockMvc
@SpringBootTest(classes = ApitesterApplication.class)
public class SecurityConfigTest {
    @Autowired
    private MockMvc mockMvc;

    private Jwt mockJwt = generateDummyKeycloakToken();

    @Test
    @DisplayName("접근가능 페이지 테스트")
    public void testPublicEndpoint() throws Exception {
        mockMvc.perform(get("/v1/api/public"))
               .andExpect(status().isOk()); 
    }

    @Test
    @WithMockUser(username="test",roles={"USER","ADMIN"})
    @DisplayName("인증된 사용자 접근 테스트")
    public void testAuthorizedAccess() throws Exception {
        // 권한 있으면 접근 가능
        mockMvc.perform(get("/v1/api/private"))
               .andExpect(status().isOk()); 
    }

    @Test
    @DisplayName("비인가 사용자 접근 테스트")
    public void testUnauthorizedAccess() throws Exception {
        // 인증 토큰 없이 접근
        mockMvc.perform(get("/v1/user/info"))
               .andExpect(status().isUnauthorized()); 
    }

    @Test
    @DisplayName("USER 권한 접근 테스트")
    public void testUserEndpointWithRole() throws Exception {
        mockMvc.perform( get("/v1/user/info")
            .with(SecurityMockMvcRequestPostProcessors.jwt()
                .jwt(mockJwt)
                .authorities(new SimpleGrantedAuthority("ROLE_USER"))
                )) 
                .andExpect(status().isOk());  
    }

}
