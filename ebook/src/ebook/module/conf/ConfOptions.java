package ebook.module.conf;

import java.util.Date;

import ebook.core.models.DbOptions;
import ebook.module.confLoad.model.DbState;
import ebook.module.tree.ITreeService;

public class ConfOptions extends DbOptions {

	private static final long serialVersionUID = 1667006960969526361L;

	// public List<Integer> openSections = new ArrayList<Integer>();

	public Integer selectedSection = ITreeService.rootId;

	public DbState status = DbState.notLoaded;
	public Date status_date;
	public DbState link_status = DbState.notLoaded;
	public Date link_status_date;
}