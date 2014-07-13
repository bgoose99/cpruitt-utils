
#ifndef __Propagator__
#define __Propagator__

// system includes
#include <string>

// local includes
#include "ObjectState.h"

/******************************************************************************
 * Ballistic propagation namespace. 
 *****************************************************************************/
namespace Propagator
{
   enum GravityModel
   {
      GRAVITY_SPHERICAL,
      GRAVITY_J2,
      GRAVITY_J4
   };
      
   enum IntegrationMethod
   {
      FEHLBERG,
      CASH_KARP
   };
      
   /***************************************************************************
    * Reads a GRAM input atmospheric file with the following format:
    *   # denotes a comment line
    *   Column 1 = altitude (m)
    *   Column 2 = temperature (K)
    *   Column 3 = pressure (N/m^2)
    *   Column 4 = density (kg/m^3)
    *   Column 5 = north wind (m/s)
    *   Column 6 = east wind (m/s)
    **************************************************************************/
   bool readGramFile( const std::string &filename );
      
   /***************************************************************************
    * Set the integration method used during propagation.
    **************************************************************************/
   void setIntegrationMethod( const IntegrationMethod &method );
      
   /***************************************************************************
    * Simple 3-DoF propagator function.
    *    time  : time to propagate (s)
    *    state : original state (ECF meters)
    *    model : gravity model to use
    **************************************************************************/
   ObjectState propagate3DoF( const double       &time,
                              const ObjectState  &state,
                              const GravityModel &model );
   

}

#endif
