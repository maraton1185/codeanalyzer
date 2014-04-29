package codeanalyzer.views.books;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;
import org.eclipse.ui.forms.widgets.TableWrapData;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.tools.BrowserComposite;
import codeanalyzer.tools.TinyTextEditor;

public class SectionBlockComposite implements ISectionBlockComposite {

	FormText ft;
	FormToolkit toolkit;
	Composite body;
	Boolean blockView;
	TinyTextEditor tinymce;

	@Override
	public TinyTextEditor getTinymce() {
		return tinymce;
	}

	private ScrolledForm form;
	// private BookSection section;
	private BookInfo book;

	/*
	 * (non-Javadoc)
	 * 
	 * @see codeanalyzer.views.books.ISectionBlockComposite#render()
	 */
	@Override
	public void render(BookSection section) {

		if (blockView)
			addTinyText(section);
		else
			addBrowserText(section);
		// addFormText(section);

	}

	private void addTinyText(BookSection section) {

		String buf = book.sections().getText(section);

		// Browser browser = new Browser(body, SWT.Resize);
		tinymce = new TinyTextEditor(body, section);
		toolkit.adapt(tinymce, true, true);
		tinymce.setText(buf);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.widthHint = 50;
		gd.horizontalSpan = numColumns - 1;
		tinymce.setLayoutData(gd);

		Composite comp = toolkit.createComposite(body);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		comp.setLayoutData(gd);

		addSections(comp);
	}

	private void addBrowserText(BookSection section) {

		String buf = book.sections().getText(section);

		// TableWrapData gd;

		// Browser browser = new Browser(body, SWT.Resize);
		BrowserComposite browserComposite = new BrowserComposite(body, buf,
				form);
		toolkit.adapt(browserComposite, true, true);
		// browserComposite.setText(buf);

		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;
		gd.widthHint = 50;
		// gd.heightHint = browserComposite.getHeight();
		gd.horizontalSpan = numColumns - 1;
		browserComposite.setLayoutData(gd);
		// browserComposite.getHeight();

		// gd = new TableWrapData(TableWrapData.FILL);
		// gd.grabHorizontal = true;
		// gd.grabVertical = false;
		// // gd.rowspan = 1;
		// gd.colspan = numColumns - 1;
		// gd.widthHint = 50;
		// browserComposite.setLayoutData(gd);

		Composite comp = toolkit.createComposite(body);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		gd = new GridData();
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = false;
		gd.verticalAlignment = SWT.TOP;
		comp.setLayoutData(gd);

		addSections(comp);
	}

	private void addFormText(BookSection section) {

		String buf = book.sections().getText(section);

		TableWrapData gd;

		ft = toolkit.createFormText(body, false);
		try {
			ft.setText(buf, true, true);
		} catch (Exception e) {
			buf = e.getMessage() + ": " + buf;
			ft.setText(buf, false, false);
			// toolkit.createLabel(body, e.getMessage());
			// e.printStackTrace();
		}
		ft.setFont(body.getFont());
		gd = new TableWrapData(TableWrapData.FILL);
		gd.grabHorizontal = true;
		gd.grabVertical = false;
		// gd.rowspan = 1;
		gd.colspan = numColumns - 1;
		ft.setLayoutData(gd);

		Composite comp = toolkit.createComposite(body);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		comp.setLayout(layout);

		gd = new TableWrapData();
		gd.grabHorizontal = false;
		// gd.grabExcessVerticalSpace = blockView;
		gd.valign = SWT.CENTER;
		gd.grabVertical = false;
		comp.setLayoutData(gd);

		addSections(comp);
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

	// private void updateSelection(CTabFolder tabFolder) {
	// CTabItem item = tabFolder.getSelection();
	// TextSection section = (TextSection) item.getData();
	// ((Label) tabFolder.getData()).setText(section.text);
	// }
	//
	// private void createText(CTabFolder tabFolder) {
	//
	// Label lbl = toolkit.createLabel(body, "");
	// GridData gd = new GridData(GridData.FILL_BOTH);
	// gd.grabExcessHorizontalSpace = true;
	// gd.grabExcessVerticalSpace = true;
	// gd.verticalAlignment = SWT.TOP;
	// lbl.setLayoutData(gd);
	//
	// tabFolder.setData(lbl);
	// }
	//
	// class TextSection {
	// String text;
	//
	// public TextSection(String text) {
	// this.text = text;
	// }
	// }

	@Override
	public void init(FormToolkit toolkit, Composite body, ScrolledForm form,
			BookInfo book) {

		// selectedColor = toolkit.getColors().getColor(IFormColors.SEPARATOR);
		this.toolkit = toolkit;
		this.body = body;
		this.form = form;
		this.book = book;

		blockView = false;

	}

	@Override
	public void setBlockView(Boolean blockView) {
		this.blockView = blockView;
	}

}
