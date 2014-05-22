package codeanalyzer.module.books.views.section.interfaces;

import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.module.books.WindowBookInfo;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.module.books.section.SectionOptions;

public interface ISectionComposite {

	void initSectionView(FormToolkit toolkit, ScrolledForm form, WindowBookInfo book,
			SectionInfo sec);

	void initBlockView(FormToolkit toolkit, ScrolledForm form, WindowBookInfo book,
			SectionInfo section, MDirtyable dirty);

	void render();

	void renderGroups();

	String getText();

	SectionOptions getSectionOptions();

}