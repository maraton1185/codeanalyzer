package ebook.module.text.service;

import java.util.ArrayList;
import java.util.List;

import ebook.module.conf.ConfService;
import ebook.module.conf.tree.ContextInfo;
import ebook.module.confLoad.model.ELevel;
import ebook.module.confLoad.services.CfBuildService;
import ebook.module.text.TextConnection;

public class ConfTextService extends TextService {

	public ConfTextService(TextConnection con) {
		super(con);

	}

	@Override
	public boolean readOnly(ContextInfo item) {
		return !item.isProc();
	}

	@Override
	public boolean setItemId(ContextInfo item) {
		Integer id = null;
		try {
			CfBuildService build = cf.build(srv.getConnection());
			List<String> path = new ArrayList<String>();

			id = build.getId((ConfService) srv, item, ELevel.proc, path);

			if (id != null) {
				item.setId(id);
				item.setProc();
				return true;
			}
			id = build.getId((ConfService) srv, item, null, path);

			if (id == null)
				return false;

			item.setId(id);
			return true;

		} catch (Exception e) {

			e.printStackTrace();
		}

		return false;

	}

}
