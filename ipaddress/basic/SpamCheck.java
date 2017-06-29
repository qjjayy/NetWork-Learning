package basic;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * 监视垃圾邮件发送者
 * 
 * @author qjj
 *
 */
public class SpamCheck {

	public static final String BLACKHOLE = "sbl.spamhaus.org";

	@SuppressWarnings("resource")
	public static void ma10in(String[] args) {
		String[] addresses = new String[3];
		Scanner scanner = new Scanner(System.in);
		int i = 0;
		while (scanner.hasNext()) {
			addresses[i++] = scanner.next();
		}
		for (String address : addresses) {
			if (isSpammer(address)) {
				System.out.println(address + "is a known spammer.");
			} else {
				System.out.println(address + "appears legitimate.");
			}
		}
	}

	private static boolean isSpammer(String arg) {
		try {
			InetAddress address = InetAddress.getByName(arg);
			byte[] quad = address.getAddress();
			String query = BLACKHOLE;
			for (byte octet : quad) {
				int unsignedByte = octet < 0 ? octet + 256 : octet;
				query = unsignedByte + "." + query;
			}
			InetAddress.getByName(query);
			return true;
		} catch (UnknownHostException e) {
			return false;
		}
	}
}
