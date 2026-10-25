package al.sybina.ebr.logbook.views.marpoldetail;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.MarpleLogSettings;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.TankPosition;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.MathUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.LocalDateTextField;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.NumberField;

public class MarpolDetailController implements Initializable {


	private static final Logger logger = LoggerCreator.createLogger(MarpolDetailController.class);
	
	@Inject
	private static ThemeManager themeManager;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private IEclipseContext context;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	
	@FXML
	private ImageView imageViewUser;
	@FXML
	private AnchorPane anchorPane;

	@FXML
	private Button cargoTanksPortSideButton;
	@FXML
	private Button cargoTanksCenterButton;
	@FXML
	private Button cargoTanksSbdSideButton;
	@FXML
	private GridPane cargoTanksPortSideGridPane;
	@FXML
	private GridPane cargoTanksCenterGridPane;
	@FXML
	private GridPane cargoTanksSbdSideGridPane;
	
	@FXML
	private Button slopTanksPortSideButton;
	@FXML
	private Button slopTanksCenterButton;
	@FXML
	private Button slopTanksSbdSideButton;
	@FXML
	private GridPane slopTanksPortSideGridPane;
	@FXML
	private GridPane slopTanksCenterGridPane;
	@FXML
	private GridPane slopTanksSbdSideGridPane;
	
	@FXML
	private Button fuelOilTanksPortSideButton;
	@FXML
	private Button fuelOilTanksCenter1Button;
	@FXML
	private Button fuelOilTanksCenter2Button;
	@FXML
	private Button fuelOilTanksSbdSideButton;
	@FXML
	private GridPane fuelOilTanksPortSideGridPane;
	@FXML
	private GridPane fuelOilTanksCenter1GridPane;
	@FXML
	private GridPane fuelOilTanksCenter2GridPane;
	@FXML
	private GridPane fuelOilTanksSbdSideGridPane;
	
	@FXML
	private Button oilBilgeTanksPortSideButton;
	@FXML
	private Button oilBilgeTanksCenter1Button;
	@FXML
	private Button oilBilgeTanksCenter2Button;
	@FXML
	private Button oilBilgeTanksSbdSideButton;
	@FXML
	private GridPane oilBilgeTanksPortSideGridPane;
	@FXML
	private GridPane oilBilgeTanksCenter1GridPane;
	@FXML
	private GridPane oilBilgeTanksCenter2GridPane;
	@FXML
	private GridPane oilBilgeTanksSbdSideGridPane;
	
	@FXML
	private Button sludgeTanksPortSideButton;
	@FXML
	private Button sludgeTanksCenter1Button;
	@FXML
	private Button sludgeTanksCenter2Button;
	@FXML
	private Button sludgeTanksSbdSideButton;
	@FXML
	private GridPane sludgeTanksPortSideGridPane;
	@FXML
	private GridPane sludgeTanksCenter1GridPane;
	@FXML
	private GridPane sludgeTanksCenter2GridPane;
	@FXML
	private GridPane sludgeTanksSbdSideGridPane;
	
//	@FXML
//	private Button mgoTanksPortSideButton;
//	@FXML
//	private Button mgoTanksCenterButton;
//	@FXML
//	private Button mgoTanksSbdSideButton;
//	@FXML
//	private GridPane mgoTanksPortSideGridPane;
//	@FXML
//	private GridPane mgoTanksCenterGridPane;
//	@FXML
//	private GridPane mgoTanksSbdSideGridPane;
	
	@FXML
	private Button ballastTanksPortSideButton;
	@FXML
	private Button ballastTanksForePeakButton;
	@FXML
	private Button ballastTanksAftPeakButton;
	@FXML
	private Button ballastTanksSbdSideButton;
	@FXML
	private GridPane ballastTanksPortSideGridPane;
	@FXML
	private GridPane ballastTanksForePeakGridPane;
	@FXML
	private GridPane ballastTanksAftPeakGridPane;
	@FXML
	private GridPane ballastTanksSbdSideGridPane;
	
	
	@FXML
	private DoubleField inceneratorCapacityDoubleField;
	@FXML
	private DoubleField oilFuelOilResiduesDoubleField;
	@FXML
	private DoubleField bilgeWaterSeparatorThroughputDoubleField;
	
	@FXML
	private TextField otherMeansTextField;
	
	
	@FXML
	private DoubleField totalVolumeofCargoTanksField;
	@FXML
	private DoubleField totalVolumeofSlopTanksField;
	@FXML
	private DoubleField totalVolumeofFuelTanksField;
	@FXML
	private DoubleField totalVolumeofOilBilgeTanksField;
	@FXML
	private DoubleField totalVolumeofSludgeTanksField;
//	@FXML
//	private FormattedTextField totalVolumeofMgoTanksField;
	@FXML
	private DoubleField totalVolumeofBallastTanksField;
	
	
	
	
	
	
	@FXML
	private GridPane shipParticularsGridPane;
	
	@FXML
	private TextField shipImoNumberTextField;
	@FXML
	private TextField shipNameTextField;
	@FXML
	private TextField shipDistinctiveNumberTextField;
	@FXML
	private TextField shipPortOfRegistryTextField;
	@FXML
	private TextField shipGrossTonnageTextField;
	@FXML
	private DoubleField shipCarringCapacityDoubleField;
	@FXML
	private DoubleField shipDeadweightDoubleField;
	@FXML
	private DoubleField shipLengthDoubleField;
	@FXML
	private LocalDateTextField shipBuildingContractDate;
	@FXML
	private LocalDateTextField shipKeelLaidDate;
	@FXML
	private LocalDateTextField shipDeliveryDate;
	
