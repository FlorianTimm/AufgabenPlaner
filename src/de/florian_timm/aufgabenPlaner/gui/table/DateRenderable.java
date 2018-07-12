package de.florian_timm.aufgabenPlaner.gui.table;

import java.util.Calendar;
import java.util.Date;

@SuppressWarnings("serial")
public class DateRenderable extends Date {

	private boolean fertig;

	public DateRenderable(Date datum, boolean fertig) {
		super(datum.getTime());
		this.fertig = fertig;
	}

	public boolean isFertig() {
		return fertig;
	}

	public Calendar getCalendar() {
		Calendar datum = Calendar.getInstance();
		datum.setTime(this);
		return datum;
	}
}
