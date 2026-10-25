package org.eclipse.e4.tools.resources;

import java.io.IOException;

import org.eclipse.e4.tools.services.BasicResourceProvider;
import org.eclipse.fx.core.URI;


public class WorkbenchResourceProvider extends BasicResourceProvider {
	
	

	public URI getImageURI(String name) {
		return SimpleURI.createPlatformPluginURI(BUNDLE_SYMBOLIC_NAME, properties.get(name));
	}
	
	private final static String BUNDLE_SYMBOLIC_NAME = "org.eclipse.e4.tools.resources";
	/**
     * Identifies the error overlay image.
     * @since 3.4
     */
    public final static String IMG_DEC_FIELD_ERROR = "IMG_DEC_FIELD_ERROR"; //$NON-NLS-1$

    /**
     * Identifies the warning overlay image.
     * @since 3.4
     */
    public final static String IMG_DEC_FIELD_WARNING = "IMG_DEC_FIELD_WARNING"; //$NON-NLS-1$

    /**
     * Identifies the default image used for views.
     */
    public final static String IMG_DEF_VIEW = "IMG_DEF_VIEW"; //$NON-NLS-1$

    /**
     * Identifies the collapse all image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_COLLAPSEALL = "IMG_ELCL_COLLAPSEALL"; //$NON-NLS-1$

    /**
     * Identifies the collapse all image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_COLLAPSEALL_DISABLED = "IMG_ELCL_COLLAPSEALL_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the remove image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_REMOVE = "IMG_ELCL_REMOVE"; //$NON-NLS-1$

    /**
     * Identifies the remove image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_REMOVE_DISABLED = "IMG_ELCL_REMOVE_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the remove all image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_REMOVEALL = "IMG_ELCL_REMOVEALL"; //$NON-NLS-1$

    /**
     * Identifies the remove all image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_REMOVEALL_DISABLED = "IMG_ELCL_REMOVEALL_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the stop image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_STOP = "IMG_ELCL_STOP"; //$NON-NLS-1$

    /**
     * Identifies the stop image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_STOP_DISABLED = "IMG_ELCL_STOP_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the synchronize image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_SYNCED = "IMG_ELCL_SYNCED"; //$NON-NLS-1$

    /**
     * Identifies the synchronize image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ELCL_SYNCED_DISABLED = "IMG_ELCL_SYNCED_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the clear image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_CLEAR = "IMG_ETOOL_CLEAR"; //$NON-NLS-1$

    /**
     * Identifies the clear image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_CLEAR_DISABLED = "IMG_ETOOL_CLEAR_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the default perspective image.
     * @since 3.4
     */
    public final static String IMG_ETOOL_DEF_PERSPECTIVE = "IMG_ETOOL_DEF_PERSPECTIVE"; //$NON-NLS-1$

    /**
     * Identifies the delete image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_DELETE = "IMG_ETOOL_DELETE"; //$NON-NLS-1$

    /**
     * Identifies the delete image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_DELETE_DISABLED = "IMG_ETOOL_DELETE_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the home image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_HOME_NAV = "IMG_ETOOL_HOME_NAV"; //$NON-NLS-1$

    /**
     * Identifies the home image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_HOME_NAV_DISABLED = "IMG_ETOOL_HOME_NAV_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the print image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_PRINT_EDIT = "IMG_ETOOL_PRINT_EDIT"; //$NON-NLS-1$

    /**
     * Identifies the print image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_PRINT_EDIT_DISABLED = "IMG_ETOOL_PRINT_EDIT_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the save image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVE_EDIT = "IMG_ETOOL_SAVE_EDIT"; //$NON-NLS-1$

    /**
     * Identifies the save image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVE_EDIT_DISABLED = "IMG_ETOOL_SAVE_EDIT_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the save all image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVEALL_EDIT = "IMG_ETOOL_SAVEALL_EDIT"; //$NON-NLS-1$

    /**
     * Identifies the save all image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVEALL_EDIT_DISABLED = "IMG_ETOOL_SAVEALL_EDIT_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the save as image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVEAS_EDIT = "IMG_ETOOL_SAVEAS_EDIT"; //$NON-NLS-1$

    /**
     * Identifies the save as image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_ETOOL_SAVEAS_EDIT_DISABLED = "IMG_ETOOL_SAVEAS_EDIT_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the help image.
     * @since 3.4
     */
    public final static String IMG_LCL_LINKTO_HELP = "IMG_LCL_LINKTO_HELP"; //$NON-NLS-1$

    /**
     * Identifies the add image.
     * @since 3.4
     */
    public final static String IMG_OBJ_ADD = "IMG_OBJ_ADD"; //$NON-NLS-1$

    /**
     * Identifies an element image.
     */
    public final static String IMG_OBJ_ELEMENT = "IMG_OBJ_ELEMENTS"; //$NON-NLS-1$

    /**
     * Identifies a file image.
     */
    public final static String IMG_OBJ_FILE = "IMG_OBJ_FILE"; //$NON-NLS-1$

    /**
     * Identifies a folder image.
     */
    public final static String IMG_OBJ_FOLDER = "IMG_OBJ_FOLDER"; //$NON-NLS-1$

    /**
     * Identifies a project image.
     * 
     * @deprecated in 3.0. This image is IDE-specific, and is therefore found
     * only in IDE configurations. IDE-specific tools should use 
     * <code>org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT</code> instead.
     */
    public final static String IMG_OBJ_PROJECT = "IMG_OBJ_PROJECT"; //$NON-NLS-1$

    /**
     * Identifies a closed project image.
     * 
     * @deprecated in 3.0. This image is IDE-specific, and is therefore found
     * only in IDE configurations. IDE-specific tools should use 
     * <code>org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJ_PROJECT_CLOSED</code> instead.
     */
    public final static String IMG_OBJ_PROJECT_CLOSED = "IMG_OBJ_PROJECT_CLOSED"; //$NON-NLS-1$

    /**
     * Identifies the default image used to indicate a bookmark.
     * 
     * @deprecated in 3.0. This image is IDE-specific, and is therefore found
     * only in IDE configurations. IDE-specific tools should use 
     * <code>org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJS_BKMRK_TSK</code> instead.
     */
    public final static String IMG_OBJS_BKMRK_TSK = "IMG_OBJS_BKMRK_TSK"; //$NON-NLS-1$

