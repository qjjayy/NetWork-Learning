package network_cache;

import java.util.Date;
import java.util.Locale;

/**
 * @author qjj <br>
 *         TODO
 */
public class CacheControl {

	private Date maxAge = null;
	private Date sMaxAge = null;
	private boolean mustRevalidate = false;
	private boolean noCache = false;
	private boolean noStore = false;
	private boolean proxyRevalidate = false;
	private boolean publicCache = false;
	private boolean privateCache = false;

	public CacheControl(String s) {
		if (s == null || !s.contains(":")) {
			return; // 默认策略
		}
		String value = s.split(":")[1].trim();
		String[] components = value.split(",");
		Date now = new Date();
		for (String component : components) {
			try {
				component = component.trim().toLowerCase(Locale.US);
				if (component.startsWith("max-age=")) { // 获取过期时间点
					int secondsInTheFuture = Integer.parseInt(component.substring(8));
					maxAge = new Date(now.getTime() + 1000 * secondsInTheFuture);
				} else if (component.startsWith("s-maxage=")) {
					int secondsInTheFuture = Integer.parseInt(component.substring(8));
					sMaxAge = new Date(now.getTime() + 1000 * secondsInTheFuture);
				} else if (component.startsWith("must-revalidate")) {
					mustRevalidate = true;
				} else if (component.startsWith("proxy-revalidate")) {
					proxyRevalidate = true;
				} else if (component.startsWith("no-cache")) {
					noCache = true;
				} else if (component.startsWith("no-store")) {
					noStore = true;
				} else if (component.startsWith("public")) {
					publicCache = true;
				} else if (component.startsWith("private")) {
					privateCache = true;
				}
			} catch (RuntimeException e) {
				continue;
			}
		}
	}

	public Date getMaxAge() {
		return maxAge;
	}

	public Date getsMaxAge() {
		return sMaxAge;
	}

	public boolean isMustRevalidate() {
		return mustRevalidate;
	}

	public boolean isNoCache() {
		return noCache;
	}

	public boolean isNoStore() {
		return noStore;
	}

	public boolean isProxyRevalidate() {
		return proxyRevalidate;
	}

	public boolean isPublicCache() {
		return publicCache;
	}

	public boolean isPrivateCache() {
		return privateCache;
	}

}
