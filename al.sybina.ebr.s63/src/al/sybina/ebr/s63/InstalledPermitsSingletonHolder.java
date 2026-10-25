package al.sybina.ebr.s63;

import javax.inject.Singleton;

import org.eclipse.e4.core.di.annotations.Creatable;

import ca.gc.chs.ihossv1.Ihossv1MetaPermitFileEntry;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

@Creatable
@Singleton
public class InstalledPermitsSingletonHolder {
	
	private ObservableList<Ihossv1MetaPermitFileEntry> allInstalledPermitsObsList;
	
	public InstalledPermitsSingletonHolder() {
		allInstalledPermitsObsList = FXCollections.observableList(S63Utils.getAllInstalledPermits());
	}

	public ObservableList<Ihossv1MetaPermitFileEntry> getAllInstalledPermitsObsList() {
		return allInstalledPermitsObsList;
	}

}
