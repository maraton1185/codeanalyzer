package ebook.dialogs;

import org.eclipse.jface.preference.FileFieldEditor;
import org.eclipse.swt.widgets.Composite;

public class MyFileFieldEditor extends FileFieldEditor {

	public MyFileFieldEditor(String name, String labelText, Composite parent) {
		super(name, labelText, false, parent);
	}

	@Override
	protected boolean checkState() {
		return true;
	}

}
