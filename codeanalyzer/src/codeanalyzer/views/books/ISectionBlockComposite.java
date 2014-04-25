package codeanalyzer.views.books;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

public interface ISectionBlockComposite {

	public abstract void init(FormToolkit toolkit, Composite body,
			ScrolledForm form, int numColumns);
	public abstract void render();

}