	@FXML
	private Button save;
	
	private static JFXButton closeButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MathUtils.setStringConverter(inceneratorCapacityDoubleField);
		MathUtils.setStringConverter(oilFuelOilResiduesDoubleField);
		MathUtils.setStringConverter(bilgeWaterSeparatorThroughputDoubleField);
		MathUtils.setStringConverter(shipCarringCapacityDoubleField);
		MathUtils.setStringConverter(shipDeadweightDoubleField);
		MathUtils.setStringConverter(shipLengthDoubleField);
		MathUtils.setStringConverter(totalVolumeofCargoTanksField);
		MathUtils.setStringConverter(totalVolumeofSlopTanksField);
		MathUtils.setStringConverter(totalVolumeofFuelTanksField);
		MathUtils.setStringConverter(totalVolumeofOilBilgeTanksField);
		MathUtils.setStringConverter(totalVolumeofSludgeTanksField);
		MathUtils.setStringConverter(totalVolumeofBallastTanksField);
		MathUtils.setMinMaxValues(inceneratorCapacityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(oilFuelOilResiduesDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(bilgeWaterSeparatorThroughputDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(shipCarringCapacityDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(shipDeadweightDoubleField, 0.0, null);
		MathUtils.setMinMaxValues(shipLengthDoubleField, 0.0, null);
		
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40); closeButton.setPrefWidth(40); closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32); closeButton.setPrefHeight(32); closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			Image userImage = imageProvider.getImage(((WorkbenchResourceProvider) resourcesService)
					.getImageURI(WorkbenchResourceProvider.IMG_IBR_LOGBOOK_USER));
			imageViewUser.setImage(userImage);
		} catch (IOException e) {
			e.printStackTrace();
		}
		shipBuildingContractDate.setDateTimeFormatter(EasyDateUtils.logbookSimpleDateTimeFormatter);
		shipKeelLaidDate.setDateTimeFormatter(EasyDateUtils.logbookSimpleDateTimeFormatter);
		shipDeliveryDate.setDateTimeFormatter(EasyDateUtils.logbookSimpleDateTimeFormatter);
		
		JavaFXUtils.addFilledPseudoClass(totalVolumeofSlopTanksField);
		JavaFXUtils.addFilledPseudoClass(totalVolumeofBallastTanksField);
		JavaFXUtils.addFilledPseudoClass(shipNameTextField);
		JavaFXUtils.addFilledPseudoClass(shipDistinctiveNumberTextField);
		JavaFXUtils.addFilledPseudoClass(shipPortOfRegistryTextField);
		JavaFXUtils.addFilledPseudoClass(shipGrossTonnageTextField);
		JavaFXUtils.addFilledPseudoClass(shipCarringCapacityDoubleField);
		JavaFXUtils.addFilledPseudoClass(shipDeadweightDoubleField);
		JavaFXUtils.addFilledPseudoClass(shipLengthDoubleField);
		JavaFXUtils.addFilledPseudoClass(shipImoNumberTextField);
		JavaFXUtils.addFilledPseudoClass(totalVolumeofFuelTanksField);
		JavaFXUtils.addFilledPseudoClass(totalVolumeofOilBilgeTanksField);
		JavaFXUtils.addFilledPseudoClass(totalVolumeofSludgeTanksField);
		JavaFXUtils.addFilledPseudoClass(inceneratorCapacityDoubleField);
		JavaFXUtils.addFilledPseudoClass(oilFuelOilResiduesDoubleField);
		JavaFXUtils.addFilledPseudoClass(bilgeWaterSeparatorThroughputDoubleField);
		JavaFXUtils.addFilledPseudoClass(shipBuildingContractDate);
		JavaFXUtils.addFilledPseudoClass(shipKeelLaidDate);
		JavaFXUtils.addFilledPseudoClass(shipDeliveryDate);
		
		loadMarpolDetails();
		
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_MASTER_SIGN)) {
					disableEditMarpol(false);
				} else {
					disableEditMarpol(true);
				}
			}
		});
		if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_MASTER_SIGN)) {
			disableEditMarpol(false);
		} else {
			disableEditMarpol(true);
		}
		
		eventBroker.subscribe(Activator.MARPOL_DETAILS_CHANGED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				marpolBean.init();
				loadMarpolDetails();
				
				ArrayList<Long> updatedTankIds = (ArrayList<Long>) event.getProperty(Activator.TANK_IDS);
				Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
				idsParameter.put(Activator.TANK_IDS, updatedTankIds);
				eventBroker.post(Activator.MARPOL_DETAILS_RELOADED, idsParameter);
			}
		});
	}



	private void loadMarpolDetails() {
		MarpleLogSettings settings = marpolBean.getSettings();
		
		if(settings.getCargoTanks()==null) settings.setCargoTanks(new ArrayList<Tank>());
		if(settings.getFuelOilTanks()==null) settings.setFuelOilTanks(new ArrayList<Tank>());
		if(settings.getMgoTanks()==null) settings.setMgoTanks(new ArrayList<Tank>());
		if(settings.getOilyBilgeTanks()==null) settings.setOilyBilgeTanks(new ArrayList<Tank>());
		if(settings.getSlopTanks()==null) settings.setSlopTanks(new ArrayList<Tank>());
		if(settings.getSludgeTanks()==null) settings.setSludgeTanks(new ArrayList<Tank>());
		if(settings.getBallastTanks()==null) settings.setBallastTanks(new ArrayList<Tank>());
		
		List<Tank> cargoTanksPortSide = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> cargoTanksCenter = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER)).collect(Collectors.toList());
		List<Tank> cargoTanksSbdSide = settings.getCargoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(cargoTanksPortSide, cargoTanksPortSideGridPane);
		initTankSettings(cargoTanksCenter, cargoTanksCenterGridPane);
		initTankSettings(cargoTanksSbdSide, cargoTanksSbdSideGridPane);
		
		List<Tank> fuelOilTanksPortSide = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> fuelOilTanksCenter1 = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> fuelOilTanksCenter2 = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> fuelOilTanksSbdSide = settings.getFuelOilTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(fuelOilTanksPortSide, fuelOilTanksPortSideGridPane);
		initTankSettings(fuelOilTanksCenter1, fuelOilTanksCenter1GridPane);
		initTankSettings(fuelOilTanksCenter2, fuelOilTanksCenter2GridPane);
		initTankSettings(fuelOilTanksSbdSide, fuelOilTanksSbdSideGridPane);
		
