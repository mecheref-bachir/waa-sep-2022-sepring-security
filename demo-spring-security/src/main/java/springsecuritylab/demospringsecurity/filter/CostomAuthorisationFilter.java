package springsecuritylab.demospringsecurity.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static java.util.Arrays.stream;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

@Slf4j
public class CostomAuthorisationFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if(request.getServletPath().equals("/login")){

            filterChain.doFilter(request,response);
        }else{
 String auth=request.getHeader(AUTHORIZATION);
 if(auth!=null&&auth.startsWith("Bearer ")){
try {
    String token = auth.substring("Bearer ".length());
    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
    JWTVerifier verifier = JWT.require(algorithm).build();
    DecodedJWT decodedJWT = verifier.verify(token);
    String username = decodedJWT.getSubject();
    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
    Collection<SimpleGrantedAuthority> authoroties = new ArrayList<>();
    stream(roles).forEach(role -> {
        authoroties.add(new SimpleGrantedAuthority(role));
        UsernamePasswordAuthenticationToken AuthenticationToken =
                new UsernamePasswordAuthenticationToken(username, null, authoroties);
        SecurityContextHolder.getContext().setAuthentication(AuthenticationToken);
        try {
            filterChain.doFilter(request, response);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }

    });
}catch(Exception exception){
log.error("error log in: {}",exception.getMessage());
response.setHeader("error",exception.getMessage());
response.sendError(FORBIDDEN.value());
     }
 }else{
     filterChain.doFilter(request,response);
 }

        }
    }
}
