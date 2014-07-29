package ebook.module.confLoad.model;

public enum EType {

	Module, Attribute, TabularSection, Template, SKD;

	private static int index;

	private int value;

	private EType() {
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