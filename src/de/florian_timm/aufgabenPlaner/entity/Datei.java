package de.florian_timm.aufgabenPlaner.entity;

import java.net.URL;
import java.util.Date;

public class Datei extends Entity {
    private Date datum;
    private String beschreibung;
    private URL url;

    public Datei(Date datum, String beschreibung, URL url) {
        super();
        this.datum = datum;
        this.beschreibung = beschreibung;
        this.url = url;
    }

    public Date getDatum() {
        return datum;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }
    
    public String toString () {
    	return getBeschreibung();
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((datum == null) ? 0 : datum.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		Datei other = (Datei) obj;
		if (beschreibung == null) {
			if (other.beschreibung != null)
				return false;
		} else if (!beschreibung.equals(other.beschreibung))
			return false;
		if (datum == null) {
			if (other.datum != null)
				return false;
		} else if (!datum.equals(other.datum))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public void update(Entity neu) {
		// TODO Auto-generated method stub
		
	}



}
