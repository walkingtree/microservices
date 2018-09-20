package com.walkingtree.serviceimpl;

import java.util.List;
import javax.inject.Singleton;
import org.jboss.logging.Logger;
import org.json.simple.JSONObject;
import org.springframework.context.ApplicationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.walkingtree.dao.EmployeeDao;
import com.walkingtree.dao.ImplementationClass;
import com.walkingtree.model.Employee;
import com.walkingtree.service.EmployeeService;

@Singleton
public class EmployeeServiceImpl implements EmployeeService {

	static ObjectMapper mapper = new ObjectMapper();
	EmployeeDao employeeDao    = new ImplementationClass().getEmployeeDao();
	private static final Logger LOGGER = Logger.getLogger(EmployeeServiceImpl.class);
	public static final java.lang.String MESSAGE = "message";
	public static final java.lang.String STATUS  = "status";
	public static final java.lang.String ERROR   = "error";
	public static final java.lang.String SUCCESS = "success";
	
	@SuppressWarnings("unchecked")
	public JSONObject getEmployeeId(int empId) {
		
		JSONObject responseObj = new JSONObject();

		try {
			
			Employee employee = employeeDao.getEmployee(empId);

			if ( employee != null) {
				responseObj.put("Data", employee);

			} else { 
				responseObj.put(EmployeeServiceImpl.MESSAGE, "Requested Employee doesn't exist in System.");

			}
			
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.SUCCESS);
			
		} catch (Exception e) {
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.ERROR);
			responseObj.put(EmployeeServiceImpl.MESSAGE, " Exception occurred while getting the Employee : " + e.getMessage());
			LOGGER.error("Exception occurred while getting the Employee : "+e.getMessage());
			
		}
		
		return responseObj;
		
	} //getEmployeeId

	@SuppressWarnings("unchecked")
	public JSONObject getEmployees(int start, int limit) {
		JSONObject responseObj = new JSONObject();

		try {

			List<Employee> employees = employeeDao.getAllEmployees(start, limit);

			if ( employees != null) {
				responseObj.put("Data", employees);

			} else { 
				responseObj.put(EmployeeServiceImpl.MESSAGE, "There are no employees exist in System.");

			}
			
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.SUCCESS);

		} catch (Exception e) {
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.ERROR);
			responseObj.put(EmployeeServiceImpl.MESSAGE, "Exception occurred while getting the Employees : "+e.getMessage());
			LOGGER.error("Exception occurred while getting the Employees : "+e.getMessage());
		}

		return responseObj;

	} //getEmployees
	

	@SuppressWarnings("unchecked")
	public JSONObject deleteEmployee(int empId) {

		JSONObject responseObj = new JSONObject();
		Boolean isEmpDeleted   = Boolean.FALSE ;

		try {

			Employee employee = employeeDao.getEmployee(empId);
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.SUCCESS);

			if ( employee != null ) {

				isEmpDeleted = employeeDao.deleteEmployee(employee);

				if ( isEmpDeleted ) {
					responseObj.put(EmployeeServiceImpl.MESSAGE, "Employee has been deleted successfully");
					responseObj.put("empId", empId);

				} else {
					responseObj.put(EmployeeServiceImpl.MESSAGE, "Failed to delete the Employee ");
					return responseObj;

				}

			} else {
				responseObj.put(EmployeeServiceImpl.MESSAGE, "Requested Employee doesn't exist in the System. Employee ID : "+empId);

			}

		} catch (Exception e) {
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.ERROR);
			responseObj.put(EmployeeServiceImpl.MESSAGE, "Exception occurred while deleting the Employee : "+e.getMessage());
			LOGGER.error("Exception occurred while deleting the Employee : "+e.getMessage());

		}

		return responseObj;
		
	} // deleteEmployee

	@SuppressWarnings("unchecked")
	public JSONObject saveOrUpdateEmployee(Employee employee) {

		JSONObject responseObj = new JSONObject();
		int beforeSaveEmpId    = employee.getId();

		try {

			int afterSaveEmpId = employeeDao.addEmployee(employee);

			if ( beforeSaveEmpId == 0) {
				responseObj.put("EmpId", afterSaveEmpId);
				responseObj.put(EmployeeServiceImpl.MESSAGE, "Employee created successfully");

			} else if (afterSaveEmpId > 0) {
				responseObj.put("EmpId", afterSaveEmpId);
				responseObj.put(EmployeeServiceImpl.MESSAGE, "Employee updated successfully");

			} else {
				responseObj.put(EmployeeServiceImpl.MESSAGE, "Requested employee doesn't exist in the System.");

			}

		} catch (Exception e) {
			responseObj.put(EmployeeServiceImpl.STATUS, EmployeeServiceImpl.ERROR);
			responseObj.put(EmployeeServiceImpl.MESSAGE, " Exception occurred while adding/updating the Employee : " + e.getMessage());
			LOGGER.error(" Exception occurred while adding/updating the Employee : " + e.getMessage() );

		}

		return responseObj;
		
	} //saveOrUpdateEmployee

	public static EmployeeDao getFromApplicationContext(ApplicationContext ctx) {
		return (EmployeeDao) ctx.getBean("employeeDao");
		
	}
}
