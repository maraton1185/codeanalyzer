package ebook.module.book.views.interfaces;

import org.eclipse.ui.forms.widgets.FormToolkit;

import ebook.module.book.service.BookService;
import ebook.module.book.views.tools.TextEdit;

public interface ITextImagesView {

	FormToolkit getToolkit();

	BookService srv();

	void setDirty();

	void executeHandler(String model);

	TextEdit getTextEditor();

	boolean textEdit();

}
