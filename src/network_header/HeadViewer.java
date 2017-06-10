package network_header;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

/**
 * @author qjj <br>
 *         返回首部
 */
public class HeadViewer {

	public static void main(String[] args) {
		String url = "http://www.oreilly.com/favicon.ico";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			System.out.println("Content-type: " + uc.getContentType());
			if (uc.getContentEncoding() != null) {
				System.out.println("Content-encoding: " + uc.getContentEncoding());
			}
			if (uc.getDate() != 0) {
				System.out.println("Date: " + new Date(uc.getDate()));
			}
			if (uc.getLastModified() != 0) {
				System.out.println("Last modified: " + new Date(uc.getLastModified()));
			}
			if (uc.getExpiration() != 0) {
				System.out.println("Content-encoding: " + new Date(uc.getExpiration()));
			}
			if (uc.getContentLength() != -1) {
				System.out.println("Content-length: " + uc.getContentLength());
			}
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
