package gui;

import java.net.URL;
import java.util.ResourceBundle;

import connector.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entites.Department;
import model.service.DepartmentService;

public class DepartmentDialogFormController implements Initializable{

	private Department department;
	
	private DepartmentService departmentService;
	
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
	
	public void setDepartmentService(DepartmentService service) {
		this.departmentService = service;
	}
	
	private Department getFormData() {
		Department department = new Department();
		
		department.setId(Utils.tryParseToInt(textID.getText()));
		department.setName(textName.getText());
		
		return department;
	}
	
	@FXML
	private void onButtonSaveAction(ActionEvent event) {
		if(department == null) {
			throw new IllegalStateException("department was null");
		}
		
		if(departmentService == null) {
			throw new IllegalStateException("serivce was null");
		}
		
		try {
			department = getFormData();
			departmentService.saveOrUpdate(department);
			Utils.currentStage(event).close();
		} catch(DbException e) {
			Alerts.showAlert("Error saving object", "error saving", e.getMessage(), AlertType.ERROR);
		}
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
