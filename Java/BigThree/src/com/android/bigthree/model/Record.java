package com.android.bigthree.model;

/*******************************************************************************
 * This class represents a single exercise record.
 ******************************************************************************/
public class Record
{
    private long id;
    private String date;
    private String description;
    private int weight;
    private int reps;
    private double max;

    /***************************************************************************
     * Constructor
     **************************************************************************/
    public Record()
    {
        this( -1, "", "", 0, 0, 0.0 );
    }

    /***************************************************************************
     * Constructor
     * 
     * @param id
     * @param date
     * @param description
     * @param weight
     * @param reps
     * @param max
     **************************************************************************/
    public Record( long id, String date, String description, int weight,
            int reps, double max )
    {
        setId( id );
        setDate( date );
        setDescription( description );
        setWeight( weight );
        setReps( reps );
        setMax( max );
    }

    /***************************************************************************
     * Returns the id associated with this record.
     * 
     * @return
     **************************************************************************/
    public long getId()
    {
        return id;
    }

    /***************************************************************************
     * Sets the id associated with this record.
     * 
     * @param id
     **************************************************************************/
    public void setId( long id )
    {
        this.id = id;
    }

    /***************************************************************************
     * Returns the date associated with this record.
     * 
     * @return
     **************************************************************************/
    public String getDate()
    {
        return date;
    }

    /***************************************************************************
     * Sets the date associated with this record.
     * 
     * @param date
     **************************************************************************/
    public void setDate( String date )
    {
        this.date = date;
    }

    /***************************************************************************
     * Returns the description associated with this record.
     * 
     * @return
     **************************************************************************/
    public String getDescription()
    {
        return description;
    }

    /***************************************************************************
     * Sets the description associated with this record.
     * 
     * @param description
     **************************************************************************/
    public void setDescription( String description )
    {
        this.description = description;
    }

    /***************************************************************************
     * Returns the weight associated with this record.
     * 
     * @return
     **************************************************************************/
    public int getWeight()
    {
        return weight;
    }

    /***************************************************************************
     * Sets the weight associated with this record.
     * 
     * @param weight
     **************************************************************************/
    public void setWeight( int weight )
    {
        this.weight = weight;
    }

    /***************************************************************************
     * Returns the reps associated with this record.
     * 
     * @return
     **************************************************************************/
    public int getReps()
    {
        return reps;
    }

    /***************************************************************************
     * Sets the reps associated with this record.
     * 
     * @param reps
     **************************************************************************/
    public void setReps( int reps )
    {
        this.reps = reps;
    }

    /***************************************************************************
     * Returns the max associated with this record.
     * 
     * @return
     **************************************************************************/
    public double getMax()
    {
        return max;
    }

    /***************************************************************************
     * Sets the max associated with this record.
     * 
     * @param max
     **************************************************************************/
    public void setMax( double max )
    {
        this.max = max;
    }
}
