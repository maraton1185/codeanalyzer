package ebook.module.confLoad.model;

public enum ELevel {

	group1, group2, module, proc, other;

	private static int index;

	private int value;

	private ELevel() {
		set();
	}

	void set() {
		this.value = index;
		index++;
	}

	public int getInt() {
		return value;
	}
}