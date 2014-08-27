package ebook.module.text.service;

import java.util.List;

import ebook.core.pico;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.text.TextConnection;
import ebook.module.text.interfaces.ITextService;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ITreeItemInfo;

public abstract class TextService implements ITextService {

	// private TextConnection con;
	protected ICfServices cf = pico.get(ICfServices.class);
	protected ContextInfo item;
	protected ITextTreeService srv;

	public TextService(TextConnection con) {
		item = con.getItem();
		srv = con.getSrv();
	}

	@Override
	public void setItem(ContextInfo item) {
		this.item = item;
	}

	@Override
	public void saveItemText(String text) {

		srv.saveText(item.getId(), text);

	}

	@Override
	public String getItemText(ContextInfo item) {

		ContextInfoOptions opt = item.getOptions();
		if (opt.type == BuildType.module)

			return getModuleText();

		else

			return getText();

	}

	protected String getModuleText() {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	protected String getText() {

		return srv.getText(item.getId());
	}

	@Override
	public ContextInfo getItemByTitle(LineInfo selected) {
		ITreeItemInfo info = srv
				.findInParent(selected.getTitle(), item.getId());

		return (ContextInfo) info;
	}

	@Override
	public ContextInfo getParent(ContextInfo item) {
		return (ContextInfo) srv.get(item.getParent());
	}

}
