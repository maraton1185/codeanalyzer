package ebook.module.book.views.section;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.events.IHyperlinkListener;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.section.tools.BrowserComposite;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class SectionView {

	// FormToolkit toolkit;
	// ScrolledForm form;
	Composite body;
	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	BookConnection book;

	SectionInfo section;
	MPart part;

	public SectionInfo getSection() {
		return section;
	}

	private List<ITreeItemInfo> sectionsList;
	private ECommandService cs;
	private EHandlerService hs;
	private MWindow window;

	// ISectionComposite sectionComposite;

	private IHyperlinkListener onEdit;
	private IHyperlinkListener onDelete;

	// Composite blockComposite;
	BrowserComposite browserComposite;
	StringBuffer text = new StringBuffer();

	// @Inject
	// @Optional
	// public void EVENT_UPDATE_CONTENT_VIEW(
	// @UIEventTopic(Events.EVENT_UPDATE_CONTENT_VIEW) EVENT_UPDATE_VIEW_DATA
	// data,
	// final EHandlerService hs, final ECommandService cs) {
	//
	// if (book != data.book)
	// return;
	//
	// // if (!data.onlySectionView)
	// // return;
	//
	// if (data.parent == null)
	// return;
	//
	// if (!data.parent.equals(section))
	// return;
	//
	// // part.setLabel(data.parent.title);
	// part.setLabel(section.getTitle());
	//
	// fillBody();
	// }

	@Focus
	public void OnFocus() {
		window.getContext().set(Events.CONTEXT_ACTIVE_VIEW_SECTION, section);
		// window.getContext().set(Const.CONTENT_SECTION_SELECTED, false);
		// ctx.declareModifiable(Const.CONTENT_SECTION_SELECTED);
		// section.parent == 0
		// window.getContext().de

	}

	@PostConstruct
	public void postConstruct(Composite parent, SectionInfo section,
			final ECommandService cs, final EHandlerService hs,
			@Active final MWindow window, @Active MPart part) {

		this.section = section;
		this.cs = cs;
		this.hs = hs;
		this.window = window;
		this.part = part;

		makeEvents();

		// SWING
		// toolkit = new FormToolkit(parent.getDisplay());
		// form = toolkit.createScrolledForm(parent);
		// form.addListener(SWT.SCROLL_PAGE, new Listener() {
		// @Override
		// public void handleEvent(Event e) {
		// // form.getVerticalBar().setFocus();
		// form.getVerticalBar().setIncrement(
		// form.getVerticalBar().getIncrement() * 3);
		// }
		// });
		body = parent;
		// body.setLayout(new RowLayout(SWT.VERTICAL));
		body.setLayout(new FillLayout());

		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		// blockComposite = toolkit.createComposite(form.getBody(), SWT.BORDER);
		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// blockComposite.setLayoutData(gd);
		// GridLayout layout = new GridLayout();
		// layout.numColumns = 2;
		// blockComposite.setLayout(layout);

		browserComposite = new BrowserComposite(body);
		// toolkit.adapt(browserComposite, true, true);

		// fillBody();

		// browserComposite.setText(text.toString());
		// parent.addControlListener(new ControlAdapter() {
		//
		// @Override
		// public void controlResized(ControlEvent e) {
		// super.controlResized(e);
		// form.layout(true);
		// }
		//
		// });
	}

	private void fillBody() {

		// for (Control ctrl : body.getChildren()) {
		// ctrl.dispose();
		// }
		// *************************************************************

		sectionsList = book.srv().getChildren(section.getId());

		for (ITreeItemInfo sec : sectionsList) {

			text.append("<div>" + sec.getTitle() + "</div>");
			// createTopLinks(sec);

			// if (sec.block) {
			// ISectionComposite sectionComposite = pico
			// .get(ISectionComposite.class);
			// sectionComposite.initSectionView(toolkit, form, book, sec);
			// sectionComposite.render();
			// }
		}

		// *************************************************************
		// form.reflow(true);
	}

	private void createTopLinks(SectionInfo sec) {
		// Hyperlink link;
		// ImageHyperlink hlink;
		//
		// Composite comp = toolkit.createComposite(body);
		// comp.setLayout(new RowLayout(SWT.HORIZONTAL));
		//
		// GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		// gd.horizontalSpan = 2;
		// comp.setLayoutData(gd);
		//
		// link = toolkit.createHyperlink(comp, sec.title, SWT.WRAP);
		// link.setFont(body.getFont());
		// link.setHref(sec);
		// link.addHyperlinkListener(onEdit);
		//
		// hlink = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// hlink.setImage(Utils.getImage("edit.png"));
		// // hlink.setText("редактировать");
		// hlink.setHref(sec);
		// hlink.setToolTipText("Изменить");
		// hlink.addHyperlinkListener(onEdit);
		//
		// hlink = toolkit.createImageHyperlink(comp, SWT.WRAP);
		// hlink.setImage(Utils.getImage("delete.png"));
		// // hlink.setText("удалить");
		// hlink.setToolTipText("Удалить");
		// hlink.setHref(sec);
		// hlink.addHyperlinkListener(onDelete);

	}

	private void makeEvents() {
		onEdit = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				SectionInfo selected = (SectionInfo) e.getHref();
				window.getContext().set(SectionInfo.class, selected);
				Utils.executeHandler(hs, cs,
						Strings.get("command.id.ShowSection"));
				// window.getContext().set(BookSection.class, current_section);
				App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
						new EVENT_UPDATE_VIEW_DATA(book, null, selected));
			}

		};

		onDelete = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {

				// BookSection current_section = window.getContext().get(
				// BookSection.class);
				window.getContext().set(SectionInfo.class,
						(SectionInfo) e.getHref());
				Utils.executeHandler(hs, cs,
						Strings.get("command.id.DeleteSection"));
				// window.getContext().set(BookSection.class, current_section);

			}

		};

	}

	public Integer getId() {

		return section.getId();
	}

}