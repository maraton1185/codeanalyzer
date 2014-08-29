package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.IPath;

public interface IDownloadService {

	String download(IPath zipFolder, ITreeItemSelection selection,
			String zipName, boolean clear) throws InvocationTargetException;

	ITreeItemInfo upload(String path, ITreeItemInfo item, boolean clear,
			boolean relative) throws InvocationTargetException;

}
