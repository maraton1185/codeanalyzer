package ebook.core.interfaces;

import java.io.File;

import ebook.module.tree.ITreeItemSelection;
import ebook.module.tree.ITreeService;

public interface IClipboard {

	boolean isEmpty();

	File getZip();

	void setCut(File zipFile, IDbConnection con, ITreeService srv,
			ITreeItemSelection sel);

	void setCopy(File zipFile, IDbConnection con, ITreeItemSelection sel);

	boolean isCopy(Integer con, Integer item);

	boolean isCut(Integer con, Integer item);

	// Integer getConnectionId();
	//
	// Integer getCopyId();
	//
	// Integer getCutId();

	void doPaste();

	String getConnectionName();

}
