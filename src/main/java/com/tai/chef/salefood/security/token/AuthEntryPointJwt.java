package com.tai.chef.salefood.security.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tai.chef.salefood.dto.MetaDTO;
import com.tai.chef.salefood.dto.ResponseMetaData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.warn("Unauthorized error: {}", authException.getMessage());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(response.SC_UNAUTHORIZED);

        ResponseMetaData responseData = new ResponseMetaData();
        responseData.setMeta(new MetaDTO(HttpStatus.UNAUTHORIZED));

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseData);
    }

}
