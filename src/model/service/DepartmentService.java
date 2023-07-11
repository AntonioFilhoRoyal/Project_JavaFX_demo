package model.service;

import java.util.ArrayList;
import java.util.List;

import model.dao.DaoFactory;
import model.dao.DepartmentDAO;
import model.entites.Department;

public class DepartmentService {
	
	private DepartmentDAO departmentDao = DaoFactory.createDepartmentDao();
	
	public List<Department> findAll(){
		return departmentDao.findAll();
	}
}
