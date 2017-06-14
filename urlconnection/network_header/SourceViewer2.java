package network_header;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author qjj <br>
 *         读取服务器的数据 <br>
 *         URLConnection 提供对HTTP首部的访问 <br>
 *         URLConnection 可以配置发送给服务器的请求参数 <br>
 *         URLConnection 除了读取服务器数据外，还可以向服务器写入数据 <br>
 *
 */
public class SourceViewer2 {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			try (InputStream raw = uc.getInputStream()) {
				Reader reader = new InputStreamReader(new BufferedInputStream(raw));
				int c;
				while ((c = reader.read()) != -1) {
					System.out.println((char) c);
				}
			}
		} catch (MalformedURLException e) {
			System.out.print(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
