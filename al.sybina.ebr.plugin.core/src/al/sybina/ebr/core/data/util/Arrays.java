/**
 * Project: capcode.basics Arrays.java created 16 janv. 2009 by cyrille 
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
 * $Date: 2009-01-21 22:13:21 $ 
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/util/Arrays.java,v 1.1 2009-01-21 22:13:21 crosay Exp $
 * $Log: not supported by cvs2svn $
 * $Revision: 1.1 $ 
 * $State: Exp $ 
 */


package al.sybina.ebr.core.data.util;

/**
 * @author cyrille
 *
 */
public class Arrays {
	/**
	 * Searches the specified array for the specified object using the binary search algorithm.
	 * sort the array into ascending order according to the natural ordering of its elements.
	 * @param array
	 * @param meaning
	 * @return index of the search key, if it is contained in the array; 
	 * otherwise the insertion point = the point at which the key would be inserted 
	 * into the array:
	 *  the index of the first element greater than the key, 
	 *  or array.length if all elements in the array are less than the specified key. 
	 *  Note that this guarantees that the return meaning will be >= 0 if and only if the key is found. 
	 */
	public static int searchArray(double[] array, double value) {
		int res = -(array.length+1);
		double gap = Double.MAX_VALUE;
		for (int i = 0; i < array.length; i++){
			if (value == array[i]){
				res = i;
				break;
			}
			else if(Math.abs(value - array[i]) > gap){
				res = i==1 ? -(array.length+1) : -(i-1);
				break;
			}
			gap = Math.abs(value - array[i]);
		}
		return res;
	}

	public static byte[] reverseArray(byte[] byteArray) {
        byte[] reversed = new byte[byteArray.length];
        for (int i = 0; i < byteArray.length; i++ ){
        	reversed[byteArray.length-i-1] = byteArray[i];
        }
		return reversed;
	}
	
}
