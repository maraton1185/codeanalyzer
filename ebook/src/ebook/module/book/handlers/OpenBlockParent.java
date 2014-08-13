package ebook.module.book.handlers;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import ebook.core.App;
import ebook.module.book.BookConnection;
import ebook.module.book.tree.SectionInfo;
import ebook.utils.Events;
import ebook.utils.Events.EVENT_UPDATE_VIEW_DATA;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OpenBlockParent {

	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	@Active
	MWindow window;

	@Execute
	public void execute(@Active BookConnection book, @Active SectionInfo section) {

		SectionInfo selected = (SectionInfo) book.srv()
				.get(section.getParent());

		selected.tag = section.getId().toString();

		window.getContext().set(SectionInfo.class, selected);

		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));

		App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, selected, section));

		// Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		// App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
		// new EVENT_UPDATE_VIEW_DATA(book, section, section));

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null && !section.isGroup();
	}

}