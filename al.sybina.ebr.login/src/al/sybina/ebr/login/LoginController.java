package al.sybina.ebr.login;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.apache.shiro.session.mgt.DefaultSessionManager;
import org.apache.shiro.subject.Subject;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.osgi.service.event.EventHandler;
import org.slf4j.MDC;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.security.entities.Role;
import com.oceanpro.core.security.entities.User;

import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.NetworkUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.login.core.DbRealm;
import al.sybina.ebr.login.core.LoginService;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.rcp.MainFrameController.MenuRightHBoxAware;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class LoginController implements MenuRightHBoxAware, Initializable {

	private static final Logger logger = LoggerCreator.createLogger(LoginController.class);
	private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);
	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private DbRealm dbRealm;
	@Inject
	private LoginService loginService;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	
	@FXML
	private AnchorPane anchorPane;
	@FXML
	private TextField usernameTextField;
	@FXML
	private PasswordField passwordField;
	@FXML
	private Button okButton;
	@FXML
	private Button cancelButton;
	@FXML
	private Label loginIncorrectLabel;
	@FXML
	private ImageView logoImageView;
	@FXML
	private ImageView eORBPart1LogoImageView;
	@FXML
	private JFXButton closeButton;
	@FXML
	private HBox hBoxLogin;
	
	private Stage dialogStage;
	private HBox menuRightHbox;
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		MDC.put("localIp", NetworkUtils.getSiteLocalIPv4());
		if(!Activator.isDbServerPresent()) {
			String serverIp = loginPreferencePage.getMainInstallationIp().getValue();
			Platform.runLater(() -> {
				Boolean restart = viewPerspectiveHelper.restart("Server is closed or not reachable at "+ serverIp +". Please restart after server startup or after application preferences fix.");
				if(restart==null || !restart) viewPerspectiveHelper.goToPerspective("al.sybina.ebr.perspectives.home");
			});
			return;
		}
		JavaFXUtils.addFilledPseudoClass(usernameTextField);
		JavaFXUtils.addFilledPseudoClass(passwordField);
		logoImageView.setImage(new Image(getClass().getResourceAsStream("/images/loginCompanyLogo.png")));
		eORBPart1LogoImageView.setImage(new Image(getClass().getResourceAsStream("/images/eORBPart1Logo.png")));
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton.setGraphic(timesGlyph);
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
	        	cancel(null);
	        });
	        Tooltip.install(closeButton, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		double[] xOffset = {0}, yOffset = {0};
	    hBoxLogin.setOnMousePressed(event -> {
	    	xOffset[0] = dialogStage.getX() - event.getScreenX();
	    	yOffset[0] = dialogStage.getY() - event.getScreenY();
	    });

	    hBoxLogin.setOnMouseDragged(event -> {
	    	dialogStage.setX(event.getScreenX() + xOffset[0]);
	    	dialogStage.setY(event.getScreenY() + yOffset[0]);
	        
	    });


		
		viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.users", false);
		viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.users", false);
		viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.audit", false);
		viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.audit", false);
		
		DefaultSecurityManager securityManager = new DefaultSecurityManager(dbRealm);
		DefaultSessionManager dsm = (DefaultSessionManager)securityManager.getSessionManager();
		dsm.setGlobalSessionTimeout(-1);
		dsm.getSessionListeners().add(new SessionListener() {
			@Override
			public void onExpiration(Session arg0) {
				logger.info("Expired security session with id: "+arg0.getId());
				eventBroker.post(Activator.LOGIN_NEEDED_EVENT_TOPIC, new HashMap<String, String>());
			}

			@Override
			public void onStart(Session arg0) {
				logger.info("Started security session with id: "+arg0.getId());
			}

			@Override
			public void onStop(Session arg0) {
				logger.info("Stoped security session with id: "+arg0.getId());
			}
			
		});
        SecurityUtils.setSecurityManager(securityManager);
        
//        RequiredFieldValidator validator = new RequiredFieldValidator();
//        validator.setMessage("Input Required");
//        validator.setIcon(GlyphsBuilder.create(FontAwesomeIconView.class)
//            .glyph(FontAwesomeIcon.WARNING)
//            .size("1em")
//            .styleClass("error")
//            .build());
//        
//        usernameTextField.getValidators().add(validator);
//        usernameTextField.focusedProperty().addListener((o, oldVal, newVal) -> {
//            if (!newVal) {
//            	usernameTextField.validate();
//            }
//        });
//        
//        passwordField.getValidators().add(validator);
//        passwordField.focusedProperty().addListener((o, oldVal, newVal) -> {
//            if (!newVal) {
//            	passwordField.validate();
//            }
//        });
		
		usernameTextField.textProperty().addListener(t -> loginIncorrectLabel.setVisible(false));
		passwordField.textProperty().addListener(t -> loginIncorrectLabel.setVisible(false));
		

		eventBroker.subscribe(UIEvents.UILifeCycle.APP_SHUTDOWN_STARTED, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
		        String serverIp = loginPreferencePage.getMainInstallationIp().getValue();
		        if(serverIp.equals("localhost") || serverIp.equals("127.0.0.1")) {
		        	remoteEventAdminService.getDistributedEventAdmin(serverIp).postEvent(new org.osgi.service.event.Event(Activator.SERVER_SHUTDOWN_STARTED_EVENT_TOPIC, new HashMap<String, String>()));
		        }
			}
		});
		
		eventBroker.subscribe(Activator.SERVER_SHUTDOWN_STARTED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
		        String serverIp = loginPreferencePage.getMainInstallationIp().getValue();
		        if(!serverIp.equals("localhost") && !serverIp.equals("127.0.0.1")) {
		        	viewPerspectiveHelper.restart("Server was closed. Please restart after server startup.");
		        }
			}
		});
		
		eventBroker.subscribe(Activator.DATABASE_CONNECTION_LOST, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
		        viewPerspectiveHelper.restart("Server connection lost. Please restart after server startup.");
			}
		});
		
		
		eventBroker.subscribe(Activator.LOGIN_NEEDED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(org.osgi.service.event.Event event) {
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.users", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.users", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.audit", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.audit", false);
			}
		});

	}

	public void setDialogStage(Stage dialogStage2) {
		this.dialogStage = dialogStage2;
		dialogStage.getScene().getWindow().addEventFilter(WindowEvent.WINDOW_CLOSE_REQUEST, this::cancel);
	}
	
	public void login(ActionEvent event) {
//		boolean usernameValid = usernameTextField.validate();
//		boolean passwordValid = passwordField.validate();
//		if(!usernameValid || !passwordValid) return;
		
        Subject currentUser = SecurityUtils.getSubject();
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken(usernameTextField.getText(), passwordField.getText());
            token.setRememberMe(true);
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
            	logger.info("There is no user with username of " + token.getPrincipal());
            	auditLogger.info("There is no user with username of " + token.getPrincipal());
            	loginIncorrectLabel.setVisible(true);
            	return;
            } catch (IncorrectCredentialsException ice) {
            	logger.info("Password for account " + token.getPrincipal() + " was incorrect!");
            	auditLogger.info("Password for account " + token.getPrincipal() + " was incorrect!");
            	loginIncorrectLabel.setVisible(true);
            	return;
            } catch (LockedAccountException lae) {
            	logger.info("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            	loginIncorrectLabel.setVisible(true);
            	return;
            } catch (AuthenticationException ae) {
            	loginIncorrectLabel.setVisible(true);
            	return;
            }
        }
        usernameTextField.setText("");
        passwordField.setText("");
        loginIncorrectLabel.setVisible(false);
		boolean provided = eventBroker.send(al.sybina.ebr.rcp.Activator.MENU_RIGHT_HBOX_REQUEST, this);
		if(provided) {
			menuRightHbox.setStyle("-fx-border-style:solid;" + "-fx-border-width: 1;" 
			     +"-fx-border-color: #ED7D31;" + "-fx-background-color: #D6DCE5;"+"-fx-padding: 0px 15px 0px 10px;");
			menuRightHbox.fillHeightProperty().set(true);
			HBox parentMenu = (HBox) menuRightHbox.getParent();
			parentMenu.setStyle("-fx-padding: 2px 0px 0px 0px;");
			Button logoutButton = new Button(" Logout");
			logoutButton.setOnAction(ae -> {
				String userName = ((User)currentUser.getPrincipal()).getUsername();
				currentUser.logout();
				menuRightHbox.getChildren().clear(); menuRightHbox.setStyle(null);
				auditLogger.info("user: " + userName + " performed logout.");
				eventBroker.post(Activator.LOGIN_NEEDED_EVENT_TOPIC, new HashMap<String, String>());
			});
			logoutButton.getStyleClass().clear();
			logoutButton.setCursor(Cursor.HAND);
			logoutButton.setStyle("-fx-border-style:solid;" + "-fx-border-width: 0px 0px 0px 1px;;" 
				     +"-fx-border-color:#ED7D31;");
			logoutButton.setMinHeight(menuRightHbox.getHeight()-2);
			StringBuilder rolesName=new StringBuilder();
			for (Role role : ((User)currentUser.getPrincipal()).getRoles()) {
				rolesName.append(role.getDescription()+",");
			}
			Label welcomeLabel = new Label(" Welcome: ") ;
			welcomeLabel.setTextFill(Paint.valueOf("#000000"));
			Label userLabel = new Label(" " +rolesName+" "
					+((User)currentUser.getPrincipal()).getFirstName()
					+" "+((User)currentUser.getPrincipal()).getLastName()+" ") ;
			userLabel.setTextFill(Paint.valueOf("#00B0F0"));
			menuRightHbox.getChildren().clear();
			if(currentUser.isPermitted(Activator.PERMISSION_CREATE_USER)) {
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.users", true);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.users", true);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.audit", true);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.audit", true);
				menuRightHbox.getChildren().addAll(welcomeLabel,userLabel,logoutButton);
			} else {
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.users", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.users", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook.ui.part.audit", false);
				viewPerspectiveHelper.setViewToBeRenderedAndVisible("al.sybina.ebr.logbook2.ui.part.audit", false);
				menuRightHbox.getChildren().addAll(welcomeLabel,userLabel,logoutButton);
			}
		}
		auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " performed login.");
		eventBroker.post(Activator.LOGGED_IN_EVENT_TOPIC, new HashMap<String, String>());
		dialogStage.close();
	}
	
	public void cancel(Event event) {
		if(event!=null) event.consume();
		if(viewPerspectiveHelper.exit()) dialogStage.close();
	}

	@Override
	public void setMenuRightHbox(HBox hbox) {
		this.menuRightHbox = hbox;
	}
	
}
