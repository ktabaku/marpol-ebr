/**
 * 
 */
package al.sybina.ebr.jasperreports;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collection;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Singleton;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.Logger.Level;
import org.eclipse.fx.core.log.LoggerCreator;
import org.osgi.framework.Bundle;

import al.sybina.ebr.core.tools.PluginTools;
import net.sf.jasperreports.engine.DefaultJasperReportsContext;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRPropertiesMap;
import net.sf.jasperreports.engine.JRPropertiesUtil;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.SimpleJasperReportsContext;
import net.sf.jasperreports.engine.component.ComponentsBundle;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.fonts.FontExtensionsCollector;
import net.sf.jasperreports.engine.fonts.FontExtensionsRegistry;
import net.sf.jasperreports.engine.fonts.FontFamily;
import net.sf.jasperreports.engine.fonts.FontSet;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionHelper;
import net.sf.jasperreports.engine.fonts.SimpleFontExtensionsRegistryFactory;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.extensions.ExtensionsEnvironment;
import net.sf.jasperreports.extensions.ExtensionsRegistry;
import net.sf.jasperreports.extensions.ExtensionsRegistryFactory;
import net.sf.jasperreports.repo.FileRepositoryPersistenceServiceFactory;
import net.sf.jasperreports.repo.FileRepositoryService;
import net.sf.jasperreports.repo.PersistenceServiceFactory;
import net.sf.jasperreports.repo.RepositoryService;




public class JasperReportsFactory {

	private static final Logger logger = LoggerCreator.createLogger(JasperReportsFactory.class);

	private SimpleJasperReportsContext context;
	
	public JasperReportsFactory() {
		context = new SimpleJasperReportsContext();
		logger.info("SimpleJasperReportsContext created..");
		JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.extensions.registry.class", DefaultExtensionsRegistry.class.getName());
		FileRepositoryService fileRepository = new FileRepositoryService(context, PluginTools.getFileLocalToRuntime("reports"), false);
		context.setExtensions(RepositoryService.class, Collections.singletonList(fileRepository));
		context.setExtensions(PersistenceServiceFactory.class, Collections.singletonList(FileRepositoryPersistenceServiceFactory.getInstance()));

//		JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.awt.ignore.missing.font", "true");
		JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.default.font.name", "DejaVu Sans");
		JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.default.pdf.embedded", "true");
		JRPropertiesUtil.getInstance(DefaultJasperReportsContext.getInstance()).setProperty("net.sf.jasperreports.default.pdf.font.name", "DejaVu Sans");
		
//		Bundle bundle = Platform.getBundle("net.sf.jasperreports.fonts");
//		URL fileURL = bundle.getEntry("net/sf/jasperreports/fonts/fonts.xml");
//		FileInputStream fileInputStream = null;
//		File file = null;
//		try {
//			URL resolvedFileURL = FileLocator.toFileURL(fileURL);
//			// We need to use the 3-arg constructor of URI in order to properly escape file system chars
//			URI resolvedURI = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
//			file = new File(resolvedURI);
//		    //file = new File(FileLocator.resolve(fileURL).toURI());
//			fileInputStream = new FileInputStream(file);
//		} catch (URISyntaxException e1) {
//		    e1.printStackTrace();
//		} catch (IOException e1) {
//		    e1.printStackTrace();
//		}
//		
//		DefaultExtensionsRegistry defaultExtensionsRegistry = (DefaultExtensionsRegistry) ExtensionsEnvironment.getExtensionsRegistry();
//		List<FontFamily> fontFamilies = defaultExtensionsRegistry.getExtensions(FontFamily.class);
//		
//		JRPropertiesMap jRPropertiesMap = new JRPropertiesMap();
//		jRPropertiesMap.setProperty("net.sf.jasperreports.extension.simple.font.families.dejavu", file.getAbsolutePath());
//		jRPropertiesMap.setProperty("net.sf.jasperreports.extension.simple.font.families.default", "net/sf/jasperreports/fonts/jasperreports-fonts.xml");
//		defaultExtensionsRegistry.addRegistry(jRPropertiesMap, "net.sf.jasperreports.extension.simple.font.families", SimpleFontExtensionsRegistryFactory.class.getName());
		
	}
	/**
	 *
	 * @param reportPath Resource path to master Jasper Report file accepting .jasper or .jrxml files.
	 * @param subReports Map of subreport parameter keys and corresponding path to resource.
	 * @param parameterMap Map of report parameter keys and corresponding Objects for report.
	 * @param beanCollection Collection of model beans
	 * @return
	 * @throws JasperReportsException
	 */
	@SuppressWarnings("unchecked")
	public ByteArrayOutputStream complieReportAsPdfByteArrayOutputStream(JasperReportDetails  jasperReportDetails, Map<String, Object> additionalReportParametersMap, Collection beanCollection) throws Exception {
		if (logger.isEnabled(Level.INFO)) {
			logger.info("Compiling Jasper Report to PDF OutPutStream. {{ JasperReportDetails }} " + jasperReportDetails.getDescription() );
		}

		if(jasperReportDetails.getMasterReportUrl() == null) {
			throw new IllegalArgumentException("A masterReportUrl is required.");
		}

		if(beanCollection == null ) {
			throw new IllegalArgumentException("BeanCollection can not be empty");
		}

		JRBeanCollectionDataSource collection = new JRBeanCollectionDataSource(beanCollection);

		Map<String, Object> reportParameters = new HashMap<String, Object>();
		reportParameters.putAll(jasperReportDetails.getReportParameters());

		JasperReport masterJasperReport = loadReport(jasperReportDetails.getMasterReportUrl());

		// Compile sub reports and add all subreports to the report parameter map
		if(jasperReportDetails.getSubReportUrls() != null && jasperReportDetails.getSubReportUrls().size() > 0){
			Map comiledSubReports = new HashMap<String, Object>(jasperReportDetails.getSubReportUrls().size());
			for (Enumeration urls = jasperReportDetails.getSubReportUrls().propertyNames(); urls.hasMoreElements();) {
				String key = (String) urls.nextElement();
				String path = jasperReportDetails.getSubReportUrls().getProperty(key);
				comiledSubReports.put(key, loadReport(path));
			}
			reportParameters.putAll(comiledSubReports);
		}

		// Add aditional parameters
		if(additionalReportParametersMap != null && additionalReportParametersMap.size() > 0) {
			reportParameters.putAll(additionalReportParametersMap);
		}

		// Fill and export jasper report
		try {

			if (logger.isEnabled(Level.INFO)) {
				logger.info("Filling Jasper Report with report parameters and collection. ");
			}
		    

			
		    JasperPrint jasperPrint = JasperFillManager.getInstance(context).fill(masterJasperReport, reportParameters, collection);
			ByteArrayOutputStream baos = new ByteArrayOutputStream(4096);

			if (logger.isEnabled(Level.INFO)) {
				logger.info("Exporting Jasper Report to pdf stream.");
			}

			JasperExportManager.exportReportToPdfStream(jasperPrint, baos);

			return baos;
		} catch (JRException jre) {
			throw new Exception("Unable to compile and fill pdf report", jre);
		} catch (Exception e){
			throw new Exception("Unkown exception while trying to compile and fill pdf report", e);
		}
	}

