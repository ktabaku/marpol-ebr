package al.sybina.ebr.logbook.dialogs;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankPosition;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ResizeHelper;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class SelectTankerDialogController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(SelectTankerDialogController.class);

	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private ThemeManager themeManager;
	@Inject
	private MarpolBean marpolBean;

	@FXML
	private AnchorPane anchorPane;

	@FXML
	private GridPane parentGridPane;

	@FXML
	private GridPane ballastForePeakGridPane;
	@FXML
	private GridPane ballastPortSideGridPane;
	@FXML
	private GridPane ballastSbdSideGridPane;
	@FXML
	private GridPane ballastAftPeakGridPane;

	@FXML
	private GridPane cargoGridPane;
	@FXML
	private GridPane slopGridPane;

	@FXML
	private GridPane fuelOilGridPane;
	@FXML
	private GridPane sludgeGridPane;
	@FXML
	private GridPane oilBilgeGridPane;

	private JFXButton closeButton;
	private Stage dialogStage;
	private JFXChipView<String> jFXChipView;
	private ObjectProperty<Font> fontTracking = new SimpleObjectProperty<Font>(Font.getDefault());

	private static final double defaultFontPointSize = 13;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40);
			closeButton.setPrefWidth(40);
			closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32);
			closeButton.setPrefHeight(32);
			closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
			});
			Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		initTankButtons();
		eventBroker.subscribe(Activator.MARPOL_DETAILS_RELOADED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				initTankButtons();
			}
		});

		Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
		anchorPane.setMaxHeight(screenBounds.getHeight() - 80);
		anchorPane.setMaxWidth(screenBounds.getWidth() - 40);

	}

	private boolean selectTankDetails(Tank tank, TankUsageContext tankUsageContext) {
		Stage tankDetailsStage = new Stage(StageStyle.UNDECORATED);
		tankDetailsStage.setTitle("Tank Details");
		tankDetailsStage.initModality(Modality.WINDOW_MODAL);
		tankDetailsStage.setResizable(true);
		tankDetailsStage.initOwner(dialogStage);

		FXMLLoader loaderTankDetails = new FXMLLoader(getClass().getResource("TankDetails.fxml"));
		try {
			Parent root = loaderTankDetails.load();
			tankDetailsStage.setScene(new Scene(root));
			JavaFXUtils.undecorate(tankDetailsStage, closeButton, themeManager);
			ResizeHelper.addResizeListener(tankDetailsStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TankDetailsController tankDetailsController = (TankDetailsController) loaderTankDetails.getController();
		tankDetailsController.setTank(tank);
		tankDetailsController.setTankUsageContext(tankUsageContext);
		tankDetailsController.setStage(tankDetailsStage);
		tankDetailsController.registerEventHandler(eventBroker);
		tankDetailsController.setSelectTankerDialogController(this);
		tankDetailsStage.showAndWait();
		return ((TankDetailsController) loaderTankDetails.getController()).isOkPressed();
	}

	public void initTankButtons() {
		MarpleLogSettings settings = marpolBean.getSettings();

		if (settings.getBallastTanks() == null)
			settings.setBallastTanks(new ArrayList<Tank>());
		if (settings.getCargoTanks() == null)
			settings.setCargoTanks(new ArrayList<Tank>());
		if (settings.getSlopTanks() == null)
			settings.setSlopTanks(new ArrayList<Tank>());
		if (settings.getFuelOilTanks() == null)
			settings.setFuelOilTanks(new ArrayList<Tank>());
		if (settings.getOilyBilgeTanks() == null)
			settings.setOilyBilgeTanks(new ArrayList<Tank>());
		if (settings.getSludgeTanks() == null)
			settings.setSludgeTanks(new ArrayList<Tank>());

		List<Tank> ballastTanksPortSide = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> ballastTanksForePeak = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.FORE_PEAK)).collect(Collectors.toList());
		List<Tank> ballastTanksAftPeak = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.AFT_PEAK)).collect(Collectors.toList());
		List<Tank> ballastTanksSbdSide = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		List<Tank> cargoTanksPortSide = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> cargoTanksCenter = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER)).collect(Collectors.toList());
		List<Tank> cargoTanksSbdSide = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		List<Tank> slopTanksPortSide = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> slopTanksCenter = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER)).collect(Collectors.toList());
		List<Tank> slopTanksSbdSide = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		List<Tank> fuelOilTanksPortSide = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> fuelOilTanksCenter1 = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> fuelOilTanksCenter2 = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> fuelOilTanksSbdSide = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		List<Tank> oilBilgeTanksPortSide = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksCenter1 = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksCenter2 = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksSbdSide = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		List<Tank> sludgeTanksPortSide = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> sludgeTanksCenter1 = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> sludgeTanksCenter2 = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> sludgeTanksSbdSide = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());

		EventHandler<ActionEvent> selectTankEventHandler = new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button button = (Button) event.getSource();
				Tank tank = (Tank) button.getUserData();
				if (!tank.getName().trim().isEmpty()) {

					if (!((TankUsageContext) jFXChipView.getUserData()).isNoNeedForUserTankQuantities()) {
						boolean okPressed = selectTankDetails(tank, (TankUsageContext) jFXChipView.getUserData());
						if (!okPressed)
							return;
						else {
							Map<Tank, Double> alreadyUpdatedTankQuantities = ((TankUsageContext) jFXChipView.getUserData()).getUserTankQuantities();
							Optional<Entry<Tank, Double>> alreadyUpdatedTank = alreadyUpdatedTankQuantities.entrySet().stream().filter(entry -> entry.getKey().getName().equals(button.getText())).findFirst();
							updateJFXSliderValue(button, tank);
							if (jFXChipView.getChips().remove(tank.getName().trim()))
								alreadyUpdatedTank.ifPresent(alreadyUpdatedTankEntry -> ((TankUsageContext) jFXChipView.getUserData()).getUserTankQuantities().put(alreadyUpdatedTankEntry.getKey(), alreadyUpdatedTankEntry.getValue()));
						}
					} else {
						jFXChipView.getChips().remove(button.getText());
						TankUsageContext tuc = (TankUsageContext) jFXChipView.getUserData();
						tuc.getUserTankQuantities().put(tank, null);
					}
					jFXChipView.getChips().add(tank.getName().trim());

					new Thread(() -> {
						try {
							while (jFXChipView.isNeedsLayout()) {
								Thread.sleep(100);
							}
						} catch (InterruptedException e) {
						}
						ObservableList<Node> children = jFXChipView.getChildrenUnmodifiable();
						if (!children.isEmpty() && children.get(0) instanceof ScrollPane) {
							Platform.runLater(() -> {
								((ScrollPane) children.get(0)).setVvalue(0.5);
								jFXChipView.requestLayout();
							});
						}
					}).start();
				} else {
					showInformation("The identity of the selected tank is empty!", "You can edit and save tanks at Marpol details.");
				}
			}
		};

		ballastPortSideGridPane.getChildren().retainAll(ballastPortSideGridPane.getChildren().get(0));
