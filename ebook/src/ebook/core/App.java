package ebook.core;

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
import org.eclipse.e4.ui.workbench.IWorkbench;
import org.eclipse.e4.ui.workbench.UIEvents;
import org.eclipse.e4.ui.workbench.lifecycle.ProcessAdditions;
import org.eclipse.e4.ui.workbench.modeling.EModelService;
import org.eclipse.e4.ui.workbench.modeling.EPartService;
import org.eclipse.e4.ui.workbench.modeling.ESelectionService;
import org.eclipse.e4.ui.workbench.modeling.IWindowCloseHandler;
import org.eclipse.equinox.p2.core.IProvisioningAgent;
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
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.osgi.service.event.Event;
import org.osgi.service.event.EventHandler;

import updatesite.P2Util;
import updatesite.UpdateCheckJob;
import ebook.core.GlobalEvents.EVENT_START_JETTY;
import ebook.core.GlobalEvents.EVENT_STOP_JETTY;
import ebook.core.GlobalEvents.EVENT_UPDATE_PERSPECTIVE_ICON;
import ebook.core.GlobalEvents.EVENT_UPDATE_STATUS;
import ebook.core.GlobalEvents.RESTART_WORKBENCH;
import ebook.core.GlobalEvents.SHOW_UPDATE_AVAILABLE;
import ebook.core.exceptions.MakeConnectionException;
import ebook.core.interfaces.IBookClipboard;
import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IManagerFactory;
import ebook.core.interfaces.IServiceFactory;
import ebook.module.book.BookConnection;
import ebook.module.book.BookOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.SectionTextView;
import ebook.module.book.views.SectionView;
import ebook.module.confLoad.ConfConnection;
import ebook.module.confLoad.services.FillProcLinkTableJob;
import ebook.utils.Events;
import ebook.utils.PreferenceSupplier;
import ebook.utils.Strings;
import ebook.utils.Utils;
import ebook.web.IJetty;

public class App {

	public enum Perspectives {
		lists, main;

		@Override
		public String toString() {
			switch (this) {
			case lists:
				return Strings.get("model.id.perspective.books");
			default:
				return Strings.get("model.id.perspective.default");
			}
		}

		public static Perspectives get(String string) {

			if (string.equalsIgnoreCase(Strings
					.get("model.id.perspective.books")))
				return lists;
			else
				return main;

		}
	}

	public enum ListParts {
		books, users, confs, current;

		@Override
		public String toString() {
			switch (this) {
			case books:
				return Strings.get("ebook.part.0");
			case users:
				return Strings.get("ebook.part.4");
			case confs:
				return Strings.get("ebook.part.confList");
			default:
				return "current";
			}
		}

		public static ListParts get(String id) {
			switch (id) {
			case "ebook.part.book":
				return ListParts.books;
			case "ebook.part.user":
				return ListParts.users;
			case "ebook.part.conf":
				return ListParts.confs;
			default:
				return ListParts.current;
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
	public static IProvisioningAgent agent;

	public static IServiceFactory srv = pico.get(IServiceFactory.class);
	public static IManagerFactory mng = pico.get(IManagerFactory.class);

	public static IBookClipboard clip = pico.get(IBookClipboard.class);

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
		App.app = application;

		MTrimmedWindow window = (MTrimmedWindow) modelService.find(
				"ebook.trimmedwindow.main", app);

		br.subscribe(UIEvents.UILifeCycle.APP_STARTUP_COMPLETE,
				new AppStartupCompleteEventHandler(window));

		br.subscribe(Events.EVENT_UPDATE_STATUS, new EVENT_UPDATE_STATUS());

		br.subscribe(Events.EVENT_START_JETTY, new EVENT_START_JETTY());
		br.subscribe(Events.EVENT_STOP_JETTY, new EVENT_STOP_JETTY());

		br.subscribe(Events.EVENT_UPDATE_PERSPECTIVE_ICON,
				new EVENT_UPDATE_PERSPECTIVE_ICON());

		br.subscribe(Events.RESTART_WORKBENCH, new RESTART_WORKBENCH());

		br.subscribe(Events.SHOW_UPDATE_AVAILABLE, new SHOW_UPDATE_AVAILABLE());

		// br.subscribe(Events.INSTALL_UPDATE, new INSTALL_UPDATE());

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
				} else
					throw new Exception();
			}

