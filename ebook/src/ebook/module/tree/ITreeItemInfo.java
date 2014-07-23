package ebook.module.tree;

import org.eclipse.swt.graphics.Image;

import ebook.core.models.DbOptions;

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

	void setOptions(DbOptions options);

	// void setOptions(DbOptions value);

	void setParent(int value);

	void setId(int value);

	void setGroup(boolean value);

	void setListImage(Image img);

	Image getListImage();

	void setACL();
}
