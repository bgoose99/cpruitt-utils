package com.android.bigthree.model;

public class Record
{
    private long id;
    private String date;
    private String description;
    private int weight;
    private int reps;
    private double max;

    public Record()
    {
        this( -1, "", "", 0, 0, 0.0 );
    }

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

    public long getId()
    {
        return id;
    }

    public void setId( long id )
    {
        this.id = id;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate( String date )
    {
        this.date = date;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription( String description )
    {
        this.description = description;
    }

    public int getWeight()
    {
        return weight;
    }

    public void setWeight( int weight )
    {
        this.weight = weight;
    }

    public int getReps()
    {
        return reps;
    }

    public void setReps( int reps )
    {
        this.reps = reps;
    }

    public double getMax()
    {
        return max;
    }

    public void setMax( double max )
    {
        this.max = max;
    }
}
