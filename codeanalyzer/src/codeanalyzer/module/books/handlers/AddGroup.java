package codeanalyzer.module.books.handlers;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Shell;

import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.utils.Strings;

public class AddGroup {
	@Execute
	public void execute(Shell shell, BookConnection book,
			@Active SectionInfo section) {

		try {

			SectionInfo data = new SectionInfo();
			data.setTitle(Strings.get("s.newsection.title"));
			data.setGroup(true);
			book.srv().add(data, section, false);

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