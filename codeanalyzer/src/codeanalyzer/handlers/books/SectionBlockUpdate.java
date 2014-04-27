 
package codeanalyzer.handlers.books;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;

import codeanalyzer.views.books.SectionsBlockView;

public class SectionBlockUpdate {
	@Execute
	public void execute(@Active MPart part) {
		Object o = part.getObject();
		if (o instanceof SectionsBlockView)
			((SectionsBlockView) o).reflow();
	}
		
}