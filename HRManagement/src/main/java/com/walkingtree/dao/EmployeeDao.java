package com.walkingtree.dao;

import java.util.List;

import javax.inject.Singleton;

import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.jboss.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.orm.hibernate4.support.HibernateDaoSupport;

import com.walkingtree.model.Employee;


@Singleton
public class EmployeeDao extends HibernateDaoSupport {

	 private static final Logger LOGGER = Logger.getLogger(EmployeeDao.class);
	 
	 
	/**
	 * 
	 * @param empId
	 * 
	 * @return
	 */
	public Employee getEmployee(int empId) {

		Session  session     = null;
		Employee employee    = null;

		try {
			session  = getHibernateTemplate().getSessionFactory().openSession();
			return (Employee) session.get(Employee.class, empId);

		} catch (Exception e) {
			LOGGER.error("Failed to get the Employee : " + e.getMessage());

		} finally {
			if (session != null) {
				session.close();
				
			}
		}
		
		return employee;
		
	}

	/**
	 * Get the All Employees 
	 * 
	 * @return
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public List<Employee> getAllEmployees(int start, int limit) {

		Session session                 = null;
		List<Employee> allEmployeesList = null;

		try {

			session          = getHibernateTemplate().getSessionFactory().openSession();
			String query     = " FROM Employee ";
			Query hqlQuery   = session.createQuery(query);
			
			hqlQuery.setFirstResult(start);
			hqlQuery.setMaxResults(limit);
			
			allEmployeesList = hqlQuery.list();

		} catch (Exception e) {
			LOGGER.error("Failed to get the All Employees : " + e.getMessage() );

		} finally {
			if (session != null) {
				session.close();

			}

		}

		return allEmployeesList;
		
	} // getAllEmployees 
	

	/**
	 * Delete specific Employee
	 * 
	 * @param employee
	 * 
	 * @return
	 */
	public Boolean deleteEmployee(Employee employee) {

		Session session = null;
		
		try {
			
			session                               = getHibernateTemplate().getSessionFactory().openSession();
			Transaction transaction               = session.beginTransaction() ; 
			session.delete(employee);
			session.flush();
			transaction.commit();
            return Boolean.TRUE ;
            
		} catch (Exception e) {
			LOGGER.error("Failed to delete the Employee : "+ e.getMessage());
			
		} finally {
			if (session != null) {
				session.close();
				
			}
		}
		
		return Boolean.FALSE ;
		
	} //deleteEmployee

	/**
	 * Add new Employee
	 * 
	 * @param employee
	 * 
	 * @return
	 */
	public int addEmployee(Employee employee) {

		Session session = null;

		try {

			session = getHibernateTemplate().getSessionFactory().openSession();

			if ( employee.getId() == 0 ) { // if Employee is new 
				return saveOrUpdateEmployee(employee);

			} else { // update existing employee 

				Employee existingEmployee = getEmployee( employee.getId() );

				if( existingEmployee != null) {
					
					if ( employee.getName() != null ) { 
						existingEmployee.setName(employee.getName());

					} 

					if ( ((Double)employee.getSalary()).intValue() > 0 ) { 
						existingEmployee.setSalary(employee.getSalary());

					} 
					org.hibernate.Transaction tr = session.beginTransaction();
					session.saveOrUpdate(existingEmployee);
					session.flush();
					tr.commit();
					
					
					
//					return saveOrUpdateEmployee(existingEmployee);

				} else{
					LOGGER.debug("This Employee doesn't exist in the Application, Employee ID : "+employee.getId());
					return -1;

				}

			}

		} catch (Exception e) {
			LOGGER.error("Failed to add/update the Employee : "+ e.getMessage() );

		} finally {
			if (session != null) {
				session.close();

			}

		}

		return employee.getId();
	}
	
	/**
	 * Save or update the employee
	 * 
	 * @param employee
	 * 
	 * @return
	 */
	public int saveOrUpdateEmployee(Employee employee){
		
		Session session           = null;
		int employeeId            = 0;
		Transaction transaction   = null;
		
		try {
			
			session                   = getHibernateTemplate().getSessionFactory().openSession();
			transaction               = session.beginTransaction();
			employeeId                = (Integer) session.save(employee);
			session.flush();
			transaction.commit();	
			
		} catch (Exception e) {
			LOGGER.error("Failed to save/update the Employee in  saveOrUpdateEmployee method : "+ e.getMessage() );
			
		} finally {
			
			if (session != null) {
				session.close();
				
			}
			
		}
		
		return employeeId;	
		
	}
	
	public static EmployeeDao getFromApplicationContext(ApplicationContext ctx) {
		return (EmployeeDao) ctx.getBean("employeeDao");
		
	}

}
