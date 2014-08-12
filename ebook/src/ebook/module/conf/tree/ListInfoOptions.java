package ebook.module.conf.tree;

import ebook.module.db.DbOptions;
import ebook.module.tree.ITreeService;

public class ListInfoOptions extends DbOptions {

	private static final long serialVersionUID = -8134048308726133820L;

	public Integer selectedContext = ITreeService.rootId;
}
