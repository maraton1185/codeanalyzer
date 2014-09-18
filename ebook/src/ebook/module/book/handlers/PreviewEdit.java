package ebook.module.book.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;

import ebook.core.App;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class PreviewEdit {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;

	@Execute
	public void execute(@Active SectionInfo section) {
		Utils.executeHandler(hs, cs, Strings.model("command.id.ShowSection"));
	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null && !section.isGroup()
				&& App.getJetty().isStarted();
	}
}