			// db.openConnection();

		} catch (Exception e) {
			Shell shell = (Shell) window.getWidget();
			if (MessageDialog.openQuestion(shell, Strings.get("appTitle"),
					Strings.get("error.initDb"))) {
				try {
					db.create();
					// db.openConnection();
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(shell, Strings.get("appTitle"),
							Strings.get("error.start"));
					IWorkbench workbench = window.getContext().get(
							IWorkbench.class);
					workbench.close();
				}
			}

			e.printStackTrace();
		}
	}

	// APP STARTUP, TRAY

	private static class AppStartupCompleteEventHandler implements EventHandler {
		private final MTrimmedWindow window;

		@Override
		public void handleEvent(Event event) {
			WindowCloseHandler closeHandler = new WindowCloseHandler();
			window.getContext().set(IWindowCloseHandler.class, closeHandler);
			App.br.post(Events.EVENT_UPDATE_STATUS, null);
			App.br.post(Events.EVENT_START_JETTY, null);

			perspectiveActions();

			openBookOnStartup();

			trayOptions();

			checkUpdates();

			showAboutOnStartup();
		}

		private void perspectiveActions() {

			currentPerspective = Perspectives.get(PreferenceSupplier
					.get(PreferenceSupplier.START_PERSPECTIVE));

			showPerspective(currentPerspective,
					ListParts.get(PreferenceSupplier
							.get(PreferenceSupplier.SELECTED_LIST)));

		}

		private void openBookOnStartup() {

			if (!PreferenceSupplier
					.getBoolean(PreferenceSupplier.OPEN_BOOK_ON_STARTUP))
				return;

			IPath p = new Path(
					PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
			if (p.isEmpty())
				return;

			jetty.setOpenBookOnStratUp();

			// App.mng.blm().open(p, (Shell) window.getWidget());
			//
			// App.br.post(Events.EVENT_SHOW_BOOK, null);

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
							open.setText("Открыть в браузере");
							open.addListener(SWT.Selection, new Listener() {
								@Override
								public void handleEvent(
										org.eclipse.swt.widgets.Event event) {

									Program.launch(App.getJetty().host());
									// IWorkbench workbench =
									// window.getContext()
									// .get(IWorkbench.class);
									// workbench.close();
								}
							});

							MenuItem exit = new MenuItem(m, SWT.NONE);
							exit.setText("Выход");
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

		private void checkUpdates() {

			BundleContext bundleContext = FrameworkUtil.getBundle(
					Activator.class).getBundleContext();
			ServiceReference<IProvisioningAgent> serviceReference = bundleContext
					.getServiceReference(IProvisioningAgent.class);
			agent = bundleContext.getService(serviceReference);
			if (agent == null) {
				System.out.println(">> no agent loaded!");
				return;
			}
			// Adding the repositories to explore
			if (!P2Util.addRepository(agent,
					PreferenceSupplier.get(PreferenceSupplier.UPDATE_SITE))) {
				System.out.println(">> could no add repostory!");
				agent = null;
				return;
			}
			// scheduling job for updates
			UpdateCheckJob job = new UpdateCheckJob();
			job.schedule();

		}

		private void showAboutOnStartup() {
			if (!PreferenceSupplier
					.getBoolean(PreferenceSupplier.SHOW_ABOUT_ON_STARTUP))
				return;

			App.br.post(Events.SHOW_ABOUT, null);

		}

		AppStartupCompleteEventHandler(MTrimmedWindow window) {
			this.window = window;
		}

	}

	// WINDOWS CLOSING

	public static class WindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			PreferenceSupplier.set(PreferenceSupplier.START_PERSPECTIVE,
					currentPerspective.toString());
			PreferenceSupplier.save();

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
					Strings.get("ebook.partstack.sections"), MPartStack.class,
					null);

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
							.get("ebook.partdescriptor.sectionView"))) {
						SectionView view = (SectionView) part.getObject();
						if (view != null)
							opt.openSections.add(view.getId());
					}

					if (id.equals(Strings
							.get("ebook.partdescriptor.sectionsBlockView"))) {
						SectionTextView view = (SectionTextView) part
								.getObject();
						if (view != null)
							opt.openSections.add(view.getId());
					}

					// if (id.equals(Strings
					// .get("ebook.partdescriptor.sectionsBlockView"))) {
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

	public static class ConfWindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			ConfConnection conf = window.getContext().get(ConfConnection.class);

			// book.srv().saveBookOptions(opt);

			conf.closeConnection();

			return true;
		}
	}

	// PERSPECTIVES
	// ******************************************************************

	// public static void togglePerspective() {
	// if (currentPerspective == Perspectives.lists)
	// currentPerspective = Perspectives.main;
	// else
	// currentPerspective = Perspectives.lists;
	// showPerspective(currentPerspective, ListParts.current);
	// }

	public static void showPerspective(Perspectives perspType,
			ListParts partType) {

		MPerspective persp;

		persp = (MPerspective) model.find(perspType.toString(), app);
		persp.setVisible(true);
		ps.switchPerspective(persp);

		currentPerspective = perspType;
		MPart part;

		if (partType != ListParts.current) {
			part = (MPart) model.find(partType.toString(), app);
			App.ps.activate(part);
		}

		br.post(Events.EVENT_UPDATE_PERSPECTIVE_ICON, null);
	}

	public static IJetty getJetty() {
		return jetty;
	}

}
