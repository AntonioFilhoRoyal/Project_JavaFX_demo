package gui;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;

public class ViewController {

	@FXML
	private MenuItem menuItemSeller;
	
	@FXML
	private MenuItem menuItemDepartment;
	
	@FXML
	private MenuItem menuItemAbout;

	
	@FXML
	public void onMenuItemSeller() {
		System.out.println("Seller");
	}
	
	@FXML
	public void onMenuItemDeparment() {
		System.out.println("Department");
	}
	
	@FXML
	public void onMenuItemAbout() {
		System.out.println("About");
	}
}
