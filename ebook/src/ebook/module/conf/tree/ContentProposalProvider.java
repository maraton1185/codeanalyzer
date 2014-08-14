package ebook.module.conf.tree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.jetty.util.ArrayUtil;
import org.eclipse.jface.fieldassist.ContentProposal;
import org.eclipse.jface.fieldassist.IContentProposal;
import org.eclipse.jface.fieldassist.IContentProposalProvider;

import ebook.core.pico;
import ebook.module.conf.ConfService;
import ebook.module.conf.model.BuildInfo;
import ebook.module.confLoad.interfaces.ICfServices;
import ebook.module.confLoad.model.ELevel;

public class ContentProposalProvider implements IContentProposalProvider {

	private MWindow window;
	private ConfService tree;
	public ICfServices cf = pico.get(ICfServices.class);

	public ContentProposalProvider(ConfService srv, MWindow window) {
		// proposals = new String[] { "Конфигурация", "ProposalTwo",
		// "ProposalThree" };
		this.window = window;
		this.tree = srv;
	}

	private boolean filterProposals = true;
	private IContentProposal[] contentProposals;
	private List<BuildInfo> proposals = new ArrayList<BuildInfo>();

	@Override
	public IContentProposal[] getProposals(String contents, int position) {

		String filter = contents;

		try {
			// ContextInfo info = window.getContext().get(ContextInfo.class);
			String title;

			String[] str = contents.split("\\.");
			Integer gr1 = null;
			Integer gr2 = null;

			if (contents.endsWith("."))
				str = ArrayUtil.addToArray(str, "", String.class);

			cf.build().setConnection(tree.getConnection());

			switch (str.length) {
			case 1:

				title = contents.replaceAll("\\.", "");
				cf.build().get(ELevel.group1, title, null, proposals);
				// proposals = cf.build().getLevel(
				// ELevel.group1, title, null);
				break;

			case 2:

				gr1 = cf.build().get(ELevel.group1, str[0], null, null);

				if (gr1 != null)
					cf.build().get(ELevel.group2, str[1], gr1, proposals);

				filter = str[1];

				break;
			case 3:

				gr1 = cf.build().get(ELevel.group1, str[0], null, null);

				if (gr1 != null)
					gr2 = cf.build().get(ELevel.group2, str[1], gr1, null);

				if (gr2 != null)
					cf.build().get(ELevel.module, str[2], gr2, proposals);

				filter = str[2];

				break;
			default:
				break;
			}

		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		// System.out.println(info.getTitle());

		if (filterProposals) {
			ArrayList<IContentProposal> list = new ArrayList<IContentProposal>();
			for (int i = 0; i < proposals.size(); i++) {
				if (proposals.get(i).title.length() >= filter.length()
						&& proposals.get(i).title.substring(0, filter.length())
								.equalsIgnoreCase(filter)) {
					list.add(new ContentProposal(proposals.get(i).title));
				}
			}
			return list.toArray(new IContentProposal[list.size()]);
		}
		if (contentProposals == null) {
			contentProposals = new IContentProposal[proposals.size()];
			for (int i = 0; i < proposals.size(); i++) {
				contentProposals[i] = new ContentProposal(
						proposals.get(i).title);
			}
		}
		return contentProposals;
	}
}
