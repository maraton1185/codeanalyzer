package updatesite;

import org.eclipse.e4.core.di.annotations.Execute;

public class UpdateHandler {

	@Execute
	public void execute() {
		UpdateInstallJob job = new UpdateInstallJob();
		job.schedule();
	}
}
