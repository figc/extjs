package test.model;

import java.util.List;

/**
 * A default user class
 */
public class User
{
    private String id;
    private String email;
    private String name;
    
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
