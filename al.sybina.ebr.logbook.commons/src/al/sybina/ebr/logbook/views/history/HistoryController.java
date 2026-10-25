package al.sybina.ebr.logbook.views.history;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.LocalInstance;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.jpedal.examples.viewer.gui.javafx.dialog.FXMessageDialog;
import org.jpedal.examples.viewer.gui.javafx.dialog.FXOptionDialog;
import org.jpedal.utils.LogWriter;
import org.osgi.framework.Bundle;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXSlider;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.oceanpro.utils.EasyDateUtils;
import com.sun.javafx.binding.ObjectConstant;

import al.sybina.ebr.commons.utils.HeterogeneousBidirectionalBinder;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ResizeHelper;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.BaseViewerFX;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.commons.ReportSignedDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.domain.reladomo.TimestampProvider;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.IntegerBinding;
import javafx.beans.property.Property;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Pagination;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Pair;
import jfxtras.scene.control.LocalDateTimeTextField;

public class HistoryController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(HistoryController.class);
	
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private static ThemeManager themeManager;
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private EHandlerService handlerService;
	@Inject
	private ECommandService commandService;
	@Inject
	private IEclipseContext context;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	@LocalInstance 
	private FXMLLoader loaderMaster;
	@Inject
	@LocalInstance 
	private FXMLLoader loaderEditLog;
	@Inject
	@LocalInstance 
	private FXMLLoader loaderLogModifications;
	@Inject
	@LocalInstance 
	private FXMLLoader loaderHistoryPreview;
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private Pagination pagination;
	@FXML
	private TableView<OilLog> logTableView;
	
	@FXML
	private TableColumn<OilLog, HBox> actionsCol;
//	@FXML
//	private TableColumn<OilLog, String> typeCol;
	@FXML
	private TableColumn<OilLog, String> businessDateCol;
	@FXML
	private TableColumn<OilLog, String> codeLetterCol;
	@FXML
	private TableColumn<OilLog, String> codeNumberCol;
	@FXML
	private TableColumn<OilLog, String> contentCol;
	@FXML
	private TableColumn<OilLog, String> correctionNoteCol;
	@FXML
	private TableColumn<OilLog, String> nameCreatorCol;
	@FXML
	private TableColumn<OilLog, ImageView> signatureCreatorCol;
	@FXML
	private TableColumn<OilLog, String> noteCol;
	@FXML
	private TableColumn<OilLog, String> masterCol;
	@FXML
	private TableColumn<OilLog, String> masterSignatureCol;
	@FXML
	private TableColumn<OilLog, String> attachmentCol;
	@FXML
	private Button historyPreviewButton;
	@FXML
	private Button showPreviewButton;
	@FXML
	private Button saveButtonMaster;

	
	@FXML
	private JFXSlider businessDateSlider;
	@FXML
	private LocalDateTimeTextField businessDatePicker;
	@FXML
	private CheckBox businessDateCheckBox;
	@FXML
	private JFXSlider processingDateSlider;
	@FXML
	private LocalDateTimeTextField processingDatePicker;
	@FXML
	private CheckBox processingDateCheckBox;
	
	private static JFXButton closeButton;
	private JFXButton closeButton1;
	private JFXButton closeButton2;
	private JFXButton closeButton3;
	private JFXButton closeButton4;
	private JFXButton closeButton5;
	
    private final static int preferedRowsPerPage = 20;
    private List<Integer> pageSizes;
    
	private List<OilLog> result;
	protected boolean PERMISSION_MASTER_SIGN;
	protected boolean PERMISSION_EDIT_LOGS;
	protected boolean PERMISSION_MASTER_EDIT_LOGS;

//	private Property<OilLog> lastProcessingDateOilLogProperty = new SimpleObjectProperty<OilLog>();
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(businessDatePicker);
		JavaFXUtils.addFilledPseudoClass(processingDatePicker);
		try {
			SVGGlyph timesGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton = new JFXButton();
			closeButton.getStyleClass().add("closeDialogButton");
			closeButton.setMinWidth(40); closeButton.setPrefWidth(40); closeButton.setMaxWidth(40);
			closeButton.setMinHeight(32); closeButton.setPrefHeight(32); closeButton.setMaxHeight(32);
			closeButton.setGraphic(timesGlyph);
			closeButton.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton, new Tooltip("Exit"));
	        
	        SVGGlyph timesGlyph1 = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
	        timesGlyph1.setSize(30);
			closeButton1 = new JFXButton();
			closeButton1.getStyleClass().add("closeDialogButton");
			closeButton1.setMinWidth(40); closeButton1.setPrefWidth(40); closeButton1.setMaxWidth(40);
			closeButton1.setMinHeight(32); closeButton1.setPrefHeight(32); closeButton1.setMaxHeight(32);
			closeButton1.setGraphic(timesGlyph1);
			closeButton1.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton1.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton1, new Tooltip("Exit"));
	        
	        SVGGlyph timesGlyph2 = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton2 = new JFXButton();
			closeButton2.getStyleClass().add("closeDialogButton");
			closeButton2.setMinWidth(40); closeButton2.setPrefWidth(40); closeButton2.setMaxWidth(40);
			closeButton2.setMinHeight(32); closeButton2.setPrefHeight(32); closeButton2.setMaxHeight(32);
			closeButton2.setGraphic(timesGlyph2);
			closeButton2.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton2.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton2, new Tooltip("Exit"));
	        
	        SVGGlyph timesGlyph3 = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton3 = new JFXButton();
			closeButton3.getStyleClass().add("closeDialogButton");
			closeButton3.setMinWidth(40); closeButton3.setPrefWidth(40); closeButton3.setMaxWidth(40);
			closeButton3.setMinHeight(32); closeButton3.setPrefHeight(32); closeButton3.setMaxHeight(32);
			closeButton3.setGraphic(timesGlyph3);
			closeButton3.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton3.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton3, new Tooltip("Exit"));
	        
	        SVGGlyph timesGlyph4 = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton4 = new JFXButton();
			closeButton4.getStyleClass().add("closeDialogButton");
			closeButton4.setMinWidth(40); closeButton4.setPrefWidth(40); closeButton4.setMaxWidth(40);
			closeButton4.setMinHeight(32); closeButton4.setPrefHeight(32); closeButton4.setMaxHeight(32);
			closeButton4.setGraphic(timesGlyph4);
			closeButton4.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton4.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton4, new Tooltip("Exit"));
	        
	        SVGGlyph timesGlyph5 = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
			closeButton5 = new JFXButton();
			closeButton5.getStyleClass().add("closeDialogButton");
			closeButton5.setMinWidth(40); closeButton5.setPrefWidth(40); closeButton5.setMaxWidth(40);
			closeButton5.setMinHeight(32); closeButton5.setPrefHeight(32); closeButton5.setMaxHeight(32);
			closeButton5.setGraphic(timesGlyph5);
			closeButton5.ripplerFillProperty().setValue(Color.LIGHTSALMON);
			closeButton5.setOnAction(event -> {
				((Stage)((Button)event.getSource()).getScene().getWindow()).close();
	        });
	        Tooltip.install(closeButton5, new Tooltip("Exit"));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		lastProcessingDateOilLogProperty.setValue(operationLogReladomoService.getLastIncludingCodeIMissedOilLog(getOilLogType()));
		try {
			actionsCol.setCellValueFactory(param -> {
							Button modificationsButton = null;
							if (isActual(param.getValue())) {
			                	if(param.getValue().getCorrectionNote()!=null) {
			        				SVGGlyph modificationsGlyph = null;
			        				try {
										modificationsGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.eye-slash");
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
			        				modificationsButton = createModificationsButton(modificationsGlyph, param.getValue());
			        				modificationsButton.setPrefWidth(40); modificationsButton.setMinWidth(40); modificationsButton.setMaxWidth(40);
			        				modificationsButton.setCache(true);
			        			}
							
//			                	boolean masterNotSigned = (param.getValue().getMasterSignerName()==null || param.getValue().getMasterSignerName().equals(""));
				                if (/*lastProcessingDateOilLogProperty.getValue() != null && param.getValue().getGroupUuid().equals(lastProcessingDateOilLogProperty.getValue().getGroupUuid()) && */ PERMISSION_EDIT_LOGS && businessDateCheckBox.isSelected() && processingDateCheckBox.isSelected()) {
				                	SVGGlyph editGlyph = null;
//				        			SVGGlyph deleteGlyph = null;
									try {
										editGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.pencil-square");
//										deleteGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.times-circle");
										
									} catch (Exception e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}
				        			Button editButton = createEditIconButton(editGlyph, param.getValue());
//				                	Button deleteButton = createDeleteIconButton(deleteGlyph, param.getValue());
				                	editButton.setPrefWidth(40); editButton.setMinWidth(40); editButton.setMaxWidth(40);
//				                	deleteButton.setPrefWidth(40); deleteButton.setMinWidth(40); deleteButton.setMaxWidth(40);
				                	editButton.setCache(true); // deleteButton.setCache(true);
				                	
				                	editButton.setDisable(!PERMISSION_EDIT_LOGS);
//				                	deleteButton.setDisable(!PERMISSION_EDIT_LOGS);
				                	if(!PERMISSION_EDIT_LOGS) {
				                		editButton.setTooltip(new Tooltip("You do not have sufficient Permissions to perform this operation."));
//				                		deleteButton.setTooltip(new Tooltip("You do not have sufficient Permissions to perform this operation."));
				                	} else {
				                		editButton.setTooltip(new Tooltip("Edit"));
//				                		deleteButton.setTooltip(new Tooltip("Delete"));
				                	}
				                	return modificationsButton==null?ObjectConstant.valueOf(new HBox(editButton /*, deleteButton*/)):ObjectConstant.valueOf(new HBox(editButton/*, deleteButton*/, modificationsButton));
				                } else {
				                	return modificationsButton==null?null:ObjectConstant.valueOf(new HBox(modificationsButton));
				                }
							} else {
								return null;
							}
						}
			);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
//		typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		businessDateCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
	        SimpleStringProperty property = new SimpleStringProperty();
	        property.setValue(EasyDateUtils.logbookDateFormatter.format(p.getBusinessDateFrom()));
	        return property;
		});