    /**
     * Identifies the default image used to indicate errors.
     */
    public final static String IMG_OBJS_ERROR_TSK = "IMG_OBJS_ERROR_TSK"; //$NON-NLS-1$

    /**
     * Identifies the default image used to indicate information only.
     */
    public final static String IMG_OBJS_INFO_TSK = "IMG_OBJS_INFO_TSK"; //$NON-NLS-1$

    /**
     * Identifies the default image used to indicate a task.
     * 
     * @deprecated in 3.0. This image is IDE-specific, and is therefore found
     * only in IDE configurations. IDE-specific tools should use 
     * <code>org.eclipse.ui.ide.IDE.SharedImages.IMG_OBJS_TASK_TSK</code> instead.
     */
    public final static String IMG_OBJS_TASK_TSK = "IMG_OBJS_TASK_TSK"; //$NON-NLS-1$

    /**
     * Identifies the default image used to indicate warnings.
     */
    public final static String IMG_OBJS_WARN_TSK = "IMG_OBJS_WARN_TSK"; //$NON-NLS-1$

    /**
     * Identifies the image used for "open marker".
     * 
     * @deprecated in 3.0. This image is IDE-specific, and is therefore found
     * only in IDE configurations. IDE-specific tools should use 
     * <code>org.eclipse.ui.ide.IDE.SharedImages.IMG_OPEN_MARKER</code> instead.
     */
    public final static String IMG_OPEN_MARKER = "IMG_OPEN_MARKER"; //$NON-NLS-1$

    /**
     * Identifies the back image in the enabled state.
     */
    public final static String IMG_TOOL_BACK = "IMG_TOOL_BACK"; //$NON-NLS-1$
    
    /**
     * Identifies the back image in the disabled state.
     */
    public final static String IMG_TOOL_BACK_DISABLED = "IMG_TOOL_BACK_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the back image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_BACK</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_BACK_HOVER = "IMG_TOOL_BACK_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the copy image in the enabled state.
     */
    public final static String IMG_TOOL_COPY = "IMG_TOOL_COPY"; //$NON-NLS-1$

    /**
     * Identifies the copy image in the disabled state.
     */
    public final static String IMG_TOOL_COPY_DISABLED = "IMG_TOOL_COPY_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the copy image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_COPY</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_COPY_HOVER = "IMG_TOOL_COPY_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the cut image in the enabled state.
     */
    public final static String IMG_TOOL_CUT = "IMG_TOOL_CUT"; //$NON-NLS-1$

    /**
     * Identifies the cut image in the disabled state.
     */
    public final static String IMG_TOOL_CUT_DISABLED = "IMG_TOOL_CUT_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the cut image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_CUT</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_CUT_HOVER = "IMG_TOOL_CUT_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the delete image in the enabled state.
     * @since 3.4
     */
    public final static String IMG_TOOL_DELETE = "IMG_TOOL_DELETE"; //$NON-NLS-1$

    /**
     * Identifies the delete image in the disabled state.
     * @since 3.4
     */
    public final static String IMG_TOOL_DELETE_DISABLED = "IMG_TOOL_DELETE_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the delete image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_DELETE</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_DELETE_HOVER = "IMG_TOOL_DELETE_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the forward image in the enabled state.
     */
    public final static String IMG_TOOL_FORWARD = "IMG_TOOL_FORWARD"; //$NON-NLS-1$

    /**
     * Identifies the forward image in the disabled state.
     */
    public final static String IMG_TOOL_FORWARD_DISABLED = "IMG_TOOL_FORWARD_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the forward image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_FORWARD</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_FORWARD_HOVER = "IMG_TOOL_FORWARD_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the new wizard image in the enabled state.
     */
    public final static String IMG_TOOL_NEW_WIZARD = "IMG_TOOL_NEW_WIZARD"; //$NON-NLS-1$

    /**
     * Identifies the new wizard image in the disabled state.
     */
    public final static String IMG_TOOL_NEW_WIZARD_DISABLED = "IMG_TOOL_NEW_WIZARD_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the new wizard image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_NEW_WIZARD</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_NEW_WIZARD_HOVER = "IMG_TOOL_NEW_WIZARD_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the paste image in the enabled state.
     */
    public final static String IMG_TOOL_PASTE = "IMG_TOOL_PASTE"; //$NON-NLS-1$

    /**
     * Identifies the paste image in the disabled state.
     */
    public final static String IMG_TOOL_PASTE_DISABLED = "IMG_TOOL_PASTE_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the paste image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_PASTE</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_PASTE_HOVER = "IMG_TOOL_PASTE_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the redo image in the enabled state.
     */
    public final static String IMG_TOOL_REDO = "IMG_TOOL_REDO"; //$NON-NLS-1$

    /**
     * Identifies the redo image in the disabled state.
     */
    public final static String IMG_TOOL_REDO_DISABLED = "IMG_TOOL_REDO_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the redo image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_REDO</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_REDO_HOVER = "IMG_TOOL_REDO_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the undo image in the enabled state.
     */
    public final static String IMG_TOOL_UNDO = "IMG_TOOL_UNDO"; //$NON-NLS-1$

    /**
     * Identifies the undo image in the disabled state.
     */
    public final static String IMG_TOOL_UNDO_DISABLED = "IMG_TOOL_UNDO_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the undo image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_UNDO</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_UNDO_HOVER = "IMG_TOOL_UNDO_HOVER"; //$NON-NLS-1$

    /**
     * Identifies the up image in the enabled state.
     */
    public final static String IMG_TOOL_UP = "IMG_TOOL_UP"; //$NON-NLS-1$

    /**
     * Identifies the up image in the disabled state.
     */
    public final static String IMG_TOOL_UP_DISABLED = "IMG_TOOL_UP_DISABLED"; //$NON-NLS-1$

    /**
     * Identifies the up image in the hover (colored) state.
     * 
     * @deprecated in 3.0. This image is now the same as <code>IMG_TOOL_UP</code>.
     *   Enabled images are now in color.  The workbench itself no longer uses the hover image variants.
     */
    public final static String IMG_TOOL_UP_HOVER = "IMG_TOOL_UP_HOVER"; //$NON-NLS-1$

