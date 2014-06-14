package ebook.module.book.views.section.interfaces;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoOptions;

public interface ISectionComposite {

	void initSectionView(FormToolkit toolkit, ScrolledForm form, BookConnection book,
			SectionInfo sec);

	void initBlockView(FormToolkit toolkit, ScrolledForm form, BookConnection book,
			SectionInfo section, MDirtyable dirty);

	void render();

	void renderGroups();

	String getText();

	SectionInfoOptions getSectionOptions();

}