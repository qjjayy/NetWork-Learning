package socket_first;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author qjj <br>
 *         通过Socket获取time.nist.gov服务器的数据
 *
 */
public class DayTime {

	public Date getDateFromNetwork() throws UnknownHostException, IOException, ParseException {
		try (Socket socket = new Socket("time.nist.gov", 13)) {
			socket.setSoTimeout(15000);
			StringBuilder time = new StringBuilder();
			Reader r = new InputStreamReader(socket.getInputStream());
			for (int c = r.read(); c != -1; c = r.read()) {
				time.append((char) c);
			}
			return parseDate(time.toString());
		}
	}

	private Date parseDate(String string) throws ParseException {
		String[] pieces = string.split(" ");
		String dateTime = pieces[1] + " " + pieces[2] + " UTC";
		SimpleDateFormat format = new SimpleDateFormat("yy-MM-dd hh:mm:ss z");
		return format.parse(dateTime);
	}
}
