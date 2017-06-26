package sslsocket;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;

public class SecureOrderTaker {

	public final static int PORT = 7000;
	public final static String algorithm = "SSL";

	public static void main(String[] args) {
		try {
			SSLContext context = SSLContext.getInstance(algorithm);

			// 参考实现只支持X.509密钥（为要使用的密钥类型创建一个KeyManagerFactory）
			KeyManagerFactory kmf = KeyManagerFactory.getInstance("SunX509");

			// Oracle的默认密钥库类型（为密钥和证书数据库创建一个KeyStore对象）
			KeyStore ks = KeyStore.getInstance("JKS");

			// 处于安全考虑，每个密钥库都必须用口令短语加密，在从磁盘加载前必须提供这个口令。
			// 口令短语已char[]数组形式存储，所以可以很快地从内存中擦除，而不是等待垃圾回收
			char[] password = System.console().readPassword();
			ks.load(new FileInputStream("jpn4e.keys"), password);
			kmf.init(ks, password);
			context.init(kmf.getKeyManagers(), null, null);

			Arrays.fill(password, '0');

			SSLServerSocketFactory factory = context.getServerSocketFactory();
			SSLServerSocket server = (SSLServerSocket) factory.createServerSocket(PORT);

			// 增加匿名（未认证）密码组
			String[] supported = server.getSupportedCipherSuites();
			String[] anonCipherSuitesSupported = new String[supported.length];
			int numAnonChiperSuitesSupported = 0;
			for (int i = 0; i < supported.length; i++) {
				if (supported[i].indexOf("_anon_") > 0) {
					anonCipherSuitesSupported[numAnonChiperSuitesSupported++] = supported[i];
				}
			}

			String[] oldEnabled = server.getEnabledCipherSuites();
			String[] newEnabled = new String[oldEnabled.length + numAnonChiperSuitesSupported];
			System.arraycopy(oldEnabled, 0, newEnabled, 0, oldEnabled.length);
			System.arraycopy(anonCipherSuitesSupported, 0, newEnabled, oldEnabled.length, //
					numAnonChiperSuitesSupported);
			server.setEnabledCipherSuites(newEnabled);

			// 现在所有工作已经完成，可以集中进行实际通信了
			while (true) {
				try (Socket connection = server.accept()) {
					InputStream in = new BufferedInputStream(connection.getInputStream());
					int c;
					while ((c = in.read()) != -1) {
						System.out.write(c);
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyStoreException e) {
			e.printStackTrace();
		} catch (CertificateException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (UnrecoverableKeyException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		} catch (RuntimeException e) {
			e.printStackTrace();
		}

	}

}
