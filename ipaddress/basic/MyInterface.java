package basic;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

public class MyInterface {

	public static void main(String[] args) {
		try {
			// 列举所有网络接口
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = interfaces.nextElement();
				System.out.println(ni);
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}

		try {
			// 返回一个IP地址绑定的网络接口
			InetAddress local = InetAddress.getByName("127.0.0.1");
			NetworkInterface ni = NetworkInterface.getByInetAddress(local);
			System.out.println(ni);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (SocketException e) {
			e.printStackTrace();
		}

		try {
			// 返回指定名字的网络接口
			NetworkInterface ni = NetworkInterface.getByName("eth2");
			System.out.println(ni);
			System.out.println(ni.getName());
			System.out.println(ni.getDisplayName());
			// 一个网络接口可能绑定多个IP地址
			Enumeration<InetAddress> addresses = ni.getInetAddresses();
			while (addresses.hasMoreElements()) {
				System.out.println(addresses.nextElement());
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
}
