package com.walkingtree.controller;

import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import javax.inject.Inject;
import javax.validation.constraints.Size;

import org.json.simple.JSONObject;

import com.walkingtree.model.Employee;
import com.walkingtree.service.EmployeeService;

@Controller("/employee")
public class EmployeeController {

	@Inject
	EmployeeService employeeService;

	@Get("/")
	public String index() {
		return "Hello world";
	}

	/**
	 * Add Employee service end point 
	 * 
	 * @param employee
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Post("/addEmployee")
	public JSONObject addEmployee(@Size(max = 1024) @Body Employee employee) {
		return 	employeeService.saveOrUpdateEmployee(employee);

	}

	/**
	 * Get All Employees Service End point
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Get("/getEmployees")
	public JSONObject getEmployees(@QueryValue("start") Integer start,@QueryValue("limit") Integer limit) {		
		JSONObject jsonObject = employeeService.getEmployees(start, limit);
		return jsonObject;

	}

	/**
	 * Update Employee details service End point 
	 * 
	 * @param text
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Post("/updateEmployee")
	public JSONObject saveOrUpdateEmployee( @Size(max = 1024) @Body Employee text ) {
		return employeeService.saveOrUpdateEmployee(text);

	}

	/**
	 * Get Single Employee service End point 
	 * 
	 * @param id
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Get("/getEmployee")
	JSONObject getEmployee(@QueryValue("id") Integer id) {
		return employeeService.getEmployeeId(id);

	}

	/**
	 * Delete specific Employee service End point
	 * 
	 * @param id
	 * 
	 * @return
	 */
	@Produces(MediaType.APPLICATION_JSON)
	@Get("/deleteEmployee")
	JSONObject deleteEmployee(@QueryValue("id") Integer id) {
		return employeeService.deleteEmployee(id);

	}


}