//		ballastPortSideGridPane.getRowConstraints().retainAll(ballastPortSideGridPane.getRowConstraints().get(0));
		ballastPortSideGridPane.getRowConstraints().clear();
		final int[] i = { 0, 0 }; // row index, column index not used in this case (1 column)
		ballastTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			ballastPortSideGridPane.addRow(i[0], buttonHBox);
			ballastPortSideGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
			i[0]++;
		});

		ballastForePeakGridPane.getChildren().retainAll(ballastForePeakGridPane.getChildren().get(0));
//		ballastForePeakGridPane.getRowConstraints().retainAll(ballastForePeakGridPane.getRowConstraints().get(0));
		ballastForePeakGridPane.getRowConstraints().clear();
		final int[] j = { 0, 0 }; // row index, column index
		ballastTanksForePeak.stream().forEach(tank -> {
//			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
//			if(j[1]>1) {
//				j[0]++; // row index increment
//				j[1] = 0; // column index reset
//				ballastForePeakGridPane.add(buttonHBox, j[1], j[0]);
//				ballastForePeakGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
//				j[1]++;
//			} else {
//				ballastForePeakGridPane.add(buttonHBox, j[1], j[0]);
//				j[1]++; //column index increment
//			}
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			ballastForePeakGridPane.addRow(j[0], buttonHBox);
			ballastForePeakGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
			j[0]++;
		});

		ballastAftPeakGridPane.getChildren().retainAll(ballastAftPeakGridPane.getChildren().get(0));
		ballastAftPeakGridPane.getRowConstraints().retainAll(ballastAftPeakGridPane.getRowConstraints().get(0));
		final int[] k = { 0, 0 }; // row index, column index
		ballastTanksAftPeak.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			if (k[1] > 1) {
				k[0]++; // row index increment
				k[1] = 0; // column index reset
				ballastAftPeakGridPane.add(buttonHBox, k[1], k[0]);
				ballastAftPeakGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
				k[1]++;
			} else {
				ballastAftPeakGridPane.add(buttonHBox, k[1], k[0]);
				k[1]++; // column index increment
			}
		});

		ballastSbdSideGridPane.getChildren().retainAll(ballastSbdSideGridPane.getChildren().get(0));
