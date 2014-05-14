package codeanalyzer.views.books;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.ColumnLayout;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.ImageHyperlink;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.books.book.CurrentBookInfo;

public class StartView {

	FormToolkit toolkit;
	ScrolledForm form;

	// Section bookSection;
	// Composite bookSectionClient;
	// HyperlinkAdapter bookSectionHandler;

	@Inject
	@Active
	CurrentBookInfo book;

	@Inject
	public StartView() {
		// TODO Your code here
	}

	@PostConstruct
	public void postConstruct(Composite parent) {

		ImageHyperlink link;
		Hyperlink hlink;
		Button button;
		Label label;

		toolkit = new FormToolkit(parent.getDisplay());
		form = toolkit.createScrolledForm(parent);
		form.setSize(448, 377);
		form.setLocation(0, 0);
		ColumnLayout layout = new ColumnLayout();
		layout.maxNumColumns = 2;
		form.getBody().setLayout(layout);

		form.setText("Очень просто)");
		// form.setText(book.getFullName());
	}

}