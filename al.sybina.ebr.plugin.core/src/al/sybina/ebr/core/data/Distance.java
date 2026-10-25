package al.sybina.ebr.core.data;

public class Distance extends Data {
	private static final long serialVersionUID = 7490649128295090380L;

	public static Distance ZERO = new Distance(0);
	
	public Distance(double v) {
		super(v, "NM");
	}

	
}
