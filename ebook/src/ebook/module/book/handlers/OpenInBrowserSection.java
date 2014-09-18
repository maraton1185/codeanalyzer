package ebook.module.book.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.adapter.Adapter;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.swt.program.Program;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.interfaces.IBrowserBridgeView;

public class OpenInBrowserSection {

	@Inject
	private Adapter _adapter;

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	@Active
	MWindow window;

	@Execute
	public void execute(@Active BookConnection book,
			@Active SectionInfo _section, @Active MPart part) {

		IBrowserBridgeView opTarget = _adapter.adapt(part.getObject(),
				IBrowserBridgeView.class);

		SectionInfo section;
		if (opTarget != null) {
			section = opTarget.getSection();
			if (section == null)
				return;
		} else
			section = _section;

		String url;

		if (section.isGroup()) {

			url = App.getJetty().host()
					+ App.getJetty().section(book.getTreeItem().getId(),
							section.getId());

		} else {
			SectionInfo selected = (SectionInfo) book.srv().get(
					section.getParent());

			url = App.getJetty().host()
					+ App.getJetty().section(book.getTreeItem().getId(),
							selected.getId()) + "#" + section.getId();

		}
		Program.launch(url);

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null && App.getJetty().isStarted();
	}

}