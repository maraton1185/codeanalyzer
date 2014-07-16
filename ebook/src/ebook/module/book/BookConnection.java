package ebook.module.book;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.core.models.BaseDbPathConnection;
import ebook.module.book.servlets.BookServletModel;
import ebook.module.book.servlets.BookServletModel.Section;
import ebook.module.book.tree.SectionInfoOptions;
import ebook.module.tree.ITreeItemInfo;

public class BookConnection extends BaseDbPathConnection {

	int id;

	public BookConnection(IPath path, boolean check)
			throws InvocationTargetException {

		super(path, new BookStructure(), check);

	}

	public BookConnection(String name) throws InvocationTargetException {

		super(name, new BookStructure());

	}

	BookService service;

	public BookService srv() {

		service = service == null ? App.srv.bs(this) : service;

		return service;
	}

	public Integer getId() {

		return App.srv.bls().getBookId(getConnectionPath());
	}

	// *****************************************************************

	public BookServletModel getModel(String section_id) {

		Integer id;
		try {
			id = Integer.parseInt(section_id);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

		ITreeItemInfo sec = srv().get(id);
		if (sec == null)
			return null;

		BookServletModel model = new BookServletModel();

		model.host = App.getJetty().book(getId());
		model.section = model.new Section();

		model.section.id = sec.getId();
		model.section.title = sec.getTitle();
		model.section.group = sec.isGroup();
		model.section.text = srv().getText(id);

		model.sections = new ArrayList<Section>();

		List<ITreeItemInfo> list = srv().getChildren(id);

		for (ITreeItemInfo item : list) {

			Section sub_section = model.new Section();

			sub_section.id = item.getId();
			sub_section.title = item.getTitle();
			sub_section.group = item.isGroup();
			sub_section.text = srv().getText(item.getId());
			sub_section.images = srv().getImages(item.getId());
			Integer bigImageCSS = ((SectionInfoOptions) item.getOptions())
					.getBigImageCSS();
			sub_section.bigImageCSS = bigImageCSS;
			sub_section.textCSS = SectionInfoOptions.gridLength - bigImageCSS;

			model.sections.add(sub_section);
		}

		return model;
	}
}
