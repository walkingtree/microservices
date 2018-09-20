package com.walkingtree;


import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.walkingtree.dao.ImplementationClass;

import io.micronaut.runtime.Micronaut;

public class Application {

	public static void main(String[] args) {
		//
		// Load db configuration file 
		//
		ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath*:**///db-configuration.xml");
		ImplementationClass.setContext(applicationContext);
		Micronaut.run(Application.class);

	}
}