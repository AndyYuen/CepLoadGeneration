package com.redhat.cep.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Type;

import com.Ostermiller.util.LabeledCSVParser;

// This is an object creator based on Java Reflection
// It has limited type support and requires the parser file parameter to be named var#1, var#2,...etc.
// Limitations:
//	- There can only be a single constructor for a fact/event class
//	- The parameters of a fact/event constructor can only be of type:
//		- java primitive data types and primitive wrapped classes
//		- classes with a constructor that only takes a single String object as a parameter
//		  (Joda's LocalTime is the only exception which takes an Object as a parameter to the constructor but that
//		  Object is actually a String)
public class ReflectionBasedObjectCreator implements ObjectCreator {
	static final String CLASS_NAME = "className";
	
	static final String JAVA_OBJECT_CLASS = "java.lang.Object";
	static final String JAVA_STRING_CLASS = "java.lang.String";
	static final String JAVA_CHARACTER_CLASS = "java.lang.Character";
	static final String JODA_LOCALTIME_CLASS = "org.joda.time.LocalTime";
	
	private String defaultPackage;
	
	// constructor provides the default package name of the fact/event classes to
	// be instantiated
	public ReflectionBasedObjectCreator(String defaultPackage) {
		this.defaultPackage = defaultPackage;
	}
	
	
	// parse csv file and create an object according to specification using Java reflection
	// class name is specified in the CLASS_NAME column of the csv file
	// the constructor parameters are specified in the csv file columns var#1, var#2, var#3, etc.
	public Object newInstance(LabeledCSVParser parser) throws Exception {
		Object obj = null;
		
		Validators.checkNotNull(parser, CLASS_NAME);
		String className = parser.getValueByLabel(CLASS_NAME);
		// prepend the package name for the fact/event classes
		if (className.indexOf('.') < 0) className = defaultPackage + "." + className;
		
	    Class<?> targetClass = Class.forName(className);
	    Constructor<?>[] allConstructors = targetClass.getDeclaredConstructors();
	    // there should only be one constructor : a requirement to use this creator class
	    // as this method only uses the first constructor returned
	    for (Constructor<?> ctor : allConstructors) {

				//System.out.format("%s%n", ctor.toGenericString());

				Type[] gpType = ctor.getGenericParameterTypes();
				
				// check if parameters are null or of primitive data type
				for (int i = 0; i < gpType.length; i++) {
					//System.out.println(getParamName(i + 1));
					Validators.checkNotNull(parser, getParamName(i + 1));
					if (gpType[i].toString().equals("int") || gpType[i].toString().equals("long")) {
						//System.out.println("validating int for: " + getParamName(i + 1));
						Validators.checkIntegers(parser, getParamName(i + 1));
					}
					else if (gpType[i].toString().equals("boolean")) {
						// check boolean true or false
						Validators.checkBooleans(parser, getParamName(i + 1));
					}
				}
				

/*				for (int j = 0; j < gpType.length; j++) {
					// print parameter type
				    System.out.format("%s[%d]: %s%n", 
					       "ParameterType", j, gpType[j]);
				}*/
				
				// build argument list for constructor
				Object argList[] = new Object[gpType.length];
				Class<?> paramClass;
				Constructor<?> paramClassCtor;
				Class<?> partypes[] = new Class[1];
				for (int i = 0; i < gpType.length; i++) {
					// build parameter objects for class constructor
					if (gpType[i].toString().equals("int") || gpType[i].toString().equals("long")) {
						// handle int/long primitive types
						argList[i] = new Integer(parser.getValueByLabel(getParamName(i + 1)));
					}
					else if (gpType[i].toString().equals("boolean")) {
						// handle boolean primitive types
						argList[i] = new Boolean(parser.getValueByLabel(getParamName(i + 1)));
					}else if (gpType[i].toString().equals("double")) {
						argList[i] = new Double(parser.getValueByLabel(getParamName(i + 1)));
					}else if (gpType[i].toString().equals("float")) {
						argList[i] = new Float(parser.getValueByLabel(getParamName(i + 1)));
					}else if (gpType[i].toString().equals("char") || gpType[i].toString().indexOf(JAVA_CHARACTER_CLASS) > 0) {
						argList[i] = new Character(parser.getValueByLabel(getParamName(i + 1)).charAt(0));
					}else {
						// requires all classes have constructor that takes String as the only parameter
						String name = gpType[i].toString();

						// get rid of unwanted stuff in the class name
						paramClass = Class.forName(name.substring(name.indexOf(' ') + 1));
						if (name.indexOf(JODA_LOCALTIME_CLASS) >= 0) {
							// special case for LocalTime which takes a String but it has to be of type Object!!!
							partypes[0] = Class.forName(JAVA_OBJECT_CLASS);
						}
						else {
							partypes[0] = Class.forName(JAVA_STRING_CLASS);
						}

						// create an object of the appropriate type in the argument list 
						// to be used by the class constructor
						paramClassCtor = paramClass.getConstructor(partypes);
						argList[i] = paramClassCtor.newInstance(parser.getValueByLabel(getParamName(i + 1)));
					}
					 
				}

				// create the object using the argument list
				// this will throw exception if there is a problem
				obj = ctor.newInstance(argList);
				break;

	    }
		return obj;
	}
	
	// return param name in form var#i
	private String getParamName(int i) {
		return "var#" + i;
	}

}
