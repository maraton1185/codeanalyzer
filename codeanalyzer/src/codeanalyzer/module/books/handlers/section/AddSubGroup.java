package codeanalyzer.module.books.handlers.section;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.model.BookConnection;
import codeanalyzer.module.books.section.SectionInfo;
import codeanalyzer.utils.Strings;

public class AddSubGroup {
	@Execute
	public void execute(Shell shell, BookConnection book,
			@Active SectionInfo section) {

		try {

			SectionInfo data = new SectionInfo();
			data.title = Strings.get("s.newsection.title ");
			data.isGroup = true;
			book.service().add(data, section, true);

		} catch (InvocationTargetException e) {
			MessageDialog.openError(shell, Strings.get("appTitle"),
					"������ �������� �������.");
		}

	}

	@CanExecute
	public boolean canExecute(@Optional @Active SectionInfo section) {
		return section != null;
	}

}