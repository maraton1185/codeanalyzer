package ebook.module.book.views.section;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
import ebook.module.book.views.section.tools.BrowserComposite;
import ebook.module.tree.ITreeItemInfo;
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

	private List<ITreeItemInfo> sectionsList;

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

		if (data.parent != section)
			return;

		browserComposite.updateUrl();
	}

	@PostConstruct
	public void postConstruct(Composite parent, SectionInfo section,
			@Active final MWindow window, @Active MPart part) {

		this.section = section;
		this.window = window;
		this.part = part;

		makeEvents();

		body = parent;
		body.setLayout(new FillLayout());
		body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		browserComposite = new BrowserComposite(body, App.getJetty().book(
				book.getId(), section.getId()));

		OnFocus();
	}

	public Integer getId() {

		return section.getId();
	}

	// *************************************************************************************
	// *************************************************************************************
	// *************************************************************************************

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
		// onEdit = new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		//
		// SectionInfo selected = (SectionInfo) e.getHref();
		// window.getContext().set(SectionInfo.class, selected);
		// Utils.executeHandler(hs, cs,
		// Strings.get("command.id.ShowSection"));
		// // window.getContext().set(BookSection.class, current_section);
		// App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
		// new EVENT_UPDATE_VIEW_DATA(book, null, selected));
		// }
		//
		// };
		//
		// onDelete = new HyperlinkAdapter() {
		// @Override
		// public void linkActivated(HyperlinkEvent e) {
		//
		// // BookSection current_section = window.getContext().get(
		// // BookSection.class);
		// window.getContext().set(SectionInfo.class,
		// (SectionInfo) e.getHref());
		// Utils.executeHandler(hs, cs,
		// Strings.get("command.id.DeleteSection"));
		// // window.getContext().set(BookSection.class, current_section);
		//
		// }
		//
		// };

	}

}