package ebook.core.interfaces;

import java.io.File;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;

public interface IBookClipboard {

	boolean isEmpty();

	File getZip();

	void setCut(File zipFile, BookConnection book, SectionInfo section);

	void setCopy(File zipFile, BookConnection book, SectionInfo section);

	Integer getBookId();

	Integer getCopyId();

	Integer getCutId();

	void doPaste();

}
