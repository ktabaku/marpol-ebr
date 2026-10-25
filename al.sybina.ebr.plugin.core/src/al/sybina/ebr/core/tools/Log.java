/**
 * CVS markups
 * last update by : $Author: crosay $
 * $Date: 2009-12-06 00:01:59 $
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/common/Debug.java,v 1.18 2009-12-06 00:01:59 crosay Exp $
 * $Name: not supported by cvs2svn $
 * $Log: not supported by cvs2svn $
 * Revision 1.17  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.16  2009/10/09 08:26:37  crosay
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.15  2009/09/29 20:33:33  crosay
 * Complete # add posibility to select a different language than the computer's default
 * http://sourceforge.net/apps/trac/capcode/ticket/32
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 *
 * Revision 1.14  2009/09/03 21:49:20  crosay
 * Incomplete # change the logging of event
 * http://sourceforge.net/apps/trac/capcode/ticket/33
 *
 * Revision 1.13  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.12  2009/07/19 09:12:43  crosay
 * Incomplete - task optimisation of the display
 * added the calling trace display for debug purpose
 *
 * Revision 1.11  2009/02/20 22:27:10  crosay
 * Incomplete - taskimplement the possibility to change perspective
 *
 * Revision 1.10  2009/01/24 20:52:36  crosay
 * Complete - taskRemoval of sqlite
 *
 * Complete - tasksuppression of debug display for preference saving
 *
 * Complete - tasktask 2531747 animation of the wearther
 * https://sourceforge.net/tracker2/?func=detail&aid=2024403&group_id=100698&atid=1045270
 *
 * Revision 1.9  2009/01/20 23:01:52  crosay
 * code cleaning (indentation)
 *
 * Revision 1.8  2008/09/11 15:06:32  crosay
 * defensive code added (check that object is not null)
 *
 * Revision 1.7  2008/05/28 16:28:55  crosay
 * added CVS markups in source code
 *
 * Revision 1.6  2008/05/28 08:14:01  crosay
 * update of class Debug: modification of the printing before the message
 *
 * $Revision: 1.18 $
 * $State: Exp $
 */
package al.sybina.ebr.core.tools;

import java.util.logging.FileHandler;

import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.Bundle;


/**
 * Class used to log events.
 */
public class Log {
	private static FileHandler fh;

	//logging in a plugin context
	public static void log(Bundle bundle, String msg) {
		log(bundle, Status.INFO, msg, null);
	}

	private static String getID(Bundle bundle) {
		if (bundle != null){
			return bundle.getSymbolicName();
		}
		return null;
	}

	private static void log(Bundle bundle, Status status) {
		if (bundle != null && Platform.isRunning()){
			Platform.getLog(bundle).log(status);
		}
	}

	private static void log(Bundle bundle, int severity, String msg, Exception e){
		if (bundle != null){
			String id = getID(bundle);
			if (id != null && !id.isEmpty()){
				Status status = new Status(severity, id, Status.OK, msg, e);
				if (status != null){
					log(bundle, status);
				}
			}
		}
	}

	public static void severe(Bundle bundle, String msg, Exception e) {
		log(bundle, Status.ERROR, msg, e);
	}

	public static void severe(Bundle bundle, String msg) {
		log(bundle, Status.ERROR, msg, null);
	}

	public static void warning(Bundle bundle, String msg, Exception e) {
		log(bundle, Status.WARNING, msg, e);
	}

	public static void warning(Bundle bundle, String msg) {
		log(bundle, Status.WARNING, msg, null);
	}

	public static void info(Bundle bundle, String msg, Exception e) {
		log(bundle, Status.INFO, msg, e);
	}

	public static void info(Bundle bundle, String msg) {
		log(bundle, Status.INFO, msg, null);
	}

}
