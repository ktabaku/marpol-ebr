/**
 * 
 */
package al.sybina.ebr.core.data;

/**
 * @author christian rosay
 *
 */
public class Temperature extends Data {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public enum E_temperatureUnit{
		Kelvin ("K"), //$NON-NLS-1$
		Celcius ("C"), //$NON-NLS-1$
		Fahrenheit ("F"); //$NON-NLS-1$
		
		String text;
		E_temperatureUnit(String aText){
			text = aText;
		}
	}

	E_temperatureUnit tempUnit = E_temperatureUnit.Celcius;
	
	public Temperature(double v, E_temperatureUnit unit){
		super(v, unit.text);
		tempUnit = unit;
	}
	
	public void changeUnit(E_temperatureUnit unit){
		switch(unit){
		case Celcius : 
			value = toCelcius();
			break;
		case Fahrenheit: 
			value = toFahrenheit();
			break;
		case Kelvin:
			value = toKelvin();
			break;
		}
		super.setUnit(unit.text);
		tempUnit = unit;
	}
	
	public double toKelvin(){
		switch(tempUnit){
			case Celcius:  return value + 273.15;
			case Fahrenheit:  return (value + 459.67) / 1.8;		
			default:
			return value;
		}
	}
	
	public double toCelcius(){
		switch(tempUnit){
			case Kelvin:  return value - 273.15;
			case Fahrenheit:  return (value -32) / 1.8; 		
			default:
			return value;
		}
	}
	public double toFahrenheit(){
		switch(tempUnit){
			case Celcius:  return value * 1.8 +32;
			case Kelvin:  return value * 1.8 - 459.67;		
			default:
			return value;
		}
	}
}
