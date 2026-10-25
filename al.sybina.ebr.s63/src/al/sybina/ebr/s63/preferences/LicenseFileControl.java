package al.sybina.ebr.s63.preferences;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Properties;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import com.dlsc.preferencesfx.formsfx.view.controls.SimpleControl;

import al.sybina.ebr.s63.S63Utils;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;

public class LicenseFileControl extends SimpleControl<FileField, HBox> {

	private static final Logger logger = LoggerCreator.createLogger(LicenseFileControl.class);
	
	protected String CACHE_FILE_NAME = System.getProperty("user.home") + "/.ebr/caches/caches.properties";
	/**
	 * - The fieldLabel is the container that displays the label property of the
	 * field. - The editableField allows users to modify the field's value. -
	 * The readOnlyLabel displays the field's value if it is not editable.
	 */
	private StackPane stackPane;
	private Label readOnlyLabel;
	private Button browse;
	private Button exportLicenseButton;

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

                        
						textarea.setOnMouseClicked(event -> {
							if (event.getButton().equals(MouseButton.PRIMARY)) {
								if (event.getClickCount() == 2) {
									stackPane.getChildren().remove(textarea);
									readOnlyLabel.setVisible(true);
								}
							}
						});
                        
                        
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
		
//		fieldLabel = new Label(field.labelProperty().getValue());
		

		browse = new Button("...");
		browse.setOnAction(event -> browse(browse.getScene().getWindow()));

		
		exportLicenseButton = new Button("Export File");
		if(field.valueProperty().getValue()==null || !field.valueProperty().getValue().exists()) {
			exportLicenseButton.setDisable(true);
		}
		field.valueProperty().addListener(new ChangeListener<File>() {
			@Override
			public void changed(ObservableValue<? extends File> observable, File oldValue, File newValue) {
				if(newValue!=null && newValue.exists()) {
					exportLicenseButton.setDisable(false);
				} else {
					exportLicenseButton.setDisable(true);
				}
			}
		});
		exportLicenseButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setInitialFileName(field.getValue().getName());
				File dest = fileChooser.showSaveDialog(exportLicenseButton.getScene().getWindow());
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
		node.getChildren().addAll(stackPane, empySpace, browse, empySpace2, exportLicenseButton);

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
	}

	@Override
	public void setupEventHandlers() {
		node.setOnMouseEntered(event -> toggleTooltip(node));
		node.setOnMouseExited(event -> toggleTooltip(node));
	}
	@Override
	protected void toggleTooltip(Node reference) {
		if (reference instanceof Control) {
			this.toggleTooltip(reference, (Control) reference);
		}
		if (reference.equals(node)) {
			this.toggleTooltip(reference, readOnlyLabel);
		}
	}

	  /**
	   * Sets the error message as tooltip for the matching control.
	   *
	   * @param below     The control needed for positioning the tooltip.
	   * @param reference The control which gets the tooltip.
	   */
	  @Override
	  protected void toggleTooltip(Node reference, Control below) {
	    String fieldTooltip = field.getTooltip();

	    if ((reference.isFocused() || reference.isHover())
	        && (!fieldTooltip.equals("") || field.getErrorMessages().size() > 0)) {
	      tooltip.setText((!fieldTooltip.equals("") ? fieldTooltip + "\n" : "")
	          + String.join("\n", field.getErrorMessages()));

	      if (tooltip.isShowing()) {
	        return;
	      }

	      Point2D p = below.localToScene(0.0, 0.0);

	      tooltip.show(
	          reference.getScene().getWindow(),
	          p.getX() + reference.getScene().getX() + reference.getScene().getWindow().getX(),
	          p.getY() + reference.getScene().getY() + reference.getScene().getWindow().getY()
	              + below.getHeight() + 5
	      );
	    } else {
	      tooltip.hide();
	    }
	  }
	  
	  
	  
	  
	private void browse(Window parent) {
		FileChooser fileChooser = new FileChooser();
		Properties properties = new Properties();
		String userInitialDirectory = null;
        try {
            File cacheFile = new File(CACHE_FILE_NAME);
            if (cacheFile.isFile()) {
            	try (FileInputStream cacheIn = new FileInputStream(cacheFile)) {
            		properties.load(cacheIn);
            	}
            }
            userInitialDirectory = properties.getProperty("licenseDir");
        } catch (Exception ex) {
        	logger.error(ex.getMessage(), ex);
        }
        if (userInitialDirectory == null) {
            fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setInitialDirectory(new File(userInitialDirectory));

        }
		FileChooser.ExtensionFilter licenseFilesFilter = new FileChooser.ExtensionFilter("License files", "*.lic");
		FileChooser.ExtensionFilter allFilesFilter = new FileChooser.ExtensionFilter("All Files", "*.*");
		fileChooser.getExtensionFilters().addAll(licenseFilesFilter, allFilesFilter);
		
		File result = fileChooser.showOpenDialog(parent);
		if (result != null) {
			
			readOnlyLabel.setText(result.getAbsolutePath());
			field.userInputProperty().set(result.getAbsolutePath());
			//field.valueProperty().set(result);
			
			
			properties.setProperty("licenseDir", result.getParent());
			File f = new File(CACHE_FILE_NAME);
			if(!f.exists()) f.getParentFile().mkdirs();
			OutputStream out;
			try {
				out = new FileOutputStream(f);
				properties.store(out, "Last directory choosed by user");
				out.close();
			} catch (IOException ex) {
				logger.error("Last directory choosed by user " + ex.toString());
			}
		}
	}
}
