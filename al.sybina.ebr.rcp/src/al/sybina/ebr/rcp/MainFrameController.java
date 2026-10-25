package al.sybina.ebr.rcp;

import java.net.URL;
import java.util.ResourceBundle;

import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;

public class MainFrameController implements Initializable {
 
    @Inject private IEventBroker eventBroker;
 
    @FXML HBox menuRightHBox;
 
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    	eventBroker.subscribe(Activator.MENU_RIGHT_HBOX_REQUEST, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				MenuRightHBoxAware menuRightHBoxAware = (MenuRightHBoxAware)event.getProperty(IEventBroker.DATA);
				menuRightHBoxAware.setMenuRightHbox(menuRightHBox);
			}
    	});
    }

    public interface MenuRightHBoxAware {
    	public void setMenuRightHbox(HBox hbox);
    }
}
