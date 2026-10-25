package al.sybina.ebr.logbook.views.history;

import java.io.IOException;

import javax.annotation.PostConstruct;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.fx.core.di.LocalInstance;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

public class HistoryView {

	
	@PostConstruct
	public void createPartControl(BorderPane borderPane, @LocalInstance FXMLLoader loader, IEclipseContext context) {
		loader.setLocation(getClass().getResource("History.fxml"));
		try {
			borderPane.setBackground(new Background(new BackgroundFill(Color.rgb(00,00,33), CornerRadii.EMPTY, Insets.EMPTY)));
			borderPane.setCenter(loader.load());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
