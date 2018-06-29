package de.florian_timm.aufgabenPlaner.gui.comp;

import javax.swing.JComboBox;

import de.florian_timm.aufgabenPlaner.entity.Person;
import de.florian_timm.aufgabenPlaner.kontroll.EntityListener;

@SuppressWarnings("serial")
public class PersonChooser extends JComboBox<Person> implements EntityListener {

	public PersonChooser() {
		this(Person.getNutzer());
	}

	public PersonChooser(Person p) {
		super(Person.getArray());
		this.setSelectedItem(Person.getPerson(p.getId()));

		Person.addListener(this);
	}

	@Override
	public void dataChanged() {
		if (this.getItemCount() > 0) {
			int selection = ((Person) this.getSelectedItem()).getId();
			this.removeAllItems();
			for (Person p : Person.getArray()) {
				this.addItem(p);
				if (p.getId() == selection) {
					this.setSelectedItem(p);
				}
			}
		}
	}

}
