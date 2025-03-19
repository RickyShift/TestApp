package gpi.scrum.config;

import gpi.scrum.service.UserDetailsServiceImp;
import gpi.scrum.service.JwtService;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsServiceImp userDetailsServiceImp;

    @Override
    protected void doFilterInternal(
            @SuppressWarnings("null") @Nonnull HttpServletRequest request,
            @SuppressWarnings("null") @Nonnull HttpServletResponse response,
            @SuppressWarnings("null") @Nonnull FilterChain filterChain
    ) throws ServletException, IOException {

        // Obtém o cabeçalho de autorização da requisição HTTP
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) {
            // Se o cabeçalho não for inválido, contínua com o próximo filtro na cadeia
            filterChain.doFilter(request, response);
            return;
        }

        // Extrai o token JWT do cabeçalho de autorização (omite os primeiros 7 caracteres "Bearer ")
        String token = authHeader.substring(7);
        String username = jwtService.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsServiceImp.loadUserByUsername(username);

            if(jwtService.isValid(token, userDetails)) {
                // Cria um token de autenticação com os detalhes do usuário e as autoridades
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities());
                // Define os detalhes da autenticação a partir da requisição HTTP
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Define a autenticação no contexto de segurança
                SecurityContextHolder.getContext().setAuthentication(authToken);            }

        }
        // Continua com o próximo filtro na cadeia
        filterChain.doFilter(request, response);
    }
}
