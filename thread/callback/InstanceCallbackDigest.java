package callback;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 回调的方式
 * 
 * @author qjj
 *
 */
public class InstanceCallbackDigest implements Runnable {

	private String filename;
	private InstanceCallbackDigestUserInterface callback;

	public InstanceCallbackDigest(String filename, InstanceCallbackDigestUserInterface callback) {
		this.filename = filename;
		this.callback = callback;
	}

	@Override
	public void run() {
		FileInputStream in = null;
		DigestInputStream din = null;
		MessageDigest sha = null;
		try {
			in = new FileInputStream(filename);
			sha = MessageDigest.getInstance("SHA-256");
			din = new DigestInputStream(in, sha);
			while (din.read() != -1)
				; // 读取整个文件
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (din != null) {
				try {
					din.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			byte[] digest = sha.digest();
			callback.receiveDigest(digest);
		}

	}

}
