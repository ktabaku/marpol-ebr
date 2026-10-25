package al.sybina.ebr.javafx.controls;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class ScaledButtons extends Application {

    @Override
    public void start(Stage primaryStage) {
        GridPane root = new GridPane();
        root.setHgap(5);
        root.setVgap(5);
        for (int i = 1; i <= 9 ; i++) {
            root.add(createScaledButton(Integer.toString(i)), (i-1) % 3, (i-1) / 3);
        }
        root.add(createScaledButton("#"), 0, 3);
        root.add(createScaledButton("0"), 1, 3);
        root.add(createScaledButton("*"), 2, 3);

        primaryStage.setScene(new Scene(root, 250, 400));
        primaryStage.show();
    }

    private Button createScaledButton(String text) {
        Button button = new Button(text);
        GridPane.setFillHeight(button, true);
        GridPane.setFillWidth(button, true);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);

        button.layoutBoundsProperty().addListener((obs, oldBounds, newBounds) -> 
            scaleButton(button));

        button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        return button ;
    }

    private void scaleButton(Button button) {
        double w = button.getWidth();
        double h = button.getHeight();

        double bw = button.prefWidth(-1);
        double bh = button.prefHeight(-1);

        if (w == 0 || h == 0 || bw == 0 || bh == 0) return ;

        double hScale = w / bw ;
        double vScale = h / bw ;

        double scale = Math.min(hScale, vScale);

        button.lookup(".text").setScaleX(scale);
        button.lookup(".text").setScaleY(scale);
    }

    public static void main(String[] args) {
        launch(args);
    }
}