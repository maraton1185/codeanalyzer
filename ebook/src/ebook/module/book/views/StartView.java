package ebook.module.book.views;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.views.section.tools.BrowserComposite;
import ebook.utils.PreferenceSupplier;

public class StartView {

	FormToolkit toolkit;
	ScrolledForm form;

	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	BookConnection book;

	@Inject
	public StartView() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		// ImageHyperlink link;
		// Hyperlink hlink;
		// Button button;
		// Label label;
		//
		// toolkit = new FormToolkit(parent.getDisplay());
		// form = toolkit.createScrolledForm(parent);
		// form.setSize(448, 377);
		// form.setLocation(0, 0);
		// ColumnLayout layout = new ColumnLayout();
		// layout.maxNumColumns = 2;
		// form.getBody().setLayout(layout);
		//
		// form.setText("Очень просто)");
		// form.setText(book.getFullName());

		parent.setLayout(new FillLayout());

		parent.setFont(new Font(parent.getDisplay(), PreferenceSupplier
				.getFontData(PreferenceSupplier.FONT)));

		new BrowserComposite(parent, App.getJetty().info());

	}

}