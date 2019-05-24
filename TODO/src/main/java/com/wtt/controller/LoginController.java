package com.wtt.controller;

import io.swagger.annotations.Api;

import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.wtt.model.Task;
import com.wtt.model.UserBean;
import com.wtt.responses.Status;
import com.wtt.service.ToDoSocialService;
import com.wtt.util.TokenValidation;

@Api(tags = "ToDo-Social", description = "ToDo-Service APIs details")
@RestController
@RequestMapping("/todo")
public class LoginController {

	enum Response {
		SUCCESS, FAILED
	}

	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	@Autowired
	ToDoSocialService toDoSocialService;

	@Autowired
	TokenValidation validation;

	@Autowired
	RestTemplate restTemplate;

	@RequestMapping(value = "/pin", method = RequestMethod.POST)
	public Status savePin(@RequestParam(value = "pin", required = true) int pin, @RequestHeader(value = "Authorization", required = true) String token)
					throws Exception {
		int pinId = 0;
		Status status = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			pinId = toDoSocialService.saveProfile(pin, userProfile);
			if (pinId > 0) {
				status.setMessage("Pin created successfully");
				status.setStatus(Response.SUCCESS.toString().toLowerCase());
			} else {
				status.setMessage("Pin creation is failed,please try again");
				status.setStatus(Response.FAILED.toString().toLowerCase());
			}
		}
		return status;
	}

	@PostMapping(value = "/updatePin")
	public Status updatePin(@RequestParam(value = "oldPin", required = true) int oldPin, @RequestParam(value = "newPin", required = true) int newPin,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status status = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			int pinId = toDoSocialService.updatePin(oldPin, newPin, userProfile);
			if (pinId == newPin) {
				status.setMessage("Pin updated successfully");
				status.setStatus(Response.SUCCESS.toString().toLowerCase());
			} else {
				status.setMessage("Pin updation failed, please try again");
				status.setStatus(Response.FAILED.toString().toLowerCase());
			}
		}
		return status;
	}

	@PostMapping(value = "/login")
	public Status loginWithPin(@RequestParam(value = "pin", required = false, defaultValue = "0") int pin,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status status = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			status = toDoSocialService.loginWithPin(pin, userProfile);
		}
		return status;
	}

	@PostMapping(value = "/tasks")
	public Status saveTask(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "date", required = true) String date,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status resp = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			int taskId = toDoSocialService.saveTask(name, sdf.parse(date), userProfile);
			if (taskId > 0) {
				resp.setMessage("Task saved successfully");
				resp.setStatus(Response.SUCCESS.toString().toLowerCase());
			} else {
				resp.setMessage("Faild to saving the task,please try again");
				resp.setStatus(Response.FAILED.toString().toLowerCase());
			}
		}
		return resp;
	}

	@RequestMapping(value = "/tasks/{id}", method = RequestMethod.POST)
	public Status updateTask(@RequestParam(value = "name", required = true) String name, @RequestParam(value = "date", required = true) String date,
					@PathVariable(value = "id", required = true) int taskId, @RequestParam(value = "status", defaultValue = "Pending") String status,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status resp = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			boolean flag = toDoSocialService.updateTask(name, sdf.parse(date), taskId, status, userProfile);
			if (flag) {
				resp.setMessage("Task updated successfully");
				resp.setStatus(Response.SUCCESS.toString().toLowerCase());
			} else {
				resp.setMessage("Task updation failed, please try again later");
				resp.setStatus(Response.FAILED.toString().toLowerCase());
			}
		}
		return resp;
	}

	@RequestMapping(value = "/tasks", method = RequestMethod.GET)
	public Status getTask(@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		List<Task> tasksList = null;
		Status res = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			tasksList = toDoSocialService.getTasks(userProfile);
			if (tasksList != null) {
				res.setTasks(tasksList);
				res.setStatus(Response.SUCCESS.toString().toLowerCase());
			} else {
				res.setTasks(tasksList);
				res.setStatus(Response.FAILED.toString().toLowerCase());
			}
		}
		return res;
	}

	@RequestMapping(value = "/tasks/{id}", method = RequestMethod.DELETE)
	public Status deleteTask(@PathVariable(value = "id", required = true) int taskId,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status status = new Status();
		Boolean flag = false;
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			flag = toDoSocialService.deleteTask(taskId, userProfile);
		}
		if (flag == true) {
			status.setMessage("Task deleted successfully");
			status.setStatus(Response.SUCCESS.toString().toLowerCase());
		} else {
			status.setMessage("Task not deleted, please try again");
			status.setStatus(Response.FAILED.toString().toLowerCase());
		}
		return status;
	}

	@GetMapping(value = "/profile")
	public Status getProfile(@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		Status profileRes = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		profileRes.setData(userProfile);
		profileRes.setStatus(Response.SUCCESS.toString().toLowerCase());
		return profileRes;
	}

	@PostMapping(value = "/profile")
	public Status updateProfile(@RequestHeader(value = "Authorization", required = true) String token, @RequestBody String payload) throws Exception {
		JSONObject request = null;
		UserBean updateUserInfo = null;
		String firstName = "";
		String lastName = "";
		String picture = "";
		Status status = new Status();
		try {
			request = new JSONObject(payload);
		} catch (Exception e) {
			System.out.println("Exception occurred while parsing the request: " + e.getMessage());
		}
		if (request != null && !request.equals("")) {
			if (request.getString("firstName") != null && !request.getString("firstName").isEmpty()) {
				firstName = request.getString("firstName");
			}
			if (request.getString("lastName") != null && !request.getString("lastName").isEmpty()) {
				lastName = request.getString("lastName");
			}
			if (request.getString("picture") != null && !request.getString("picture").isEmpty()) {
				picture = request.getString("picture");
			}
		}
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			updateUserInfo = toDoSocialService.updateProfile(userProfile, firstName, lastName, picture);
		}
		if (updateUserInfo != null) {
			status.setMessage("Profile updated successfully");
			status.setStatus(Response.SUCCESS.toString().toLowerCase());
		} else {
			status.setMessage("Profile updation failed, please try again");
			status.setStatus(Response.FAILED.toString().toLowerCase());
		}
		return status;
	}

	@GetMapping(value = "/getTasksByStatus")
	public Status getTaskByStatus(@RequestParam(value = "status", required = true) String status,
					@RequestHeader(value = "Authorization", required = true) String token) throws Exception {
		List<Task> tasksList = null;
		Status res = new Status();
		UserBean userProfile = validation.validateAccessToken(token);
		if (userProfile != null) {
			tasksList = toDoSocialService.getTaskByStatus(status, userProfile);
			res.setTasks(tasksList);
			res.setStatus(Response.SUCCESS.toString().toLowerCase());
		} else {
			res.setTasks(tasksList);
			res.setStatus(Response.FAILED.toString().toLowerCase());
		}
		return res;
	}

}
