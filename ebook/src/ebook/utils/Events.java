package ebook.utils;

import ebook.core.interfaces.IDbConnection;
import ebook.module.tree.ITreeItemInfo;

public abstract class Events {

	// Events *******************************************************
	public static final String EVENT_UPDATE_STATUS = "update_status";

	public static final String EVENT_PROGRESS_WORKED = "EVENT_PROGRESS_WORKED";
	public static final String EVENT_PROGRESS_BEGIN_TASK = "EVENT_PROGRESS_BEGIN_TASK";
	public static final String EVENT_PROGRESS_DONE = "EVENT_PROGRESS_DONE";
	public static final String EVENT_PROGRESS_ERROR = "EVENT_PROGRESS_ERROR";

	// public static final String EVENT_UPDATE_CONFIG_LIST =
	// "update_config_list";

	public static final String EVENT_SHOW_BOOK = "EVENT_SHOW_BOOK";
	public static final String EVENT_UPDATE_BOOK_INFO = "EVENT_UPDATE_BOOK_INFO";
	public static final String EVENT_UPDATE_BOOK_LIST = "EVENT_UPDATE_BOOK_LIST";
	public static final String EVENT_UPDATE_CONTENT_VIEW = "EVENT_UPDATE_CONTENT_VIEW";
	public static final String EVENT_UPDATE_SECTION_VIEW = "EVENT_UPDATE_SECTION_VIEW";

	public static final String EVENT_SHOW_CONF = "EVENT_SHOW_CONF";

	public static final String EVENT_EDIT_TITLE_CONTENT_VIEW = "EVENT_EDIT_TITLE_CONTENT_VIEW";
	public static final String EVENT_SET_SECTIONVIEW_DIRTY = "EVENT_SET_SECTIONVIEW_DIRTY";
	public static final String CONTEXT_ACTIVE_VIEW_SECTION = "CONTEXT_ACTIVE_VIEW_SECTION";
	public static final String EVENT_UPDATE_SECTION_BLOCK_VIEW = "EVENT_UPDATE_SECTION_BLOCK_VIEW";
	public static final String EVENT_EDIT_TITLE_BOOK_LIST = "EVENT_EDIT_TITLE_BOOK_LIST";
	public static final String EVENT_EDIT_TITLE_USERS_LIST = "EVENT_EDIT_TITLE_USERS_LIST";
	public static final String EVENT_UPDATE_USERS = "EVENT_UPDATE_USERS";

	public static final String EVENT_UPDATE_USER_INFO = "EVENT_UPDATE_USER_INFO";

	public static final String EVENT_UPDATE_USER_ROLES = "EVENT_UPDATE_USER_ROLES";

	public static final String EVENT_BOOK_LIST_SET_SELECTION = "EVENT_BOOK_LIST_SET_SELECTION";

	public static final String EVENT_USER_LIST_SET_SELECTION = "EVENT_USER_LIST_SET_SELECTION";

	public static final String EVENT_START_JETTY = "EVENT_START_JETTY";
	public static final String EVENT_STOP_JETTY = "EVENT_STOP_JETTY";

	public static final String EVENT_UPDATE_CONF_LIST = "EVENT_UPDATE_CONF_LIST";

	public static final String EVENT_EDIT_TITLE_CONF_LIST = "EVENT_EDIT_TITLE_CONF_LIST";

	public static final String EVENT_CONF_LIST_SET_SELECTION = "EVENT_CONF_LIST_SET_SELECTION";

	public static final String EVENT_UPDATE_CONF_INFO = "EVENT_UPDATE_CONF_INFO";

	public static final String EVENT_UPDATE_PERSPECTIVE_ICON = "EVENT_UPDATE_PERSPECTIVE_ICON";

	public static final String EVENT_UPDATE_LABELS_BOOK_LIST = "EVENT_UPDATE_LABELS_BOOK_LIST";

	public static final String EVENT_UPDATE_LABELS = "EVENT_UPDATE_LABELS_CONTENT_VIEW";

	public static final String EVENT_UPDATE_SECTION_INFO = "EVENT_UPDATE_SECTION_INFO";

	public static final String EVENT_HIDE_BOOK_ROLES = "EVENT_HIDE_BOOK_ROLES";

	public static final String RESTART_WORKBENCH = "RESTART_WORKBENCH";

	// public static final String INSTALL_UPDATE = "INSTALL_UPDATE";

	public static final String SHOW_UPDATE_AVAILABLE = "SHOW_UPDATE_AVAILABLE";

	public static final String SHOW_ABOUT = "SHOW_ABOUT";

	public static final String EVENT_UPDATE_CONF_VIEW = "EVENT_UPDATE_CONF_VIEW";

	public static final String EVENT_EDIT_TITLE_CONF_VIEW = "EVENT_EDIT_TITLE_CONTEXT_VIEW";

	public static final String EVENT_UPDATE_LABELS_CONF_VIEW = "EVENT_UPDATE_LABELS_CONF_VIEW";

	public static final String EVENT_CONF_VIEW_SETSELECTION = "EVENT_CONF_VIEW_SETSELECTION";

	public static final String EVENT_HIDE_BOOK_PANEL = "EVENT_HIDE_BOOK_PANEL";

	public static final String EVENT_HIDE_BOOK_CONTEXT = "EVENT_HIDE_BOOK_CONTEXT";

	public static final String EVENT_UPDATE_CONTEXT_VIEW = "EVENT_UPDATE_SECTION_CONTEXT_VIEW";

	public static final String EVENT_UPDATE_LIST_VIEW = "EVENT_UPDATE_LIST_VIEW";

	public static final String EVENT_EDIT_TITLE_LIST_VIEW = "EVENT_EDIT_TITLE_LIST_VIEW";

	public static final String CONTEXT_ACTIVE_LIST = "CONTEXT_ACTIVE_LIST";

	// public static final String EVENT_UPDATE_CONF_CONTEXT_PART =
	// "EVENT_UPDATE_CONF_CONTEXT_PART";

	public static class EVENT_UPDATE_VIEW_DATA {

		public EVENT_UPDATE_VIEW_DATA(IDbConnection con, ITreeItemInfo section,
				ITreeItemInfo parent, ITreeItemInfo selected) {
			super();
			this.con = con;
			this.parent = parent;
			this.selected = selected;
			this.section = section;
		}

		public EVENT_UPDATE_VIEW_DATA(IDbConnection con, ITreeItemInfo parent,
				ITreeItemInfo selected) {
			super();
			this.con = con;
			this.parent = parent;
			this.selected = selected;
			// onlySectionView = false;
		}

		public EVENT_UPDATE_VIEW_DATA(IDbConnection con, ITreeItemInfo parent) {
			this.con = con;
			this.parent = parent;
			// this.onlySectionView = onlySectionView;
		}

		public IDbConnection con;
		public ITreeItemInfo parent;
		public ITreeItemInfo selected;
		public ITreeItemInfo section;
		// public boolean setBook;
		// public boolean onlySectionView;

	}

	public static class EVENT_UPDATE_TREE_DATA {

		public EVENT_UPDATE_TREE_DATA(ITreeItemInfo parent,
				ITreeItemInfo selected) {
			super();
			// this.book = book;
			this.parent = parent;
			this.selected = selected;
		}

		// public CurrentBookInfo book;
		public ITreeItemInfo parent;
		public ITreeItemInfo selected;

	}

}
