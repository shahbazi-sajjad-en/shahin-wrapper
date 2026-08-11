package biz.espc.shahin.logging;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
/**
 * author: Ebrahim Sheyki
 * Created on: 2/10/2026  10:27 AM
 */
@Aspect
@Component
public class ApiLoggingAspect {

    private static final Logger log = LoggerFactory.getLogger(ApiLoggingAspect.class);

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logApi(ProceedingJoinPoint joinPoint) throws Throwable {
        String method = joinPoint.getSignature().toShortString();
        log.info("API request started: {}", method);

        try {
            Object result = joinPoint.proceed();
            log.info("API request finished: {}", method);
            return result;
        } catch (Exception ex) {
            log.error("API request failed: {}", method, ex);
            throw ex;
        }
    }
}
