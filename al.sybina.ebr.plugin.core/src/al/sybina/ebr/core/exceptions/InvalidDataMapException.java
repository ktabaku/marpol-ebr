package al.sybina.ebr.core.exceptions;

public class InvalidDataMapException extends Exception {
	private static final long serialVersionUID = 5395947659701734898L;
	public InvalidDataMapException(String fileName){
		super(fileName + " is not a valid data map file");
	}

}
