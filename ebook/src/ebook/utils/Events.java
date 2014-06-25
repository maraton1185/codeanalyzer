package ebook.utils;

import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.tree.ITreeItemInfo;

public abstract class Events {

	// Events *******************************************************
	public static final String EVENT_UPDATE_STATUS = "update_status";

	public static final String EVENT_PROGRESS_WORKED = "EVENT_PROGRESS_WORKED";
	public static final String EVENT_PROGRESS_BEGIN_TASK = "EVENT_PROGRESS_BEGIN_TASK";
	public static final String EVENT_PROGRESS_DONE = "EVENT_PROGRESS_DONE";
	public static final String EVENT_PROGRESS_ERROR = "EVENT_PROGRESS_ERROR";

	public static final String EVENT_UPDATE_CONFIG_LIST = "update_config_list";

	public static final String EVENT_SHOW_BOOK = "EVENT_SHOW_BOOK";
	public static final String EVENT_UPDATE_BOOK_INFO = "EVENT_UPDATE_BOOK_INFO";
	public static final String EVENT_UPDATE_BOOK_LIST = "EVENT_UPDATE_BOOK_LIST";
	public static final String EVENT_UPDATE_CONTENT_VIEW = "EVENT_UPDATE_CONTENT_VIEW";

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

	public static final String EVENT_UPDATE_CONF_LIST = "EVENT_UPDATE_CONF_LIST";

	public static final String EVENT_EDIT_TITLE_CONF_LIST = "EVENT_EDIT_TITLE_CONF_LIST";

	public static final String EVENT_CONF_LIST_SET_SELECTION = "EVENT_CONF_LIST_SET_SELECTION";

	public static final String EVENT_UPDATE_CONF_INFO = "EVENT_UPDATE_CONF_INFO";

	public static class EVENT_UPDATE_VIEW_DATA {

		public EVENT_UPDATE_VIEW_DATA(BookConnection book, SectionInfo parent,
				SectionInfo selected) {
			super();
			this.book = book;
			this.parent = parent;
			this.selected = selected;
			onlySectionView = false;
		}

		// public EVENT_UPDATE_VIEW_DATA(BookConnection book, SectionInfo
		// parent,
		// SectionInfo selected, boolean setBook) {
		// this(book, parent, selected);
		// this.setBook = setBook;
		// }

		public EVENT_UPDATE_VIEW_DATA(BookConnection book, SectionInfo parent,
				boolean onlySectionView) {
			this.book = book;
			this.parent = parent;
			this.onlySectionView = onlySectionView;
		}

		public BookConnection book;
		public SectionInfo parent;
		public SectionInfo selected;
		// public boolean setBook;
		public boolean onlySectionView;

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
