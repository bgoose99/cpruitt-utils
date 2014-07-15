
// system includes
#include <cmath>
#include <cstring>

// local includes
#include "Vector3D.h"

const int Vector3D::SIZEOF = sizeof(double) * 3;

/******************************************************************************
 *
 *****************************************************************************/
Vector3D::Vector3D() :
   x( 0.0 ), y( 0.0 ), z( 0.0 )
{
   calculateMagnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D::Vector3D( const double &x, const double &y, const double &z ) :
   x( x ), y( y ), z( z )
{
   calculateMagnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D::Vector3D( const Vector3D &vec ) :
   x( vec.x ), y( vec.y ), z( vec.z ), mag( vec.mag )
{
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D::~Vector3D()
{
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::X() const
{
   return x;
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::setX( const double &x )
{
   this->x = x;
   calculateMagnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::Y() const
{
   return y;
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::setY( const double &y )
{
   this->y = y;
   calculateMagnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::Z() const
{
   return z;
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::setZ( const double &z )
{
   this->z = z;
   calculateMagnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::magnitude() const
{
   return mag;
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::distance( const Vector3D &v ) const
{
   return ( *this - v ).magnitude();
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::normalize()
{
   *this = this->normalized();
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D Vector3D::normalized() const
{
   return Vector3D( x / mag, y / mag, z / mag );
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::rotate( const Vector3D &axis, const double &angle )
{
   *this = this->rotated( axis, angle );
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D Vector3D::rotated( const Vector3D &axis, const double &angle ) const
{
   Vector3D a = axis.normalized();

   double xx = 0.0;
   double yy = 0.0;
   double zz = 0.0;
   double c = cos( angle );
   double s = sin( angle );

   xx += ( c + ( 1.0 - c ) * a.x * a.x ) * x;
   xx += ( ( 1.0 - c ) * a.x * a.y - a.z * s ) * y;
   xx += ( ( 1.0 - c ) * a.x * a.z + a.y * s ) * z;

   yy += ( ( 1.0 - c ) * a.x * a.y + a.z * s ) * x;
   yy += ( c + ( 1.0 - c ) * a.y * a.y ) * y;
   yy += ( ( 1.0 - c ) * a.y * a.z - a.x * s ) * z;

   zz += ( ( 1.0 - c ) * a.x * a.z - a.y * s ) * x;
   zz += ( ( 1.0 - c ) * a.y * a.z + a.x * s ) * y;
   zz += ( c + ( 1.0 - c ) * a.z * a.z ) * z;

   return Vector3D( xx, yy, zz );
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::operator=( const Vector3D &vec )
{
   if( this != &vec )
   {
      x = vec.x;
      y = vec.y;
      z = vec.z;
      calculateMagnitude();
   }

   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
const Vector3D Vector3D::operator+( const Vector3D &vec ) const
{
   return Vector3D( x + vec.x, y + vec.y, z + vec.z );
}

/******************************************************************************
 *
 *****************************************************************************/
const Vector3D Vector3D::operator-( const Vector3D &vec ) const
{
   return Vector3D( x - vec.x, y - vec.y, z - vec.z );
}

/******************************************************************************
 *
 *****************************************************************************/
const Vector3D Vector3D::operator*( const double &scale ) const
{
   return Vector3D( x * scale, y * scale, z * scale );
}

/******************************************************************************
 *
 *****************************************************************************/
const Vector3D Vector3D::operator/( const double &divisor ) const
{
   return Vector3D( x / divisor, y / divisor, z / divisor );
}

/******************************************************************************
 *
 *****************************************************************************/
bool Vector3D::operator==( const Vector3D &vec ) const
{
   if( this == &vec )
      return true;
   return ( distance( vec ) < 1e-6 );
}

/******************************************************************************
 *
 *****************************************************************************/
bool Vector3D::operator!=( const Vector3D &vec ) const
{
   return !( *this == vec );
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::operator+=( const Vector3D &vec )
{
   x += vec.x;
   y += vec.y;
   z += vec.z;
   calculateMagnitude();
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::operator-=( const Vector3D &vec )
{
   x -= vec.x;
   y -= vec.y;
   z -= vec.z;
   calculateMagnitude();
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::operator*=( const double &scale )
{
   x *= scale;
   y *= scale;
   z *= scale;
   calculateMagnitude();
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::operator/=( const double &divisor )
{
   x /= divisor;
   y /= divisor;
   z /= divisor;
   calculateMagnitude();
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D Vector3D::crossProduct( const Vector3D &v1, const Vector3D &v2 )
{
   return Vector3D( v1.y * v2.z - v1.z * v2.y,
      v1.z * v2.x - v1.x * v2.z,
      v1.x * v2.y - v1.y * v2.x );
}

/******************************************************************************
 *
 *****************************************************************************/
double Vector3D::dotProduct( const Vector3D &v1, const Vector3D &v2 )
{
   return ( v1.x * v2.x + v1.y * v2.y + v1.z * v2.z );
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::calculateMagnitude()
{
   mag = sqrt( x * x + y * y + z * z );
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::toBytes( char *buf ) const
{
   int offset = 0;
   memcpy( &buf[offset], &x, sizeof( x ) ); offset += sizeof( x );
   memcpy( &buf[offset], &y, sizeof( y ) ); offset += sizeof( y );
   memcpy( &buf[offset], &z, sizeof( z ) );
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::fromBytes( const char *buf )
{
   int offset = 0;
   memcpy( &x, &buf[offset], sizeof( x ) ); offset += sizeof( x );
   memcpy( &y, &buf[offset], sizeof( y ) ); offset += sizeof( y );
   memcpy( &z, &buf[offset], sizeof( z ) );
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
int Vector3D::binarySize() const
{
   return Vector3D::SIZEOF;
}

/******************************************************************************
 *
 *****************************************************************************/
void Vector3D::toBinaryStream( std::ostream &out ) const
{
   out.write( (char *)&x, sizeof( x ) );
   out.write( (char *)&y, sizeof( y ) );
   out.write( (char *)&z, sizeof( z ) );
}

/******************************************************************************
 *
 *****************************************************************************/
Vector3D &Vector3D::fromBinaryStream( std::istream &in )
{
   in.read( (char *)&x, sizeof( x ) );
   in.read( (char *)&y, sizeof( y ) );
   in.read( (char *)&z, sizeof( z ) );
   calculateMagnitude();
   return *this;
}
