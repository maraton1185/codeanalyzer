package ebook.module.book;

import java.io.File;

import ebook.core.App;
import ebook.core.interfaces.IBookClipboard;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.tree.SectionInfoSelection;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class BookClipboard implements IBookClipboard {

	boolean cut = false;

	boolean empty = true;

	File zipFile;
	BookConnection book;
	SectionInfo section;

	@Override
	public boolean isEmpty() {
		return empty;
	}

	@Override
	public File getZip() {
		return zipFile;
	}

	@Override
	public void doPaste() {
		if (cut) {

			SectionInfoSelection sel = new SectionInfoSelection();
			sel.add(section);
			book.srv().delete(sel);
		}

		empty = true;

		App.br.post(Events.EVENT_UPDATE_LABELS_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section));

	}

	@Override
	public void setCut(File zipFile, BookConnection book, SectionInfo section) {
		empty = false;
		cut = true;
		this.zipFile = zipFile;
		this.book = book;
		this.section = section;

		App.br.post(Events.EVENT_UPDATE_LABELS_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section));
	}

	@Override
	public void setCopy(File zipFile, BookConnection book, SectionInfo section) {
		empty = false;
		cut = false;
		this.zipFile = zipFile;
		this.book = book;
		this.section = section;

		App.br.post(Events.EVENT_UPDATE_LABELS_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, section));
	}

	@Override
	public Integer getBookId() {
		if (empty)
			return null;

		if (book == null)
			return null;
		return book.getTreeItem().getId();
	}

	@Override
	public Integer getCopyId() {
		if (empty)
			return null;

		if (cut)
			return null;

		if (section == null)
			return null;
		return section.getId();
	}

	@Override
	public Integer getCutId() {
		if (empty)
			return null;
		if (!cut)
			return null;

		if (section == null)
			return null;

		return section.getId();
	}

}
