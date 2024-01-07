package voting.app.voting.app.aop;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.CodeSignature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import voting.app.voting.app.constant.JwtConstants;
import voting.app.voting.app.helper.JwtHelper;
import voting.app.voting.app.model.User;
import voting.app.voting.app.repository.UserRepository;

import java.util.Arrays;

@Aspect
@Component
@RequiredArgsConstructor
public class ExtractUserAspect {
    private final JwtHelper jwtHelper;
    private final UserRepository userRepository;

    @Around("@annotation(ExtractUser)")
    public Object pointcutExtractUser(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        ExtractUser extractUser = signature.getMethod().getAnnotation(ExtractUser.class);
        String[] argNames = ((CodeSignature) joinPoint.getSignature()).getParameterNames();

        int idx = Arrays.asList(argNames).indexOf(extractUser.fieldName());
        if (idx == -1)
            throw new Exception();
        Object[] args = joinPoint.getArgs();
        args[idx] = searchOrCreateUser(jwtHelper.getCurrentRequestToken());

        return joinPoint.proceed(args);
    }

    private User searchOrCreateUser(String token) {
        String email = jwtHelper.getByClaimName(token, JwtConstants.CLAIM_EMAIL),
                name = jwtHelper.getByClaimName(token, JwtConstants.CLAIM_NAME);
        return userRepository.findByEmail(email).orElse(User.builder()
                                                .email(email)
                                                .username(name)
                                                .build());
    }
}
