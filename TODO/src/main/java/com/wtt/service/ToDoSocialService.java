package com.wtt.service;

import java.util.Date;
import java.util.List;

import com.wtt.model.Task;
import com.wtt.model.UserBean;
import com.wtt.responses.Status;

public interface ToDoSocialService {

	public int saveProfile(int pin, UserBean uesrInfo);

	public int updatePin(int oldPin, int newPin, UserBean userProfile);

	public Status loginWithPin(int pin, UserBean bean);

	public int saveTask(String name, Date date, UserBean userInfo);

	public Boolean updateTask(String name, Date date, int taskId, String status, UserBean userInfo);

	public List<Task> getTasks(UserBean userInfo);

	public Boolean deleteTask(int taskId, UserBean userInfo);

	public UserBean updateProfile(UserBean userProfile, String firstName, String lastName,String picture);

	public List<Task> getTaskByStatus(String status,UserBean userInfo);

}
