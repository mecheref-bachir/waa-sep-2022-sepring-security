package springsecuritylab.demospringsecurity.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager){

        this.authenticationManager=authenticationManager;
    }
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
      User user = (User) authResult.getPrincipal();
        Algorithm algorithm=Algorithm.HMAC256("secret".getBytes());
        String access_Token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+10*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .withClaim("roles",user
                        .getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()))
                .sign(algorithm);
        String refresh_Token= JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis()+30*60*1000))
                .withIssuer(request.getRequestURL().toString())
                .sign(algorithm);

        response.setHeader("access_Token",access_Token);
        response.setHeader("refresh_Token",refresh_Token);


    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
       String userName=request.getParameter("userName");
       String password=request.getParameter("password");
       log.info("username is {} ,password is {} ",userName,password);
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken=
                new UsernamePasswordAuthenticationToken(userName,password);
        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);


    }
}
