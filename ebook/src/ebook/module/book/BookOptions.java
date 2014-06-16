package ebook.module.book;

import java.util.ArrayList;
import java.util.List;

import ebook.core.models.DbOptions;
import ebook.module.tree.ITreeService;

public class BookOptions extends DbOptions {

	private static final long serialVersionUID = 1667006960969526361L;

	public List<Integer> openSections = new ArrayList<Integer>();

	public Integer selectedSection = ITreeService.rootId;
}
