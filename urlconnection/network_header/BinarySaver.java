package network_header;

import java.io.BufferedInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author qjj <br>
 *         获取首部中的内容长度类型
 */
public class BinarySaver {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			String contentType = uc.getContentType();
			int contentLength = uc.getContentLength(); // 获取首部中的内容长度字段，可以起到提前预知响应内容的大小
			if (contentType.startsWith("text/") || contentLength == -1) {
				throw new IOException("This is not a binary file");
			}
			try (InputStream raw = uc.getInputStream()) {
				InputStream in = new BufferedInputStream(raw);
				byte[] data = new byte[contentLength];
				int offset = 0;
				while (offset < contentLength) {
					int bytesRead = in.read(data, offset, contentLength - offset);
					if (bytesRead == -1) {
						break;
					}
					offset += bytesRead;
				}
				if (offset != contentLength) {
					throw new IOException("Only read" + offset + "bytes; Excepted" + contentLength + "bytes");
				}
				String filename = u.getFile();
				filename = filename.substring(filename.lastIndexOf('/') + 1); // 获取文件名，创建同名文件，并且往里写入数据
				try (FileOutputStream fout = new FileOutputStream(filename)) {
					fout.write(data);
					fout.flush();
				}
			}
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
