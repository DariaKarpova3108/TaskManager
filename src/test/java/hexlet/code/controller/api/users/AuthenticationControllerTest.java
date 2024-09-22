package hexlet.code.controller.api.users;

import com.fasterxml.jackson.databind.ObjectMapper;
import hexlet.code.dto.AuthRequest;
import hexlet.code.utils.JWTUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private JWTUtils jwtUtils;
    @MockBean
    private AuthenticationManager authenticationManager;

    @Test
    public void shouldReturnTokenWhenLoginIsSuccessful() throws Exception {
        AuthRequest personData = new AuthRequest();
        personData.setUsername("hexlet@example.com");
        personData.setPassword("qwerty");

        var token = "mocked-jwt-token";

        Mockito.when(jwtUtils.generateToken(personData.getUsername())).thenReturn(token);
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken(personData.getUsername(),
                        personData.getPassword()));

        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personData));
        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    public void shouldReturnTokenWhenLoginIsFail() throws Exception {
        AuthRequest personData = new AuthRequest();
        personData.setUsername("hexlet@example.com");
        personData.setPassword("wrong-password");

        Mockito.doThrow(new BadCredentialsException("Invalid username or password"))
                .when(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));

        var request = post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(personData));
        mockMvc.perform(request)
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid username or password"));
    }
}
