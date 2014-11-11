package ebook.module.book.views.interfaces;

import org.eclipse.swt.layout.GridData;

public interface ITextEditor {

	String getText();

	void updateUrl();

	void setLayoutData(GridData gridData);

	void addSectionLink(Integer id, Integer id2, String string, String title);

	void addLink(Integer id, String title);

}
