
#ifndef __Propagator__
#define __Propagator__

// system includes
#include <string>

// local includes
#include "ObjectState.h"

#define MAX_GRAM_POINTS 2048

/******************************************************************************
 * Ballistic propagator. 
 *****************************************************************************/
class Propagator
{
   public:
      
      enum GravityModel
      {
         GRAVITY_SPHERICAL,
         GRAVITY_J2,
         GRAVITY_J4
      };
      
      /*************************************************************************
       * Reads a GRAM input atmospheric file with the following format:
       *   # denotes a comment line
       *   Column 1 = altitude (m)
       *   Column 2 = temperature (K)
       *   Column 3 = pressure (N/m^2)
       *   Column 4 = density (kg/m^3)
       *   Column 5 = north wind (m/s)
       *   Column 6 = east wind (m/s)
       ************************************************************************/
      static bool readGramFile( const std::string &filename );
      
      /*************************************************************************
       * Simple 3-DoF propagator function.
       *    time  : time to propagate (s)
       *    state : original state (ECF meters)
       *    model : gravity model to use
       ************************************************************************/
      static ObjectState propagate3DoF( const double       &time,
                                        const ObjectState  &state,
                                        const GravityModel &model );
   
   private:
      
      // CONSTANTS
      static const double R_EARTH;
      static const double R_EARTH_M;
      static const double GRAV;
      static const double G_ACCEL;
      static const double J2;
      static const double J3;
      static const double J4;
      static const double J5;
      static const double A_EARTH;
      static const double A_EARTH_2;
      static const double A_EARTH_3;
      static const double A_EARTH_4;
      static const double AIR_OVER_GAS;
      static const double COEFF_PRESSURE;
      static const double MACH_1;
      static const double MACH_2;
      static const double INT_RATE_32Hz;
      static const double DT_MIN;
      static const double DT_MAX;
      static const double SAFETY;
      static const double P_SHRINK;
      static const double P_GROW;
      static const double ERR_CON;
      static const double RKF45_TOL;
      
      static double X_ERR[];
      static int numGramPoints;
      static double envGramAltitude[MAX_GRAM_POINTS];
      static double envGramTemperature[MAX_GRAM_POINTS];
      static double envGramPressure[MAX_GRAM_POINTS];
      static double envGramDensity[MAX_GRAM_POINTS];
      static double envGramNorthWind[MAX_GRAM_POINTS];
      static double envGramEastWind[MAX_GRAM_POINTS]; 
      
      /*************************************************************************
       * 
       ************************************************************************/
      static double interpolateLinear( const double &x1,
                                       const double &x2,
                                       const double &y1,
                                       const double &y2,
                                       const double &x );
      
      /*************************************************************************
       * 
       ************************************************************************/
      static void getGramData( const double &alt,
                                     double &pressure,   // OUTPUT
                                     double &density,    // OUTPUT
                                     double &northWind,  // OUTPUT
                                     double &eastWind ); // OUTPUT
      
      /*************************************************************************
       * 
       ************************************************************************/
      static Vector3D calculateGravitationalForces( const ObjectState &state, const GravityModel &model );
      
      /*************************************************************************
       * 
       ************************************************************************/
      static Vector3D calculateDragForces( const ObjectState &state );
      
      /*************************************************************************
       * 
       ************************************************************************/
      static void calculateDerivatives( const ObjectState  &state,
                                        const GravityModel &model,
                                              double       *derivs ); // OUTPUT double[6]
      
      /*************************************************************************
       * 
       ************************************************************************/
      static ObjectState integrateRungeKutta45( const ObjectState  &state,
                                                const double       &dt,
                                                const GravityModel &model );
      
      /*************************************************************************
       * 
       ************************************************************************/
      static void calculateScaleFactors( const ObjectState &state,
                                               double      *scaleFactors ); // OUTPUT double[6]
      
      /*************************************************************************
       * 
       ************************************************************************/
      static double calculateStepSize( double *scaleFactors, const double &dt, const double &tolerance );
      
};

#endif
