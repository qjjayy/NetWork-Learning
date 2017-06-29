package callback;

import java.util.Scanner;

import javax.xml.bind.DatatypeConverter;

public class InstanceCallbackDigestUserInterface {

	private String filename;
	private byte[] digest;

	public InstanceCallbackDigestUserInterface(String filename) {
		this.filename = filename;
	}

	public void calculateDigest() {
		InstanceCallbackDigest cb = new InstanceCallbackDigest(filename, this);
		Thread t = new Thread(cb);
		t.start();
	}

	public void receiveDigest(byte[] digest) {
		this.digest = digest;
		System.out.println(this);
	}

	@Override
	public String toString() {
		String result = filename + ": ";
		if (digest != null) {
			result += DatatypeConverter.printHexBinary(digest);
		} else {
			result += "digest not available";
		}
		return result;
	}

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String[] filenames = new String[3];
		Scanner scanner = new Scanner(System.in);
		int i = 0;
		while (scanner.hasNext()) {
			filenames[i++] = scanner.next();
		}

		for (String filename : filenames) {
			InstanceCallbackDigestUserInterface d = new InstanceCallbackDigestUserInterface(filename);
			d.calculateDigest();
		}
	}
}
