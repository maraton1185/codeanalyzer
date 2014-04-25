package codeanalyzer.views.books;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class SectionView {

	FormToolkit toolkit;
	ScrolledForm form;
	Composite body;
	Text text;
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
	// fillBody();
	// }

	@Inject
	public SectionView() {
		// TODO Your code here
	}

	private void fillBody() {

		Hyperlink hlink;
		// Button button;
		Label label;
		FormText ft;
		GridData gd;
		Section bookSection;
		Composite bookSectionClient;
		HyperlinkAdapter bookSectionHandler;
		CTabFolder tabFolder;
		Color selectedColor = toolkit.getColors().getColor(
				IFormColors.SEPARATOR);

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}
		// *************************************************************

		sectionsList = book.sections().getChildren(section);

		for (BookSection sec : sectionsList) {

			// Composite comp = toolkit.createComposite(body);
			// GridLayout gl = new GridLayout(2, false);
			// comp.setLayout(gl);
			// comp.setLayoutData(new RowdaGridData(GridData.FILL_BOTH));

			hlink = toolkit.createHyperlink(body, sec.title, SWT.WRAP);
			hlink.setHref(sec);
			hlink.addHyperlinkListener(new HyperlinkAdapter() {
				@Override
				public void linkActivated(HyperlinkEvent e) {
					window.getContext().set(BookSection.class,
							(BookSection) e.getHref());
					Utils.executeHandler(hs, cs,
							Strings.get("command.id.ShowSection"));
				}

			});
			gd = new GridData();
			gd.horizontalSpan = 2;
			hlink.setLayoutData(gd);

			if (sec.block) {

				// String buf =
				// "<form><p><strong>Hellow</strong>, world!</p></form>";
				//
				// buf = buf.replaceAll("strong>", "b>");
				// ft = toolkit.createFormText(comp, false);
				// try {
				// ft.setText(buf, true, true);
				// } catch (Exception e) {
				// ft.setText(buf, false, false);
				// label = toolkit.createLabel(comp, e.getMessage());
				// e.printStackTrace();
				// }
				// Browser browser = new Browser(comp, SWT.NONE);
				// browser.setJavascriptEnabled(false);
				//
				// toolkit.adapt(browser, true, true);
				// browser.setBackground(selectedColor);
				// browser.setText(buf);
				// gd = new GridData(SWT.FILL, SWT.FILL, true, true);
				// // gd.grabExcessHorizontalSpace = true;
				// // gd.grabExcessHorizontalSpace = false;
				// gd.horizontalSpan = 2;
				// gd.verticalSpan = 2;
				// ft.setLayoutData(gd);

				// Composite tcomp = toolkit.createComposite(comp);
				// gd = new GridData();
				// tcomp.setLayoutData(gd);
				//
				// RowLayout layout = new RowLayout();
				// layout.type = SWT.VERTICAL;
				// tcomp.setLayout(layout);

				// label = toolkit.createLabel(comp, "text1");
				// gd = new GridData(SWT.FILL, SWT.FILL, true, true);
				// // gd.grabExcessHorizontalSpace = true;
				// // gd.grabExcessHorizontalSpace = false;
				// gd.horizontalSpan = 2;
				// gd.verticalSpan = 2;
				// label.setLayoutData(gd);
				// label = toolkit.createLabel(comp, "text2");
				// gd = new GridData(SWT.FILL, SWT.FILL, true, true);
				// // gd.grabExcessHorizontalSpace = true;
				// // gd.grabExcessHorizontalSpace = false;
				// gd.horizontalSpan = 2;
				// gd.verticalSpan = 2;
				// label.setLayoutData(gd);
				// tabFolder = new CTabFolder(tcomp, SWT.FLAT | SWT.TOP);
				// toolkit.adapt(tabFolder, true, true);
				//
				// // gd = new GridData(GridData.FILL_HORIZONTAL);
				// // gd.heightHint = 0;
				// // tabFolder.setLayoutData(gd);
				//
				// tabFolder
				// .setSelectionBackground(new Color[] { selectedColor,
				// toolkit.getColors().getBackground() },
				// new int[] { 50 });
				//
				// toolkit.paintBordersFor(tabFolder);
				//
				// createTabs(tabFolder);
				// // createText(tcomp);
				// tabFolder.addSelectionListener(new SelectionAdapter() {
				// @Override
				// public void widgetSelected(SelectionEvent e) {
				// updateSelection((CTabFolder) e.getSource());
				// }
				// });
				// tabFolder.setSelection(0);
				// updateSelection(tabFolder);
				// comp.layout(true);
			}
		}

		// *************************************************************
		// body.layout(true);
		// form.getBody().layout(true);
		form.reflow(true);
	}

	private void createTabs(CTabFolder tabFolder) {
		createTab(tabFolder, Strings.get("image"),
				Strings.get("PageWithSubPages.copyright.text"));
		createTab(tabFolder, Strings.get("code"),
				Strings.get("PageWithSubPages.license.text"));
		createTab(tabFolder, Strings.get("bookmarks"),
				Strings.get("PageWithSubPages.desc.text"));
	}

	private void createTab(CTabFolder tabFolder, String title, String content) {
		CTabItem item = new CTabItem(tabFolder, SWT.NULL);
		TextSection section = new TextSection(content);
		item.setText(title);
		item.setData(section);
	}

	private void updateSelection(CTabFolder tabFolder) {
		CTabItem item = tabFolder.getSelection();
		TextSection section = (TextSection) item.getData();
		text.setText(section.text);
	}

	private void createText(Composite parent) {
		Composite tabContent = toolkit.createComposite(parent);
		// tabContent.setLayoutData(new GridData(GridData.FILL_BOTH));
		GridLayout layout = new GridLayout();
		tabContent.setLayout(layout);
		layout.numColumns = 2;
		layout.marginWidth = 0;
		GridData gd;
		text = toolkit.createText(tabContent, "", SWT.MULTI | SWT.WRAP); //$NON-NLS-1$
		gd = new GridData(GridData.FILL_BOTH);
		gd.verticalSpan = 2;
		text.setLayoutData(gd);
		Button apply = toolkit.createButton(tabContent,
				Strings.get("PageWithSubPages.apply"), SWT.PUSH); //$NON-NLS-1$
		apply.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
		Button reset = toolkit.createButton(tabContent,
				Strings.get("PageWithSubPages.reset"), SWT.PUSH); //$NON-NLS-1$
		reset.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
				| GridData.VERTICAL_ALIGN_BEGINNING));
	}

	class TextSection {
		String text;

		public TextSection(String text) {
			this.text = text;
		}
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
		layout.numColumns = 2;
		// layout.type = SWT.VERTICAL;
		// layout.maxNumColumns = 2;
		body.setLayout(layout);

		form.setText(section.title);

		// body = toolkit.createComposite(form.getBody());
		// body.setLayout(layout);

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