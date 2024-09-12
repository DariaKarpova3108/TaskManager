package hexlet.code.controller.api;

import hexlet.code.dto.AuthRequest;
import hexlet.code.service.CustomUserDetailsService;
import hexlet.code.utils.JWTUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class AuthenticationController {

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/login")
    public String create(@Valid @RequestBody AuthRequest authRequest) {
        var athentication = new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword());
        authenticationManager.authenticate(athentication);
        return jwtUtils.generateToken(authRequest.getUsername());
    }
}
