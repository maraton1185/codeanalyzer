package codeanalyzer;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import codeanalyzer.core.pico;
import codeanalyzer.core.interfaces.IDbManager;


public class Activator implements BundleActivator {
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		
//		IEclipseContext ctx = E4Workbench.getServiceContext();
//		SnippetRepository repository = new SnippetRepository();
//		repository.repositoryPath = "test";		
//		ctx.set(SnippetRepository.class, repository);
		pico.get(IDbManager.class).init();
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//work

	}

}
