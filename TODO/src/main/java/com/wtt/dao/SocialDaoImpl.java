package com.wtt.dao;

import java.util.List;

import javax.persistence.EntityManager;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.wtt.model.Task;
import com.wtt.model.UserBean;

@Repository
@Transactional
public class SocialDaoImpl implements SocialDao {

	@Autowired
	EntityManager env;

	@Override
	public int updatePin(int oldPin, int newPin, long id) {
		Session session = env.unwrap(Session.class);
		UserBean user = session.get(UserBean.class, id);
		if (user.getPin() == newPin && user.getPin() != oldPin) {
			return user.getPin();
		} else {
			user.setPin(newPin);
			session.update(user);
			return user.getPin();
		}
	}

	@Override
	public UserBean loginPin(int pin, String email) {
		Session session = env.unwrap(Session.class);
		Criteria crit = session.createCriteria(UserBean.class);
		crit.add(Restrictions.eq("pin", pin));
		crit.add(Restrictions.eq("email", email));
		UserBean user = (UserBean) crit.uniqueResult();
		return user;
	}

	@Override
	public void saveTask(Task task) {
		Session session = env.unwrap(Session.class);
		session.save(task);
	}

	@Override
	public Boolean updateTask(Task task) {
		Session session = env.unwrap(Session.class);
		boolean flag = false;
		try {
			session.update(task);
			flag = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	@SuppressWarnings({ "deprecation", "unchecked" })
	@Override
	public List<Task> getAllTasks(long userId) {
		Session session = env.unwrap(Session.class);
		List<Task> tasks = null;
		Criteria crit = null;
		try {
			crit = session.createCriteria(Task.class);
			crit.add(Restrictions.eq("userId", userId));
			tasks = crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@SuppressWarnings("deprecation")
	@Override
	public UserBean getProfile(String email) {
		Session session = env.unwrap(Session.class);
		Criteria crit = session.createCriteria(UserBean.class);
		crit.add(Restrictions.eq("email", email));
		return (UserBean) crit.uniqueResult();
	}

	@Override
	public UserBean updateProfile(UserBean userBean) {
		Session session = env.unwrap(Session.class);
		session.update(userBean);
		return userBean;
	}

	@Override
	synchronized public void saveProfile(UserBean user) {
		Session session = null;
		try {

			UserBean existingUser = getProfile(user.getEmail());
			session = env.unwrap(Session.class);
			if (existingUser != null) {
				existingUser.setFirstName(user.getFirstName());
				existingUser.setImage(user.getImage());
				existingUser.setLastName(user.getLastName());
				existingUser.setPin(user.getPin());
				existingUser.setProvider(user.getProvider());
				System.out.println("User already exists with: " + user.getEmail());
				session.update(existingUser);
			} else {
				System.out.println("User does not exist with: " + user.getEmail());
				session.save(user);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public List<Task> getTasksByStatus(String status, long userId) {
		List<Task> tasks = null;
		Session session = null;
		try {
			session = env.unwrap(Session.class);
			Criteria crit = session.createCriteria(Task.class);
			crit.add(Restrictions.eq("userId", userId));
			crit.add(Restrictions.eq("status", status));
			crit.addOrder(Order.desc("createDate"));
			tasks = crit.list();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return tasks;
	}

	@Override
	public Task getTaskById(int taskId, long id) {
		Session session = env.unwrap(Session.class);
		Task task = null;
		Criteria crit = null;
		try {
			crit = session.createCriteria(Task.class);
			crit.add(Restrictions.eq("userId", id));
			crit.add(Restrictions.eq("taskId", taskId));
			task = (Task) crit.uniqueResult();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return task;
	}

	@Override
	public void saveOrUpdateUserProfile(UserBean userBean) {
		Session session = env.unwrap(Session.class);
		session.saveOrUpdate(userBean);
	}

}
