package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import connector.DbException;
import gui.listeners.DataChangerListener;
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
import model.entites.Seller;
import model.exceptions.ValidationException;
import model.service.SellerService;

public class SellerDialogFormController implements Initializable{

	private Seller seller;
	
	private SellerService sellerService;
	
	private List<DataChangerListener> dataChangeListener = new ArrayList<>();
	
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
	
	public void setSeller(Seller entity) {
		this.seller = entity;
	}
	
	public void setSellerService(SellerService service) {
		this.sellerService = service;
	}
	
	private Seller getFormData() {
		Seller seller = new Seller();
		ValidationException validation = new ValidationException("Validation Error");
		
		seller.setId(Utils.tryParseToInt(textID.getText()));
		
		if(textName.getText() == null || textName.getText().trim().equals("")) {
			validation.addError("name", "Field was null");
		}
		seller.setName(textName.getText());
		
		if(validation.getErrors().size() > 0) {
			throw validation;
		}
		
		return seller;
	}
	
	public void subscribeDataChangeListener(DataChangerListener listener) {
		dataChangeListener.add(listener);
	}
	
	private void notifyListener() {
		for(DataChangerListener listener : dataChangeListener) {
			listener.onDataChange();
		}
		
	}
	
	@FXML
	private void onButtonSaveAction(ActionEvent event) {
		if(seller == null) {
			throw new IllegalStateException("seller was null");
		}
		
		if(sellerService == null) {
			throw new IllegalStateException("serivce was null");
		}
		
		try {
			seller = getFormData();
			sellerService.saveOrUpdate(seller);
			notifyListener();
			Utils.currentStage(event).close();
		} 
		catch(ValidationException e) {
			setErrors(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error saving object", "error saving", e.getMessage(), AlertType.ERROR);
		}
	}
	

	@FXML
	private void onButtonCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
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
		if(seller == null) {
			throw new IllegalArgumentException("Error seller");
		}
		textID.setText(String.valueOf(seller.getId()));
		textName.setText(seller.getName());
	}
	
	public void setErrors(Map<String, String> errors) {
		Set<String> field = errors.keySet();
		
		if(field.contains("name")) {
			labelAlert.setText(errors.get("name"));
		}
		
	}
	
}
