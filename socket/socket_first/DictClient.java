package socket_first;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author qjj <br>
 *         一个给予网络的英语－拉丁语翻译程序
 */
public class DictClient {

	public static final String SERVER = "dict.org";
	public static final int PORT = 2628;
	public static final int TIMEOUT = 15000;

	public static void main(String[] args) throws UnknownHostException, IOException {
		String word = "silver";
		try (Socket socket = new Socket(SERVER, PORT)) {
			socket.setSoTimeout(TIMEOUT);
			Writer writer = new BufferedWriter( //
					new OutputStreamWriter( //
							socket.getOutputStream(), "UTF-8"));
			BufferedReader reader = new BufferedReader( //
					new InputStreamReader( //
							socket.getInputStream(), "UTF-8"));
			define(word, writer, reader);

			writer.write("quit\r\n");
			writer.flush();
		}
	}

	private static void define(String word, Writer writer, BufferedReader reader) throws IOException {
		writer.write("DEFINE fd-lat-eng " + word + "\r\n");
		writer.flush();

		for (String line = reader.readLine(); line != null; line = reader.readLine()) {
			if (line.startsWith("250")) { // OK
				return;
			} else if (line.startsWith("552")) { // no match
				System.out.println("No defination found for " + word);
				return;
			} else if (line.matches("\\d\\d\\d .*")) {
				continue;
			} else if (line.trim().equals(".")) {
				continue;
			} else {
				System.out.println(line);
			}
		}
	}

}
