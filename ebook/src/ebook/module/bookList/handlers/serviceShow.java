package ebook.module.bookList.handlers;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.core.App.BookWindowCloseHandler;
import ebook.module.book.BookConnection;
import ebook.module.db.DbOptions;
import ebook.module.tree.item.ITreeItemInfo;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class serviceShow {

	@Inject
	@Optional
	Shell shell;
	@Inject
	EPartService partService;
	@Inject
	EModelService model;
	@Inject
	EHandlerService hs;
	@Inject
	ECommandService cs;
	@Inject
	IEclipseContext ctx;

	@Inject
	@Optional
	public void EVENT_SHOW_BOOK(@UIEventTopic(Events.EVENT_SHOW_BOOK) Object o) {

		Utils.executeHandler(hs, cs, Strings.model("command.id.ShowBook"));
	}

	@CanExecute
	public boolean canExecute(@Optional BookConnection book) {
		return book != null;
	}

	@Execute
	public void execute(final @Active BookConnection con) {

		// книги нет в списке
		ITreeItemInfo item = con.getTreeItem();
		if (item == null) {
			if (shell != null)
				MessageDialog.openError(shell, Strings.title("appTitle"),
						Strings.msg("error.book_not_in_list"));
			return;
		}

		MWindow mainWindow = App.app.getChildren().get(0);

		@SuppressWarnings("serial")
		List<MTrimmedWindow> windows = model.findElements(App.app, null,
				MTrimmedWindow.class, new ArrayList<String>() {
					{
						add(con.getFullName());
					}
				});

		if (windows.isEmpty())

			createWindow(mainWindow, con);

		else {
			MWindow w = windows.get(0);
			w.setVisible(true);
			App.app.setSelectedElement(w);
		}

		App.ctx.set(BookConnection.class, null);
	}

	private void createWindow(MWindow mainWindow, BookConnection con) {

		MTrimmedWindow newWindow = (MTrimmedWindow) model.cloneSnippet(App.app,
				Strings.model("model.id.book.window"), null);

		newWindow.setLabel(con.getWindowTitle());
		// + (App.getJetty().isStarted() ? ""
		// : " : Web-сервер не запущен"));

		String s = PreferenceSupplier.get(PreferenceSupplier.WINDOW_SIZE);

		if (s.isEmpty()) {
			newWindow.setX(mainWindow.getX() + 20);
			newWindow.setY(mainWindow.getY() + 20);
			newWindow.setWidth(mainWindow.getWidth());
			newWindow.setHeight(mainWindow.getHeight());
		} else {
			Rectangle rect = DbOptions.load(Rectangle.class, s);
			newWindow.setX(rect.x);
			newWindow.setY(rect.y);
			newWindow.setWidth(rect.width);
			newWindow.setHeight(rect.height);
		}
		newWindow.getTags().add(con.getFullName());

		App.app.getChildren().add(newWindow);
		// App.app.setSelectedElement(bookWindow);
		newWindow.getContext().set(BookConnection.class, con);

		BookWindowCloseHandler closeHandler = new BookWindowCloseHandler();
		newWindow.getContext().set(IWindowCloseHandler.class, closeHandler);

		// List<MPart> parts = model.findElements(newWindow,
		// Strings.get("model.id.part.SectionsStartView"), MPart.class,
		// null);
		// if (!parts.isEmpty()) {
		// MPart part = parts.get(0);
		// if (PreferenceSupplier
		// .getBoolean(PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW))
		// part.setVisible(false);
		// }
	}
}