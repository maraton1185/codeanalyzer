package codeanalyzer.views.books;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.SectionInfo;
import codeanalyzer.books.section.SectionSaveData;
import codeanalyzer.core.pico;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.views.books.interfaces.ISectionComposite;

public class BlockView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;

	@Inject
	@Active
	BookInfo book;

	SectionInfo section;

	@Inject
	MDirtyable dirty;

	ISectionComposite sectionComposite;

	private MWindow window;

	@Inject
	public BlockView() {
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

		SectionSaveData data = new SectionSaveData();
		data.text = sectionComposite.getText();
		data.options = sectionComposite.getSectionOptions();
		book.sections().saveBlock(section, data);
		dirty.setDirty(false);
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_BLOCK_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_SECTION_BLOCK_VIEW) EVENT_UPDATE_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs) {

		if (book != data.book)
			return;

		// if (!data.onlySectionView)
		// return;

		if (data.parent == null)
			return;

		if (!data.parent.equals(section))
			return;

		// part.setLabel(data.parent.title);
		// part.setLabel(section.title);

		sectionComposite.renderGroups();
	}

	@PostConstruct
	public void postConstruct(Composite parent, SectionInfo section,
			@Active MWindow window) {

		this.section = section;
		this.window = window;
		// String buf = book.sections().getText(section);

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		body = form.getBody();
		body.setLayout(new FillLayout());
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));
		sectionComposite = pico.get(ISectionComposite.class);
		sectionComposite.initBlockView(toolkit, form, book, section, dirty);
		sectionComposite.render();

	}

	@Focus
	public void OnFocus() {
		window.getContext().set(Const.CONTEXT_ACTIVE_VIEW_SECTION, section);
	}

}