package network_config;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author qjj <br>
 *         配置请求首部
 */
public class RequestConfig {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			URLConnection uc = u.openConnection();
			uc.setRequestProperty("Cookie", "username=qjj; password=asdsdfrg; session=100678945");
			Map<String, List<String>> map = uc.getRequestProperties();
			Iterator<Entry<String, List<String>>> iterator = map.entrySet().iterator();
			while (iterator.hasNext()) {
				Entry<String, List<String>> entry = iterator.next();
				System.out.println("a" + entry.getKey() + ": " + entry.getValue());
			}
			System.out.println();
			uc.connect();
		} catch (MalformedURLException e) {
			System.out.print(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
