package io.mykim.projectboard.global.config.security.handler;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private static final String PREFIX_REST_API = "/api";
    private static final String REDIRECT_URL = "/users/sign-in";

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        String requestURI = request.getRequestURI();

        // rest api
        if(requestURI.startsWith(PREFIX_REST_API)) {
            sendErrorResponseApi(response, CustomErrorCode.UN_AUTHORIZED_USER);
        } else {
            // view
            response.sendRedirect(REDIRECT_URL);
        }
    }

    private void sendErrorResponseApi(HttpServletResponse response, CustomErrorCode errorCode) throws HttpMessageNotWritableException, IOException {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;
        CommonResponse result = new CommonResponse(errorCode, REDIRECT_URL);
        if(jsonConverter.canWrite(result.getClass(), jsonMimeType)) {
            jsonConverter.write(result, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }
}