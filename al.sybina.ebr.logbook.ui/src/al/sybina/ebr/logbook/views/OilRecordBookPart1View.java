package al.sybina.ebr.logbook.views;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.di.InjectingFXMLLoader;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.dlsc.preferencesfx.PreferencesFx;
import com.google.gson.internal.LinkedTreeMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.opcoach.e4.preferences.E4PreferenceRegistry;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.logbook.core.services.DerbyDatabaseBackupService;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.login.Activator;
import al.sybina.ebr.login.Login;
import al.sybina.ebr.s63.License3j;
import al.sybina.ebr.s63.preferences.InstallPermitPreferencePage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class OilRecordBookPart1View {
	
	private static Logger logger = LoggerCreator.createLogger(OilRecordBookPart1View.class);
	@Inject private InstallPermitPreferencePage installPermitPreferencePage;
	@Inject private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject private E4PreferenceRegistry prefReg;
	@Inject private IEventBroker eventBroker;
	@Inject private static ThemeManager themeManager;
	@Inject private Login login;
	@Inject private DerbyDatabaseBackupService derbyDatabaseBackupService;
	@Inject private OperationLogReladomoService operationLogReladomoService;
	
	private static JFXButton closeButton;
	
	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader, IEclipseContext context, Stage stage) {
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
		boolean installPermitValid = false;
		PreferencesFx preferencesFx = prefReg.getPreferencesFx();
		try {
			Object fileObject = installPermitPreferencePage.getLicenseFileObj().getValue();
			if(fileObject instanceof File) installPermitValid = License3j.validateLogbook1License((File)fileObject);
			else if(fileObject instanceof LinkedTreeMap) {
				String path = (String)((LinkedTreeMap)fileObject).get("path");
				installPermitValid = License3j.validateLogbook1License(new File(path));
			}
		} catch(Exception ex) {
			logger.debug("Error validating installPermit. " + ex.getMessage());
			installPermitValid = false;
		}
		if(!installPermitValid) {
			String str_date = "01-06-2041";
			DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
			Date nowDate = new Date();
			Date date = null;
			try {
				date = formatter.parse(str_date);
			} catch (ParseException e) {
				logger.error("Never happens");
				e.printStackTrace();
			}
			if(nowDate.after(OilRecordBookPart1View.addDays(date, 90))) {
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Info");
		        alert.setHeaderText("Invalid license");
		        alert.setContentText("You don't have an valid install permit.\nPlease enter the install permit in Window -> Preferences.");
		        JavaFXUtils.undecorate(alert, closeButton, themeManager);
		        alert.showAndWait();
		        viewPerspectiveHelper.goToPerspective("al.sybina.ebr.perspectives.home");
		        return;
			} else {
				logger.info("Demo version!");
			}
		}
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_MASTER_SIGN)) {
					final Map<String, List<OilLog>> logsPart1WithoutMasterSignature = operationLogReladomoService.getValidOil1LogNoMasterSignature(al.sybina.ebr.logbook.commons.Activator.OIL_LOG_TYPE_1);	
					if(logsPart1WithoutMasterSignature.size()>0) {
						LocalDateTime oneWeekBeforeNowUTC = ZonedDateTime.now(ZoneOffset.UTC).minusWeeks(1).toLocalDateTime();
						final List<List<OilLog>> logsWithoutMasterSignatureOlderThanOneWeek = logsPart1WithoutMasterSignature.values().stream()
						        .filter(myList -> myList.get(0).getProcessingDateFrom().before(Timestamp.valueOf(oneWeekBeforeNowUTC)))
						        .collect(Collectors.toList());
						if(logsWithoutMasterSignatureOlderThanOneWeek.size()>0 && logsPart1WithoutMasterSignature.size()<3) {
							showInformation("Part 1 Master Logs Signature", "There is/are recording more than one week old that need to be validated.");
						} else if(logsWithoutMasterSignatureOlderThanOneWeek.size()==0 && logsPart1WithoutMasterSignature.size()>2) {
							showInformation("Part 1 Master Logs Signature", "There are three or more recordings that need to be validated.");
						} else if(logsWithoutMasterSignatureOlderThanOneWeek.size()>0 && logsPart1WithoutMasterSignature.size()>2) {
							showInformation("Part 1 Master Logs Signature", "There is/are recording more than one week old that need to be validated and there are three or more recordings that need to be validated.");
						}
					}
				}
			}
		});
		if(!SecurityUtils.getSubject().isAuthenticated()) {
			boolean loggedIn = eventBroker.send(Activator.LOGIN_NEEDED_EVENT_TOPIC, new HashMap<String, String>());
			if(!loggedIn) return;
		} else {
			new Thread(() -> {
				try {
					while (loader.getRoot()==null) {
						Thread.sleep(100);
					}
				} catch (InterruptedException e) {
				}
				eventBroker.post(Activator.LOGGED_IN_EVENT_TOPIC, new HashMap<String, String>());
			}).start();
		}
		
		AnchorPane mainAnchorPane = null;
		TabPane mainTabPane = null;
		OilRecordBookPart1ViewController oilRecordBookPart1ViewController = null;
		loader.setLocation(getClass().getResource("OilRecordBookPart1.fxml"));
		try {
			mainAnchorPane = loader.load();
			mainTabPane = (TabPane) mainAnchorPane.lookup("#mainLogbook1TabPane");
			borderPane.setCenter(mainAnchorPane);
			oilRecordBookPart1ViewController = loader.getController();
		} catch (IOException e) {
			e.printStackTrace();
		}

		InjectingFXMLLoader<Node> tabsLoader = InjectingFXMLLoader.create(context, getClass(),
				"OilRecordBookPart1CodeLetterA.fxml");
		try {
			TabPane tabPaneA = (TabPane) tabsLoader.load();
			Tab tabA = tabPaneA.getTabs().get(0);
			mainTabPane.getTabs().add(tabA);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterAController(
					context.getLocal(OilRecordBookPart1CodeLetterAController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterB.fxml");
		try {
			TabPane tabPaneB = (TabPane) tabsLoader.load();
			Tab tabB = tabPaneB.getTabs().get(0);
			mainTabPane.getTabs().add(tabB);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterBController(
					context.getLocal(OilRecordBookPart1CodeLetterBController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterC.fxml");
		try {
			TabPane tabPaneC = (TabPane) tabsLoader.load();
			Tab tabC = tabPaneC.getTabs().get(0);
			mainTabPane.getTabs().add(tabC);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterCController(
					context.getLocal(OilRecordBookPart1CodeLetterCController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}

		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterD.fxml");
		try {
			TabPane tabPaneD = (TabPane) tabsLoader.load();
			Tab tabD = tabPaneD.getTabs().get(0);
			mainTabPane.getTabs().add(tabD);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterDController(
					context.getLocal(OilRecordBookPart1CodeLetterDController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterE.fxml");
		try {
			TabPane tabPaneE = (TabPane) tabsLoader.load();
			Tab tabE = tabPaneE.getTabs().get(0);
			mainTabPane.getTabs().add(tabE);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterEController(
					context.getLocal(OilRecordBookPart1CodeLetterEController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterF.fxml");
		try {
			TabPane tabPaneF = (TabPane) tabsLoader.load();
			Tab tabF = tabPaneF.getTabs().get(0);
			mainTabPane.getTabs().add(tabF);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterFController(
					context.getLocal(OilRecordBookPart1CodeLetterFController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterG.fxml");
		try {
			TabPane tabPaneG = (TabPane) tabsLoader.load();
			Tab tabG = tabPaneG.getTabs().get(0);
			mainTabPane.getTabs().add(tabG);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterGController(
					context.getLocal(OilRecordBookPart1CodeLetterGController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterH.fxml");
		try {
			TabPane tabPaneH = (TabPane) tabsLoader.load();
			Tab tabH = tabPaneH.getTabs().get(0);
			mainTabPane.getTabs().add(tabH);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterHController(
					context.getLocal(OilRecordBookPart1CodeLetterHController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		tabsLoader = InjectingFXMLLoader.create(context, getClass(), "OilRecordBookPart1CodeLetterI.fxml");
		try {
			TabPane tabPaneI = (TabPane) tabsLoader.load();
			Tab tabI = tabPaneI.getTabs().get(0);
			mainTabPane.getTabs().add(tabI);
			oilRecordBookPart1ViewController.setOilRecordBookPart1CodeLetterIController(
					context.getLocal(OilRecordBookPart1CodeLetterIController.class));
		} catch (IOException e) {
			e.printStackTrace();
		}
		for(Tab tab : mainTabPane.getTabs()) {
			mainTabPane.getSelectionModel().select(tab);
		}
		mainTabPane.getSelectionModel().select(0);

		context.getParent().set(OilRecordBookPart1ViewController.class, oilRecordBookPart1ViewController);
		oilRecordBookPart1ViewController.setOilRecordBookPart1ViewControllerToCodeLetterI(context.getLocal(OilRecordBookPart1ViewController.class));
	}

    public static Date addDays(Date origDate, int daysToAdd) {
        Calendar cal = GregorianCalendar.getInstance();
        cal.setFirstDayOfWeek(2);
        cal.setMinimalDaysInFirstWeek(4);
        cal.setTime(origDate);
        cal.add(5, daysToAdd);
        return cal.getTime();
    }
    
	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initModality(Modality.WINDOW_MODAL);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
}