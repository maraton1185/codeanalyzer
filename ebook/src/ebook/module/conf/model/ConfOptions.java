package ebook.module.conf.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ebook.module.confLoad.model.DbState;
import ebook.module.db.DbOptions;
import ebook.module.tree.service.ITreeService;

public class ConfOptions extends DbOptions {

	private static final long serialVersionUID = 1667006960969526361L;

	// public List<Integer> openSections = new ArrayList<Integer>();

	public Integer selectedSection = ITreeService.rootId;
	public List<Integer> openSections = new ArrayList<Integer>();

	public DbState status = DbState.notLoaded;
	public Date status_date;
	// public DbState link_status = DbState.notLoaded;
	// public Date link_status_date;

}
