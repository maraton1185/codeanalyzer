package ebook.module.text.interfaces;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.model.LineInfo;

public interface ITextService {

	void setItem(ContextInfo item);

	void saveItemText(String string);

	String getItemText(ContextInfo item);

	ContextInfo getItemByTitle(LineInfo selected);

	ContextInfo getParent(ContextInfo item);

	boolean readOnly(ContextInfo item);

	boolean setItemId(ContextInfo item);

}
