package ebook.module.text;

import ebook.core.interfaces.IDbConnection;
import ebook.module.tree.ITreeItemInfo;

public class TextConnection {

	IDbConnection con;
	ITreeItemInfo item;

	public IDbConnection getCon() {
		return con;
	}

	public ITreeItemInfo getItem() {
		return item;
	}

	public TextConnection(IDbConnection con, ITreeItemInfo item) {
		super();
		this.con = con;
		this.item = item;
	}

	public boolean isValid() {
		return con != null && item != null;
	}
}
