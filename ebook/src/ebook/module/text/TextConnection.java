package ebook.module.text;

import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.ConfService;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public class TextConnection {

	IDbConnection con;
	ITreeItemInfo item;
	ITreeService srv;

	public boolean isConf() {
		return srv instanceof ConfService;
	}

	public ITreeService getSrv() {
		return srv;
	}

	public IDbConnection getCon() {
		return con;
	}

	public ITreeItemInfo getItem() {
		return item;
	}

	public TextConnection(IDbConnection con, ITreeItemInfo item,
			ITreeService srv) {
		super();
		this.con = con;
		this.item = item;
		this.srv = srv;
	}

	public boolean isValid() {
		return con != null && item != null && srv != null;
	}
}
