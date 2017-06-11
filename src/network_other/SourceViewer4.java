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
 *         输出服务器返回的详细的错误信息
 */
public class SourceViewer4 {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			HttpURLConnection http = (HttpURLConnection) u.openConnection();
			try (InputStream raw = http.getInputStream()) {
				printFromStream(raw);
			} catch (IOException ex) {
				printFromStream(http.getErrorStream());
			}
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void printFromStream(InputStream raw) throws IOException {
		try (InputStream buffer = new BufferedInputStream(raw)) {
			Reader r = new InputStreamReader(buffer);
			int c;
			while ((c = r.read()) != -1) {
				System.out.print((char) c);
			}
		}
	}
}
