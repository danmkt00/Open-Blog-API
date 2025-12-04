package com.example.openblogapi.open_blog_api.security;


import com.example.openblogapi.open_blog_api.model.User;
import com.example.openblogapi.open_blog_api.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import java.io.IOException;
import java.util.Collections;

@Component
public class JwtAuthenticationFilter extends GenericFilter
{

    private final JwtUtils jwtUtils;
    private final UserRepository userRepo;

    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserRepository userRepo)
    {
        this.jwtUtils = jwtUtils;
        this.userRepo = userRepo;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest http = (HttpServletRequest) request;
        String header = http.getHeader("Authorization");

        if (StringUtils.hasText(header) && header.startsWith("Bearer "))
        {
            String token = header.substring(7);
            if (jwtUtils.isValid(token))
            {
                String email = jwtUtils.getSubject(token);
                User user = userRepo.findByEmail(email).orElse(null);
                if (user != null)
                {
                    Authentication auth = new UsernamePasswordAuthenticationToken(
                            user, null, Collections.emptyList());
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        chain.doFilter(request, response);
    }
}
