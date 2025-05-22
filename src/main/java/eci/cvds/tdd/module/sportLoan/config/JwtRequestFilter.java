package eci.cvds.tdd.module.sportLoan.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secret;


//    @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
//            throws ServletException, IOException {
//
//        final String authorizationHeader = request.getHeader("Authorization");
//
//        String idUsuario = null;
//        String jwtToken = null;
//
//        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//            jwtToken = authorizationHeader.substring(7);
//            try {
//                // Parseamos el token y obtenemos las claims (datos del JWT)
//                Claims claims = Jwts.parser()
//                        .setSigningKey(secret)
//                        .parseClaimsJws(jwtToken)
//                        .getBody();
//
//                // Extraemos el ID del usuario del token
//                idUsuario = claims.getSubject();
//
//            } catch (JwtException e) {
//                // En caso de error con el token, respondemos con 401 y un mensaje de error
//                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//                response.getWriter().write("Token inválido: " + e.getMessage());
//                return;
//            }
//        }
//
//        if (idUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//            // Cargamos el usuario basado en el ID (aquí puedes cargar más detalles si es necesario)
//            UserDetails userDetails = this.userDetailsService.loadUserByUsername(idUsuario);
//
//            if (userDetails != null) {
//                // Si se cargan los detalles del usuario, creamos un token de autenticación
//                // Esto también incluye los roles del usuario
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
//                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
//
//                // Establecemos la autenticación en el contexto de seguridad
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//
//        // Continuamos con el siguiente filtro en la cadena
//        chain.doFilter(request, response);
//    }
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        String idUsuario = null;
        String jwtToken = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwtToken = authorizationHeader.substring(7);
            try {
                // Parseamos el token y obtenemos las claims
                Claims claims = Jwts.parser()
                        .setSigningKey(secret)
                        .parseClaimsJws(jwtToken)
                        .getBody();

                // Extraemos el ID del usuario (sub) y el rol del token
                idUsuario = claims.getSubject();
                String rol = claims.get("rol", String.class);

                // Si no hay autenticación actual, la establecemos
                if (idUsuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    UserDetails userDetails = this.userDetailsService.loadUserByUsername(idUsuario);

                    if (userDetails != null) {
                        // Creamos los authorities desde el rol del token
                        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(rol));

                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(userDetails, null, authorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }
                }

            } catch (ExpiredJwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expirado");
                return;
            } catch (JwtException e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token inválido");
                return;
            }
        }

        // Continuamos con la cadena de filtros
        chain.doFilter(request, response);
    }

}
