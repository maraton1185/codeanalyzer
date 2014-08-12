package ebook.module.confList.tree;

import java.util.Date;

import ebook.module.confLoad.model.DbState;
import ebook.module.db.DbOptions;

public class ListConfInfoOptions extends DbOptions {

	private static final long serialVersionUID = 972083111087138872L;

	public String description = "";

	public DbState status = DbState.notLoaded;
	public Date status_date;
	public DbState link_status = DbState.notLoaded;
	public Date link_status_date;

	public ConfInfo info;

	public ListConfInfoOptions() {
		super();
		this.info = new ConfInfo();
	}

}
