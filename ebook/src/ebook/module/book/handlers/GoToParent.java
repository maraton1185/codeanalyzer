package ebook.module.book.handlers;

import javax.inject.Named;

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

public class GoToParent {
	@Execute
	public void execute(
			@Active BookConnection book,
			@Active MWindow window,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section,
			ECommandService cs, EHandlerService hs) {

		SectionInfo selected = (SectionInfo) book.srv()
				.get(section.getParent());

		if (selected == null)
			return;

		if (!section.isGroup())
			selected.tag = section.getId().toString();

		window.getContext().set(SectionInfo.class, selected);
		Utils.executeHandler(hs, cs, Strings.model("command.id.ShowSection"));
		// window.getContext().set(BookSection.class, current_section);
		App.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, null, selected));
		App.br.post(Events.EVENT_UPDATE_SECTION_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, selected, selected));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null && section.getParent() != 0;
	}

}