package al.sybina.ebr.javafx.controls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Main extends Application {

    //maximum width of the text/label
    private final double MAX_TEXT_WIDTH = 400;
    //default (nonscaled) font size of the text/label
    private final double defaultFontSize = 32;
    private final Font defaultFont = Font.font(defaultFontSize);

    @Override
    public void start(Stage primaryStage) {

        final TextField tf = new TextField("Label text goes here");

        final Label lbl = new Label();
        lbl.setFont(defaultFont);
        lbl.textProperty().addListener((observable, oldValue, newValue) -> {
            //create temp Text object with the same text as the label
            //and measure its width using default label font size
            Text tmpText = new Text(newValue);
            tmpText.setFont(defaultFont);

            double textWidth = tmpText.getLayoutBounds().getWidth();

            //check if text width is smaller than maximum width allowed
            if (textWidth <= MAX_TEXT_WIDTH) {
                lbl.setFont(defaultFont);
            } else {
                //and if it isn't, calculate new font size,
                // so that label text width matches MAX_TEXT_WIDTH
                double newFontSize = defaultFontSize * MAX_TEXT_WIDTH / textWidth;
                lbl.setFont(Font.font(defaultFont.getFamily(), newFontSize));
            }

        });
        lbl.textProperty().bind(tf.textProperty());

        final AnchorPane root = new AnchorPane(lbl, tf);
        AnchorPane.setLeftAnchor(tf, 0d);
        AnchorPane.setRightAnchor(tf, 0d);
        AnchorPane.setBottomAnchor(tf, 0d);


        primaryStage.setScene(new Scene(root, MAX_TEXT_WIDTH, 200));
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}