package ebook.module.text.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import ebook.core.App;
import ebook.core.exceptions.GetRootException;
import ebook.core.interfaces.IDbConnection;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.ConfService;
import ebook.module.conf.ListService;
import ebook.module.conf.model.BuildType;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.conf.tree.ContextInfoOptions;
import ebook.module.conf.tree.ListInfo;
import ebook.module.text.TextConnection;
import ebook.module.text.interfaces.ITextTreeService;
import ebook.module.text.model.LineInfo;
import ebook.module.tree.ITreeItemInfo;
import ebook.utils.Events;

public class TextService {

	protected ContextInfo item;
	protected ITextTreeService srv;
	IDbConnection con;

	public TextService(TextConnection con) {
		this.con = con.getCon();
		item = con.getItem();
		srv = con.getSrv();
	}

	public void setItem(ContextInfo item) {
		this.item = item;
	}

	public boolean readOnly(ContextInfo item) {
		ContextInfoOptions opt = item.getOptions();
		return opt != null && opt.type == BuildType.module;
	}

	public void saveItemText(String text) {

		srv.saveText(item.getId(), text);

	}

	public String getItemText(ContextInfo item) {

		ContextInfoOptions opt = item.getOptions();
		if (opt.type == BuildType.module)

			return getModuleText(item);

		else

			return getText(item);

	}

	protected String getModuleText(ContextInfo item) {
		List<ITreeItemInfo> list = srv.getChildren(item.getId());

		StringBuilder result = new StringBuilder();

		for (ITreeItemInfo info : list) {

			String text = srv.getText(info.getId());

			result.append(text);
		}

		return result.toString();

	}

	protected String getText(ContextInfo item) {

		return srv.getText(item.getId());
	}

	public ContextInfo getItemByTitle(LineInfo selected) {
		ITreeItemInfo info = srv
				.findInParent(selected.getTitle(), item.getId());

		return (ContextInfo) info;
	}

	public ContextInfo getModule(ContextInfo item) {
		return (ContextInfo) srv.getModule(item);
	}

	public void buildText(ContextInfo item, String line, boolean withContext) {

		try {

			if (!(con instanceof ConfConnection))
				return;

			ConfConnection _con = (ConfConnection) con;

			ListInfo newList = App.mng.clm(_con).getNewList();
			ConfService conf = _con.srv(newList);
			ListService lsrv = _con.lsrv();

			List<ITreeItemInfo> result = conf.getRoot();
			if (result.isEmpty())
				throw new InvocationTargetException(new GetRootException());

			ContextInfo dest = (ContextInfo) result.get(0);

			if (withContext) {
				List<ITreeItemInfo> path = srv.getParents(item);
				for (int i = 2; i < path.size(); i++)
					path.remove(path.size() - 1);

				for (ITreeItemInfo p : path) {
					conf.add(p, dest, true);
					dest = (ContextInfo) p;
				}
			}

			ContextInfoOptions opt = new ContextInfoOptions();
			opt.type = BuildType.text;
			ContextInfo data = new ContextInfo(opt);
			data.setTitle(line);
			data.setGroup(true);
			conf.add(data, dest, true);

			newList.getOptions().selectedContext = data.getId();
			lsrv.saveOptions(newList);

			App.ctx.set(ConfConnection.class, (ConfConnection) con);
			App.ctx.set(ListInfo.class, newList);
			App.br.post(Events.EVENT_SHOW_CONF, null);

		} catch (InvocationTargetException e) {

			App.ctx.set(ConfConnection.class, null);
			App.ctx.set(ListInfo.class, null);

		}

	}
}
