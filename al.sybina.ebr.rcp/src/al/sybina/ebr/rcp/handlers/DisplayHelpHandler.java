package al.sybina.ebr.rcp.handlers;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.osgi.framework.Bundle;

import al.sybina.ebr.rcp.Activator;
import al.sybina.ebr.rcp.BaseViewerFX;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class DisplayHelpHandler {
	
	@Execute
	public void execute(EPartService partService, IWorkbench workbench, Stage stage) {
		
		Bundle bundle = Activator.getBundleContext().getBundle(); //Platform.getBundle(Activator.PLUGIN_ID);
		URL fileURL = bundle.getEntry("doc/EBR_USER_MANUAL.pdf");
		File pdffile = null;
		try {
			URL resolvedFileURL = FileLocator.toFileURL(fileURL);
			URI uri = new URI(resolvedFileURL.getProtocol(), resolvedFileURL.getPath(), null);
			pdffile = new File(uri);
		} catch (URISyntaxException e1) {
		    e1.printStackTrace();
		} catch (IOException e1) {
		    e1.printStackTrace();
		}
		
		Stage helpStage = new Stage();
		helpStage.initOwner(stage);
		helpStage.initModality(Modality.APPLICATION_MODAL);
		BaseViewerFX baseViewerFX = new BaseViewerFX();
		baseViewerFX.start(helpStage, pdffile);
		
	}
}