//		businessDateCol.setCellValueFactory(new PropertyValueFactory<>("businessDateFrom"));
//		businessDateCol.setCellFactory(column -> {
//	        TableCell<OilLog, Date> cell = new TableCell<OilLog, Date>() {
//	            private SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm");
//	            @Override
//	            protected void updateItem(Date item, boolean empty) {
//	                super.updateItem(item, empty);
//	                if(empty) {
//	                    setText(null);
//	                }
//	                else {
//	                    this.setText(format.format(item));
//	                }
//	            }
//	        };
//	        return cell;
//	    });
		codeLetterCol.setCellValueFactory(new PropertyValueFactory<>("codeLetter"));
		codeNumberCol.setCellValueFactory(new PropertyValueFactory<>("codeNumber"));
		contentCol.setCellValueFactory(new PropertyValueFactory<>("content"));
		contentCol.setStyle("-fx-alignment: center-left;");
		correctionNoteCol.setCellValueFactory(new PropertyValueFactory<>("correctionNote"));
		nameCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			return new ReadOnlyStringWrapper(p.getSignerRank() + " " + p.getSignerName() + " " + p.getSignerSurname());
		});
		signatureCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			byte[] signatureBytes = Base64.getDecoder().decode(p.getSignature());
			BufferedImage bImage;
			try {
				bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
				WritableImage image = SwingFXUtils.toFXImage(bImage, null);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(50);
				imageView.setFitHeight(30);
				return new SimpleObjectProperty(imageView);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		});
//		noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		masterCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			if(p.getMasterSignerName()==null) return null;
			return new ReadOnlyStringWrapper(p.getMasterSignerName() + " " + p.getMasterSignerSurname());
		});
		masterSignatureCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			if(p.getMasterSignature()==null) return null;
			byte[] signatureBytes = Base64.getDecoder().decode(p.getMasterSignature());
			BufferedImage bImage;
			try {
				bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
				WritableImage image = SwingFXUtils.toFXImage(bImage, null);
				ImageView imageView = new ImageView(image);
				imageView.setFitWidth(50);
				imageView.setFitHeight(30);
				return new SimpleObjectProperty(imageView);
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		});
		attachmentCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue();
			if(p.getAttachmentName()==null) return null;
			Label fileNameLabel = new Label(p.getAttachmentName());
			fileNameLabel.getStyleClass().add("logbookOtherLabel");
			SVGGlyph downloadGlyph = null;
			try {
				downloadGlyph = SVGGlyphLoader.getIcoMoonGlyph("icomoon.svg.download");
			} catch (Exception e) {
				logger.error(e.getMessage(), e);
			}
			Button downloadButton = createDownloadIconButton(downloadGlyph, p);
			return new SimpleObjectProperty(new HBox(fileNameLabel, downloadButton));
		});
		noteCol.setCellValueFactory(new PropertyValueFactory<>("note"));
		
		logTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		logTableView.setRowFactory(new javafx.util.Callback<TableView<OilLog>, TableRow<OilLog>>() {  
            @Override  
            public TableRow<OilLog> call(TableView<OilLog> tableView) {  
                final TableRow<OilLog> row = new TableRow<OilLog>() {
                    @Override
                    public void updateItem(OilLog item, boolean empty){
                        super.updateItem(item, empty);
                        
                        if (item == null || empty) {
//                            setStyle("");
                        } else {
                            if (item.getMasterSignerName()==null || item.getMasterSignerName().equals("")) {
                            	if(getTableView().getSelectionModel().getSelectedItems().contains(item)) {
	                              	setStyle("-fx-background-color: rgba(232, 107, 17, 1);");
	                            } else {
	                            	setStyle("-fx-background-color: rgba(152, 160, 174, 1);");
	                            }
                            }
//                            if (isActual(item)) {
//    	                        this.setOnMouseClicked(event -> {
//    	                            if (event.getClickCount() == 2 && (! this.isEmpty()) ) {
//    	                            	edit((ActionEvent)null);
//    	                            }
//    	                        });
//                            }
                        }
                    }
                };

                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem showPreviewSingleMenuItem = new MenuItem("Preview");
                showPreviewSingleMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
                    @Override  
                    public void handle(ActionEvent event) {  
                    	showPreview(null);
                    }  
                });
                
//                final MenuItem editMenuItem = new MenuItem("Edit");
//                editMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
//                    @Override  
//                    public void handle(ActionEvent event) {  
//                    	edit((ActionEvent)null);
//                    }  
//                });
                
                contextMenu.getItems().addAll(showPreviewSingleMenuItem);//, editMenuItem);
                
                final ContextMenu multiSelectContextMenu = new ContextMenu();
                final MenuItem showPreviewMenuItem = new MenuItem("Preview");  
                showPreviewMenuItem.setOnAction(new EventHandler<ActionEvent>() {  
                    @Override  
                    public void handle(ActionEvent event) {
                    	showPreview(null);
                    }  
                });  
                multiSelectContextMenu.getItems().addAll(showPreviewMenuItem);

               // Set context menu on row, but use a binding to make it only show for non-empty rows:
                IntegerBinding sizeProperty = Bindings.size(logTableView.getSelectionModel().getSelectedItems());
                SimpleObjectProperty<OilLog> sbpItem = new SimpleObjectProperty<OilLog>(logTableView.getSelectionModel().getSelectedItem());
                BooleanBinding bb = Bindings.createBooleanBinding(() -> sbpItem.getValue()!=null && isActual(sbpItem.getValue()), sbpItem);
                row.contextMenuProperty().bind(  
                        Bindings.when(row.emptyProperty())
                        .then((ContextMenu)null)  
                        .otherwise(
                        		Bindings.when(sizeProperty.isNotEqualTo(1).or(bb.not()))
                        		.then(multiSelectContextMenu)
                        		.otherwise(contextMenu))  
                );
                return row ;  
            }  
		}); 
		

		logTableView.getSelectionModel().getSelectedItems().addListener(new ListChangeListener<OilLog>() {
			@Override
			public void onChanged(Change<? extends OilLog> c) {
				int selectionSize = logTableView.getSelectionModel().getSelectedItems().size();
				logTableView.refresh();
				if(selectionSize == 0) {
					showPreviewButton.setDisable(true);
					saveButtonMaster.setDisable(true);
					return;
				} else {
					showPreviewButton.setDisable(false);
					boolean disableMasterSig = false;
//					String groupUUID = null;
					if(PERMISSION_MASTER_SIGN && businessDateCheckBox.isSelected() && processingDateCheckBox.isSelected()) {
						ObservableList<OilLog> selectedOilLogs = logTableView.getSelectionModel().getSelectedItems();
						for(OilLog oilLog : selectedOilLogs) {
							if (oilLog.getMasterSignerName()!=null && !oilLog.getMasterSignerName().equals("")) disableMasterSig = true;
//							if(groupUUID == null) {
//								groupUUID = oilLog.getGroupUuid();
//							} else if(!groupUUID.equals(oilLog.getGroupUuid())) {
//								disableMasterSig = true;
//								break;
//							}
							if(!isActual(oilLog)) {
								disableMasterSig = true;
								break;
							};
						}
						if(!disableMasterSig) {
							Set<String> groupUuids = selectedOilLogs.stream().map(OilLog::getGroupUuid).collect(Collectors.toSet());
							List<OilLog> sameGroupOilLogList = operationLogReladomoService.getOil1LogListByGroupUUID(groupUuids.toArray(new String[groupUuids.size()]));
							for(OilLog oilLog : selectedOilLogs) {
								sameGroupOilLogList.removeIf(ol -> ol.getId() == oilLog.getId());
							}
							if(!sameGroupOilLogList.isEmpty()) {disableMasterSig = true;}
						}
					}
					saveButtonMaster.setDisable(!PERMISSION_MASTER_SIGN || disableMasterSig);
				}
			}
		});
		initPagination(null, null, null);
		pagination.setPageFactory(this::createPage);
		
		logTableView.getSelectionModel().select(0);
		
		mainAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(() -> {
					customResizeLargeContentColumn(logTableView);
				});
		}});
		
		eventBroker.subscribe(al.sybina.ebr.login.Activator.LOGGED_IN_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				initPermissions();
				logTableView.getSelectionModel().clearSelection();
				logTableView.getSelectionModel().selectFirst();
			}
		});
		initPermissions();
		
		eventBroker.subscribe(Activator.LOGS_CHANGED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
//				lastProcessingDateOilLogProperty.setValue(operationLogReladomoService.getLastIncludingCodeIMissedOilLog(getOilLogType()));
				if(businessDateCheckBox.isSelected() && processingDateCheckBox.isSelected()) {
					businessDatePicker.setLocalDateTime(LocalDateTime.now());
					processingDatePicker.setLocalDateTime(LocalDateTime.now());
					pagination.setCurrentPageIndex(0);
					pagination.requestLayout();
					new Thread(() -> {
						try {
							while(pagination.isNeedsLayout()) {
								Thread.sleep(200);
							}
						} catch (InterruptedException e) {
						}
						Platform.runLater(() -> {
							initPagination(null, null, null);
							logTableView.getSelectionModel().selectFirst();
						});
					}).start();
				} else {
					businessDateCheckBox.setSelected(true);
					processingDateCheckBox.setSelected(true);
				}
			}
		});
