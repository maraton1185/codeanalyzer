package ebook.module.text.service;

import ebook.core.pico;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.text.TextConnection;
import ebook.module.text.interfaces.ITextService;
import ebook.module.tree.ITreeService;

public abstract class TextService implements ITextService {

	// private TextConnection con;
	protected ICfServices cf = pico.get(ICfServices.class);
	protected ContextInfo item;
	protected ITreeService srv;

	public TextService(TextConnection con) {
		item = con.getItem();
		srv = con.getSrv();
	}

	@Override
	public void setItem(ContextInfo item) {
		this.item = item;
	}

	// @Override
	// public ContextInfo get(Integer parent) {
	// return (ContextInfo) srv.get(parent);
	// }

	@Override
	public void getItemPath(ContextInfo item) {

	}

}
