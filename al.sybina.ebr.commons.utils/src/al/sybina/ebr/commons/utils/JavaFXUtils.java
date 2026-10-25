package al.sybina.ebr.commons.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import org.controlsfx.control.spreadsheet.SpreadsheetCell;
import org.controlsfx.control.spreadsheet.SpreadsheetView;
import org.eclipse.fx.ui.services.theme.ThemeManager;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXChip;
import com.jfoenix.controls.JFXChipView;
import com.jfoenix.skins.JFXChipViewSkin;
import com.sun.javafx.scene.control.skin.TableViewSkin;

import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBase;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.TitledPane;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jfxtras.scene.control.LocalDateTextField;
import jfxtras.scene.control.LocalDateTimeTextField;
import jfxtras.scene.control.LocalTimeTextField;

public class JavaFXUtils {

	
    private static Method columnToFitMethod;

    static {
        try {
            columnToFitMethod = TableViewSkin.class.getDeclaredMethod("resizeColumnToFitContent", TableColumn.class, int.class);
            columnToFitMethod.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

	public static void autoFitTable(TableView tableView) {
		if(tableView.getSkin()!=null) {
			for (Object column : tableView.getColumns()) {
				try {
					columnToFitMethod.invoke(tableView.getSkin(), column, -1);
				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	
	public static void undecorate(Alert alert, JFXButton closeButton, ThemeManager themeManager) {
		alert.initStyle(StageStyle.UNDECORATED);
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        VBox contentPane = new VBox();
        HBox titleHBox = new HBox(new Label(alert.getTitle())); titleHBox.setAlignment(Pos.CENTER_LEFT); HBox.setHgrow(titleHBox, Priority.ALWAYS);
        HBox closeHBox = new HBox(closeButton); closeHBox.setAlignment(Pos.CENTER_RIGHT);
        HBox barHBox = new HBox(titleHBox, closeHBox);
        barHBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#333F50"), CornerRadii.EMPTY, Insets.EMPTY)));
        barHBox.setMinHeight(32); barHBox.setPrefHeight(32); barHBox.setMaxHeight(32);
        
        contentPane.getChildren().addAll(barHBox, alert.getDialogPane());
        Scene scene = new Scene(contentPane);
        stage.setScene(scene);
        themeManager.registerScene(scene);
        
        double[] xOffset = {0}, yOffset = {0};
        barHBox.setOnMousePressed(event -> {
	    	xOffset[0] = stage.getX() - event.getScreenX();
	    	yOffset[0] = stage.getY() - event.getScreenY();
	    });

        barHBox.setOnMouseDragged(event -> {
        	stage.setX(event.getScreenX() + xOffset[0]);
        	stage.setY(event.getScreenY() + yOffset[0]);
	        
	    });
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				((Stage) alert.getDialogPane().getScene().getWindow()).setMinHeight(alert.getDialogPane().getHeight()+32);
			}
		});
	}

	public static void undecorate(Stage dialogStage, JFXButton closeButton, ThemeManager themeManager) {
		dialogStage.initStyle(StageStyle.UNDECORATED);
        VBox contentPane = new VBox();
        Label titleLabel = new Label(dialogStage.getTitle());
        titleLabel.getStyleClass().add("windowTitleBarLabel");
        HBox titleHBox = new HBox(titleLabel); titleHBox.setAlignment(Pos.CENTER_LEFT); HBox.setHgrow(titleHBox, Priority.ALWAYS);
        HBox closeHBox = new HBox(closeButton); closeHBox.setAlignment(Pos.CENTER_RIGHT);
        HBox barHBox = new HBox(titleHBox, closeHBox);
        barHBox.setBackground(new Background(new BackgroundFill(Color.valueOf("#333F50"), CornerRadii.EMPTY, Insets.EMPTY)));
        barHBox.setMinHeight(32); barHBox.setPrefHeight(32); barHBox.setMaxHeight(32);
        
        Node root = dialogStage.getScene().getRoot();
        VBox.setVgrow(root, Priority.ALWAYS);
        contentPane.setPadding(new Insets(2.0));
//        contentPane.setBackground(new Background(new BackgroundFill(Color.valueOf("#00ADEF"), CornerRadii.EMPTY, Insets.EMPTY)));
        contentPane.getChildren().addAll(barHBox, root);
        Scene scene = new Scene(contentPane);
        dialogStage.setScene(scene);
        themeManager.registerScene(scene);
        
        double[] xOffset = {0}, yOffset = {0};
        barHBox.setOnMousePressed(event -> {
	    	xOffset[0] = scene.getWindow().getX() - event.getScreenX();
	    	yOffset[0] = scene.getWindow().getY() - event.getScreenY();
	    });

        barHBox.setOnMouseDragged(event -> {
        	scene.getWindow().setX(event.getScreenX() + xOffset[0]);
        	scene.getWindow().setY(event.getScreenY() + yOffset[0]);
	        
	    });
	}
	
	public static TextFormatter<String> getUpperCaseTextFormatter() {
		return new TextFormatter<String>(
				(change) -> {
		            change.setText(change.getText().toUpperCase());
		            return change;
		        }
		);
	}
	
	
	public static void addFilledPseudoClass(TextField textField) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		textField.pseudoClassStateChanged(filled, false);
		textField.textProperty().addListener((obs, oldText, newText) -> {
		    textField.pseudoClassStateChanged(filled, newText!=null && !newText.isEmpty());
		});
	}



	public static void addFilledPseudoClass(TextArea contentTextArea) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		contentTextArea.pseudoClassStateChanged(filled, false);
		contentTextArea.textProperty().addListener((obs, oldText, newText) -> {
			contentTextArea.pseudoClassStateChanged(filled, newText!=null && !newText.isEmpty());
		});
	}



	public static void addFilledPseudoClass(DatePicker compileDate) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		compileDate.pseudoClassStateChanged(filled, false);
		compileDate.valueProperty().addListener((obs, oldLocalDate, newLocalDate) -> {
			compileDate.pseudoClassStateChanged(filled, newLocalDate!=null);
		});
	}



