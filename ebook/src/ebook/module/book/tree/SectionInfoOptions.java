package ebook.module.book.tree;

import ebook.core.models.DbOptions;

public class SectionInfoOptions extends DbOptions {

	private static final long serialVersionUID = -8134048308726133820L;

	public static final int scaledImageMaxWidth = 500;
	public static final int scaledImageMinWidth = 100;

	public Integer scaledImageWidth = 300;

	// public int columnCount = 1;

	public int getCompositeWidthHint() {
		return scaledImageWidth + 30;
	}

	// public static String save(SectionOptions data) {
	//
	// String value = "";
	// try {
	// ByteArrayOutputStream baos = new ByteArrayOutputStream();
	// ObjectOutputStream oos = new ObjectOutputStream(baos);
	// oos.writeObject(data);
	// oos.close();
	// BASE64Encoder encoder = new BASE64Encoder();
	// value = encoder.encodeBuffer(baos.toByteArray());
	// } catch (Exception e) {
	// value = "";
	// }
	// return value;
	// }
	//
	// public static SectionOptions load(String s) {
	//
	// if (s == null || s.isEmpty())
	// return new SectionOptions();
	//
	// SectionOptions obj;
	// ObjectInputStream ois = null;
	// try {
	// BASE64Decoder decoder = new BASE64Decoder();
	// byte[] data = decoder.decodeBuffer(s);
	// ois = new ObjectInputStream(new ByteArrayInputStream(data));
	//
	// obj = (SectionOptions) ois.readObject();
	//
	// } catch (Exception e) {
	// return new SectionOptions();
	// } finally {
	// try {
	// if (ois != null)
	// ois.close();
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// }
	// return obj;
	// }

}
