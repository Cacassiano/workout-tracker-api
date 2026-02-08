package dev.cacassiano.workout_tracker.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cacassiano.workout_tracker.errors.custom.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AuthorizationEntryPoint implements AuthenticationEntryPoint{

    @Autowired
    private ObjectMapper mapper;
    private final int STATUS_CODE = 401;
    
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {

        ErrorResponse res = new ErrorResponse(authException.getMessage(), STATUS_CODE);
        response.setStatus(STATUS_CODE);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(res));
    }
    
}