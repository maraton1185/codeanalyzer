package ebook.module.book.views.interfaces;

import java.util.HashMap;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ImageHyperlink;

import ebook.module.book.service.BookService;
import ebook.module.book.tree.SectionImage;
import ebook.module.book.tree.SectionInfo;

public interface IPictureTuneData {

	Composite getImagesComposite();

	HashMap<Composite, Listener> getListeners();

	FormToolkit getToolkit();

	BookService srv();

	void reflow();

	void addImage(SectionInfo section);

	void expand();

	void collapse();

	void rename(SectionInfo section);

	void moveUp(ImageHyperlink imageHyperlink);

	void moveDown(ImageHyperlink imageHyperlink);

	void saveOrder();

	void addImageBelow(SectionInfo section, Composite sectionClient);

	void addLink(SectionImage image);

	boolean textEdit();

}
