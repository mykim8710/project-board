package io.mykim.projectboard.global.config.security.handler;

import io.mykim.projectboard.global.result.enums.CustomSuccessCode;
import io.mykim.projectboard.global.result.model.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Slf4j
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("Sign-in Success");

        // sign-in 실패 했었을 때, session에 에러 지우기
        clearAuthenticationAttributes(request);

        /**
         * form login 방식
         */
        response.sendRedirect("/");

        /**
         * API login 방식
         */
        /*MappingJackson2HttpMessageConverter jsonConverter = new MappingJackson2HttpMessageConverter();
        MediaType jsonMimeType = MediaType.APPLICATION_JSON;

        HttpStatus result = HttpStatus.OK;
        CommonResponse commonResult = new CommonResponse(CustomSuccessCode.SIGN_IN_OK);

        if (jsonConverter.canWrite(result.getClass(), jsonMimeType)) {
            jsonConverter.write(commonResult, jsonMimeType, new ServletServerHttpResponse(response));
        }*/
    }

    /*
     * Spring Security는 로그인하는 과정에서 로그인이 실패한 경우에 세션에 관련 에러를 저장
     * 로그인이 실패한 상황이 한번이라도 발생했으면 에러가 세션에 저장되어 있을것
     * 세션을 받아와서 WebAttributes.AUTHENTICATION_EXCEPTION 변수에 정의된 이름으로 된 세션 값을 지운다.
     */
    protected void clearAuthenticationAttributes(HttpServletRequest request) {
        HttpSession httpSession = request.getSession(false);

        if (httpSession == null) {
            return;
        }

        httpSession.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
    }
}