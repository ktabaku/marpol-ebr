package al.sybina.ebr.log4j.configuration;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import al.sybina.ebr.log4j.configuration.LogViewPane.LogViewWrapper;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import jfxtras.scene.control.LocalDateTimeTextField;

/**
 * A view that allows the user to select a date-time to go to in an opened log file.
 */
class GoToDateView {
	public static DateTimeFormatter logbookDateTimeFormatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy HH:mm", Locale.ENGLISH);
    private final Dialog dialog;

    GoToDateView( LogView selectedLogView,
    		LogViewWrapper wrapper ) {
        VBox root = new VBox( 20 );
        


        dialog = new Dialog( root );
        dialog.setStyle( StageStyle.UNDECORATED );
        dialog.setResizable( false );
        
       

        Label dateLabel = new Label( "Go to date-time:" );

    	LocalDateTimeTextField dateTimeField = new LocalDateTimeTextField();
    	dateTimeField.setDateTimeFormatter(logbookDateTimeFormatter);
        dateTimeField.setMinWidth( 240.0 );
        dateTimeField.setTooltip( new Tooltip( "Enter a UTC date and time (dd-MM-yyyy HH:mm:ss[.SSS z])" ) );

        Button cancelButton = new Button( "Cancel" );
        cancelButton.setOnAction( event -> dialog.hide() );
        cancelButton.setStyle("-fx-background-color:#00ADEF");
     

        Button goButton = new Button( "Go" );
        goButton.disableProperty().bind( dateTimeField.localDateTimeProperty().isNull() );
        goButton.setStyle("-fx-background-color:ED7D31");

		goButton.setOnAction(event -> {
			LocalDateTime dateTime = dateTimeField.getLocalDateTime();
			Platform.runLater(() -> wrapper.getLogView().goTo(dateTime.atZone(ZoneId.of("UTC")), wrapper::scrollTo));
			dialog.hide();
		});

        HBox buttonBox = new HBox( 10 );
        buttonBox.getChildren().addAll( cancelButton, goButton );
       
        

        root.getChildren().addAll( dateLabel, dateTimeField, buttonBox);
        root.getParent().setStyle("-fx-background-color:#D6DCE5");
       

    }

    void show() {
        dialog.show();
    }

}