	public File complieReportAsPdfFile(File file, JasperReportDetails  jasperReportDetails, Map<String, Object> additionalReportParametersMap, Collection beanCollection) throws Exception {
		if (logger.isEnabled(Level.DEBUG)) {
			logger.debug("Compling PDF Report. ");
		}

		ByteArrayOutputStream baos = complieReportAsPdfByteArrayOutputStream(jasperReportDetails, additionalReportParametersMap, beanCollection);

		FileOutputStream fos = null;

		try {
			fos = new FileOutputStream(file);
		} catch (FileNotFoundException e) {
			logger.error("File ["+file.getPath()+"] not found.",e);
			throw new Exception("File ["+file.getPath()+"] not found.",e);
		}
		try {
			fos.write(baos.toByteArray());
			fos.close();
		} catch (IOException e) {
			logger.error("Error writing to file " + file.getPath(), e);
			throw new Exception(e);
		}
		return file;
	}

	/**
	 * Loads a <code>JasperReport</code> from the specified <code>Resource</code>. If
	 * the <code>Resource</code> points to an uncompiled report design file then the
	 * report file is compiled dynamically and loaded into memory.
	 * @param resource the <code>Resource</code> containing the report definition or design
	 * @return a <code>JasperReport</code> instance
	 */
	private JasperReport loadReport(String path) throws Exception {
		try {
			File file = new File(path);
			String fileName = file.getAbsolutePath();

			if (fileName.endsWith(".jasper")) {
				// Load pre-compiled report.
				if (logger.isEnabled(Level.INFO)) {
					logger.info("Loading pre-compiled Jasper Report from " + path );
				}
				return (JasperReport) JRLoader.loadObject(context, new File(path));
			}

			else if (fileName.endsWith(".jrxml")) {
				// Compile report on-the-fly.
				if (logger.isEnabled(Level.INFO)) {
					logger.info("Compiling Jasper Report loaded from " + path );
				}
				JasperDesign design = JRXmlLoader.load(context, new File(path));
				return JasperCompileManager.compileReport(design);
			}

			else {
				throw new IllegalArgumentException(
						"Report [" + path + "] must end in either .jasper or .jrxml");
			}
		}
		catch (JRException ex) {
			throw new Exception(
					"Could not parse JasperReports report  from resource [" + path + "]", ex);
		}
	}

}