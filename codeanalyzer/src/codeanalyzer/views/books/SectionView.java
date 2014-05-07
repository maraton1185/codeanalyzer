package codeanalyzer.views.books;

import java.util.List;

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
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.books.section.BookSection;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.pico;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;
import codeanalyzer.views.books.interfaces.IBlockComposite;

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
	MPart part;

	public BookSection getSection() {
		return section;
	}

	private List<BookSection> sectionsList;
	private ECommandService cs;
	private EHandlerService hs;
	private MWindow window;

	IBlockComposite sectionComposite;

	private IHyperlinkListener onEdit;
	private IHyperlinkListener onDelete;

	@Inject
	@Optional
	public void EVENT_UPDATE_CONTENT_VIEW(
			@UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA data,
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
		part.setLabel(section.title);

		fillBody();
	}

	@Inject
	public SectionView() {
		// TODO Your code here
	}

	private void fillBody() {

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}
		// *************************************************************

		sectionsList = book.sections().getChildren(section);

		for (BookSection sec : sectionsList) {

			createTopLinks(sec);

			if (sec.block)
				sectionComposite.render(sec);
		}

		// *************************************************************
		form.reflow(true);
	}

	private void createTopLinks(BookSection sec) {
		Hyperlink link;
		ImageHyperlink hlink;
		GridData gd;

		Composite comp = toolkit.createComposite(body);
		// gd = new TableWrapData();
		// gd.colspan = ISectionBlockComposite.numColumns;
		// comp.setLayoutData(gd);
		gd = new GridData();
		gd.horizontalSpan = IBlockComposite.numColumns;
		comp.setLayoutData(gd);
		comp.setLayout(new RowLayout(SWT.HORIZONTAL));

		link = toolkit.createHyperlink(comp, sec.title, SWT.WRAP);
		link.setFont(body.getFont());
		link.setHref(sec);
		link.addHyperlinkListener(onEdit);

		hlink = toolkit.createImageHyperlink(comp, SWT.WRAP);
		hlink.setImage(Utils.getImage("edit.png"));
		// hlink.setText("�������������");
		hlink.setHref(sec);
		hlink.setToolTipText("��������");
		hlink.addHyperlinkListener(onEdit);

		hlink = toolkit.createImageHyperlink(comp, SWT.WRAP);
		hlink.setImage(Utils.getImage("delete.png"));
		// hlink.setText("�������");
		hlink.setToolTipText("�������");
		hlink.setHref(sec);
		hlink.addHyperlinkListener(onDelete);

	}

	private void makeEvents() {
		onEdit = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				BookSection selected = (BookSection) e.getHref();
				window.getContext().set(BookSection.class, selected);
				Utils.executeHandler(hs, cs,
						Strings.get("command.id.ShowSection"));
				// window.getContext().set(BookSection.class, current_section);
				AppManager.br.post(Const.EVENT_UPDATE_CONTENT_VIEW,
						new EVENT_UPDATE_VIEW_DATA(book, null, selected));
			}

		};

		onDelete = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				// BookSection current_section = window.getContext().get(
				// BookSection.class);
				window.getContext().set(BookSection.class,
						(BookSection) e.getHref());
				Utils.executeHandler(hs, cs,
						Strings.get("command.id.DeleteSection"));
				// window.getContext().set(BookSection.class, current_section);

			}

		};

	}

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section,
			final ECommandService cs, final EHandlerService hs,
			@Active final MWindow window, @Active MPart part) {

		this.section = section;
		this.cs = cs;
		this.hs = hs;
		this.window = window;
		this.part = part;

		makeEvents();

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		// form.addListener(SWT.SCROLL_PAGE, new Listener() {
		// @Override
		// public void handleEvent(Event e) {
		// // form.getVerticalBar().setFocus();
		// form.getVerticalBar().setIncrement(
		// form.getVerticalBar().getIncrement() * 3);
		// }
		// });
		body = form.getBody();
		// TableWrapLayout layout = new TableWrapLayout();
		GridLayout layout = new GridLayout();
		layout.numColumns = IBlockComposite.numColumns;
		body.setLayout(layout);
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));
		sectionComposite = pico.get(IBlockComposite.class);
		sectionComposite.init(toolkit, body, form, book);

		fillBody();

	}

	@Focus
	public void OnFocus() {
		window.getContext().set(Const.CONTEXT_ACTIVE_VIEW_SECTION, section);
		// window.getContext().set(Const.CONTENT_SECTION_SELECTED, false);
		// ctx.declareModifiable(Const.CONTENT_SECTION_SELECTED);
		// section.parent == 0
		// window.getContext().de

	}
}