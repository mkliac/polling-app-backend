package voting.app.voting.app.aop;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import voting.app.voting.app.constant.JwtConstants;
import voting.app.voting.app.helper.JwtHelper;
import voting.app.voting.app.model.User;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class ExtractUserAspect {
    private final JwtHelper jwtHelper;

    @Around("@annotation(ExtractUser)")
    public Object pointcutExtractUser(ProceedingJoinPoint joinPoint) throws Throwable {
        String token = getToken();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ExtractUser extractUser = signature.getMethod().getAnnotation(ExtractUser.class);
        String fieldName = extractUser.fieldName();

        String[] argNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();
        int idx = Arrays.asList(argNames).indexOf(fieldName);

        if (idx == -1)
            throw new Exception();
        Object[] args = joinPoint.getArgs();

        //TODO: 1.when new user firstly enter, save new User to DB
        //      2.search from DB instead here
        args[idx] = new User(jwtHelper.getByClaimName(token, JwtConstants.CLAIM_EMAIL),
                             jwtHelper.getByClaimName(token, JwtConstants.CLAIM_NAME));

        return joinPoint.proceed(args);
    }

    private String getToken() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        return request.getHeader("Authorization");
    }
}
