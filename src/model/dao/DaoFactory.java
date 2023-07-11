package model.dao;

import connector.DbConnect;
import model.dao.impl.DepartmentDaoJdbc;
import model.dao.impl.SellerDaoJdbc;

public class DaoFactory {

	public static SellerDAO createSellerDao() {
		return new SellerDaoJdbc(DbConnect.getConnection());
	}
	
	public static DepartmentDAO createDepartmentDao() {
		return new DepartmentDaoJdbc(DbConnect.getConnection());
	}
}
