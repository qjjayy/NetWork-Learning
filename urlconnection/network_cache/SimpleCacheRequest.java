package network_cache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.CacheRequest;

/**
 * @author qjj <br>
 *         定制的CacheRequest子类，协议处理器在处理ResponseCache类中的put()缓存时会自动调用 <br>
 *         CacheRequest包装了一个OutputStream，URL把读取的可缓存的数据写入这个输出流中 <br>
 *         OutputStream 相当于缓存的“数据库”，这个“数据库”与同时传入put()方法的URI相对应 <br>
 *         例如，如果数据存储在一个文件中，就要返回连接到该文件的FileOutputStream，<br>
 *         协议处理器会把读取的数据复制到这个OutputStream <br>
 *         如果复制时出现问题，协议处理器就会调用abort()方法，这个方法应当从缓存中删除为这个请求存储的所有数据 <br>
 */
public class SimpleCacheRequest extends CacheRequest {

	private ByteArrayOutputStream out = new ByteArrayOutputStream();

	@Override
	public OutputStream getBody() throws IOException {
		return out;
	}

	@Override
	public void abort() {
		out.reset();
	}

	public byte[] getData() {
		if (out.size() == 0) {
			return null;
		} else {
			return out.toByteArray();
		}
	}
}
