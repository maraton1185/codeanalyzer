package ebook.module.book.model;

import java.util.ArrayList;
import java.util.List;

import ebook.module.db.DbOptions;
import ebook.module.tree.ITreeService;

public class BookOptions extends DbOptions {

	private static final long serialVersionUID = 1667006960969526361L;

	public List<Integer> openSections = new ArrayList<Integer>();

	public Integer selectedSection = ITreeService.rootId;
}
