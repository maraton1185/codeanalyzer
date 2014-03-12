package codeanalyzer.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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

import codeanalyzer.book.BookInfo;
import codeanalyzer.core.interfaces.IBookManager;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class BooksView {

	FormToolkit toolkit;

	@Inject
	public BooksView() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent, final IBookManager bm,
			final Shell shell) {

		// ImageHyperlink link;

		toolkit = new FormToolkit(parent.getDisplay());
		final ScrolledForm form = toolkit.createScrolledForm(parent);
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText(Strings.get("appTitle"));

		final Section section = toolkit.createSection(form.getBody(),
		// Section.DESCRIPTION | Section.TITLE_BAR | Section.TWISTIE |
		// Section.EXPANDED
				Section.TITLE_BAR | Section.EXPANDED | Section.TWISTIE);

		section.setText("Список книг");
		final Composite sectionClient = toolkit.createComposite(section);
		sectionClient.setLayout(new GridLayout());
		final HyperlinkAdapter handler = new HyperlinkAdapter() {
			@Override
			public void linkActivated(HyperlinkEvent e) {
				bm.openBook((BookInfo) e.getHref(), shell);
				super.linkActivated(e);
			}

		};
		section.addExpansionListener(new ExpansionAdapter() {
			@Override
			public void expansionStateChanged(ExpansionEvent e) {
				if (!e.getState())
					for (org.eclipse.swt.widgets.Control ctrl : sectionClient
							.getChildren()) {
						ctrl.dispose();
					}
				else {
					Utils.fillBooks(bm, sectionClient, toolkit, shell, handler);

					section.setClient(sectionClient);
				}
				form.reflow(true);
			}
		});

		Utils.fillBooks(bm, sectionClient, toolkit, shell, handler);
		section.setClient(sectionClient);
	}
}