package ebook.module.conf;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;

import ebook.core.App;
import ebook.module.conf.service.BookmarkService;
import ebook.module.conf.service.ConfService;
import ebook.module.conf.service.ConfTreeService;
import ebook.module.conf.service.ListService;
import ebook.module.conf.tree.ListInfo;
import ebook.module.db.BaseDbPathConnection;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.PreferenceSupplier;

public class ConfConnection extends BaseDbPathConnection {

	public ConfConnection(IPath path) throws InvocationTargetException {
		super(path, new ConfStructure(), true, false);
	}

	public ConfConnection(IPath path, boolean check)
			throws InvocationTargetException {
		super(path, new ConfStructure(), check, false);
	}

	public ConfConnection(IPath path, boolean check, boolean license)
			throws InvocationTargetException {
		super(path, new ConfStructure(), check, license);
	}

	public ConfConnection(String name) throws InvocationTargetException {
		super(name, new ConfStructure());
	}

	@Override
	protected IPath getBasePath() {

		return new Path(
				PreferenceSupplier
						.get(PreferenceSupplier.DEFAULT_CONF_DIRECTORY));

	}

	HashMap<ListInfo, ConfService> srv_map = new HashMap<ListInfo, ConfService>();

	public void remove(ListInfo list) {
		srv_map.remove(list);
	}

	public ConfService srv(ListInfo list) {

		ConfService srv = srv_map.get(list);
		if (srv == null) {
			srv = new ConfService(this, list);
			srv_map.put(list, srv);
		}
		return srv;
	}

	private ConfTreeService conf_service;

	public ConfTreeService conf() {

		conf_service = conf_service == null ? new ConfTreeService(this)
				: conf_service;

		return conf_service;

	}

	private ListService service;

	public ListService lsrv() {

		service = service == null ? new ListService(this) : service;

		return service;

	}

	private BookmarkService bmsrv;

	public BookmarkService bmsrv() {

		bmsrv = bmsrv == null ? new BookmarkService(this) : bmsrv;

		return bmsrv;

	}

	ITreeItemInfo treeItem;

	@Override
	public ITreeItemInfo getTreeItem() {
		if (treeItem == null)
			treeItem = App.srv.cl().getTreeItem(getName(), getFullName());

		return treeItem;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ConfConnection)
			return ((ConfConnection) obj).getName().equals(getName());
		else
			return super.equals(obj);
	}
	// @Override
	// public String getWindowTitle() {
	// return getName();
	// }

}
