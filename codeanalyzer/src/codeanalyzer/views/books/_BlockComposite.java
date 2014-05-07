package codeanalyzer.views.books;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.utils.Strings;

public class _BlockComposite {
	// implements ISectionBlockComposite {

	FormText ft;
	GridData gd;
	CTabFolder tabFolder;
	Color selectedColor;
	FormToolkit toolkit;
	Composite body;

	// private int numColumns;


	public void render() {

		// Section bookSection;
		// Composite bookSectionClient;
		// HyperlinkAdapter bookSectionHandler;

		String buf = "<form><p><strong>Hellow</strong>, world!</p></form>";

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
		gd.verticalSpan = 2;
		// gd.horizontalSpan = numColumns - 1;
		ft.setLayoutData(gd);

		// Composite comp = toolkit.createComposite(body);


		tabFolder = new CTabFolder(body, SWT.FLAT | SWT.TOP);
		toolkit.adapt(tabFolder, true, true);

		gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = false;
		// gd.verticalSpan = 2;
		tabFolder.setLayoutData(gd);

		tabFolder.setSelectionBackground(new Color[] { selectedColor,
				toolkit.getColors().getBackground() }, new int[] { 50 });

		toolkit.paintBordersFor(tabFolder);

		createTabs(tabFolder);
		createText(tabFolder);
		tabFolder.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateSelection((CTabFolder) e.getSource());
			}
		});
		tabFolder.setSelection(0);
		updateSelection(tabFolder);

	}

	private void createTabs(CTabFolder tabFolder) {
		createTab(
				tabFolder,
				Strings.get("image"),
				Strings.get("PageWithSubPages.copyright.text"));
		createTab(tabFolder, Strings.get("code"),
				Strings.get("PageWithSubPages.license.text"));
		createTab(tabFolder, Strings.get("bookmarks"),
				Strings.get("PageWithSubPages.desc.text"));
	}

	private void createTab(CTabFolder tabFolder, String title, String content) {
		CTabItem item = new CTabItem(tabFolder, SWT.NULL);

		// GridData gd = new GridData(GridData.FILL_BOTH);
		// gd.grabExcessHorizontalSpace = true;
		// gd.grabExcessVerticalSpace = false;
		// // gd.verticalSpan = 2;
		// item.setLayoutData(gd);

		TextSection section = new TextSection(content);
		item.setText(title);
		item.setData(section);
	}

	private void updateSelection(CTabFolder tabFolder) {
		CTabItem item = tabFolder.getSelection();
		TextSection section = (TextSection) item.getData();
		((Label) tabFolder.getData()).setText(section.text);
	}

	private void createText(CTabFolder tabFolder) {

		Label lbl = toolkit.createLabel(body, "");
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		gd.verticalAlignment = SWT.TOP;
		lbl.setLayoutData(gd);

		tabFolder.setData(lbl);
	}

	class TextSection {
		String text;

		public TextSection(String text) {
			this.text = text;
		}
	}

	public void init(FormToolkit toolkit, Composite body, ScrolledForm form) {

		selectedColor = toolkit.getColors().getColor(IFormColors.SEPARATOR);
		this.toolkit = toolkit;
		this.body = body;
		// this.numColumns = numColumns;

	}

}
