package javautils.vector;

import java.util.Random;

import javautils.game.Sprite;

/*******************************************************************************
 * This class contains some useful vector manipulation functions.
 * 
 * @see Vector2D
 * @see Vector3D
 * 
 ******************************************************************************/
public final class VectorUtils
{
    /***************************************************************************
     * Standard 2D distance formula. In this case, the supplied vectors are
     * treated as 2D Cartesian coordinates.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static double calculateDistance( Vector2D v1, Vector2D v2 )
    {
        double x = Math.pow( v1.x - v2.x, 2.0 );
        double y = Math.pow( v1.y - v2.y, 2.0 );
        return Math.sqrt( x + y );
    }

    /***************************************************************************
     * Standard 3D distance formula. In this case, the supplied vectors are
     * treated as 3D Cartesian coordinates.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static double calculateDistance( Vector3D v1, Vector3D v2 )
    {
        double x = Math.pow( v1.x - v2.x, 2.0 );
        double y = Math.pow( v1.y - v2.y, 2.0 );
        double z = Math.pow( v1.z - v2.z, 2.0 );
        return Math.sqrt( x + y + z );
    }

    /***************************************************************************
     * Calculates the dot product of the two supplied vectors.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static double calculateDotProduct( Vector2D v1, Vector2D v2 )
    {
        return ( ( v1.x * v2.x ) + ( v1.y * v2.y ) );
    }

    /***************************************************************************
     * Calculates the dot product of the two supplied vectors.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static double calculateDotProduct( Vector3D v1, Vector3D v2 )
    {
        return ( ( v1.x * v2.x ) + ( v1.y * v2.y ) + ( v1.z * v2.z ) );
    }

    /***************************************************************************
     * Calculates the cross product of the two supplied vectors.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static Vector3D calculateCrossProduct( Vector3D v1, Vector3D v2 )
    {
        double i = v1.y * v2.z - v1.z - v2.y;
        double j = v1.z * v2.x - v1.x * v2.z;
        double k = v1.x * v2.y - v1.y * v2.x;
        return new Vector3D( i, j, k );
    }

    /***************************************************************************
     * Add two vectors.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static Vector2D addVector( Vector2D v1, Vector2D v2 )
    {
        return new Vector2D( v1.x + v2.x, v1.y + v2.y );
    }

    /***************************************************************************
     * Add two vectors.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static Vector3D addVector( Vector3D v1, Vector3D v2 )
    {
        return new Vector3D( v1.x + v2.x, v1.y + v2.y, v1.z + v2.z );
    }

    /***************************************************************************
     * Subtract two vectors.
     * 
     * @param v1
     * @param v2
     * @return v1 - v2
     **************************************************************************/
    public static Vector2D subtractVector( Vector2D v1, Vector2D v2 )
    {
        return new Vector2D( v1.x - v2.x, v1.y - v2.y );
    }

    /***************************************************************************
     * Subtract two vectors.
     * 
     * @param v1
     * @param v2
     * @return v1 - v2
     **************************************************************************/
    public static Vector3D subtractVector( Vector3D v1, Vector3D v2 )
    {
        return new Vector3D( v1.x - v2.x, v1.y - v2.y, v1.z - v2.z );
    }

    /***************************************************************************
     * Scales a vector.
     * 
     * @param v
     * @param scale
     * @return
     **************************************************************************/
    public static Vector2D scaleVector( Vector2D v, double scale )
    {
        return new Vector2D( v.x * scale, v.y * scale );
    }

    /***************************************************************************
     * Scales a vector.
     * 
     * @param v
     * @param scale
     * @return
     **************************************************************************/
    public static Vector3D scaleVector( Vector3D v, double scale )
    {
        return new Vector3D( v.x * scale, v.y * scale, v.z * scale );
    }

    /***************************************************************************
     * Rotates a vector.
     * 
     * @param v
     * @param radians
     * @return
     **************************************************************************/
    public static Vector2D rotateVector( Vector2D v, double radians )
    {
        double x = v.x * Math.cos( radians ) - v.y * Math.sin( radians );
        double y = v.y * Math.cos( radians ) + v.x * Math.sin( radians );
        return new Vector2D( x, y );
    }

