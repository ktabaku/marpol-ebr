package com.oceanpro.utils;

public class Coordinates {
	
	private double latitude; // 0, 180
	private double longitude; // 0, 360
	
	public static int COOD_TYPE_POSITIVE_DEGEE = 1; // 0,360; 0,180
	public static int COOD_TYPE_NEGATIVE_DEGEE = 2; // -180,180; 90,-90
	public static int COOD_TYPE_DMS = 3; // Gradi minuti secondi (DMS) Esempio: N 41<simbolo_gradi> 53' 24" E 012<simbolo_gradi> 29' 32" 
	
	
	public Coordinates(String latitude, String longitude, int coordsType){
		
		if (coordsType==COOD_TYPE_POSITIVE_DEGEE) {
			this.latitude = Double.parseDouble(latitude);
			this.longitude = Double.parseDouble(longitude);
		}
		else if (coordsType==COOD_TYPE_NEGATIVE_DEGEE) {
//			if (Double.parseDouble(latitude)>=0) this.latitude = Double.parseDouble(latitude);
//			else this.latitude = Double.parseDouble(latitude)+360;
			this.latitude = 90.0-Double.parseDouble(latitude);
			
			if (Double.parseDouble(longitude)>=0) this.longitude = Double.parseDouble(longitude);
			else this.longitude = Double.parseDouble(longitude)+360;
		}
		else if (coordsType==COOD_TYPE_DMS) {
			this.latitude = getGradi(latitude);
			this.longitude = getGradi(longitude);
		}
	}

	
	
	@Override
	public String toString() {
		String res = "";
		
		res+= "Longitude: " + getLongitudeDms() + " \n";
		res+= "Latitude: " + getLatitudeDms() + " \n";
		
		return res;
	}
	
	
	/**
	 * Restituisce la longitudine nel formato 0 - 360.
	 * @return
	 */
	public double getLongitudePositiveDegree() {
		return longitude;
	}
	/**
	 * Restituisce la latitudine nel formato 0 - 180.
	 * @return
	 */
	public double getLatitudePositiveDegree() {
		return latitude;
	}
	
	/**
	 * Restituisce la longitudine nel formato 0 - 180. Positivo se East, negativo se Ovest
	 * @return
	 */
	public double getLongitudeNegativeDegree() {
		if (longitude>180) return longitude-360; 
		return longitude;
	}
	/**
	 * Restituisce la latitudine nel formato 0 - 90. Positivo se Nord, negativo se Sud
	 * @return
	 */
	public double getLatitudeNegativeDegree() {
		
		if (latitude<=90) return 90-latitude; 
		return -(latitude-90);
	}
	
	/**
	 * Restituisce la longitudine nel formato 0 - 180 E/W. 
	 * @return
	 */
	public String getLongitudeDegreeWithCardinalSign() {
		if (longitude>180) return Math.abs(longitude-360)+"W"; 
		return longitude+"E";
	}
	/**
	 * Restituisce la latitudine nel formato 0 - 90 S/N. 
	 * @return
	 */
	public String getLatitudeDegreeWithCardinalSign() {
		if (latitude<=90) return 90-latitude + "N"; 
		return (latitude-90)+"S";
	}
	
	/**
	 * Restituisce la longitudine nel formato N 41<simbolo_gradi> 53' 24". 
	 * @return
	 */
	public String getLongitudeDms() {
		return getDmsFromPosition(false);
	}
	/**
	 * Restituisce la latitudine nel formato N 41<simbolo_gradi> 53' 24". 
	 * @return
	 */
	public String getLatitudeDms() {
		return getDmsFromPosition(true);
	}
	
	
	
	
	
	
	/********************AC 14/03 Aggiunti metodi per restituire Lati e Long troncata ******************** 
	/**
	 * Restituisce la longitudine nel formato 0 - 180 E/W. 
	 * @return
	 */
	public String getLongitudeDegreeWithCardinalSign(int decimale) { 
		if (longitude>180) {

			return arrotondaPerDifetto(Math.abs(longitude-360), decimale)+"W";
		}
		return arrotondaPerDifetto(longitude,decimale) +"E";
	}
	/**
	 * Restituisce la latitudine nel formato 0 - 180 S/N. 
	 * @return
	 */
	public String getLatitudeDegreeWithCardinalSign(int decimale) {
		if (latitude>180) return arrotondaPerDifetto(Math.abs(latitude-360), decimale) +"S"; 
		return arrotondaPerDifetto(latitude, decimale) +"N";
	}
	
	
	// ARROTONDAMENTO PER DIFETTO
    public double arrotondaPerDifetto (double value, int numCifreDecimali) {
	  double temp = Math.pow(10, numCifreDecimali);
	  return Math.floor(value * temp) / temp; 
    }
	
	/*******************************************************************************************************
	
	
	
	
	
	/************************* PRIVATE ****************************/
	
