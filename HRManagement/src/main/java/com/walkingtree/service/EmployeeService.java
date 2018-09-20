package com.walkingtree.service;

import org.json.simple.JSONObject;

import com.walkingtree.model.Employee;

public interface EmployeeService {
	public JSONObject getEmployeeId(int empId);
	public JSONObject getEmployees(int start, int limit);
	public JSONObject deleteEmployee(int empId);
	public JSONObject saveOrUpdateEmployee(Employee employee);

}
