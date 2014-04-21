package codeanalyzer.views.books;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.tools.TextEditor;

public class PageView {

	FormToolkit toolkit;
	ScrolledForm form;

	@Inject
	@Active
	BookInfo book;

	BookSection section;

	@Inject
	public PageView() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section) {

		this.section = section;

		// ImageHyperlink link;
		// Hyperlink hlink;
		// Button button;
		// Label label;
		//
		// toolkit = new FormToolkit(parent.getDisplay());
		// form = toolkit.createScrolledForm(parent);
		// form.setSize(448, 377);
		// form.setLocation(0, 0);
		// TableWrapLayout layout = new TableWrapLayout();
		// // ColumnLayout layout = new ColumnLayout();
		// // layout.maxNumColumns = 2;
		// form.getBody().setLayout(layout);
		//
		// layout.numColumns = 2;
		//
		// // form.setText(Strings.get("appTitle"));
		// form.setText(section.title);
		//
		// // link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		// //
		// link.setText("/////////////****************************************///////////////////////");
		TextEditor leetEdit = new TextEditor(parent, SWT.NONE);
		// // // Set text
		leetEdit.setText("LeetEdit is kind of leet.");
		// TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.colspan = 2;
		// link.setLayoutData(td);
		// leetEdit.setLayoutData(td);
	}

}