    // The following set of constants represent the image pairs that are used
    // to construct cursors for drag and drop operations within the workbench
    // Each cursor is represented by two images; the 'source' and the 'mask'
    // These need to be combined using the following code snippet:
	//    source = getImageDescriptor(sourceId);
	//    mask = getImageDescriptor(maskId);
	//    cursor = new Cursor(display, source.getImageData(), mask.getImageData(), 16, 16);

    /**
     * Cursor 'source' for the left arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_LEFT_SOURCE = "IMG_OBJS_DND_LEFT_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the left arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_LEFT_MASK = "IMG_OBJS_DND_LEFT_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the right arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_RIGHT_SOURCE = "IMG_OBJS_DND_RIGHT_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the right arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_RIGHT_MASK = "IMG_OBJS_DND_RIGHT_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the up arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_TOP_SOURCE = "IMG_OBJS_DND_TOP_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the up arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_TOP_MASK = "IMG_OBJS_DND_TOP_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the down arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_BOTTOM_SOURCE = "IMG_OBJS_DND_BOTTOM_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the down arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_BOTTOM_MASK = "IMG_OBJS_DND_BOTTOM_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the 'no drop' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_INVALID_SOURCE = "IMG_OBJS_DND_INVALID_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the 'no drop' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_INVALID_MASK = "IMG_OBJS_DND_INVALID_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the 'in stack' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_STACK_SOURCE = "IMG_OBJS_DND_STACK_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the 'in stack' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_STACK_MASK = "IMG_OBJS_DND_STACK_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the 'off-screen' (detached window) arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_OFFSCREEN_SOURCE = "IMG_OBJS_DND_OFFSCREEN_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the 'off-screen' (detached window) arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public final static String IMG_OBJS_DND_OFFSCREEN_MASK = "IMG_OBJS_DND_OFFSCREEN_MASK"; //$NON-NLS-1$

    /**
     * Cursor 'source' for the 'fast-view' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public static final String IMG_OBJS_DND_TOFASTVIEW_SOURCE = "IMG_OBJS_DND_TOFASTVIEW_SOURCE"; //$NON-NLS-1$
    /**
     * Cursor 'mask' for the 'fast-view' arrow cursor. For cursor construction see:
     * @see Cursor#Cursor(Device, ImageData, ImageData, int, int)
     * @since 3.5
     */
    public static final String IMG_OBJS_DND_TOFASTVIEW_MASK = "IMG_OBJS_DND_TOFASTVIEW_MASK"; //$NON-NLS-1$    
   
    		
    
    public static final String IMG_IBR_RCP_TEXTURE = "IMG_IBR_RCP_TEXTURE";
    public static final String IMG_IBR_RCP_TEXTUREINTERNE = "IMG_IBR_RCP_TEXTUREINTERNE";
    public static final String IMG_IBR_RCP_TEXTUREINTERNE2 = "IMG_IBR_RCP_TEXTUREINTERNE2";
    public static final String IMG_IBR_RCP_TEXTURELOGBOOK = "IMG_IBR_RCP_TEXTURELOGBOOK";
    public static final String IMG_IBR_RCP_CENTER = "IMG_IBR_RCP_CENTER";
    public static final String IMG_IBR_RCP_TITLEMARPOLORB = "IMG_IBR_RCP_TITLEMARPOLORB";
    public static final String IMG_IBR_RCP_TITLEELOGBOOKS = "IMG_IBR_RCP_TITLEELOGBOOKS";
    public static final String IMG_IBR_RCP_HOMENEW = "IMG_IBR_RCP_HOMENEW";
    public static final String IMG_IBR_RCP_ALERTNEW = "IMG_IBR_RCP_ALERTNEW";
    public static final String IMG_IBR_RCP_COMPANYLOGO = "IMG_IBR_RCP_COMPANYLOGO";
    public static final String IMG_IBR_RCP_ECDISUP = "IMG_IBR_RCP_ECDISUP";
    public static final String IMG_IBR_RCP_LOGBOOK1 = "IMG_IBR_RCP_LOGBOOK1";
    public static final String IMG_IBR_RCP_LOGBOOK2 = "IMG_IBR_RCP_LOGBOOK2";
	
	
    public static final String IMG_IBR_CORE_TEXTURE = "IMG_IBR_CORE_TEXTURE";
    public static final String IMG_IBR_CORE_HARDWARETEXTURE = "IMG_IBR_CORE_HARDWARETEXTURE";
    public static final String IMG_IBR_CORE_REPEATER = "IMG_IBR_CORE_REPEATER";
    public static final String IMG_IBR_CORE_MOCKUP = "IMG_IBR_CORE_MOCKUP";
    
