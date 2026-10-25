package al.sybina.ebr.login.views;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.subject.Subject;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.tools.services.IResourceProviderService;
import org.eclipse.fx.core.di.Service;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.ui.services.resources.ImageProvider;
import org.eclipse.fx.ui.services.theme.ThemeManager;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.svg.SVGGlyph;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.security.entities.Role;
import com.oceanpro.core.security.entities.User;
import com.oceanpro.utils.EasyDateUtils;

import al.sybina.ebr.commons.utils.JavaFXUtils;
import al.sybina.ebr.commons.utils.ViewPerspectiveHelper;
import al.sybina.ebr.login.Activator;
import al.sybina.ebr.login.core.LoginService;
import al.sybina.ebr.login.preferences.LoginPreferencePage;
import al.sybina.ebr.login.services.RemoteEventAdminService;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

public class ManageUsersController implements Initializable {

	private static final Logger logger = LoggerCreator.createLogger(ManageUsersController.class);
	
	@Inject
	private IResourceProviderService resourcesService;
	@Inject
	private ImageProvider imageProvider;
	@Inject
	private LoginPreferencePage loginPreferencePage;
	@Inject
	private IEventBroker eventBroker;
	@Inject
	@Service
	private RemoteEventAdminService remoteEventAdminService;
	@Inject
	private ViewPerspectiveHelper viewPerspectiveHelper;
	@Inject
	private static ThemeManager themeManager;
	@FXML
	private AnchorPane anchorPane;

	private static JFXButton closeButton;
	
	
	
	
	
	
	
	
	
	@FXML
    private Button btnLogout;
	
	@FXML
    private TextField username;
	
	@FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private DatePicker dob;
    
    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private TextField email;

    @FXML
    private PasswordField password;
    
    @FXML
    private Button reset;
	
	@FXML
    private Button saveUser;
	
	@FXML
	private TableView<User> userTable;

	@FXML
	private TableColumn<User, String> colUsername;
	
	@FXML
	private TableColumn<User, String> colFirstName;

	@FXML
	private TableColumn<User, String> colLastName;

	@FXML
	private TableColumn<User, LocalDate> colDOB;
	
	@FXML
    private TableColumn<User, String> colRole;

	@FXML
	private TableColumn<User, String> colEmail;
	
	@FXML
    private TableColumn<User, Boolean> colEdit;
	
	@FXML
    private MenuItem deleteUsers;
	
	
	@Inject
	private LoginService loginService;
	
	private ObservableList<User> userList = FXCollections.observableArrayList();
	private ObservableList<String> roles;
	private Long editUserId;
	
	
	
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		JavaFXUtils.addFilledPseudoClass(username);
		JavaFXUtils.addFilledPseudoClass(password);
		JavaFXUtils.addFilledPseudoClass(firstName);
		JavaFXUtils.addFilledPseudoClass(lastName);
		JavaFXUtils.addFilledPseudoClass(email);
		JavaFXUtils.addFilledPseudoClass(dob);

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
		
		dob.setConverter(new StringConverter<LocalDate>()
		{
		    private DateTimeFormatter dateTimeFormatter=EasyDateUtils.logbookSimpleDateTimeFormatter;

		    @Override
		    public String toString(LocalDate localDate)
		    {
		        if(localDate==null)
		            return "";
		        return dateTimeFormatter.format(localDate);
		    }

		    @Override
		    public LocalDate fromString(String dateString)
		    {
		        if(dateString==null || dateString.trim().isEmpty())
		        {
		            return null;
		        }
		        return LocalDate.parse(dateString,dateTimeFormatter);
		    }
		});
		
		roles = FXCollections.observableArrayList(
				loginService.findAllRoles()
				.stream()
	            .map(Role::getDescription)
	            .collect(Collectors.toList()));
		if(!SecurityUtils.getSubject().isPermitted(Activator.PERMISSION_CREATE_ADMIN_USER))
			roles.remove("admin");
		cbRole.setItems(roles);
		
		userTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		
		setColumnProperties();
		
		// Add all users into table
		loadUserDetails();
		
