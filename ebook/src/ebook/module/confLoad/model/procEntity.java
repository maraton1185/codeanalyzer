package ebook.module.confLoad.model;

import java.lang.reflect.Field;
import java.util.List;

public class procEntity extends Entity {

	public int id;
	public String proc_name;
	public String proc_title;
	public String section;
	public StringBuilder text;
	public Boolean export;
	public String[] params;
	public List<procCall> calls;

	public procEntity(boolean init) {

		if (init)
			for (Field f : this.getClass().getFields()) {
				try {
					if (f.getType().isAssignableFrom(String.class))
						f.set(this, "");
					if (f.getType().isAssignableFrom(Boolean.class))
						f.set(this, false);
				} catch (Exception e) {
				}
			}

	}

	public procEntity(Entity line) {

		this(true);

		for (Field f : line.getClass().getFields()) {
			try {
				f.set(this, f.get(line));

			} catch (Exception e) {
			}
		}
	}
}
