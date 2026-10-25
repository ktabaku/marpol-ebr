package al.sybina.ebr.rcp.handlers;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.dlsc.preferencesfx.PreferencesFx;
import com.google.gson.internal.LinkedTreeMap;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.opcoach.e4.preferences.E4PreferenceRegistry;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.s63.License3j;
import al.sybina.ebr.s63.preferences.InstallPermitPreferencePage;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

// switcher perspectives based on a command parameter 
public class PerspectiveSwitchHandler {
	
	private static Logger logger = LoggerCreator.createLogger(PerspectiveSwitchHandler.class);
	private static JFXButton closeButton;
	@Inject private static ThemeManager themeManager;
	@Inject private E4PreferenceRegistry prefReg;
	@Inject private InstallPermitPreferencePage installPermitPreferencePage;
	
	
	public PerspectiveSwitchHandler() {
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
	}
	
	@Execute
	public void switchPerspective(
			MWindow window, 
			EPartService partService,
			EModelService modelService,
			@Named("org.eclipse.ui.perspectives.showPerspective.perspectiveId") String perspectiveId) {
		// use parameter to find perspectives
		List<MPerspective> perspectives = modelService.findElements(window,
				perspectiveId, MPerspective.class, null);


		if (!perspectives.isEmpty()) {

			boolean installPermitValid = false;
			PreferencesFx preferencesFx = prefReg.getPreferencesFx();
			try {
				Object fileObject = installPermitPreferencePage.getLicenseFileObj().getValue();
				if(fileObject instanceof File) {
					switch(perspectiveId) {
						case "al.sybina.ebr.logbook.ui.perspective.logbook" :
							installPermitValid = License3j.validateLogbook1License((File)fileObject);
							break;
						case "al.sybina.ebr.logbook.ui.perspective.logbook2":
							installPermitValid = License3j.validateLogbook2License((File)fileObject);
							break;
						case "al.sybina.ebr.marpol.ui.perspective.Marpol":
							installPermitValid = License3j.validateMarpolLicense((File)fileObject);
							break;
						case "al.sybina.ebr.nav.ui.perspective.Navtex":
							installPermitValid = License3j.validateNavigationWarningsLicense((File)fileObject);
							break;
						case "al.sybina.ebr.echart.ui.perspective.EChart":
							installPermitValid = License3j.validateEchartLicense((File)fileObject);
							break;
						case "al.sybina.ebr.perspectives.home":
							installPermitValid = true;
							break;
					}
				}
				else if(fileObject instanceof LinkedTreeMap) {
					String path = (String)((LinkedTreeMap)fileObject).get("path");
					switch(perspectiveId) {
						case "al.sybina.ebr.logbook.ui.perspective.logbook" :
							installPermitValid = License3j.validateLogbook1License(new File(path));
							break;
						case "al.sybina.ebr.logbook.ui.perspective.logbook2":
							installPermitValid = License3j.validateLogbook2License(new File(path));
							break;
						case "al.sybina.ebr.marpol.ui.perspective.Marpol":
							installPermitValid = License3j.validateMarpolLicense(new File(path));
							break;
						case "al.sybina.ebr.nav.ui.perspective.Navtex":
							installPermitValid = License3j.validateNavigationWarningsLicense(new File(path));
							break;
						case "al.sybina.ebr.echart.ui.perspective.EChart":
							installPermitValid = License3j.validateEchartLicense(new File(path));
							break;
						case "al.sybina.ebr.perspectives.home":
							installPermitValid = true;
							break;
					}
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
				if(nowDate.after(PerspectiveSwitchHandler.addDays(date, 90))) {
					Alert alert = new Alert(AlertType.INFORMATION);
			        alert.setTitle("Info");
			        alert.setHeaderText("Invalid license");
			        alert.setContentText("You don't have an valid install permit.\nPlease enter the install permit in Window -> Preferences.");
			        JavaFXUtils.undecorate(alert, closeButton, themeManager);
			        alert.showAndWait();
			        return;
				} else {
					logger.info("Demo version!");
				}
			}
			
			// switch to perspective with the ID if found
			partService.switchPerspective(perspectives.get(0));
		}
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
