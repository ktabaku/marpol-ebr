package al.sybina.ebr.logbook.views.audit;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.log4j.configuration.BindableValue;
import al.sybina.ebr.log4j.configuration.Dialog;
import al.sybina.ebr.log4j.configuration.HighlightExpression;
import al.sybina.ebr.log4j.configuration.HighlightOptions;
import al.sybina.ebr.log4j.configuration.LogLineColors;
import al.sybina.ebr.log4j.configuration.LogView;
import al.sybina.ebr.log4j.configuration.LogViewPane;
import al.sybina.ebr.log4j.configuration.concurrent.TaskRunner;
import al.sybina.ebr.log4j.configuration.file.FileContentReader;
import al.sybina.ebr.log4j.configuration.file.FileReader;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class AuditController implements Initializable {


	private static final Logger logger = LoggerCreator.createLogger(AuditController.class);
	
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
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	
	@FXML
	private VBox mainVBox;
	@FXML
	private AnchorPane anchorPane;

	private MPart part;
	private FileContentReader fileReader = null;
	
	private static JFXButton closeButton;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		
		
    	final SimpleObjectProperty<LogLineColors> standardLogColors = new SimpleObjectProperty<>( new LogLineColors( Color.WHITE, Color.BLACK ) );
    	final ObservableList<HighlightExpression> observableExpressions = FXCollections.observableArrayList();
    	final SimpleBooleanProperty enableFilters = new SimpleBooleanProperty( false );
        HighlightOptions highlightOptions = new HighlightOptions(
                standardLogColors,
                observableExpressions,
                enableFilters);
        TaskRunner taskRunner = new TaskRunner( false );
        
        
        
        try {
            fileReader = new FileReader( new File(System.getProperty("logdir") + "/audit.log"), LogView.MAX_LINES );
        } catch ( IllegalStateException e ) {
            Dialog.showMessage( e.getMessage(), Dialog.MessageLevel.ERROR );
        }

        LogView view = new LogView( new BindableValue<>( Font.font( "Courier New" ) ), mainVBox.widthProperty(),
                highlightOptions, fileReader, taskRunner );
        LogViewPane logsPane = new LogViewPane();
        logsPane.add( view);
        
		mainVBox.getChildren().add(logsPane.getNode());
	}



//	@Inject
//	@Optional
//	public void handleBringToTop(@UIEventTopic(UIEvents.UILifeCycle.BRINGTOTOP) Event event, MPart thisPart) {
//		if (thisPart == event.getProperty(UIEvents.EventTags.ELEMENT)) {
//			fileReader.resumeWatching();
//		} else {
//			fileReader.pauseWatching();
//		}
//	}

	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }



    
    
    public void handleReturnToOrb() {
    	viewPerspectiveHelper.showView("al.sybina.ebr.logbook.ui.part.oilrecordbook1");
    }





	public void setPart(MPart thisPart) {
		this.part = thisPart;
	}
}
