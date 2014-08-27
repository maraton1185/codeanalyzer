package ebook.module.text.interfaces;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.model.LineInfo;

public interface ITextService {

	void setItem(ContextInfo item);

	void saveItemText(String string);

	String getItemText(ContextInfo item);

	ContextInfo getItemByTitle(LineInfo selected);

	// ContextInfo get(Integer parent);

	void getItemPath(ContextInfo item);

	ContextInfo getParent(ContextInfo item);

	boolean readOnly(ContextInfo item);

	void setItemId(ContextInfo item);

}
