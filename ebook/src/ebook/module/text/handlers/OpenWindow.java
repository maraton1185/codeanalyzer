package ebook.module.text.handlers;

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
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.module.text.TextConnection;
import ebook.module.text.model.History;
import ebook.utils.Events;
import ebook.utils.Strings;
import ebook.utils.Utils;

public class OpenWindow {

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
	public void EVENT_OPEN_TEXT(@UIEventTopic(Events.EVENT_OPEN_TEXT) Object con) {

		if (con instanceof TextConnection && ((TextConnection) con).isValid()) {
			App.ctx.set(TextConnection.class, (TextConnection) con);
			Utils.executeHandler(hs, cs, Strings.model("openTextWindow"));
		}

	}

	@CanExecute
	public boolean canExecute(@Optional TextConnection con) {
		return con != null && con.isValid();
	}

	@Execute
	public void execute(final @Active TextConnection con) {

		MWindow mainWindow = App.app.getChildren().get(0);

		@SuppressWarnings("serial")
		List<MTrimmedWindow> windows = model.findElements(App.app, null,
				MTrimmedWindow.class, new ArrayList<String>() {
					{
						add(con.getCon().getFullName() + "_text");
					}
				});

		if (windows.isEmpty())

			createWindow(mainWindow, con);

		else {
			MWindow w = windows.get(0);
			w.getContext().set(TextConnection.class, con);
			w.setVisible(true);
			App.app.setSelectedElement(w);
		}

		Utils.executeHandler(hs, cs, Strings.model("TextView.show"));
		App.ctx.set(TextConnection.class, null);
	}

	private void createWindow(MWindow mainWindow, TextConnection con) {

		MTrimmedWindow newWindow = (MTrimmedWindow) model.cloneSnippet(App.app,
				Strings.model("ebook.window.text"), null);

		newWindow.setLabel(con.getCon().getWindowTitle());
		// + (App.getJetty().isStarted() ? ""
		// : " : Web-сервер не запущен"));

		newWindow.setX(mainWindow.getX() + 20);
		newWindow.setY(mainWindow.getY() + 20);
		newWindow.setWidth(mainWindow.getWidth());
		newWindow.setHeight(mainWindow.getHeight());
		newWindow.getTags().add(con.getCon().getFullName() + "_text");

		App.app.getChildren().add(newWindow);
		newWindow.getContext().set(TextConnection.class, con);
		newWindow.getContext().set(History.class, new History());

		// BookWindowCloseHandler closeHandler = new BookWindowCloseHandler();
		// newWindow.getContext().set(IWindowCloseHandler.class, closeHandler);

	}
}
