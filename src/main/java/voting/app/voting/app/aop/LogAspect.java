package voting.app.voting.app.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voting.app.voting.app.constant.JwtConstants;
import voting.app.voting.app.helper.JwtHelper;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Aspect
@Component
@RequiredArgsConstructor
public class LogAspect {
    private static final String EMPTY_PLACE_HOLDER = "(empty)";
    private static final String REQUEST_ARG_DELIMITER = ", ";
    private final JwtHelper jwtHelper;

    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        String requestId = UUID.randomUUID().toString();
        log.info("Request received({}): {}", requestId, getRequestInfoMsg(joinPoint));
        Object result;
        long start = System.currentTimeMillis();
        try {
            result = joinPoint.proceed();
        } finally {
            log.info("Request processed({}): {}", requestId, getExecutionTimeMsg(start));
        }
        return result;
    }

    private String getRequestInfoMsg(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        StringBuilder message = new StringBuilder();

        message.append("[")
                .append(request.getMethod())
                .append("] ")
                .append(request.getRequestURI());

        String token = request.getHeader("Authorization");
        String userId = null;
        if (token != null) {
            userId = jwtHelper.getByClaimName(token, JwtConstants.CLAIM_EMAIL);
        }

        message.append(" [Requester] ")
                .append(userId)
                .append(" [Request Body] ");

        String[] argNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        if (argNames.length == 0) {
            message.append(EMPTY_PLACE_HOLDER);
        } else {
            Object[] args = joinPoint.getArgs();
            int index = 0;
            for (String argName : argNames) {
                String argVal = Objects.toString(args[index++]);
                if (argVal.isEmpty()) {
                    argVal = EMPTY_PLACE_HOLDER;
                }
                message.append(argName)
                        .append("=")
                        .append(argVal)
                        .append(REQUEST_ARG_DELIMITER);
            }
        }
        String logMessage = message.toString();
        if (logMessage.endsWith(", ")) {
            logMessage = logMessage.substring(0, logMessage.lastIndexOf(REQUEST_ARG_DELIMITER));
        }
        return logMessage;
    }

    private String getExecutionTimeMsg(long startTime) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        return "[Time] " + elapsedTime + "ms";
    }
}
