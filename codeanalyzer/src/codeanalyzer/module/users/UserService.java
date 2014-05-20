package codeanalyzer.module.users;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import codeanalyzer.module.tree.ITreeItemInfo;
import codeanalyzer.module.tree.ITreeService;

public class UserService implements ITreeService {

	@Override
	public List<ITreeItemInfo> getRoot() {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public List<ITreeItemInfo> getChildren(int parent) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasChildren(int parent) {
		// NEXT Auto-generated method stub
		return false;
	}

	@Override
	public ITreeItemInfo get(int item) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public ITreeItemInfo getLast(int parent) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public void add(ITreeItemInfo item, ITreeItemInfo parent, boolean sub)
			throws InvocationTargetException {
		// NEXT Auto-generated method stub

	}

	@Override
	public void delete(ITreeItemInfo item) {
		// NEXT Auto-generated method stub

	}

	@Override
	public Boolean setParent(ITreeItemInfo item, ITreeItemInfo target) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setAfter(ITreeItemInfo item, ITreeItemInfo target) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public Boolean setBefore(ITreeItemInfo item, ITreeItemInfo target) {
		// NEXT Auto-generated method stub
		return null;
	}

	@Override
	public void updateOrder(List<ITreeItemInfo> items) {
		// NEXT Auto-generated method stub

	}

	@Override
	public void saveTitle(ITreeItemInfo object) {
		// NEXT Auto-generated method stub

	}

	@Override
	public ITreeItemInfo get(Integer id) {
		// NEXT Auto-generated method stub
		return null;
	}
}
