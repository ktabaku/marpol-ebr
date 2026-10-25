package al.sybina.ebr.logbook.commons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.EclipseContextFactory;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;

import com.jfoenix.controls.JFXChipView;
import com.jfoenix.svg.SVGGlyphLoader;
import com.oceanpro.core.entity.oceanManager.logbook.operationLog.Tank;

import al.sybina.ebr.logbook.dialogs.TankUsageContext;
import al.sybina.ebr.logbook.dialogs.databackup.DataBackupDialog;
import al.sybina.ebr.core.tools.PluginTools;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "al.sybina.ebr.logbook.commons";
	public static final String RUNTIME_LOCATION_FOLDER_NAME = "logbook";
	public static final String MARPOL_DETAILS_CHANGED = "MARPOL_DETAILS_CHANGED";
	public static final String MARPOL_DETAILS_RELOADED = "MARPOL_DETAILS_RELOADED";
	public static final String LOGS_CHANGED_EVENT_TOPIC = "LOGS_CHANGED_EVENT_TOPIC";
	public static final String TANK_IDS = "TANK_IDS";
	public static final String LOG_IDS = "LOG_IDS";
	public static final String OIL_LOG_TYPE_1 = "OIL1";
	public static final String OIL_LOG_TYPE_2 = "OIL2";
	public static final String OIL_LOG_TYPE_1_PERSPECTIVE_ID = "al.sybina.ebr.logbook.ui.perspective.logbook";
	public static final String OIL_LOG_TYPE_2_PERSPECTIVE_ID = "al.sybina.ebr.logbook.ui.perspective.logbook2";
	
	

	private static BundleContext context;
	private static Activator plugin;
	
	public void start(BundleContext context) throws Exception {
		plugin = this;
		Activator.context = context;
		PluginTools.installPluginDataInRuntimeLocation(FrameworkUtil.getBundle(this.getClass()), RUNTIME_LOCATION_FOLDER_NAME);
	}
	
	public void stop(BundleContext context) throws Exception {
		plugin = null;
	}


	public static Activator getDefault() {
		return plugin;
	}
	public static BundleContext getContext() {
		return context;
	}
	
	public static void removeChangedTanksFromJFXChipView(ArrayList<Long> changedTankIds, JFXChipView<String> jFXChipView) {
		Set<Tank> selectedTanks = ((TankUsageContext)jFXChipView.getUserData()).getUserTankQuantities().keySet();
		List<Tank> changedTanks = selectedTanks.stream().filter(tank -> changedTankIds.contains(tank.getId())).collect(Collectors.toList());
		changedTanks.forEach(tank -> jFXChipView.getChips().remove(tank.getName()));
	}
}
