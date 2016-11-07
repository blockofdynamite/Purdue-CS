import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.Collection;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class WrtingObjects {
	public static <T> void save(Collection<T> in, OutputStream out) throws IOException {
		GZIPOutputStream gzip = new GZIPOutputStream(out);
		ObjectOutputStream data = new ObjectOutputStream(gzip);
		data.writeInt(in.size());
		for (Object o : in) {
			data.writeObject(o);
		}
		data.flush();
		gzip.finish();
	}

	@SuppressWarnings("unchecked")
	public static <T> void load(Collection<T> out, InputStream in) throws IOException, ClassNotFoundException {
		GZIPInputStream gzip = new GZIPInputStream(in);
		ObjectInputStream data = new ObjectInputStream(gzip);
		int len = data.readInt();
		for (int i = 0; i < len; i++) {
			out.add((T)data.readObject());
		}
	}
}
