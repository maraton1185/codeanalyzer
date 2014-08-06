package ebook.module.tree;

import java.io.File;

import ebook.core.App;
import ebook.core.interfaces.IClipboard;
import ebook.core.interfaces.IDbConnection;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class Clipboard implements IClipboard {

	boolean cut = false;

	boolean empty = true;

	File zipFile;
	IDbConnection con;

	ITreeItemInfo item;

	private ITreeService srv;

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
		if (cut && srv != null) {

			TreeItemInfoSelection sel = new TreeItemInfoSelection();
			sel.add(item);
			srv.delete(sel);
		}

		empty = true;

		App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(con,
				item));

	}

	@Override
	public void setCut(File zipFile, IDbConnection con, ITreeService srv,
			ITreeItemInfo item) {
		empty = false;
		cut = true;
		this.zipFile = zipFile;
		this.con = con;
		this.srv = srv;
		this.item = item;

		App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(con,
				item));
	}

	@Override
	public void setCopy(File zipFile, IDbConnection con, ITreeItemInfo item) {
		empty = false;
		cut = false;
		this.zipFile = zipFile;
		this.con = con;
		this.item = item;

		App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(con,
				item));
	}

	@Override
	public Integer getConnectionId() {
		if (empty)
			return null;

		if (con == null)
			return null;
		return con.getTreeItem().getId();
	}

	@Override
	public String getConnectionName() {
		if (empty)
			return null;

		if (con == null)
			return null;
		return con.getName();
	}

	@Override
	public Integer getCopyId() {
		if (empty)
			return null;

		if (cut)
			return null;

		if (item == null)
			return null;
		return item.getId();
	}

	@Override
	public Integer getCutId() {
		if (empty)
			return null;
		if (!cut)
			return null;

		if (item == null)
			return null;

		return item.getId();
	}

}
