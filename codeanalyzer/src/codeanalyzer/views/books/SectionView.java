package codeanalyzer.views.books;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.tools.TextEditor;
import codeanalyzer.utils.Const;

public class SectionView {

	FormToolkit toolkit;
	ScrolledForm form;

	@Inject
	@Active
	BookInfo book;

	BookSection section;

	@Inject
	MDirtyable dirty;

	TextEditor tinymce;

	@Inject
	public SectionView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_SET_SECTIONVIEW_DIRTY(
			@UIEventTopic(Const.EVENT_SET_SECTIONVIEW_DIRTY) Object section) {
		if (section == this.section)
			dirty.setDirty(true);
	}

	@Persist
	public void save() {
		System.out.println(tinymce.getText());
		dirty.setDirty(false);
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
		tinymce = new TextEditor(parent, SWT.NONE, section);
		tinymce.setText("HELLO");
		// TableWrapData td = new TableWrapData(TableWrapData.FILL_GRAB);
		// td.colspan = 2;
		// link.setLayoutData(td);
		// leetEdit.setLayoutData(td);
	}

}