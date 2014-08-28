package ebook.module.text.service;

import java.util.List;

import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.TextConnection;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ITreeItemInfo;

public class TextService {

	protected ContextInfo item;
	protected ITextTreeService srv;

	public TextService(TextConnection con) {
		item = con.getItem();
		srv = con.getSrv();
	}

	public void setItem(ContextInfo item) {
		this.item = item;
	}

	public boolean readOnly(ContextInfo item) {
		ContextInfoOptions opt = item.getOptions();
		return opt != null && opt.type == BuildType.module;
	}

	public void saveItemText(String text) {

		srv.saveText(item.getId(), text);

	}

	public String getItemText(ContextInfo item) {

		ContextInfoOptions opt = item.getOptions();
		if (opt.type == BuildType.module)

			return getModuleText(item);

		else

			return getText(item);

	}

	protected String getModuleText(ContextInfo item) {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	protected String getText(ContextInfo item) {

		return srv.getText(item.getId());
	}

	public ContextInfo getItemByTitle(LineInfo selected) {
		ITreeItemInfo info = srv
				.findInParent(selected.getTitle(), item.getId());

		return (ContextInfo) info;
	}

	public ContextInfo getParent(ContextInfo item) {
		return (ContextInfo) srv.getParent(item);
	}

}
