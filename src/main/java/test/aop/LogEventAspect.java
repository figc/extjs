package test.aop;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import test.model.User;
import test.route.worker.AceEventNotifier;

@Component
@Aspect
public class LogEventAspect {

    private AceEventNotifier notifier;
    
    @Autowired
    public LogEventAspect(AceEventNotifier notifier) {
		this.notifier = notifier;
	}

//	@Around("execution(*test.route.BufferingAceTransporter.transportEvent(..)")
//	@After("execution(*test.route.BufferingAceTransporter.transportEvent(..)")
	@Around("execution(* *(..)) && @annotation(Auditable)")
//	public void foo(JoinPoint joinPoint) throws Throwable {
	public void foo(ProceedingJoinPoint joinPoint) throws Throwable {
		Object[] args = joinPoint.getArgs();
		
		System.out.println(Arrays.toString(args));

		Method method = MethodSignature.class.cast(joinPoint.getSignature()).getMethod();
		
		RequestMapping mapping = method.getAnnotation(RequestMapping.class);
		String[] value = mapping.value();
		
//		if (value != null && value.length > 0) {
//			System.out.println(value[0]);
//		}

		Object o = joinPoint.proceed();
		
		@SuppressWarnings("unchecked")
		Map<String, Object> map = (Map<String, Object>) o;
		User user = (User) map.get("user");
		
		notifier.publishEvent(user);
	}
}
