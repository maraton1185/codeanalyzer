package ebook.module.confList.handlers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.eclipse.e4.core.commands.ECommandService;
import org.eclipse.e4.core.commands.EHandlerService;
import org.eclipse.e4.core.contexts.Active;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.CanExecute;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.internal.workbench.PartServiceSaveHandler;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ISaveHandler;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;

import ebook.core.App;
import ebook.core.App.ConfWindowCloseHandler;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.ConfOptions;
import ebook.module.conf.tree.ListInfo;
import ebook.module.confList.tree.ListConfInfo;
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
	public void EVENT_SHOW_CONF(@UIEventTopic(Events.EVENT_SHOW_CONF) Object o) {

		Utils.executeHandler(hs, cs, Strings.model("command.id.ShowConf"));
	}

	@CanExecute
	public boolean canExecute(@Optional ConfConnection conf) {
		return conf != null;
	}

	@Execute
	public void execute(final @Active ConfConnection con,
			@Optional ListInfo list) {

		// ����� ��� � ������
		ITreeItemInfo item = con.getTreeItem();
		if (item == null) {
			if (shell != null)
				MessageDialog.openError(shell, Strings.title("appTitle"),
						Strings.msg("error.conf_not_in_list"));
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

		MWindow w;
		if (windows.isEmpty())

			w = createWindow(mainWindow, con);

		else {
			w = windows.get(0);
			w.setVisible(true);
			App.app.setSelectedElement(w);
		}

		w.getContext().set(ListConfInfo.class, (ListConfInfo) item);
		showSections(con, w, list);

		App.ctx.set(ListInfo.class, null);
		App.ctx.set(ConfConnection.class, null);
	}

	private MWindow createWindow(MWindow mainWindow, ConfConnection con) {

		MTrimmedWindow newWindow = (MTrimmedWindow) model.cloneSnippet(App.app,
				Strings.model("ebook.window.conf"), null);

		newWindow.setLabel(con.getWindowTitle());

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
		newWindow.getContext().set(ConfConnection.class, con);

		ConfWindowCloseHandler closeHandler = new ConfWindowCloseHandler();
		newWindow.getContext().set(IWindowCloseHandler.class, closeHandler);

		// ISaveHandler saveHandler = new CustomSaveHandler();
		newWindow.getContext().set(ISaveHandler.class,
				new PartServiceSaveHandler() {
					@Override
					public Save promptToSave(MPart dirtyPart) {
						return Save.NO;
					}

					@Override
					public Save[] promptToSave(Collection<MPart> dirtyParts) {
						Save[] rc = new Save[dirtyParts.size()];
						for (int i = 0; i < rc.length; i++) {
							rc[i] = Save.NO;
						}
						return rc;
					}
				});
		// List<MPart> parts = model.findElements(newWindow,
		// Strings.get("model.id.part.SectionsStartView"), MPart.class,
		// null);
		// if (!parts.isEmpty()) {
		// MPart part = parts.get(0);
		// if (PreferenceSupplier
		// .getBoolean(PreferenceSupplier.NOT_OPEN_SECTION_START_VIEW))
		// part.setVisible(false);
		// }
		return newWindow;
	}

	private void showSections(ConfConnection con, MWindow window, ListInfo list) {

		ConfOptions opt = con.srv(null).getRootOptions(ConfOptions.class);

		if (list != null && !opt.openSections.contains(list.getId())) {
			opt.openSections.add(list.getId());
		}

		Map<String, String> map = new HashMap<String, String>();
		map.put(Strings.model("ebook.commandparameter.dirty"), "true");

		for (Integer i : opt.openSections) {

			final ListInfo section = (ListInfo) con.lsrv().get(i);
			if (section == null)
				continue;

			window.getContext().set(ListInfo.class, section);

			// App.br.post(Events.EVENT_SHOW_CONF_LIST, null);
			if (section.equals(list))
				Utils.executeHandler(hs, cs, Strings.model("ListView.show"),
						map);
			else
				Utils.executeHandler(hs, cs, Strings.model("ListView.show"));
			// show(window, section);
		}

		if (opt.openSections == null || opt.openSections.isEmpty()) {

			List<ITreeItemInfo> input = con.lsrv().getRoot();
			if (input.isEmpty()) {
				return;
			}
			int section_id = input.get(0).getId();

			final ListInfo section = (ListInfo) con.lsrv().get(section_id);
			if (section == null)
				return;

			window.getContext().set(ListInfo.class, section);

			// App.br.post(Events.EVENT_SHOW_CONF_LIST, null);
			Utils.executeHandler(hs, cs, Strings.model("ListView.show"));
			// show(window, section);

		}

	}

	// private void show(MWindow window, final ListInfo list) {
	// List<MPartStack> stacks = model.findElements(window,
	// Strings.get("ebook.partstack.conf"), MPartStack.class, null);
	//
	// String partID = Strings.get("ebook.partdescriptor.0");
	//
	// stacks.get(0).setVisible(true);
	//
	// @SuppressWarnings("serial")
	// List<MPart> parts = model.findElements(stacks.get(0), partID,
	// MPart.class, new ArrayList<String>() {
	// {
	// add(list.getId().toString());
	// }
	// });
	//
	// MPart part;
	//
	// if (parts.isEmpty()) {
	// part = partService.createPart(partID);
	//
	// part.setLabel(list.getTitle());
	// part.getTags().add(list.getId().toString());
	// stacks.get(0).getChildren().add(part);
	// } else {
	// part = parts.get(0);
	// part.setLabel(list.getTitle());
	// }
	//
	// partService.showPart(part, PartState.VISIBLE);
	// // partService.
	//
	// // stacks.get(0).
	// }
}