//		logTableView.setColumnResizePolicy((param) -> true );
//		logGroupTableView.setColumnResizePolicy((param) -> true );
		
		
		bindBusinessDatePickerWithSlider(businessDatePicker, businessDateSlider);
		bindProcessingDatePickerWithSlider(processingDatePicker, processingDateSlider);
		
		businessDatePicker.setLocalDateTime(LocalDateTime.now());
		processingDatePicker.setLocalDateTime(LocalDateTime.now());
		
		businessDatePicker.localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>() {
			@Override
			public void changed(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) {
				if(!businessDatePicker.isDisable()) {
			    	LocalDateTime validInBusinessDate = newValue;
			    	LocalDateTime validInProcessingDate = processingDatePicker.getLocalDateTime();
			    	initPagination(validInBusinessDate, validInProcessingDate, null);
				}
			}
		});
	    processingDatePicker.localDateTimeProperty().addListener(new ChangeListener<LocalDateTime>() {
			@Override
			public void changed(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) {
				if(!processingDatePicker.isDisable()) {
			    	LocalDateTime validInBusinessDate = businessDatePicker.getLocalDateTime();
			    	LocalDateTime validInProcessingDate = newValue;
			    	initPagination(validInBusinessDate, validInProcessingDate, null);
				}
			}
		});

	    businessDateCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
	    	if(isNowSelected) {
	    		businessDatePicker.setDisable(true);
	    		businessDateSlider.setDisable(true);
	    		businessDatePicker.setLocalDateTime(LocalDateTime.now());
		    	LocalDateTime validInBusinessDate = null;
		    	LocalDateTime validInProcessingDate = processingDateCheckBox.isSelected()?null:processingDatePicker.getLocalDateTime();
		    	initPagination(validInBusinessDate, validInProcessingDate, null);
	    	} else {
	    		businessDatePicker.setDisable(false);
	    		businessDateSlider.setDisable(false);
	    		logTableView.refresh();
	    	}
	    	
	    });
	    processingDateCheckBox.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
	    	if(isNowSelected) {
		    	processingDatePicker.setDisable(true);
		    	processingDateSlider.setDisable(true);
		    	processingDatePicker.setLocalDateTime(LocalDateTime.now());
		    	LocalDateTime validInBusinessDate = businessDateCheckBox.isSelected()?null:businessDatePicker.getLocalDateTime();
		    	LocalDateTime validInProcessingDate = null;
		    	initPagination(validInBusinessDate, validInProcessingDate, null);
	    	} else {
		    	processingDatePicker.setDisable(false);
		    	processingDateSlider.setDisable(false);
		    	logTableView.refresh();
	    	}
    	
	    });
	}

	private void initPermissions() {
		if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_MASTER_SIGN)) {
			PERMISSION_MASTER_SIGN = true;
			saveButtonMaster.setTooltip(null);
		} else {
			PERMISSION_MASTER_SIGN = false;
			saveButtonMaster.setTooltip(new Tooltip("You do not have sufficient Permissions to perform this operation."));
		}
		if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_MASTER_EDIT_LOGS)) {
			PERMISSION_MASTER_EDIT_LOGS = true;
		} else {
			PERMISSION_MASTER_EDIT_LOGS = false;
		}
		if(SecurityUtils.getSubject().isPermitted(al.sybina.ebr.login.Activator.PERMISSION_EDIT_LOGS)) {
			PERMISSION_EDIT_LOGS = true;
		} else {
			PERMISSION_EDIT_LOGS = false;
		}
	}
	private void bindBusinessDatePickerWithSlider(LocalDateTimeTextField datePicker, JFXSlider dateSlider) {
		Function<LocalDateTime, Boolean> datePickerChanged = newValue -> {
			dateSlider.setValue(-1*(newValue.getDayOfMonth() + newValue.getHour()/24.0 + newValue.getMinute()/1440.0 + newValue.getSecond()/86400.0));
			return Boolean.FALSE;
		};
		Function<Boolean, LocalDateTime> sliderChanging = newValue -> {
			LocalDateTime actualLocalDateTime = datePicker.localDateTimeProperty().getValue();
			if (!newValue) {
				Double value = -1*(dateSlider.getValue());
				if(actualLocalDateTime==null) actualLocalDateTime = LocalDateTime.now();
				int maxDays = actualLocalDateTime.getMonth().maxLength();
				if(Math.round(value.intValue()) > maxDays) {
					dateSlider.setValue(-1*(maxDays));
					actualLocalDateTime = actualLocalDateTime.withDayOfMonth(maxDays).withHour(0).withMinute(0).withSecond(0);
				} else {
					actualLocalDateTime = actualLocalDateTime.withDayOfMonth(Math.round(value.intValue())).withHour(0).withMinute(0).withSecond(0);
				}
				datePicker.setLocalDateTime(actualLocalDateTime);
			}
			return actualLocalDateTime;
		};
		HeterogeneousBidirectionalBinder<LocalDateTime, Boolean> businessCalendarSliderBinder = new HeterogeneousBidirectionalBinder(
	    		datePicker.localDateTimeProperty(),
	    		dateSlider.valueChangingProperty(),
	    		    datePickerChanged,
	    		    sliderChanging
	    );
	}
	
	
	private void bindProcessingDatePickerWithSlider(LocalDateTimeTextField datePicker, JFXSlider dateSlider) {
		Function<LocalDateTime, Boolean> datePickerChanged = newValue -> {
			dateSlider.setValue(newValue.getDayOfMonth() + newValue.getHour()/24.0 + newValue.getMinute()/1440.0 + newValue.getSecond()/86400.0);
			return Boolean.FALSE;
		};
		Function<Boolean, LocalDateTime> sliderChanging = newValue -> {
			LocalDateTime actualLocalDateTime = datePicker.localDateTimeProperty().getValue();
			if (!newValue) {
				Double value = dateSlider.getValue();
				if(actualLocalDateTime==null) actualLocalDateTime = LocalDateTime.now();
				int maxDays = actualLocalDateTime.getMonth().maxLength();
				if(Math.round(value.intValue()) > maxDays) {
					dateSlider.setValue(maxDays);
					actualLocalDateTime = actualLocalDateTime.withDayOfMonth(maxDays).withHour(0).withMinute(0).withSecond(0);
				} else {
					actualLocalDateTime = actualLocalDateTime.withDayOfMonth(Math.round(value.intValue())).withHour(0).withMinute(0).withSecond(0);
				}
				datePicker.setLocalDateTime(actualLocalDateTime);
			}
			return actualLocalDateTime;
		};
		HeterogeneousBidirectionalBinder<LocalDateTime, Boolean> businessCalendarSliderBinder = new HeterogeneousBidirectionalBinder(
	    		datePicker.localDateTimeProperty(),
	    		dateSlider.valueChangingProperty(),
	    		    datePickerChanged,
	    		    sliderChanging
	    );
	}
	
