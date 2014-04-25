package codeanalyzer.views.books;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.core.pico;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;
	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	BookInfo book;

	BookSection section;
	private List<BookSection> sectionsList;
	private ECommandService cs;
	private EHandlerService hs;
	private MWindow window;

	ISectionBlockComposite sectionComposite;
	private final int numColumns = 2;

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_CONTENT_VIEW_DATA data,
			final EHandlerService hs, final ECommandService cs) {

		if (book != data.book)
			return;

		if (!data.parent.equals(section))
			return;

		fillBody();
	}

	@Inject
	public SectionView() {
		// TODO Your code here
	}

	private void fillBody() {

		Hyperlink hlink;
		GridData gd;

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}
		// *************************************************************

		sectionsList = book.sections().getChildren(section);

		for (BookSection sec : sectionsList) {

			hlink = toolkit.createHyperlink(body, sec.title, SWT.WRAP);
			// hlink.setFont(body.getFont());
			hlink.setHref(sec);
			hlink.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {

					BookSection current_section = window.getContext().get(
							BookSection.class);
					window.getContext().set(BookSection.class,
							(BookSection) e.getHref());
					Utils.executeHandler(hs, cs,
							Strings.get("command.id.ShowSection"));
					window.getContext().set(BookSection.class, current_section);

				}

			});
			gd = new GridData();
			gd.horizontalSpan = numColumns;
			hlink.setLayoutData(gd);

			if (sec.block)
				sectionComposite.render();
		}

		// *************************************************************
		form.reflow(true);
	}

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section,
			final ECommandService cs, final EHandlerService hs,
			@Active final MWindow window) {

		this.section = section;
		this.cs = cs;
		this.hs = hs;
		this.window = window;

		// ImageHyperlink link;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		body = form.getBody();
		GridLayout layout = new GridLayout();
		layout.numColumns = numColumns;
		body.setLayout(layout);

		// form.setFont(new Font(parent.getDisplay(), PreferenceSupplier
		// .getFontData(PreferenceSupplier.FONT)));
		// form.setText(section.title);

		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));
		sectionComposite = pico.get(ISectionBlockComposite.class);
		sectionComposite.init(toolkit, body, form, numColumns);

		fillBody();

		// IMAGEHYPERLINKS
		// *******************************************************

		// link = toolkit.createImageHyperlink(form.getBody(), SWT.WRAP);
		// // link.setImage(Utils.getImage("add_book.png"));
		// link.setText("Добавить блок текста");
		// link.addHyperlinkListener(new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		// Utils.executeHandler(hs, cs,
		// Strings.get("command.id.AddSectionsBlock"));
		// super.linkActivated(e);
		// }
		//
		// });

	}
}