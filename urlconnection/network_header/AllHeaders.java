package network_header;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author qjj <br>
 *         获取全部首部字段
 */
public class AllHeaders {

	public static void main(String[] args) {
		String url = "http://www.oreilly.com/favicon.ico";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			int i = 0;
			while (true) {
				String header = uc.getHeaderField(i);
				if (header == null) {
					break;
				}
				System.out.println(uc.getHeaderFieldKey(i) + ": " + header);
				i++;
			}
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
