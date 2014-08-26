package ebook.module.text.service;

import java.util.List;

import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ITreeItemInfo;

public class ContextTextService extends TextService {

	public ContextTextService(TextConnection con) {
		super(con);
	}

	@Override
	public void saveItemText(String text) {

		srv.saveText(item.getId(), text);

	}

	@Override
	public String getItemText() {

		ContextInfoOptions opt = (ContextInfoOptions) item.getOptions();
		if (opt.type == BuildType.module)

			return getModuleText();
		else

			return srv.getText(item.getId());

	}

	private String getModuleText() {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	@Override
	public ContextInfo getItemByTitle(LineInfo selected) {
		ITreeItemInfo info = srv
				.findInParent(selected.getTitle(), item.getId());

		return (ContextInfo) info;
	}

}