//		List<Tank> mgoTanksPortSide = settings.getMgoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
//		List<Tank> mgoTanksCenter = settings.getMgoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER)).collect(Collectors.toList());
//		List<Tank> mgoTanksSbdSide = settings.getMgoTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
//		initTankSettings(mgoTanksPortSide, mgoTanksPortSideGridPane);
//		initTankSettings(mgoTanksCenter, mgoTanksCenterGridPane);
//		initTankSettings(mgoTanksSbdSide, mgoTanksSbdSideGridPane);
		
		List<Tank> oilBilgeTanksPortSide = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksCenter1 = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksCenter2 = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> oilBilgeTanksSbdSide = settings.getOilyBilgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(oilBilgeTanksPortSide, oilBilgeTanksPortSideGridPane);
		initTankSettings(oilBilgeTanksCenter1, oilBilgeTanksCenter1GridPane);
		initTankSettings(oilBilgeTanksCenter2, oilBilgeTanksCenter2GridPane);
		initTankSettings(oilBilgeTanksSbdSide, oilBilgeTanksSbdSideGridPane);
		
		List<Tank> slopTanksPortSide = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> slopTanksCenter = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER)).collect(Collectors.toList());
		List<Tank> slopTanksSbdSide = settings.getSlopTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(slopTanksPortSide, slopTanksPortSideGridPane);
		initTankSettings(slopTanksCenter, slopTanksCenterGridPane);
		initTankSettings(slopTanksSbdSide, slopTanksSbdSideGridPane);
		
		List<Tank> sludgeTanksPortSide = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> sludgeTanksCenter1 = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER1)).collect(Collectors.toList());
		List<Tank> sludgeTanksCenter2 = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.CENTER2)).collect(Collectors.toList());
		List<Tank> sludgeTanksSbdSide = settings.getSludgeTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(sludgeTanksPortSide, sludgeTanksPortSideGridPane);
		initTankSettings(sludgeTanksCenter1, sludgeTanksCenter1GridPane);
		initTankSettings(sludgeTanksCenter2, sludgeTanksCenter2GridPane);
		initTankSettings(sludgeTanksSbdSide, sludgeTanksSbdSideGridPane);
		
		List<Tank> ballastTanksPortSide = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.PORTSIDE)).collect(Collectors.toList());
		List<Tank> ballastTanksForePeak = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.FORE_PEAK)).collect(Collectors.toList());
		List<Tank> ballastTanksAftPeak = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.AFT_PEAK)).collect(Collectors.toList());
		List<Tank> ballastTanksSbdSide = settings.getBallastTanks().stream().filter(c -> c.getTankPosition().equals(TankPosition.SBDSIDE)).collect(Collectors.toList());		
		initTankSettings(ballastTanksPortSide, ballastTanksPortSideGridPane);
		initTankSettings(ballastTanksForePeak, ballastTanksForePeakGridPane);
		initTankSettings(ballastTanksAftPeak, ballastTanksAftPeakGridPane);
		initTankSettings(ballastTanksSbdSide, ballastTanksSbdSideGridPane);
		
		inceneratorCapacityDoubleField.setValue(settings.getIncineratorCapacity());
		oilFuelOilResiduesDoubleField.setValue(settings.getMixingOilCapacity());
		bilgeWaterSeparatorThroughputDoubleField.setValue(settings.getBilgeWaterSeparatorThroughput());