    public static final String IMG_IBR_SERIAL_SERIALPORT16 = "IMG_IBR_SERIAL_SERIALPORT16";
    public static final String IMG_IBR_SERIAL_SERIALPORT32 = "IMG_IBR_SERIAL_SERIALPORT32";
    public static final String IMG_IBR_SERIAL_SERIALPORTNMEA16 = "IMG_IBR_SERIAL_SERIALPORTNMEA16";
    public static final String IMG_IBR_SERIAL_SERIALPORTNMEA32 = "IMG_IBR_SERIAL_SERIALPORTNMEA32";
    public static final String IMG_IBR_SERIAL_LAPTOP16 = "IMG_IBR_SERIAL_LAPTOP16";
    public static final String IMG_IBR_SERIAL_LAPTOP32 = "IMG_IBR_SERIAL_LAPTOP32";
    public static final String IMG_IBR_SERIAL_INSTRUMENTRADAR16 = "IMG_IBR_SERIAL_INSTRUMENTRADAR16";
    public static final String IMG_IBR_SERIAL_INSTRUMENTRADAR32 = "IMG_IBR_SERIAL_INSTRUMENTRADAR32";
    public static final String IMG_IBR_SERIAL_INSTRUMENTWIND16 = "IMG_IBR_SERIAL_INSTRUMENTWIND16";
    public static final String IMG_IBR_SERIAL_INSTRUMENTWIND32 = "IMG_IBR_SERIAL_INSTRUMENTWIND32";
    public static final String IMG_IBR_SERIAL_INSTRUMENTII16 = "IMG_IBR_SERIAL_INSTRUMENTII16";
    public static final String IMG_IBR_SERIAL_INSTRUMENTII32 = "IMG_IBR_SERIAL_INSTRUMENTII32";
    public static final String IMG_IBR_SERIAL_GPS16 = "IMG_IBR_SERIAL_GPS16";
    public static final String IMG_IBR_SERIAL_GPS32 = "IMG_IBR_SERIAL_GPS32";
    public static final String IMG_IBR_SERIAL_NMEA16 = "IMG_IBR_SERIAL_NMEA16";
    public static final String IMG_IBR_SERIAL_NMEA32 = "IMG_IBR_SERIAL_NMEA32";
    public static final String IMG_IBR_SERIAL_ADD16 = "IMG_IBR_SERIAL_ADD16";
    public static final String IMG_IBR_SERIAL_EDIT16 = "IMG_IBR_SERIAL_EDIT16";
    public static final String IMG_IBR_SERIAL_DELETE16 = "IMG_IBR_SERIAL_DELETE16";
    public static final String IMG_IBR_SERIAL_WARNING16 = "IMG_IBR_SERIAL_WARNING16";
    public static final String IMG_IBR_SERIAL_WARNING32 = "IMG_IBR_SERIAL_WARNING32";
    public static final String IMG_IBR_SERIAL_HARDWARETEXTURE = "IMG_IBR_SERIAL_HARDWARETEXTURE";
    public static final String IMG_IBR_SERIAL_TEXTURE = "IMG_IBR_SERIAL_TEXTURE";
    public static final String IMG_IBR_SERIAL_SCANICON32 = "IMG_IBR_SERIAL_SCANICON32";
    public static final String IMG_IBR_SERIAL_FREEZEICON32 = "IMG_IBR_SERIAL_FREEZEICON32";
    public static final String IMG_IBR_SERIAL_VALIDATEICONS32 = "IMG_IBR_SERIAL_VALIDATEICONS32";
    
    public static final String IMG_IBR_GPS_EARTH = "IMG_IBR_GPS_EARTH";
    public static final String IMG_IBR_GPS_GPS16 = "IMG_IBR_GPS_GPS16";
    public static final String IMG_IBR_GPS_GPS32 = "IMG_IBR_GPS_GPS32";
    public static final String IMG_IBR_GPS_SAMPLE = "IMG_IBR_GPS_SAMPLE";
    public static final String IMG_IBR_GPS_SATELLITE128 = "IMG_IBR_GPS_SATELLITE128";
    public static final String IMG_IBR_GPS_SATELLITE24 = "IMG_IBR_GPS_SATELLITE24";
    public static final String IMG_IBR_GPS_TEXTURE = "IMG_IBR_GPS_TEXTURE";
    
    public static final String IMG_IBR_AIS_VESSEL16 = "IMG_IBR_AIS_VESSEL16";
    public static final String IMG_IBR_AIS_VESSEL32 = "IMG_IBR_AIS_VESSEL32";
    public static final String IMG_IBR_AIS_VESSEL64 = "IMG_IBR_AIS_VESSEL64";
    public static final String IMG_IBR_AIS_VESSELBLUE = "IMG_IBR_AIS_VESSELBLUE";
    public static final String IMG_IBR_AIS_VESSELCYAN = "IMG_IBR_AIS_VESSELCYAN";
    public static final String IMG_IBR_AIS_VESSELGRAY = "IMG_IBR_AIS_VESSELGRAY";
    public static final String IMG_IBR_AIS_VESSELGREEN = "IMG_IBR_AIS_VESSELGREEN";
    public static final String IMG_IBR_AIS_VESSELMAGENTA = "IMG_IBR_AIS_VESSELMAGENTA";
    public static final String IMG_IBR_AIS_VESSELORANGE = "IMG_IBR_AIS_VESSELORANGE";
    public static final String IMG_IBR_AIS_VESSELPINK = "IMG_IBR_AIS_VESSELPINK";
    public static final String IMG_IBR_AIS_VESSELRED = "IMG_IBR_AIS_VESSELRED";
    
    public static final String IMG_IBR_CHARTS_CENTERONBOAT = "IMG_IBR_CHARTS_CENTERONBOAT";
    public static final String IMG_IBR_CHARTS_CENTERONBOAT16 = "IMG_IBR_CHARTS_CENTERONBOAT16";
    public static final String IMG_IBR_CHARTS_LEFT = "IMG_IBR_CHARTS_LEFT";
    public static final String IMG_IBR_CHARTS_RIGHT = "IMG_IBR_CHARTS_RIGHT";
    public static final String IMG_IBR_CHARTS_ZOOMPLUS16 = "IMG_IBR_CHARTS_ZOOMPLUS16";
    public static final String IMG_IBR_CHARTS_ZOOMMINUS16 = "IMG_IBR_CHARTS_ZOOMMINUS16";
    public static final String IMG_IBR_CHARTS_ZOOMPLUS48 = "IMG_IBR_CHARTS_ZOOMPLUS48";
    public static final String IMG_IBR_CHARTS_ZOOMMINUS48 = "IMG_IBR_CHARTS_ZOOMMINUS48";
    public static final String IMG_IBR_CHARTS_BOAT = "IMG_IBR_CHARTS_BOAT";
    
    
    public static final String IMG_IBR_NWS_VALIDATE = "IMG_IBR_NWS_VALIDATE";
    public static final String IMG_IBR_NWS_VALIDATEALL = "IMG_IBR_NWS_VALIDATEALL";
    
    public static final String IMG_IBR_WAYPOINTS_DIRTO = "IMG_IBR_WAYPOINTS_DIRTO";
    public static final String IMG_IBR_WAYPOINTS_FLAG16 = "IMG_IBR_WAYPOINTS_FLAG16";
    public static final String IMG_IBR_WAYPOINTS_TEXTURE = "IMG_IBR_WAYPOINTS_TEXTURE";
    public static final String IMG_IBR_WAYPOINTS_WAYPOINTS16 = "IMG_IBR_WAYPOINTS_WAYPOINTS16";

    
	public static final String IMG_IBR_WEATHER_WEATHER16 = "IMG_IBR_WEATHER_WEATHER16";
	
