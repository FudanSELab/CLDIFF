package edu.fdu.se.astdiff.generatingactions;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class JdtMethodCall {
	private String invoker;
	private String methodName;
	private String returnType;
	private String declaringClass;
	private boolean isJdk;
	private List<String> parameters;
	
	public JdtMethodCall(String invoker, String methodName, String returnType, String declaringClass){
		this.invoker = invoker;
		this.methodName = methodName;
		this.returnType = returnType;
		this.declaringClass = declaringClass;
		parameters = new ArrayList<>();
	}
	
	
	public boolean isJdk() {
		return isJdk;
	}


	public void setJdk(boolean isJdk) {
		this.isJdk = isJdk;
	}


	public String getDeclaringClass() {
		return declaringClass;
	}
	
	public void setDeclaringClass(String declaringClass) {
		this.declaringClass = declaringClass;
	}


	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public String getInvoker() {
		return invoker;
	}
	public void setInvoker(String invoker) {
		this.invoker = invoker;
	}
	public String getMethodName() {
		return methodName;
	}
	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}
	public List<String> getParameters() {
		return parameters;
	}
	public void addParameter(String parameter) {
		this.parameters.add(parameter);
	}
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append(invoker);
		builder.append(".");
		builder.append(methodName);
		builder.append(parameters.stream().collect(Collectors.joining(",", "(", ")")));
		return builder.toString();
	}
}
