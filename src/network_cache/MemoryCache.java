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

	@Override
	public CacheResponse get(URI uri, String rqstMethod, Map<String, List<String>> rqstHeaders) throws IOException {
		return null;
	}

	@Override
	public CacheRequest put(URI uri, URLConnection conn) throws IOException {
		return null;
	}

}