    /***************************************************************************
     * Returns a vector of length 1.0 that points in the same direction as the
     * supplied vector.
     * 
     * @param v
     * @return
     **************************************************************************/
    public static Vector2D calculateUnitVector( Vector2D v )
    {
        return scaleVector( v, 1.0 / v.getMagnitude() );
    }

    /***************************************************************************
     * Returns a vector of length 1.0 that points in the same direction as the
     * supplied vector.
     * 
     * @param v
     * @return
     **************************************************************************/
    public static Vector3D calculateUnitVector( Vector3D v )
    {
        return scaleVector( v, 1.0 / v.getMagnitude() );
    }

    /***************************************************************************
     * Calculates the unit normal vector that points from v1 to v2.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static Vector2D calculateUnitNormalVector( Vector2D v1, Vector2D v2 )
    {
        Vector2D normal = subtractVector( v2, v1 );
        return calculateUnitVector( normal );
    }

    /***************************************************************************
     * Calculates the unit normal vector that points from v1 to v2.
     * 
     * @param v1
     * @param v2
     * @return
     **************************************************************************/
    public static Vector3D calculateUnitNormalVector( Vector3D v1, Vector3D v2 )
    {
        Vector3D normal = subtractVector( v2, v1 );
        return calculateUnitVector( normal );
    }

    /***************************************************************************
     * Returns a random downward unit vector. This is accomplished by creating a
     * vector with a (Gaussian) random x velocity, scaled by x, and a y velocity
     * given by y. A unit vector is then calculated from the resulting vector.
     * 
     * @param x
     *            velocity in the x direction, scaled by Random.nextGaussian()
     * @param y
     *            velocity in the y direction
     * @return
     * @see java.util.Random#nextGaussian()
     **************************************************************************/
    public static Vector2D getRandomDownwardUnitVector2D( double x, double y )
    {
        Random random = new Random();
        return calculateUnitVector( new Vector2D( random.nextGaussian() * x, y ) );
    }

    /***************************************************************************
     * Simulates a perfectly elastic 2D collision between two objects.
     * 
     * @param p1
     *            centroid of object 1
     * @param v1
     *            velocity of object 1
     * @param mass1
     *            mass of object 1
     * @param p2
     *            centroid of object 2
     * @param v2
     *            velocity of object 2
     * @param mass2
     *            mass of object 2
     * @return an array of 2 new velocity vectors where <code>v[0]</code> is the
     *         new velocity vector of object 1, and <code>v[1]</code> is the new
     *         velocity vector of object 2
     **************************************************************************/
    public static Vector2D[] simulateElasticCollision( Vector2D p1,
            Vector2D v1, double mass1, Vector2D p2, Vector2D v2, double mass2 )
    {
        // calculate the normal vectors between the two position vectors
        Vector2D unitNormal = calculateUnitNormalVector( p1, p2 );
        Vector2D unitTan = new Vector2D( unitNormal.y * -1.0, unitNormal.x );

        // calculate the normal/tangential components of the velocity vectors
        double v1normal = calculateDotProduct( unitNormal, v1 );
        double v2normal = calculateDotProduct( unitNormal, v2 );
        double v1tan = calculateDotProduct( unitTan, v1 );
        double v2tan = calculateDotProduct( unitTan, v2 );

        // the tangential components of the new velocity vectors don't change
        // calculate the magnitude of the new normal components
        double v1normalNew = ( ( v1normal * ( mass1 - mass2 ) ) + ( 2 * mass2 * v2normal ) )
                / ( mass1 + mass2 );
        double v2normalNew = ( ( v2normal * ( mass2 - mass1 ) ) + ( 2 * mass1 * v1normal ) )
                / ( mass1 + mass2 );

        // calculate new normal/tangential components of the velocity vectors
        Vector2D v1normalPrime = scaleVector( unitNormal, v1normalNew );
        Vector2D v1tanPrime = scaleVector( unitTan, v1tan );
        Vector2D v2normalPrime = scaleVector( unitNormal, v2normalNew );
        Vector2D v2tanPrime = scaleVector( unitTan, v2tan );

        // combine the normal/tangential vectors to get new velocity vectors
        Vector2D[] vecArray = new Vector2D[2];
        vecArray[0] = addVector( v1normalPrime, v1tanPrime );
        vecArray[1] = addVector( v2normalPrime, v2tanPrime );

        return vecArray;
    }

