/**
 * Project: capcode.basics IO.java created 22 ao�t 2009 by cyrille 
 * Copyright (c) 2007 Capcode Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * - Neither the name of Capcode, Inc. or the names of authors may be used to
 * endorse or promote products derived from this software without specific prior
 * written permission.
 * 
 * - This software is provided "AS IS," without a warranty of any kind. ALL
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING ANY
 * IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE OR
 * NON-INFRINGEMENT, ARE HEREBY EXCLUDED. CAPCODE AND ITS
 * LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE AS A
 * RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS DERIVATIVES.
 * IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST REVENUE, PROFIT
 * OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR
 * PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY,
 * ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, EVEN IF CAPCODE HAS
 * BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * - You acknowledge that this software is not designed, licensed or intended for
 * use in the design, construction, operation or maintenance of any nuclear
 * facility or airborne software.
 * - You acknowledge that using this software for the purpose of navigation does not
 * engage the developer responsability in case of damage, injuries or casualities due
 * to the use of this software.
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2009-12-06 00:01:59 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/io/IO.java,v 1.6 2009-12-06 00:01:59 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.5  2009/11/02 00:10:37  crosay
 * Incomplete # add AIS capability
 * http://sourceforge.net/apps/trac/capcode/ticket/42
 * Complete # required by ticket 6: install a change waypoint event mechanism
 * http://sourceforge.net/apps/trac/capcode/ticket/37
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.4  2009/10/14 21:50:17  crosay
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 *
 * Revision 1.3  2009/09/26 23:06:19  crosay
 * change message in case of error on com port
 *
 * Revision 1.2  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.1  2009/08/25 22:33:59  crosay
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 * Incomplete # move the display of the VMG arrow from map display plugin to polar display plugin
 * http://sourceforge.net/apps/trac/capcode/ticket/29
 *
 * $Revision: 1.6 $ 
 * $State: Exp $ 
 */


package al.sybina.ebr.core.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import org.eclipse.osgi.internal.debug.Debug;

import al.sybina.ebr.core.tools.Log;


/**
 * @author cyrille
 *
 */
public class IO {
	/**
	 * copy a the fileInName file tofileOutName.
	 * @param fileInName
	 * @param fileOutName
	 * @throws IOException
	 */
	public static void copyFile(String srcPath, String dstPath)
			throws IOException {
		File src = new File(srcPath);
		File dst = new File(dstPath);
		copyFile(src, dst);
	}
	
	public static void copyFile(File src, File dst) throws IOException {
		if (!src.getAbsoluteFile().equals(dst.getAbsoluteFile())) {
			FileChannel inChannel = new FileInputStream(src).getChannel();
			FileChannel outChannel = new FileOutputStream(dst).getChannel();
			try {
				inChannel.transferTo(0, inChannel.size(), outChannel);
			} finally {
				if (inChannel != null) {
					inChannel.close();
				}
				if (outChannel != null) {
					outChannel.close();
				}
			}
		}
	}

	public static void copyFolder(File src, File dest) throws IOException{
		Debug.println("copying " + src  + " into " + dest);
		if(src.isDirectory()){    
			if(!dest.exists()){
				dest.mkdir();
			}
			for (String file : src.list()) {
				src = new File(src, file);
				dest = new File(dest, file);
				copyFolder(src, dest);
			}
		}else{
			
		}
	}

}
