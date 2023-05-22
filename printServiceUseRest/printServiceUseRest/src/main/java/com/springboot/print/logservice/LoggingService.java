package com.springboot.print.logservice;

public class LoggingService {

	Object object;
	String className;
	public LoggingService() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public LoggingService(Object object) {
		super();
		this.object = object;
		this.className = object.toString();
	}

	public void debug(String className, String mssg) {
		System.out.println("DEBUG: CLASSNAME:"+ className + " Message: "+ mssg);
	}
	public void info(String className, String mssg) {
		System.out.println("INFO: CLASSNAME:"+ className + " Message: "+ mssg);
	}
	public void error(String className, String mssg) {
		System.out.println("ERROR: CLASSNAME:"+ className + " Message: "+ mssg);	
	}
	public void debug(String mssg) {
		System.out.println("DEBUG: CLASSNAME:"+ className + " Message: "+ mssg);
	}
	public void info(String mssg) {
		System.out.println("INFO: CLASSNAME:"+ className + " Message: "+ mssg);
	}
	public void error(String mssg) {
		System.out.println("ERROR: CLASSNAME:"+ className + " Message: "+ mssg);	
	}
}
