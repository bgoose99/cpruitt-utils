
#ifndef __Vector3D__
#define __Vector3D__

// local includes
#include "ISerializable.h"

/******************************************************************************
 * 
 *****************************************************************************/
class Vector3D : public ISerializable<Vector3D>
{
   public:
      
      static const int SIZEOF;
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      Vector3D();
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      Vector3D( const double &x, const double &y, const double &z );
      
      /************************************************************************
       * Copy constructor
       ***********************************************************************/
      Vector3D( const Vector3D &vec );
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~Vector3D();
      
      /************************************************************************
       * Returns the x component of this vector.
       ***********************************************************************/
      double X() const;
      
      /************************************************************************
       * Sets the x component of this vector.
       ***********************************************************************/
      void setX( const double &x );
      
      /************************************************************************
       * Returns the y component of this vector.
       ***********************************************************************/
      double Y() const;
      
      /************************************************************************
       * Sets the y component of this vector.
       ***********************************************************************/
      void setY( const double &y );
      
      /************************************************************************
       * Returns the z component of this vector.
       ***********************************************************************/
      double Z() const;
      
      /************************************************************************
       * Sets the z component of this vector.
       ***********************************************************************/
      void setZ( const double &z );
      
      /************************************************************************
       * Returns the magnitude of this vector.
       ***********************************************************************/
      double magnitude() const;
      
      /************************************************************************
       * Normalizes this vector. (Converts this vector to a unit vector.)
       ***********************************************************************/
      Vector3D &normalize();
      
      /************************************************************************
       * Returns a normalized vector in the direction of this vector.
       ***********************************************************************/
      Vector3D normalized() const;
      
      /************************************************************************
       * Rotates this vector about the supplied axis by the supplied angle.
       * NOTE: angle must be in radians. Positive angles are measured CCW.
       ***********************************************************************/
      Vector3D &rotate( const Vector3D &axis, const double &angle );
      
      /************************************************************************
       * Returns a vector that has been rotated about the supplied axis by
       * the supplied angle.
       ***********************************************************************/
      Vector3D rotated( const Vector3D &axis, const double &angle ) const;
      
      /************************************************************************
       * Assignment operator.
       ***********************************************************************/
      Vector3D &operator=( const Vector3D &vec );
      
      /************************************************************************
       * Addition operator.
       ***********************************************************************/
      const Vector3D operator+( const Vector3D &vec ) const;
      
      /************************************************************************
       * Subtraction operator.
       ***********************************************************************/
      const Vector3D operator-( const Vector3D &vec ) const;
      
      /************************************************************************
       * Multiplication operator.
       ***********************************************************************/
      const Vector3D operator*( const double &scale ) const;
      
      /************************************************************************
       * Division operator.
       ***********************************************************************/
      const Vector3D operator/( const double &divisor ) const;
      
      /************************************************************************
       * Equality operator.
       ***********************************************************************/
      bool operator==( const Vector3D &vec ) const;
      
      /************************************************************************
       * Inequality operator.
       ***********************************************************************/
      bool operator!=( const Vector3D &vec ) const;
      
      /************************************************************************
       * Addition operator.
       ***********************************************************************/
      Vector3D &operator+=( const Vector3D &vec );
      
      /************************************************************************
       * Subtraction operator.
       ***********************************************************************/
      Vector3D &operator-=( const Vector3D &vec );
      
      /************************************************************************
       * Multiplication operator.
       ***********************************************************************/
      Vector3D &operator*=( const double &scale );
      
      /************************************************************************
       * Division operator.
       ***********************************************************************/
      Vector3D &operator/=( const double &divisor );
      
      /************************************************************************
       * Returns the cross product of the two supplied vectors.
       ***********************************************************************/
      static Vector3D crossProduct( const Vector3D &v1, const Vector3D &v2 );
      
      /************************************************************************
       * Returns the scalar dot product of the two supplied vectors.
       ***********************************************************************/
      static double dotProduct( const Vector3D &v1, const Vector3D &v2 );
      
      /************************************************************************
       * Serialization functions.
       ***********************************************************************/
      virtual void toBytes( char *buf ) const;
      virtual Vector3D &fromBytes( const char *buf );
      virtual int binarySize() const;
      virtual void toBinaryStream( std::ostream &out ) const;
      virtual Vector3D &fromBinaryStream( std::istream &in );
      
   private:
      
      double x;
      double y;
      double z;
      double mag;
      
      void calculateMagnitude();
      
};

#endif
