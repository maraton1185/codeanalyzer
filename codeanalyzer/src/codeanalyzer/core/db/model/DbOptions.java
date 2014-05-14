package codeanalyzer.core.db.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class DbOptions implements Serializable {

	private static final long serialVersionUID = -8134048308726133820L;

	// public String path;

	// public int columnCount = 1;

	public static String save(DbOptions data) {

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

	public static DbOptions load(String s) {

		if (s == null || s.isEmpty())
			return new DbOptions();

		DbOptions obj;
		ObjectInputStream ois = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] data = decoder.decodeBuffer(s);
			ois = new ObjectInputStream(new ByteArrayInputStream(data));

			obj = (DbOptions) ois.readObject();

		} catch (Exception e) {
			return new DbOptions();
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
