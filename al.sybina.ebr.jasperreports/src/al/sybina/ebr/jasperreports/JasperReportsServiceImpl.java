package al.sybina.ebr.jasperreports;

import java.util.Properties;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import al.sybina.ebr.core.tools.PluginTools;

public class JasperReportsServiceImpl implements JasperReportsService {

	private static final Logger logger = LoggerCreator.createLogger(JasperReportsServiceImpl.class);
	
	private JasperReportsFactory jasperReportsFactory;
	
	public JasperReportsServiceImpl() {
//		Thread t = new Thread("Create JasperReports Factory") {
//			@Override
//			public void run() {
//				Properties props = System.getProperties();
//				String userDirProperty = props.getProperty("user.dir");
//				props.setProperty("user.dir", PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME));
//				jasperReportsFactory = new JasperReportsFactory();
//				props.setProperty("user.dir", userDirProperty);
//			}
//		};
//		t.start();
	}
	@Override
	public JasperReportsFactory getJasperReportsFactory() {
		if(jasperReportsFactory==null) {
			Properties props = System.getProperties();
			String userDirProperty = props.getProperty("user.dir");
			logger.info("User Dir:" + userDirProperty);
			props.setProperty("user.dir", PluginTools.getFileLocalToRuntime(Activator.RUNTIME_LOCATION_FOLDER_NAME));
			logger.info("New User Dir:" + System.getProperty("user.dir"));
			jasperReportsFactory = new JasperReportsFactory();
			props.setProperty("user.dir", userDirProperty);
			logger.info("User Dir:" + System.getProperty("user.dir"));
		}
		return jasperReportsFactory;
	}
	
	public void activate() {
		//jasperReportsFactory = new JasperReportsFactory();
	}
	
	public void deactivate() {
		//this.jasperReportsFactory = null;
	}

}
