package codeanalyzer.module.tree;

import codeanalyzer.core.models.DbOptions;

public interface ITreeItemInfo {

	Integer getId();

	void setTitle(String string);

	String getTitle();

	boolean isGroup();

	String getSuffix();

	@Override
	boolean equals(Object obj);

	Integer getParent();

	DbOptions getOptions();
}
