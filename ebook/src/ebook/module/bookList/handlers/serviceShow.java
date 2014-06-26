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
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;

import ebook.core.App;
import ebook.core.App.BookWindowCloseHandler;
import ebook.module.book.BookConnection;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class serviceShow {

	@Inject
	@Optional
	public void EVENT_SHOW_BOOK(@UIEventTopic(Events.EVENT_SHOW_BOOK) Object o,
			final EHandlerService hs, final ECommandService cs) {

		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowBook"));
	}

	@CanExecute
	public boolean canExecute(@Optional BookConnection book) {
		return book != null;
	}

	@Execute
	public void execute(EPartService partService, EModelService model,
			final @Active BookConnection con, IEclipseContext ctx,
			EHandlerService hs, ECommandService cs) {

		MWindow mainWindow = App.app.getChildren().get(0);

		@SuppressWarnings("serial")
		List<MTrimmedWindow> windows = model.findElements(App.app, null,
				MTrimmedWindow.class, new ArrayList<String>() {
					{
						add(con.getFullName());
					}
				});

		if (windows.isEmpty())

			createWindow(mainWindow, con, model, ctx, partService, hs, cs);

		else {
			MWindow w = windows.get(0);
			w.setVisible(true);
			App.app.setSelectedElement(w);
		}
	}

	private void createWindow(MWindow mainWindow, BookConnection con,
			EModelService model, IEclipseContext ctx, EPartService partService,
			EHandlerService hs, ECommandService cs) {

		MTrimmedWindow newWindow = (MTrimmedWindow) model.cloneSnippet(
				App.app, Strings.get("model.id.book.window"), null);

		newWindow.setLabel(con.getWindowTitle());
		newWindow.setX(mainWindow.getX() + 20);
		newWindow.setY(mainWindow.getY() + 20);
		newWindow.setWidth(mainWindow.getWidth());
		newWindow.setHeight(mainWindow.getHeight());
		newWindow.getTags().add(con.getFullName());

		App.app.getChildren().add(newWindow);
		// App.app.setSelectedElement(bookWindow);
		newWindow.getContext().set(BookConnection.class, con);

		BookWindowCloseHandler closeHandler = new BookWindowCloseHandler();
		newWindow.getContext().set(IWindowCloseHandler.class, closeHandler);

		List<MPart> parts = model.findElements(newWindow,
				Strings.get("model.id.part.SectionsStartView"), MPart.class,
				null);
		if (!parts.isEmpty()) {
			MPart part = parts.get(0);
			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW))
				part.setVisible(false);
		}
	}
}