//	protected void businessDatePickerChanged(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) {
//		if(businessDatePicker.getUserData()!=null && (Boolean)businessDatePicker.getUserData()) {
//			businessDatePicker.setUserData(new Boolean(false));
//			return;
//		}
//		businessDateSlider.setUserData(new Boolean(true));
//		businessDateSlider.setValue(-1*(newValue.getDayOfMonth() + newValue.getHour()/24.0 + newValue.getMinute()/1440.0 + newValue.getSecond()/86400.0));
//	};
//	protected void businessSliderChanging(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
//		if (!newValue) {
//	        Double value = -1*(businessDateSlider.getValue());
//			if(businessDateSlider.getUserData()!=null && (Boolean)businessDateSlider.getUserData()) {
//				businessDateSlider.setUserData(new Boolean(false));
//				return;
//			}
//			businessDatePicker.setUserData(new Boolean(true));
//			LocalDateTime actualLocalDateTime = businessDatePicker.localDateTimeProperty().getValue();
//			if(actualLocalDateTime==null) actualLocalDateTime = LocalDateTime.now();
//			int maxDays = actualLocalDateTime.getMonth().maxLength();
//			if(Math.round(value.intValue()) > maxDays) {
//				businessDateSlider.setValue(-1*(maxDays));
//				businessDatePicker.setLocalDateTime(actualLocalDateTime.withDayOfMonth(maxDays).withHour(0).withMinute(0).withSecond(0));
//			} else {
//				businessDatePicker.setLocalDateTime(actualLocalDateTime.withDayOfMonth(Math.round(value.intValue())).withHour(0).withMinute(0).withSecond(0));
//			}
//		}
//	};
	
	protected void processingDatePickerChanged(ObservableValue<? extends LocalDateTime> observable, LocalDateTime oldValue, LocalDateTime newValue) {
		if(processingDatePicker.getUserData()!=null && (Boolean)processingDatePicker.getUserData()) {
			processingDatePicker.setUserData(new Boolean(false));
			return;
		}
		processingDateSlider.setUserData(new Boolean(true));
		processingDateSlider.setValue(newValue.getDayOfMonth() + newValue.getHour()/24.0 + newValue.getMinute()/1440.0 + newValue.getSecond()/86400.0);
	};
	protected void processingSliderChanging(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (!newValue) {
	        Double value = processingDateSlider.getValue();
			if(processingDateSlider.getUserData()!=null && (Boolean)processingDateSlider.getUserData()) {
				processingDateSlider.setUserData(new Boolean(false));
				return;
			}
			processingDatePicker.setUserData(new Boolean(true));
			LocalDateTime actualLocalDateTime = processingDatePicker.localDateTimeProperty().getValue();
			if(actualLocalDateTime==null) actualLocalDateTime = LocalDateTime.now();
			int maxDays = actualLocalDateTime.getMonth().maxLength();
			if(Math.round(value.intValue()) > maxDays) {
				processingDateSlider.setValue(maxDays);
				processingDatePicker.setLocalDateTime(actualLocalDateTime.withDayOfMonth(maxDays).withHour(0).withMinute(0).withSecond(0));
			} else {
				processingDatePicker.setLocalDateTime(actualLocalDateTime.withDayOfMonth(Math.round(value.intValue())).withHour(0).withMinute(0).withSecond(0));
			}
		}
	};

	private int initPagination(LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate, Integer currentPageIndex) {
		String oilLogType = getOilLogType();
		Map<Long, Pair<String, Integer>> orderedLogGroupSizes = operationLogReladomoService.getAllOil1LogGroupSizes(oilLogType, validInBusinessDate, validInProcessingDate);
		pageSizes = calculatePageSizes(preferedRowsPerPage, orderedLogGroupSizes);
		int pageCount = pageSizes.size();
		pagination.setPageCount(pageCount>0 ? pageCount : 1);
		loadLogs(validInBusinessDate, validInProcessingDate, 0, pageCount>0 ? pageSizes.get(pageCount-1): 0);
		if(currentPageIndex==null) {
			pagination.setCurrentPageIndex(pageCount>0 ? pageCount-1 : 0);
		} else {
			pagination.setCurrentPageIndex(currentPageIndex);
		}
		return pageCount;
	}
	private ArrayList<Integer> calculatePageSizes(int preferedRowsPerPage, Map<Long, Pair<String, Integer>> orderedLogGroupSizes) {
		ArrayList<Integer> pageSizes = new ArrayList<Integer>();
		int groupSizeSum = 0;
		for(Pair<String, Integer> pair : orderedLogGroupSizes.values()) {
			groupSizeSum += pair.getValue();
			if(groupSizeSum == preferedRowsPerPage || groupSizeSum > preferedRowsPerPage) {
				pageSizes.add(new Integer(groupSizeSum));
				groupSizeSum = 0;
			}
		}
		if(groupSizeSum>0) pageSizes.add(new Integer(groupSizeSum));
		return pageSizes;
	}

	private Node createPage(int pageIndex) {
        int fromIndex = pageSizes.stream().mapToInt(Integer::intValue).limit(pageIndex).sum();
        int toIndex = fromIndex + (pageSizes.size()!=0 ? pageSizes.get(pageIndex) : 0);
        loadLogs(null, null, fromIndex, toIndex);
        return new BorderPane(logTableView);
    }
	public void loadLogs(LocalDateTime validInBusinessDate, LocalDateTime validInProcessingDate, int fromIndex, int toIndex) {
		String oilLogType = getOilLogType();
		result = operationLogReladomoService.getValidOil1Log(oilLogType, validInBusinessDate, validInProcessingDate, fromIndex, toIndex);
		logTableView.getItems().clear();
		logTableView.getItems().addAll(result);
		
		Comparator<OilLog> idComparator = Comparator.comparingLong(oilLog -> oilLog.getId());
		logTableView.getItems().sort(idComparator);
	}
	public void customResize(TableView<?> view) {
        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
            width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
                col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/view.getColumns().size())-1);
            });
        }
    }
	public void customResizeLargeContentColumn(TableView<?> view) {
        AtomicLong width = new AtomicLong();
        view.getColumns().forEach(col -> {
        	width.addAndGet((long) col.getWidth());
        });
        double tableWidth = view.getWidth();

        if (tableWidth > width.get()) {
            view.getColumns().forEach(col -> {
            	if(col.getId()==null || !col.getId().startsWith("content"))
            		col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/(2*(view.getColumns().size()-1)))-1);
            	else
            		col.setPrefWidth(col.getWidth()+((tableWidth-width.get())/2)-1);
            });
        }
    }

