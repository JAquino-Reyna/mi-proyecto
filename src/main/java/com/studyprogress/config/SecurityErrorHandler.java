package com.studyprogress.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.studyprogress.dto.ErrorResponseDTO;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SecurityErrorHandler {
    private final ObjectMapper mapper;
    public void write(HttpServletRequest request, HttpServletResponse response, int status) throws IOException {
        response.setStatus(status);
        response.setContentType("application/json");
        mapper.writeValue(response.getOutputStream(), new ErrorResponseDTO(Instant.now(), status,
                status == 401 ? "Unauthorized" : "Forbidden", status == 401 ? "Autenticación requerida o token inválido" : "Permiso denegado",
                request.getRequestURI(), Map.of()));
    }
}
