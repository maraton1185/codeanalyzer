package codeanalyzer.core.components;

public interface ITreeItemInfo {

	Integer getId();

	void setTitle(String string);

	String getTitle();

	boolean isGroup();

	String getSuffix();

	@Override
	boolean equals(Object obj);

	Integer getParent();
}
