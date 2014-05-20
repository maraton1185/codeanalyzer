package codeanalyzer.module.books.section;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

@SuppressWarnings("restriction")
public class SectionOptions implements Serializable {

	private static final long serialVersionUID = -8134048308726133820L;

	public static final int scaledImageMaxWidth = 500;
	public static final int scaledImageMinWidth = 100;

	public Integer scaledImageWidth = 300;

	public int columnCount = 1;

	public int getCompositeWidthHint() {
		return scaledImageWidth + 30;
	}

	public static String save(SectionOptions data) {

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

	public static SectionOptions load(String s) {

		if (s == null || s.isEmpty())
			return new SectionOptions();

		SectionOptions obj;
		ObjectInputStream ois = null;
		try {
			BASE64Decoder decoder = new BASE64Decoder();
			byte[] data = decoder.decodeBuffer(s);
			ois = new ObjectInputStream(new ByteArrayInputStream(data));

			obj = (SectionOptions) ois.readObject();

		} catch (Exception e) {
			return new SectionOptions();
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
