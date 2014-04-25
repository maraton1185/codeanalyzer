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
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.IFormColors;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.FormText;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Const.EVENT_UPDATE_CONTENT_VIEW_DATA;
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
		// Button button;
		// Label label;
		FormText ft;
		GridData gd;
		// Section bookSection;
		// Composite bookSectionClient;
		// HyperlinkAdapter bookSectionHandler;
		CTabFolder tabFolder;
		Color selectedColor = toolkit.getColors().getColor(
				IFormColors.SEPARATOR);

		for (Control ctrl : body.getChildren()) {
			ctrl.dispose();
		}
		// *************************************************************

		sectionsList = book.sections().getChildren(section);

		for (BookSection sec : sectionsList) {

			hlink = toolkit.createHyperlink(body, sec.title, SWT.WRAP);
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
			gd.horizontalSpan = 2;
			hlink.setLayoutData(gd);

			if (sec.block) {

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

				gd = new GridData(GridData.FILL_BOTH);
				gd.grabExcessHorizontalSpace = true;
				gd.grabExcessVerticalSpace = false;
				gd.verticalSpan = 2;
				ft.setLayoutData(gd);

				tabFolder = new CTabFolder(body, SWT.FLAT | SWT.TOP);
				toolkit.adapt(tabFolder, true, true);

				gd = new GridData(GridData.FILL_BOTH);
				gd.grabExcessHorizontalSpace = true;
				gd.grabExcessVerticalSpace = false;
				// gd.verticalSpan = 2;
				tabFolder.setLayoutData(gd);

				tabFolder
						.setSelectionBackground(new Color[] { selectedColor,
								toolkit.getColors().getBackground() },
								new int[] { 50 });

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
		}

		// *************************************************************
		form.reflow(true);
	}

	private void createTabs(CTabFolder tabFolder) {
		createTab(
				tabFolder,
				Strings.get("image"),
				Strings.get("PageWithSubPages.copyright.text ***********************************************456666666666666666666666"));
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
		// Composite tabContent = toolkit.createComposite(parent);
		// GridLayout layout = new GridLayout();
		// tabContent.setLayout(layout);
		// layout.numColumns = 2;
		// layout.marginWidth = 0;
		// GridData gd;
		// Text text = toolkit.createText(body, "", SWT.MULTI | SWT.WRAP);
		Label lbl = toolkit.createLabel(body, "");
		GridData gd = new GridData(GridData.FILL_BOTH);
		gd.grabExcessHorizontalSpace = true;
		gd.grabExcessVerticalSpace = true;
		// gd.verticalSpan = 2;
		lbl.setLayoutData(gd);
		// gd = new GridData(GridData.FILL_BOTH);
		// gd.verticalSpan = 2;
		// text.setLayoutData(gd);
		// Button apply = toolkit.createButton(tabContent,
		//				Strings.get("PageWithSubPages.apply"), SWT.PUSH); //$NON-NLS-1$
		// apply.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
		// | GridData.VERTICAL_ALIGN_BEGINNING));
		// Button reset = toolkit.createButton(tabContent,
		//				Strings.get("PageWithSubPages.reset"), SWT.PUSH); //$NON-NLS-1$
		// reset.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL
		// | GridData.VERTICAL_ALIGN_BEGINNING));

		tabFolder.setData(lbl);
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
		// layout.
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