package ebook.module.tree;

import org.eclipse.jface.fieldassist.ControlDecoration;
import org.eclipse.jface.viewers.TextCellEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Tree;

import ebook.utils.Utils;

public class EbookTextCellEditor extends TextCellEditor {

	public EbookTextCellEditor(Tree tree) {
		super(tree);
	}

	@Override
	protected Control createControl(Composite parent) {
		Control ctrl = super.createControl(parent);

		final ControlDecoration deco = new ControlDecoration(ctrl, SWT.TOP
				| SWT.LEFT);

		deco.setDescriptionText("Use CNTL + SPACE to see possible values");
		deco.setImage(Utils.getImage("treeedit.png"));
		deco.setShowOnlyOnFocus(false);

		return ctrl;
	}

}
