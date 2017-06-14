package network_cache;

import java.net.ResponseCache;

/**
 * @author qjj <br>
 *         Java 要求一次只能有一个URL缓存
 */
public class Main {

	public static void main(String[] args) {
		System.out.println(ResponseCache.getDefault()); // 获取系统的默认缓存
		ResponseCache.setDefault(new MemoryCache()); // 将自定义的缓存设置为默认缓存
	}
}
