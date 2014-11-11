package ebook.module.tree.item;

import org.eclipse.swt.graphics.Image;

import ebook.module.db.DbOptions;

public interface ITreeItemInfo {

	Integer getId();

	void setTitle(String string);

	String getTitle();

	boolean isTitleIncrement();

	boolean isGroup();

	String getSuffix();

	@Override
	boolean equals(Object obj);

	Integer getParent();

	DbOptions getOptions();

	// <T> T getOptions(Class<T> clazz);

	void setOptions(DbOptions options);

	// void setOptions(DbOptions value);

	void setParent(int value);

	void setId(int value);

	void setGroup(boolean value);

	void setListImage(Image img);

	Image getListImage();

	void setACL();

	int getSort();

	void setSort(int sort);

	boolean isRoot();

	void setRoot(boolean value);

}
