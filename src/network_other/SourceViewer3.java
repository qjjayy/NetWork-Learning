package network_other;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author qjj <br>
 *         处理服务器响应
 */
public class SourceViewer3 {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			HttpURLConnection http = (HttpURLConnection) u.openConnection();

			int code = http.getResponseCode();
			String response = http.getResponseMessage();
			System.out.println("HTTP/1.x" + code + " " + response);
			for (int i = 1;; i++) {
				String header = http.getHeaderField(i);
				String key = http.getHeaderFieldKey(i);
				if (header != null && key != null) {
					System.out.println(key + ": " + header);
				} else {
					break;
				}
			}
			System.out.println();

			try (InputStream in = new BufferedInputStream(http.getInputStream())) {
				Reader r = new InputStreamReader(in);
				int c;
				while ((c = r.read()) != -1) {
					System.out.print((char) c);
				}
			}
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
