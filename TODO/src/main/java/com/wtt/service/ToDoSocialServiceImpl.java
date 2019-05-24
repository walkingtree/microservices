package com.wtt.service;

import java.net.URI;
import java.util.Date;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.wtt.dao.SocialDao;
import com.wtt.model.Task;
import com.wtt.model.UserBean;
import com.wtt.responses.Status;
import com.wtt.util.BinaryFileExtractor;
import com.wtt.util.TodoConstants;

@Service("toDoSocialService")
public class ToDoSocialServiceImpl implements ToDoSocialService {

	@Autowired
	SocialDao socialDaoImpl;
	
	@Autowired
	RestTemplate restTemplate;

	@Override
	public int saveProfile(int pin, UserBean userInfo) {
		userInfo.setPin(pin);
		socialDaoImpl.saveOrUpdateUserProfile(userInfo);
		return userInfo.getPin();
	}

	@Override
	public int updatePin(int oldPin, int newPin, UserBean userProfile) {
		int PIN = 0;
		if (userProfile.getPin() == oldPin && oldPin != newPin) {
			userProfile.setPin(newPin);
			socialDaoImpl.saveOrUpdateUserProfile(userProfile);
			PIN = userProfile.getPin();
		}
		return PIN;
	}

	@Override
	public Status loginWithPin(int pin, UserBean user) {
		Status status = new Status();
		if (user.getPin() > 0 && pin == user.getPin()) {
			status.setEmail(user.getEmail());
			status.setStatus("success");
			return status;
		}
		if (user.getPin() > 0 && pin != user.getPin()) {
			status.setMessage("Please enter valid PIN");
			status.setStatus("success");
			return status;
		} else {
			status.setEmail(user.getEmail());
			status.setStatus("success");
			return status;
		}
	}

	@Override
	public int saveTask(String name, Date date, UserBean userInfo) {
		int taskId = 0;
		try {
			Task task = new Task();
			task.setTaskName(name);
			task.setCreateDate(date);
			task.setStatus(TodoConstants.PENDING);
			task.setParentId(0);
			task.setUserId(userInfo.getId());
			socialDaoImpl.saveTask(task);
			taskId = task.getTaskId();
		} catch (Exception e) {
			System.out.println("Exception Occurred while creating the task: " + e.getMessage());
		}
		return taskId;
	}

	@Override
	public Boolean updateTask(String name, Date date, int taskId, String status, UserBean bean) {
		Task task = null;
		Boolean flag = false;
		try {
			task = socialDaoImpl.getTaskById(taskId, bean.getId());
			if (task != null) {
				task.setCreateDate(date);
				task.setTaskName(name);
				if (status.equalsIgnoreCase(TodoConstants.COMPLETED)) {
					status = TodoConstants.COMPLETED;
				} else if (status.equalsIgnoreCase(TodoConstants.PENDING)) {
					status = TodoConstants.PENDING;
				} else if (status.equalsIgnoreCase(TodoConstants.OVERDUE)) {
					status = TodoConstants.OVERDUE;
				} else {
					status = TodoConstants.RESCHEDULED;
				}
				task.setStatus(status);
				flag = socialDaoImpl.updateTask(task);
			}
		} catch (Exception e) {
			System.out.println("Exception Occurred while updating the task: " + e.getMessage());
		}
		return flag;
	}

	@Override
	public List<Task> getTasks(UserBean userInfo) {
		List<Task> list = null;
		try {
			list = socialDaoImpl.getAllTasks(userInfo.getId());
		} catch (Exception e) {
			System.out.println("Exception Occurred while Retriving tasks: " + e.getMessage());
		}
		return list;
	}

	@Override
	public Boolean deleteTask(int taskId, UserBean userInfo) {
		Boolean flag = false;
		Task task = socialDaoImpl.getTaskById(taskId, userInfo.getId());
		if (task != null && (!task.getStatus().equalsIgnoreCase(TodoConstants.DELETED))) {
			task.setCreateDate(new Date());
			task.setStatus(TodoConstants.DELETED);
			socialDaoImpl.saveTask(task);
			flag = true;
		}
		return flag;
	}

	@Override
	public UserBean updateProfile(UserBean userProfile, String firstName, String lastName, String picture) {
		UserBean bean = null;
		try {
			userProfile.setFirstName(firstName);
			userProfile.setLastName(lastName);
			userProfile.setImage(picture);
			bean = socialDaoImpl.updateProfile(userProfile);
		
		} catch (Exception e) {
			System.out.println("Exception Occurred while Updating the profile: " + e.getMessage());
		}
		return bean;
	}

	@Override
	public List<Task> getTaskByStatus(String status, UserBean userInfo) {
		List<Task> tasksList = null;
		try {
		if (status.equalsIgnoreCase(TodoConstants.COMPLETED)) {
			status = TodoConstants.COMPLETED;
		} else if (status.equalsIgnoreCase(TodoConstants.PENDING)) {
			status = TodoConstants.PENDING;
		} else if (status.equalsIgnoreCase(TodoConstants.OVERDUE)) {
			status = TodoConstants.OVERDUE;
		} else {
			status = TodoConstants.RESCHEDULED;
		}
			tasksList = socialDaoImpl.getTasksByStatus(status, userInfo.getId());
		}catch (Exception e) {
		System.out.println("Exception occurred while retriving status by status: "+e.getMessage());
		}
		return tasksList;
	}

}
