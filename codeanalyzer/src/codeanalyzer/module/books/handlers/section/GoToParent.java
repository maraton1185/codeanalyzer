package codeanalyzer.module.books.handlers.section;

import javax.inject.Named;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;

import codeanalyzer.core.AppManager;
import codeanalyzer.core.Events;
import codeanalyzer.core.Events.EVENT_UPDATE_VIEW_DATA;
import codeanalyzer.module.books.WindowBookInfo;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class GoToParent {
	@Execute
	public void execute(
			WindowBookInfo book,
			@Active MWindow window,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section,
			ECommandService cs, EHandlerService hs) {

		SectionInfo selected = book.sections().getParent(section);

		if (selected == null)
			return;

		window.getContext().set(SectionInfo.class, selected);
		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowSection"));
		// window.getContext().set(BookSection.class, current_section);
		AppManager.br.post(Events.EVENT_UPDATE_CONTENT_VIEW,
				new EVENT_UPDATE_VIEW_DATA(book, null, selected));
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null && section.parent != 0;
	}

}