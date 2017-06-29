package httpserver;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.file.Files;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RequestProcessor implements Runnable {

	private static final Logger logger = Logger.getLogger(JHTTP.class.getCanonicalName());
	private File rootDirectory;
	private String indexFileName = "index.html";
	private Socket connection;

	public RequestProcessor(File rootDirectory, String indexFileName, Socket connection) {
		if (rootDirectory.isFile()) {
			throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
		}

		try {
			rootDirectory = rootDirectory.getCanonicalFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.rootDirectory = rootDirectory;
		if (indexFileName != null) {
			this.indexFileName = indexFileName;
		}
		this.connection = connection;
	}

	@Override
	public void run() {
		String root = rootDirectory.getPath();
		try {
			OutputStream raw = new BufferedOutputStream(connection.getOutputStream());
			Writer out = new OutputStreamWriter(raw);
			Reader in = new InputStreamReader( //
					new BufferedInputStream(connection.getInputStream()), "US-ASCII");
			StringBuilder request = new StringBuilder(80);
			while (true) {
				int c = in.read();
				if (c == '\r' || c == '\n' || c == -1)
					break;
				request.append((char) c);
			}
			String get = request.toString();
			logger.info(connection.getRemoteSocketAddress() + " " + get);

			String[] tokens = get.split("\\s+");
			String method = tokens[0];
			String version = "";

			if (method.equals("GET")) {
				String fileName = tokens[1];
				if (fileName.endsWith("/")) {
					fileName += indexFileName;
				}
				String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
				if (tokens.length > 2) {
					version = tokens[2];
				}
				File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));
				// 不要让客户端超出文档之外
				if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
					byte[] theData = Files.readAllBytes(theFile.toPath());
					if (version.startsWith("HTTP/")) { // 发送一个MIME首部
						sendHeader(out, "HTTP/1.0 200 OK", contentType, theData.length);
					}
					raw.write(theData);
					raw.flush();
				} else { // 无法找到文件
					String body = new StringBuilder("<HTML>\r\n") //
							.append("<HEAD><TITLE>File Not Found</TITLE>\r\n") //
							.append("</HEAD>\r\n") //
							.append("BODY") //
							.append("<H1>HTTP Error 404: File Not Found</H1>\r\n") //
							.append("</BODY></HTML>\r\n").toString();
					if (version.startsWith("HTTP/")) {
						sendHeader(out, "HTTP/1.0 404 File Not Found", "text/html; charset=utf-8", body.length());
					}
					out.write(body);
					out.flush();
				}
			} else { // 方法不等于"GET"
				String body = new StringBuilder("<HTML>\r\n") //
						.append("<HEAD><TITLE>Not Implemented</TITLE>\r\n") //
						.append("</HEAD>\r\n") //
						.append("BODY") //
						.append("<H1>HTTP Error 501: Not Implemented</H1>\r\n") //
						.append("</BODY></HTML>\r\n").toString();
				if (version.startsWith("HTTP/")) {
					sendHeader(out, "HTTP/1.0 501 Not Implemented", "text/html; charset=utf-8", body.length());
				}
				out.write(body);
				out.flush();
			}
		} catch (IOException e) {
			logger.log(Level.WARNING, "Error talking to " + connection.getRemoteSocketAddress(), e);
		} finally {
			try {
				connection.close();
			} catch (IOException e) {

			}
		}
	}

	private void sendHeader(Writer out, String responseCode, String contentType, int length) //
			throws IOException {
		out.write(responseCode + "\r\n");
		Date now = new Date();
		out.write("Date: " + now + "\r\n");
		out.write("Server: JHTTP 2.0\r\n");
		out.write("Content-length: " + length + "\r\n");
		out.write("Content-type: " + contentType + "\r\n\r\n");
		out.flush();
	}
}
