package al.sybina.ebr.logbook.views.marpoldetail;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.fx.core.di.LocalInstance;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class MarpolDetailView {

	
	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader) {
		loader.setLocation(getClass().getResource("MarpolDetail.fxml"));
		try {
//			borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(00,00,33), CornerRadii.EMPTY, Insets.EMPTY)));
			borderPane.setCenter(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
