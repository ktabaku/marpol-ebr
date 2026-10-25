package al.sybina.ebr.s63.preferences;

import java.io.File;

import com.dlsc.formsfx.model.structure.DataField;
import com.dlsc.formsfx.model.structure.Field;
import com.dlsc.formsfx.view.controls.SimpleControl;
import com.google.gson.internal.LinkedTreeMap;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.util.StringConverter;

/**
 * This class provides an implementation of a {@link Field} containing a
 * {@code string} value.
 *
 * @author Sacha Schmid
 * @author Rinesch Murugathas
 */
public class FileField extends DataField<ObjectProperty<File>, File, FileField> {

    protected FileField(ObjectProperty<File> valueProperty, ObjectProperty<File> persistentValueProperty) {
        super(valueProperty, persistentValueProperty);

        stringConverter = new AbstractStringConverter<File>() {
			@Override
			public File fromString(String string) {
				return new File(string);
			}
        };

        userInput.set(stringConverter.toString(value.getValue()));
    }

	public static Field<FileField> ofFileType(ObjectProperty<File> binding) {
		return new FileField(new SimpleObjectProperty<File>(binding.getValue()), new SimpleObjectProperty<File>(binding.getValue())).bind(binding);
	}

	public String toString() {
		if(value.get()==null) return "";
		return value.get().getAbsolutePath();
	}
	
    protected static abstract class AbstractStringConverter<V> extends StringConverter<V> {
        @Override
        public String toString(V object) {
        	if(object == null) return "";
        	if(object instanceof File)
        		return ((File)object).getAbsolutePath();
        	if(object instanceof LinkedTreeMap)
        		return (String) ((LinkedTreeMap)object).get("path");
            return String.valueOf(object);
        }
    }
}
