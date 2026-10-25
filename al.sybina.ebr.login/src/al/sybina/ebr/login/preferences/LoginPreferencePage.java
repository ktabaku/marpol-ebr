package al.sybina.ebr.login.preferences;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Singleton;

import org.apache.shiro.SecurityUtils;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.preferences.ConfigurationScope;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.di.ContextBoundValue;
import org.eclipse.fx.core.di.ContextValue;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;
import org.eclipse.fx.core.preferences.Preference;
import org.osgi.service.prefs.BackingStoreException;
import org.osgi.service.prefs.Preferences;

import com.dlsc.preferencesfx.model.Category;
import com.dlsc.preferencesfx.model.Group;
import com.dlsc.preferencesfx.model.Setting;
import com.oceanpro.core.security.entities.User;
import com.opcoach.e4.preferences.E4PreferencePage;

import al.sybina.ebr.commons.utils.AuditLogger;
import al.sybina.ebr.login.Activator;
import al.sybina.ebr.core.tools.PluginTools;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


@Creatable
@Singleton
public class LoginPreferencePage extends E4PreferencePage {

	private static Logger logger = LoggerCreator.createLogger(LoginPreferencePage.class);
	private static final Logger auditLogger = LoggerCreator.createLogger(AuditLogger.class);

	@Inject
	@Preference(nodePath=E4PreferencePage.PLUGIN_ID, key="mainInstallationIp")
	public Property<String> mainInstallationIpContextValue;
	
	@Inject
	@Preference(nodePath=E4PreferencePage.PLUGIN_ID, key="mainInstallationPort")
	public Property<String> mainInstallationPortContextValue;
	
	@Inject
	@Preference(nodePath=E4PreferencePage.PLUGIN_ID, key="mainInstallationDb")
	public Property<String> mainInstallationDbContextValue;
		
	private StringProperty mainInstallationIp = new SimpleStringProperty(Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationIp", "127.0.0.1", null));
	private StringProperty mainInstallationPort = new SimpleStringProperty(Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationPort", "1527", null));
	private StringProperty mainInstallationDb = new SimpleStringProperty(Platform.getPreferencesService().getString(E4PreferencePage.PLUGIN_ID, "mainInstallationDb", PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME + "/db"), null));
	
	public LoginPreferencePage() {
		setCategory(Category.of("Multi Installations", Group.of("Main Installation",
				Setting.of("Main Installation IP:", mainInstallationIp),
				Setting.of("Main Installation PORT:", mainInstallationPort),
				Setting.of("Main Installation Database:", mainInstallationDb)
		)));
	}

	@PostConstruct
	public void post() {
		mainInstallationIpContextValue.bind(mainInstallationIp);
		mainInstallationPortContextValue.bind(mainInstallationPort);
		mainInstallationDbContextValue.bind(mainInstallationDb);
		mainInstallationIp.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated mainInstallationIp to: " + mainInstallationIp.get());
			}
		});
		mainInstallationPort.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated mainInstallationPort to: " + mainInstallationPort.get());
			}
		});
		mainInstallationDb.addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				auditLogger.info("user: " + ((User)SecurityUtils.getSubject().getPrincipal()).getUsername() + " updated mainInstallationDb to: " + mainInstallationDb.get());
			}
		});
	}
	
	public StringProperty getMainInstallationIp() {
		return mainInstallationIp;
	}

	public StringProperty getMainInstallationPort() {
		return mainInstallationPort;
	}

	public StringProperty getMainInstallationDb() {
		return mainInstallationDb;
	}
	
	
}