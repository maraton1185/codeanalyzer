package codeanalyzer.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.IJobManager;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.advanced.MPerspective;
import org.eclipse.e4.ui.model.application.ui.basic.MPart;
import org.eclipse.e4.ui.model.application.ui.basic.MPartStack;
import org.eclipse.e4.ui.model.application.ui.basic.MStackElement;
import org.eclipse.e4.ui.model.application.ui.basic.MTrimmedWindow;
import org.eclipse.e4.ui.model.application.ui.basic.MWindow;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.PostContextCreate;
import org.eclipse.e4.ui.workbench.lifecycle.PreSave;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.EPartService.PartState;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import codeanalyzer.auth.interfaces.IAuthorize;
import codeanalyzer.core.interfaces.IDbConnection;
import codeanalyzer.core.interfaces.IServiceFactory;
import codeanalyzer.module.books.BookConnection;
import codeanalyzer.module.books.BookOptions;
import codeanalyzer.module.books.tree.SectionInfo;
import codeanalyzer.module.books.views.section.SectionView;
import codeanalyzer.module.booksList.IBookListManager;
import codeanalyzer.module.cf.services.FillProcLinkTableJob;
import codeanalyzer.utils.Events;
import codeanalyzer.utils.PreferenceSupplier;
import codeanalyzer.utils.Strings;
import codeanalyzer.utils.Utils;

public class App {

	public static IEventBroker br;
	public static IEclipseContext ctx;
	public static UISynchronize sync;
	public static EModelService model;
	public static MApplication app;
	public static EPartService ps;
	public static ESelectionService ss;

	public static IServiceFactory srv = pico.get(IServiceFactory.class);

	// public static EHandlerService hs;
	// public static ECommandService cs;

	// private static EContextService cs;
	// public static EHandlerService hs;
	// public static ECommandService cs;

	@PostContextCreate
	public void postContextCreate() {

	}

	@PreSave
	public void preSave() {

	}

	@ProcessAdditions
	public void processAdditions(IEventBroker br, IEclipseContext ctx,
			UISynchronize sync, EModelService modelService,
			MApplication application, EPartService ps, ESelectionService ss) {

		App.ss = ss;
		App.br = br;
		App.ctx = ctx;
		App.sync = sync;
		App.model = modelService;
		App.ps = ps;
		// App.hs = hs;
		// App.cs = cs;
		App.app = application;

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(
				"codeanalyzer.trimmedwindow.main", app);

		br.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
				new AppStartupCompleteEventHandler(window));

		br.subscribe(Events.EVENT_UPDATE_STATUS, new EVENT_UPDATE_STATUS());

		IDbConnection db = pico.get(IDbConnection.class);
		try {
			// throw new SQLException();
			db.check();
			db.openConnection();
		} catch (Exception e) {
			Shell shell = (Shell) window.getWidget();
			if (MessageDialog.openQuestion(shell, Strings.get("appTitle"),
					Strings.get("error.initDb"))) {
				try {
					db.create();
					db.openConnection();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
			// IWorkbench workbench = window.getContext().get(IWorkbench.class);

			// workbench.close();
			e.printStackTrace();
		}
	}

	private static class AppStartupCompleteEventHandler implements EventHandler {
		private MTrimmedWindow window;

		@Override
		public void handleEvent(Event event) {
			WindowCloseHandler closeHandler = new WindowCloseHandler();
			window.getContext().set(IWindowCloseHandler.class, closeHandler);
			App.br.post(Events.EVENT_UPDATE_STATUS, null);

			perspectiveActions();

			openBookOnStartup();

			trayOptions();
		}

		private void trayOptions() {
			final Image image = Utils.getImage("favicon.png");
			final Shell shell = ((Shell) window.getWidget());

			shell.addShellListener(new ShellAdapter() {
				@Override
				public void shellIconified(ShellEvent e) {

					if (!PreferenceSupplier
							.getBoolean(PreferenceSupplier.MINIMIZE_TO_TRAY))
						return;

					window.setVisible(false);

					final Tray tray = e.display.getSystemTray();
					if (tray == null)
						return;
					if (tray.getItemCount() != 0)
						return;
					final TrayItem item = new TrayItem(tray, SWT.NONE);
					item.setToolTipText(Strings.get("appTitle"));
					item.setImage(image);
					item.addListener(SWT.DefaultSelection, new Listener() {

						@Override
						public void handleEvent(
								org.eclipse.swt.widgets.Event event) {

							window.setVisible(true);

							shell.setMinimized(false);

							shell.forceActive();
						}

					});

				}
			});

			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.MINIMIZE_TO_TRAY_ON_STARTUP))
				shell.setMinimized(true);

		}

