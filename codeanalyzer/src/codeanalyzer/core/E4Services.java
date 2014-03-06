package codeanalyzer.core;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.e4.ui.di.UISynchronize;
import org.eclipse.e4.ui.model.application.MApplication;
import org.eclipse.e4.ui.workbench.modeling.EModelService;

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

}
