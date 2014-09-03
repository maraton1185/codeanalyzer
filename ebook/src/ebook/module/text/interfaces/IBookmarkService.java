package ebook.module.text.interfaces;

import java.util.List;

import ebook.module.text.tree.BookmarkInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public interface IBookmarkService extends ITreeService {

	List<ITreeItemInfo> getBookmarks(String path);

	BookmarkInfo getBookmark(BookmarkInfo data);

	void removeBookmark(BookmarkInfo item);

}