	/**
	 * Restituisce una posizione nel formato N 41<simbolo_gradi> 53' 24". 
	 * @param latitude Indica se deve restituire la latitudine o longitudine.
	 * @return
	 */
	private String getDmsFromPosition(boolean latitude) {
		
		double position =0;
		
		if (latitude) position = getLatitudeNegativeDegree();
		else position = getLongitudeNegativeDegree();
		
		//System.out.println("in negative degree:" + position);
		
		String cardinalSign ="";
		if (latitude) {
			if (position>0) cardinalSign = "N";
			else cardinalSign = "S";
		}
		else {
			if (position>0) cardinalSign = "E";
			else cardinalSign = "W";
		}
		position = Math.abs(position);
		
		int gradi = (int)position;
		
		double restoGradi = position-gradi;
		
		

//		String doubleValue = Double.toString(position);  
//		String minutiStr = doubleValue.substring(doubleValue.indexOf('.'));  
//		double minutiDou = Double.parseDouble(minutiStr);
		
//		int minuti = (int)minutiDou/60;
		int primi = (int)(restoGradi*60.0);
		
		double restoPrimi = (restoGradi*60.0)-primi;
		
//		doubleValue = Double.toString(minutiDou/60);  
//		String secondiStr = doubleValue.substring(doubleValue.indexOf('.'));  
//		double secondiDou = Double.parseDouble(secondiStr);
//		
//		int secondi = (int)secondiDou;
		int secondi = (int)(restoPrimi*60.0);
		
		
//		return cardinalSign + " " + gradi + (char)176 + " " + minuti + "' " + secondi + "\"";
		return cardinalSign + " " + gradi + (char)176 + " " + primi + "' " + secondi + "\"";
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * trasforma una posizione in gradi (0 - 360)
	 * 
	 * @param position position like     N 41<simbolo_gradi> 53' 24"   OR 28,45 N   OR   S 127,34    OR  145 33' N
	 *
	 * @return Position in Primi
	 */
	private static Double getGradi(String position) {
		try {
			System.out.println("Posizione da trasformare : " + position);
			if (position==null) return null;
			
			int sign = 1;
			if (position.indexOf("N")!=-1) {
				position = position.substring(0, position.indexOf("N")) + position.substring(position.indexOf("N")+1);
				sign = 1;
			}
			if (position.indexOf("n")!=-1) {
				position = position.substring(0, position.indexOf("n")) + position.substring(position.indexOf("n")+1);
				sign = 1;
			}
			if (position.indexOf("S")!=-1) {
				position = position.substring(0, position.indexOf("S")) + position.substring(position.indexOf("S")+1);
				sign = -1;
			}
			if (position.indexOf("s")!=-1) {
				position = position.substring(0, position.indexOf("s")) + position.substring(position.indexOf("s")+1);
				sign = -1;
			}
			if (position.indexOf("E")!=-1) {
				position = position.substring(0, position.indexOf("E")) + position.substring(position.indexOf("E")+1);
				sign = 1;
			}
			if (position.indexOf("e")!=-1) {
				position = position.substring(0, position.indexOf("e")) + position.substring(position.indexOf("e")+1);
				sign = 1;
			}
			if (position.indexOf("O")!=-1) {
				position = position.substring(0, position.indexOf("O")) + position.substring(position.indexOf("O")+1);
				sign = -1;
			}
			if (position.indexOf("o")!=-1) {
				position = position.substring(0, position.indexOf("o")) + position.substring(position.indexOf("o")+1);
				sign = -1;
			}
			if (position.indexOf("W")!=-1) {
				position = position.substring(0, position.indexOf("W")) + position.substring(position.indexOf("W")+1);
				sign = -1;
			}
			if (position.indexOf("w")!=-1) {
				position = position.substring(0, position.indexOf("w")) + position.substring(position.indexOf("w")+1);
				sign = -1;
			}
			
			//System.out.println("calcolo segno - pos: " + position + "  sign:" + sign);
			
			
			
			// Elimino spazi iniziali e finali
			position = position.trim();
			
			String gradi = null;
			String primi = null;
			String secondi = null;
			
			
			// Elimino segno dei gradi, i gradi sono sempre il primo numero
			if (position.indexOf(176)!=-1) { // simbolo gradi
				gradi = position.substring(0, position.indexOf(176));
				position = position.substring(position.indexOf(176)+1);
			}
			else if (position.indexOf(",")!=-1) {
				gradi = position.substring(0, position.indexOf(","));
				primi = position.substring(position.indexOf(",")+1);
				position=null;
			}
			else if (position.indexOf(".")!=-1) {
				gradi = position.substring(0, position.indexOf("."));
				primi = position.substring(position.indexOf(".")+1);
				position=null;
			}
			else if (position.indexOf(" ")!=-1) { // caso separatore spazio
				gradi = position.substring(0, position.indexOf(" "));
				position = position.substring(position.indexOf(" ")+1);
			}
			
			if (position!=null) {
				position = position.trim();
				if (position.length()>0) {
					// Calcolo primi e secondi
					if (position.indexOf("'")!=-1) {
						primi = position.substring(0, position.indexOf("'"));
						position = position.substring(position.indexOf("'")+1);
					}
				
					position = position.trim();
					
					if (position.indexOf("\"")!=-1) {
						secondi = position.substring(0, position.indexOf("\""));
					}
				}
			}
			
			
			//System.out.println("gradi:" + gradi + "  primi:" + primi + "  secondi:" + secondi);
			
			double gradiNum =0;
			if (gradi!=null) {
				gradi = gradi.trim();
				// Trasformo
				gradiNum =  Double.parseDouble(gradi);
			}
			if (primi!=null) {
				primi = primi.trim();
				// Trasformo
				gradiNum +=  Double.parseDouble(primi)/60;
			}
			if (secondi!=null) {
				secondi = secondi.trim();
				// Trasformo
				gradiNum +=  (Double.parseDouble(secondi)/60)/60;
			}

			if (sign<0) gradiNum = gradiNum+360;
			
			System.out.println("Gradi:" + gradiNum);
			
			return gradiNum;
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
