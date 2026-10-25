package al.sybina.ebr.commons.utils;

import org.apache.commons.validator.routines.RegexValidator;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;

import jidefx.scene.control.field.FormattedTextField;
import jidefx.scene.control.field.verifier.IntegerRangePatternVerifier;
import jidefx.scene.control.field.verifier.NumberRangePatternVerifier;
import jidefx.scene.control.field.verifier.StringValuesPatternVerifier;
import jidefx.scene.control.validation.ValidationMode;
import jidefx.scene.control.validation.ValidationUtils;
import jidefx.utils.CommonUtils;

public class CoordinateUIUtils {
	
	private static final String PATTERN_DMM = "\\d{1,3}°\\d{1,2}(.\\d+)?'[NESW]";
	
	public static FormattedTextField<Latitude> createLatitudeField() {
		FormattedTextField<Latitude> latField = new FormattedTextField<>();
		initializeLatitudeField(latField);
		return latField;
	}

	public static void initializeLatitudeField(FormattedTextField<Latitude> latField) {
		latField.getPatternVerifiers().put("degres", new IntegerRangePatternVerifier(0, 90, true));
		latField.getPatternVerifiers().put("decimalMinutes", new NumberRangePatternVerifier(0.0, 59.9999) {
			@Override
			public Boolean call(String text) {
		        if (text.length() > getMaxLength()) return false;
		        try {
		            double i = parse(text).doubleValue();
					
					String degres = latField.getGroupText("degres");
					int dd = Integer.parseInt(degres);
					if(dd == 90) return i == 0.0;	
		            
					if (i >= getMin().doubleValue() && i <= getMax().doubleValue()) return true;
		        }
		        catch (NumberFormatException e) {
		            CommonUtils.ignoreException(e);
		        }
		        return false;
			}	
		});
		latField.getPatternVerifiers().put("northsouth", new StringValuesPatternVerifier<String>(new String[]{"N", "S"}){
		    @Override
		    public String toTargetValue(String fieldValue) {
		        return fieldValue;
		    }

		    @Override
		    public String fromTargetValue(String previousFieldValue, String targetValue) {
		        return targetValue;
		    }
		});
		
		latField.setPattern("degres°decimalMinutes'northsouth");
		latField.setStringConverter(new LatitudeConverter().toStringConverter());
		latField.setText("00°00.00'N");
		ValidationUtils.install(latField, new SimpleValidator(new RegexValidator(PATTERN_DMM)), ValidationMode.ON_FLY);
	}
	
	public static FormattedTextField<Longitude> createLongitudeField() {
		FormattedTextField<Longitude> lonField = new FormattedTextField<>();
		initializeLongitudeField(lonField);
		return lonField;
	}

	public static void initializeLongitudeField(FormattedTextField<Longitude> lonField) {
		lonField.getPatternVerifiers().put("degres", new IntegerRangePatternVerifier(0, 180, true));
		lonField.getPatternVerifiers().put("decimalMinutes", new NumberRangePatternVerifier(0.0, 59.9999) {
			@Override
			public Boolean call(String text) {
		        if (text.length() > getMaxLength()) return false;
		        try {
		            double i = parse(text).doubleValue();
					
					String degres = lonField.getGroupText("degres");
					int dd = Integer.parseInt(degres);
					if(dd == 180) return i == 0;	
		            
					if (i >= getMin().doubleValue() && i <= getMax().doubleValue()) return true;
		        }
		        catch (NumberFormatException e) {
		            CommonUtils.ignoreException(e);
		        }
		        return false;
			}	
		});
		lonField.getPatternVerifiers().put("westeast", new StringValuesPatternVerifier<String>(new String[]{"W", "E"}){
		    @Override
		    public String toTargetValue(String fieldValue) {
		        return fieldValue;
		    }

		    @Override
		    public String fromTargetValue(String previousFieldValue, String targetValue) {
		        return targetValue;
		    }
		});
		lonField.setPattern("degres°decimalMinutes'westeast");
		lonField.setStringConverter(new LongitudeConverter().toStringConverter());
		lonField.setText("000°00.00'W");
		ValidationUtils.install(lonField, new SimpleValidator(new RegexValidator(PATTERN_DMM)), ValidationMode.ON_FLY);
	}
}
