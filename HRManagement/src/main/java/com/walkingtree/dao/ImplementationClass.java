package com.walkingtree.dao;

import org.jboss.logging.Logger;
import org.springframework.context.ApplicationContext;

public class ImplementationClass {
	private static ApplicationContext ctx  = null;
	private static EmployeeDao employeeDao = null;
	private static final Logger LOGGER = Logger.getLogger(ImplementationClass.class);

	public static void setContext(ApplicationContext context) {

		try {
			ctx = context;
			employeeDao = EmployeeDao.getFromApplicationContext(ctx);

		} catch (Exception e) {
			LOGGER.error("Failed to get the EmployeeDao context : "+e.getMessage());

		}

	}

	public static ApplicationContext getContexObject() {
		return ctx;

	}

	public EmployeeDao getEmployeeDao() {
		return employeeDao;

	}
}