	public static final String IMG_IBR_POLAR_FORCESPEED = "IMG_IBR_POLAR_FORCESPEED";
	public static final String IMG_IBR_POLAR_POLAR16 = "IMG_IBR_POLAR_POLAR16";
	public static final String IMG_IBR_POLAR_POLAR32 = "IMG_IBR_POLAR_POLAR32";
	public static final String IMG_IBR_POLAR_POLAR64 = "IMG_IBR_POLAR_POLAR64";
	public static final String IMG_IBR_POLAR_POLAR128 = "IMG_IBR_POLAR_POLAR128";
	public static final String IMG_IBR_POLAR_REDBTNRECORD = "IMG_IBR_POLAR_REDBTNRECORD";
	public static final String IMG_IBR_POLAR_SAMPLE = "IMG_IBR_POLAR_SAMPLE";
	
	public static final String IMG_IBR_ADP_SELECTCURSOR = "IMG_IBR_ADP_SELECTCURSOR";
	public static final String IMG_IBR_ADP_CHARACTERISTICS16 = "IMG_IBR_ADP_CHARACTERISTICS16";
	public static final String IMG_IBR_ADP_DETAILS16 = "IMG_IBR_ADP_DETAILS16";
	public static final String IMG_IBR_ADP_LIGHT16 = "IMG_IBR_ADP_LIGHT16";
	public static final String IMG_IBR_ADP_LIGHT32 = "IMG_IBR_ADP_LIGHT32";
	public static final String IMG_IBR_ADP_MOON16 = "IMG_IBR_ADP_MOON16";
	public static final String IMG_IBR_ADP_NOTES16 = "IMG_IBR_ADP_NOTES16";
	
	public static final String IMG_IBR_ECHART_HAND24 = "IMG_IBR_ECHART_HAND24";
	public static final String IMG_IBR_ECHART_ZOOMPLUS24 = "IMG_IBR_ECHART_ZOOMPLUS24";
	public static final String IMG_IBR_ECHART_ZOOMMINUS24 = "IMG_IBR_ECHART_ZOOMMINUS24";
	public static final String IMG_IBR_ECHART_SELECTION24 = "IMG_IBR_ECHART_SELECTION24";
	public static final String IMG_IBR_ECHART_WAYPOINTSELECTION24 = "IMG_IBR_ECHART_WAYPOINTSELECTION24";
	public static final String IMG_IBR_ECHART_INFORMATION24 = "IMG_IBR_ECHART_INFORMATION24";
	public static final String IMG_IBR_ECHART_FIND24 = "IMG_IBR_ECHART_FIND24";
	public static final String IMG_IBR_ECHART_EARTH24 = "IMG_IBR_ECHART_EARTH24";
	public static final String IMG_IBR_ECHART_CATALOGDOWNLOAD24 = "IMG_IBR_ECHART_CATALOGDOWNLOAD24";
	public static final String IMG_IBR_ECHART_ADD24 = "IMG_IBR_ECHART_ADD24";
	public static final String IMG_IBR_ECHART_DELETE24 = "IMG_IBR_ECHART_DELETE24";
	public static final String IMG_IBR_ECHART_KEY24 = "IMG_IBR_ECHART_KEY24";
	public static final String IMG_IBR_ECHART_PRINTER24 = "IMG_IBR_ECHART_PRINTER24";
	public static final String IMG_IBR_ECHART_SYSTEM_ENC24 = "IMG_IBR_ECHART_SYSTEM_ENC24";
	public static final String IMG_IBR_ECHART_HOME24 = "IMG_IBR_ECHART_HOME24";
	public static final String IMG_IBR_ECHART_BACKGROUND = "IMG_IBR_ECHART_BACKGROUND";
	
	public static final String IMG_IBR_ECHART_HOME50 = "IMG_IBR_ECHART_HOME50";
	public static final String IMG_IBR_ECHART_EXIT50 = "IMG_IBR_ECHART_EXIT50";
	public static final String IMG_IBR_ECHART_HAND50 = "IMG_IBR_ECHART_HAND50";
	public static final String IMG_IBR_ECHART_ZOOMPLUS50 = "IMG_IBR_ECHART_ZOOMPLUS50";
	public static final String IMG_IBR_ECHART_ZOOMMINUS50 = "IMG_IBR_ECHART_ZOOMMINUS50";
	public static final String IMG_IBR_ECHART_SELECTION50 = "IMG_IBR_ECHART_SELECTION50";
	public static final String IMG_IBR_ECHART_WAYPOINTSELECTION50 = "IMG_IBR_ECHART_WAYPOINTSELECTION50";
	public static final String IMG_IBR_ECHART_INFORMATION50 = "IMG_IBR_ECHART_INFORMATION50";
	public static final String IMG_IBR_ECHART_FIND50 = "IMG_IBR_ECHART_FIND50";
	public static final String IMG_IBR_ECHART_UPDATE50 = "IMG_IBR_ECHART_UPDATE50";
	public static final String IMG_IBR_ECHART_CATALOGDOWNLOAD50 = "IMG_IBR_ECHART_CATALOGDOWNLOAD50";
	public static final String IMG_IBR_ECHART_PRINTER50 = "IMG_IBR_ECHART_PRINTER50";
	
	public static final String IMG_IBR_PUKHO_HOME = "IMG_IBR_PUKHO_HOME";
	public static final String IMG_IBR_PUKHO_PUKHO = "IMG_IBR_PUKHO_PUKHO";
	public static final String IMG_IBR_PUKHO_COMP = "IMG_IBR_PUKHO_COMP";
	public static final String IMG_IBR_PUKHO_CLOCK = "IMG_IBR_PUKHO_CLOCK";
	public static final String IMG_IBR_PUKHO_LIGHT = "IMG_IBR_PUKHO_LIGHT";
	public static final String IMG_IBR_PUKHO_RADAR = "IMG_IBR_PUKHO_RADAR";
	public static final String IMG_IBR_PUKHO_LOCATION = "IMG_IBR_PUKHO_LOCATION";
	public static final String IMG_IBR_PUKHO_PRINTER = "IMG_IBR_PUKHO_PRINTER";
	public static final String IMG_IBR_PUKHO_SYNC = "IMG_IBR_PUKHO_SYNC";
	public static final String IMG_IBR_PUKHO_EXIT = "IMG_IBR_PUKHO_EXIT";

