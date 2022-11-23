package com.cs.tesis.trace.aspect;

import java.util.LinkedList;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;





@Aspect
public class TraceAspect {

	@Pointcut("execution(* *(..)) && !within(com.cs.tesis.trace..*)")
	public void traceMetodos() {
	}

	@Around("traceMetodos()")
	public Object around(ProceedingJoinPoint pjp) throws Throwable {
		System.out.println("a");
		LinkedList log = new LinkedList();

		String thisID = "";
		if (pjp.getThis() != null) {
			thisID = pjp.getThis().toString();
		} else {
			thisID = getStaticClassName(pjp.getSourceLocation().toString());
		}

		String targetId = "-1";
		if (pjp.getTarget() != null)
			targetId = pjp.getTarget().toString();

		log.add("<statment xsi:type=\"trace_Metamodel:MethodCall\" target=\"" + ObjectId.getObjectId().add(targetId)
				+ "\" caller=\"" + ObjectId.getObjectId().add(thisID) + "\" actualParameters=\""
				+ getActualParameters(pjp.getArgs()) + "\" name=\"" + getMethodName(pjp.toString()) + "\">");
		log.add("<subtrace name=\"" + getMethodName(pjp.toString()) + "_trace\">");

		MethodSignature signature = (MethodSignature) pjp.getStaticPart().getSignature();

		log.addAll(getFormalParameters(pjp.getArgs(), signature.getParameterNames()));

		Logger.getLoggingClient().instrument(log);
		try {
			return pjp.proceed();
		} finally {

		}

	}

	@AfterReturning(pointcut = "execution(* *(..)) && !within(com.cs.tesis.trace..*)", returning = "o")
	public void afterReturning(Object o) {
		LinkedList log = new LinkedList();

		log.add("<statement xsi:type=\"trace_Metamodel:Return\" returnedObject=\"//"
				+ (o != null ? ObjectId.getObjectId().add(o.toString()) : "null") + "\"/>");
		log.add("</subtrace>");
		log.add("</statement>");

		Logger.getLoggingClient().instrument(log);
	}

	@After("execution(public static void main(..)) && !within(com.cs.tesis.trace..*)")
	public void afterMain() {
		LinkedList log = new LinkedList();

		log.addAll(ObjectId.getObjectId().log());

		log.add("</trace_Metamodel:ExecutionTrace>");
		Logger.getLoggingClient().instrument(log);
	}

	private static String getLineNumber(String s) {
		return s.substring(s.indexOf(":") + 1);
	}

	private static String getFileName(String s) {
		return s.substring(0, s.indexOf(":"));
	}

	private static String getStaticClassName(String s) {
		s = s.substring(0, s.indexOf("."));
		return s + "_static";
	}

	private static String getBindToClassName(String s) {
		if (s.contains("@")) {
			s = s.substring(0, s.indexOf("@"));
		}
		return s;
	}

	public static String getClassNameWithoutPackage(String s) {
		if (s.contains(".")) {
			s = s.substring(s.lastIndexOf(".") + 1);
		}
		return s;
	}

	private static String getStaticBindToClassName(String s) {
		return s.substring(s.indexOf(" ") + 1, s.lastIndexOf("."));
	}

	private static String getNewBindToClassName(String s) {
		return s.substring(s.indexOf("(") + 1, s.lastIndexOf("("));
	}

	private static String getMethodSignature(String s) {
		return s.substring(s.indexOf("(") + 1, s.lastIndexOf(")"));
	}

	private static String getStaticLifelineName(String s) {
		if (s.contains("call(")) {
			s = s.substring(5, s.length());
		}
		if (s.indexOf("(") >= 0) {
			s = s.substring(0, s.indexOf("("));
		}
		if (s.indexOf("@") >= 0) {
			s = s.substring(0, s.indexOf("@"));
		}
		if (s.indexOf(".") >= 0) {
			s = s.substring(s.indexOf(".") + 1, s.length());
		}
		return getStaticClassName(s);
	}

	private static String getMethodName(String s) {
		return s.substring(s.lastIndexOf(".") + 1, s.lastIndexOf("("));
	}

	private static String getActualParameters(Object[] args) {
		String log = new String();
		for (int i = 0; i < args.length; ++i) {
			log = log.concat(" //" + ObjectId.getObjectId().add(args[i].toString()));
		}
		return log.trim();
	}

	private static LinkedList getFormalParameters(Object[] args, String[] names) {
		LinkedList log = new LinkedList();
		for (int i = 0; i < args.length; ++i) {
			log.add("<statement xsi:type=\"trace_Metamodel:FormalParameter\" allocatedObject=\"//"
					+ ObjectId.getObjectId().add(args[i].toString()) + "\" name=\"" + names[i] + "\"/>");
		}
		return log;
	}

}
