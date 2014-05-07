package codeanalyzer.books.section;

public class SectionInfo {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof SectionInfo)
			return ((SectionInfo) obj).id.equals(id);
		else
			return super.equals(obj);
	}

	public SectionInfo() {

		// for (Field f : this.getClass().getFields()) {
		// try {
		// if (f.getType().isAssignableFrom(String.class))
		// f.set(this, "");
		// else if (f.getType().isAssignableFrom(Integer.class))
		// f.set(this, -1);
		// } catch (Exception e) {
		// }
		// }
	}

	public SectionInfo(Integer id) {
		this();
		this.id = id;
	}

	public String title;

	public Integer id;

	public Integer order;

	public Integer parent;

	public boolean block;

	public SectionOptions options;

}
