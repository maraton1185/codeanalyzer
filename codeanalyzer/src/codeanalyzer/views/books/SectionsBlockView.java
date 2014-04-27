package codeanalyzer.views.books;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.Persist;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.MDirtyable;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.tools.TinyTextEditor;
import codeanalyzer.utils.Const;

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

	@Inject
	public SectionsBlockView() {
		// TODO Your code here
	}

	// @Inject
	// @Optional
	// public void EVENT_UPDATE_CONTENT_VIEW(
	// @UIEventTopic(Const.EVENT_UPDATE_CONTENT_VIEW)
	// EVENT_UPDATE_CONTENT_VIEW_DATA data,
	// final EHandlerService hs, final ECommandService cs) {
	//
	// if (book != data.book)
	// return;
	//
	// if (!data.parent.equals(section))
	// return;
	//
	// part.setLabel(data.parent.title);
	// }

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

	@PostConstruct
	public void postConstruct(Composite parent, BookSection section) {

		this.section = section;

		String buf = book.sections().getText(section);

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);

		// Composite comp = new Composite(parent, SWT.None);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		form.setLayout(layout);
		body = form.getBody();

		GridLayout l1 = new GridLayout();
		l1.numColumns = 2;
		body.setLayout(l1);

		TinyTextEditor tinymce = new TinyTextEditor(body, SWT.NONE, section);
		toolkit.adapt(tinymce, true, true);
		tinymce.setText(buf);

		GridData gd = new GridData();
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		tinymce.setLayoutData(gd);

		Composite comp = toolkit.createComposite(body);
		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.FILL;
		gd.horizontalAlignment = SWT.FILL;
		comp.setLayoutData(gd);

		form.setText("form text");
		toolkit.createLabel(comp, "test");

		// FillLayout l = new FillLayout();
		// comp.setLayout(l);
		//
		// body = form.getBody();
		// GridLayout layout = new GridLayout();
		// // TableWrapLayout layout = new TableWrapLayout();
		// layout.numColumns = ISectionBlockComposite.numColumns;
		// body.setLayout(layout);
		// body.setFont(new Font(parent.getDisplay(), PreferenceSupplier
		// .getFontData(PreferenceSupplier.FONT)));



		// addSections(body);

		// form.setLayoutData(layoutData);
		// sectionComposite = pico.get(ISectionBlockComposite.class);
		// sectionComposite.init(toolkit, body, form, book, section);
		// sectionComposite.setBlockView(true);
		// sectionComposite.render();

		// tinymce = new TextEditor(parent, SWT.NONE, section);
		// tinymce.setText("HELLO");

		// String buf = book.sections().getText(section);

		// TinyTextEditor tinymce = new TinyTextEditor(body, SWT.NONE, section);
		// toolkit.adapt(tinymce, true, true);
		// tinymce.setText(buf);

		// gd.rowspan = 1;
		// gd.minimumWidth = 300;
		// gd.horizontalSpan = ISectionBlockComposite.numColumns - 1;

		// gd = new TableWrapData(TableWrapData.FILL_GRAB);
		// gd.grabHorizontal = true;
		// gd.grabVertical = true;
		// gd.valign = SWT.FILL;
		// gd.align = SWT.FILL;
		// gd.vhalign = SWT.FILL;
		// gd.rowspan = 1;
		// gd.minimumWidth = 300;
		// gd.colspan = numColumns - 1;

		// Composite comp = toolkit.createComposite(body);
		// GridLayout layout1 = new GridLayout();
		// layout1.numColumns = 2;
		// comp.setLayout(layout1);
		//
		// gd = new GridData();
		// gd.grabExcessHorizontalSpace = false;
		// gd.grabExcessVerticalSpace = true;
		// gd.verticalAlignment = SWT.TOP;
		// // gd = new TableWrapData();
		// // gd.grabHorizontal = false;
		// // gd.grabVertical = true;
		// // gd.valign = SWT.TOP;
		// comp.setLayoutData(gd);

		// addSections(body);

	}

	private void addSections(Composite comp) {
		addSection(comp, "picture 1");
		addSection(comp, "picture 2");
		addSection(comp, "picture 3");
		addSection(comp, "picture 4");
		addSection(comp, "code 1");
		addSection(comp, "bookmarks");

	}

	private void addSection(Composite comp, String title) {
		Section section = toolkit.createSection(comp, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);

		section.setText(title);
		Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new FillLayout());
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				form.reflow(true);
			}
		});
		toolkit.createLabel(sectionClient, "test");
		section.setClient(sectionClient);
	}
	public void reflow() {
		// form.reflow(true);
		// form.g
		body.layout(true);

	}

}