package network_write_server;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author qjj <br>
 *         提交表单数据
 */
public class FormPoster {

	private URL url;
	private QueryString query = new QueryString();

	public FormPoster(URL url) {
		if (!url.getProtocol().toLowerCase().startsWith("http")) {
			throw new IllegalArgumentException( //
					"Posting only works for http URLs");
		}
		this.url = url;
	}

	public void add(String name, String value) {
		query.add(name, value);
	}

	public URL getURL() {
		return this.url;
	}

	public InputStream post() throws IOException {
		URLConnection uc = url.openConnection();
		uc.setDoOutput(true); // 向服务器中写入数据之前，必须将doOutput设置为true，设置之后请求方法由GET变为POST
		try (OutputStreamWriter out = new OutputStreamWriter(uc.getOutputStream(), "UTF-8")) {
			// POST行，Content-type首部和Content-Length首部，由URLConnection发送，我们只需要发送数据
			out.write(query.toString());
			out.write("\r\n");
			out.flush();
		}
		return uc.getInputStream();
	}

	public static void main(String[] args) {
		String url = "http://www.cafeaulait.org/books/jnp4/postquery.phtml";
		URL u = null;
		try {
			u = new URL(url);
		} catch (MalformedURLException e) {
			System.out.print(url + "is not a parseable URL");
		}

		FormPoster poster = new FormPoster(u);
		poster.add("name", "qjjazry");
		poster.add("email", "xiaohaixie@qq.com");

		try (InputStream in = poster.post()) {
			Reader r = new InputStreamReader(in);
			int c;
			while ((c = r.read()) != -1) {
				System.out.print((char) c);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
