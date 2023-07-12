package model.service;

import java.util.List;

import model.dao.DaoFactory;
import model.dao.SellerDAO;
import model.entites.Department;
import model.entites.Seller;

public class SellerService {
	
	private SellerDAO sellerDao = DaoFactory.createSellerDao();
	
	public List<Seller> findAll(){
		return sellerDao.findAll();
	}
	
	public void saveOrUpdate(Seller seller) {
		if(seller.getId() == null) {
			sellerDao.insert(seller);
		} else {
			sellerDao.update(seller);
		}
	}
	
	public void remove(Seller seller) {
		sellerDao.deleteById(seller.getId());
	}
}
