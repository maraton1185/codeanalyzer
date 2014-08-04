package ebook.core.interfaces;

import java.io.File;

import ebook.module.tree.ITreeItemInfo;

public interface IClipboard {

	boolean isEmpty();

	File getZip();

	void setCut(File zipFile, IDbConnection con, ITreeItemInfo item);

	void setCopy(File zipFile, IDbConnection con, ITreeItemInfo item);

	Integer getConnectionId();

	Integer getCopyId();

	Integer getCutId();

	void doPaste();

}