//		otherMeansTextField.setText(settings.getOtherMeans());
		
		updateTotalLabels(settings);
		
		if(settings.getShip()==null) settings.setShip(new Ship());
		initShipSettings(settings.getShip());
	}



	private void disableEditMarpol(boolean disable) {
		cargoTanksPortSideButton.setDisable(disable);
		cargoTanksCenterButton.setDisable(disable);
		cargoTanksSbdSideButton.setDisable(disable);
		cargoTanksPortSideGridPane.setDisable(disable);
		cargoTanksCenterGridPane.setDisable(disable);
		cargoTanksSbdSideGridPane.setDisable(disable);
		
		fuelOilTanksPortSideButton.setDisable(disable);
		fuelOilTanksCenter1Button.setDisable(disable);
		fuelOilTanksCenter2Button.setDisable(disable);
		fuelOilTanksSbdSideButton.setDisable(disable);
		fuelOilTanksPortSideGridPane.setDisable(disable);
		fuelOilTanksCenter1GridPane.setDisable(disable);
		fuelOilTanksCenter2GridPane.setDisable(disable);
		fuelOilTanksSbdSideGridPane.setDisable(disable);
		
		oilBilgeTanksPortSideButton.setDisable(disable);
		oilBilgeTanksCenter1Button.setDisable(disable);
		oilBilgeTanksCenter2Button.setDisable(disable);
		oilBilgeTanksSbdSideButton.setDisable(disable);
		oilBilgeTanksPortSideGridPane.setDisable(disable);
		oilBilgeTanksCenter1GridPane.setDisable(disable);
		oilBilgeTanksCenter2GridPane.setDisable(disable);
		oilBilgeTanksSbdSideGridPane.setDisable(disable);
		
		slopTanksPortSideButton.setDisable(disable);
		slopTanksCenterButton.setDisable(disable);
		slopTanksSbdSideButton.setDisable(disable);
		slopTanksPortSideGridPane.setDisable(disable);
		slopTanksCenterGridPane.setDisable(disable);
		slopTanksSbdSideGridPane.setDisable(disable);
		
		sludgeTanksPortSideButton.setDisable(disable);
		sludgeTanksCenter1Button.setDisable(disable);
		sludgeTanksCenter2Button.setDisable(disable);
		sludgeTanksSbdSideButton.setDisable(disable);
		sludgeTanksPortSideGridPane.setDisable(disable);
		sludgeTanksCenter1GridPane.setDisable(disable);
		sludgeTanksCenter2GridPane.setDisable(disable);
		sludgeTanksSbdSideGridPane.setDisable(disable);
		
		ballastTanksPortSideButton.setDisable(disable);
		ballastTanksForePeakButton.setDisable(disable);
		ballastTanksAftPeakButton.setDisable(disable);
		ballastTanksSbdSideButton.setDisable(disable);
		ballastTanksPortSideGridPane.setDisable(disable);
		ballastTanksForePeakGridPane.setDisable(disable);
		ballastTanksAftPeakGridPane.setDisable(disable);
		ballastTanksSbdSideGridPane.setDisable(disable);
		
		inceneratorCapacityDoubleField.setDisable(disable);
		oilFuelOilResiduesDoubleField.setDisable(disable);
		bilgeWaterSeparatorThroughputDoubleField.setDisable(disable);
		shipParticularsGridPane.setDisable(disable);
		

		

		

		
		save.setDisable(disable);
	}



	private void initTankSettings(List<Tank> tankList, GridPane gridPane) {
		if(gridPane.getUserData()==null) gridPane.setUserData(new TreeMap<Integer, Tank>());
		if(tankList!=null) {
			int i = 1;
			gridPane.getChildren().removeIf(node -> GridPane.getRowIndex(node)!=null && GridPane.getRowIndex(node) > 0);
			for(Tank tank : tankList) {
				TextField tankIdentifier = new TextField(tank.getName());
				DoubleField tankVolume = new DoubleField();
				MathUtils.setStringConverter(tankVolume);
				MathUtils.setMinMaxValues(tankVolume, 0.0, null);
				tankVolume.setId("tankVolume" + i);
				tankVolume.setValue(tank.getSize());
				DoubleField actualTankLevel = new DoubleField();
				MathUtils.setStringConverter(actualTankLevel);
				MathUtils.setMinMaxValues(actualTankLevel, 0.0, tankVolume.getValue());
				tankVolume.valueProperty().addListener(new ChangeListener<Double>() {
					@Override
					public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
						MathUtils.setMinMaxValues(actualTankLevel, 0.0, newValue);
					}
				});
				actualTankLevel.setId("actualTankLevel" + i);
				actualTankLevel.setValue(tank.getActualLevel());
				tankIdentifier.getStyleClass().add("logbook-text-field");
				actualTankLevel.getStyleClass().add("logbook-text-field");
				tankVolume.getStyleClass().add("logbook-text-field");
				JavaFXUtils.addFilledPseudoClass(tankIdentifier);
				JavaFXUtils.addFilledPseudoClass(actualTankLevel);
				JavaFXUtils.addFilledPseudoClass(tankVolume);
				Button deleteButton = new Button("X");
				deleteButton.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						Button deleteButton = (Button)event.getSource();
						GridPane parent = (GridPane)deleteButton.getParent();
						Integer rowIndex = GridPane.getRowIndex(deleteButton);
						parent.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
						TreeMap<Integer, Tank> oldTankMap = (TreeMap<Integer, Tank>) gridPane.getUserData();
						oldTankMap.remove(rowIndex);
					}
				});
				gridPane.addRow(i, tankIdentifier, actualTankLevel, tankVolume, deleteButton);
				((TreeMap<Integer, Tank>)gridPane.getUserData()).put(i, tank);
				i++;
			}
		}
	}
	

	
	public void addCargoTankPortSide() {
		addTank(cargoTanksPortSideGridPane);
	}
	public void addCargoTankCenter() {
		addTank(cargoTanksCenterGridPane);
	}
	public void addCargoTankSbdSide() {
		addTank(cargoTanksSbdSideGridPane);
	}
	
	public void addSlopTanksPortSide() {
		addTank(slopTanksPortSideGridPane);
	}
	public void addSlopTanksCenter() {
		addTank(slopTanksCenterGridPane);
	}
	public void addSlopTanksSbdSide() {
		addTank(slopTanksSbdSideGridPane);
	}
	
	public void addFuelOilTanksPortSide() {
		addTank(fuelOilTanksPortSideGridPane);
	}
	public void addFuelOilTanksCenter1() {
		addTank(fuelOilTanksCenter1GridPane);
	}
	public void addFuelOilTanksCenter2() {
		addTank(fuelOilTanksCenter2GridPane);
	}
	public void addFuelOilTanksSbdSide() {
		addTank(fuelOilTanksSbdSideGridPane);
	}
	
	public void addOilBilgeTanksPortSide() {
		addTank(oilBilgeTanksPortSideGridPane);
	}
	public void addOilBilgeTanksCenter1() {
		addTank(oilBilgeTanksCenter1GridPane);
	}
	public void addOilBilgeTanksCenter2() {
		addTank(oilBilgeTanksCenter2GridPane);
	}
	public void addOilBilgeTanksSbdSide() {
		addTank(oilBilgeTanksSbdSideGridPane);
	}
	
	public void addSludgeTanksPortSide() {
		addTank(sludgeTanksPortSideGridPane);
	}
	public void addSludgeTanksCenter1() {
		addTank(sludgeTanksCenter1GridPane);
	}
	public void addSludgeTanksCenter2() {
		addTank(sludgeTanksCenter2GridPane);
	}
	public void addSludgeTanksSbdSide() {
		addTank(sludgeTanksSbdSideGridPane);
	}
	
