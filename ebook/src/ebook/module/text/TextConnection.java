package ebook.module.text;

import ebook.core.pico;
import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.ConfService;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.text.interfaces.ITextService;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.LineInfo;
import ebook.module.text.service.ConfTextService;
import ebook.module.text.service.ContextTextService;

public class TextConnection {

	IDbConnection con;
	ITextTreeService srv;

	ContextInfo item;
	ContextInfo parent;

	Object activated = new Object();

	LineInfo line;

	boolean canOpen = true;

	public TextConnection(IDbConnection con, ContextInfo item,
			ITextTreeService srv) {
		super();
		this.con = con;
		this.srv = srv;

		if (isConf())
			try {
				srv = pico.get(ICfServices.class).build(srv.getConnection());
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				srv = null;
			}
		this.item = new ContextInfo(item);
		if (!srv().setItemId(item))
			canOpen = false;

	}

	public LineInfo getLine() {
		return line;
	}

	public void setLine(LineInfo line) {
		this.line = line;
	}

	public ContextInfo getParent() {
		return parent;
	}

	// public void setParent(ContextInfo parent) {
	// this.parent = parent;
	// }

	public void setItem(ContextInfo item) {
		this.item = item;
		activated = new Object();
		ContextInfo parentItem = srv().getParent(item);
		parent = parentItem;

		if (parentItem != null) {
			ContextInfoOptions opt1 = parentItem.getOptions();
			if (opt1.type != BuildType.module)
				parent = null;
		}

	}

	public void setActivated(Object activated) {
		this.activated = activated;
	}

	public Object getActivated() {
		return activated;
	}

	private boolean isConf() {
		return srv instanceof ConfService;
	}

	public ITextTreeService getSrv() {
		return srv;
	}

	public IDbConnection getCon() {
		return con;
	}

	public ContextInfo getItem() {
		return item;
	}

	public boolean isValid() {
		return con != null && item != null && srv != null && canOpen;
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
