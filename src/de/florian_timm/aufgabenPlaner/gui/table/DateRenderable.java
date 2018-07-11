package de.florian_timm.aufgabenPlaner.gui.table;

import java.util.Calendar;
import java.util.Date;

public abstract interface DateRenderable {
	public abstract Calendar getCalendar();
	public abstract int getStatusAsZahl();
	public abstract Date getDate();
}
