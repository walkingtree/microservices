package com.wtt.dao;

import java.util.List;

import com.wtt.model.Task;
import com.wtt.model.UserBean;

public interface SocialDao {

	public int updatePin(int oldPin, int newPin, long id);

	public UserBean loginPin(int pin,String email);

	public void saveTask(Task task);

	public Boolean updateTask(Task task);

	public List<Task> getAllTasks(long userId);

	public Task getTaskById(int taskId,long id);

	public UserBean getProfile(String email);

	public UserBean updateProfile(UserBean userProfile);

	public void saveProfile(UserBean bean);

	public List<Task> getTasksByStatus(String status,long userId);
	
	public void saveOrUpdateUserProfile(UserBean userBean);


}
