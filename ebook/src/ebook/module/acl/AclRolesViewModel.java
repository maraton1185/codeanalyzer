package ebook.module.acl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;

import ebook.core.App;
import ebook.core.models.ModelObject;
import ebook.module.acl.ACLService.ACLResult;
import ebook.module.tree.ITreeItemInfo;
import ebook.module.userList.tree.UserInfo;
import ebook.utils.Events.EVENT_UPDATE_TREE_DATA;

public class AclRolesViewModel extends ModelObject {

	protected CheckboxTableViewer rolesViewer;

	List<AclViewModel> roles = new ArrayList<AclViewModel>();

	private final String updateEvent;

	private ITreeItemInfo info;

	public AclRolesViewModel(CheckboxTableViewer rolesViewer,
			ITreeItemInfo info, String updateEvent) {
		super();
		this.rolesViewer = rolesViewer;
		this.info = info;
		this.updateEvent = updateEvent;
	}

	public List<AclViewModel> getRoles() {

		return roles;
	}

	public void setRoles() {

		if (!info.isGroup()) {

			roles.clear();
			activeRoles.clear();

			return;
		}

		List<AclViewModel> result = new ArrayList<AclViewModel>();

		List<UserInfo> input = App.srv.us().getRoles();
		for (UserInfo info : input) {

			AclViewModel item = new AclViewModel(info.getId());
			item.setTitle(info.getTitle());
			result.add(item);

		}
		roles = result;
		firePropertyChange("roles", null, null);

		ACLResult out = new ACLResult();
		activeRoles = App.srv.acl().get(info.getId(), out);

		firePropertyChange("activeRoles", null, null);

		if (out.inherited)
			rolesViewer.getTable().setBackground(
					Display.getCurrent().getSystemColor(
							SWT.COLOR_WIDGET_LIGHT_SHADOW));
		else
			rolesViewer.getTable().setBackground(
					Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

	}

	private Set<AclViewModel> activeRoles = new HashSet<AclViewModel>();

	public Set<AclViewModel> getActiveRoles() {
		return new HashSet<AclViewModel>(activeRoles);
	}

	public void setActiveRoles(Object[] objects) {

		App.srv.acl().set(info.getId(), objects);

		rolesViewer.getTable().setBackground(
				Display.getCurrent().getSystemColor(SWT.COLOR_WHITE));

		info.setACL();

		App.br.post(updateEvent, new EVENT_UPDATE_TREE_DATA(info, null));

	}
}