//	public void addMgoTanksPortSide() {
//		addTank(mgoTanksPortSideGridPane);
//	}
//	public void addMgoTanksCenter() {
//		addTank(mgoTanksCenterGridPane);
//	}
//	public void addMgoTanksSbdSide() {
//		addTank(mgoTanksSbdSideGridPane);
//	}
	
	public void addBallastTanksPortSide() {
		addTank(ballastTanksPortSideGridPane);
	}
	public void addBallastTanksForePeak() {
		addTank(ballastTanksForePeakGridPane);
	}
	public void addBallastTanksAftPeak() {
		addTank(ballastTanksAftPeakGridPane);
	}
	public void addBallastTanksSbdSide() {
		addTank(ballastTanksSbdSideGridPane);
	}
	
	public void addTank(GridPane tankGridPane) {
		TextField tankIdentifier = new TextField();
		tankIdentifier.setTextFormatter(JavaFXUtils.getUpperCaseTextFormatter());
		DoubleField tankVolume = new DoubleField();
		DoubleField actualTankLevel = new DoubleField();
		MathUtils.setStringConverter(tankVolume);
		MathUtils.setStringConverter(actualTankLevel);
		MathUtils.setMinMaxValues(tankVolume, 0.0, null);
		MathUtils.setMinMaxValues(actualTankLevel, 0.0, null);
		tankVolume.valueProperty().addListener(new ChangeListener<Double>() {
			@Override
			public void changed(ObservableValue<? extends Double> observable, Double oldValue, Double newValue) {
				if(actualTankLevel.getValue()!=null && actualTankLevel.getValue()>newValue) actualTankLevel.setValue(newValue);
				MathUtils.setMinMaxValues(actualTankLevel, 0.0, newValue);
			}
		});
		Button deleteButton = new Button("X");
		deleteButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Button deleteButton = (Button)event.getSource();
				GridPane parent = (GridPane)deleteButton.getParent();
				Integer rowIndex = GridPane.getRowIndex(deleteButton);
				parent.getChildren().removeIf(node -> GridPane.getRowIndex(node) == rowIndex);
			}
		});
		Integer rows = null;
		try {
			Method method = tankGridPane.getClass().getDeclaredMethod("getNumberOfRows");
			method.setAccessible(true);
			rows = (Integer) method.invoke(tankGridPane);
		} catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		}
		tankVolume.setId("tankVolume" + rows);
		actualTankLevel.setId("actualTankLevel" + rows);
		tankIdentifier.getStyleClass().add("logbook-text-field");
		actualTankLevel.getStyleClass().add("logbook-text-field");
		tankVolume.getStyleClass().add("logbook-text-field");
		JavaFXUtils.addFilledPseudoClass(tankIdentifier);
		JavaFXUtils.addFilledPseudoClass(actualTankLevel);
		JavaFXUtils.addFilledPseudoClass(tankVolume);
		tankGridPane.addRow(rows, tankIdentifier, actualTankLevel, tankVolume, deleteButton);
	}
	
	@FXML
	private void save(ActionEvent event) {
		MarpleLogSettings settings = marpolBean.getSettings();
		
		List<Integer> cargoTanksOld = settings.getCargoTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> fuelOilTanksOld = settings.getFuelOilTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> mgoTanksOld = settings.getMgoTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> oilyBilgeTanksOld = settings.getOilyBilgeTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> slopTanksOld = settings.getSlopTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> sludgeTanksOld = settings.getSludgeTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		List<Integer> ballastTanksOld = settings.getBallastTanks().stream().map(tank -> tank.changeTrackHashCode()).collect(Collectors.toList());
		
		Map<Long, String> idListOld = settings.getCargoTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName));
		idListOld.putAll(settings.getFuelOilTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		idListOld.putAll(settings.getMgoTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		idListOld.putAll(settings.getOilyBilgeTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		idListOld.putAll(settings.getSlopTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		idListOld.putAll(settings.getSludgeTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		idListOld.putAll(settings.getBallastTanks().stream().collect(Collectors.toMap(Tank::getId, Tank::getName)));
		
		settings.setCargoTanks(new ArrayList<Tank>());
		settings.setFuelOilTanks(new ArrayList<Tank>());
		settings.setMgoTanks(new ArrayList<Tank>());
		settings.setOilyBilgeTanks(new ArrayList<Tank>());
		settings.setSlopTanks(new ArrayList<Tank>());
		settings.setSludgeTanks(new ArrayList<Tank>());
		settings.setBallastTanks(new ArrayList<Tank>());
		
		
		SortedMap<Integer, Tank> cargoPortSideTanks = getTanks(cargoTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> cargoCenterTanks = getTanks(cargoTanksCenterGridPane, TankPosition.CENTER);
		SortedMap<Integer, Tank> cargoSbdSideTanks = getTanks(cargoTanksSbdSideGridPane, TankPosition.SBDSIDE);
		cargoPortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsCargo(settings));
		cargoCenterTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsCargo(settings));
		cargoSbdSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsCargo(settings));
		settings.getCargoTanks().addAll(cargoPortSideTanks.values());
		settings.getCargoTanks().addAll(cargoCenterTanks.values());
		settings.getCargoTanks().addAll(cargoSbdSideTanks.values());
		
		SortedMap<Integer, Tank> fuelOilPortSideTanks = getTanks(fuelOilTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> fuelOilCenter1Tanks = getTanks(fuelOilTanksCenter1GridPane, TankPosition.CENTER1);
		SortedMap<Integer, Tank> fuelOilCenter2Tanks = getTanks(fuelOilTanksCenter2GridPane, TankPosition.CENTER2);
		SortedMap<Integer, Tank> fuelOilSbdSideTanks = getTanks(fuelOilTanksSbdSideGridPane, TankPosition.SBDSIDE);
		fuelOilPortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsFuel(settings));
		fuelOilCenter1Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsFuel(settings));
		fuelOilCenter2Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsFuel(settings));
		fuelOilSbdSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsFuel(settings));
		settings.getFuelOilTanks().addAll(fuelOilPortSideTanks.values());
		settings.getFuelOilTanks().addAll(fuelOilCenter1Tanks.values());
		settings.getFuelOilTanks().addAll(fuelOilCenter2Tanks.values());
		settings.getFuelOilTanks().addAll(fuelOilSbdSideTanks.values());
		
