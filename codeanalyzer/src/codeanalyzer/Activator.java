package codeanalyzer;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;


public class Activator implements BundleActivator {
	
	
	@Override
	public void start(BundleContext context) throws Exception {
		
		IEclipseContext ctx = E4Workbench.getServiceContext();
//		SnippetRepository repository = new SnippetRepository();
//		repository.repositoryPath = "test";		
//		ctx.set(SnippetRepository.class, repository);
		
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		//work

	}

}
