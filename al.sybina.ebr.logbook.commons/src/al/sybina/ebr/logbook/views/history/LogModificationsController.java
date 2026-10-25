package al.sybina.ebr.logbook.views.history;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicLong;

import javax.imageio.ImageIO;
import javax.inject.Inject;

import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.office.Ship;
import com.oceanpro.core.services.OperationLogReladomoService;
import com.sun.javafx.binding.ObjectConstant;

import al.sybina.ebr.commons.utils.ImageCell;
import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.jasperreports.BaseViewerFX;
import al.sybina.ebr.jasperreports.JasperReportDetails;
import al.sybina.ebr.jasperreports.JasperReportsService;
import al.sybina.ebr.logbook.commons.Activator;
import al.sybina.ebr.logbook.commons.ReportDataSourceBean;
import al.sybina.ebr.logbook.domain.reladomo.OilLog;
import al.sybina.ebr.logbook.domain.reladomo.TimestampProvider;
import al.sybina.ebr.logbook.signature.SignatureMaster;
import al.sybina.ebr.logbook.views.history.LogModificationsController.OilLogWithChangedFields;
import al.sybina.ebr.logbook.views.marpoldetail.MarpolBean;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TableView.ResizeFeatures;
import javafx.scene.control.Tooltip;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class LogModificationsController implements Initializable {
	
	private static final Logger logger = LoggerCreator.createLogger(LogModificationsController.class);
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	private static ThemeManager themeManager;
	@Inject
	@Service
	private JasperReportsService jasperReportsService;
	@Inject
	private MarpolBean marpolBean;
	@Inject
	@Service
	private OperationLogReladomoService operationLogReladomoService;
	
	@FXML
	private AnchorPane mainAnchorPane;
	@FXML
	private TableView<OilLogWithChangedFields> logTableView;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> typeCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> dateCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> nameCreatorCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, ImageView> signatureCreatorCol;
	
	@FXML
	private TableColumn<OilLogWithChangedFields, HBox> masterCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> correctionNoteCol;
	
	@FXML
	private TableColumn<OilLogWithChangedFields, String> codeLetterGroupCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> codeNumberGroupCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> contentGroupCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> correctionDateCol;
	@FXML
	private TableColumn<OilLogWithChangedFields, String> noteCol;
    
		
	@FXML
	private Button detailedButton;
	@FXML
	private Button simpleButton;
	@FXML
	private Button showPreviewButton;
	
	private static JFXButton closeButton;
	
	private List<OilLog> logModificationsList;
	private List<OilLog> simpleLogModificationsList;
	
	private Stage stage;

	private boolean simple = true;

	private OilLog actuallyValidLog;

	

	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
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
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		javafx.util.Callback<TableColumn<OilLogWithChangedFields, String>, TableCell<OilLogWithChangedFields, String>> markChangedCallback = new Callback<TableColumn<OilLogWithChangedFields,String>, TableCell<OilLogWithChangedFields,String>>() {
			@Override
			public TableCell<OilLogWithChangedFields, String> call(TableColumn<OilLogWithChangedFields, String> column) {
				TableCell<OilLogWithChangedFields, String> cell = new TableCell<OilLogWithChangedFields, String>() {
					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);
						Line line = new Line();
						Line line2 = new Line();
						boolean changed = false;
						String columnId = getTableColumn().getId();
						if(getTableRow()!=null && getTableRow().getItem()!=null) {
							switch(columnId) {
								case "typeCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isTypeChanged();
									break;
								case "dateCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isBusinessDateFromChanged();
									break;
								case "codeLetterGroupCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isCodeLetterGroupChanged();
									break;
								case "codeNumberGroupCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isCodeNumberGroupChanged();
									break;
								case "contentGroupCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isContentGroupChanged();
									break;
								case "nameCreatorCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isNameCreatorChanged();
									break;
								case "signatureCreatorCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isSignatureCreatorChanged();
									break;
								case "masterCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isMasterChanged();
									break;
								case "correctionNoteCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isCorrectionNoteChanged();
									break;
								case "correctionDateCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isCorrectionDateChanged();
									break;
								case "noteCol":
									changed = ((OilLogWithChangedFields)getTableRow().getItem()).isNoteChanged();
									break;	
								default:
									changed = false;
							}
						}
						if(changed) {
							if(item!=null && !empty) {
								if(item.contains("\n")) {
									line.setStroke(Color.RED);
							        line.setStartX(5);
							        line.setStartY(5);
							        line.endXProperty().bind(this.widthProperty().subtract(5));
							        line.endYProperty().bind(this.heightProperty().subtract(5));
							        
									line2.setStroke(Color.RED);
							        line2.setStartX(5);
							        line2.startYProperty().bind(this.heightProperty().subtract(5));
							        line2.endXProperty().bind(this.widthProperty().subtract(5));
							        line2.setEndY(5);
							        setGraphic(new Group(line, line2));
							        setContentDisplay(ContentDisplay.CENTER);
								} else {
									line.setStroke(Color.RED);
									line.setStartX(5);
							        line.setStartY(0);
							        line.endXProperty().bind(this.widthProperty().subtract(5));
							        line.setEndY(0);
							        setGraphic(line);
							        setContentDisplay(ContentDisplay.CENTER);
								}

							} else {
								setGraphic(null);
							}
						} else {
							setGraphic(null);
						}
						this.setText(item);
						this.setTooltip((empty || item==null) ? null : new Tooltip(item));
					}
				};
				return cell;
			}
		};
		typeCol.setCellFactory(markChangedCallback);
		typeCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getType());
		});
		
		dateCol.setCellFactory(markChangedCallback);
		dateCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
	        SimpleStringProperty property = new SimpleStringProperty();
	        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	        property.setValue(dateFormat.format(p.getBusinessDateFrom()));
	        return property;
		});
		
		nameCreatorCol.setCellFactory(markChangedCallback);
		nameCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getSignerRank() + " " + p.getSignerName() + " " + p.getSignerSurname());
		});
		
