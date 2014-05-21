package codeanalyzer.module.db;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public abstract class DbOptions implements Serializable {

	private static final long serialVersionUID = -8134048308726133820L;

	public static String save(Serializable data) {

		String value = "";
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);
			oos.writeObject(data);
			oos.close();
			BASE64Encoder encoder = new BASE64Encoder();
			value = encoder.encodeBuffer(baos.toByteArray());
		} catch (Exception e) {
			value = "";
		}
		return value;
	}

	@SuppressWarnings("unchecked")
	public static <T> T load(Class<T> c, String s) {

		T obj;
		ObjectInputStream ois = null;
		try {

			if (s == null || s.isEmpty())
				return c.newInstance();

			BASE64Decoder decoder = new BASE64Decoder();
			byte[] data = decoder.decodeBuffer(s);
			ois = new ObjectInputStream(new ByteArrayInputStream(data));

			obj = (T) ois.readObject();

			if (obj == null)
				return c.newInstance();

		} catch (Exception e) {
			try {
				return c.newInstance();
			} catch (Exception e1) {
				return null;
			}
		} finally {
			try {
				if (ois != null)
					ois.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return obj;
	}

}
