package al.sybina.ebr.s63;

import ca.gc.chs.ihossv1.Ihossv1PublicKey;

public class PublicKeyWithProperties implements Comparable<PublicKeyWithProperties> {
	
	private Ihossv1PublicKey theIhossv1PublicKey;
	
	private boolean isSAPublicKey;

	public PublicKeyWithProperties(Ihossv1PublicKey theIhossv1PublicKey, boolean isSAPublicKey) {
		this.theIhossv1PublicKey = theIhossv1PublicKey;
		this.isSAPublicKey = isSAPublicKey;
	}

	public Ihossv1PublicKey getTheIhossv1PublicKey() {
		return theIhossv1PublicKey;
	}

	public void setTheIhossv1PublicKey(Ihossv1PublicKey theIhossv1PublicKey) {
		this.theIhossv1PublicKey = theIhossv1PublicKey;
	}

	public boolean isSAPublicKey() {
		return isSAPublicKey;
	}

	public void setSAPublicKey(boolean isSAPublicKey) {
		this.isSAPublicKey = isSAPublicKey;
	}

	@Override
	public int compareTo(PublicKeyWithProperties o) {
	    return (this.isSAPublicKey() == o.isSAPublicKey() ? 0 : 
            (this.isSAPublicKey() ? 0 : 1));
	}
}