		eventBroker.subscribe(Activator.LOGIN_NEEDED_EVENT_TOPIC, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				userList.clear();
			}
		});
				
		eventBroker.subscribe(Activator.LOGGED_IN_EVENT_TOPIC, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
				loadUserDetails();
				roles = FXCollections.observableArrayList(
						loginService.findAllRoles()
						.stream()
			            .map(Role::getDescription)
			            .collect(Collectors.toList()));
				if(!SecurityUtils.getSubject().isPermitted(Activator.PERMISSION_CREATE_ADMIN_USER))
					roles.remove("admin");
				cbRole.setItems(roles);
			}
		});
		
		eventBroker.subscribe(Activator.USER_CHANGED_EVENT_TOPIC, new EventHandler() {
			@Override
			public void handleEvent(Event event) {
    			Subject currentUser = SecurityUtils.getSubject();
    			ArrayList<Long> ids = (ArrayList<Long>) event.getProperty(Activator.USER_IDS);
    			if(ids!=null && currentUser.getPrincipal()!=null && !ids.contains(((User)currentUser.getPrincipal()).getId())) {
    				loadUserDetails();
    			}
			}
		});
	}

	
	
	
	

    
    @FXML
    void reset(ActionEvent event) {
    	clearFields();
    }
    
    @FXML
    private void saveUser(ActionEvent event){
    	
    	if(validate("Username", getUsername(), "[a-zA-Z]+") &&
    	   validate("First Name", getFirstName(), "[a-zA-Z]+") &&
    	   validate("Last Name", getLastName(), "[a-zA-Z]+") &&
    	   emptyValidation("Date of Birth", dob.getEditor().getText().isEmpty()) && 
    	   emptyValidation("Role", getRole() == null) ){
    		
    		if(editUserId==null){
    			if(validate("Email", getEmail(), "[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+") &&
    				emptyValidation("Password", getPassword().isEmpty()) &&
    				usernameExistsValidation())	{
    				User user = new User();
    				user.setUsername(getUsername());
        			user.setFirstName(getFirstName());
        			user.setLastName(getLastName());
        			user.setDateOfBirth(Date.from(getDob().atStartOfDay(ZoneId.of("UTC")).toInstant()));
        			Set<Role> roles = new HashSet<Role>();
        			roles.add(loginService.getRoleByRoleDescription(getRole()));
        			user.setRoles(roles);
        			user.setEmail(getEmail());
        			String password = new SimpleHash("SHA-256", getPassword()).toHex();
        			user.setPassword(password);
        			
        			User newUser = loginService.saveUser(user);
        			HashMap<String, List<Long>> parameters = new HashMap<String, List<Long>>();
        			ArrayList<Long> ids = new ArrayList<Long>();
        			ids.add(newUser.getId());
        			parameters.put(Activator.USER_IDS, ids);
        			remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(Activator.USER_CHANGED_EVENT_TOPIC, parameters));
        			saveAlert(newUser);
        			clearFields();
    			}
    			
    		}else{
    			
    			User user = loginService.getUserById(editUserId);
    			user.setUsername(getUsername());
    			user.setFirstName(getFirstName());
    			user.setLastName(getLastName());
    			user.setDateOfBirth(Date.from(getDob().atStartOfDay(ZoneId.of("UTC")).toInstant()));
    			user.getRoles().clear();
    			user.getRoles().add(loginService.getRoleByRoleDescription(getRole()));
    			user.setEmail(getEmail());
    			if(!getPassword().equals(user.getPassword())) {
    				String password = new SimpleHash("SHA-256", getPassword()).toHex();
    				user.setPassword(password);
    			}
    			User updatedUser =  loginService.updateUser(user);
    			HashMap<String, List<Long>> parameters = new HashMap<String, List<Long>>();
    			ArrayList<Long> ids = new ArrayList<Long>();
    			ids.add(updatedUser.getId());
    			parameters.put(Activator.USER_IDS, ids);
    			updateAlert(updatedUser);
    			clearFields();
    			remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(Activator.USER_CHANGED_EVENT_TOPIC, parameters));
    		}
    		
    		
    		loadUserDetails();
    	}
    	
    	
    }



    
    @FXML
    private void deleteUsers(ActionEvent event){
    	List<User> users = userTable.getSelectionModel().getSelectedItems();
    	List<User> allUsers = loginService.findAllUsers();
    	List<User> remainingUsers = allUsers.stream().filter(user -> !users.contains(user)).collect(Collectors.toList());
    	Role adminRole = loginService.getRoleByRoleName("admin");
    	Role masterRole = loginService.getRoleByRoleName("master");
    	Role masterSsoRole = loginService.getRoleByRoleName("masterSso");
    	
    	List<User> adminUsers = remainingUsers.stream().filter(user -> user.getRoles().contains(adminRole)).collect(Collectors.toList());
    	List<User> masterUsers = remainingUsers.stream().filter(user -> user.getRoles().contains(masterRole) || user.getRoles().contains(masterSsoRole)).collect(Collectors.toList());
    	
    	if(adminUsers.isEmpty() || masterUsers.isEmpty()) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User delete");
			// alert.setHeaderText(null);
			alert.setContentText("At least one user with role master should be present in the system!");
			JavaFXUtils.undecorate(alert, closeButton, themeManager);
			alert.showAndWait();
			return;
    	}
    	
    	Alert alert = new Alert(AlertType.CONFIRMATION);
		alert.setTitle("Confirmation Dialog");
		alert.setHeaderText(null);
		alert.setContentText("Are you sure you want to delete selected?");
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		Optional<ButtonType> action = alert.showAndWait();
		
		if(action.get() == ButtonType.OK) {
			loginService.deleteAllUsers(users);
		
			HashMap<String, List<Long>> parameters = new HashMap<String, List<Long>>();
			parameters.put(Activator.USER_IDS, users.stream().map(User::getId).collect(Collectors.toList()));
			remoteEventAdminService.getDistributedEventAdmin(loginPreferencePage.getMainInstallationIp().getValue()).postEvent(new Event(Activator.USER_CHANGED_EVENT_TOPIC, parameters));
    	
			loadUserDetails();
		}
    }
    
   	private void clearFields() {
   		editUserId=null;
   		username.clear();
		firstName.clear();
		lastName.clear();
		dob.setValue(null);
		dob.getEditor().clear();
		cbRole.getSelectionModel().clearSelection();
		email.clear();
		password.clear();
	}
	
	private void saveAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User saved successfully.");
		//alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been created.");
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		alert.showAndWait();
	}
	
	private void updateAlert(User user){
		
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("User updated successfully.");
		//alert.setHeaderText(null);
		alert.setContentText("The user "+user.getFirstName()+" "+user.getLastName() +" has been updated.");
		JavaFXUtils.undecorate(alert, closeButton, themeManager);
		alert.showAndWait();
	}
	

	public String getUsername() {
		return username.getText();
	}
	public String getFirstName() {
		return firstName.getText();
	}

	public String getLastName() {
		return lastName.getText();
	}

	public LocalDate getDob() {
		return dob.getValue();
	}
	
	public String getRole() {
		return cbRole.getSelectionModel().getSelectedItem();
	}

	public String getEmail() {
		return email.getText();
	}

	public String getPassword() {
		return password.getText();
	}
  

	
	
	
	/*
	 *  Set All userTable column properties
	 */
	private void setColumnProperties(){
		/* Override date format in table
		 * colDOB.setCellFactory(TextFieldTableCell.forTableColumn(new StringConverter<LocalDate>() {
			 String pattern = "dd/MM/yyyy";
			 DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);
		     @Override 
		     public String toString(LocalDate date) {
		         if (date != null) {
		             return dateFormatter.format(date);
		         } else {
		             return "";
		         }
		     }

		     @Override 
		     public LocalDate fromString(String string) {
		         if (string != null && !string.isEmpty()) {
		             return LocalDate.parse(string, dateFormatter);
		         } else {
		             return null;
		         }
		     }
		 }));*/
		colUsername.setCellValueFactory(new PropertyValueFactory<>("username"));
		colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		colLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		colDOB.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
		colRole.setCellValueFactory(cellData -> new SimpleStringProperty(((Role)cellData.getValue().getRoles().toArray()[0]).getDescription()));
		colEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
		colEdit.setCellFactory(cellFactory);
	}
	
	Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>> cellFactory = 
			new Callback<TableColumn<User, Boolean>, TableCell<User, Boolean>>()
	{
		@Override
		public TableCell<User, Boolean> call( final TableColumn<User, Boolean> param)
		{
			final TableCell<User, Boolean> cell = new TableCell<User, Boolean>()
			{
				Image imgEdit = new Image(getClass().getResourceAsStream("/images/edit.png"));
				final Button btnEdit = new Button();
				
				@Override
				public void updateItem(Boolean check, boolean empty)
				{
					super.updateItem(check, empty);
					if(empty)
					{
						setGraphic(null);
						setText(null);
					}
					else{
						btnEdit.setOnAction(e ->{
							User user = getTableView().getItems().get(getIndex());
							updateUser(user);
						});
						
						btnEdit.setStyle("-fx-background-color: transparent;");
						ImageView iv = new ImageView();
				        iv.setImage(imgEdit);
				        iv.setPreserveRatio(true);
				        iv.setSmooth(true);
				        iv.setCache(true);
						btnEdit.setGraphic(iv);
						
						setGraphic(btnEdit);
						setAlignment(Pos.CENTER);
						setText(null);
					}
				}

				private void updateUser(User user) {
					editUserId=user.getId();
					username.setText(user.getUsername());
					firstName.setText(user.getFirstName());
					lastName.setText(user.getLastName());
					if(user.getDateOfBirth()!=null) {
						dob.setValue(user.getDateOfBirth().toInstant().atZone(ZoneId.of("UTC")).toLocalDate());
						dob.requestLayout();
					}
					email.setText(user.getEmail());
					cbRole.getSelectionModel().select(((Role)user.getRoles().toArray()[0]).getDescription());
					password.setText(user.getPassword());
				}
			};
			return cell;
		}
	};

	
	
	/*
	 *  Add All users to observable list and update table
	 */
	private void loadUserDetails(){
		userList.clear();
		List<User> allUsers = loginService.findAllUsers();
		if(!SecurityUtils.getSubject().isPermitted(Activator.PERMISSION_CREATE_ADMIN_USER)) {
			allUsers = allUsers.stream().filter(user -> !((Role)user.getRoles().toArray()[0]).getName().equalsIgnoreCase("admin")).collect(Collectors.toList());
		}
		userList.addAll(allUsers);

		userTable.setItems(userList);
	}
	
	/*
	 * Validations
	 */
	
	private boolean usernameExistsValidation() {
		User existingUser = loginService.findUserByUsername(getUsername());
		if (existingUser == null) {
			return true;
		} else {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("User exists");
			// alert.setHeaderText(null);
			alert.setContentText("A user with username: " + getUsername() + " already exists!");
			JavaFXUtils.undecorate(alert, closeButton, themeManager);
			alert.showAndWait();
			return false;
		}
	}
	
	
	private boolean validate(String field, String value, String pattern){
		if(!value.isEmpty()){
			Pattern p = Pattern.compile(pattern);
	        Matcher m = p.matcher(value);
	        if(m.find() && m.group().equals(value)){
	            return true;
	        }else{
	        	validationAlert(field, false);            
	            return false;            
	        }
		}else{
			validationAlert(field, true);            
            return false;
		}        
    }
	
	private boolean emptyValidation(String field, boolean empty){
        if(!empty){
            return true;
        }else{
        	validationAlert(field, true);            
            return false;            
        }
    }	
	
	private void validationAlert(String field, boolean empty){
		Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Validation Error");
        //alert.setHeaderText(null);
        if(field.equals("Role")) alert.setContentText("Please Select "+ field);
        else{
        	if(empty) alert.setContentText("Please Enter "+ field);
        	else alert.setContentText("Please Enter Valid "+ field);
        }
        JavaFXUtils.undecorate(alert, closeButton, themeManager);
        alert.showAndWait();
	}

	



	
	

}