//		signatureCreatorCol.setCellFactory(column->{
//			TableCell<OilLogWithChangedFields, ImageView> cell = new TableCell<OilLogWithChangedFields, ImageView>(){
//				Line line = new Line();
//				@Override
//				protected void updateItem(ImageView item, boolean empty) {
//					super.updateItem(item, empty);
//					if(getTableRow()!=null && getTableRow().getItem()!=null && ((OilLogWithChangedFields)getTableRow().getItem()).isSignatureCreatorChanged()) {
//						if(!empty) {
//							line.setStroke(Color.RED);
//					        line.startXProperty().bind(this.layoutXProperty());
//					        line.startYProperty().bind(this.layoutYProperty().divide(2.0));
//					        line.endXProperty().bind(this.layoutXProperty().add(this.widthProperty()));
//					        line.endYProperty().bind(this.layoutYProperty().divide(2.0));
//					        setGraphic(new StackPane(item, line));
//					        setContentDisplay(ContentDisplay.CENTER);
//						} else {
//							setGraphic(null);
//						}
//					} else {
//						if(!empty) {
//							setGraphic(item);
//						} else {
//							setGraphic(null);
//						}
//					}
//				}
//			};
//			return cell;
//		});
		signatureCreatorCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			byte[] signatureBytes = Base64.getDecoder().decode(p.getSignature());
			try {
				BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
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
		
		
		masterCol.setCellFactory(column->{
			TableCell<OilLogWithChangedFields, HBox> cell = new TableCell<OilLogWithChangedFields, HBox>(){
				Line line = new Line();
				@Override
				protected void updateItem(HBox item, boolean empty) {
					super.updateItem(item, empty);
					if(getTableRow()!=null && getTableRow().getItem()!=null && ((OilLogWithChangedFields)getTableRow().getItem()).isMasterChanged()) {
						if(item!=null && !empty) {
							line.setStroke(Color.RED);
							line.setStartX(5);
					        line.setStartY(0);
					        line.endXProperty().bind(this.widthProperty().subtract(5));
					        line.setEndY(0);
					        setGraphic(new StackPane(item, line));
					        setContentDisplay(ContentDisplay.CENTER);
						} else {
							setGraphic(null);
						}
					} else {
						if(item!=null && !empty) {
							setGraphic(item);
						} else {
							setGraphic(null);
						}
					}
				}
			};
			return cell;
		});
		masterCol.setCellValueFactory(cellData -> {
			if(cellData.getValue().getOilLog().getMasterSignerName()==null || cellData.getValue().getOilLog().getMasterSignerName().equals("")) {
				return null;
			} else {
				OilLog p = cellData.getValue().getOilLog();
				byte[] signatureBytes = Base64.getDecoder().decode(p.getMasterSignature());
				ImageView imageView;
				try {
					BufferedImage bImage = ImageIO.read(new ByteArrayInputStream(signatureBytes));
					WritableImage image = SwingFXUtils.toFXImage(bImage, null);
					imageView = new ImageView(image);
					imageView.setFitWidth(50);
					imageView.setFitHeight(30);
				} catch (IOException e) {
					e.printStackTrace();
					return null;
				}
				HBox hBox = new HBox(new Label(p.getMasterSignerName() + " " + p.getMasterSignerSurname()), imageView);
				hBox.setAlignment(Pos.CENTER);
				hBox.getStyleClass().add("chiefEngMasterHbox");
				return ObjectConstant.valueOf(hBox);
			}
		});
		
		correctionNoteCol.setCellFactory(markChangedCallback);
		correctionNoteCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getCorrectionNote());
		});
		
		codeLetterGroupCol.setCellFactory(markChangedCallback);
		codeLetterGroupCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getCodeLetter());
		});
		
		codeNumberGroupCol.setCellFactory(markChangedCallback);
		codeNumberGroupCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getCodeNumber());
		});
		
		contentGroupCol.setCellFactory(markChangedCallback);
		contentGroupCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getContent());
		});
		
