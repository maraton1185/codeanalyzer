package ebook.module.book.views;

import org.eclipse.jface.viewers.CheckboxTableViewer;

import ebook.core.App;
import ebook.module.acl.ACLService.ACLResult;
import ebook.module.acl.AclRolesViewModel;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;

public class SectionViewModel extends AclRolesViewModel {

	private ITreeItemInfo info;
	private BookConnection book;

	public SectionViewModel(BookConnection book,
			CheckboxTableViewer rolesViewer, ITreeItemInfo info) {
		super(rolesViewer, info);

		this.book = book;
		this.info = info;
	}

	@Override
	protected void loadActiveRoles(ACLResult out) {
		activeRoles = App.srv.acl().get(book.getTreeItem().getId(),
				info.getId(), out);

	}

	@Override
	protected void saveActiveRoles(Object[] objects) {
		App.srv.acl().set(book.getTreeItem().getId(), info.getId(), objects);

	}

	@Override
	protected void updateList() {
		App.br.post(Events.EVENT_UPDATE_LABELS,
				new EVENT_UPDATE_VIEW_DATA(book, (SectionInfo) info));

	}
}
