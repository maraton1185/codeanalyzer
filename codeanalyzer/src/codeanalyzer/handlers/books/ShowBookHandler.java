package codeanalyzer.handlers.books;

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

import codeanalyzer.book.BookInfo;
import codeanalyzer.core.AppManager;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class ShowBookHandler {

	@Inject
	@Optional
	public void EVENT_SHOW_BOOK(@UIEventTopic(Const.EVENT_SHOW_BOOK) Object o,
			final EHandlerService hs, final ECommandService cs) {

		Utils.executeHandler(hs, cs, Strings.get("command.id.ShowBook"));
	}

	@CanExecute
	public boolean canExecute(@Optional BookInfo book) {
		return book != null;
	}

	@Execute
	public void execute(EPartService partService, EModelService model,
			final @Active BookInfo book, IEclipseContext ctx) {

		MWindow mainWindow = AppManager.app.getChildren().get(0);

		@SuppressWarnings("serial")
		List<MTrimmedWindow> windows = model.findElements(AppManager.app, null,
				MTrimmedWindow.class, new ArrayList<String>() {
					{
						add(book.getFullName());
					}
				});

		if (windows.isEmpty())

			createBookWindow(mainWindow, book, model, ctx);

		else {
			MWindow w = windows.get(0);
			w.setVisible(true);
			AppManager.app.setSelectedElement(w);
		}
	}

	private void createBookWindow(MWindow mainWindow, BookInfo book,
			EModelService model, IEclipseContext ctx) {

		MTrimmedWindow bookWindow = (MTrimmedWindow) model.cloneSnippet(
				AppManager.app, Strings.get("model.id.book.window"), null);

		// MTrimmedWindow bookWindow = MBasicFactory.INSTANCE
		// .createTrimmedWindow();

		bookWindow.setLabel(book.getName());
		bookWindow.setX(mainWindow.getX() + 20);
		bookWindow.setY(mainWindow.getY() + 20);
		bookWindow.setWidth(mainWindow.getWidth());
		bookWindow.setHeight(mainWindow.getHeight());
		bookWindow.getTags().add(book.getFullName());

		// bookWindow.getTransientData().put(Const.WINDOW_CONTEXT, book);
		// IEclipseContext ctx1 = ctx.createChild();
		// ctx1.set(BookInfo.class, book);
		// bookWindow.setContext(ctx1);
		// bookWindow.getContext().set(BookInfo.class, book);
		AppManager.app.getChildren().add(bookWindow);
		bookWindow.getContext().set(BookInfo.class, book);

		// // bookWindow.getTags().add("");
		// List<MPart> part = model.findElements(AppManager.app,
		// Strings.get("codeanalyzer.part.3"), MPart.class, null);
		//
		// bookWindow.getChildren().add(part.get(0));
		// // stacks.get(0).getChildren().add(part);
		//
		// application.getChildren().add(bookWindow);
	}
}