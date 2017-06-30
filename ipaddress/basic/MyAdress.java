package basic;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author qjj<br>
 *         InetAddress类的大部分方法:<br>
 *         getByName、getAllByName、getByAddress、getLocalHost、getHostName、<br>
 *         getCanonicalHostName、getHostAddress、getAddress、isReachable
 */
public class MyAdress {

	public static void main(String[] args) {
		try {
			// 这个方法会建立连接查找DNS服务器，如果DNS服务器找不到这个地址，会抛出异常
			// java.net包之外无法在后台改变InetAddress对象的字段，这使得InetAddress不可变，因此是线程安全的
			InetAddress address1 = InetAddress.getByName("www.baidu.com");
			System.out.println(address1);
		} catch (UnknownHostException e) {
			System.out.println("Could not find www.baidu.com");
		}

		try {
			// 查找的地址没有相应的主机名，getHostName就返回提供的点份四段地址
			// 使用IP地址字符串创建一个InetAddress对象时，不会检查DNS，这个对象的主机名初始设置为这个IP地址字符串，这说明，可能会为实际上不存在也无法连接的主机创建InetAddress对象
			// 只有当请求主机名时（即显示调用getHostName），才会真正完成主机名的DNS查找，但此时查找不到对应的主机，不会抛出异常，只会显示IP地址字符串
			// 主机名比IP地址稳定得多，所以尽量使用主机名
			InetAddress address2 = InetAddress.getByName("202.108.22.5");
			// getCanonicalHostName比getHostName更积极一些，getHostName只是在不知道主机名时才会联系DNS，
			// getCanonicalHostName知道主机名也会联系DNS，可能会替换原来缓存的主机名
			System.out.println(address2.getHostName());
			System.out.println(address2.getCanonicalHostName());
		} catch (UnknownHostException e) {
			System.out.println("Could not find 202.108.22.5");
		}

		try {
			// 一个主机名可能对应多个IP地址
			InetAddress[] addresses = InetAddress.getAllByName("www.baidu.com");
			for (InetAddress address : addresses) {
				System.out.println(address);
			}
		} catch (UnknownHostException e) {
			System.out.println("Could not find www.baidu.com");
		}

		try {
			// 这个方法返回主机的InetAddress，如果失败就返回回送地址，即localhost/127.0.0.1
			InetAddress address3 = InetAddress.getLocalHost();
			System.out.println(address3);
			System.out.println(address3.getHostAddress());
			// 测试getAddress返回数组的字节数，可以确定处理的是IPV4还是IPV6
			byte[] addressTmp = address3.getAddress();
			for (byte ad : addressTmp) {
				System.out.print((ad < 0 ? ad + 256 : ad) + " ");
			}
			System.out.println("");
		} catch (UnknownHostException e) {
			System.out.println("Could not find this computer's address");
		}

		byte[] address4 = { 107, 23, (byte) 216, (byte) 196 };
		try {
			// 以下两个方法不能保证这个主机一定存在，或者保证主机名能正确地映射到IP地址，只有当作为address参数与传入的字节数组大小不合法时，才会抛出异常
			// 如果域名服务器不可用，或者可能有不正确的信息，这会很有用
			InetAddress lessWrong = InetAddress.getByAddress(address4);
			System.out.println(lessWrong);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		try {
			InetAddress lessWrongWithname = InetAddress.getByAddress("lesswrong.com", address4);
			System.out.println(lessWrongWithname);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		// SecurityManager sm = System.getSecurityManager();
		// sm.checkConnect("180.160.27.177", -1);

		try {
			InetAddress address5 = InetAddress.getByName("180.160.27.177");
			// 测试可达性
			System.out.println(address5.isReachable(3000));
		} catch (UnknownHostException e) {
			System.out.println("Could not find 180.160.27.177");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
