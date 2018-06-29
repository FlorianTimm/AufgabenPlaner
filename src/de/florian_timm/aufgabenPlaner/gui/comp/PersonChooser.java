package de.florian_timm.aufgabenPlaner.gui.comp;

import javax.swing.JComboBox;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

@SuppressWarnings("serial")
public class PersonChooser extends JComboBox<Person> implements EntityListener {

	public PersonChooser() {
		super(Person.getArray());
		this.setSelectedItem(Person.getNutzer());

		Person.addListener(this);
	}

	@Override
	public void dataChanged() {

		if (this.isShowing()) {
			/*
			 * int selection = -1; if (this.getItemCount() > 0) { selection = ((Person)
			 * this.getSelectedItem()).getId(); } this.removeAllItems(); for (Person p :
			 * Person.getArray()) { this.addItem(p); if (p.getId() == selection) {
			 * this.setSelectedItem(p); } }
			 */
		} else {
			Person.removeListener(this);
		}

	}

}
