package al.sybina.ebr.logbook.dialogs;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicBoolean;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.service.event.Event;

import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.sun.javafx.binding.SelectBinding.AsDouble;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.logbook.commons.Activator;
import javafx.application.Platform;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import jidefx.scene.control.field.DoubleField;

public class TankDetailsController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(TankDetailsController.class);

	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private Label quantityInTheTankLabel;
	@FXML
	private Label addedRemovedLabel;
	@FXML
	private Label tankNameLabel;
	@FXML
	private DoubleField maxCapacityDoubleField;
	@FXML
	private DoubleField quantityInTheTankDoubleField;
	@FXML
	private DoubleField quantityAddedRemovedDoubleField;
	@FXML
	private DoubleField retainedDoubleField;
	@FXML
	private HBox holderHBox;
	@FXML
	private CheckBox emptyTankCheckBox;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;

	private Stage stage;

	private Tank tank;

	private TankUsageContext tankUsageContext;

	private boolean cancelPressed;
	private boolean okPressed;

	private AtomicBoolean disableEmptyCheckBoxListener = new AtomicBoolean(false);

	private SelectTankerDialogController selectTankerDialogController;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(maxCapacityDoubleField);
		MathUtils.setStringConverter(quantityInTheTankDoubleField);
		MathUtils.setStringConverter(quantityAddedRemovedDoubleField);
		MathUtils.setStringConverter(retainedDoubleField);

		JavaFXUtils.addFilledPseudoClass(quantityAddedRemovedDoubleField);
		okButton.disableProperty().bind(quantityAddedRemovedDoubleField.valueProperty().isNull());
		quantityAddedRemovedDoubleField.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observableValue, Double oldValue, Double newValue) {
				if (newValue != null) {
					Double actualQuantity = quantityInTheTankDoubleField.getValue() != null ? quantityInTheTankDoubleField.getValue() : 0;
					if (!tankUsageContext.isC11_1() && !tankUsageContext.isC11_4()) {
						if (tankUsageContext.isAddition()) {
							if ((actualQuantity + newValue) > tank.getSize() || newValue < 0) {
								selectTankerDialogController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + (tank.getSize() - actualQuantity));
								quantityAddedRemovedDoubleField.setValue(null);
								return;
							}
						} else {

							if ((actualQuantity - newValue) < 0 || newValue < 0) {
								selectTankerDialogController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + actualQuantity);
								emptyTankCheckBox.setSelected(false);
								quantityAddedRemovedDoubleField.setValue(null);
								return;
							}

							// set emptyCheckBox to false if new value is different from actual qty
							if ((Math.abs(actualQuantity - newValue) > MathUtils.DOUBLE_THRESHOLD) && emptyTankCheckBox.isSelected()) {
								disableEmptyCheckBoxListener.set(true);
								emptyTankCheckBox.setSelected(false);
								disableEmptyCheckBoxListener.set(false);
							}
							// if qty  retained ==0.0 select emptycheckbox 
							if (actualQuantity - newValue == 0) {
								disableEmptyCheckBoxListener.set(true);
								emptyTankCheckBox.setSelected(true);
								disableEmptyCheckBoxListener.set(false);
							}
						}

					} else {
						if (tankUsageContext.isC11_1()) {
							if (newValue > tank.getSize() || newValue < 0) {
								selectTankerDialogController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + tank.getSize());
								quantityAddedRemovedDoubleField.setValue(null);
								return;
							}
						} else if (tankUsageContext.isC11_4()) {
							if (tankUsageContext.getMaxC11_4() < actualQuantity) {
								if ((tankUsageContext.getMaxC11_4() - newValue) < 0 || newValue < 0) {
									selectTankerDialogController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + tankUsageContext.getMaxC11_4());
									quantityAddedRemovedDoubleField.setValue(null);
									return;
								}
							} else {
								if ((actualQuantity - newValue) < 0 || newValue < 0) {
									selectTankerDialogController.showWarning("Invalid value:" + newValue, "Valid value range:0.0-" + actualQuantity);
									quantityAddedRemovedDoubleField.setValue(null);
									return;
								}
							}

						}
					}
				}
			}
		});
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				emptyTankCheckBoxOnChange();
			}
		});

	}

	private void emptyTankCheckBoxOnChange() {
		if (!tankUsageContext.isAddition()) {
			holderHBox.setVisible(true);
			emptyTankCheckBox.setDisable(false);
			emptyTankCheckBox.setSelected(false);
			emptyTankCheckBox.selectedProperty().addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					Double actualQuantity = quantityInTheTankDoubleField.getValue() != null ? quantityInTheTankDoubleField.getValue() : 0.0;
					if (!disableEmptyCheckBoxListener.get()) {
						if (newValue) {
							quantityAddedRemovedDoubleField.setValue(actualQuantity);
						} else {
							quantityAddedRemovedDoubleField.setValue(null);
						}
					}
				}
			});
		} else {
			holderHBox.setVisible(false);
			emptyTankCheckBox.setSelected(false);
		}

	}

	@FXML
	public void ok() {
		okPressed = true;
		cancelPressed = false;
		Double actualQuantity = quantityInTheTankDoubleField.getValue() != null ? quantityInTheTankDoubleField.getValue() : 0;
		tankUsageContext.getUserTankQuantities().put(tank, quantityAddedRemovedDoubleField.getValue());
//		if(tankUsageContext.isAddition()) {
//			tankUsageContext.getUserTankQuantities().put(tank, actualQuantity + quantityAddedRemovedDoubleField.getValue());
//		} else {
//			tankUsageContext.getUserTankQuantities().put(tank, actualQuantity - quantityAddedRemovedDoubleField.getValue());
//		}
		stage.close();
	}

	@FXML
	public void cancel() {
		okPressed = false;
		cancelPressed = true;
		stage.close();
	}

	public boolean isCancelPressed() {
		return cancelPressed;
	}

	public void setCancelPressed(boolean cancelPressed) {
		this.cancelPressed = cancelPressed;
	}

	public boolean isOkPressed() {
		return okPressed;
	}

	public void setOkPressed(boolean okPressed) {
		this.okPressed = okPressed;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setTank(Tank tank) {
		this.tank = tank;
	}

	public void setTankUsageContext(TankUsageContext tankUsageContext) {
		this.cancelPressed = false;
		this.okPressed = false;
		this.tankUsageContext = tankUsageContext;
		tankNameLabel.setText(tank.getName());
		if (!tankUsageContext.isC11_1()) {
			addedRemovedLabel.setText(tankUsageContext.isAddition() ? "QUANTITY ADDED" : "QUANTITY REMOVED");
			maxCapacityDoubleField.setValue(tank.getSize());
			quantityInTheTankDoubleField.setValue(tank.getActualLevel());
			quantityAddedRemovedDoubleField.setValue(null);

			DoubleExpression quantityInTheTankDoubleProperty = AsDouble.doubleExpression(quantityInTheTankDoubleField.valueProperty());
			DoubleExpression quantityAddedRemovedDoubleProperty = AsDouble.doubleExpression(quantityAddedRemovedDoubleField.valueProperty());
			if (tankUsageContext.isAddition()) {
				retainedDoubleField.valueProperty().bind(quantityInTheTankDoubleProperty.add(quantityAddedRemovedDoubleProperty).asObject());
			} else {
				retainedDoubleField.valueProperty().bind(quantityInTheTankDoubleProperty.subtract(quantityAddedRemovedDoubleProperty).asObject());
			}
		} else {
			quantityInTheTankLabel.setText("PREV. Q.TY IN THE TANK");
			addedRemovedLabel.setText("Q.TY IN THE TANK");
			maxCapacityDoubleField.setValue(tank.getSize());
			quantityInTheTankDoubleField.setValue(tank.getActualLevel());
			quantityAddedRemovedDoubleField.setValue(tank.getActualLevel());

			DoubleExpression quantityAddedRemovedDoubleProperty = AsDouble.doubleExpression(quantityAddedRemovedDoubleField.valueProperty());
			retainedDoubleField.valueProperty().bind(quantityAddedRemovedDoubleProperty.asObject());
		}

	}

	public void registerEventHandler(IEventBroker eventBroker) {
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> ids = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				if (ids == null || (tank != null && ids.contains(tank.getId()))) {
					cancel();
				}
			}
		});
	}

	public SelectTankerDialogController getSelectTankerDialogController() {
		return selectTankerDialogController;
	}

	public void setSelectTankerDialogController(SelectTankerDialogController selectTankerDialogController) {
		this.selectTankerDialogController = selectTankerDialogController;
	}

}
