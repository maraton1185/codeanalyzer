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
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.pico;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.PreferenceSupplier;

public class SectionsBlockView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;

	@Inject
	@Active
	BookInfo book;

	BookSection section;

	@Inject
	MDirtyable dirty;

	// TextEditor tinymce;

	ISectionBlockComposite sectionComposite;
	private MWindow window;

	@Inject
	public SectionsBlockView() {
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
		book.sections().setText(section,
				sectionComposite.getTinymce().getText());
		// System.out.println(tinymce.getText());
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

		sectionComposite.renderGroups(section);
	}

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section,
			@Active MWindow window) {

		this.section = section;
		this.window = window;
		// String buf = book.sections().getText(section);

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		body = form.getBody();
		GridLayout layout = new GridLayout();
		layout.numColumns = ISectionBlockComposite.numColumns;
		body.setLayout(layout);
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));
		sectionComposite = pico.get(ISectionBlockComposite.class);
		sectionComposite.init(toolkit, body, form, book);
		sectionComposite.setBlockView(true);
		sectionComposite.render(section);

	}

	@Focus
	public void OnFocus() {
		window.getContext().set(Const.CONTEXT_ACTIVE_VIEW_SECTION, section);
	}

}