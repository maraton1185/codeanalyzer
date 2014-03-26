package codeanalyzer.book;

public class BookSection {

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BookSection)
			return ((BookSection) obj).id == id;
		else
			return super.equals(obj);
	}

	public BookSection() {

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

	public BookSection(Integer id) {
		this();
		this.id = id;
	}

	public String title;

	public Integer id;

	public Integer order;

	public Integer parent;
	// public boolean root;

}
