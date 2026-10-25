package al.sybina.ebr.commons.utils;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.regex.Pattern;

public class NetworkUtils {

	public static String getSiteLocalIPv4() {
		Enumeration<NetworkInterface> e;
		try {
			e = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e1) {
			return null;
		}
		String ipToReturn = null;
		while (e.hasMoreElements()) {
			NetworkInterface n = (NetworkInterface) e.nextElement();
			Enumeration<InetAddress> ee = n.getInetAddresses();
			while (ee.hasMoreElements()) {
				InetAddress i = (InetAddress) ee.nextElement();
				String currentAddress = i.getHostAddress();

				if (i.isSiteLocalAddress() && !i.isLoopbackAddress() && validate(currentAddress)) {
					ipToReturn = currentAddress;
				}
			}
		}

		return ipToReturn;
	}

	private static final Pattern IPv4RegexPattern = Pattern
			.compile("^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");

	public static boolean validate(final String ip) {
		return IPv4RegexPattern.matcher(ip).matches();
	}
	
	
	public static boolean isReachable(String addr, int openPort, int timeOutMillis) {
	    // Any Open port on other machine
	    // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
	    try {
	        try (Socket soc = new Socket()) {
	            soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
	            soc.close();
	        }
	        return true;
	    } catch (IOException ex) {
	        return false;
	    }
	}
}
