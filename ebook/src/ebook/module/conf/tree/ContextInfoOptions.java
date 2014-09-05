package ebook.module.conf.tree;

import ebook.module.conf.model.BuildType;
import ebook.module.db.DbOptions;

public class ContextInfoOptions extends DbOptions {

	private static final long serialVersionUID = -8134048308726133820L;

	public String conf;

	public BuildType type;

	public Integer start_offset;

	public String proc;

	public boolean compare = false;

	public boolean openInComparison = false;

	public boolean isSearch() {
		return start_offset != null;
	}

}
