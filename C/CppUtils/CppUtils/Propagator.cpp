
// system includes
#include <cmath>
#include <cstdlib>
#include <fstream>
#include <vector>

// local includes
#include "CoordConversions.h"
#include "Propagator.h"
#include "StringUtils.h"

namespace Propagator
{
   // anonymous namespace for "private" variables and methods
   namespace
   {
      // convenience struct for holding Runge-Kutta constants
      // from the Butcher tablequ
      struct RKConstants
      {
         double t2, t3, t4, t5, t6;
         double k21;
         double k31, k32;
         double k41, k42, k43;
         double k51, k52, k53, k54;
         double k61, k62, k63, k64, k65;
         double b11, b12, b13, b14, b15, b16;
         double b21, b22, b23, b24, b25, b26;
      };
      
      // single record from GRAM file
      struct GramRecord
      {
         double alt;
         double temp;
         double pressure;
         double density;
         double northWind;
         double eastWind;
      };
      
      // CONSTANTS
      const double R_EARTH        = 6371029.8792;   // m
      const double G              = 6.67384e-11;    // constant of gravitation ( m^3 / ( kg * s^2 ) )
      const double GE             = 3.986004391e14; // geocentric gravitation constant ( m^3 / s^2 )
      const double A              = 6378137.0;      // elliptical earth semi-major axis (m)
      const double A2             = A * A;
      const double A3             = A2 * A;
      const double A4             = A3 * A;
      const double PRESSURE_COEFF = 1.4;
      const double MACH_FACTOR_1  = 0.6;
      const double MACH_FACTOR_2  = 1.2;
      
      // gravitational harmonics from Fundamentals of Astrodynamics
      const double J2             = 1.08264e-3;
      const double J3             = -2.5e-6;
      const double J4             = -1.6e-6;
      const double J5             = -1.5e-7;
      
      const double DT_DEFAULT     = 1.0 / 32.0;   // default integration step size
      const double DT_MIN         = 1.0 / 4096.0;
      const double DT_MAX         = 1.0;
      const double RK_TOL         = 1.0e-8;       // integrator tolerance
      const double RK_SAFETY      = 0.9;          // "chicken" factor
      
      double nextStepSize         = DT_DEFAULT;   // next step size
      
      // Runge-Kutta-Fehlberg
      RKConstants RKF = 
         {
                1 / 4,      // t2
                3 / 8,      // t3
               12 / 13,     // t4
                1,          // t5
                1 / 2,      // t6
                1 / 4,      // k21
                3 / 32,     // k31
                9 / 32,     // k32
             1932 / 2197,   // k41
            -7200 / 2197,   // k42
             7296 / 2197,   // k43
              439 / 216,    // k51
               -8,          // k52
             3680 / 513,    // k53
             -845 / 4104,   // k54
               -8 / 27,     // k61
                2,          // k62
            -3544 / 2565,   // k63
             1859 / 4104,   // k64
              -11 / 40,     // k65
               25 / 216,    // b11
                0,          // b12
             1408 / 2565,   // b13
             2197 / 4104,   // b14
               -1 / 5,      // b15
                0,          // b16
               16 / 135,    // b21
                0,          // b22
             6656 / 12825,  // b23
            28561 / 56430,  // b24
               -9 / 50,     // b25
                2 / 55      // b26
         };
      
      // Runge-Kutta-Cash-Karp
      RKConstants RKCK =
         {
                1 / 5,      // t2
                3 / 10,     // t3
                3 / 5,      // t4
                1,          // t5
                7 / 8,      // t6
                1 / 5,      // k21
                3 / 40,     // k31
                9 / 40,     // k32
                3 / 10,     // k41
               -9 / 10,     // k42
                6 / 5,      // k43
              -11 / 54,     // k51
                5 / 2,      // k52
              -70 / 27,     // k53
               35 / 27,     // k54
             1631 / 55296,  // k61
              175 / 512,    // k62
              575 / 13824,  // k63
            44275 / 110592, // k64
              253 / 4096,   // k65
               37 / 378,    // b11
                0,          // b12
              250 / 621,    // b13
              125 / 594,    // b14
                0,          // b15
              512 / 1771,   // b16
             2825 / 27648,  // b21
                0,          // b22
            18575 / 48384,  // b23
            13525 / 55296,  // b24
              277 / 14336,  // b25
                1 / 4       // b26
         };
      
