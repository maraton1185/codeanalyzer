package ebook.utils;

import java.util.ArrayList;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.TextConnection;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.item.ITreeItemInfo;

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
	// public static final String CONTEXT_ACTIVE_VIEW_SECTION =
	// "CONTEXT_ACTIVE_VIEW_SECTION";
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

	public static final String EVENT_UPDATE_CONF_VIEW_EDIT_TITLE = "EVENT_UPDATE_CONF_VIEW_EDIT_TITLE";

	public static final String EVENT_UPDATE_LABELS_CONF_VIEW = "EVENT_UPDATE_LABELS_CONF_VIEW";

	public static final String EVENT_CONF_VIEW_SETSELECTION = "EVENT_CONF_VIEW_SETSELECTION";

	public static final String EVENT_HIDE_BOOK_PANEL = "EVENT_HIDE_BOOK_PANEL";

	public static final String EVENT_HIDE_BOOK_CONTEXT = "EVENT_HIDE_BOOK_CONTEXT";

	public static final String EVENT_UPDATE_CONTEXT_VIEW = "EVENT_UPDATE_CONTEXT_VIEW";

	public static final String EVENT_UPDATE_LIST_VIEW = "EVENT_UPDATE_LIST_VIEW";

	public static final String EVENT_EDIT_TITLE_LIST_VIEW = "EVENT_EDIT_TITLE_LIST_VIEW";

	public static final String CONTEXT_ACTIVE_LIST = "CONTEXT_ACTIVE_LIST";

	public static final String EVENT_UPDATE_CONTEXT_VIEW_EDIT_TITLE = "EVENT_UPDATE_CONTEXT_VIEW_EDIT_TITLE";

	public static final String EVENT_OPEN_TEXT = "EVENT_OPEN_TEXT";

	public static final String EVENT_TEXT_VIEW_DOUBLE_CLICK = "EVENT_TEXT_VIEW_DOUBLE_CLICK";

	public static final String EVENT_UPDATE_TEXT_MODEL = "EVENT_UPDATE_TEXT_MODEL";

	public static final String EVENT_TEXT_VIEW_OUTLINE_SELECTED = "EVENT_TEXT_VIEW_OUTLINE_SELECTED";

	public static final String EVENT_UPDATE_OUTLINE_VIEW = "EVENT_UPDATE_OUTLINE_VIEW";

	public static final String EVENT_OUTLINE_VIEW_SET_CURRENT = "EVENT_OUTLINE_VIEW_SET_CURRENT";

	public static final String TEXT_VIEW_ACTIVE_PROCEDURE = "TEXT_VIEW_ACTIVE_PROCEDURE";

	public static final String EVENT_TEXT_VIEW_UPDATE = "EVENT_TEXT_VIEW_UPDATE";

	public static final String EVENT_TEXT_VIEW_BUILD_TEXT = "EVENT_TEXT_VIEW_BUILD_TEXT";

	public static final String EVENT_TEXT_VIEW_FIND_TEXT = "EVENT_TEXT_VIEW_FIND_TEXT";

	public static final String EVENT_TEXT_VIEW_FIND_TEXT_IN_MODULE = "EVENT_TEXT_VIEW_FIND_TEXT_IN_MODULE";

	public static final String EVENT_TEXT_VIEW_REMOVE_MARKERS = "EVENT_TEXT_VIEW_REMOVE_MARKERS";

	public static final String EVENT_TEXT_VIEW_UPDATE_TEXT = "EVENT_TEXT_VIEW_UPDATE_TEXT";

	public static final String EVENT_UPDATE_BOOKMARK_VIEW_EDIT_TITLE = "EVENT_UPDATE_BOOKMARK_VIEW_EDIT_TITLE";

	public static final String EVENT_UPDATE_BOOKMARK_VIEW = "EVENT_UPDATE_BOOKMARK_VIEW";

	public static final String EVENT_TEXT_VIEW_ADD_BOOKMARK = "EVENT_TEXT_VIEW_ADD_BOOKMARK";

	public static final String EVENT_UPDATE_BOOKMARK_VIEW_FULL_UPDATE = "EVENT_UPDATE_BOOKMARK_VIEW_FULL_UPDATE";

	public static final String EVENT_TEXT_VIEW_FILL_BOOKMARKS = "EVENT_TEXT_VIEW_FILL_BOOKMARKS";

	public static final String EVENT_SET_SECTION_CONTEXT = "EVENT_SET_SECTION_CONTEXT";

	public static final String CONTEXT_PREVIEW_VIEW_BLOCK = "CONTEXT_PREVIEW_VIEW";

	public static final String EVENT_ADD_SECTION_LINK = "EVENT_ADD_SECTION_LINK";

	// public static final String CONTEXT_SECTION_TO_OPEN =
	// "CONTEXT_SECTION_TO_OPEN";

	// public static final String CONF_LIST_VIEW_COMPARISON =
	// "CONF_LIST_VIEW_COMPARISON";

	// public static final String EVENT_TEXT_VIEW_GOTO_DEFINITION =
	// "EVENT_TEXT_VIEW_GOTO_DEFINITION";

	public static class EVENT_TEXT_DATA {
		public EVENT_TEXT_DATA(TextConnection con, IDocument fDocument,
				ArrayList<ITreeItemInfo> model, ArrayList<Position> markers) {
			this.con = con;
			this.document = fDocument;
			this.model = model;
			this.markers = markers;

		}

		public EVENT_TEXT_DATA(TextConnection con, IDocument fDocument,
				LineInfo selected) {
			this.con = con;
			this.document = fDocument;
			this.selected = selected;
		}

		public EVENT_TEXT_DATA(ContextInfo parent) {
			this.parent = parent;
		}

		public EVENT_TEXT_DATA(ContextInfo item, String search) {
			this.item = item;
			this.search = search;
		}

		public IDocument document;
		public ArrayList<ITreeItemInfo> model;
		public ArrayList<Position> markers;
		public TextConnection con;
		public LineInfo selected;
		public ContextInfo parent;
		public ContextInfo item;
		public String search;
	}

	// public static final String EVENT_SHOW_CONF_LIST = "EVENT_SHOW_CONF_LIST";

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

		public EVENT_UPDATE_VIEW_DATA(IDbConnection con) {
			this.con = con;

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
