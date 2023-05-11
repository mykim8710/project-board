package io.mykim.projectboard.global.config.security.handler;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

@Slf4j
public class CustomAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        log.error("Sign-in Fail");

        /**
         * form login 방식
         */
        String errorMessage = CustomErrorCode.NOT_VALID_ACCOUNT.getMessage();

//        // 해당계정이 없을때
//        if (exception instanceof UsernameNotFoundException) {
//            errorMessage = CustomErrorCode.NOT_FOUND_USER.getMessage();
//        }
//
//        // 비밀번호가 틀릴때 BadCredentialsException < AuthenticationException < RuntimeException
//        if (exception instanceof BadCredentialsException) {
//            errorMessage = CustomErrorCode.NOT_MATCH_PASSWORD.getMessage();
//        }

        errorMessage= URLEncoder.encode(errorMessage,"UTF-8"); // 한글 인코딩 깨지는 문제방지
        setDefaultFailureUrl("/users/sign-in?error=true&exception=" +errorMessage);
        super.onAuthenticationFailure(request, response, exception);



        /**
         * API login 방식
         */
        // 해당계정이 없을때
        /*if (exception instanceof UsernameNotFoundException) {
            sendErrorResponseApiLogin(response, CustomErrorCode.NOT_FOUND_USER);
        }

        // 비밀번호가 틀릴때 BadCredentialsException < AuthenticationException < RuntimeException
        if (exception instanceof BadCredentialsException) {
            sendErrorResponseApiLogin(response, CustomErrorCode.NOT_MATCH_PASSWORD);
        }*/
    }

    // API login 방식
    private void sendErrorResponseApiLogin(HttpServletResponse response, CustomErrorCode errorCode) throws HttpMessageNotWritableException, IOException {
        MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;
        CommonResponse result = new CommonResponse(errorCode);
        if(jsonConverter.canWrite(result.getClass(), jsonMimeType)) {
            jsonConverter.write(result, jsonMimeType, new ServletServerHttpResponse(response));
        }
    }
}