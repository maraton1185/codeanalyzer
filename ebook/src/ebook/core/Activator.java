package ebook.core;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.ui.internal.workbench.E4Workbench;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import ebook.core.interfaces.IDbConnection;
import ebook.core.interfaces.IManagerFactory;
import ebook.core.interfaces.IServiceFactory;

public class Activator implements BundleActivator {

	public static final String PLUGIN_ID = "ru.ebook";
	private static Activator bundle;

	@Override
	public void start(BundleContext context) throws Exception {

		IEclipseContext ctx = E4Workbench.getServiceContext();
		// ctx.set(IConfManager.class, pico.get(IConfManager.class));
		// pico.get(IConfManager.class).init();

		// ctx.set(IBookListManager.class, pico.get(IBookListManager.class));
		// ctx.set(IUserManager.class, pico.get(IUserManager.class));
		// ctx.set(IConfManager.class, pico.get(IConfManager.class));
		ctx.set(IDbConnection.class, pico.get(IDbConnection.class));

		ctx.set(IServiceFactory.class, pico.get(IServiceFactory.class));
		ctx.set(IManagerFactory.class, pico.get(IManagerFactory.class));

		bundle = this;

		Bundle bundle = Platform.getBundle("org.eclipse.equinox.http.registry");
		if (bundle.getState() == Bundle.RESOLVED) {
			bundle.start(Bundle.START_TRANSIENT);
		}

		// App.getJetty().startJetty();

		Bundle bundle1 = Platform.getBundle("org.mozilla.xulrunner"); //$NON-NLS-1$  
		if (bundle1 != null) {
			URL resourceUrl = bundle1.getResource("xulrunner"); //$NON-NLS-1$
			if (resourceUrl != null) {
				try {
					URL fileUrl = FileLocator.toFileURL(resourceUrl);
					File file = new File(fileUrl.toURI());
					System.setProperty("org.eclipse.swt.browser.DefaultType",
							"mozilla");
					System.setProperty(
							"org.eclipse.swt.browser.XULRunnerPath", file.getAbsolutePath()); //$NON-NLS-1$

					// Mozilla mozilla = Mozilla.getInstance();
					// org.mozilla.interfaces.nsIServiceManager serviceManager =
					// mozilla
					// .getServiceManager();
					// org.mozilla.interfaces.nsIPrefBranch prefs =
					// (org.mozilla.interfaces.nsIPrefBranch) serviceManager
					// .getServiceByContractID(
					// "@mozilla.org/preferences-service;1",
					// org.mozilla.interfaces.nsIPrefBranch.NS_IPREFBRANCH_IID);
					// prefs.setCharPref("capability.policy.policynames",
					// "allowclipboard");
					// prefs.setCharPref(
					// "capability.policy.allowclipboard.Clipboard.cutcopy",
					// "allAccess");
					// prefs.setCharPref(
					// "capability.policy.allowclipboard.Clipboard.paste",
					// "allAccess");
					// prefs.setCharPref("capability.policy.allowclipboard.sites",
					// "file://");

					// out.println("capability.policy.policynames, allowclipboard");
					// out.println("capability.policy.allowclipboard.Clipboard.cutcopy,allAccess");
					// out.println("capability.policy.allowclipboard.Clipboard.paste,allAccess");
					// out.println("capability.policy.allowclipboard.sites,file://");

					// int /* long */[] result = new int /* long */[1];
					// int rc = XPCOM.NS_GetServiceManager(result);
					// if (rc != XPCOM.NS_OK)
					// error(rc);
					// if (result[0] == 0)
					// error(XPCOM.NS_NOINTERFACE);
					// org.mozilla.xpcom.Mozilla
					// org.eclipse.swt.internal.mozilla.
					// nsIServiceManager serviceManager = new nsIServiceManager(
					// result[0]);
					//
					// nsIServiceManager serviceManager = new nsIServiceManager(
					// result[0]);
					// result[0] = 0;
					// byte[] buffer = MozillaDelegate.wcsToMbcs(null ,
					// XPCOM.NS_OBSERVER_CONTRACTID, true);
					// rc =
					// serviceManager.GetService("@mozilla.org/preferences-service;1",
					// nsIPrefService.NS_IPREFSERVICE_IID, result);
					// if (rc != XPCOM.NS_OK)
					// error(rc);
					// if (result[0] == 0)
					// error(XPCOM.NS_NOINTERFACE);
					//
					// serviceManager.GetServiceByContractID(
					// "@mozilla.org/preferences-service;1",
					// nsIPrefBranch.NS_IPREFBRANCH_IID);
					// prefs.setBoolPref("capability.policy.policynames",
					// "allowclipboard");
					// org.eclipse.swt.browser.MozillaDelegate
					// delegate = new MozillaDelegate(browser);
					// org.eclipse.swt.internal.mozilla.init.mo mozilla.init.
					// MozillaDelegate loadClass = Activator
					// .getDefault()
					// .getClass()
					// .getClassLoader()
					// .loadClass(
					// "org.eclipse.swt.browser.MozillaDelegate");
					// Method declaredMethod = loadClass
					// .getDeclaredMethod("wcsToMbcs");
					// declaredMethod.setAccessible(true);
					// byte[] buffer = (byte[]) declaredMethod.invoke(
					// "capability.policy.policynames", "allowclipboard",
					// true);
					// XPCOM.buffer = MozillaDelegate.wcsToMbcs(null,
					// PREFERENCE_DISABLEWINDOWSTATUSCHANGE, true);
					// rc = prefBranch.SetBoolPref(buffer, 0);
					// if (rc != XPCOM.NS_OK) {
					// browser.dispose();
					// error(rc);
					// }
					// File userPrefs = new File(profilePath + File.separator
					// + "prefs.js");
					// System.out.println(userPrefs.toString());
					//

					// Class<?> loadClass = Activator
					// .getDefault()
					// .getClass()
					// .getClassLoader()
					// .loadClass(
					// "org.eclipse.swt.browser.MozillaDelegate");
					// Method declaredMethod = loadClass
					// .getDeclaredMethod("getProfilePath");
					// declaredMethod.setAccessible(true);
					// String profilePath = (String)
					// declaredMethod.invoke(null);
					// File userPrefs = new File(profilePath + File.separator
					// + "prefs.js");
					//
					// try (PrintWriter out = new PrintWriter(new
					// BufferedWriter(
					// new FileWriter(userPrefs)))) {
					// out.println("user_pref(\"capability.policy.policynames\", \"allowclipboard\");");
					// out.println("user_pref(\"capability.policy.allowclipboard.Clipboard.cutcopy\", \"allAccess\");");
					// out.println("user_pref(\"capability.policy.allowclipboard.Clipboard.paste\", \"allAccess\");");
					// out.println("user_pref(\"capability.policy.allowclipboard.sites\", \"localhost\");");
					//
					// //
					// out.println("capability.policy.policynames, allowclipboard");
					// //
					// out.println("capability.policy.allowclipboard.Clipboard.cutcopy,allAccess");
					// //
					// out.println("capability.policy.allowclipboard.Clipboard.paste,allAccess");
					// //
					// out.println("capability.policy.allowclipboard.sites,file://");
					//
					// } catch (IOException e) {
					// // exception handling left as an exercise for the reader
					// }

				} catch (IOException e) {
					e.printStackTrace();
				} catch (URISyntaxException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		// E4Services.disposed = true;
		// IJobManager jobMan = Job.getJobManager();
		// jobMan.cancel(FillProcLinkTableJob.MY_FAMILY);
		// jobMan.join(FillProcLinkTableJob.MY_FAMILY, null);
	}

	public static Activator getDefault() {
		return bundle;
	}
}
