package com.wtt.responses;

import java.io.Serializable;
import java.util.List;

import com.wtt.model.Task;
import com.wtt.model.UserBean;

/**
 *
 * @author ManikantaReddy
 */
public class Status implements Serializable{

	private static final long serialVersionUID = -7710119224174330707L;

	private String message;

	private String status;

	private String email;

	private List<Task> tasks;

	private UserBean data;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public void setTasks(List<Task> tasks) {
		this.tasks = tasks;
	}

	public UserBean getData() {
		return data;
	}

	public void setData(UserBean data) {
		this.data = data;
	}

}
