package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

import org.eclipse.core.runtime.IPath;

import ebook.module.conf.tree.ContextInfo;
import ebook.module.text.interfaces.ITextTreeService;

public abstract class AbstractTreeService implements ITreeService,
		ITextTreeService, IDownloadService {

	@Override
	public String download(IPath zipFolder, ITreeItemSelection selection,
			String zipName, boolean clear) throws InvocationTargetException {

		return null;
	}

	@Override
	public ITreeItemInfo upload(String path, ITreeItemInfo item, boolean clear,
			boolean relative) throws InvocationTargetException {

		return null;
	}

	@Override
	public ITreeItemInfo getModule(ITreeItemInfo item) {

		return null;
	}

	@Override
	public List<ITreeItemInfo> getParents(ITreeItemInfo item) {

		return null;
	}

	@Override
	public String getPath(ContextInfo item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ContextInfo getByPath(String path) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean check() {

		return false;
	}

	@Override
	public List<ITreeItemInfo> getRoot() {

		return null;
	}

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {

		return null;
	}

	@Override
	public boolean hasChildren(int parent) {

		return false;
	}

	@Override
	public ITreeItemInfo get(int item) {

		return null;
	}

	@Override
	public String getText(int id) {

		return null;
	}

	@Override
	public void saveText(int id, String text) {

	}

	@Override
	public Connection getConnection() throws IllegalAccessException {

		return null;
	}

	@Override
	public ITreeItemInfo getLast(int parent) {

		return null;
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException {

	}

	@Override
	public void delete(ITreeItemInfo item) {

	}

	@Override
	public void delete(ITreeItemSelection sel) {

	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {

		return null;
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {

		return null;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {

		return null;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {

	}

	@Override
	public void saveTitle(ITreeItemInfo object) {

	}

	@Override
	public ITreeItemInfo findInParent(String title, Integer parent) {

		return null;
	}

	@Override
	public ITreeItemInfo getSelected() {

		return null;
	}

	@Override
	public ITreeItemInfo getUploadRoot() {

		return null;
	}

	@Override
	public void edit(ITreeItemInfo item) {
		// TODO Auto-generated method stub

	}

}
