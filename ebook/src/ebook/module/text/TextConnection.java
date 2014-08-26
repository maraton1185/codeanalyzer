package ebook.module.text;

import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.ConfService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.interfaces.ITextService;
import ebook.module.text.service.ConfTextService;
import ebook.module.text.service.ContextTextService;
import ebook.module.tree.ITreeService;

public class TextConnection {

	IDbConnection con;
	ContextInfo item;
	ContextInfo parent;

	public ContextInfo getParent() {
		return parent;
	}

	public void setParent(ContextInfo parent) {
		this.parent = parent;
	}

	public void setItem(ContextInfo item) {
		this.item = item;
	}

	ITreeService srv;

	private boolean isConf() {
		return srv instanceof ConfService;
	}

	public ITreeService getSrv() {
		return srv;
	}

	public IDbConnection getCon() {
		return con;
	}

	public ContextInfo getItem() {
		return item;
	}

	public TextConnection(IDbConnection con, ContextInfo item, ITreeService srv) {
		super();
		this.con = con;
		this.item = new ContextInfo(item);
		this.srv = srv;
	}

	public boolean isValid() {
		return con != null && item != null && srv != null;
	}

	ConfTextService cnf;
	ContextTextService cont;

	public ITextService srv() {

		ITextService service;
		if (isConf())
			service = cnf == null ? new ConfTextService(this) : cnf;
		else
			service = cont == null ? new ContextTextService(this) : cont;
		service.setItem(item);
		return service;

	}
}
