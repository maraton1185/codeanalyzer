package ebook.module.confList.tree;

import java.util.Date;

import ebook.core.models.DbOptions;

public class ListConfInfoOptions extends DbOptions {

	public static enum DbState {
		notLoaded, Loaded;
	}

	/**
	 * 
	 */
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
