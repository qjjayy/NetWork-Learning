package network_config;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

// URLConnection 有7个保护字段，必须在连接之前改变（
// 创建URLConnection时并未连接，connect()、getInputStream()、getOutputStream()时才会连接），
// 7个字段为 URL  doInput  doOutput  allowUserInteraction  useCaches  ifModifiedSince  connected

/**
 * @author qjj <br>
 *         请求中包含If-Modified-Since，表明服务器只有在这个时间点之后修改过文档，才会回传文档 <br>
 *         该程序验证是否24小时内，服务器修改过文档
 */
public class Last24 {

	public static void main(String[] args) {
		Date today = new Date();
		long millisecondsPerDay = 24 * 60 * 60 * 1000;

		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			System.out.println("Original if modified since: " + new Date(uc.getIfModifiedSince()));
			uc.setIfModifiedSince(new Date(today.getTime() - millisecondsPerDay).getTime());
			System.out.println("Will retrieve file if it's modified since" + new Date(uc.getIfModifiedSince()));
			try (InputStream in = new BufferedInputStream(uc.getInputStream())) {
				Reader r = new InputStreamReader(in);
				int c;
				while ((c = r.read()) != -1) {
					System.out.print((char) c);
				}
			}
		} catch (MalformedURLException e) {
			System.out.print(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
