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
 *         获取首部中的内容类型
 *
 */
public class EncodingAwareSourceViewer {

	public static void main(String[] args) {
		String encoding = "ISO-8859-1";
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			String contentType = uc.getContentType(); // 获取响应主体中的MIME内容类型
			int encodingStart = contentType.indexOf("charset=");
			if (encodingStart != -1) { // 如果是文本类型，获取对应的字符集部分，来标识文档的字符编码方式
				encoding = contentType.substring(encodingStart + 8);
			}
			try (InputStream in = uc.getInputStream()) {
				Reader r = new InputStreamReader(new BufferedInputStream(in), encoding);
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
