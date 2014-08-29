package ebook.module.tree;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.util.List;

public abstract class AbstractTreeService implements ITreeService {

	@Override
	public boolean check() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<ITreeItemInfo> getRoot() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(int parent) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITreeItemInfo get(int item) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void saveText(int id, String text) {
		// TODO Auto-generated method stub

	}

	@Override
	public Connection getConnection() throws IllegalAccessException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITreeItemInfo getLast(int parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ITreeItemInfo item) {
		// TODO Auto-generated method stub

	}

	@Override
	public void delete(ITreeItemSelection sel) {
		// TODO Auto-generated method stub

	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		// TODO Auto-generated method stub

	}

	@Override
	public void saveTitle(ITreeItemInfo object) {
		// TODO Auto-generated method stub

	}

	@Override
	public ITreeItemInfo findInParent(String title, Integer parent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ITreeItemInfo getSelected() {
		// TODO Auto-generated method stub
		return null;
	}

}
