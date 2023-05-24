package io.mykim.projectboard.global.admin.aop;

import io.mykim.projectboard.global.result.enums.CustomErrorCode;
import io.mykim.projectboard.global.result.exception.NotAllowAccessException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@Component
@Aspect
public class PortCheckAspect {
    @Before("@annotation(io.mykim.projectboard.global.admin.aop.PortCheck)")
    public void checkPort(JoinPoint joinPoint) throws Throwable {
        log.info("[LOG] {}", joinPoint.getSignature());
        Object[] args = joinPoint.getArgs();

        HttpServletRequest request = (HttpServletRequest) args[0];

        // todo : 외부에서 해당 이 프로젝트 내의 api를 호출할 때 호출을 허락할지에 대한 검증 고민 필요
        if(request.getRemotePort() != 9090) {
            log.error("api에 접근할 수 없는 포트번호 입니다. port : {}", request.getRemotePort());
            throw new NotAllowAccessException(CustomErrorCode.NOT_ALLOW_ACCESS);
        }
    }
}
