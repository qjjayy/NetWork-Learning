package network_other;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class LastModified {

	public static void main(String[] args) {
		String url = "http://www.baidu.com";
		try {
			URL u = new URL(url);
			HttpURLConnection http = (HttpURLConnection) u.openConnection();
			http.setRequestMethod("HEAD");
			System.out.println(url + " was last modified at " + new Date(http.getLastModified()));
		} catch (MalformedURLException e) {
			System.out.println(url + "is not a parseable URL");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
