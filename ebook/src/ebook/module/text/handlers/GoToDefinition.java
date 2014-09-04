package ebook.module.text.handlers;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.TextConnection;
import ebook.module.text.model.GotoDefinitionData;
import ebook.module.text.model.LineInfo;
import ebook.module.text.views.DefinitionDialog;
import ebook.module.text.views.TextView;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Strings;

public class GoToDefinition {

	@Inject
	EPartService partService;
	@Inject
	EModelService model;

	@Execute
	public void execute(@Active TextConnection con, @Active MPart part,
			Shell shell, @Active MWindow window) {

		if (!(part.getObject() instanceof TextView))
			return;

		String path = con.getSrv().getPath(con.getItem());
		if (path.contains("...")) {
			MessageDialog.openInformation(shell, Strings.title("appTitle"),
					"ѕереход к определению возможнен только из модул€.");
			return;
		}

		GotoDefinitionData data = ((TextView) part.getObject())
				.getDefinitionData();

		if (data == null)
			return;

		List<ITreeItemInfo> defs = con.getSrv().getDefinitions(
				data.getProcName());

		if (defs == null)
			return;

		if (defs.isEmpty())
			return;

		ContextInfo item = null;
		if (defs.size() > 1) {
			DefinitionDialog dlg = new DefinitionDialog(shell,
					data.getProcTitle());
			dlg.setData(defs);
			if (dlg.open() == Window.OK)
				item = dlg.getItem();
		} else
			item = (ContextInfo) defs.get(0);

		if (item == null)
			return;

		LineInfo line = new LineInfo(item.getOptions());

		con.setLine(line);
		con.setItem(item);
		Show.show(window, model, partService, con);

	}

	@CanExecute
	public boolean canExecute(@Active @Optional TextConnection con) {
		return con != null && con.isValid();
	}
}