		private void openBookOnStartup() {

			if (!PreferenceSupplier
					.getBoolean(PreferenceSupplier.OPEN_BOOK_ON_STARTUP))
				return;

			IPath p = new Path(
					PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
			if (p.isEmpty())
				return;

			IBookListManager bm = pico.get(IBookListManager.class);

			bm.openBook(p, (Shell) window.getWidget());

			App.br.post(Events.EVENT_SHOW_BOOK, null);

		}

		AppStartupCompleteEventHandler(MTrimmedWindow window) {
			this.window = window;
		}

	}

	private static class EVENT_UPDATE_STATUS implements EventHandler {

		@Override
		public void handleEvent(Event event) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					final String status = pico.get(IAuthorize.class).getInfo()
							.ShortMessage();
					App.sync.asyncExec(new Runnable() {
						@Override
						public void run() {
							MHandledToolItem element = (MHandledToolItem) App.model
									.find(Strings.get("model_id_activate"),
											App.app);
							element.setLabel(status);
							element.setVisible(true);
						}
					});
				}
			}).start();
		}
	}

	public static class WindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			IJobManager jobMan = Job.getJobManager();
			jobMan.cancel(FillProcLinkTableJob.FillProcLinkTableJob_FAMILY);
			try {
				jobMan.join(FillProcLinkTableJob.FillProcLinkTableJob_FAMILY,
						null);
			} catch (OperationCanceledException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			IWorkbench workbench = window.getContext().get(IWorkbench.class);
			workbench.close();
			return true;
		}
	}

	public static class BookWindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			BookConnection book = window.getContext().get(BookConnection.class);

			SectionInfo section = window.getContext().get(SectionInfo.class);
			BookOptions opt = new BookOptions();
			if (section != null)
				opt.selectedSection = section.getId();

			List<MPartStack> stacks = model.findElements(App.app,
					Strings.get("codeanalyzer.partstack.sections"),
					MPartStack.class, null);

			if (!stacks.isEmpty()) {

				opt.openSections = new ArrayList<Integer>();
				for (MStackElement _part : stacks.get(0).getChildren()) {

					if (!(_part instanceof MPart))
						continue;

					MPart part = (MPart) _part;

					if (!part.isVisible())
						continue;
					String id = part.getElementId();

					if (id.equals(Strings
							.get("codeanalyzer.partdescriptor.sectionView"))) {
						SectionView view = (SectionView) part.getObject();
						opt.openSections.add(view.getId());
					}

					// if (id.equals(Strings
					// .get("codeanalyzer.partdescriptor.sectionsBlockView"))) {
					// BlockView view = (BlockView) part.getObject();
					// opt.openSections.add(view.getId());
					// }

				}

			}

			book.srv().saveBookOptions(opt);

			book.closeConnection();

			return true;
		}
	}

	public static void perspectiveActions() {
		MPerspective persp;

		if (PreferenceSupplier
				.getBoolean(PreferenceSupplier.SHOW_BOOK_PERSPECTIVE)) {

			persp = (MPerspective) model.find(
					Strings.get("model.id.perspective.books"), app);
			persp.setVisible(true);
			ps.switchPerspective(persp);

		} else {

			persp = (MPerspective) model.find(
					Strings.get("model.id.perspective.default"), app);
			persp.setVisible(true);
			ps.switchPerspective(persp);

			List<MPart> parts = model.findElements(app,
					Strings.get("model.id.part.start"), MPart.class, null);
			MPart part = parts.get(0);
			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.SHOW_START_PAGE))
				ps.showPart(part, PartState.ACTIVATE);
			else
				ps.hidePart(part);

		}
	}

}
