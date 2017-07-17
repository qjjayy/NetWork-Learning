package basic;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.URL;

public class SecureSourceViewer {
	public static void main(String[] args) {
		Authenticator.setDefault(new DialogAuthenticator());

		try {
			URL u = new URL("http://www.baidu.com");
			try (InputStream in = new BufferedInputStream(u.openStream())) {
				Reader r = new InputStreamReader(in);
				int c;
				while ((c = r.read()) != -1) {
					System.out.println((char) c);
				}
			}
		} catch (MalformedURLException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		}
		System.out.println();
		System.exit(0);
	}
}
