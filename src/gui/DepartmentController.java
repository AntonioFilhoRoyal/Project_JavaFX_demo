package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import connector.DbException;
import gui.listeners.DataChangerListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entites.Department;
import model.service.DepartmentService;

public class DepartmentController implements Initializable, DataChangerListener {

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
	private TableColumn<Department, Department> tableColumnEDIT;

	@FXML
	private TableColumn<Department, Department> tableColumnREMOVE;

	@FXML
	private Button registerBtn;

	@FXML
	private void onActionButtonRegister(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Department department = new Department();
		createDepartmentDialogForm(department, "/gui/DepartmentDialogForm.fxml", parentStage);
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
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Department> list = service.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(observableList);
		initEditButtons();
		initRemoveButtons();
	}

	public void createDepartmentDialogForm(Department department, String absoluteParent, Stage stageParent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteParent));
			Pane pane = loader.load();

			DepartmentDialogFormController controller = loader.getController();
			controller.setDepartment(department);
			controller.setDepartmentService(service);
			controller.subscribeDataChangeListener(this);
			controller.updateForm();

			Stage dialog = new Stage();
			dialog.setTitle("Departament New");
			dialog.setScene(new Scene(pane));
			dialog.setResizable(false);
			dialog.initOwner(stageParent);
			dialog.initModality(Modality.WINDOW_MODAL);
			dialog.showAndWait();

		} catch (IOException e) {
			Alerts.showAlert("View Error Loading", "View error", "No possible load view", AlertType.ERROR);
		}
	}

	public void onDataChange() {
		updateDeparment();
	}

	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createDepartmentDialogForm(obj, "/gui/DepartmentDialogForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Department, Department>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Department obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

	public void removeEntity(Department obj) {
		Optional <ButtonType> optionalButton = Alerts.showConfirmation("Confirmation", "Are you sure delete?");
		
		if(optionalButton.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				service.remove(obj);
				updateDeparment();
			} catch(DbException e) {
				Alerts.showAlert("Error removing", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}

}