      RKConstants &rk = RKCK; // default
      
      std::vector<GramRecord> gramData;
      
      // FUNCTIONS
      /************************************************************************
       * 
       ***********************************************************************/
      double linearInterp( const double &x1,
                           const double &x2,
                           const double &y1,
                           const double &y2,
                           const double &x )
      {
         double m     = 0;
         double num   = y2 - y1;
         double denom = x2 - x1;
         if( fabs( denom ) > 1e-8 ) m = num / denom;
         return y1 + ( x - x1 ) * m;
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      GramRecord getGramData( const double &alt )
      {
         GramRecord r;
         r.alt = alt;
         
         if( !gramData.empty() )
         {
            if( alt <= gramData.front().alt )
            {
               r = gramData.front();
            }
            else if( alt > gramData.back().alt )
            {
               r = gramData.back();
            }
            else
            {
               // interpolate
               for( unsigned int i = 0; i < ( gramData.size() - 1 ); i++ )
               {
                  if( alt > gramData[i].alt && alt <= gramData[i+1].alt )
                  {
                     r.pressure  = linearInterp( gramData[i].alt, gramData[i+1].alt, gramData[i].pressure, gramData[i+1].pressure, alt );
                     r.density   = linearInterp( gramData[i].alt, gramData[i+1].alt, gramData[i].density, gramData[i+1].density, alt );
                     r.northWind = linearInterp( gramData[i].alt, gramData[i+1].alt, gramData[i].northWind, gramData[i+1].northWind, alt );
                     r.eastWind  = linearInterp( gramData[i].alt, gramData[i+1].alt, gramData[i].eastWind, gramData[i+1].eastWind, alt );
                     break;
                  }
               }
            }
         }
         
         return r;
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      Vector3D calculateGravitationalAccel( const ObjectState &state, const GravityModel &model )
      {
         double r, r2, r3, r4, s, s2, d1, d2, d3, c1, c2, c3;
         Vector3D rval;
         
         r = state.pos.magnitude();
         r2 = r * r;
         r3 = r2 * r;
         r4 = r3 * r;
         
         s = state.pos.Z() / r;
         s2 = s * s;
         
         switch( model )
         {
            case GRAVITY_J2:
               d1 = 1.5 * J2 * A2 / r2;
               d2 = d3 = 0.0;
               break;
            case GRAVITY_J4:
               d1 = 0.0;
               d2 = 2.5 * J3 * A3 / r3;
               d3 = 0.625 * J4 * A4 / r4;
               break;
            case GRAVITY_SPHERICAL:
            default:
               d1 = d2 = d3 = 0.0;
               break;
         }
         
         c1 = 1.0 - 5.0 * s2;
         c2 = ( 3.0 - 7.0 * s2 ) * s;
         c3 = 3.0 - 42.0 * s2 + 63.0 * s2 * s2;
         
         rval.setX( -1.0 * ( ( GE * state.pos.X() ) / r3 ) * ( 1.0 + d1 * c1 + d2 * c2 - d3 * c3 ) );
         rval.setY( state.pos.Y() * ( 1.0 / state.pos.X() * rval.X() ) );
         
         c1 = 3.0 - 5.0 * s2;
         c2 = ( 6.0 - 7.0 * s2 ) * s;
         c3 = 15.0 - 70.0 * s2 + 63.0 * s2 * s2;
         
         rval.setZ( -1.0 * ( ( GE * state.pos.Z() ) / r3 ) * ( 1.0 + d1 * c1 + d2 * c2 - d3 * c3 ) );
         
         return rval;
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      Vector3D calculateDragAccel( const ObjectState &state )
      {
         Vector3D posECF = CoordConversions::convertECItoECF( state.pos, state.validityTime );
         Vector3D velECF = CoordConversions::convertECItoECFVel( state.pos, state.vel, state.validityTime );
         Vector3D velENU = CoordConversions::convertECFtoENU( velECF, posECF );
         Vector3D posLLA = CoordConversions::convertECFtoLLA( posECF );
         GramRecord gram = getGramData( posLLA.Z() );
         
         // adjust velocity by wind
         velENU -= Vector3D( gram.eastWind, gram.northWind, 0.0 );
         
         // -> ECF
         velECF = CoordConversions::convertENUtoECF( velENU, posECF );
         
         double speedOfSound = sqrt( PRESSURE_COEFF * gram.pressure / gram.density );
         double mach = velECF.magnitude() / speedOfSound;
         double beta;
         
         if( mach <= MACH_FACTOR_1 || state.sbeta < 1e-6 )
            beta = state.beta;
         else if( mach > MACH_FACTOR_1 && mach >= MACH_FACTOR_2 )
            beta = linearInterp( MACH_FACTOR_1, MACH_FACTOR_2, state.beta, state.sbeta, mach );
         else
            beta = state.sbeta;
         
         double dragScaleFactor = -1e-3 * ( gram.density * velECF.magnitude() * velECF.magnitude() ) / ( 2.0 * beta );
         
         Vector3D dragECF = velECF.normalized() * dragScaleFactor;
         
         return CoordConversions::convertECFtoECI( dragECF, state.validityTime );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      Vector3D deriveVel( const ObjectState &state )
      {
         return state.vel;
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      Vector3D deriveAcc( const ObjectState &state, const GravityModel &model )
      {
         Vector3D g = calculateGravitationalAccel( state, model );
         
         if( state.beta > 1e-6 )
            g += calculateDragAccel( state );
         
         return g;
      }
      
      /************************************************************************
       * NOTE: dt will be modified as needed based on error calculations.
       ***********************************************************************/
      ObjectState integrateState( const ObjectState &state, const GravityModel &model, double &dt )
      {
         ObjectState k1( state );
         ObjectState k2( state );
         ObjectState k3( state );
         ObjectState k4( state );
         ObjectState k5( state );
         ObjectState k6( state );
         ObjectState b1( state );
         ObjectState b2( state );
         Vector3D v1, v2, v3, v4, v5, v6;
         Vector3D a1, a2, a3, a4, a5, a6;
         double pErr, vErr, err;
         
         ObjectState newState( state );
         bool done = false;
         
         while( !done )
         {
            // TODO: stop if error can't be bounded
            // K1 = first state
            // K2
            v1 = deriveVel( k1 );
            a1 = deriveAcc( k1, model );
            k2.pos += v1 * rk.k21 * dt;
            k2.vel += a1 * rk.k21 * dt;
            k2.validityTime += rk.t2 * dt;
            
            // K3
            v2 = deriveVel( k2 );
            a2 = deriveAcc( k2, model );
            k3.pos += ( v1 * rk.k31 + v2 * rk.k32 ) * dt;
            k3.vel += ( a1 * rk.k31 + a2 * rk.k32 ) * dt;
            k3.validityTime += rk.t3 * dt;
            
            // K4
            v3 = deriveVel( k3 );
            a3 = deriveAcc( k3, model );
            k4.pos += ( v1 * rk.k41 + v2 * rk.k42 + v3 * rk.k43 ) * dt;
            k4.vel += ( a1 * rk.k41 + a2 * rk.k42 + a3 * rk.k43 ) * dt;
            k4.validityTime += rk.t4 * dt;
            
            // K5
            v4 = deriveVel( k4 );
            a4 = deriveAcc( k4, model );
            k5.pos += ( v1 * rk.k51 + v2 * rk.k52 + v3 * rk.k53 + v4 * rk.k54 ) * dt;
            k5.vel += ( a1 * rk.k51 + a2 * rk.k52 + a3 * rk.k53 + a4 * rk.k54 ) * dt;
            k5.validityTime += rk.t5 * dt;
            
            // K6
            v5 = deriveVel( k5 );
            a5 = deriveAcc( k5, model );
            k6.pos += ( v1 * rk.k61 + v2 * rk.k62 + v3 * rk.k63 + v4 * rk.k64 + v5 * rk.k65 ) * dt;
            k6.vel += ( a1 * rk.k61 + a2 * rk.k62 + a3 * rk.k63 + a4 * rk.k64 + a5 * rk.k65 ) * dt;
            
            // take final derivatives
            v6 = deriveVel( k6 );
            a6 = deriveAcc( k6, model );
            
            // b1 ( 4th order )
            b1.pos += ( v1 * rk.b11 + v2 * rk.b12 + v3 * rk.b13 + v4 * rk.b14 + v5 * rk.b15 + v6 * rk.b16 ) * dt;
            b1.vel += ( a1 * rk.b11 + a2 * rk.b12 + a3 * rk.b13 + a4 * rk.b14 + a5 * rk.b15 + a6 * rk.b16 ) * dt;
            
            // b2 ( 5th order )
            b2.pos += ( v1 * rk.b21 + v2 * rk.b22 + v3 * rk.b23 + v4 * rk.b24 + v5 * rk.b25 + v6 * rk.b26 ) * dt;
            b2.vel += ( a1 * rk.b21 + a2 * rk.b22 + a3 * rk.b23 + a4 * rk.b24 + a5 * rk.b25 + a6 * rk.b26 ) * dt;
            
            // calculate error
            pErr = ( b2.pos - b1.pos ).magnitude();
            vErr = ( b2.vel - b1.vel ).magnitude();
            
            err = ( 1.0 / dt ) * std::max( pErr, vErr );
            
            nextStepSize = RK_SAFETY * pow( RK_TOL * dt / err, 0.25 );
            if( nextStepSize < DT_MIN ) nextStepSize = DT_MIN;
            if( nextStepSize > DT_MAX ) nextStepSize = DT_MAX;
            
            if( err < RK_TOL )
            {
               // we're done
               done = true;
            }
            else
            {
               // integrate again with new step size
               dt = nextStepSize;
            }
         }
         
         // new state
         newState.validityTime += dt;
         newState.pos = b1.pos;
         newState.vel = b1.vel;
         newState.acc = deriveAcc( newState, model );
         
         return newState;
      }
      
   }
   // end anonymous namespace
   
   /***************************************************************************
    * 
    **************************************************************************/
   bool readGramFile( const std::string &filename )
   {
      std::ifstream in;
      in.open( filename.c_str() );
      if( !in.is_open() ) return false;
      
      std::string s;
      std::vector<std::string> sArray;
      
      // clear any old data
      gramData.clear();
      
      while( in.good() )
      {
         getline( in, s );
         StringUtils::trimString( s );
         
         // ignore comment lines
         if( StringUtils::startsWith( s, "c" ) || StringUtils::startsWith( s, "C" ) ) continue;
         
         sArray = StringUtils::split( s, true );
         
         GramRecord r;
         if( sArray.size() >= 6 )
         {
            r.alt       = atof( sArray[0].c_str() );
            r.temp      = atof( sArray[1].c_str() );
            r.pressure  = atof( sArray[2].c_str() );
            r.density   = atof( sArray[3].c_str() );
            r.northWind = atof( sArray[4].c_str() );
            r.eastWind  = atof( sArray[5].c_str() );
            gramData.push_back( r );
         }
      }
      
      if( in.is_open() ) in.close();
      
      return true;
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   void setIntegrationMethod( const IntegrationMethod &method )
   {
      switch( method )
      {
         case CASH_KARP:
            rk = RKCK;
            break;
         case FEHLBERG:
         default:
            rk = RKF;
            break;
      }
   }
   
   /***************************************************************************
    * 
    **************************************************************************/
   ObjectState propagate3DoF( const double       &time,
                              const ObjectState  &state,
                              const GravityModel &model )
   {
      ObjectState newState( state );
      
      // state -> ECI
      newState.pos = CoordConversions::convertECFtoECI( state.pos, state.validityTime );
      newState.vel = CoordConversions::convertECFtoECIVel( state.pos, state.vel, state.validityTime );
      newState.acc = CoordConversions::convertECFtoECIAcc( state.pos, state.vel, state.acc, state.validityTime );
      
      double integrationTime = 0.0;
      double intStepSize = DT_DEFAULT * ( time < 0.0 ? -1.0 : 1.0 );
      while( fabs( integrationTime ) < fabs( time ) )
      {
         if( fabs( integrationTime + intStepSize ) > fabs( time ) )
            intStepSize = time - integrationTime;
         
         newState = integrateState( newState, model, intStepSize );
         
         integrationTime += intStepSize;
         intStepSize = nextStepSize;
      }
      
      // new state -> ECF
      newState.acc = CoordConversions::convertECItoECFAcc( newState.pos, newState.vel, newState.acc, newState.validityTime );
      newState.vel = CoordConversions::convertECItoECFVel( newState.pos, newState.vel, newState.validityTime );
      newState.pos = CoordConversions::convertECItoECF( newState.pos, newState.validityTime );
      
      return newState;
   }
   
}