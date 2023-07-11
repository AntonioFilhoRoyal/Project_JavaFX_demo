package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entites.Department;

public class DepartmentDialogFormController implements Initializable{

	private Department department;
	
	@FXML
	private TextField textID;
	
	@FXML
	private TextField textName;
	
	@FXML
	private Label labelAlert;
	
	@FXML
	private Button buttonSave;
	
	@FXML
	private Button buttonCancel;
	
	public void setDepartment(Department entity) {
		this.department = entity;
	}
	
	@FXML
	private void onButtonSaveAction() {
		System.out.println("Save");
	}
	
	@FXML
	private void onButtonCancelAction() {
		System.out.println("Cancel");
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		
	}

	public void initializeNodes() {
		Constraints.setTextFieldInteger(textID);
		Constraints.setTextFieldMaxLength(textName, 30);
	}

	public void updateForm() {
		if(department == null) {
			throw new IllegalArgumentException("Error department");
		}
		textID.setText(String.valueOf(department.getId()));
		textName.setText(department.getName());
	}
	
}