//		tankList = new ArrayList<Tank>();
//		tankList.addAll(getTanks(mgoTanksPortSideGridPane, TankPosition.PORTSIDE));
//		tankList.addAll(getTanks(mgoTanksCenterGridPane, TankPosition.CENTER));
//		tankList.addAll(getTanks(mgoTanksSbdSideGridPane, TankPosition.SBDSIDE));
//		tankList.stream().forEach(tank -> tank.setMarpleLogSettingsMgo(settings));
//		settings.getMgoTanks().addAll(tankList);
		
		SortedMap<Integer, Tank> oilBilgePortSideTanks = getTanks(oilBilgeTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> oilBilgeCenter1Tanks = getTanks(oilBilgeTanksCenter1GridPane, TankPosition.CENTER1);
		SortedMap<Integer, Tank> oilBilgeCenter2Tanks = getTanks(oilBilgeTanksCenter2GridPane, TankPosition.CENTER2);
		SortedMap<Integer, Tank> oilBilgeSbdTanks = getTanks(oilBilgeTanksSbdSideGridPane, TankPosition.SBDSIDE);
		oilBilgePortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBilge(settings));
		oilBilgeCenter1Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBilge(settings));
		oilBilgeCenter2Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBilge(settings));
		oilBilgeSbdTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBilge(settings));
		settings.getOilyBilgeTanks().addAll(oilBilgePortSideTanks.values());
		settings.getOilyBilgeTanks().addAll(oilBilgeCenter1Tanks.values());
		settings.getOilyBilgeTanks().addAll(oilBilgeCenter2Tanks.values());
		settings.getOilyBilgeTanks().addAll(oilBilgeSbdTanks.values());
		
		SortedMap<Integer, Tank> slopPortSideTanks = getTanks(slopTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> slopCenterTanks = getTanks(slopTanksCenterGridPane, TankPosition.CENTER);
		SortedMap<Integer, Tank> slopSbdSideTanks = getTanks(slopTanksSbdSideGridPane, TankPosition.SBDSIDE);
		slopPortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSlop(settings));
		slopCenterTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSlop(settings));
		slopSbdSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSlop(settings));
		settings.getSlopTanks().addAll(slopPortSideTanks.values());
		settings.getSlopTanks().addAll(slopCenterTanks.values());
		settings.getSlopTanks().addAll(slopSbdSideTanks.values());
		
		SortedMap<Integer, Tank> sludgePortSideTanks = getTanks(sludgeTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> sludgeCenter1Tanks = getTanks(sludgeTanksCenter1GridPane, TankPosition.CENTER1);
		SortedMap<Integer, Tank> sludgeCenter2Tanks = getTanks(sludgeTanksCenter2GridPane, TankPosition.CENTER2);
		SortedMap<Integer, Tank> sludgeSbdSideTanks = getTanks(sludgeTanksSbdSideGridPane, TankPosition.SBDSIDE);
		sludgePortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSludge(settings));
		sludgeCenter1Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSludge(settings));
		sludgeCenter2Tanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSludge(settings));
		sludgeSbdSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsSludge(settings));
		settings.getSludgeTanks().addAll(sludgePortSideTanks.values());
		settings.getSludgeTanks().addAll(sludgeCenter1Tanks.values());
		settings.getSludgeTanks().addAll(sludgeCenter2Tanks.values());
		settings.getSludgeTanks().addAll(sludgeSbdSideTanks.values());
		
		
		SortedMap<Integer, Tank> ballastPortSideTanks = getTanks(ballastTanksPortSideGridPane, TankPosition.PORTSIDE);
		SortedMap<Integer, Tank> ballastForePeakTanks = getTanks(ballastTanksForePeakGridPane, TankPosition.FORE_PEAK);
		SortedMap<Integer, Tank> ballastAftPeakTanks = getTanks(ballastTanksAftPeakGridPane, TankPosition.AFT_PEAK);
		SortedMap<Integer, Tank> ballastSbdSideTanks = getTanks(ballastTanksSbdSideGridPane, TankPosition.SBDSIDE);
		ballastPortSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBallast(settings));
		ballastForePeakTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBallast(settings));
		ballastAftPeakTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBallast(settings));
		ballastSbdSideTanks.values().stream().forEach(tank -> tank.setMarpleLogSettingsBallast(settings));
		settings.getBallastTanks().addAll(ballastPortSideTanks.values());
		settings.getBallastTanks().addAll(ballastForePeakTanks.values());
		settings.getBallastTanks().addAll(ballastAftPeakTanks.values());
		settings.getBallastTanks().addAll(ballastSbdSideTanks.values());
		
    	settings.setTotalSizeBilgeTanks(getTotalBilgeVolume(settings));
    	settings.setTotalSizeCargoTanks(getTotalCargoVolume(settings));
    	settings.setTotalSizeFuelOilTanks(getTotalFuelVolume(settings));
    	settings.setTotalSizeMgoTanks(getTotalMgoVolume(settings));
    	settings.setTotalSizeSlopTanks(getTotalSlopVolume(settings));
    	settings.setTotalSizeSludgeTanks(getTotalSludgeVolume(settings));
    	settings.setTotalSizeBallastTanks(getTotalBallastVolume(settings));

    	settings.setIncineratorCapacity(inceneratorCapacityDoubleField.getValue());
    	settings.setMixingOilCapacity(oilFuelOilResiduesDoubleField.getValue());
    	settings.setBilgeWaterSeparatorThroughput(bilgeWaterSeparatorThroughputDoubleField.getValue());
