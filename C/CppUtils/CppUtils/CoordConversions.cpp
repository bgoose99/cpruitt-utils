
// system includes
#include <cmath>

// local includes
#include "CoordConversions.h"

#ifndef M_PI
#define M_PI 3.14159265359
#endif

namespace CoordConversions
{

   const double RAD_2_DEG = 180.0 / M_PI;
   const double DEG_2_RAD = M_PI / 180.0;
   const double A = 6378137.0;           // Earth semi-major axis
   const double B = 6356752.3;           // Earth semi-minor axis
   const double F = 1.0 / 298.257223563; // reciprocal flattening
   const double E2 = 2 * F - F * F;      // first eccentricity squared
   const double W = 0.00007292115856;    // angular velocity of Earth (radians/s)

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoECI( const Vector3D &ecf, const double &time )
   {
      double wt = -1.0 * W * time;
      return Vector3D( ecf.X() * cos( wt ) + ecf.Y() * sin( wt ),
         -1.0 * ecf.X() * sin( wt ) + ecf.Y() * cos( wt ),
         ecf.Z() );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoECIVel( const Vector3D &ecfPos, const Vector3D &ecfVel, const double &time )
   {
      return convertECFtoECI( Vector3D( ecfVel.X() + ( -1.0 * W * ecfPos.Y() ),
         ecfVel.Y() - ( -1.0 * W * ecfPos.X() ),
         ecfVel.Z() ), time );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoECIAcc( const Vector3D &ecfPos, const Vector3D &ecfVel, const Vector3D &ecfAcc, const double &time )
   {
      return convertECFtoECI( Vector3D( ecfAcc.X() - ( W * ( 2.0 * ecfVel.Y() + W * ecfPos.X() ) ),
         ecfAcc.Y() - ( W * ( -2.0 * ecfVel.X() + W * ecfPos.Y() ) ),
         ecfAcc.Z() ), time );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECItoECF( const Vector3D &eci, const double &time )
   {
      return convertECFtoECI( eci, -1.0 * time );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECItoECFVel( const Vector3D &eciPos, const Vector3D &eciVel, const double &time )
   {
      return convertECItoECF( Vector3D( eciVel.X() + ( W * eciPos.Y() ),
         eciVel.Y() - ( W * eciPos.X() ),
         eciVel.Z() ), time );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECItoECFAcc( const Vector3D &eciPos, const Vector3D &eciVel, const Vector3D &eciAcc, const double &time )
   {
      return convertECItoECF( Vector3D( eciAcc.X() + ( W * ( 2.0 * eciVel.Y() - W * eciPos.X() ) ),
         eciAcc.Y() - ( W * ( 2.0 * eciVel.X() + W * eciPos.Y() ) ),
         eciAcc.Z() ), time );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertLLAtoECF( const Vector3D &lla )
   {
      double lat = lla.X();
      double lon = lla.Y();
      double alt = lla.Z();

      while( lat < -90.0 ) lat += 180.0;
      while( lat >  90.0 ) lat -= 180.0;

      lat *= DEG_2_RAD;
      lon *= DEG_2_RAD;

      // lat correction
      if( cos( lat ) == 0.0 )
      {
         if( lat == ( 90.0 * DEG_2_RAD ) ) // north pole
            lat -= 1.0e-12;
         else
            lat += 1.0e-12;
      }

      double chi = sqrt( 1.0 - E2 * sin( lat ) * sin( lat ) );

      return Vector3D( ( A / chi + alt ) * cos( lat ) * cos( lon ),
         ( A / chi + alt ) * cos( lat ) * sin( lon ),
         ( A * ( 1.0 - E2 ) / chi + alt ) * sin( lat ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoLLA( const Vector3D &ecf )
   {
      double p = sqrt( ecf.X() * ecf.X() + ecf.Y() * ecf.Y() );
      double theta = atan( ecf.Z() * A / ( p * B ) );
      double sinTheta3 = pow( sin( theta ), 3.0 );
      double cosTheta3 = pow( cos( theta ), 3.0 );
      double nlat = ecf.Z() + ( ( A * A - B * B ) / B ) * sinTheta3;
      double dlat = p - E2 * A * cosTheta3;
      double lat = atan( nlat / dlat );
      double lon = atan2( ecf.Y(), ecf.X() );
      double temp = 1.0 - E2 * sin( lat ) * sin( lat );
      double n = ( temp < 0.0 ) ? A : A / sqrt( temp );
      double alt = p / cos( lat ) - n;

      return Vector3D( lat * RAD_2_DEG, lon * RAD_2_DEG, alt );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertENUtoRAE( const Vector3D &enu )
   {
      return Vector3D( enu.magnitude(),
         atan2( enu.X(), enu.Y() ),
         asin( enu.Z() / enu.magnitude() ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRAEtoENU( const Vector3D &rae )
   {
      return Vector3D( rae.X() * sin( rae.Y() ) * cos( rae.Z() ),
         rae.X() * cos( rae.Y() ) * cos( rae.Z() ),
         rae.X() * sin( rae.Z() ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoENU( const Vector3D &ecf, const Vector3D &ref )
   {
      Vector3D lla = convertECFtoLLA( ref );
      double sinLat = sin( lla.X() * DEG_2_RAD );
      double cosLat = cos( lla.X() * DEG_2_RAD );
      double sinLon = sin( lla.Y() * DEG_2_RAD );
      double cosLon = cos( lla.Y() * DEG_2_RAD );

      Vector3D diff = ecf - ref;

      return Vector3D( -1.0 * sinLon * diff.X() + cosLon * diff.Y(),
         -1.0 * sinLat * cosLon * diff.X() - sinLat * sinLon * diff.Y() + cosLat * diff.Z(),
         cosLat * cosLon * diff.X() + cosLat * sinLon * diff.Y() + sinLat * diff.Z() );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertENUtoECF( const Vector3D &enu, const Vector3D &ref )
   {
      Vector3D lla = convertECFtoLLA( ref );
      double sinLat = sin( lla.X() * DEG_2_RAD );
      double cosLat = cos( lla.X() * DEG_2_RAD );
      double sinLon = sin( lla.Y() * DEG_2_RAD );
      double cosLon = cos( lla.Y() * DEG_2_RAD );

      Vector3D temp( -1.0 * sinLon * enu.X() - sinLat * cosLon * enu.Y() + cosLat * cosLon * enu.Z(),
         cosLon * enu.X() - sinLat * sinLon * enu.Y() + cosLat * sinLon * enu.Z(),
         cosLat * enu.Y() + sinLat * enu.Z() );

      return temp + ref;
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoRAE( const Vector3D &ecf, const Vector3D &ref )
   {
      return convertENUtoRAE( convertECFtoENU( ecf, ref ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRAEtoECF( const Vector3D &rae, const Vector3D &ref )
   {
      return convertENUtoECF( convertRAEtoENU( rae ), ref );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRAEtoPhasedArray( const Vector3D &rae, const Vector3D &boresight )
   {
      double sa = sin( boresight.X() * DEG_2_RAD );
      double ca = cos( boresight.X() * DEG_2_RAD );
      double se = sin( boresight.Y() * DEG_2_RAD );
      double ce = cos( boresight.Y() * DEG_2_RAD );
      double sc = sin( boresight.Z() * DEG_2_RAD );
      double cc = cos( boresight.Z() * DEG_2_RAD );

      double x = rae.X() * ( -1.0 * cc * ca - sc * se * sa ) +
         rae.Y() * ( cc * sa - sc * se * ca ) +
         rae.Z() * sc * ce;
      double y = rae.X() * ( sc * ca - cc * se * sa ) +
         rae.Y() * ( -1.0 * sc * sa - cc * se * ca ) +
         rae.Z() * cc * ce;
      double z = rae.X() * ce * sa + rae.Y() * ce * ca + rae.Z() * se;

      return Vector3D( x, y, z );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertPhasedArraytoRUV( const Vector3D &p )
   {
      return Vector3D( p.magnitude(), p.X() / p.magnitude(), p.Y() / p.magnitude() );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertPhasedArraytoRUVVel( const Vector3D &p, const Vector3D &v )
   {
      Vector3D ruv = convertPhasedArraytoRUV( p );

      double rdot = ( p.X() * p.X() + p.Y() * p.Y() + p.Z() * p.Z() ) / ruv.X();
      double udot = ( p.X() - ruv.Y() * rdot ) / ruv.X();
      double vdot = ( p.Y() - ruv.Z() * rdot ) / ruv.X();

      return Vector3D( rdot, udot, vdot );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertPhasedArraytoRAE( const Vector3D &p, const Vector3D &boresight )
   {
      double sa = sin( boresight.X() * DEG_2_RAD );
      double ca = cos( boresight.X() * DEG_2_RAD );
      double se = sin( boresight.Y() * DEG_2_RAD );
      double ce = cos( boresight.Y() * DEG_2_RAD );
      double sc = sin( boresight.Z() * DEG_2_RAD );
      double cc = cos( boresight.Z() * DEG_2_RAD );

      double r = p.X() * ( -1.0 * cc * ca - sc * se * sa ) +
         p.Y() * ( sc * ca - cc * se * sa ) +
         p.Z() * ce * sa;
      double a = p.X() * ( cc * sa - sc * se * ca ) +
         p.Y() * ( -1.0 * sc * sa - cc * se * ca ) +
         p.Z() * ce * ca;
      double e = p.X() * sc * ce + p.Y() * cc * ce + p.Z() * se;

      return Vector3D( r, a, e );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRUVtoPhasedArray( const Vector3D &ruv )
   {
      double w = sqrt( 1.0 - ruv.Y() * ruv.Y() - ruv.Z() * ruv.Z() );

      return Vector3D( ruv.X() * ruv.Y(),
         ruv.X() * ruv.Z(),
         ruv.X() * w );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRUVtoPhasedArrayVel( const Vector3D &ruv, const Vector3D &ruvVel )
   {
      double w = sqrt( 1.0 - ruv.Y() * ruv.Y() - ruv.Z() * ruv.Z() );

      return Vector3D( ruvVel.X() * ruv.Y() + ruv.X() * ruvVel.Y(),
         ruvVel.X() * ruv.Z() + ruv.X() * ruvVel.Z(),
         ruvVel.X() * w - ( ruv.X() * ( ruv.Y() * ruvVel.Y() + ruv.Z() * ruvVel.Z() ) / w ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoRUV( const Vector3D &ecf, const Vector3D &ref, const Vector3D &boresight )
   {
      return convertPhasedArraytoRUV( convertRAEtoPhasedArray( convertECFtoRAE( ecf, ref ), boresight ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRUVtoECF( const Vector3D &ruv, const Vector3D &ref, const Vector3D &boresight )
   {
      return convertRAEtoECF( convertPhasedArraytoRAE( convertRUVtoPhasedArray( ruv ), boresight ), ref );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertECFtoRUVVel( const Vector3D &ecf, const Vector3D &ecfVel, const Vector3D &ref, const Vector3D &boresight )
   {
      Vector3D p = convertRAEtoPhasedArray( convertECFtoRAE( ecf, ref ), boresight );
      return convertPhasedArraytoRUVVel( p, convertRAEtoPhasedArray( convertECFtoRAE( ecfVel, ref ), boresight ) );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertRUVtoECFVel( const Vector3D &ruv, const Vector3D &ruvVel, const Vector3D &ref, const Vector3D &boresight )
   {
      return convertRAEtoECF( convertPhasedArraytoRAE( convertRUVtoPhasedArrayVel( ruv, ruvVel ), boresight ), ref );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector3D convertQuaternionsToEuler( const Vector4D &q )
   {
      return Vector3D(
         atan2( 2 * ( q.X() * q.Y() + q.W() * q.Z() ), q.W() * q.W() + q.X() * q.X() - q.Y() * q.Y() - q.Z() * q.Z() ),
         -1.0 * asin( 2 * q.X() * q.Z() - 2.0 * q.W() * q.Y() ),
         atan2( 2 * ( q.Y() * q.Z() + q.W() * q.X() ), q.W() * q.W() - q.X() * q.X() - q.Y() * q.Y() + q.Z() * q.Z() )
         );
   }

   /***************************************************************************
    *
    **************************************************************************/
   Vector4D convertEulerToQuaternions( const Vector3D &e )
   {
      return Vector4D(
         cos( e.X() / 2 ) * cos( e.Y() / 2 ) * cos( e.Z() / 2 ) + sin( e.X() / 2 ) * sin( e.Y() / 2 ) * sin( e.Z() / 2 ),
         cos( e.X() / 2 ) * cos( e.Y() / 2 ) * sin( e.Z() / 2 ) - sin( e.X() / 2 ) * sin( e.Y() / 2 ) * cos( e.Z() / 2 ),
         cos( e.X() / 2 ) * sin( e.Y() / 2 ) * cos( e.Z() / 2 ) + sin( e.X() / 2 ) * cos( e.Y() / 2 ) * sin( e.Z() / 2 ),
         sin( e.X() / 2 ) * cos( e.Y() / 2 ) * cos( e.Z() / 2 ) - cos( e.X() / 2 ) * sin( e.Y() / 2 ) * sin( e.Z() / 2 )
         );
   }

};