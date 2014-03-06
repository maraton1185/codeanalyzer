package codeanalyzer.core;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.model.application.ui.menu.MHandledToolItem;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

import codeanalyzer.core.interfaces.IAuthorize;
import codeanalyzer.utils.Const;
import codeanalyzer.utils.Strings;

public class E4Services {

	public static IEventBroker br;
	public static IEclipseContext ctx;
	public static UISynchronize sync;
	public static EModelService model;
	public static MApplication app;

	@Inject
	public E4Services(IEventBroker br, IEclipseContext ctx, UISynchronize sync,
			EModelService modelService, MApplication application) {
		E4Services.br = br;
		E4Services.ctx = ctx;
		E4Services.sync = sync;
		E4Services.model = modelService;
		E4Services.app = application;

	}

	@Inject
	@Optional
	public void showStatus(@UIEventTopic(Const.EVENT_UPDATE_STATUS) Object o) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				final String status = pico.get(IAuthorize.class).getInfo()
						.ShortMessage();
				E4Services.sync.asyncExec(new Runnable() {
					@Override
					public void run() {
						MHandledToolItem element = (MHandledToolItem) E4Services.model
								.find(Strings.get("model_id_activate"),
										E4Services.app);
						element.setLabel(status);
						element.setVisible(true);
					}
				});
			}
		}).start();
	}
}