    /***************************************************************************
     * Simulates an elastic collision between two {@link Sprite}s.
     * 
     * @param s1
     * @param s2
     **************************************************************************/
    public static void simulateElasticCollision( Sprite s1, Sprite s2 )
    {
        // calculate the normal vectors between the two position vectors
        Vector2D unitNormal = VectorUtils.calculateUnitNormalVector(
                s1.getPos(), s2.getPos() );
        Vector2D unitTan = new Vector2D( unitNormal.y * -1.0, unitNormal.x );

        // calculate the normal/tangential components of the velocity vectors
        double v1normal = VectorUtils.calculateDotProduct( unitNormal,
                s1.getVel() );
        double v2normal = VectorUtils.calculateDotProduct( unitNormal,
                s2.getVel() );
        double v1tan = VectorUtils.calculateDotProduct( unitTan, s1.getVel() );
        double v2tan = VectorUtils.calculateDotProduct( unitTan, s2.getVel() );

        // the tangential components of the new velocity vectors don't change
        // calculate the magnitude of the new normal components
        double v1normalNew = ( ( v1normal * ( s1.getMass() - s2.getMass() ) ) + ( 2 * s2
                .getMass() * v2normal ) ) / ( s1.getMass() + s2.getMass() );
        double v2normalNew = ( ( v2normal * ( s2.getMass() - s1.getMass() ) ) + ( 2 * s1
                .getMass() * v1normal ) ) / ( s1.getMass() + s2.getMass() );

        // calculate new normal/tangential components of the velocity vectors
        Vector2D v1normalPrime = VectorUtils.scaleVector( unitNormal,
                v1normalNew );
        Vector2D v1tanPrime = VectorUtils.scaleVector( unitTan, v1tan );
        Vector2D v2normalPrime = VectorUtils.scaleVector( unitNormal,
                v2normalNew );
        Vector2D v2tanPrime = VectorUtils.scaleVector( unitTan, v2tan );

        // move each sphere to the actual collision plane so that they don't
        // overlap each other
        // calculate amount of overlap (assumed, because they are colliding)
        double deltaDistance = ( s1.getCollisionRadius() + s2
                .getCollisionRadius() )
                - ( VectorUtils.calculateDistance( s1.getPos(), s2.getPos() ) );
        if( deltaDistance > 0 )
        {
            // we have to move them just a little further apart than
            // deltaDistance / 2
            // or they will continuously collide
            double d = ( deltaDistance / 2.0 ) + 2.0;
            // use unitNormal to move spheres back to collision plane
            s1.setPos( VectorUtils.addVector( s1.getPos(),
                    VectorUtils.scaleVector( unitNormal, -d ) ) );
            s2.setPos( VectorUtils.addVector( s2.getPos(),
                    VectorUtils.scaleVector( unitNormal, d ) ) );
        }

        // combine the normal/tangential vectors to get new velocity vectors
        s1.setVel( VectorUtils.addVector( v1normalPrime, v1tanPrime ) );
        s2.setVel( VectorUtils.addVector( v2normalPrime, v2tanPrime ) );
    }

    /***************************************************************************
     * Simulates a perfectly elastic collision between two objects.
     * 
     * @param p1
     *            centroid of object 1
     * @param v1
     *            velocity of object 1
     * @param mass1
     *            mass of object 1
     * @param p2
     *            centroid of object 2
     * @param v2
     *            velocity of object 2
     * @param mass2
     *            mass of object 2
     * @return an array of 2 new velocity vectors where <code>v[0]</code> is the
     *         new velocity vector of object 1, and <code>v[1]</code> is the new
     *         velocity vector of object 2
     **************************************************************************/
    public static Vector3D[] simulateElasticCollision( Vector3D p1,
            Vector3D v1, double mass1, Vector3D p2, Vector3D v2, double mass2 )
    {

        Vector3D[] vecArray = new Vector3D[2];
        return vecArray;
    }
}
