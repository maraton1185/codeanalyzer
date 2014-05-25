package codeanalyzer.module.books.handlers;

import java.lang.reflect.InvocationTargetException;

import javax.inject.Named;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.Strings;

public class Add {
	@Execute
	public void execute(
			Shell shell,
			BookConnection book,
			@Active MPart part,
			@Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {

		try {

			SectionInfo data = new SectionInfo();
			data.setTitle(Strings.get("s.newblock.title"));
			data.setGroup(true);
			book.srv().add(data, section, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"Ошибка создания блока текста.");
		}
	}

	@CanExecute
	public boolean canExecute(
			@Optional @Active @Named(Events.CONTEXT_ACTIVE_VIEW_SECTION) SectionInfo section) {
		return section != null;
	}

}