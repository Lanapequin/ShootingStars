package eci.cvds.tdd.module.sportLoan.config;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Collections;

@Component
public class JwtRequestFilter extends OncePerRequestFilter{
    @Autowired
    private UserDetailsService userDetailsService;

    private final String SECRET = "secret";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Extraemos el token sin "Bearer "

            try {
                var verifier = JWT.require(Algorithm.HMAC256( SECRET)).build();
                var decodedJWT = verifier.verify(token);

                String userId = decodedJWT.getClaim("id").asString();
                String role = decodedJWT.getClaim("role").asString();

                // Aquí puedes crear un objeto de autenticación con los datos extraídos del token
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userId, null, Collections.emptyList());

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Guardamos la autenticación en el contexto de seguridad de Spring
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (JWTVerificationException e) {
                // Token inválido o expirado, no autenticamos
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
