package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import gui.util.Utils;

import application.Main;
import gui.util.Alerts;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entites.Department;
import model.service.DepartmentService;

public class DepartmentController implements Initializable{
	
	private DepartmentService service;
	
	private ObservableList<Department> observableList;
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	@FXML
	private TableView<Department> tableViewDepartment;
	
	@FXML
	private TableColumn<Department, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Department, String> tableColumnName;

	@FXML
	private Button registerBtn;
	
	@FXML
	private void onActionButtonRegister(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		createDepartmentDialogForm("/gui/DepartmentDialogForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		initializeNodes();
		
	}
	
	public void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
	
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
		
	}


	public void updateDeparment() {
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		List<Department> list = service.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(observableList);
	}
	
	public void createDepartmentDialogForm(String absoluteParent, Stage stageParent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteParent));
			Pane pane = loader.load();
			
			Stage dialog = new Stage();
			dialog.setTitle("Departament New");
			dialog.setScene(new Scene(pane));
			dialog.setResizable(false);
			dialog.initOwner(stageParent);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.showAndWait();
			
		} catch(IOException e) {
			Alerts.showAlert("View Error Loading", "View error", "No possible load view", AlertType.ERROR);
		}
	}
	
	
}
