package ebook.web.controllers;

import java.util.ArrayList;
import java.util.List;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.BookService;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Utils;
import ebook.web.model.BookModel;
import ebook.web.model.ModelItem;
import ebook.web.model.Section;

public class BookController {

	BookConnection book;
	BookService srv;

	public BookController(BookConnection book) {
		super();
		this.book = book;
		this.srv = book.srv();
	}

	public BookModel getModel(String section_id) {

		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ITreeItemInfo treeItem = srv.get(id);
		if (treeItem == null)
			return null;

		BookModel model = new BookModel();

		ITreeItemInfo bookItem = book.getTreeItem();
		if (bookItem == null)
			return null;

		String host = App.getJetty().book(bookItem.getId());
		model.url = App.getJetty().list(bookItem.getParent());
		model.title = bookItem.getTitle();

		model.section = new Section();

		model.section.id = treeItem.getId();
		model.section.title = treeItem.getTitle();
		model.section.group = treeItem.isGroup();
		model.section.text = srv.getText(id);
		model.section.url = Utils.getUrl(host, model.section.id);

		model.sections = new ArrayList<Section>();

		List<ITreeItemInfo> list = srv.getChildren(id);

		for (ITreeItemInfo item : list) {

			Section section = new Section();

			section.id = item.getId();
			section.title = item.getTitle();
			section.group = item.isGroup();
			section.text = srv.getText(item.getId());
			section.images = srv.getImages(item.getId());
			Integer bigImageCSS = ((SectionInfoOptions) item.getOptions())
					.getBigImageCSS();
			section.bigImageCSS = bigImageCSS;
			section.textCSS = SectionInfoOptions.gridLength - bigImageCSS;

			section.url = Utils.getUrl(host, section.id);

			model.sections.add(section);
		}

		model.parents = new ArrayList<ModelItem>();
		ITreeItemInfo parent = srv.get(treeItem.getParent());
		while (parent != null) {

			ModelItem item = new ModelItem();
			item.title = parent.getTitle();
			item.url = Utils.getUrl(host, parent.getId());
			item.id = parent.getId();

			model.parents.add(0, item);

			ITreeItemInfo current = parent;
			parent = srv.get(current.getParent());
		}

		return model;
	}
}
