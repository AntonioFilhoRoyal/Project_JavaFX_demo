package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
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
import model.entites.Seller;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerController implements Initializable, DataChangerListener {

	private SellerService service;

	private ObservableList<Seller> observableList;

	public void setSellerService(SellerService service) {
		this.service = service;
	}

	@FXML
	private TableView<Seller> tableViewSeller;

	@FXML
	private TableColumn<Seller, Integer> tableColumnId;

	@FXML
	private TableColumn<Seller, String> tableColumnName;
	
	@FXML
	private TableColumn<Seller, String> tableColumnEmail;
	
	@FXML
	private TableColumn<Seller, Date> tableColumnBirthDate;
	
	@FXML
	private TableColumn<Seller, Double> tableColumnBaseSalary;
	
	@FXML
	private TableColumn<Seller, Seller> tableColumnEDIT;

	@FXML
	private TableColumn<Seller, Seller> tableColumnREMOVE;

	@FXML
	private Button registerBtn;

	@FXML
	private void onActionButtonRegister(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		Seller seller = new Seller();
		createSellerDialogForm(seller, "/gui/SellerDialogForm.fxml", parentStage);
	}

	@Override
	public void initialize(URL url, ResourceBundle resource) {
		initializeNodes();

	}

	public void initializeNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("name"));
		tableColumnEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		tableColumnBirthDate.setCellValueFactory(new PropertyValueFactory<>("birthDate"));
		Utils.formatTableColumnDate(tableColumnBirthDate, "dd/MM/yyyy");
		tableColumnBaseSalary.setCellValueFactory(new PropertyValueFactory<>("baseSalary"));
		Utils.formatTableColumnDouble(tableColumnBaseSalary, 2);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewSeller.prefHeightProperty().bind(stage.heightProperty());

	}

	public void updateSeller() {
		if (service == null) {
			throw new IllegalStateException("Service was null");
		}

		List<Seller> list = service.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewSeller.setItems(observableList);
		initEditButtons();
		initRemoveButtons();
	}
	
	public void createSellerDialogForm(Seller seller, String absoluteParent, Stage stageParent) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteParent));
			Pane pane = loader.load();

			SellerDialogFormController controller = loader.getController();
			controller.setSeller(seller);
			controller.setServices(new SellerService(), new DepartmentService());
			controller.loadAssociatedObject();
			controller.subscribeDataChangeListener(this);
			controller.updateForm();

			Stage dialog = new Stage();
			dialog.setTitle("Seller New");
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
		updateSeller();
	}

	
	private void initEditButtons() {
		tableColumnEDIT.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEDIT.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("edit");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> createSellerDialogForm(obj, "/gui/SellerDialogForm.fxml",
						Utils.currentStage(event)));
			}
		});
	}
	

	private void initRemoveButtons() {
		tableColumnREMOVE.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnREMOVE.setCellFactory(param -> new TableCell<Seller, Seller>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(Seller obj, boolean empty) {
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

	public void removeEntity(Seller obj) {
		Optional <ButtonType> optionalButton = Alerts.showConfirmation("Confirmation", "Are you sure delete?");
		
		if(optionalButton.get() == ButtonType.OK) {
			if(service == null) {
				throw new IllegalStateException("Service was null");
			}
			
			try {
				service.remove(obj);
				updateSeller();
			} catch(DbException e) {
				Alerts.showAlert("Error removing", null, e.getMessage(), AlertType.ERROR);
			}
		}
		
	}
	
}
