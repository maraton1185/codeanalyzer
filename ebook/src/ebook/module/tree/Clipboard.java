package ebook.module.tree;

import java.io.File;
import java.util.Iterator;

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

	ITreeItemSelection sel;

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

			srv.delete(sel);
		}

		empty = true;

		conName = null;

		update(sel);

	}

	@Override
	public void setCut(File zipFile, IDbConnection con, ITreeService srv,
			ITreeItemSelection sel) {

		ITreeItemSelection old = this.sel;
		empty = false;
		cut = true;
		this.zipFile = zipFile;
		this.con = con;
		this.srv = srv;
		this.sel = sel;

		update(old);
		update(sel);

	}

	@Override
	public void setCopy(File zipFile, IDbConnection con, ITreeItemSelection sel) {

		ITreeItemSelection old = this.sel;

		empty = false;
		cut = false;
		this.zipFile = zipFile;
		this.con = con;
		this.sel = sel;

		update(old);
		update(sel);

	}

	private void update(ITreeItemSelection sel) {
		if (sel == null)
			return;
		Iterator<ITreeItemInfo> iterator = sel.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			App.br.post(Events.EVENT_UPDATE_LABELS, new EVENT_UPDATE_VIEW_DATA(
					con, item));
		}

	}

	@Override
	public String getConnectionName() {
		if (empty)
			return null;

		if (con == null)
			return null;

		return conName == null ? con.getName() : conName;
	}

	String conName = null;

	@Override
	public void setConnectionName(String name) {
		// TODO Auto-generated method stub
		conName = name;
	}

	@Override
	public boolean isCopy(Integer _con, Integer _item) {
		if (sel == null || empty || cut)
			return false;
		Integer con = getConnectionId();
		if (con != null && !con.equals(_con))
			return false;

		return itemInSelection(_item);
	}

	@Override
	public boolean isCut(Integer _con, Integer _item) {
		if (sel == null || empty || !cut)
			return false;

		Integer con = getConnectionId();
		if (con != null && !con.equals(_con))
			return false;

		return itemInSelection(_item);
	}

	private Integer getConnectionId() {
		if (empty)
			return null;

		if (con == null)
			return null;
		return con.getTreeItem().getId();
	}

	private boolean itemInSelection(Integer _item) {

		Iterator<ITreeItemInfo> iterator = sel.iterator();
		while (iterator.hasNext()) {
			ITreeItemInfo item = iterator.next();
			if (item.getId().equals(_item))
				return true;
		}
		return false;

	}

}
