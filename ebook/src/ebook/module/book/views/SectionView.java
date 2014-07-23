package ebook.module.book.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.tools.BrowserComposite;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;

public class SectionView {

	Composite body;

	@Inject
	@Active
	BookConnection book;

	SectionInfo section;
	MPart part;

	public SectionInfo getSection() {
		return section;
	}

	private MWindow window;

	BrowserComposite browserComposite;
	StringBuffer text = new StringBuffer();

	@Focus
	public void OnFocus() {
		window.getContext().set(Events.CONTEXT_ACTIVE_VIEW_SECTION, section);
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_SECTION_VIEW(
			@UIEventTopic(Events.EVENT_UPDATE_SECTION_VIEW) EVENT_UPDATE_VIEW_DATA data) {

		if (book != data.book)
			return;

		if (!data.parent.equals(section))
			return;

		browserComposite.updateUrl(data.parent.tag);

		window.getContext().set(SectionInfo.class, data.selected);
	}

	@PostConstruct
	public void postConstruct(Composite parent, SectionInfo section,
			@Active final MWindow window, @Active MPart part,
			final EHandlerService hs, final ECommandService cs) {

		this.section = section;
		this.window = window;
		this.part = part;

		body = parent;
		body.setLayout(new FillLayout());
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		String url = App.getJetty().host()
				+ App.getJetty().section(book.getTreeItem().getId(),
						section.getId());
		// url = "http://localhost/tmpl/book/css/bootstrap.min.css";
		// url = "http://localhost/tmpl/book/js/book.js";
		browserComposite = new BrowserComposite(body, url, section.tag, book,
				window, hs, cs);

		OnFocus();
	}

	public Integer getId() {

		return section.getId();
	}

	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************

}