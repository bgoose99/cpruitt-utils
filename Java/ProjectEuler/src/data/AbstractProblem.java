package data;

/*******************************************************************************
 * 
 ******************************************************************************/
public abstract class AbstractProblem
{
    protected int number;
    protected String description;

    /***************************************************************************
     * Returns the number of this problem.
     * 
     * @return
     **************************************************************************/
    public int getNumber()
    {
        return number;
    }

    /***************************************************************************
     * Sets the number of this problem.
     * 
     * @param number
     **************************************************************************/
    protected void setNumber( int number )
    {
        this.number = number;
    }

    /***************************************************************************
     * Returns the description of this problem.
     * 
     * @return
     **************************************************************************/
    public String getDescription()
    {
        return description;
    }

    /***************************************************************************
     * Sets the description of this problem.
     * 
     * @param description
     **************************************************************************/
    protected void setDescription( String description )
    {
        this.description = description;
    }

    /***************************************************************************
     * Returns the solution to this problem.
     **************************************************************************/
    public abstract String getSolution();

    @Override
    public String toString()
    {
        return "Problem " + number;
    }
}
