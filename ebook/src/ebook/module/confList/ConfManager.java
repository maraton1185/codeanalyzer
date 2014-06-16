package ebook.module.confList;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

import ebook.core.App;
import ebook.module.conf.ConfConnection;
import ebook.module.confList.tree.ListConfInfo;
import ebook.module.confList.tree.ListConfInfoOptions;

public class ConfManager implements IConfManager {

	ConfListService srv = App.srv.cls();

	@Override
	public void Add(String value, ListConfInfo parent)
			throws InvocationTargetException {

		ConfConnection con = new ConfConnection(value);

		ListConfInfoOptions opt = new ListConfInfoOptions();
		opt.path = con.getFullName();
		ListConfInfo data = new ListConfInfo(opt);
		data.setTitle(con.getName());
		data.setGroup(false);

		// data.options = opt;
		srv.add(data, parent, true);

		// TODO: open load dialog

	}

	@Override
	public void addToList(IPath path, ListConfInfo parent)
			throws InvocationTargetException {

		ConfConnection con = new ConfConnection(path);

		ListConfInfoOptions opt = new ListConfInfoOptions();
		opt.path = con.getFullName();
		ListConfInfo data = new ListConfInfo(opt);
		data.setTitle(con.getName());
		data.setGroup(false);

		srv.add(data, parent, true);

	}

	@Override
	public void addGroup(ListConfInfo data, ListConfInfo selected, boolean sub)
			throws InvocationTargetException {
		srv.add(data, selected, sub);

	}

}
