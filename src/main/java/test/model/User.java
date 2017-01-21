package test.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A default user class
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class User implements Serializable {
	
	@XmlElement
    private String id;
	@XmlElement
	private String email;
	@XmlElement
	private String name;
	@XmlElement
    private List<String> results;

	
    public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	public String getId()
    {
        return id;
    }

    public void setId( String id )
    {
        this.id = id;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getName()
    {
        return name;
    }


    public void setName( String name )
    {
        this.name = name;
    }
}
