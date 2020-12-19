package KG.Neobis.FMS.Config;

import KG.Neobis.FMS.Entities.Users.AppUsers;
import KG.Neobis.FMS.dto.AuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private AuthenticationManager authenticationManager;
    private JwtOutils jwtOutils;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JwtOutils jwtOutils) {
        this.authenticationManager = authenticationManager;
        this.jwtOutils = jwtOutils;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req,
                                                HttpServletResponse res) throws AuthenticationException {
        try {
            System.out.println("req login = [" + req + "], res = [" + res + "]");
            AppUsers creds = new ObjectMapper()
                    .readValue(req.getInputStream(), AppUsers.class);
            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            creds.getEmail(),
                            creds.getPassword(),
                            null)
            );
        } catch (IOException e) {
            throw new RuntimeException("not authorizer");
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth)
            throws IOException, ServletException {

        String token = jwtOutils.create((AppUsers) auth.getPrincipal());
        ObjectMapper mapper = new ObjectMapper();
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().print(
                mapper.writeValueAsString(new AuthResponse(token)));
    }

}
