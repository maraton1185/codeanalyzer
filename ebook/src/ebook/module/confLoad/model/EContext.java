package ebook.module.confLoad.model;

public enum EContext {

	Module, Config, Form, CommonModule, CommonForm, ManagerModule, GlobalCommonModule, Command;

	private static int index;

	private int value;

	private EContext() {
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
