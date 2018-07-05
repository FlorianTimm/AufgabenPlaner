package de.florian_timm.aufgabenPlaner.gui.comp;

import java.awt.Window;
import javax.swing.JComboBox;
import javax.swing.SwingUtilities;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.entity.ordner.PersonenOrdner;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;
import de.florian_timm.aufgabenPlaner.kontroll.PersonenNotifier;

@SuppressWarnings("serial")
public class PersonChooser extends JComboBox<Person> implements EntityListener {
	private boolean active = false;
	private int preSelect = -1;

	public PersonChooser() {
		this(PersonenOrdner.getInstanz().getNutzer());
	}

	public PersonChooser(Person person) {
		this(person.getId());
	}

	public PersonChooser(int personId) {
		super();
		preSelect = personId;
		addAll(personId);
		PersonenNotifier.getInstanz().addListener(this);
	}

	private void addAll(int personId) {
		this.removeAllItems();
		for (Person p : PersonenOrdner.getInstanz().getArray()) {
			this.addItem(p);
			if (p.getId() == personId) {
				super.setSelectedItem(p);
			}
		}

	}

	@Override
	public void dataChanged() {
		Window topFrame = SwingUtilities.windowForComponent(this);
		if (topFrame != null && topFrame.isVisible() && this.isShowing()) {
			System.out.println("PersonChooser dataChanged");
			Person p = (Person) this.getSelectedItem();
			int pid = preSelect;
			if (p != null)
				pid = p.getId();
			addAll(pid);
			active = true;
		} else if (topFrame != null && !topFrame.isVisible() && active) {
			close();
		}

	}

	public void close() {
		PersonenNotifier.getInstanz().removeListener(this);
	}

}
