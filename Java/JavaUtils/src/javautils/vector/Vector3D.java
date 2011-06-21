package javautils.vector;

/*******************************************************************************
 * This class represents a 3-dimensional vector.
 ******************************************************************************/
public class Vector3D
{
    public double x;
    public double y;
    public double z;

    /***************************************************************************
     * Default constructor.
     **************************************************************************/
    public Vector3D()
    {
        this( 0.0, 0.0, 0.0 );
    }

    /***************************************************************************
     * Constructor.
     * 
     * @param x
     * @param y
     * @param z
     **************************************************************************/
    public Vector3D( double x, double y, double z )
    {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /***************************************************************************
     * Returns the magnitude of this 3-D vector.
     * 
     * @return
     **************************************************************************/
    public double getMagnitude()
    {
        return Math.sqrt( x * x + y * y + z * z );
    }
}
