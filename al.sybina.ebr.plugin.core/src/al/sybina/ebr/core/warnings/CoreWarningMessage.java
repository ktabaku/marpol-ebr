package al.sybina.ebr.core.warnings;

import java.util.Calendar;

public class CoreWarningMessage {
	public enum ESeverity {
		message(1),
		caution(2),
		warning(4);
		public int binaryCode;

		ESeverity(int binaryCode){
			this.binaryCode = binaryCode;
		}
	};

	private String key;
	private Calendar time;	
	private ESeverity severity;
	private String text;
	private boolean ack;
	
	public CoreWarningMessage() {
		super();
	}
	
	public CoreWarningMessage(ESeverity type, String key, String msg) {
		this();
		setSeverity(type);
		this.key = key;
		text = msg;
	}
	public static CoreWarningMessage getMessageInstance(String key, String message) {
		return new CoreWarningMessage(ESeverity.message, key, message);
	}
	public static CoreWarningMessage getCautionInstance(String key, String message) {
		return new CoreWarningMessage(ESeverity.caution, key, message);
	}
	public static CoreWarningMessage getWarningInstance(String key, String message) {
		return new CoreWarningMessage(ESeverity.warning, key, message);
	}
	
	public String toString(){
		return text;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Calendar getTime() {
		return time;
	}

	public void setTime(Calendar time) {
		this.time = time;
	}

	public ESeverity getSeverity() {
		return severity;
	}

	public void setSeverity(ESeverity severity) {
		this.severity = severity;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void setAck(boolean v) {
		ack = v;
	}
	
	public boolean getAck(){
		return ack;
	}
	
	
}
