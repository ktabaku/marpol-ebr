package al.sybina.ebr.commons.utils;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.util.Locale;

import org.eclipse.fx.core.log.Logger;
import org.eclipse.fx.core.log.LoggerCreator;

import javafx.util.StringConverter;
import jidefx.scene.control.field.DoubleField;
import jidefx.scene.control.field.verifier.NumberValuePatternVerifier;
import jidefx.utils.CommonUtils;

public class MathUtils {

	private static final Logger logger = LoggerCreator.createLogger(MathUtils.class);
	public static final DecimalFormat decimalFormat = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));
	public static final double DOUBLE_THRESHOLD = 0.00000001;
	
	public static void setStringConverter(DoubleField doubleField) {
		doubleField.setStringConverter(new StringConverter<Double>() {
            @Override
            public String toString(Double o) {
                if (o == null) return "";
                return decimalFormat.format(o);
            }

            @Override
            public Double fromString(String s) {
                if (s == null || s.trim().isEmpty()) return null;
                try {
					return decimalFormat.parse(s).doubleValue();
				} catch (ParseException e) {
					logger.error(e.getMessage(), e);
					return Double.NaN;
				}
            }
        });
	}
	public static void setMinMaxValues(DoubleField doubleField, Double min, Double max) {
		if((min!=null && !min.isNaN()) || (max!=null && !max.isNaN())) {
			if(min==null || min.isNaN()) min = Double.NEGATIVE_INFINITY;
			if(max==null || min.isNaN()) max = Double.POSITIVE_INFINITY;
			doubleField.getPatternVerifiers().put("double", new NumberValuePatternVerifier<Double>(min, max) { //NON-NLS
	            @Override
	            public Double toTargetValue(Double fieldValue) {
	                return fieldValue;
	            }
	
	            @Override
	            public Double fromTargetValue(Double previousFieldValue, Number value) {
	                return value.doubleValue();
	            }
	            
	            @Override
	            public int getMaxLength() {
	                return 20;
	            }
	        });
			doubleField.setPattern("double"); //NON-NLS
		}
	}
	
	public static double findClosest(double[] arr, double target) {
		int n = arr.length;

		// Corner cases
		if (target <= arr[0])
			return arr[0];
		if (target >= arr[n - 1])
			return arr[n - 1];

		// Doing binary search
		int i = 0, j = n, mid = 0;
		while (i < j) {
			mid = (i + j) / 2;

			// if arr[mid] and target are very close
			if (Math.abs(arr[mid] - target) < 0.0001)
				return arr[mid];

			/*
			 * If target is less than array element, then search in left
			 */
			if (target < arr[mid]) {

				// If target is greater than previous
				// to mid, return closest of two
				if (mid > 0 && target > arr[mid - 1])
					return getClosest(arr[mid - 1], arr[mid], target);

				/* Repeat for left half */
				j = mid;
			}

			// If target is greater than mid
			else {
				if (mid < n - 1 && target < arr[mid + 1])
					return getClosest(arr[mid], arr[mid + 1], target);
				i = mid + 1; // update i
			}
		}

		// Only single element left after search
		return arr[mid];
	}
	

    // Method to compare which one is the more close
    // We find the closest by taking the difference
    //  between the target and both values. It assumes
    // that val2 is greater than val1 and target lies
    // between these two.
    public static double getClosest(double val1, double val2,
                                 double target)
    {
        if (target - val1 >= val2 - target)
            return val2;
        else
            return val1;
    }

}