//		ballastSbdSideGridPane.getRowConstraints().retainAll(ballastSbdSideGridPane.getRowConstraints().get(0));
		ballastSbdSideGridPane.getRowConstraints().clear();
		final int[] l = { 0, 0 }; // row index, column index
		ballastTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			ballastSbdSideGridPane.addRow(l[0], buttonHBox);
			ballastSbdSideGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.ALWAYS, VPos.CENTER, true));
			l[0]++;
		});

		cargoGridPane.getChildren().retainAll(cargoGridPane.getChildren().get(0));
		cargoGridPane.getRowConstraints().retainAll(cargoGridPane.getRowConstraints().get(0));
		final int[] m = { 0, 0 }; // row index, column index
		cargoTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			cargoGridPane.add(buttonHBox, m[1], m[0]);
			m[0]++;
		});
		final int[] n = { 0, 1 }; // row index, column index
		cargoTanksCenter.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			cargoGridPane.add(buttonHBox, n[1], n[0]);
			n[0]++;
		});
		final int[] o = { 0, 2 }; // row index, column index
		cargoTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			cargoGridPane.add(buttonHBox, o[1], o[0]);
			o[0]++;
		});
		try {
			Method method = cargoGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(cargoGridPane);
			for (int ii = 1; ii < rows; ii++) {
				cargoGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		slopGridPane.getChildren().retainAll(slopGridPane.getChildren().get(0));
		slopGridPane.getRowConstraints().retainAll(slopGridPane.getRowConstraints().get(0));
		final int[] p = { 0, 0 }; // row index, column index
		slopTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			slopGridPane.add(buttonHBox, p[1], p[0]);
			p[0]++;
		});
		final int[] q = { 0, 1 }; // row index, column index
		slopTanksCenter.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			slopGridPane.add(buttonHBox, q[1], q[0]);
			q[0]++;
		});
		final int[] r = { 0, 2 }; // row index, column index
		slopTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			slopGridPane.add(buttonHBox, r[1], r[0]);
			r[0]++;
		});
		try {
			Method method = slopGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(slopGridPane);
			for (int ii = 1; ii < rows; ii++) {
				slopGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		fuelOilGridPane.getChildren().retainAll(fuelOilGridPane.getChildren().get(0));
		fuelOilGridPane.getRowConstraints().retainAll(fuelOilGridPane.getRowConstraints().get(0));
		final int[] s1 = { 0, 0 }; // row index, column index
		fuelOilTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			fuelOilGridPane.add(buttonHBox, s1[1], s1[0]);
			s1[0]++;
		});
		final int[] s2 = { 0, 1 }; // row index, column index
		fuelOilTanksCenter1.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			fuelOilGridPane.add(buttonHBox, s2[1], s2[0]);
			s2[0]++;
		});
		final int[] s3 = { 0, 2 }; // row index, column index
		fuelOilTanksCenter2.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			fuelOilGridPane.add(buttonHBox, s3[1], s3[0]);
			s3[0]++;
		});
		final int[] s4 = { 0, 3 }; // row index, column index
		fuelOilTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			fuelOilGridPane.add(buttonHBox, s4[1], s4[0]);
			s4[0]++;
		});
		try {
			Method method = fuelOilGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(fuelOilGridPane);
			for (int ii = 1; ii < rows; ii++) {
				fuelOilGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		oilBilgeGridPane.getChildren().retainAll(oilBilgeGridPane.getChildren().get(0));
		oilBilgeGridPane.getRowConstraints().retainAll(oilBilgeGridPane.getRowConstraints().get(0));
		final int[] t1 = { 0, 0 }; // row index, column index
		oilBilgeTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			oilBilgeGridPane.add(buttonHBox, t1[1], t1[0]);
			t1[0]++;
		});
		final int[] t2 = { 0, 1 }; // row index, column index
		oilBilgeTanksCenter1.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			oilBilgeGridPane.add(buttonHBox, t2[1], t2[0]);
			t2[0]++;
		});
		final int[] t3 = { 0, 2 }; // row index, column index
		oilBilgeTanksCenter2.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			oilBilgeGridPane.add(buttonHBox, t3[1], t3[0]);
			t3[0]++;
		});
		final int[] t4 = { 0, 3 }; // row index, column index
		oilBilgeTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			oilBilgeGridPane.add(buttonHBox, t4[1], t4[0]);
			t4[0]++;
		});
		try {
			Method method = oilBilgeGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(oilBilgeGridPane);
			for (int ii = 1; ii < rows; ii++) {
				oilBilgeGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		sludgeGridPane.getChildren().retainAll(sludgeGridPane.getChildren().get(0));
		sludgeGridPane.getRowConstraints().retainAll(sludgeGridPane.getRowConstraints().get(0));
		final int[] u1 = { 0, 0 }; // row index, column index
		sludgeTanksPortSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			sludgeGridPane.add(buttonHBox, u1[1], u1[0]);
			u1[0]++;
		});
		final int[] u2 = { 0, 1 }; // row index, column index
		sludgeTanksCenter1.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			sludgeGridPane.add(buttonHBox, u2[1], u2[0]);
			u2[0]++;
		});
		final int[] u3 = { 0, 2 }; // row index, column index
		sludgeTanksCenter2.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			sludgeGridPane.add(buttonHBox, u3[1], u3[0]);
			u3[0]++;
		});
		final int[] u4 = { 0, 3 }; // row index, column index
		sludgeTanksSbdSide.stream().forEach(tank -> {
			HBox buttonHBox = createButtonHBox(selectTankEventHandler, tank);
			sludgeGridPane.add(buttonHBox, u4[1], u4[0]);
			u4[0]++;
		});
		try {
			Method method = sludgeGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			Integer rows = (Integer) method.invoke(sludgeGridPane);
			for (int ii = 1; ii < rows; ii++) {
				sludgeGridPane.getRowConstraints().add(new RowConstraints(75, Region.USE_COMPUTED_SIZE, Region.USE_COMPUTED_SIZE, Priority.SOMETIMES, VPos.CENTER, true));
			}
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			e.printStackTrace();
		}

		parentGridPane.getRowConstraints().clear();
		anchorPane.requestLayout();
		if (dialogStage != null) {
			dialogStage.sizeToScene();
		}
	}

	@FXML
	private void handleClose() {
		dialogStage.close();
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setJFXChipView(JFXChipView jFXChipView) {
		this.jFXChipView = jFXChipView;
	}

	private HBox createButtonHBox(EventHandler<ActionEvent> selectTankEventHandler, Tank tank) {
		StringBuilder textButton = new StringBuilder();
		textButton.append(tank.getName());
		textButton.append(System.lineSeparator());
//		textButton.append(System.lineSeparator());
		textButton.append("MAX CAPACITY");
		textButton.append(System.lineSeparator());
		textButton.append(tank.getSize() + " M3");
		Button button = createScaledButton(textButton.toString());
		button.setTextAlignment(TextAlignment.CENTER);
		button.setUserData(tank);
		button.setOnAction(selectTankEventHandler);
		HBox buttonHBox = new HBox();
		JFXSlider vSlider = new JFXSlider();
		Double actualLevel = tank.getActualLevel() != null ? tank.getActualLevel() : 0.0;
		Tooltip.install(vSlider, new Tooltip("Actual Level " + actualLevel + " m3"));
		vSlider.setMin(0);
		vSlider.setMax(tank.getSize() != null ? tank.getSize() : 0.0);
		vSlider.setOrientation(Orientation.VERTICAL);
		vSlider.getStyleClass().add("changeWidthSlider");
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				vSlider.setValue(actualLevel);
			}
		});
		Label actualVolLabel = new Label(actualLevel + "M3");
		actualVolLabel.setTextAlignment(TextAlignment.CENTER);
		actualVolLabel.setAlignment(Pos.CENTER);
		actualVolLabel.setTextFill(Color.valueOf("black"));
		actualVolLabel.setPrefWidth(60);
		actualVolLabel.autosize();
		VBox sliderAndActualVolHolderVBox = new VBox();
		sliderAndActualVolHolderVBox.getStyleClass().add("vBoxSliderHolder");
		sliderAndActualVolHolderVBox.getChildren().addAll(actualVolLabel, vSlider);
		buttonHBox.getChildren().addAll(button, sliderAndActualVolHolderVBox);
		return buttonHBox;
	}

	private void updateJFXSliderValue(Button button, Tank tank) {
		try {
			HBox buttonHBox = (HBox) button.getParent();
			VBox sliderAndActualVolHolderVBox = (VBox) buttonHBox.getChildren().get(1);
			Label actualVolLabel = (Label) sliderAndActualVolHolderVBox.getChildren().get(0);
			JFXSlider vSlider = (JFXSlider) sliderAndActualVolHolderVBox.getChildren().get(1);
			TankUsageContext tankUsageContext = ((TankUsageContext) jFXChipView.getUserData());
			Double lastLevel = tank.getActualLevel() != null ? tank.getActualLevel() : 0.0;
			Double qtyAddRem = tankUsageContext.getUserTankQuantities().get(tank) != null ? tankUsageContext.getUserTankQuantities().get(tank) : 0.0;
			if (tankUsageContext.isAddition()) {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						vSlider.setValue(lastLevel + qtyAddRem);
						actualVolLabel.setText(lastLevel + qtyAddRem + "M3");
					}
				});

			} else {
				Platform.runLater(new Runnable() {
					@Override
					public void run() {
						vSlider.setValue(lastLevel - qtyAddRem);
						actualVolLabel.setText(lastLevel - qtyAddRem + "M3");
					}
				});
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	private Button createScaledButton(String text) {
		Button button = new Button(text);
		HBox.setHgrow(button, Priority.ALWAYS);
		button.getStyleClass().add("logbookSelectTankerButton");
		GridPane.setFillHeight(button, true);
		GridPane.setFillWidth(button, true);
		GridPane.setHgrow(button, Priority.ALWAYS);
		GridPane.setVgrow(button, Priority.ALWAYS);

		button.fontProperty().bind(fontTracking);

		button.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> scaleButton(button));

		button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

		return button;
	}

	private void scaleButton(Button button) {
		Text tmpText = new Text(button.getText());
		tmpText.setFont(button.getFont());
		double textWidth = tmpText.getLayoutBounds().getWidth();

		double maxAllowedWidth = button.getLayoutBounds().getWidth();
		// check if text width is smaller than maximum width allowed
		if (textWidth > maxAllowedWidth) {
			double newFontSize = defaultFontPointSize * maxAllowedWidth / textWidth;
			fontTracking.set(Font.font(newFontSize));
		}
//        else {
//        	fontTracking.set(Font.font(defaultFontPointSize));
//        }

	}

	public void showInformation(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Information");
		alert.setHeaderText(title);
		alert.setContentText(message);
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		alert.showAndWait();
	}

	public void showWarning(String title, String message) {
		Alert alert = new Alert(Alert.AlertType.WARNING);
		alert.initStyle(StageStyle.UTILITY);
		alert.setTitle("Warning");
		alert.setHeaderText(title);
		alert.setContentText(message);
		JavaFXUtils.undecorate(alert, createCloseButton(), themeManager);
		alert.showAndWait();
	}

	private JFXButton createCloseButton() {
		JFXButton closeButton = new JFXButton();
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40);
			closeButton.setPrefWidth(40);
			closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32);
			closeButton.setPrefHeight(32);
			closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage) ((Button) event.getSource()).getScene().getWindow()).close();
			});
			Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return closeButton;
	}

}