//		correctionDateCol.setCellFactory(markChangedCallback);
		correctionDateCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
	        SimpleStringProperty property = new SimpleStringProperty();
	        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm");
	        property.setValue(dateFormat.format(p.getProcessingDateFrom()));
	        return property;
		});
		
		noteCol.setCellFactory(markChangedCallback);
		noteCol.setCellValueFactory(cellData -> {
			OilLog p = cellData.getValue().getOilLog();
			return new ReadOnlyStringWrapper(p.getNote());
		});
		
		logTableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		//logTableView.setSelectionModel(null);
		
		mainAnchorPane.widthProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				Platform.runLater(() -> {
					customResize(logTableView);
				});
		}});
		
		eventBroker.subscribe(Activator.LOGS_CHANGED_EVENT_TOPIC, new org.osgi.service.event.EventHandler() {
			@Override
			public void handleEvent(Event event) {
				ArrayList<Long> ids = (ArrayList<Long>) event.getProperty(Activator.LOG_IDS);
    			if(ids==null || (actuallyValidLog!=null && ids.contains(actuallyValidLog.getId()))) {
    				cancel();
    			}
			}
		});
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
	
	
	
	@FXML
	private void showPreview(ActionEvent event) {
		logger.info("showPreview");		
		
//		List<String> groupUIIDList = new ArrayList<String>();
//		for(OilLog oilLog : result.getValue()) {
//			groupUIIDList.add(oilLog.getUuid());
//		}
//		Map<String, List<OilLog>> logGroupsMap = operationLogService.getGroupsOil1LogList(groupUIIDList);
		Ship ship = marpolBean.getSettings().getShip();
		String shipName = "";
		String shipCode = "";
		if(ship!=null) {
			shipName = ship.getName();
			shipCode = ship.getShipDistinctiveNumber();
		}
		StringBuilder partAndVersion= new StringBuilder();

	    if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook")) {
			partAndVersion.append("I ");
		}else if (viewPerspectiveHelper.getActivePerspectiveId().equals("al.sybina.ebr.logbook.ui.perspective.logbook2")){
			partAndVersion.append("II ");
		}
	    partAndVersion.append("V 1.0.0");
		List<ReportDataSourceBean> reportDataSourceBeanList = new ArrayList<ReportDataSourceBean>();
