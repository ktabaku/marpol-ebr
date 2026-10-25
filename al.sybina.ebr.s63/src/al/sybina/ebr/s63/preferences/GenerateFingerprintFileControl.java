package al.sybina.ebr.s63.preferences;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.dlsc.preferencesfx.formsfx.view.controls.SimpleControl;

import al.sybina.ebr.s63.S63Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;

public class GenerateFingerprintFileControl extends SimpleControl<FileField, HBox> {

	private static final Logger logger = LoggerCreator.createLogger(GenerateFingerprintFileControl.class);
	/**
	 * - The fieldLabel is the container that displays the label property of the
	 * field. - The editableField allows users to modify the field's value. -
	 * The readOnlyLabel displays the field's value if it is not editable.
	 */
	private StackPane stackPane;
	private Label readOnlyLabel;
	private Label fieldLabel;
	private Button generateFingerprintButton;
	private Button exportFingerprintFileButton;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void initializeParts() {
		super.initializeParts();

		node = new HBox();
		node.getStyleClass().add("simple-text-control");

		String labelString = field.getValue()==null?"":field.getValue().getAbsolutePath();
		readOnlyLabel = new Label(labelString);
		stackPane = new StackPane();
		HBox.setHgrow(readOnlyLabel, Priority.ALWAYS);
		readOnlyLabel.wrapTextProperty().set(true);
		readOnlyLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                    	readOnlyLabel.setVisible(false);
                        TextArea textarea = new TextArea(readOnlyLabel.getText());
                        textarea.setPrefHeight(readOnlyLabel.getHeight() + 10);
                        stackPane.getChildren().add(textarea);

						textarea.setOnKeyPressed(event -> {
							if (event.getCode().toString().equals("ENTER")) {
								stackPane.getChildren().remove(textarea);
								readOnlyLabel.setVisible(true);
							}
						});
                    }
                }
            }
        });
		stackPane.getChildren().add(readOnlyLabel);
		
		fieldLabel = new Label(field.labelProperty().getValue());
		
		String message = "To obtain an Install Permit, you must generate a unique System Identification File.\n"
		+ "This file il also known as a \"fingerprint\" file.\n"
		+ "The fingerprint file contains information to uniquely identify this computer.\n"
		+ "\n"
		+ "After creating this file, you need it to generate your InstallPermit at the sybina.al web site.\n"
		+ "Proceed to create Fingerprint file?";
		generateFingerprintButton = new Button("Create System Identifier File...");
		generateFingerprintButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Alert alert = new Alert(AlertType.INFORMATION);
		        alert.setTitle("Info");
		        alert.setHeaderText("Generate fingerprint file?");
		        alert.setContentText(message);
		        Optional<ButtonType> optButtonType = alert.showAndWait();
		        if(optButtonType.isPresent() && optButtonType.get().equals(ButtonType.OK)) {
					File fingerPrintFile;
					try {
						fingerPrintFile = S63Utils.generateFingerPrintFile();
						readOnlyLabel.setText(fingerPrintFile.getAbsolutePath());
						fieldLabel.setText(fingerPrintFile.getAbsolutePath());
						field.valueProperty().set(fingerPrintFile);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
		        }
		        
			}
		});
		
		exportFingerprintFileButton = new Button("Export File");
		if(field.valueProperty().getValue()==null || !field.valueProperty().getValue().exists()) {
			exportFingerprintFileButton.setDisable(true);
		}
		field.valueProperty().addListener(new ChangeListener<File>() {
			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				if(newValue!=null && newValue.exists()) {
					exportFingerprintFileButton.setDisable(false);
				} else {
					exportFingerprintFileButton.setDisable(true);
				}
			}
		});
		exportFingerprintFileButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialFileName(field.getValue().getName());
				File dest = fileChooser.showSaveDialog(exportFingerprintFileButton.getScene().getWindow());
				if (dest != null) {
				    try {
				        Files.copy(field.getValue().toPath(), dest.toPath());
				    } catch (IOException ex) {
				    	logger.error("Error exporting file.", ex);
				    }
				}
			}
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void layoutParts() {
		readOnlyLabel.getStyleClass().add("read-only-label");

		readOnlyLabel.setPrefHeight(26);
	    Region empySpace = new Region();
	    empySpace.setMinWidth(20);
	    Region empySpace2 = new Region();
	    empySpace2.setMinWidth(20);
		node.getChildren().addAll(stackPane, empySpace, generateFingerprintButton, empySpace2, exportFingerprintFileButton);

		node.setAlignment(Pos.CENTER_LEFT);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setupBindings() {
		super.setupBindings();

//		editableField.visibleProperty().bind(field.editableProperty().not());
//		readOnlyLabel.visibleProperty().bind(field.editableProperty().not());

//		editableField.textProperty().bindBidirectional(field.userInputProperty());
//		readOnlyLabel.textProperty().bind(field.userInputProperty());
//		editableField.promptTextProperty().bind(field.placeholderProperty());
		
//		editableField.managedProperty().bind(editableField.visibleProperty());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setupValueChangedListeners() {
		super.setupValueChangedListeners();

//		field.multilineProperty().addListener((observable, oldValue, newValue) -> {
//			node.setPrefHeight(newValue ? 80 : 0);
//			readOnlyLabel.setPrefHeight(newValue ? 80 : 26);
//		});
//
//		field.errorMessagesProperty().addListener(
//				(observable, oldValue, newValue) -> toggleTooltip(field.isMultiline() ? editableArea : editableField));

//		editableField.focusedProperty().addListener((observable, oldValue, newValue) -> toggleTooltip(editableField));
	}

}
