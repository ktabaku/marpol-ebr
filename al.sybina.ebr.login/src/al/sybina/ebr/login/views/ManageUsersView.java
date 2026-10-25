package al.sybina.ebr.login.views;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class ManageUsersView {
	
	private static Logger logger = LoggerCreator.createLogger(ManageUsersView.class);

	
	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader, IEclipseContext context) {
		loader.setLocation(getClass().getResource("ManageUsers.fxml"));
		try {
			borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(00,00,00), CornerRadii.EMPTY, Insets.EMPTY)));
			AnchorPane aPane = loader.load();
			borderPane.setCenter(aPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}