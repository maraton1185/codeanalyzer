package ebook.module.book.views.interfaces;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.Section;

public interface IBlockTune {

	void tune(FormToolkit toolkit, Section section, Composite sectionClient);

}