//	@FXML
//	private void edit(ActionEvent event) {
//		OilLog oilLogToEdit = logTableView.getSelectionModel().getSelectedItem();
//		edit(oilLogToEdit);
//	};
	private void edit(OilLog oilLogToEdit) {
		Stage dialogStage = new Stage(StageStyle.UNDECORATED);
		dialogStage.setTitle("Edit Log");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setResizable(true);
		dialogStage.initOwner(logTableView.getScene().getWindow());
		
		Parent root = null;
		try {
			if(loaderEditLog.getRoot()==null) {
				loaderEditLog.setLocation(getClass().getResource("EditLog.fxml"));
				root = loaderEditLog.load();
				dialogStage.setScene(new Scene(root));
				JavaFXUtils.undecorate(dialogStage, closeButton1, themeManager);
				ResizeHelper.addResizeListener(dialogStage);
			} else {
				root = loaderEditLog.getRoot();
				dialogStage.setScene(root.getScene());
				ResizeHelper.addResizeListener(dialogStage);
			}
			EditLogController editLogController = (EditLogController)loaderEditLog.getController();
			editLogController.setLogToEdit(oilLogToEdit);
			editLogController.setStage(dialogStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialogStage.showAndWait();
	}
    private void terminate(OilLog oilLog) {
    	if(showConfirmation("Delete Log", "Are you sure to delete this log?")) {
    		operationLogReladomoService.terminateOil1Log(oilLog);
    		remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(al.sybina.ebr.logbook.commons.Activator.LOGS_CHANGED_EVENT_TOPIC, new HashMap<String, String>()));
    	}
		
	}
	private void showModifications(OilLog oilLogToEdit) {
		List<OilLog> logModificationsList = operationLogReladomoService.getLogModificationHistory(oilLogToEdit);
		Stage dialogStage = new Stage(StageStyle.UNDECORATED);
		dialogStage.setTitle("Log Modifications");
		dialogStage.initModality(Modality.WINDOW_MODAL);
		dialogStage.setResizable(true);
		dialogStage.initOwner(logTableView.getScene().getWindow());
		
		Parent root = null;
		try {
			if(loaderLogModifications.getRoot()==null) {
				loaderLogModifications.setLocation(getClass().getResource("LogModifications.fxml"));
				root = loaderLogModifications.load();
				dialogStage.setScene(new Scene(root));
				JavaFXUtils.undecorate(dialogStage, closeButton5, themeManager);
				ResizeHelper.addResizeListener(dialogStage);
			} else {
				root = loaderLogModifications.getRoot();
				dialogStage.setScene(root.getScene());
				ResizeHelper.addResizeListener(dialogStage);
			}
			LogModificationsController logModificationsController = (LogModificationsController)loaderLogModifications.getController();
			logModificationsController.setLogModificationsList(logModificationsList);
			logModificationsController.setActuallyValidLog(oilLogToEdit);
			logModificationsController.setStage(dialogStage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		dialogStage.showAndWait();
	}
    
	
	@FXML
	private void showHistoryPreview(ActionEvent event) {
	    Stage stage = new Stage(StageStyle.UNDECORATED);
		stage.setTitle("History Preview");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(true);
		stage.initOwner(logTableView.getScene().getWindow());
	    
	    Parent root = null;
		try {
			if(loaderHistoryPreview.getRoot()==null) {
				loaderHistoryPreview.setLocation(getClass().getResource("HistoryPreview.fxml"));
				root = loaderHistoryPreview.load();
				stage.setScene(new Scene(root));
				JavaFXUtils.undecorate(stage, closeButton4, themeManager);
				ResizeHelper.addResizeListener(stage);
			} else {
				root = loaderHistoryPreview.getRoot();
				stage.setScene(root.getScene());
				ResizeHelper.addResizeListener(stage);
			}
			HistoryPreviewController historyPreviewController = (HistoryPreviewController)loaderHistoryPreview.getController();
//			historyPreviewController.setLogGroupsMap(logGroupsMap);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    stage.setTitle("History Preview");
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.initOwner(((Node)event.getSource()).getScene().getWindow() );
	    stage.show();
	}
	
	
	@FXML
	private void showPreview(ActionEvent event) {
		ObservableList<OilLog> selectedItems = logTableView.getSelectionModel().getSelectedItems();
		Map<String, Map<String, List<OilLog>>> logSignatureGroupsMap = groupBySignatureGroupsOil1LogMaps(selectedItems);
		logSignatureGroupsMap = addCorrectedHistoryLogs(logSignatureGroupsMap);
				
		Ship ship = marpolBean.getSettings().getShip();
		String shipName = "";
		String shipCode = "";
		String portRegistry = "";
		String grossTonnage = "";
		String periodFrom = "";
		String periodTo = "";
		String partNote = "";
		String partIntroduction = "";
		String partType="";
		String appVersion="V 1.0.0";
		try {
			InputStream resourceAsStream = al.sybina.ebr.rcp.Activator.class.getResourceAsStream("/version.properties");
			Properties prop = new Properties();
			prop.load(resourceAsStream);
			appVersion = "V " + prop.getProperty("releasedVersion.version");
		} catch (IOException e1) {
			logger.error(e1.getMessage(), e1);
		}
		if(ship!=null) {
			shipName = ship.getName();
			shipCode = ship.getShipDistinctiveNumber();
			portRegistry = ship.getShipPortOfRegistry();
			grossTonnage = ship.getShipGrossTonnage();
		}
		if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook")) {
	    	partType="Part I – Machinery space operations";
	    	partNote="NOTE\r\n" + 
					"Oil Record Book Part I shall be provided to every oil tanker of 150 tons gross tonnage and above and every ship of 400 tons gross tonnage and above, other than oil tankers, to record relevant space operations. For oil tankers, Oil Record Book Part II shall also be provided to record relevant cargo/ballast operations.\r\n" + 
					"";
	    	partIntroduction="INTRODUCTION\r\n" + 
	    			"The following pages of this section show a comprehensive list of items of machinery space operations which are, when appropriate, to be recorded in the Oil Record Book, in accordance with Regulation 17 of Annex I of the International Convention for the Prevention of Pollution from Ships, 1973, as modified by the Protocol of 1978 relating thereto (MARPOL 73/ 78). The items have been grouped into operational sections, each of which is denoted by a letter code.\r\n" + 
	    			"When making entries in the Oil Record Book Part I, the date, operational code and item number shall be inserted in the appropriate columns and the required particulars shall be recorded chronologically in the blank spaces. Each completed operation shall be signed for and dated by the officer of officers in charge. The master of the ship shall sign each completed page.\r\n" + 
	    			"The Oil Record Book Part I contains many references to oil quantity. The limited accuracy of tank measurement devices, temperature variations and cling age will affect the accuracy of these readings. The entries in the Oil Record Book should be considered accordingly.\r\n" + 
	    			"In the event of accidental or other exceptional discharge of oil, statement shall be made in the Oil Record Book Part I of the circumstances of, and the reasons for, the discharge.\r\n" + 
	    			"Any failure of the oil filtering equipment shall be noted in the Oil Record Book Part I. \r\n" + 
	    			"The Entries in the Oil Record Book Part I, for ships holding an IOPP Certificate, shall be at least in English, French or Spanish. Where entries in official language of the State whose flag the ship is entitled to fly are also used, this shall prevail in case of a dispute or discrepancy.\r\n" + 
	    			"The Oil Record Book Part I shall be kept in such a place as to be readily available for inspection at all reasonable times and, except in the case of unmanned ships under tow, shall be kept on board the ship. It shall be preserved for a period of three years after the last entry has been made.\r\n" + 
	    			"The competent authority of the Government of a Party to the Convention may inspect the Oil Record Book Part I on board any ship to which this Annex applies while the ship is in its port or offshore terminals and may make a copy of any entry in that book and may require the master of the ship to certify that the copy is a true copy of such entry. Any copy so made which has been certified by the master of the ship as a true copy of an entry in the Oil Record Book Part I shall be made admissible in any juridical proceedings as evidence of the facts stated in the entry. The inspection of an Oil Record Book Part I and the taking of a certified copy by the competent authority under this paragraph shall be performed as expeditiously as possible without causing the ship to be unduly delayed.\r\n" + 
	    			"";
		}else if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook2")){
			partType="Part II – Cargo and ballast operations";
			partNote="NOTE\r\n" + 
					"Oil Record Book Part I shall be provided to every oil tanker of 150 tons gross tonnage and above and every ship of 400 tons gross tonnage and above, other than oil tankers, to record relevant space operations. For oil tankers, Oil Record Book Part II shall also be provided to record relevant cargo/ballast operations.\r\n" + 
					"";
			partIntroduction="INTRODUCTION\r\n" + 
					"The following pages of this section show a comprehensive list of items of cargo and ballast operations which\r\n" + 
					"are, when appropriate, to be recorded in the Oil Record Book Part II, in accordance with Regulation 36 of\r\n" + 
					"Annex I of the International Convention for the Prevention\r\n" + 
					"of Pollution from Ships, 1973, as modified by the protocol of 1978 relating thereto (MARPOL 73/78). The\r\n" + 
					"items have been grouped into operational sections, each of which is denoted by a code letter.\r\n" + 
					"When making entries in the Oil Record Book Part II, the date, operational code and item number shall be\r\n" + 
					"inserted in the appropriate columns and the required particulars shall be recorded chronologically in the\r\n" + 
					"blank spaces.\r\n" + 
					"Each completed operation shall be signed for and dated by the officer or officers in charge. Each completed\r\n" + 
					"page shall be countersigned by the master of the ship.\r\n" + 
					"In respect of the oil tankers engaged in specific trades in accordance with Regulation 2.5 of Annex I of\r\n" + 
					"MARPOL 73/78, appropriate entry in the Oil Record Book part II shall be endorsed by the competent port\r\n" + 
					"State authority [*].\r\n" + 
					"The Oil Record Book Part II contains many references to oil quantity. The limited accuracy of tank\r\n" + 
					"measurement devices, temperature\r\n" + 
					"variations and clingage will affect the accuracy of these readings. The entries in the Oil Record Book Part II\r\n" + 
					"should be considered accordingly.\r\n" + 
					"In the event of accidental or other exceptional discharge of oil, a statement shall be made in the Oil Record\r\n" + 
					"Book Part II of the circumstances of, and the reasons for, the discharge.\r\n" + 
					"Any failure of the oil discharge monitoring and control system shall be noted in the Oil Record Book Part II.\r\n" + 
					"The entries in the Oil Record Book Part II, for ships holding an IOPP Certificate, shall be at least in English,\r\n" + 
					"French or Spanish. Where entries in an official language of the State whose flag the ship is entitled to fly\r\n" + 
					"are also used, this shall prevail in case of a dispute or discrepancy.\r\n" + 
					"The Oil Record Book Part II shall be kept in such a place as to be readily available for inspection at all\r\n" + 
					"reasonable times and, except in the case of unmanned ships under taw, shall be kept on board the ship. It\r\n" + 
					"shall be preserved for a period of three years after the last entry has been made.\r\n" + 
					"The competent authority of the Government of a Party to the Convention may inspect the Oil Record Book\r\n" + 
					"Part II on board the ship to which this Annex applies while the ship is in its port or offshore terminals and\r\n" + 
					"may make a copy of any entry in that book and may require the master of the ship to certify that the copy\r\n" + 
					"is a true copy of such entry. Any copy so made which has been certified by the master of the ship as a true\r\n" + 
					"copy of an entry in the Oil Record Book Part II shall be made admissible in any juridical proceedings as\r\n" + 
					"evidence of the facts stated in the entry.\r\n" + 
					"The inspection of an Oil Record Book Part II and the taking of a certified copy by the competent authority\r\n" + 
					"under this paragraph shall be performed as expeditiously as possible without causing the ship to be unduly\r\n" + 
					"delayed.";
		}
	    List<ReportSignedDataSourceBean> reportSignedDataSourceBeanList = new ArrayList<ReportSignedDataSourceBean>();
		for(Map<String, List<OilLog>> logGroupsMap : logSignatureGroupsMap.values()) {
			OilLog oilLog00 = logGroupsMap.values().stream().findFirst().get().get(0);
			
			String masterSignerName = null;
			String masterSignerSurname = null;
			String masterSignature = null;
			Date masterSigDate = null;
			String refusedToSignMsg = null;
			if(oilLog00.getMasterSignature()!=null) {
				masterSignerName = oilLog00.getMasterSignerName();
				masterSignerSurname = oilLog00.getMasterSignerSurname();
				masterSignature = createTempImageFile(oilLog00.getMasterSignature());
				masterSigDate = oilLog00.getMasterSigDate();
				refusedToSignMsg = oilLog00.getNote()!=null && oilLog00.getNote().contains("REJECTED. Reason:")?"Master refused to sign the entries in this page.\n"+oilLog00.getNote().substring(oilLog00.getNote().indexOf("REJECTED. Reason:")):null;
			}
			
			List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
			for(List<OilLog> oilLogList : logGroupsMap.values()) {
				for(OilLog oilLog : oilLogList) {
					if(oilLog.getCorrectionNote()!=null) {
						oilLog.setSignatureTempFileName(createTempImageFile(oilLog.getSignature()));
					}
				}
				
				OilLog oilLog0 = oilLogList.stream().filter(l-> l.getNote()==null || !l.getNote().contains("Missed Operation")).findFirst().orElse(null);
				OilLog missedLog = oilLogList.stream().filter(l-> l.getNote()!=null && l.getNote().contains("Missed Operation")).findFirst().orElse(null);
				
				String rank = null;
				String name = null;
				String surname = null;
				Date date = null;
				String signatureTempFileName = null;
				String missedRank = (missedLog != null ? missedLog.getSignerRank() : null);
				String missedName = (missedLog != null ? missedLog.getSignerName() : null);
				String missedSurname = (missedLog != null ? missedLog.getSignerSurname() : null);
				Date missedOperationDate = (missedLog != null ? missedLog.getBusinessDate() : null);
				String signaturemMissedTempFileName = (missedLog != null ? createTempImageFile(missedLog.getSignature()) : null);
				
				if (oilLog0 != null) {
					rank = oilLog0.getSignerRank();
					name = oilLog0.getSignerName();
					surname = oilLog0.getSignerSurname();
					date = oilLog0.getBusinessDate();
					signatureTempFileName = createTempImageFile(oilLog0.getSignature());

				} else if (missedLog != null) {
					rank = missedLog.getSignerRank();
					name = missedLog.getSignerName();
					surname = missedLog.getSignerSurname();
				}
				
				ReportDataSourceBean reportDataSourceBean = new ReportDataSourceBean();
				reportDataSourceBean.setShipName(shipName);
				reportDataSourceBean.setShipCode(shipCode);
				reportDataSourceBean.setCompilationDate(date);
				reportDataSourceBean.setRank(rank);
				reportDataSourceBean.setName(name);
				reportDataSourceBean.setSurname(surname);
				reportDataSourceBean.setSignatureFileName(signatureTempFileName);
				reportDataSourceBean.setOilLogList(oilLogList);
				reportDataSourceBean.setMissedRank(missedRank);
				reportDataSourceBean.setMissedName(missedName);
				reportDataSourceBean.setMissedSurname(missedSurname);
				reportDataSourceBean.setCompilationDateMissedLetter(missedOperationDate);
				reportDataSourceBean.setSignatureFileNameMissedLetter(signaturemMissedTempFileName);
				reportDataSourceBeanList.add(reportDataSourceBean);
			}
			
			
			ReportSignedDataSourceBean reportSignedDataSourceBean = new ReportSignedDataSourceBean();
			reportSignedDataSourceBean.setMasterSignerName(masterSignerName);
			reportSignedDataSourceBean.setMasterSignerSurname(masterSignerSurname);
			reportSignedDataSourceBean.setMasterSignatureFilename(masterSignature);
			reportSignedDataSourceBean.setMasterSigDate(masterSigDate);
			reportSignedDataSourceBean.setRefusedToSignMsg(refusedToSignMsg);
			reportSignedDataSourceBean.setReportDataSourceBeanList(reportDataSourceBeanList);
			reportSignedDataSourceBeanList.add(reportSignedDataSourceBean);
		}

		Collections.sort(reportSignedDataSourceBeanList);
		for(ReportSignedDataSourceBean reportSignedDataSourceBean : reportSignedDataSourceBeanList) {
			Collections.sort(reportSignedDataSourceBean.getReportDataSourceBeanList());
			for(ReportDataSourceBean reportDataSourceBean : reportSignedDataSourceBean.getReportDataSourceBeanList()) {
				Collections.sort(reportDataSourceBean.getOilLogList());
			}
		}
		
		logger.info("showPreview");
		JasperReportDetails det = new JasperReportDetails();
		det.setDescription("Oil Record Book Log Part 1");
		
		Bundle bundle = org.eclipse.core.runtime.Platform.getBundle(Activator.PLUGIN_ID);
		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME+"/reports/LogbookPart1_History.jasper");
		File file = new File(filePath);
		det.setMasterReportUrl(file.getAbsolutePath());
		
		
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("shipName", shipName);
		reportParams.put("shipCode", shipCode);
		reportParams.put("partType", partType);
		reportParams.put("portRegistry", portRegistry);
		reportParams.put("grossTonnage", grossTonnage);
		reportParams.put("partNote", partNote);
		reportParams.put("partIntroduction", partIntroduction);
		reportParams.put("partIntroduction", partIntroduction);
		reportParams.put("appVersion", appVersion);
		//String periodFrom = "";
		//String periodTo = "";
		det.setReportParameters(reportParams);
		try {
			
			File pdffile = jasperReportsService.getJasperReportsFactory().complieReportAsPdfFile(new File(PluginTools.getFileLocalToRuntime("reports")+"/Logbook_Signed.pdf"), det, reportParams, reportSignedDataSourceBeanList);
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.setTitle("Logbook Report");
			baseViewerFX.start(stage, pdffile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("end showPreview");
	}
	private Map<String, Map<String, List<OilLog>>> addCorrectedHistoryLogs(Map<String, Map<String, List<OilLog>>> logSignatureGroupsMap) {
		for(String signatureUUID : logSignatureGroupsMap.keySet()) {
			Map<String, List<OilLog>> groupsOil1LogLists = logSignatureGroupsMap.get(signatureUUID);
			for(String groupUuid : groupsOil1LogLists.keySet()) {
				List<OilLog> list = groupsOil1LogLists.get(groupUuid);
				List<OilLog> simpleLogModificationsList = new ArrayList<OilLog>();
				for(OilLog oilLog : list) {
					List<OilLog> logModificationsList = operationLogReladomoService.getLogModificationHistory(oilLog);
					for(OilLog ol : logModificationsList) {
						if(!simpleLogModificationsList.contains(ol) && !ol.equals(oilLog)) {
							simpleLogModificationsList.add(ol);
						}
					}
				}
				list.addAll(simpleLogModificationsList);
			}
		}
		return logSignatureGroupsMap;
	}
	private String createTempImageFile(String signatureString) {
		WritableImage signatureImage = null;
		try {
			byte[] signatureBytes = Base64.getDecoder().decode(signatureString);
			BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
			signatureImage = SwingFXUtils.toFXImage(bImage, null);
		}catch (Exception e) {
			e.printStackTrace();
		} 
		File tempSignatureFile = null;
		File tempFolder = null;
		try {
			tempFolder = new File(PluginTools.getFileLocalToRuntime("reports"));
			tempFolder.mkdir();
			tempSignatureFile = File.createTempFile("signature", ".png", tempFolder);
			OutputStream os = new FileOutputStream(tempSignatureFile);
			ImageIO.write(SwingFXUtils.fromFXImage(signatureImage, null), "png", os);
			os.flush();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tempSignatureFile.deleteOnExit();
		return tempSignatureFile.getName();
	}
	private File createTempPdfFile(byte[] pdfFileBytes) {
		File tempPdfFile = null;
		File tempFolder = null;
		try {
			tempFolder = new File(PluginTools.getFileLocalToRuntime("reports"));
			tempFolder.mkdir();
			tempPdfFile = File.createTempFile("pdf", ".pdf", tempFolder);
			OutputStream os = new FileOutputStream(tempPdfFile);
			os.write(pdfFileBytes);
			os.flush();
			os.close();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		tempPdfFile.deleteOnExit();
		return tempPdfFile;
	}

	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
	public static boolean showConfirmation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Confirmation");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        Optional<ButtonType> optional = alert.showAndWait();
        if(optional.isPresent() && optional.get()==ButtonType.OK) return true;
        else return false;
    }

	@FXML
	private void saveDialogMaster(ActionEvent event) {
		ObservableList<OilLog> selectedItems = logTableView.getSelectionModel().getSelectedItems();
		List<String> signatureGroupUIIDList = new ArrayList<String>();
		for(OilLog oilLog : selectedItems) {
			signatureGroupUIIDList.add(oilLog.getSignatureGroupUuid());
		}
		Map<String, List<OilLog>> logSignatureGroupsMap = groupByGroupOil1Log(selectedItems);
		
		
	    Stage stage = new Stage(StageStyle.UNDECORATED);
	    stage.setTitle("Master Sign & Save");
		stage.initModality(Modality.WINDOW_MODAL);
		stage.setResizable(true);
		stage.initOwner(logTableView.getScene().getWindow());
	    Parent root = null;
		try {
			if(loaderMaster.getRoot()==null) {
				loaderMaster.setLocation(getClass().getResource("SaveMaster.fxml"));
				root = loaderMaster.load();
				stage.setScene(new Scene(root));
				JavaFXUtils.undecorate(stage, closeButton3, themeManager);
				ResizeHelper.addResizeListener(stage);
			} else {
				root = loaderMaster.getRoot();
				stage.setScene(root.getScene());
				ResizeHelper.addResizeListener(stage);
			}
			SaveMasterController saveController = (SaveMasterController)loaderMaster.getController();
			saveController.setLogGroupsMap(logSignatureGroupsMap);
			saveController.setStage(stage);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    
	    stage.setTitle("Sign & Save");
	    stage.initModality(Modality.WINDOW_MODAL);
	    stage.initOwner(
	        ((Node)event.getSource()).getScene().getWindow() );
	    stage.show();
	}
	
	
	
	private Map<String, Map<String, List<OilLog>>> groupBySignatureGroupsOil1LogMaps(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> signatureGroupedOil1Logs = groupBySignatureOil1Log(allOilLogList);
		Map<String, Map<String, List<OilLog>>> signatureAndGroupsGrouped = groupByGroupOil1LogMaps(signatureGroupedOil1Logs);
		return signatureAndGroupsGrouped;
	}
	
	private Map<String, List<OilLog>> groupBySignatureOil1Log(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : allOilLogList) {
			List<OilLog> list = resultMap.get(oilLog.getSignatureGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getSignatureGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}

	private Map<String, List<OilLog>> groupByGroupOil1Log(List<OilLog> allOilLogList) {
		Map<String, List<OilLog>> resultMap = new HashMap<String, List<OilLog>>();
		for(OilLog oilLog : allOilLogList) {
			List<OilLog> list = resultMap.get(oilLog.getGroupUuid());
			if(list==null) {
				List<OilLog> list2 = new ArrayList<OilLog>();
				list2.add(oilLog);
				resultMap.put(oilLog.getGroupUuid(), list2);
			} else {
				list.add(oilLog);
			}
		}
		return resultMap;
	}
	
	private Map<String, Map<String, List<OilLog>>> groupByGroupOil1LogMaps(Map<String, List<OilLog>> signatureGroupedOil1Logs) {
		Map<String, Map<String, List<OilLog>>> result = new HashMap<String, Map<String, List<OilLog>>>();
		for(String signatureUUID : signatureGroupedOil1Logs.keySet()) {
			List<OilLog> oilLogList = signatureGroupedOil1Logs.get(signatureUUID);
			Map<String, List<OilLog>> groupsOil1LogLists = new HashMap<String, List<OilLog>>();
			for(OilLog oilLog : oilLogList) {
				List<OilLog> list = groupsOil1LogLists.get(oilLog.getGroupUuid());
				if(list==null) {
					List<OilLog> list2 = new ArrayList<OilLog>();
					list2.add(oilLog);
					groupsOil1LogLists.put(oilLog.getGroupUuid(), list2);
				} else {
					list.add(oilLog);
				}
			}
			result.put(signatureUUID, groupsOil1LogLists);
		}
		return result;
	}
	
	
	
	
	
	
	

	
	
	private Button createDownloadIconButton(SVGGlyph glyph, OilLog oilLog) {
        JFXButton button = new JFXButton(null, glyph);
        button.setPrefSize(30,30);
        button.setMinSize(30,30);
        button.setMaxSize(30,30);
        button.ripplerFillProperty().setValue(Color.LIGHTSALMON);
        button.setOnAction(event -> {
        	showAttachment(oilLog);
        });
        Tooltip.install(button, new Tooltip("Edit"));
        return button;
    }
    private Button createDeleteIconButton(SVGGlyph glyph, OilLog oilLog) {
    	glyph.setSize(24);
        JFXButton button = new JFXButton(null, glyph);
        button.setPrefSize(30,30);
        button.setMinSize(30,30);
        button.setMaxSize(30,30);
        button.ripplerFillProperty().setValue(Color.LIGHTSALMON);
        button.setOnAction(event -> {
        	terminate(oilLog);
        });
        Tooltip.install(button, new Tooltip("Delete"));
        return button;
    }
	private Button createEditIconButton(SVGGlyph glyph, OilLog oilLog) {
		glyph.setSize(24);
        JFXButton button = new JFXButton(null, glyph);
        button.setPrefSize(30,30);
        button.setMinSize(30,30);
        button.setMaxSize(30,30);
        button.ripplerFillProperty().setValue(Color.LIGHTSALMON);
        button.setOnAction(event -> {
        	edit(oilLog);
        });
        Tooltip.install(button, new Tooltip("Edit"));
        return button;
    }
	private Button createModificationsButton(SVGGlyph glyph, OilLog oilLog) {
		glyph.setSize(24);
        JFXButton button = new JFXButton(null, glyph);
        button.setPrefSize(30,30);
        button.setMinSize(30,30);
        button.setMaxSize(30,30);
        button.ripplerFillProperty().setValue(Color.LIGHTSALMON);
        button.setOnAction(event -> {
        	showModifications(oilLog);
        });
        Tooltip.install(button, new Tooltip("Modifications"));
        return button;
    }
	
	private boolean isActual(OilLog item) {
		return item.getBusinessDateTo().equals(TimestampProvider.getInfinityDate()) && item.getProcessingDate().equals(TimestampProvider.getInfinityDate());
	}
	
	private String getOilLogType() {
		String oilLogType = null;
		String currentActivePerspecticeId = viewPerspectiveHelper.getActivePerspectiveId();
		if(Activator.OIL_LOG_TYPE_1_PERSPECTIVE_ID.equals(currentActivePerspecticeId)) oilLogType = Activator.OIL_LOG_TYPE_1;
		if(Activator.OIL_LOG_TYPE_2_PERSPECTIVE_ID.equals(currentActivePerspecticeId)) oilLogType = Activator.OIL_LOG_TYPE_2;
		return oilLogType;
	}
	
	private void showAttachment(OilLog oilLog) {
		try {
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage stage = new Stage();
			stage.initModality(Modality.APPLICATION_MODAL);
			stage.initOwner(logTableView.getScene().getWindow());
			stage.setTitle("Receipt");
			baseViewerFX.start(stage, createTempPdfFile(oilLog.getAttachmentData()));
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		}
		logger.info("end showPreview");
	}
	private void downloadAttachment(OilLog oilLog) {
		Stage stage = (Stage) logTableView.getScene().getWindow();
        File fileToSave;
        String fileToSavePath;
        boolean finished = false;

        while (!finished) {
            final FileChooser fileChooser = new FileChooser();
//            fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("PDF file (*.pdf)", "*.pdf"),
//                    new FileChooser.ExtensionFilter("FDF file (*.fdf)", "*.fdf"));

            fileChooser.setInitialFileName(oilLog.getAttachmentName());

            fileToSave = fileChooser.showSaveDialog(stage.getOwner());

            FileOutputStream fos = null;

            if (fileToSave != null) {

                fileToSavePath = fileToSave.getAbsolutePath();

                if (!fileToSavePath.endsWith(".pdf")) {
                    fileToSavePath += ".pdf";
                    fileToSave = new File(fileToSavePath);
                }

                if (fileToSavePath.equals(fileToSave)) {
                    return;
                }

                if (fileToSave.exists()) {
                	String message = fileToSavePath + '\n'
                    + "The file already exists." + '\n'
                    + "Replace the existing file?";
                	String message2 = "Replace the existing file";
                    final FXOptionDialog optionsDialog = new FXOptionDialog(stage, message, message2, FXOptionDialog.YES_NO_OPTION, null, null);
                    final int n =  optionsDialog.showOptionDialog();
                    if (n == 1) {
                        continue;
                    }
                }

                try {
                    fos = new FileOutputStream(fileToSavePath);
                    fos.write(oilLog.getAttachmentData());
                } catch (final Exception e1) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            final FXMessageDialog dialog = new FXMessageDialog(stage, "Error while saving" + ' ' + e1);
                            dialog.show();
                        }
                    });
                }

                try {
                    fos.close();
                } catch (final Exception e2) {
                    LogWriter.writeLog("Exception attempting to Read File: " + e2);
                }
            }
            finished = true;
        
        };
	}

	public LocalDateTimeTextField getBusinessDatePicker() {
		return businessDatePicker;
	}

	public CheckBox getBusinessDateCheckBox() {
		return businessDateCheckBox;
	}

	public LocalDateTimeTextField getProcessingDatePicker() {
		return processingDatePicker;
	}

	public CheckBox getProcessingDateCheckBox() {
		return processingDateCheckBox;
	}
	
	
}
