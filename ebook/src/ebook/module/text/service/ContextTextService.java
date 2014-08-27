package ebook.module.text.service;

import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.text.TextConnection;

public class ContextTextService extends TextService {

	public ContextTextService(TextConnection con) {
		super(con);
	}

	@Override
	public boolean readOnly(ContextInfo item) {
		ContextInfoOptions opt = item.getOptions();
		return opt != null && opt.type == BuildType.module;
	}

	@Override
	public boolean setItemId(ContextInfo item) {
		return true;
	}

}
