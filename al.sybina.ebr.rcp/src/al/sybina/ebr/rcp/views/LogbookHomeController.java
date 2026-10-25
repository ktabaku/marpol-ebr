package al.sybina.ebr.rcp.views;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.core.commands.ParameterizedCommand;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.tools.resources.WorkbenchResourceProvider;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Light;
import javafx.scene.effect.Lighting;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import jfxtras.scene.control.ImageViewButton;
import jfxtras.scene.layout.CircularPane;

public class LogbookHomeController  implements Initializable, ControlledScreen {
	
	private static final Logger logger = LoggerCreator.createLogger(LogbookHomeController.class);
	
	@FXML private AnchorPane mainPane;
	@FXML private ImageView centerImgView;
	@FXML private ImageView titleLogbookImgView;
	@FXML private ImageView companyLogoImgView;
	@FXML private ImageView homeNewImgView;
	@FXML private ImageViewButton alertNewImgViewButton;
	@FXML private CircularPane iconsCircularPane;
	@FXML private ImageViewButton logBook1ImgViewButton;
	@FXML private ImageViewButton logBook2ImgViewButton;
	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private IEclipseContext ctx;
	
	private ScreensController myController;
    
    public void setScreenParent(ScreensController screenParent){
        myController = screenParent;
    }
    @FXML
    private void goToAlert(MouseEvent event){
    	goToPerspective("al.sybina.ebr.perspectives.nws");
    }
    @FXML
    private void goToLogbook1(MouseEvent event){
    	goToPerspective("al.sybina.ebr.logbook.ui.perspective.logbook");
    }
    @FXML
    private void goToLogbook2(MouseEvent event){
    	goToPerspective("al.sybina.ebr.logbook.ui.perspective.logbook2");
    }

    @FXML
    private void goToLClose(MouseEvent event){
       exit();
    }
    
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			Image backgroundImage = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_TEXTURE));
			mainPane.setBackground(new Background(new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, new BackgroundSize(100,100,true, true, true, true))));
			
			Image titleLogbook = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_TITLEMARPOLORB));
			titleLogbookImgView.setImage(titleLogbook);
			titleLogbookImgView.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(40));
			
			Image companyLogo = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_COMPANYLOGO));
			companyLogoImgView.setImage(companyLogo);
			companyLogoImgView.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(40));
			
			Image centerImage = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_CENTER));
			centerImgView.setImage(centerImage);
			centerImgView.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(30));
			
			Image homeNew = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_HOMENEW));
			homeNewImgView.setImage(homeNew);
			homeNewImgView.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(5));
			
			Image alertNew = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_ALERTNEW));
			alertNewImgViewButton.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(5));
			alertNewImgViewButton.setImage(alertNew);
			
			iconsCircularPane.diameterProperty().bind(mainPane.widthProperty().divide(100).multiply(50).asObject());
			
			Image logBook1 = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_LOGBOOK1));
			logBook1ImgViewButton.setImage(logBook1);
			logBook1ImgViewButton.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(18));
			Tooltip.install(logBook1ImgViewButton, new Tooltip("Log Book I"));
			
			Image logBook2 = imageProvider.getImage(((WorkbenchResourceProvider)resourcesService).getImageURI(WorkbenchResourceProvider.IMG_IBR_RCP_LOGBOOK2));
			logBook2ImgViewButton.setImage(logBook2);
			logBook2ImgViewButton.fitWidthProperty().bind(mainPane.widthProperty().divide(100).multiply(18));
			Tooltip.install(logBook2ImgViewButton, new Tooltip("Log Book II"));

		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	@FXML
	private void onMouseEntered(MouseEvent event) {
		// Create a light source and position it in the space
		Light.Distant light = new Light.Distant(45.0, 60.0, Color.rgb(2, 168, 210));
		// Create a Lighting effect with the light source
		Lighting effect = new Lighting();
		effect.setLight(light);
		effect.setSurfaceScale(8.0);
		((ImageViewButton)event.getSource()).setEffect(effect);
	}
	@FXML
	private void onMouseExited(MouseEvent event) {
		((ImageViewButton)event.getSource()).setEffect(null);
	}
	
	public void goToPerspective(String perspectiveID) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("org.eclipse.ui.perspectives.showPerspective.perspectiveId", perspectiveID);
		ParameterizedCommand cmd = commandService.createCommand("al.sybina.ebr.rcp.command.switchperspective",
				parameters);
		try {
			handlerService.executeHandler(cmd, ctx);
		} catch (Exception e1) {
			logger.error("cannot open perspective " + perspectiveID, e1);
		}
	}
	
	public void exit() {
		ParameterizedCommand cmd = commandService.createCommand("org.eclipse.ui.file.exit", null);
		try {
			handlerService.executeHandler(cmd, ctx);
		} catch (Exception ex) {
			logger.error("cannot exit ibr ", ex);
		}
	}
	
	
	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
//        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
}
