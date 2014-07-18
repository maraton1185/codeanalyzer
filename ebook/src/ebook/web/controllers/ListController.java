package ebook.web.controllers;

import java.util.ArrayList;
import java.util.List;

import ebook.core.App;
import ebook.module.bookList.BookListService;
import ebook.module.bookList.tree.ListBookInfo;
import ebook.module.bookList.tree.ListBookInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.web.model.Book;
import ebook.web.model.ListModel;
import ebook.web.model.ModelItem;

public class ListController {

	BookListService srv;

	public ListController(BookListService srv) {
		super();
		this.srv = srv;
	}

	public ListModel getModel(String book_id) {
		ListModel model = new ListModel();

		Integer id;
		try {
			id = Integer.parseInt(book_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ITreeItemInfo treeItem = srv.get(id);
		if (treeItem == null)
			return null;

		// String host = App.getJetty().book(treeItem.getId());
		// model.url = App.getJetty().list();
		// model.aboutUrl = App.getJetty().info();
		model.title = treeItem.getTitle();
		// model.brand = PreferenceSupplier.get(PreferenceSupplier.APP_BRAND);

		model.parents = new ArrayList<ModelItem>();
		ITreeItemInfo parent = srv.get(treeItem.getParent());
		while (parent != null) {

			ModelItem item = new ModelItem();
			item.title = parent.getTitle();
			item.url = App.getJetty().list(parent.getId());
			item.id = parent.getId();

			model.parents.add(0, item);

			ITreeItemInfo current = parent;
			parent = srv.get(current.getParent());
		}

		model.books = new ArrayList<Book>();

		List<ITreeItemInfo> list = srv.getChildren(id);

		for (ITreeItemInfo item : list) {

			Book book = new Book();

			book.title = item.getTitle();
			book.description = ((ListBookInfoOptions) item.getOptions()).description;

			book.hasImage = true;
			boolean hasImage = ((ListBookInfo) item).hasImage();
			if (hasImage)
				book.image = App.getJetty().bookListImage(item.getId());
			else {
				if (item.isGroup())
					book.image = "/tmpl/list/img/group.png";
				else
					book.image = "/tmpl/list/img/book.png";
			}

			if (item.isGroup())
				book.url = App.getJetty().list(item.getId());
			else
				book.url = App.getJetty().book(item.getId());

			model.books.add(book);
		}

		return model;
	}
}
