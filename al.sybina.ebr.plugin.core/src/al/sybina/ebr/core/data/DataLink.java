/**
 * Project: capcode.basics DataLink.java created 23 mars 2009 by cyrille
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
 * $Header: /media/disk/capcodeCVSbackup/capcode.basics/src/capcode/data/DataLink.java,v 1.4 2009-12-06 00:01:59 crosay Exp $
 * $Log: not supported by cvs2svn $
 * Revision 1.3  2009/10/14 21:50:17  crosay
 * Complete # when importing a polar, ask also to save it under capcode format.
 * http://sourceforge.net/apps/trac/capcode/ticket/40
 *
 * Revision 1.2  2009/08/31 22:18:08  crosay
 * Incomplete # preparation for delivery to version 0.9.3
 * http://sourceforge.net/apps/trac/capcode/ticket/17
 *
 * Revision 1.1  2009/03/31 07:42:19  crosay
 * Incomplete - taskSourceForge.net: CapCode-software suite for sailors: Detail: 2705654 - NMEA data mapping (FR 2605966 Input Mapping)
 * https://sourceforge.net/tracker2/index.php?func=detail&aid=2705654&group_id=100698&atid=1045270
 *
 * $Revision: 1.4 $
 * $State: Exp $
 */


package al.sybina.ebr.core.data;

import al.sybina.ebr.core.data.util.Fields;

/**
 * this class establish the link between a data and a NMEA field.
 */
public class DataLink {
	public enum DataKind{
		internalComputation,
		nmeaLink;
	}
	public DataKind kind = DataKind.nmeaLink;
	/**NMEA code (eg VWM)*/
	public String nmeaCode = ""; //$NON-NLS-1$
	/**index of the data in the NMEA message*/
	public int[] fieldNum;
	/**name of the data (must be a field of the calling class)*/
	public String dataName = ""; //$NON-NLS-1$
	/**field num in the same message to check for validity of the data*/
	public int[] validityField;
	/**meaning to check for validity of the data*/
	public String[] validityValues;
	/** information (text) about the computation */
	public String computationInfo;
	/**
	 * create a nmea linked data and initialise with params.
	 * @param name
	 * @param code
	 * @param indexes (comma separated list of indexes used to build the meaning)
	 * @param validityIndexes (comma separated list of validity field indexes used to check the meaning)
	 * @param validValues (comma separated list of values used to check the meaning)
	 */
	public DataLink(String name, String code, String indexes, String validityIndexes, String validValues) {
		this.kind = DataKind.nmeaLink;
		dataName = name;
		nmeaCode = code;
		String[] indexesAsString = indexes.split(","); //$NON-NLS-1$
		fieldNum = new int[indexesAsString.length];
		for(int i = 0; i<fieldNum.length; i++) {
			try {
				fieldNum[i] = Integer.parseInt(indexesAsString[i]);
			}catch(NumberFormatException nfe) {
				fieldNum[i] = -1;
			}
		}
		String[] validitiesAsString = validityIndexes.split(","); //$NON-NLS-1$
		validityField = new int[validitiesAsString.length];
		for(int i = 0; i<validityField.length; i++) {
			try {
				validityField[i] = Integer.parseInt(validitiesAsString[i]);
			}catch(NumberFormatException nfe) {
				validityField[i] = -1;
			}
		}
		validityValues = validValues.split(","); //$NON-NLS-1$
	}

	/**
	 * create a internal computation dataLink and initialise with its information.
	 * @param name
	 * @param information : free text to explain what is the computation
	 */
	public DataLink(String name, String information) {
		this.kind = DataKind.internalComputation;
		dataName = name;
		this.computationInfo = information;
	}

	/**
	 * get the fields as a string separated by a comma.
	 * @return a String for ex: "1,2"
	 */
	public String getFields() {
		String res = ""; //$NON-NLS-1$
		for (int i = 0; i < fieldNum.length; i++) {
			res += Integer.toString(fieldNum[i]);
			if (i < fieldNum.length - 1) {
				res += ","; //$NON-NLS-1$
			}
		}
		return res;
	}

	/**
	 * get the validity fields as a string separated by a comma.
	 * @return a String for ex: "1,2"
	 */
	public String getValidityFields() {
		String res = ""; //$NON-NLS-1$
		for (int i = 0; i < validityField.length; i++) {
			res += Integer.toString(validityField[i]);
			if (i < validityField.length - 1) {
				res += ","; //$NON-NLS-1$
			}
		}
		return res;
	}

	/**
	 * get the validity values as a string separated by a comma.
	 * @return a String for ex: "1,2"
	 */
	public String getValidityValues() {
		String res = ""; //$NON-NLS-1$
		for (int i = 0; i < validityValues.length; i++) {
			res += validityValues[i];
			if (i < validityValues.length - 1) {
				res += ","; //$NON-NLS-1$
			}
		}
		return res;
	}

	/**
	 * perform a OR check in the values with expected values.
	 * they must be a same number of validityField than validity expexted values.<br>
	 * ex: we expect &ltvalidity field="2,2" values= "R,L"/&gt<br>
	 * it means that we want field 2 to be either R or L <br>
	 * if we write only &ltvalidity field="2" values= "R,L"/&gt<br>
	 * it means that we expect field 2 to be "R" ("L" will be ignored)
	 * @param nmeaMessageFields : the NMEA message.getFields()
	 * @return true as soon as a valid result is found
	 */
	public boolean isValid(Fields nmeaMessageFields) {
		//by default valid = true (we may not request validity check)
		boolean res = true;
		for(int i = 0; i<validityField.length; i++) {
			res = nmeaMessageFields.atPos(validityField[i]).equalsIgnoreCase(validityValues[i]);
			if (res) {
				break;
			}
		}
		return res;
	}

	/**
	 * build a string with all values of a Fields structure. a Fields structure
	 * is an object issued from a {@link String#split(String)}<br>
	 * this is used for example to get the longitude value with the N/S/W/E
	 * field of a NMEA message
	 * 
	 * @param fields
	 * @return a string with all the values of the string field
	 */
	public String getValuesFromMessage(Fields fields) {
		String res = ""; //$NON-NLS-1$
		//for all fields
		for (int element : fieldNum) {
			//concat the field meaning into result
			res = res + fields.atPos(element);
		}
		//a string representing all the values of the fields
		return res;
	}

	/**
	 * returns the name of the message and index as String.
	 */
	@Override
	public String toString() {
		String param = ""; //$NON-NLS-1$
		if (kind == DataKind.nmeaLink) {
			String nums = ""; //$NON-NLS-1$
			for (int i=0; i < fieldNum.length; i++) {
				nums += fieldNum[i] + (i < fieldNum.length -1 ? "," : ""); //$NON-NLS-1$ //$NON-NLS-2$
			}
			param = nums;
		}
		else {
			param = computationInfo;
		}
		return String.format("%s: %s[%s]", kind.toString(), nmeaCode, param); //$NON-NLS-1$
	}

}
