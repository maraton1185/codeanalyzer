package codeanalyzer.module.books;

import java.util.List;

import codeanalyzer.core.models.DbOptions;
import codeanalyzer.module.tree.ITreeService;

public class BookOptions extends DbOptions {

	private static final long serialVersionUID = 1667006960969526361L;

	public List<Integer> openSections;

	public Integer selectedSection = ITreeService.rootId;
}