	public static final String IMG_IBR_ECHART_BASKET_HOME65="IMG_IBR_ECHART_BASKET_HOME65";
	public static final String IMG_IBR_ECHART_BASKET_ADD65="IMG_IBR_ECHART_BASKET_ADD65";
	public static final String IMG_IBR_ECHART_BASKET_DELETE65="IMG_IBR_ECHART_BASKET_DELETE65";
	public static final String IMG_IBR_ECHART_BASKET_FIND65="IMG_IBR_ECHART_BASKET_FIND65";
	public static final String IMG_IBR_ECHART_BASKET_PRINT65="IMG_IBR_ECHART_BASKET_PRINT65";
	public static final String IMG_IBR_ECHART_BASKET_SETTING65="IMG_IBR_ECHART_BASKET_SETTING65";
	public static final String IMG_IBR_ECHART_BASKET_SETTING65_EMPTY="IMG_IBR_ECHART_BASKET_SETTING65_EMPTY";
	public static final String IMG_IBR_ECHART_BASKET_EXIT65="IMG_IBR_ECHART_BASKET_EXIT65";
	
	public static final String IMG_IBR_ROOT_EDIT="IMG_IBR_ROOT_EDIT"; 
	
	public static final String IMG_IBR_LOGBOOK_INFO="IMG_IBR_LOGBOOK_INFO";
	public static final String IMG_IBR_LOGBOOK_HELPB = "IMG_IBR_LOGBOOK_HELPB";
	public static final String IMG_IBR_LOGBOOK_HELPC1 = "IMG_IBR_LOGBOOK_HELPC1";
	public static final String IMG_IBR_LOGBOOK_HELPC2 = "IMG_IBR_LOGBOOK_HELPC2";
	public static final String IMG_IBR_LOGBOOK_HELPC3 = "IMG_IBR_LOGBOOK_HELPC3";
	public static final String IMG_IBR_LOGBOOK_HELPC4 = "IMG_IBR_LOGBOOK_HELPC4";
	public static final String IMG_IBR_LOGBOOK_HELPC5 = "IMG_IBR_LOGBOOK_HELPC5";
	public static final String IMG_IBR_LOGBOOK2_HELPC2 = "IMG_IBR_LOGBOOK2_HELPC2";
	public static final String IMG_IBR_LOGBOOK2_HELPC4 = "IMG_IBR_LOGBOOK2_HELPC4";
	public static final String IMG_IBR_LOGBOOK2_HELPC6 = "IMG_IBR_LOGBOOK2_HELPC6";
	public static final String IMG_IBR_LOGBOOK2_HELPC7 = "IMG_IBR_LOGBOOK2_HELPC7";
	public static final String IMG_IBR_LOGBOOK_HELPC6 = "IMG_IBR_LOGBOOK_HELPC6";
	public static final String IMG_IBR_LOGBOOK_HELPC7 = "IMG_IBR_LOGBOOK_HELPC7";
	public static final String IMG_IBR_LOGBOOK_TANKMENU1 = "IMG_IBR_LOGBOOK_TANKMENU1";
	public static final String IMG_IBR_LOGBOOK_HELPA1 = "IMG_IBR_LOGBOOK_HELPA1";
	public static final String IMG_IBR_LOGBOOK2_HELPA8 = "IMG_IBR_LOGBOOK2_HELPA8";
	public static final String IMG_IBR_LOGBOOK2_HELPA9 = "IMG_IBR_LOGBOOK2_HELPA9";
	public static final String IMG_IBR_LOGBOOK_HELPA2 = "IMG_IBR_LOGBOOK_HELPA2";
	public static final String IMG_IBR_LOGBOOK_HELPA3 = "IMG_IBR_LOGBOOK_HELPA3";
	public static final String IMG_IBR_LOGBOOK_HELPA5 = "IMG_IBR_LOGBOOK_HELPA5";
	public static final String IMG_IBR_LOGBOOK_HELPI2 = "IMG_IBR_LOGBOOK_HELPI2";
	public static final String IMG_IBR_LOGBOOK_HELPI3 = "IMG_IBR_LOGBOOK_HELPI3";
	public static final String IMG_IBR_LOGBOOK_HELPI4 = "IMG_IBR_LOGBOOK_HELPI4";
	public static final String IMG_IBR_LOGBOOK_HELPI5 = "IMG_IBR_LOGBOOK_HELPI5";
	public static final String IMG_IBR_LOGBOOK_HELPI6 = "IMG_IBR_LOGBOOK_HELPI6";
	public static final String IMG_IBR_LOGBOOK2_HELPJ2 = "IMG_IBR_LOGBOOK2_HELPJ2";
	public static final String IMG_IBR_LOGBOOK2_HELPI2 = "IMG_IBR_LOGBOOK2_HELPI2";
	public static final String IMG_IBR_LOGBOOK2_HELPH2 = "IMG_IBR_LOGBOOK2_HELPH2";
	public static final String IMG_IBR_LOGBOOK_HELPA6 = "IMG_IBR_LOGBOOK_HELPA6";
	public static final String IMG_IBR_LOGBOOK2_HELPG1 = "IMG_IBR_LOGBOOK2_HELPG1";
	public static final String IMG_IBR_LOGBOOK2_HELPF1 = "IMG_IBR_LOGBOOK2_HELPF1";
	public static final String IMG_IBR_LOGBOOK2_HELPA1 = "IMG_IBR_LOGBOOK2_HELPA1";
	public static final String IMG_IBR_LOGBOOK_HELPA7 = "IMG_IBR_LOGBOOK_HELPA7";
	public static final String IMG_IBR_LOGBOOK2_HELPA7 = "IMG_IBR_LOGBOOK2_HELPA7";
	public static final String IMG_IBR_LOGBOOK2_HELPE1 = "IMG_IBR_LOGBOOK2_HELPE1";
	public static final String IMG_IBR_LOGBOOK2_HELPE2 = "IMG_IBR_LOGBOOK2_HELPE2";
	public static final String IMG_IBR_LOGBOOK2_HELPE3 = "IMG_IBR_LOGBOOK2_HELPE3";
	public static final String IMG_IBR_LOGBOOK2_HELPJ3 = "IMG_IBR_LOGBOOK2_HELPJ3";
	public static final String IMG_IBR_LOGBOOK2_HELPI3 = "IMG_IBR_LOGBOOK2_HELPI3";
	public static final String IMG_IBR_LOGBOOK_HELPA8 = "IMG_IBR_LOGBOOK_HELPA8";
	public static final String IMG_IBR_LOGBOOK2_HELPJ4 = "IMG_IBR_LOGBOOK2_HELPJ4";
	public static final String IMG_IBR_LOGBOOK_HELPA9 = "IMG_IBR_LOGBOOK_HELPA9";
	public static final String IMG_IBR_LOGBOOK2_HELPJ5 = "IMG_IBR_LOGBOOK2_HELPJ5";
	public static final String IMG_IBR_LOGBOOK2_HELPK2 = "IMG_IBR_LOGBOOK2_HELPK2";
	public static final String IMG_IBR_LOGBOOK2_HELPK3 = "IMG_IBR_LOGBOOK2_HELPK3";
	public static final String IMG_IBR_LOGBOOK2_HELPL2 = "IMG_IBR_LOGBOOK2_HELPL2";
	public static final String IMG_IBR_LOGBOOK2_HELPL4 = "IMG_IBR_LOGBOOK2_HELPL4";
	public static final String IMG_IBR_LOGBOOK2_HELPL3 = "IMG_IBR_LOGBOOK2_HELPL3";
	public static final String IMG_IBR_LOGBOOK2_HELPM2 = "IMG_IBR_LOGBOOK2_HELPM2";
	public static final String IMG_IBR_LOGBOOK2_HELPM3 = "IMG_IBR_LOGBOOK2_HELPM3";
	public static final String IMG_IBR_LOGBOOK2_HELPN2 = "IMG_IBR_LOGBOOK2_HELPN2";
	public static final String IMG_IBR_LOGBOOK2_HELPN3 = "IMG_IBR_LOGBOOK2_HELPN3";
	public static final String IMG_IBR_LOGBOOK2_HELPO3 = "IMG_IBR_LOGBOOK2_HELPO3";
	public static final String IMG_IBR_LOGBOOK2_HELPO4 = "IMG_IBR_LOGBOOK2_HELPO4";
	public static final String IMG_IBR_LOGBOOK2_HELPO5 = "IMG_IBR_LOGBOOK2_HELPO5";
	public static final String IMG_IBR_LOGBOOK2_HELPP2 = "IMG_IBR_LOGBOOK2_HELPP2";
	public static final String IMG_IBR_LOGBOOK2_HELPP3 = "IMG_IBR_LOGBOOK2_HELPP3";
	public static final String IMG_IBR_LOGBOOK2_HELPQ2 = "IMG_IBR_LOGBOOK2_HELPQ2";
	public static final String IMG_IBR_LOGBOOK2_HELPR2 = "IMG_IBR_LOGBOOK2_HELPR2";
	public static final String IMG_IBR_LOGBOOK2_HELPR3 = "IMG_IBR_LOGBOOK2_HELPR3";
	public static final String IMG_IBR_LOGBOOK2_HELPH4 = "IMG_IBR_LOGBOOK2_HELPH4";
	public static final String IMG_IBR_LOGBOOK2_HELPH3 = "IMG_IBR_LOGBOOK2_HELPH3";
	public static final String IMG_IBR_LOGBOOK2_HELPH1 = "IMG_IBR_LOGBOOK2_HELPH1";
	public static final String IMG_IBR_LOGBOOK2_HELPI1 = "IMG_IBR_LOGBOOK2_HELPI1";
	public static final String IMG_IBR_LOGBOOK2_HELPI4 = "IMG_IBR_LOGBOOK2_HELPI4";
	public static final String IMG_IBR_LOGBOOK2_HELPK1 = "IMG_IBR_LOGBOOK2_HELPK1";
	public static final String IMG_IBR_LOGBOOK2_HELPK4 = "IMG_IBR_LOGBOOK2_HELPK4";
	public static final String IMG_IBR_LOGBOOK2_HELPL1 = "IMG_IBR_LOGBOOK2_HELPL1";
	public static final String IMG_IBR_LOGBOOK2_HELPM1 = "IMG_IBR_LOGBOOK2_HELPM1";
	public static final String IMG_IBR_LOGBOOK2_HELPM4 = "IMG_IBR_LOGBOOK2_HELPM4";
	public static final String IMG_IBR_LOGBOOK2_HELPN1 = "IMG_IBR_LOGBOOK2_HELPN1";
	public static final String IMG_IBR_LOGBOOK2_HELPO1 = "IMG_IBR_LOGBOOK2_HELPO1";
	public static final String IMG_IBR_LOGBOOK2_HELPO2 = "IMG_IBR_LOGBOOK2_HELPO2";
	public static final String IMG_IBR_LOGBOOK2_HELPP1 = "IMG_IBR_LOGBOOK2_HELPP1";
	public static final String IMG_IBR_LOGBOOK2_HELPQ1 = "IMG_IBR_LOGBOOK2_HELPQ1";
	public static final String IMG_IBR_LOGBOOK2_HELPQ3 = "IMG_IBR_LOGBOOK2_HELPQ3";
	public static final String IMG_IBR_LOGBOOK2_HELPR1 = "IMG_IBR_LOGBOOK2_HELPR1";
	public static final String IMG_IBR_LOGBOOK2_HELPF2 = "IMG_IBR_LOGBOOK2_HELPF2";
	public static final String IMG_IBR_LOGBOOK2_HELPG2 = "IMG_IBR_LOGBOOK2_HELPG2";
	public static final String IMG_IBR_LOGBOOK2_HELPG3 = "IMG_IBR_LOGBOOK2_HELPG3";
	public static final String IMG_IBR_LOGBOOK2_HELPJ1 = "IMG_IBR_LOGBOOK2_HELPJ1";
	public static final String IMG_IBR_LOGBOOK2_HELPF3 = "IMG_IBR_LOGBOOK2_HELPF3";
	public static final String IMG_IBR_LOGBOOK2_HELPE4 = "IMG_IBR_LOGBOOK2_HELPE4";
	public static final String IMG_IBR_LOGBOOK2_HELPB1 = "IMG_IBR_LOGBOOK2_HELPB1";
	public static final String IMG_IBR_LOGBOOK2_HELPB3 = "IMG_IBR_LOGBOOK2_HELPB3";
	public static final String IMG_IBR_LOGBOOK2_HELPC1 = "IMG_IBR_LOGBOOK2_HELPC1";
	
	
	
	
	
	
	public static final String IMG_IBR_LOGBOOK_USER = "IMG_IBR_LOGBOOK_USER";
	public static final String IMG_IBR_LOGBOOK_ADD = "IMG_IBR_LOGBOOK_ADD";
	public static final String IMG_IBR_LOGBOOK_INFOICON="IMG_IBR_LOGBOOK_INFOICON";
	public static final String IMG_IBR_LOGBOOK_HELPB1 = "IMG_IBR_LOGBOOK_HELPB1";
	public static final String IMG_IBR_LOGBOOK_HELPB2 = "IMG_IBR_LOGBOOK_HELPB2";
	public static final String IMG_IBR_LOGBOOK_HELPB3 = "IMG_IBR_LOGBOOK_HELPB3";
	public static final String IMG_IBR_LOGBOOK_HELPB4 = "IMG_IBR_LOGBOOK_HELPB4";
	public static final String IMG_IBR_LOGBOOK_HELPB5 = "IMG_IBR_LOGBOOK_HELPB5";
	public static final String IMG_IBR_LOGBOOK2_HELPB2 = "IMG_IBR_LOGBOOK2_HELPB2";
	public static final String IMG_IBR_LOGBOOK_HELPB6 = "IMG_IBR_LOGBOOK_HELPB6";
	public static final String IMG_IBR_LOGBOOK_HELPD1 = "IMG_IBR_LOGBOOK_HELPD1";
	public static final String IMG_IBR_LOGBOOK_HELPD2 = "IMG_IBR_LOGBOOK_HELPD2";
	public static final String IMG_IBR_LOGBOOK_HELPD3 = "IMG_IBR_LOGBOOK_HELPD3";
	public static final String IMG_IBR_LOGBOOK_HELPD4 = "IMG_IBR_LOGBOOK_HELPD4";
	public static final String IMG_IBR_LOGBOOK_HELPD5 = "IMG_IBR_LOGBOOK_HELPD5";
	public static final String IMG_IBR_LOGBOOK2_HELPD4 = "IMG_IBR_LOGBOOK2_HELPD4";
	public static final String IMG_IBR_LOGBOOK2_HELPD2 = "IMG_IBR_LOGBOOK2_HELPD2";
	public static final String IMG_IBR_LOGBOOK_HELPD6 = "IMG_IBR_LOGBOOK_HELPD6";
	public static final String IMG_IBR_LOGBOOK2_HELPD3 = "IMG_IBR_LOGBOOK2_HELPD3";
	public static final String IMG_IBR_LOGBOOK_HELPE1 = "IMG_IBR_LOGBOOK_HELPE1";
	public static final String IMG_IBR_LOGBOOK_HELPE2 = "IMG_IBR_LOGBOOK_HELPE2";
	public static final String IMG_IBR_LOGBOOK_HELPE3 = "IMG_IBR_LOGBOOK_HELPE3";
	public static final String IMG_IBR_LOGBOOK_HELPF1 = "IMG_IBR_LOGBOOK_HELPF1";
	public static final String IMG_IBR_LOGBOOK_HELPF2 = "IMG_IBR_LOGBOOK_HELPF2";
	public static final String IMG_IBR_LOGBOOK_HELPF3 = "IMG_IBR_LOGBOOK_HELPF3";
	public static final String IMG_IBR_LOGBOOK_HELPG1 = "IMG_IBR_LOGBOOK_HELPG1";
	public static final String IMG_IBR_LOGBOOK_HELPG2 = "IMG_IBR_LOGBOOK_HELPG2";
	public static final String IMG_IBR_LOGBOOK_HELPG3 = "IMG_IBR_LOGBOOK_HELPG3";
	public static final String IMG_IBR_LOGBOOK_HELPH1 = "IMG_IBR_LOGBOOK_HELPH1";
	public static final String IMG_IBR_LOGBOOK_HELPH2 = "IMG_IBR_LOGBOOK_HELPH2";
	public static final String IMG_IBR_LOGBOOK_HELPH3 = "IMG_IBR_LOGBOOK_HELPH3";
	public static final String IMG_IBR_LOGBOOK_ALERT = "IMG_IBR_LOGBOOK_ALERT";
	public static final String IMG_IBR_LOGBOOK_HELPI = "IMG_IBR_LOGBOOK_HELPI";


	public static final String IMG_IBR_CLEAR_ROUTE="IMG_IBR_CLEAR_ROUTE";
	public static final String IMG_IBR_CLEAR_ROUTE_HOVER="IMG_IBR_CLEAR_ROUTE_HOVER";
	public static final String IMG_IBR_LOAD_ROUTE="IMG_IBR_LOAD_ROUTE";
	public static final String IMG_IBR_LOAD_ROUTE_HOVER="IMG_IBR_LOAD_ROUTE_HOVER";
	public static final String IMG_IBR_REPORT="IMG_IBR_REPORT";
	public static final String IMG_IBR_REPORT_HOVER="IMG_IBR_REPORT_HOVER";
	public static final String IMG_IBR_UPDATES="IMG_IBR_UPDATES";
	public static final String IMG_IBR_UPDATES_HOVER="IMG_IBR_UPDATES_HOVER";

	
	public static final String IMG_IBR_VERTICAL_LOGO="IMG_IBR_VERTICAL_LOGO";
	
	public static final String IMG_SMARTLOAD_LOGO="IMG_SMARTLOAD_LOGO";
	






}
