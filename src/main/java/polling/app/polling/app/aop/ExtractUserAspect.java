package polling.app.polling.app.aop;

import java.util.Arrays;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import polling.app.polling.app.helper.JwtHelper;

@Aspect
@Component
@RequiredArgsConstructor
public class ExtractUserAspect {
    private final JwtHelper jwtHelper;

    @Around("@annotation(ExtractUser)")
    public Object pointcutExtractUser(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ExtractUser extractUser = signature.getMethod().getAnnotation(ExtractUser.class);
        String[] argNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        int idx = Arrays.asList(argNames).indexOf(extractUser.fieldName());
        if (idx == -1) throw new Exception();
        Object[] args = joinPoint.getArgs();
        args[idx] = jwtHelper.getCurrentUser();

        return joinPoint.proceed(args);
    }
}