//		for(List<OilLog> oilLogList : logGroupsMap.values()) {
		if(simpleLogModificationsList!=null && !simpleLogModificationsList.isEmpty()) {
			List<OilLog> oilLogList = simpleLogModificationsList;
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
			reportDataSourceBean.setPartAndVersion(partAndVersion.toString());
			reportDataSourceBean.setCompilationDate(date);
			reportDataSourceBean.setRank(rank);
			reportDataSourceBean.setName(name);
			reportDataSourceBean.setSurname(surname);
			reportDataSourceBean.setSignatureFileName(signatureTempFileName);
			reportDataSourceBean.setMissedRank(missedRank);
			reportDataSourceBean.setMissedName(missedName);
			reportDataSourceBean.setMissedSurname(missedSurname);
			reportDataSourceBean.setCompilationDateMissedLetter(missedOperationDate);
			reportDataSourceBean.setSignatureFileNameMissedLetter(signaturemMissedTempFileName);
			oilLogList.sort(Comparator.comparing(OilLog::getProcessingDate).thenComparing(OilLog::getProcessingDateTo).thenComparing(OilLog::getBusinessDateTo));
			reportDataSourceBean.setOilLogList(oilLogList);
			reportDataSourceBeanList.add(reportDataSourceBean);
		}
		
		
		
		JasperReportDetails det = new JasperReportDetails();
		det.setDescription("Oil Record Book Log Part 1");
		
		String filePath = PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME+"/reports/LogbookPart1_Modifications.jasper");
		File file = new File(filePath);
		det.setMasterReportUrl(file.getAbsolutePath());
		
		
		Map<String, Object> reportParams = new HashMap<String, Object>();
		reportParams.put("shipName", shipName);
		reportParams.put("shipCode", shipCode);
		reportParams.put("elementsSize", simpleLogModificationsList.size());
		reportParams.put("partAndVersion", partAndVersion.toString());
		det.setReportParameters(reportParams);
		try {
			File pdffile = jasperReportsService.getJasperReportsFactory().complieReportAsPdfFile(new File(PluginTools.getFileLocalToRuntime("reports")+"/Logbook.pdf"), det, reportParams, reportDataSourceBeanList);
			BaseViewerFX baseViewerFX = new BaseViewerFX();
			Stage reportStage = new Stage();
			reportStage.initOwner(((Node)event.getSource()).getScene().getWindow());
			reportStage.initModality(Modality.APPLICATION_MODAL);
			reportStage.setTitle("Logbook Report");
			baseViewerFX.start(reportStage, pdffile);

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		logger.info("end showPreview");
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
	
	
	
	
	
	public static void showInformation(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initStyle(StageStyle.UTILITY);
        alert.setTitle("Information");
        alert.setHeaderText(title);
        alert.setContentText(message);
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
    }
	
	

	public void setLogModificationsList(List<OilLog> logList) {
		this.logModificationsList = logList;
		List<OilLog> simpleLogModificationsList = new ArrayList<OilLog>();
		for(OilLog oilLog : logList) {
			if(!simpleLogModificationsList.contains(oilLog)) {
				simpleLogModificationsList.add(oilLog);
			}
		}
		this.simpleLogModificationsList = simpleLogModificationsList;
		initTableView(simpleLogModificationsList);
		simple();
		logTableView.refresh();
		JavaFXUtils.autoFitTable(logTableView);
	}

	private void initTableView(List<OilLog> logList) {
		logTableView.getItems().clear();
		Comparator<OilLog> dateComparator = Comparator.comparing(olcf -> olcf.getProcessingDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		Comparator<OilLog> secondDateComparator = Comparator.comparing(olcf -> olcf.getProcessingDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		Comparator<OilLog> thirdDateComparator = Comparator.comparing(olcf -> olcf.getBusinessDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		logList.sort(dateComparator.reversed().thenComparing(secondDateComparator.reversed()).thenComparing(thirdDateComparator.reversed()));
		OilLog lastLog = null;
		for (OilLog log : logList) {
			logTableView.getItems().add(new OilLogWithChangedFields(log, lastLog));
			lastLog = log;
		}
		Comparator<OilLogWithChangedFields> dateComparator2 = Comparator.comparing(olcf -> olcf.getOilLog().getProcessingDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		Comparator<OilLogWithChangedFields> secondDateComparator2 = Comparator.comparing(olcf -> olcf.getOilLog().getProcessingDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		Comparator<OilLogWithChangedFields> thirdDateComparator2 = Comparator.comparing(olcf -> olcf.getOilLog().getBusinessDateTo().toInstant().truncatedTo( ChronoUnit.SECONDS ));
		logTableView.getItems().sort(dateComparator2.thenComparing(secondDateComparator2).thenComparing(thirdDateComparator2));
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@FXML
	public void simple() {
		simple  = true;
		detailedButton.setDisable(false);
		showPreviewButton.setDisable(false);
		simpleButton.setDisable(true);
		masterCol.setVisible(false);
//		correctionDateCol.setVisible(false);
		noteCol.setVisible(false);
		initTableView(simpleLogModificationsList);
		customResize(logTableView);
	}
	
	@FXML
	public void detailed() {
		simple = false;
		detailedButton.setDisable(true);
		showPreviewButton.setDisable(true);
		simpleButton.setDisable(false);
		masterCol.setVisible(true);
//		correctionDateCol.setVisible(true);
		noteCol.setVisible(true);
		initTableView(logModificationsList);
		customResize(logTableView);
	}
	
	@FXML
	public void cancel() {
		stage.close();
	}
	
	class OilLogWithChangedFields {
		private OilLog oilLog;
		private boolean typeChanged;
		private boolean businessDateFromChanged;
		private boolean nameCreatorChanged;
		private boolean signatureCreatorChanged;
		private boolean masterChanged;
		private boolean correctionNoteChanged;
		private boolean codeLetterGroupChanged;
		private boolean codeNumberGroupChanged;
		private boolean contentGroupChanged;
		private boolean correctionDateChanged;
		private boolean noteChanged;
		
		public OilLogWithChangedFields(OilLog log, OilLog lastLog) {
			this.oilLog = log;
			if(lastLog==null) return;
			if((log.getType()==null && lastLog.getType()==null) || (log.getType()!=null && log.getType().equals(lastLog.getType()))) {
				typeChanged = false;
			} else {
				typeChanged = true;
			}
			if((log.getBusinessDateFrom()==null && lastLog.getBusinessDateFrom()==null) || (log.getBusinessDateFrom()!=null && log.getBusinessDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS ).equals(lastLog.getBusinessDateFrom().toInstant().truncatedTo( ChronoUnit.SECONDS )))) {
				businessDateFromChanged = false;
			} else {
				businessDateFromChanged = true;
			}
			if((log.getSignerRank()==null && lastLog.getSignerRank()==null) || (log.getSignerRank()!=null && log.getSignerRank().equals(lastLog.getSignerRank()))) {
				if((log.getSignerName()==null && lastLog.getSignerName()==null) || (log.getSignerName()!=null && log.getSignerName().equals(lastLog.getSignerName()))) {
					if((log.getSignerSurname()==null && lastLog.getSignerSurname()==null) || (log.getSignerSurname()!=null && log.getSignerSurname().equals(lastLog.getSignerSurname()))) {
						nameCreatorChanged = false;
					} else {
						nameCreatorChanged = true;
					}
				} else {
					nameCreatorChanged = true;
				}
			} else {
				nameCreatorChanged = true;
			}
			if((log.getSignature()==null && lastLog.getSignature()==null) || (log.getSignature()!=null && log.getSignature().equals(lastLog.getSignature()))) {
				signatureCreatorChanged = false;
			} else {
				signatureCreatorChanged = true;
			}
			if((log.getMasterSignerName()==null && lastLog.getMasterSignerName()==null) || (log.getMasterSignerName()!=null && log.getMasterSignerName().equals(lastLog.getMasterSignerName()))) {
				if((log.getMasterSignerSurname()==null && lastLog.getMasterSignerSurname()==null) || (log.getMasterSignerSurname()!=null && log.getMasterSignerSurname().equals(lastLog.getMasterSignerSurname()))) {
					if((log.getMasterSignature()==null && lastLog.getMasterSignature()==null) || (log.getMasterSignature()!=null && log.getMasterSignature().equals(lastLog.getMasterSignature()))) {
						masterChanged = false;
					} else {
						masterChanged = true;
					}
				} else {
					masterChanged = true;
				}
			} else {
				masterChanged = true;
			}
			if((log.getCorrectionNote()==null && lastLog.getCorrectionNote()==null) || (log.getCorrectionNote()!=null && log.getCorrectionNote().equals(lastLog.getCorrectionNote()))) {
				correctionNoteChanged = false;
			} else {
				correctionNoteChanged = true;
			}
			if((log.getCodeLetter()==null && lastLog.getCodeLetter()==null) || (log.getCodeLetter()!=null && log.getCodeLetter().equals(lastLog.getCodeLetter()))) {
				codeLetterGroupChanged = false;
			} else {
				codeLetterGroupChanged = true;
			}
			if((log.getCodeNumber()==null && lastLog.getCodeNumber()==null) || (log.getCodeNumber()!=null && log.getCodeNumber().equals(lastLog.getCodeNumber()))) {
				codeNumberGroupChanged = false;
			} else {
				codeNumberGroupChanged = true;
			}
			if((log.getContent()==null && lastLog.getContent()==null) || (log.getContent()!=null && log.getContent().equals(lastLog.getContent()))) {
				contentGroupChanged = false;
			} else {
				contentGroupChanged = true;
			}
			if((log.getProcessingDateFrom()==null && lastLog.getProcessingDateFrom()==null) || (log.getProcessingDateFrom()!=null && log.getProcessingDateFrom().equals(lastLog.getProcessingDateFrom()))) {
				correctionDateChanged = false;
			} else {
				correctionDateChanged = true;
			}
			if((log.getNote()==null && lastLog.getNote()==null) || (log.getNote()!=null && log.getNote().equals(lastLog.getNote()))) {
				noteChanged = false;
			} else {
				noteChanged = true;
			}
		}
		public OilLog getOilLog() {
			return oilLog;
		}
		public void setOilLog(OilLog oilLog) {
			this.oilLog = oilLog;
		}
		
		
		
		public boolean isTypeChanged() {
			return typeChanged;
		}
		public void setTypeChanged(boolean typeChanged) {
			this.typeChanged = typeChanged;
		}
		public boolean isBusinessDateFromChanged() {
			return businessDateFromChanged;
		}
		public void setBusinessDateFromChanged(boolean businessDateFromChanged) {
			this.businessDateFromChanged = businessDateFromChanged;
		}
		public boolean isNameCreatorChanged() {
			return nameCreatorChanged;
		}
		public void setNameCreatorChanged(boolean nameCreatorChanged) {
			this.nameCreatorChanged = nameCreatorChanged;
		}
		public boolean isSignatureCreatorChanged() {
			return signatureCreatorChanged;
		}
		public void setSignatureCreatorChanged(boolean signatureCreatorChanged) {
			this.signatureCreatorChanged = signatureCreatorChanged;
		}
		public boolean isMasterChanged() {
			return masterChanged;
		}
		public void setMasterChanged(boolean masterChanged) {
			this.masterChanged = masterChanged;
		}
		public boolean isCorrectionNoteChanged() {
			return correctionNoteChanged;
		}
		public void setCorrectionNoteChanged(boolean correctionNoteChanged) {
			this.correctionNoteChanged = correctionNoteChanged;
		}
		public boolean isCodeLetterGroupChanged() {
			return codeLetterGroupChanged;
		}
		public void setCodeLetterGroupChanged(boolean codeLetterGroupChanged) {
			this.codeLetterGroupChanged = codeLetterGroupChanged;
		}
		public boolean isCodeNumberGroupChanged() {
			return codeNumberGroupChanged;
		}
		public void setCodeNumberGroupChanged(boolean codeNumberGroupChanged) {
			this.codeNumberGroupChanged = codeNumberGroupChanged;
		}
		public boolean isContentGroupChanged() {
			return contentGroupChanged;
		}
		public void setContentGroupChanged(boolean contentGroupChanged) {
			this.contentGroupChanged = contentGroupChanged;
		}
		public boolean isCorrectionDateChanged() {
			return correctionDateChanged;
		}
		public void setCorrectionDateChanged(boolean correctionDateChanged) {
			this.correctionDateChanged = correctionDateChanged;
		}
		public boolean isNoteChanged() {
			return noteChanged;
		}
		public void setNoteChanged(boolean noteChanged) {
			this.noteChanged = noteChanged;
		}
	}

	public void setActuallyValidLog(OilLog actuallyValidLog) {
		this.actuallyValidLog = actuallyValidLog;
	}
}
