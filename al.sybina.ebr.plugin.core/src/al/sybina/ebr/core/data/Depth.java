/**
 * Project: capcode.basics Height.java created 30 juil. 2009 by cyrille 
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
 * $Date: 2009-10-09 08:26:37 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/Height.java,v 1.5 2009-10-09 08:26:37 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.4  2009/09/25 21:27:06  crosay
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.3  2009/09/13 12:10:39  crosay
 * Incomplete # wordwind plugin for capcode
 * http://sourceforge.net/apps/trac/capcode/ticket/6
 *
 * Revision 1.2  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.1  2009/08/15 23:43:49  crosay
 * Complete # add a close button on all internal frames
 * http://sourceforge.net/apps/trac/capcode/ticket/30
 * Complete # add reference points to maps
 * http://sourceforge.net/apps/trac/capcode/ticket/22
 * Incomplete # flight plugins
 * http://sourceforge.net/apps/trac/capcode/ticket/27
 *
 * $Revision: 1.5 $ 
 * $State: Exp $ 
 */


package al.sybina.ebr.core.data;

/**
 * @author cyrille
 *
 */
@SuppressWarnings("serial")
public class Depth extends Height {
	public static final Depth ZERO = new Depth(0);

	public Depth(double v, E_Unit unit) {
		super(v, unit);
	}
	
	public Depth(double v) {
		this(v, E_Unit.meters);
	}

	public Depth clone(){
		Depth result = new Depth(this.value, heightUnit);
		return result;
	}
	
	public E_Unit getDepthUnit(){
		return heightUnit;
	}
}
