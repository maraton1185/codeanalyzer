package ebook.module.book.handlers;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import ebook.module.book.views.SectionView;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;

public class SaveAsTemplate {
	@Execute
	public void execute(@Active MPart part, Shell shell) {
		SectionView view = (SectionView) part.getObject();
		if (view == null)
			return;

		InputDialog dlg = new InputDialog(shell,
				ebook.utils.Strings.title("appTitle"), "Введите имя шаблона:",
				ebook.utils.Strings.value("templateFileName"), null);
		if (dlg.open() == Window.OK) {

			BufferedWriter writer = null;
			try {

				File f = new File(
						PreferenceSupplier
								.get(PreferenceSupplier.EDITOR_TEMPLATES_FOLDER));

				if (!f.exists())
					f.mkdirs();

				f = new File(
						PreferenceSupplier
								.get(PreferenceSupplier.EDITOR_TEMPLATES_FOLDER)
								+ "/" + dlg.getValue() + ".txt");

				if (f.exists()
						&& !MessageDialog.openConfirm(shell,
								Strings.title("appTitle"),
								"Шаблон уже существует. Перезаписать?"))
					return;

				writer = new BufferedWriter(new FileWriter(f));
				writer.write(view.getTextEditor().getText());
			} catch (Exception e) {
				MessageDialog.openError(shell, Strings.title("appTitle"),
						"Ошибка записи шаблона.");
			} finally {
				try {
					// Close the writer regardless of what happens...
					writer.close();
				} catch (Exception e) {
				}
			}

		}

	}

}