
#ifndef __CoordConversions__
#define __CoordConversions__

#include "Vector3D.h"
#include "Vector4D.h"

/******************************************************************************
 *
 *****************************************************************************/
namespace CoordConversions
{

   extern const double RAD_2_DEG;
   extern const double DEG_2_RAD;

   /***************************************************************************
    * Convert ECF to ECI.
    **************************************************************************/
   Vector3D convertECFtoECI( const Vector3D &ecf, const double &time );
   Vector3D convertECFtoECIVel( const Vector3D &ecfPos, const Vector3D &ecfVel, const double &time );
   Vector3D convertECFtoECIAcc( const Vector3D &ecfPos, const Vector3D &ecfVel, const Vector3D &ecfAcc, const double &time );

   /***************************************************************************
    * Convert ECI to ECF.
    **************************************************************************/
   Vector3D convertECItoECF( const Vector3D &eci, const double &time );
   Vector3D convertECItoECFVel( const Vector3D &eciPos, const Vector3D &eciVel, const double &time );
   Vector3D convertECItoECFAcc( const Vector3D &eciPos, const Vector3D &eciVel, const Vector3D &eciAcc, const double &time );

   /**************************************************************************
    * Converts latitude (degrees), longitude (degrees), and altitude (meters)
    * to ECF (meters).
    *************************************************************************/
   Vector3D convertLLAtoECF( const Vector3D &lla );

   /**************************************************************************
    * Converts ECF (meters) to latitude (degrees), longitude (degrees), and
    * altitude (meters)
    *************************************************************************/
   Vector3D convertECFtoLLA( const Vector3D &ecf );

   /**************************************************************************
    * Converts ENU (meters) to range (meters), azimuth (radians), and
    * elevation (radians).
    *************************************************************************/
   Vector3D convertENUtoRAE( const Vector3D &enu );

   /**************************************************************************
    * Converts range (meters), azimuth (radians), and elevation (radians) to
    * ENU (meters).
    *************************************************************************/
   Vector3D convertRAEtoENU( const Vector3D &rae );

   /**************************************************************************
    * Converts ECF (meters) to ENU (meters), given a reference point in
    * ECF (meters).
    *************************************************************************/
   Vector3D convertECFtoENU( const Vector3D &ecf, const Vector3D &ref );

   /**************************************************************************
    * Converts ENU (meters) to ECF (meters), given a reference point in
    * ECF (meters).
    *************************************************************************/
   Vector3D convertENUtoECF( const Vector3D &enu, const Vector3D &ref );

   /**************************************************************************
    * Converts ECF (meters) to range (meters), azimuth (radians), and
    * elevation (radians), given a reference point in ECF (meters).
    *************************************************************************/
   Vector3D convertECFtoRAE( const Vector3D &ecf, const Vector3D &ref );

   /**************************************************************************
    * Converts range (meters), azimuth (radians), and elevation (radians) to
    * ECF (meters), given a reference point in ECF (meters).
    *************************************************************************/
   Vector3D convertRAEtoECF( const Vector3D &rae, const Vector3D &ref );

   /***************************************************************************
    * Convert local range, azimuth (radians), and elevation (radians) to
    * local Phased Array, given boresight (Az, El, and Clocking Angle in
    * degrees).
    **************************************************************************/
   Vector3D convertRAEtoPhasedArray( const Vector3D &rae, const Vector3D &boresight );

   /***************************************************************************
    * Convert local Phased Array to local RUV.
    **************************************************************************/
   Vector3D convertPhasedArraytoRUV( const Vector3D &p );

   /***************************************************************************
    * Convert local Phased Array velocity to local RUV velocity, given
    * Phased Array position and velocity.
    **************************************************************************/
   Vector3D convertPhasedArraytoRUVVel( const Vector3D &p, const Vector3D &v );

   /***************************************************************************
    * Convert local Phased Array to local range, azimuth (radians), and
    * elevation (radians), given boresight (Az, El, and Clocking Angle in
    * degrees).
    **************************************************************************/
   Vector3D convertPhasedArraytoRAE( const Vector3D &p, const Vector3D &boresight );

   /***************************************************************************
    * Convert local RUV to local Phased Array.
    **************************************************************************/
   Vector3D convertRUVtoPhasedArray( const Vector3D &ruv );

   /***************************************************************************
    * Convert local RUV velocity to local Phased Array velocity, given RUV
    * position and velocity.
    **************************************************************************/
   Vector3D convertRUVtoPhasedArrayVel( const Vector3D &ruv, const Vector3D &ruvVel );

   /***************************************************************************
    * Convert ECF to local RUV, given sensor location (ECF) and boresight
    * (Az, El, and Clocking Angle in degrees).
    **************************************************************************/
   Vector3D convertECFtoRUV( const Vector3D &ecf, const Vector3D &ref, const Vector3D &boresight );

   /***************************************************************************
    * Convert local RUV to ECF, given sensor location (ECF) and boresight
    * (Az, El, and Clocking Angle in degrees).
    **************************************************************************/
   Vector3D convertRUVtoECF( const Vector3D &ruv, const Vector3D &ref, const Vector3D &boresight );

   /***************************************************************************
    * Convert ECF velocity to local RUV velocity, given sensor location (ECF),
    * boresight (Az, El, and Clocking Angle in degrees), and ECF position
    * and velocity.
    **************************************************************************/
   Vector3D convertECFtoRUVVel( const Vector3D &ecf, const Vector3D &ecfVel, const Vector3D &ref, const Vector3D &boresight );

   /***************************************************************************
    * Convert local RUV velocity to ECF velocity, given sensor location (ECF),
    * boresight (Az, El, and Clocking Angle in degrees), and ECF position
    * and velocity.
    **************************************************************************/
   Vector3D convertRUVtoECFVel( const Vector3D &ruv, const Vector3D &ruvVel, const Vector3D &ref, const Vector3D &boresight );

   /**************************************************************************
    * Convert supplied quaternions to Euler angles (radians).
    * NOTE: quaternions.W() is assumed to be the scalar element.
    * NOTE: Returned Euler angles vector has the following convention:
    *       x = 1st rotation in 321 sequence
    *       y = 2nd rotation in 321 sequence
    *       z = 3rd rotation in 321 sequence
    *************************************************************************/
   Vector3D convertQuaternionsToEuler( const Vector4D &quaternions );

   /**************************************************************************
    * Convert supplied Euler angles (radians) to quaternions.
    * NOTE: quaternions.W() is assumed to be the scalar element.
    * NOTE: Supplied Euler angles vector has the following convention:
    *       x = 1st rotation in 321 sequence
    *       y = 2nd rotation in 321 sequence
    *       z = 3rd rotation in 321 sequence
    *************************************************************************/
   Vector4D convertEulerToQuaternions( const Vector3D &euler );

};

#endif
