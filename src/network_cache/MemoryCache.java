package network_cache;

import java.io.IOException;
import java.net.CacheRequest;
import java.net.CacheResponse;
import java.net.ResponseCache;
import java.net.URI;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MemoryCache extends ResponseCache {

	private final Map<URI, SimpleCacheResponse> responses = //
			new ConcurrentHashMap<URI, SimpleCacheResponse>();
	private final int maxEntries;

	public MemoryCache() {
		this(100);
	}

	public MemoryCache(int maxEntries) {
		this.maxEntries = maxEntries;
	}

	// 从Map中得到URI对应的CacheResponse响应,协议处理器会自动调用CacheResponse的getBody方法
	// 从返回的InputStream中读取数据
	@Override
	public CacheResponse get(URI uri, String rqstMethod, Map<String, List<String>> rqstHeaders) throws IOException {
		if ("GET".equals(rqstMethod)) {
			SimpleCacheResponse response = responses.get(uri);
			// 检查过期时间
			if (response != null && response.isExpired()) {
				responses.remove(uri);
				response = null;
			}
			return response;
		} else {
			return null;
		}
	}

	// 创建自定义的CacheRequest请求，协议处理器会自动调用CacheRequest的getBody方法，
	// 并将读取的数据复制到getBody方法返回的OutputStream
	// 创建自定义的CacheResponse响应，该对象将绑定对应的request和control对象
	// 最后将创建好的CacheResponse响应，存到Map中
	@Override
	public CacheRequest put(URI uri, URLConnection conn) throws IOException {
		if (responses.size() >= maxEntries) {
			return null;
		}
		CacheControl control = new CacheControl(conn.getHeaderField("Cache-Control"));
		if (control.isNoStore()) {
			return null;
		} else if (!conn.getHeaderField(0).startsWith("GET")) {
			return null; // 只缓存GET
		}

		SimpleCacheRequest request = new SimpleCacheRequest();
		SimpleCacheResponse response = new SimpleCacheResponse(request, conn, control);

		responses.put(uri, response);
		return request;
	}

}
