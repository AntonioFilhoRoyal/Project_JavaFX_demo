package model.service;

import java.util.ArrayList;
import java.util.List;

import mode.entites.Department;

public class DepartmentService {
	
	public List<Department> findAll(){
		List<Department> list = new ArrayList<>();
		list.add(new Department(1, "Informatica"));
		list.add(new Department(2, "Eletronica"));
		list.add(new Department(3, "Consoles"));
		list.add(new Department(4, "Carros"));
		return list;
	}
}
