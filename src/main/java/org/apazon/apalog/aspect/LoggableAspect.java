package org.apazon.apalog.aspect;

import java.lang.reflect.Method;

import org.apazon.apalog.annotation.Log;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Aspect
public class LoggableAspect {

	@Around("@annotation(org.apazon.apalog.annotation.Log)")
	public Object logMethod(ProceedingJoinPoint joinPoint) {
		Log logAnnotation = getLogAnnotation(joinPoint);
		
		Logger logger = null;
		if(logAnnotation.appender().isEmpty()) {
			logger = LoggerFactory.getLogger(joinPoint.getSignature().getClass());
		} else {
			logger = LoggerFactory.getLogger(logAnnotation.appender());
		}

		logger.info("Starting method "+joinPoint.getSignature().getName());
		Object result =null;
		try {
			result = joinPoint.proceed();
			logger.info("End method "+joinPoint.getSignature().getName());
		} catch (Throwable e) {
			logger.error("Error method "+joinPoint.getSignature().getName());
		}
		
		return result;
	}
	 
	private Log getLogAnnotation(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
	    Method method = signature.getMethod();

	    Log myAnnotation = method.getAnnotation(Log.class);
	    
	    return myAnnotation;
	}
	
}
