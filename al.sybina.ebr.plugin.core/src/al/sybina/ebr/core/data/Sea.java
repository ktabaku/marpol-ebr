/**
 * Project: capcode.toolbox
 * Sea.java
 * created 8 May 2007 by rosaycy
 * Copyright (c) 2007 Capcode Inc. All Rights Reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * -Redistribution of source code must retain the above copyright notice, this
 *  list of conditions and the following disclaimer.
 * 
 * -Redistribution in binary form must reproduce the above copyright notice, 
 *  this list of conditions and the following disclaimer in the documentation
 *  and/or other materials provided with the distribution.
 * 
 * Neither the name of Capcode, Inc. or the names of authors may 
 * be used to endorse or promote products derived from this software without 
 * specific prior written permission.
 * 
 * This software is provided "AS IS," without a warranty of any kind. ALL 
 * EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES, INCLUDING
 * ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE
 * OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN MIDROSYSTEMS, INC. ("SUN")
 * AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY LICENSEE
 * AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THIS SOFTWARE OR ITS
 * DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE FOR ANY LOST 
 * REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT, SPECIAL, CONSEQUENTIAL, 
 * INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER CAUSED AND REGARDLESS OF THE THEORY 
 * OF LIABILITY, ARISING OUT OF THE USE OF OR INABILITY TO USE THIS SOFTWARE, 
 * EVEN IF SUN HAS BEEN ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * You acknowledge that this software is not designed, licensed or intended
 * for use in the design, construction, operation or maintenance of any
 * nuclear facility.

 * 
 */
/**
 * CVS markups
 * last update by : $Author: crosay $ 
 * $Date: 2008-05-28 16:28:54 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Sea.java,v 1.6 2008-05-28 16:28:54 crosay Exp $
 * $Log: not supported by cvs2svn $
 * $Revision: 1.6 $ 
 * $State: Exp $ 
 */
package al.sybina.ebr.core.data;

/**
 * Class used to store Sea information.
 * 
 * @author rosaycy
 */
public class Sea {
	/**
	 * sea Disturbance Number in beaufort 0 Calm 0m 1 Ripples 0m - 0,1m 2
	 * Wavelets 0,1m - 0,5m 3 Small waves 0,5m - 1,25m 4 Moderate 1,25m - 2,5m 5
	 * Rough 2,5m - 4m 6 Very rough 4m - 6m 7 High waves 6m - 9m 8 Very High
	 * waves 9m - 14m 9 Huge 14m and more
	 */
	private int code;
	public static Sea CALM = new Sea(0);
	public static Sea RIPPLES = new Sea(1);
	public static Sea WAVELETS = new Sea(2);
	public static Sea SMALL_WAVES = new Sea(3);
	public static Sea MODERATE = new Sea(4);
	public static Sea ROUGH = new Sea(5);
	public static Sea VERY_ROUGH = new Sea(6);
	public static Sea HIGH_WAVES = new Sea(7);
	public static Sea VERY_HIGH_WAVES = new Sea(8);
	public static Sea HUGE = new Sea(9);

	/**
	 * 
	 * creates a Sea structure initialized with
	 * 
	 * @param code
	 */
	public Sea(int code) {
		this.code = code;
	}

	/**
	 * @return the code
	 */
	public int getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(int code) {
		this.code = code;
	}
}
