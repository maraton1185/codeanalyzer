package ebook.module.text;

import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.ConfTreeService;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.LineInfo;
import ebook.module.text.service.TextService;

public class TextConnection {

	IDbConnection con;
	ITextTreeService srv;

	ContextInfo item;
	ContextInfo module;

	Object activated = new Object();

	LineInfo line;

	boolean canOpen = true;

	public TextConnection(IDbConnection con, ContextInfo item,
			ITextTreeService srv) {
		super();
		this.con = con;
		this.srv = srv;
		this.item = item;

	}

	public LineInfo getLine() {
		return line;
	}

	public void setLine(LineInfo line) {
		this.line = line;
	}

	public ContextInfo getModule() {
		return module;
	}

	// public void setParent(ContextInfo parent) {
	// this.parent = parent;
	// }

	public void setItem(ContextInfo item) {
		this.item = item;
		activated = new Object();
		ContextInfo moduleItem = srv().getModule(item);
		module = moduleItem;

		if (moduleItem != null) {
			ContextInfoOptions opt1 = moduleItem.getOptions();
			if (opt1.type != BuildType.module)
				module = null;
		}

	}

	public void setActivated(Object activated) {
		this.activated = activated;
	}

	public Object getActivated() {
		return activated;
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
		return con != null && item != null && srv != null && item.canOpen;
	}

	public boolean isConf() {
		return srv instanceof ConfTreeService;
	}

	TextService text;

	public TextService srv() {

		text = text == null ? new TextService(this) : text;
		text.setItem(item);
		return text;

	}
}
