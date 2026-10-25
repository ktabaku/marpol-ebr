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
public class Height extends Data {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	public enum E_Unit{
		meters,
		feet,
		fathoms,
	}
	//1 foot = 0.3048 meters
	static final double FOOT_TO_METER = 0.3048;
	//1 meter = 3.2808399 feet
	static final double METER_TO_FEET = 3.2808399;
	static final double FATHOM_TO_METER = 1.8288;
	static final double METER_TO_FATHOM = 0.546806649;
	public static final Height ZERO = new Height(0);
	protected E_Unit heightUnit = E_Unit.meters;

	public Height() {
		super();
		value = 0;
		setHeightUnit(E_Unit.meters);
	}

	public Height(double v, boolean isValid) {
		super(v, isValid);
		setHeightUnit(E_Unit.meters);
	}

	public Height(String s) {
		super(s.isEmpty()? 0 : Double.parseDouble(s), true);
		setHeightUnit(E_Unit.meters);
	}

	public Height(double v, E_Unit u) {
		setValue(v, u);
		setHeightUnit(E_Unit.meters);
	}


	public Height(double v) {
		this(v, true);
	}

	@Override
	public void setValue(double v) {
		value = v;
	}

	public void setValue(double v, E_Unit unit) {
		setUnit(unit.toString());
		switch (unit) {
			case meters:
				value = v;
				break;
			case feet:
				value = v * FOOT_TO_METER;
				break;
			case fathoms:
				value = v * FATHOM_TO_METER;
				default:
					value = v;
		}
	}

	@Override
	public double getValue(){
		return getValue(heightUnit);
	}

	public double getValue(E_Unit unit) {
		double res = 0;
		switch (unit) {
			case meters:
				res = value;
				break;
			case feet:
				res = value * METER_TO_FEET;
				break;
			case fathoms:
				res = value * METER_TO_FATHOM;
				break;
			default:
				res = value;
		}
		return res;
	}

	/**
	 * @return the heightUnit
	 */
	public E_Unit getHeightUnit() {
		return heightUnit;
	}

	/**
	 * @param u the heightUnit to set
	 */
	public void setHeightUnit(E_Unit u) {
		heightUnit = u;
		unit = u.toString();
	}

	public double toKilometer() {
		return value * 1000;
	}

	public double toFeet() {
		return value * METER_TO_FEET;
	}

	public double toFathoms() {
		return value * METER_TO_FATHOM;
	}

	/**
	 * @param u the heightUnit to set
	 */
	@Override
	public void setUnit(String u) {
		if (u.equalsIgnoreCase("m") || u.equalsIgnoreCase("meter") || u.equalsIgnoreCase("meters")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setHeightUnit(E_Unit.meters);
		}
		else if (u.equalsIgnoreCase("ft") || u.equalsIgnoreCase("foot") || u.equalsIgnoreCase("feet")) { //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
			this.setHeightUnit(E_Unit.feet);
		}
		else if (u.equalsIgnoreCase("fathom") || u.equalsIgnoreCase("fathoms")) { //$NON-NLS-1$ //$NON-NLS-2$
			this.setHeightUnit(E_Unit.fathoms);
		}
		else{
			this.setHeightUnit(E_Unit.meters);
		}
	}

	@Override
	public Height clone() {
		Height result = new Height(this.value, this.valid);
		result.unit = this.unit;
		return result;
	}

	/**
	 * return the in meters the meaning v (supposed to be in feet)
	 * @param v altitude in feet
	 * @return the meaning of v in meters
	 */
	public static double feetToMeters(double v) {
		return v * FOOT_TO_METER;
	}
	/**
	 * return the in meters the meaning v (supposed to be in fathoms)
	 * @param v  in fathoms
	 * @return the meaning of v in meters
	 */
	public static double fathomsToMeters(double v) {
		return v * FATHOM_TO_METER;
	}

	/**
	 *
	 * @param u : must be either meters, feet, fathoms
	 * @return 0 for meter, 1 for feet, 2 for fathoms
	 */
	public static int getUnitCode(String u) {
		String su = u.toLowerCase();
		return E_Unit.valueOf(su).ordinal();
	}

}
