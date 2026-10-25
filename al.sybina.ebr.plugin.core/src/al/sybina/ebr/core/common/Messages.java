package al.sybina.ebr.core.common;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "al.sybina.ebr.core.common.messages"; //$NON-NLS-1$
	public static String Date_formatDuration1;
	public static String MapElement2DImage_cantRead;
	public static String Position_errorProj;
	public static String PreferenceTools_installing;
	public static String ProjectionFieldEditor_add;
	public static String ProjectionFieldEditor_addParameter;
	public static String ProjectionFieldEditor_changeParameter;
	public static String ProjectionFieldEditor_down;
	public static String ProjectionFieldEditor_edit;
	public static String ProjectionFieldEditor_parameter;
	public static String ProjectionFieldEditor_remove;
	public static String ProjectionFieldEditor_up;
	public static String ProjectionFieldEditor_validate;
	public static String ResourceManager_errorSearching;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
