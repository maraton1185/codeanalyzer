package codeanalyzer.core;

import java.lang.reflect.InvocationTargetException;
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
import org.eclipse.e4.ui.model.application.ui.menu.MDirectToolItem;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import codeanalyzer.auth.interfaces.IAuthorize;
import codeanalyzer.core.exceptions.MakeConnectionException;
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
import codeanalyzer.web.IJetty;

public class App {

	public enum Perspectives {
		books, main;

		@Override
		public String toString() {
			switch (this) {
			case books:
				return Strings.get("model.id.perspective.books");
			default:
				return Strings.get("model.id.perspective.default");
			}
		}
	}

	public static Perspectives currentPerspective;

	public static IEventBroker br;
	public static IEclipseContext ctx;
	public static UISynchronize sync;
	public static EModelService model;
	public static MApplication app;
	public static EPartService ps;
	public static ESelectionService ss;

	public static IServiceFactory srv = pico.get(IServiceFactory.class);

	private static IJetty jetty = pico.get(IJetty.class);

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

		dbInit(window);

	}

	// DB INIT
	private void dbInit(MTrimmedWindow window) {
		IDbConnection db = pico.get(IDbConnection.class);
		try {
			// throw new SQLException();

			try {
				db.check();
			} catch (InvocationTargetException e1) {
				if (e1.getTargetException() instanceof MakeConnectionException) {
					db.create();
				}
			}

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

			e.printStackTrace();
		}
	}

	// APP STARTUP, TRAY

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
					final Tray tray = Display.getCurrent().getSystemTray();
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

					item.addListener(SWT.MenuDetect, new Listener() {
						@Override
						public void handleEvent(
								org.eclipse.swt.widgets.Event event) {

							// Style must be pop up
							Menu m = new Menu(shell, SWT.POP_UP);

							MenuItem open = new MenuItem(m, SWT.NONE);
							open.setText("������� � ��������");
							open.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(
										org.eclipse.swt.widgets.Event event) {

									Program.launch(App.getJetty().info());
									// IWorkbench workbench =
									// window.getContext()
									// .get(IWorkbench.class);
									// workbench.close();
								}
							});

							MenuItem exit = new MenuItem(m, SWT.NONE);
							exit.setText("�����");
							exit.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(
										org.eclipse.swt.widgets.Event event) {
									IWorkbench workbench = window.getContext()
											.get(IWorkbench.class);
									workbench.close();
								}
							});

							// We need to make the menu visible
							m.setVisible(true);
						};
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

	// UPADTE STATUS

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
							// MHandledToolItem element;
							MHandledToolItem h_element = (MHandledToolItem) App.model
									.find(Strings.get("model_id_activate"),
											App.app);
							h_element.setLabel(status);
							h_element.setVisible(true);

							MDirectToolItem d_element = (MDirectToolItem) App.model.find(
									Strings.get("codeanalyzer.directtoolitem.1"),
									App.app);
							d_element.setLabel(jetty.jettyMessage());
							d_element.setVisible(true);

						}
					});
				}
			}).start();
		}
	}

	// WINDOWS CLOSING

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

	// PERSPECTIVES
	// ******************************************************************

	public static void perspectiveActions() {

		if (PreferenceSupplier
				.getBoolean(PreferenceSupplier.SHOW_BOOK_PERSPECTIVE))
			currentPerspective = Perspectives.books;
		else
			currentPerspective = Perspectives.main;

		showPerspective(currentPerspective);

	}

	public static void togglePerspective() {
		if (currentPerspective == Perspectives.books)
			currentPerspective = Perspectives.main;
		else
			currentPerspective = Perspectives.books;
		showPerspective(currentPerspective);
	}

	public static void showPerspective(Perspectives perspType) {

		MPerspective persp;

		persp = (MPerspective) model.find(perspType.toString(), app);
		persp.setVisible(true);
		ps.switchPerspective(persp);

	}

	public static IJetty getJetty() {
		return jetty;
	}
}
