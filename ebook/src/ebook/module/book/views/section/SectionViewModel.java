package ebook.module.book.views.section;

import org.eclipse.jface.viewers.CheckboxTableViewer;

import ebook.module.acl.AclRolesViewModel;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Events;

public class SectionViewModel extends AclRolesViewModel {

	public SectionViewModel(CheckboxTableViewer rolesViewer, ITreeItemInfo info) {
		super(rolesViewer, info, Events.EVENT_UPDATE_LABELS_CONTENT_VIEW);
	}
}