//    	settings.setOtherMeans(otherMeansTextField.getText());
		
    	if(settings.getShip()==null) {
    		settings.setShip(new Ship());
    	}
    	Ship ship = settings.getShip();
    	setShipValues(ship);
    	
    	// gets the id of the tanks modified.
    	// New tanks added have id==null and are not included (.filter(tank -> tank.getId()!=null)) to be fixed for correct logical behavior
    	List<Tank> updatedTanks = new ArrayList<Tank>();
    	updatedTanks.addAll(settings.getCargoTanks().stream().filter(tank -> !cargoTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getFuelOilTanks().stream().filter(tank -> !fuelOilTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getMgoTanks().stream().filter(tank -> !mgoTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getOilyBilgeTanks().stream().filter(tank -> !oilyBilgeTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getSlopTanks().stream().filter(tank -> !slopTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getSludgeTanks().stream().filter(tank -> !sludgeTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	updatedTanks.addAll(settings.getBallastTanks().stream().filter(tank -> !ballastTanksOld.contains(tank.changeTrackHashCode())).collect(Collectors.toList()));
    	List<Long> updatedTankIds = updatedTanks.stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList());
    	List<String> updatedTankNames = updatedTanks.stream().filter(tank -> tank.getId()!=null).map(tank -> tank.getName()).collect(Collectors.toList());
    	List<String> addedTankNames = updatedTanks.stream().filter(tank -> tank.getId()==null).map(tank -> tank.getName()).collect(Collectors.toList());
    	
    	idListOld.keySet().removeAll(settings.getCargoTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getFuelOilTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getMgoTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getOilyBilgeTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getSlopTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getSludgeTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	idListOld.keySet().removeAll(settings.getBallastTanks().stream().filter(tank -> tank.getId()!=null).mapToLong(tank -> tank.getId()).boxed().collect(Collectors.toList()));
    	updatedTankIds.addAll(idListOld.keySet());
    	
		marpolBean.saveSettings(addedTankNames, updatedTankNames, idListOld.values());

		updateTotalLabels(settings);
		

		Map<String, List<Long>> idsParameter = new HashMap<String, List<Long>>();
		idsParameter.put(Activator.TANK_IDS, updatedTankIds);
		remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.MARPOL_DETAILS_CHANGED, idsParameter));
		
		MarpolDetailController.showInformation("Save Marpol Details", "Marpol Details updated successfully.");
	}





	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }

	private SortedMap<Integer, Tank> getTanks(GridPane tanksGridPane, TankPosition tankPosition) {
		TreeMap<Integer, Tank> oldTankMap = (TreeMap<Integer, Tank>) tanksGridPane.getUserData();
		Map<Integer, List<Node>> tankNodesMap = new TreeMap<Integer, List<Node>>();
		for(Node childNode : tanksGridPane.getChildren()) {
			if(GridPane.getRowIndex(childNode) != null) {
				if(tankNodesMap.get(GridPane.getRowIndex(childNode)) != null) {
					tankNodesMap.get(GridPane.getRowIndex(childNode)).add(childNode);
				} else {
					List<Node> nodeList = new ArrayList<Node>();
					nodeList.add(childNode);
					tankNodesMap.put(GridPane.getRowIndex(childNode), nodeList);
				}
			}
		}
		TreeMap<Integer, Tank> tankMap = new TreeMap<Integer, Tank>();
		Tank tank = null;
		for(Integer key : tankNodesMap.keySet()) {
			List<Node> tankNodeList = tankNodesMap.get(key);
			for(Node node : tankNodeList) {
				if(node instanceof TextField && !(node instanceof DoubleField)) {
					tank = oldTankMap.get(key)!=null ? oldTankMap.get(key) : new Tank();
					tank.setTankPosition(tankPosition);
					tank.setName(((TextField)node).getText());
				}
				if(node.getId()!=null && node.getId().startsWith("tankVolume")) {
					tank.setSize(((DoubleField)node).getValue());
					tank.setSizeMeasureUnit("cm");
				}
				if(node.getId()!=null && node.getId().startsWith("actualTankLevel")) {
					tank.setActualLevel(((DoubleField)node).getValue());
				}
			}
			if(tank!=null && tank.getName()!=null && !tank.getName().equals("") && tank.getSize()!=null) tankMap.put(key, tank);
		}
		return tankMap;
	}

	private void updateTotalLabels(MarpleLogSettings settings) {
		totalVolumeofCargoTanksField.setValue(getTotalCargoVolume(settings));
		totalVolumeofFuelTanksField.setValue(getTotalFuelVolume(settings));
//		totalVolumeofMgoTanksField.setText(getTotalMgoVolume(settings) + "");
		totalVolumeofOilBilgeTanksField.setValue(getTotalBilgeVolume(settings));
		totalVolumeofSlopTanksField.setValue(getTotalSlopVolume(settings));
		totalVolumeofSludgeTanksField.setValue(getTotalSludgeVolume(settings));
		totalVolumeofBallastTanksField.setValue(getTotalBallastVolume(settings));
	}
	
    public Double getTotalCargoVolume(MarpleLogSettings settings) {
    	if (settings.getCargoTanks()!=null && settings.getCargoTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getCargoTanks()) {
				if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalSlopVolume(MarpleLogSettings settings) {
    	if (settings.getSlopTanks()!=null && settings.getSlopTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getSlopTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalFuelVolume(MarpleLogSettings settings) {
    	if (settings.getFuelOilTanks()!=null && settings.getFuelOilTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getFuelOilTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalMgoVolume(MarpleLogSettings settings) {
    	if (settings.getMgoTanks()!=null && settings.getMgoTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getMgoTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalBilgeVolume(MarpleLogSettings settings) {
    	if (settings.getOilyBilgeTanks()!=null && settings.getOilyBilgeTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getOilyBilgeTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalSludgeVolume(MarpleLogSettings settings) {
    	if (settings.getSludgeTanks()!=null && settings.getSludgeTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getSludgeTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    public Double getTotalBallastVolume(MarpleLogSettings settings) {
    	if (settings.getBallastTanks()!=null && settings.getBallastTanks().size()>0) {
    		double tot =0.0;
    		for (Tank t : settings.getBallastTanks()) {
    			if (t.getSize()!=null) tot+=t.getSize();
			}
    		return tot;
    	}
    	else return 0.0;
    }
    
    
    
    
    
	private void initShipSettings(Ship ship) {
		shipImoNumberTextField.setText(ship.getNumeroIMO());
		shipNameTextField.setText(ship.getName());
		shipDistinctiveNumberTextField.setText(ship.getShipDistinctiveNumber());
		shipPortOfRegistryTextField.setText(ship.getShipPortOfRegistry());
		shipGrossTonnageTextField.setText(ship.getShipGrossTonnage());
		shipCarringCapacityDoubleField.setValue(ship.getShipCarringCapacity());
		shipDeadweightDoubleField.setValue(ship.getShipDeadweight());
		shipLengthDoubleField.setValue(ship.getShipLength());
		if(ship.getShipBuildingContractDate()!=null) shipBuildingContractDate.setLocalDate(Instant.ofEpochMilli(ship.getShipBuildingContractDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		if(ship.getShipKeelLaidDate()!=null) shipKeelLaidDate.setLocalDate(Instant.ofEpochMilli(ship.getShipKeelLaidDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
		if(ship.getShipDeliveryDate()!=null) shipDeliveryDate.setLocalDate(Instant.ofEpochMilli(ship.getShipDeliveryDate().getTime()).atZone(ZoneId.systemDefault()).toLocalDate());
	}
    
	private void setShipValues(Ship ship) {
		ship.setNumeroIMO(shipImoNumberTextField.getText());
		ship.setName(shipNameTextField.getText());
		ship.setShipDistinctiveNumber(shipDistinctiveNumberTextField.getText());
		ship.setShipPortOfRegistry(shipPortOfRegistryTextField.getText());
		ship.setShipGrossTonnage(shipGrossTonnageTextField.getText());
		ship.setShipCarringCapacity(shipCarringCapacityDoubleField.getValue());
		ship.setShipDeadweight(shipDeadweightDoubleField.getValue());
		ship.setShipLength(shipLengthDoubleField.getValue());
		if(shipBuildingContractDate.getLocalDate()!=null) ship.setShipBuildingContractDate(Date.from(shipBuildingContractDate.getLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		if(shipKeelLaidDate.getLocalDate()!=null) ship.setShipKeelLaidDate(Date.from(shipKeelLaidDate.getLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
		if(shipDeliveryDate.getLocalDate()!=null) ship.setShipDeliveryDate(Date.from(shipDeliveryDate.getLocalDate().atStartOfDay(ZoneId.systemDefault()).toInstant()));
	}
    
    
    public void handleReturnToOrb() {
    	viewPerspectiveHelper.showView("al.sybina.ebr.logbook.ui.part.oilrecordbook1");
    }
}
