
#ifndef __CoordConversions__
#define __CoordConversions__

#include "Vector3D.h"
#include "Vector4D.h"

/******************************************************************************
 * 
 *****************************************************************************/
class CoordConversions
{
   public:
      
      static const double RAD_2_DEG;
      static const double DEG_2_RAD;
      
      /*************************************************************************
       * Convert ECF to ECI.
       ************************************************************************/
      static Vector3D convertECFtoECI( const Vector3D &ecf, const double &time );
      static Vector3D convertECFtoECIVel( const Vector3D &ecfPos, const Vector3D &ecfVel, const double &time );
      static Vector3D convertECFtoECIAcc( const Vector3D &ecfPos, const Vector3D &ecfVel, const Vector3D &ecfAcc, const double &time );
      
      /*************************************************************************
       * Convert ECI to ECF.
       ************************************************************************/
      static Vector3D convertECItoECF( const Vector3D &eci, const double &time );
      static Vector3D convertECItoECFVel( const Vector3D &eciPos, const Vector3D &eciVel, const double &time );
      static Vector3D convertECItoECFAcc( const Vector3D &eciPos, const Vector3D &eciVel, const Vector3D &eciAcc, const double &time );
      
      /************************************************************************
       * Converts latitude (degrees), longitude (degrees), and altitude (meters)
       * to ECF (meters).
       ***********************************************************************/
      static Vector3D convertLLAtoECF( const Vector3D &lla );
      
      /************************************************************************
       * Converts ECF (meters) to latitude (degrees), longitude (degrees), and
       * altitude (meters)
       ***********************************************************************/
      static Vector3D convertECFtoLLA( const Vector3D &ecf );
      
      /************************************************************************
       * Converts ENU (meters) to range (meters), azimuth (radians), and
       * elevation (radians).
       ***********************************************************************/
      static Vector3D convertENUtoRAE( const Vector3D &enu );
      
      /************************************************************************
       * Converts range (meters), azimuth (radians), and elevation (radians) to
       * ENU (meters).
       ***********************************************************************/
      static Vector3D convertRAEtoENU( const Vector3D &rae );
      
      /************************************************************************
       * Converts ECF (meters) to ENU (meters), given a reference point in
       * ECF (meters).
       ***********************************************************************/
      static Vector3D convertECFtoENU( const Vector3D &ecf, const Vector3D &ref );
      
      /************************************************************************
       * Converts ENU (meters) to ECF (meters), given a reference point in
       * ECF (meters).
       ***********************************************************************/
      static Vector3D convertENUtoECF( const Vector3D &enu, const Vector3D &ref );
      
      /************************************************************************
       * Converts ECF (meters) to range (meters), azimuth (radians), and
       * elevation (radians), given a reference point in ECF (meters).
       ***********************************************************************/
      static Vector3D convertECFtoRAE( const Vector3D &ecf, const Vector3D &ref );
      
      /************************************************************************
       * Converts range (meters), azimuth (radians), and elevation (radians) to
       * ECF (meters), given a reference point in ECF (meters).
       ***********************************************************************/
      static Vector3D convertRAEtoECF( const Vector3D &rae, const Vector3D &ref );
      
      /*************************************************************************
       * Convert local range, azimuth (radians), and elevation (radians) to
       * local Phased Array, given boresight (Az, El, and Clocking Angle in
       * degrees).
       ************************************************************************/
      static Vector3D convertRAEtoPhasedArray( const Vector3D &rae, const Vector3D &boresight );
      
      /*************************************************************************
       * Convert local Phased Array to local RUV.
       ************************************************************************/
      static Vector3D convertPhasedArraytoRUV( const Vector3D &p );
      
      /*************************************************************************
       * Convert local Phased Array velocity to local RUV velocity, given
       * Phased Array position and velocity.
       ************************************************************************/
      static Vector3D convertPhasedArraytoRUVVel( const Vector3D &p, const Vector3D &v );
      
      /*************************************************************************
       * Convert local Phased Array to local range, azimuth (radians), and
       * elevation (radians), given boresight (Az, El, and Clocking Angle in
       * degrees).
       ************************************************************************/
      static Vector3D convertPhasedArraytoRAE( const Vector3D &p, const Vector3D &boresight );
      
      /*************************************************************************
       * Convert local RUV to local Phased Array.
       ************************************************************************/
      static Vector3D convertRUVtoPhasedArray( const Vector3D &ruv );
      
      /*************************************************************************
       * Convert local RUV velocity to local Phased Array velocity, given RUV
       * position and velocity.
       ************************************************************************/
      static Vector3D convertRUVtoPhasedArrayVel( const Vector3D &ruv, const Vector3D &ruvVel );
      
      /*************************************************************************
       * Convert ECF to local RUV, given sensor location (ECF) and boresight
       * (Az, El, and Clocking Angle in degrees).
       ************************************************************************/
      static Vector3D convertECFtoRUV( const Vector3D &ecf, const Vector3D &ref, const Vector3D &boresight );
      
      /*************************************************************************
       * Convert local RUV to ECF, given sensor location (ECF) and boresight
       * (Az, El, and Clocking Angle in degrees).
       ************************************************************************/
      static Vector3D convertRUVtoECF( const Vector3D &ruv, const Vector3D &ref, const Vector3D &boresight );
      
      /*************************************************************************
       * Convert ECF velocity to local RUV velocity, given sensor location (ECF),
       * boresight (Az, El, and Clocking Angle in degrees), and ECF position
       * and velocity.
       ************************************************************************/
      static Vector3D convertECFtoRUVVel( const Vector3D &ecf, const Vector3D &ecfVel, const Vector3D &ref, const Vector3D &boresight );
      
      /*************************************************************************
       * Convert local RUV velocity to ECF velocity, given sensor location (ECF),
       * boresight (Az, El, and Clocking Angle in degrees), and ECF position
       * and velocity.
       ************************************************************************/
      static Vector3D convertRUVtoECFVel( const Vector3D &ruv, const Vector3D &ruvVel, const Vector3D &ref, const Vector3D &boresight );
      
      /************************************************************************
       * Convert supplied quaternions to Euler angles (radians).
       * NOTE: quaternions.W() is assumed to be the scalar element.
       * NOTE: Returned Euler angles vector has the following convention:
       *       x = 1st rotation in 321 sequence
       *       y = 2nd rotation in 321 sequence
       *       z = 3rd rotation in 321 sequence
       ***********************************************************************/
      static Vector3D convertQuaternionsToEuler( const Vector4D &quaternions );
      
      /************************************************************************
       * Convert supplied Euler angles (radians) to quaternions.
       * NOTE: quaternions.W() is assumed to be the scalar element.
       * NOTE: Supplied Euler angles vector has the following convention:
       *       x = 1st rotation in 321 sequence
       *       y = 2nd rotation in 321 sequence
       *       z = 3rd rotation in 321 sequence
       ***********************************************************************/
      static Vector4D convertEulerToQuaternions( const Vector3D &euler );
      
   private:
      
      static const double A;  // Earth semi-major axis
      static const double B;  // Earth semi-minor axis
      static const double F;  // reciprocal flattening
      static const double E2; // first eccentricity squared
      static const double W;  // angular velocity of Earth (radians/s)
      
};

#endif
