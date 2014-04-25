package codeanalyzer.views.books;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

public class SectionBlockComposite implements ISectionBlockComposite {

	FormText ft;
	GridData gd;
	CTabFolder tabFolder;
	Color selectedColor;
	FormToolkit toolkit;
	Composite body;
	private int numColumns;
	private ScrolledForm form;


	/* (non-Javadoc)
	 * @see codeanalyzer.views.books.ISectionBlockComposite#render()
	 */
	@Override
	public void render() {

		// Section bookSection;
		// Composite bookSectionClient;
		// HyperlinkAdapter bookSectionHandler;

		String buf = "<form><p><strong>Hellow</strong>, Marat!</p></form>";

		buf = buf.replaceAll("strong>", "b>");
		ft = toolkit.createFormText(body, false);
		try {
			ft.setText(buf, true, true);
		} catch (Exception e) {
			ft.setText(buf, false, false);
			toolkit.createLabel(body, e.getMessage());
			e.printStackTrace();
		}
		ft.setFont(body.getFont());
		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;
		gd.verticalSpan = 1;
		gd.horizontalSpan = numColumns - 1;
		ft.setLayoutData(gd);

		Composite comp = toolkit.createComposite(body);
		RowLayout layout = new RowLayout(SWT.VERTICAL);
		// FillLayout layout = new FillLayout(SWT.VERTICAL);
		comp.setLayout(layout);

		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = false;
		gd.grabExcessVerticalSpace = false;
		// gd.verticalSpan = 1;
		// gd.horizontalSpan = numColumns - 1;
		gd.minimumWidth = 100;
		gd.widthHint = 150;
		comp.setLayoutData(gd);

		addSection(comp, "picture 1");
		addSection(comp, "picture 2");
		addSection(comp, "code 1");
		addSection(comp, "bookmarks");

	}

	private void addSection(Composite comp, String title) {
		Section section = toolkit.createSection(comp, Section.SHORT_TITLE_BAR
				| Section.TWISTIE);

		// gd = new GridData(GridData.FILL_BOTH);
		// section.setLayoutData(gd);

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
			int numColumns) {

		selectedColor = toolkit.getColors().getColor(IFormColors.SEPARATOR);
		this.toolkit = toolkit;
		this.body = body;
		this.numColumns = numColumns;
		this.form = form;

	}

}
