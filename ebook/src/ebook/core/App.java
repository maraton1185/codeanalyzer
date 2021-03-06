package ebook.core;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import org.eclipse.core.runtime.OperationCanceledException;
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
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
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
import ebook.core.exceptions.DbStructureException;
import ebook.core.interfaces.IClipboard;
import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IManagerFactory;
import ebook.core.interfaces.IServiceFactory;
import ebook.module.book.BookConnection;
import ebook.module.book.model.BookOptions;
import ebook.module.book.tree.SectionInfo;
import ebook.module.book.views.SectionView;
import ebook.module.book.views._SectionView;
import ebook.module.conf.ConfConnection;
import ebook.module.conf.model.ConfOptions;
import ebook.module.conf.views.ConfView;
import ebook.module.confLoad.services._FillProcLinkTableJob;
import ebook.module.db.DbOptions;
import ebook.module.tree.Clipboard;
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
				return Strings.model("model.id.perspective.books");
			default:
				return Strings.model("model.id.perspective.default");
			}
		}

		public static Perspectives get(String string) {

			if (string.equalsIgnoreCase(Strings
					.model("model.id.perspective.books")))
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
				return Strings.model("ebook.part.0");
			case users:
				return Strings.model("ebook.part.4");
			case confs:
				return Strings.model("ebook.part.confList");
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

	public static IClipboard bookClip = new Clipboard();
	public static IClipboard contextClip = new Clipboard();

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

			try {
				db.check();
			} catch (Exception e) {
				File f = new File(db.getFullName());
				if (!f.exists())
					db.create();
				else
					throw new DbStructureException();

			}

		} catch (Exception e) {
			Shell shell = (Shell) window.getWidget();
			if (MessageDialog.openQuestion(shell, Strings.title("appTitle"),
					Strings.msg("error.initDb"))) {
				try {
					db.create();
					// db.openConnection();
				} catch (Exception e1) {
					e1.printStackTrace();
					MessageDialog.openError(shell, Strings.title("appTitle"),
							Strings.msg("error.start"));
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

			// openBookOnStartup();

			trayOptions();

			checkUpdates();

			showAboutOnStartup();

			window.setLabel(PreferenceSupplier
					.get(PreferenceSupplier.APP_BRAND));
		}

		private void perspectiveActions() {

			currentPerspective = Perspectives.get(PreferenceSupplier
					.get(PreferenceSupplier.START_PERSPECTIVE));

			showPerspective(currentPerspective,
					ListParts.get(PreferenceSupplier
							.get(PreferenceSupplier.SELECTED_LIST)));

		}

		// private void openBookOnStartup() {
		//
		// if (!PreferenceSupplier
		// .getBoolean(PreferenceSupplier.OPEN_BOOK_ON_STARTUP))
		// return;
		//
		// IPath p = new Path(
		// PreferenceSupplier.get(PreferenceSupplier.BOOK_ON_STARTUP));
		// if (p.isEmpty())
		// return;
		//
		// jetty.setOpenBookOnStratUp();
		//
		// // App.mng.blm().open(p, (Shell) window.getWidget());
		// //
		// // App.br.post(Events.EVENT_SHOW_BOOK, null);
		//
		// }

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
					item.setToolTipText(Strings.title("appTitle"));
					item.setImage(image);

					item.addSelectionListener(new SelectionListener() {

						@Override
						public void widgetSelected(SelectionEvent e) {

							window.setVisible(true);

							shell.setMinimized(false);

							shell.forceActive();

						}

						@Override
						public void widgetDefaultSelected(SelectionEvent e) {
							// TODO Auto-generated method stub

						}
					});
					// item.addListener(SWT.DefaultSelection, handler);

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

									Program.launch(App.getJetty().host());
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

		private void checkUpdates() {

			if (!PreferenceSupplier
					.getBoolean(PreferenceSupplier.CHECK_UPDATE_ON_STARTUP))
				return;

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
			if (!P2Util.addRepository(agent, Strings.updateSite)) {
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

			PreferenceSupplier.set(PreferenceSupplier.SHOW_ABOUT_ON_STARTUP,
					false);
			PreferenceSupplier.save();
			// .getBoolean(PreferenceSupplier.SHOW_ABOUT_ON_STARTUP))

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

			if (PreferenceSupplier
					.getBoolean(PreferenceSupplier.MINIMIZE_TO_TRAY)) {

				Shell shell = ((Shell) window.getWidget());
				shell.setMinimized(true);
				return false;
			}

			// Integer id = (Integer) App.ctx
			// .get(Events.CONF_LIST_VIEW_COMPARISON);
			// if (id != null)
			// PreferenceSupplier.set(
			// PreferenceSupplier.CONF_LIST_VIEW_COMPARISON, id);

			PreferenceSupplier.set(PreferenceSupplier.START_PERSPECTIVE,
					currentPerspective.toString());
			PreferenceSupplier.save();

			IJobManager jobMan = Job.getJobManager();
			jobMan.cancel(_FillProcLinkTableJob.FillProcLinkTableJob_FAMILY);
			try {
				jobMan.join(_FillProcLinkTableJob.FillProcLinkTableJob_FAMILY,
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
			BookOptions opt = book.srv().getRootOptions(BookOptions.class);
			if (section != null)
				opt.selectedSection = section.getId();

			List<MPartStack> stacks = model.findElements(App.app,
					Strings.model("ebook.partstack.sections"),
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
							.model("ebook.partdescriptor.sectionView"))) {
						_SectionView view = (_SectionView) part.getObject();
						if (view != null)
							opt.openSections.add(view.getId());
					}

					if (id.equals(Strings
							.model("ebook.partdescriptor.sectionsBlockView"))) {
						SectionView view = (SectionView) part.getObject();
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

			Rectangle rect = new Rectangle(window.getX(), window.getY(),
					window.getWidth(), window.getHeight());
			PreferenceSupplier.set(PreferenceSupplier.WINDOW_SIZE,
					DbOptions.save(rect));
			PreferenceSupplier.save();

			book.srv().saveRootOptions(opt);

			book.closeConnection();

			return true;
		}
	}

	public static class ConfWindowCloseHandler implements IWindowCloseHandler {

		@Override
		public boolean close(MWindow window) {

			ConfConnection conf = window.getContext().get(ConfConnection.class);

			// ContextInfo section = window.getContext().get(ContextInfo.class);

			ConfOptions opt = conf.srv(null).getRootOptions(ConfOptions.class);
			// if (section != null)
			// opt.selectedSection = section.getId();

			List<MPartStack> stacks = model.findElements(App.app,
					Strings.model("ebook.partstack.conf"), MPartStack.class,
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

					if (id.equals(Strings.model("ebook.partdescriptor.0"))) {
						ConfView view = (ConfView) part.getObject();
						if (view != null)
							opt.openSections.add(view.getId());
					}

				}

			}

			Rectangle rect = new Rectangle(window.getX(), window.getY(),
					window.getWidth(), window.getHeight());
			PreferenceSupplier.set(PreferenceSupplier.WINDOW_SIZE,
					DbOptions.save(rect));
			PreferenceSupplier.save();

			conf.srv(null).saveRootOptions(opt);

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

	public static LinkedHashSet<String> TextFindHistory = new LinkedHashSet<String>();

}
