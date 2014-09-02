package ebook.module.text.interfaces;

import java.util.List;

import ebook.module.tree.ITreeItemInfo;
import ebook.module.tree.ITreeService;

public interface IBookmarkService extends ITreeService {

	List<ITreeItemInfo> getBookmarks(int item);

}
