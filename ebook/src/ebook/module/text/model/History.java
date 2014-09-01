package ebook.module.text.model;

import java.util.ArrayList;

public class History {
	ArrayList<HistoryItem> list = new ArrayList<HistoryItem>();

	int index = 0;

	public void add(HistoryItem item) {
		if (list.contains(item)) {
			int i = list.indexOf(item);
			list.get(i).line = item.line;
			return;
		}

		int s = list.size();
		for (int i = index; i <= s - 1; i++)
			if (i >= 0)
				list.remove(list.size() - 1);
		// if (index == list.size())
		list.add(item);
		// else
		// list.add(index, item);

		// index++;
		index = list.size();

		// System.out.println(index);
	}

	public HistoryItem previous() {

		if (index <= 1)
			return null;

		index--;
		return list.get(index - 1);

	}

	public HistoryItem next() {
		if (index > list.size() - 1)
			return null;

		index++;
		return list.get(index - 1);

	}
}
