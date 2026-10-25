package com.oceanpro.core.utils;

public class ScaleConversionUtils {

	
	
	public static double  fromBeaufortToMaxKnots(int grade) {
		
		if (grade==0) return 1.0;
		if (grade==1) return 3.0;
		if (grade==2) return 6.0;
		if (grade==3) return 10.0;
		if (grade==4) return 16.0;
		if (grade==5) return 21.0;
		if (grade==6) return 27.0;
		if (grade==7) return 33.0;
		if (grade==8) return 40.0;
		if (grade==9) return 47.0;
		if (grade==10) return 55.0;
		if (grade==11) return 63.0;
		if (grade==12) return Double.MAX_VALUE;
		
		return 0.0;
	}
	
	
	public static int  fromKnotsToBeaufort(double knots) {
		
		if (knots<=0) return 0;
		
		if (knots<=1.0) return 0;
		else if (knots<=3.0) return 1;
		else if (knots<=6.0) return 2;
		else if (knots<=10.0) return 3;
		else if (knots<=16.0) return 4;
		else if (knots<=21.0) return 5;
		else if (knots<=27.0) return 6;
		else if (knots<=33.0) return 7;
		else if (knots<=40.0) return 8;
		else if (knots<=47.0) return 9;
		else if (knots<=55.0) return 10;
		else if (knots<=63.0) return 11;
		else return 12;
	}

	public static double  fromDouglasToMaxWaveHeightInMeters(int grade) {
 	
		if (grade==0) return 0.0;
		if (grade==1) return 0.1;
		if (grade==2) return 0.5;
		if (grade==3) return 1.3;
		if (grade==4) return 2.5;
		if (grade==5) return 4.0;
		if (grade==6) return 6.0;
		if (grade==7) return 9.0;
		if (grade==8) return 14.0;
		if (grade==9) return Double.MAX_VALUE;
		
		return 0.0;
	}
}
