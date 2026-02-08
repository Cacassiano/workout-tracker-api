package dev.cacassiano.workout_tracker.infra.security;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import dev.cacassiano.workout_tracker.errors.custom.ErrorResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class AcessDeniedHandler implements AccessDeniedHandler{

    @Autowired
    private ObjectMapper mapper;
    private final int STATUS_CODE = 403;

    @Override
    public void handle(
        HttpServletRequest request, 
        HttpServletResponse response,
        AccessDeniedException accessDeniedException
    ) throws IOException, ServletException {

        ErrorResponse res = new ErrorResponse(accessDeniedException.getMessage(), STATUS_CODE);
        response.setStatus(STATUS_CODE);
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(res));
    }
    
}
