package codeanalyzer.views.books.interfaces;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.SectionInfo;
import codeanalyzer.books.section.SectionOptions;

public interface ISectionComposite {

	void initSectionView(FormToolkit toolkit, ScrolledForm form, BookInfo book,
			SectionInfo sec);

	void initBlockView(FormToolkit toolkit, ScrolledForm form, BookInfo book,
			SectionInfo section, MDirtyable dirty);

	void render();

	void renderGroups();

	String getText();

	SectionOptions getSectionOptions();

}