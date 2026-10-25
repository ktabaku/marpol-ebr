package al.sybina.ebr.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.oceanpro.core.security.entities.User;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

@Creatable
@Singleton
public class Login {

	private static Logger logger = LoggerCreator.createLogger(Login.class);

	@Inject
	private IEventBroker eventBroker;
	
	@Inject
	public void createPartControl(ThemeManager themeManager, BorderPane borderPane, @LocalInstance FXMLLoader loader, Stage stage) {
		loader.setLocation(getClass().getResource("Login.fxml"));
		AnchorPane dialogAnchorPane = null;
		try {
			dialogAnchorPane = (AnchorPane) loader.load();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		LoginController loginController = (LoginController)loader.getController();
		dialogAnchorPane.setId("paneId");
		Scene dialogScene = new Scene(dialogAnchorPane);
		themeManager.registerScene(dialogScene);
		Stage dialogStage = new Stage(StageStyle.UNDECORATED);
		dialogStage.setTitle("Login");
		dialogStage.initModality(Modality.APPLICATION_MODAL);
		dialogStage.setResizable(false);
		dialogStage.initOwner(stage);
		dialogStage.setScene(dialogScene);
		loginController.setDialogStage(dialogStage);
		eventBroker.subscribe(Activator.LOGIN_NEEDED_EVENT_TOPIC, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
					Platform.runLater(new Runnable() {
						@Override
						public void run() {

							 //Show the dialog and wait until the user closes it
							dialogStage.show();
						}
					});

			}
		});
		eventBroker.subscribe(Activator.USER_CHANGED_EVENT_TOPIC, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
    			Subject currentUser = SecurityUtils.getSubject();
    			ArrayList<Long> ids = (ArrayList<Long>) event.getProperty(Activator.USER_IDS);
    			if(ids==null || (currentUser.getPrincipal()!=null && ids.contains(((User)currentUser.getPrincipal()).getId()))) {
    				currentUser.logout();
    				eventBroker.post(Activator.LOGIN_NEEDED_EVENT_TOPIC, new HashMap<String, String>());
    			}
			}
		});
	}
}