	public static void addFilledPseudoClass(LocalDateTimeTextField dateTextField) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		dateTextField.pseudoClassStateChanged(filled, false);
		dateTextField.localDateTimeProperty().addListener((obs, oldLocalDateTimeTextField, newLocalDateTimeTextField) -> {
			dateTextField.pseudoClassStateChanged(filled, newLocalDateTimeTextField!=null);
			if(!dateTextField.getChildrenUnmodifiable().isEmpty()) dateTextField.getChildrenUnmodifiable().get(0).pseudoClassStateChanged(filled, newLocalDateTimeTextField!=null);
		});
	}



	public static void addFilledPseudoClass(LocalDateTextField localDateTextField) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		localDateTextField.pseudoClassStateChanged(filled, false);
		localDateTextField.localDateProperty().addListener((obs, oldLocalDateTextField, newLocalDateTextField) -> {
			localDateTextField.pseudoClassStateChanged(filled, newLocalDateTextField!=null);
			if(!localDateTextField.getChildrenUnmodifiable().isEmpty()) localDateTextField.getChildrenUnmodifiable().get(0).pseudoClassStateChanged(filled, newLocalDateTextField!=null);
		});
	}
	
	public static void addFilledPseudoClass(LocalTimeTextField localTimeTextField) {
		PseudoClass filled = PseudoClass.getPseudoClass("filled");
		localTimeTextField.pseudoClassStateChanged(filled, false);
		localTimeTextField.localTimeProperty().addListener((obs, oldLocalTimeTextField, newLocalTimeTextField) -> {
			localTimeTextField.pseudoClassStateChanged(filled, newLocalTimeTextField!=null);
			if(!localTimeTextField.getChildrenUnmodifiable().isEmpty()) localTimeTextField.getChildrenUnmodifiable().get(0).pseudoClassStateChanged(filled, newLocalTimeTextField!=null);
		});
	}
	
	
	public static void addChildFocusdPseudoClass(TitledPane titledPane, ButtonBase button1, ButtonBase button2, ButtonBase button3, ButtonBase button4, ButtonBase button5) {
		PseudoClass childFocus = PseudoClass.getPseudoClass("childFocused");
		titledPane.pseudoClassStateChanged(childFocus, false);
		BooleanProperty anyFocusedProperty = new SimpleBooleanProperty();
		BooleanBinding anyFocusedBinding = null;
		if(button1 != null && button2 != null && button3 !=null && button4 != null && button5 != null) {
			anyFocusedBinding = button1.focusedProperty().or(button2.focusedProperty()).or(button3.focusedProperty()).or(button4.focusedProperty().or(button5.focusedProperty()));
		} else if(button1 != null && button2 != null && button3 !=null && button4 != null) {
			anyFocusedBinding = button1.focusedProperty().or(button2.focusedProperty()).or(button3.focusedProperty()).or(button4.focusedProperty());
		} else if(button1 != null && button2 != null && button3 !=null) {
			anyFocusedBinding = button1.focusedProperty().or(button2.focusedProperty()).or(button3.focusedProperty());
		} else if(button1 != null && button2 != null) {
			anyFocusedBinding = button1.focusedProperty().or(button2.focusedProperty());
		} else if(button1 != null) {
			anyFocusedBinding = button1.focusedProperty().and(new SimpleBooleanProperty(true));
		}
		
		anyFocusedProperty.bind(anyFocusedBinding);
		anyFocusedProperty.addListener(new ChangeListener<Boolean>() {
				@Override
				public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
					titledPane.pseudoClassStateChanged(childFocus, newValue);
				}
		});
	}
	

	public static void addTooltip(JFXChipView<String> identifyOfTanksJFXChipView) {
		Platform.runLater(new Runnable() {
			@Override
			public void run() {
				ScrollPane scrollPane = (ScrollPane)(((JFXChipViewSkin)identifyOfTanksJFXChipView.getSkin()).getChildren().get(0));
				List<Node> chips = ((FlowPane)scrollPane.getContent()).getChildren();
				chips.stream().forEach(chip -> {
					if(chip instanceof JFXChip) {
						Tooltip tt = new Tooltip(((JFXChip<String>)chip).getItem());
						Tooltip.install(chip, tt);
					}
				});
			}
		});
	}
	
	public static void refreshCellText(SpreadsheetView spreadsheetView) {
		for (ObservableList<SpreadsheetCell> row : spreadsheetView.getGrid().getRows()) {
			for (SpreadsheetCell cell : row) {
				try {
					cell.setFormat(cell.getFormat());
				} catch (Exception e) {
					System.out.print(cell);
				}
					
				
			}
		}
	}
}
