package codeanalyzer.views.main;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.events.ExpansionAdapter;
import org.eclipse.ui.forms.events.ExpansionEvent;
import org.eclipse.ui.forms.events.HyperlinkAdapter;
import org.eclipse.ui.forms.events.HyperlinkEvent;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.Section;

import codeanalyzer.books.book.BookInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class BooksPerspectiveView {

	FormToolkit toolkit;
	ScrolledForm form;
	Section bookSection;
	Composite bookSectionClient;
	HyperlinkAdapter bookSectionHandler;

	@Inject
	public BooksPerspectiveView() {
		// TODO Your code here
	}

	@Inject
	@Optional
	public void EVENT_UPDATE_BOOK_LIST(
			@UIEventTopic(Const.EVENT_UPDATE_BOOK_LIST) Object o,
			@Optional BookInfo book, Shell shell) {

		for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
				.getChildren()) {
			ctrl.dispose();
		}

		Utils.fillBooks(bookSectionClient, toolkit, shell, bookSectionHandler);
		bookSection.setClient(bookSectionClient);

		form.reflow(true);
	}

	@PostConstruct
	public void postConstruct(Composite parent, final IBookManager bm,
			final Shell shell) {

		// ImageHyperlink link;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

		bookSection = toolkit.createSection(form.getBody(),
		// Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE |
		// Section.EXPANDED
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);

		bookSection.setText("Список книг");
		bookSectionClient = toolkit.createComposite(bookSection);
		bookSectionClient.setLayout(new GridLayout());
		bookSectionHandler = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				bm.openBook((BookInfo) e.getHref(), shell);
				AppManager.br.post(Const.EVENT_SHOW_BOOK, null);

				super.linkActivated(e);
			}

		};
		bookSection.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (!e.getState())
					for (org.eclipse.swt.widgets.Control ctrl : bookSectionClient
							.getChildren()) {
						ctrl.dispose();
					}
				else {
					Utils.fillBooks(bookSectionClient, toolkit, shell,
							bookSectionHandler);

					bookSection.setClient(bookSectionClient);
				}
				form.reflow(true);
			}
		});

		Utils.fillBooks(bookSectionClient, toolkit, shell, bookSectionHandler);
		bookSection.setClient(bookSectionClient);
	}
}