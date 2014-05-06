package codeanalyzer.views.books;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import codeanalyzer.book.BookInfo;
import codeanalyzer.book.BookSection;
import codeanalyzer.tools.TinyTextEditor;

public interface ISectionBlockComposite {

	int numColumns = 2;
	int groupWidth = 300;

	public abstract void init(FormToolkit toolkit, Composite body,
			ScrolledForm form, BookInfo book);

	public abstract void render(BookSection sec);

	void setBlockView(Boolean blockView);

	TinyTextEditor getTinymce();

	public void renderGroups(BookSection section);

}