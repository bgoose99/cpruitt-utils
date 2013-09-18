
// system includes
#include <cstdlib>
#include <cmath>
#include <fstream>
#include <vector>

// local includes
#include "CoordConversions.h"
#include "Propagator.h"
#include "StringUtils.h"

const double Propagator::R_EARTH        =   6371.0298792;        // radius of spherical Earth (km)
const double Propagator::GRAV           = 398600.4418;           // gravity constant (km^3/s^2)
const double Propagator::G_ACCEL        =      0.0097976432222;  // acceleration due to gravity (km/s^2)
const double Propagator::J2             =      0.00108264;
const double Propagator::J3             =     -0.0000025;
const double Propagator::J4             =     -0.0000016;
const double Propagator::J5             =     -0.00000015;
const double Propagator::A_EARTH        =   6378.137;            // Earth semi-major axis (km)
const double Propagator::AIR_OVER_GAS   =      0.00348367635597; // air molecular weight divided by universal gas constant
const double Propagator::COEFF_PRESSURE =      1.4;
const double Propagator::MACH_1         =      0.6;
const double Propagator::MACH_2         =      1.2;
const double Propagator::DT_MAX         =      1.0;
const double Propagator::SAFETY         =      0.9;
const double Propagator::P_SHRINK       =     -0.25;
const double Propagator::P_GROW         =     -0.2;
const double Propagator::ERR_CON        =      1.89e-4;
const double Propagator::RKF45_TOL      =      1.0e-8;

const double Propagator::R_EARTH_M      = R_EARTH * 1000.0;      // radius of spherical Earth (m)
const double Propagator::A_EARTH_2      = A_EARTH * A_EARTH;
const double Propagator::A_EARTH_3      = A_EARTH_2 * A_EARTH;
const double Propagator::A_EARTH_4      = A_EARTH_3 * A_EARTH;
const double Propagator::INT_RATE_32Hz  = 1.0 / 32.0;
const double Propagator::DT_MIN         = 1.0 / 4096.0;

double Propagator::X_ERR[]                             = { 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 };
int    Propagator::numGramPoints                       = 0;
double Propagator::envGramAltitude[MAX_GRAM_POINTS]    = { 0.0 };
double Propagator::envGramTemperature[MAX_GRAM_POINTS] = { 0.0 };
double Propagator::envGramPressure[MAX_GRAM_POINTS]    = { 0.0 };
double Propagator::envGramDensity[MAX_GRAM_POINTS]     = { 0.0 };
double Propagator::envGramNorthWind[MAX_GRAM_POINTS]   = { 0.0 };
double Propagator::envGramEastWind[MAX_GRAM_POINTS]    = { 0.0 };

/*******************************************************************************
 * 
 ******************************************************************************/
bool Propagator::readGramFile( const std::string &filename )
{
   std::ifstream in;
   in.open( filename.c_str() );
   if( !in.is_open() ) return false;
   
   std::string s;
   std::vector<std::string> sArray;
   
   getline( in, s );
   while( in.good() && numGramPoints <= MAX_GRAM_POINTS )
   {
      StringUtils::trimString( s );
      if( !StringUtils::startsWith( s, "#" ) )
      {
         sArray.clear();
         sArray = StringUtils::split( s, " ", true );
         
         if( sArray.size() >= 6 )
         {
            envGramAltitude[numGramPoints]    = atof( sArray[0].c_str() );
            envGramTemperature[numGramPoints] = atof( sArray[1].c_str() );
            envGramPressure[numGramPoints]    = atof( sArray[2].c_str() );
            envGramDensity[numGramPoints]     = atof( sArray[3].c_str() );
            envGramNorthWind[numGramPoints]   = atof( sArray[4].c_str() );
            envGramEastWind[numGramPoints]    = atof( sArray[5].c_str() );
            numGramPoints++;
         }
      }
      
      getline( in, s );
   }
   
   if( in.is_open() ) in.close();
   
   return true;
}
      
/*******************************************************************************
 * 
 ******************************************************************************/
ObjectState Propagator::propagate3DoF( const double       &time,
                                       const ObjectState  &state,
                                       const GravityModel &model )
{
   double scale[6];
   ObjectState newState( state );
   
   // convert state to ECI and km
   newState.pos = CoordConversions::convertECFtoECI( state.pos, state.validityTime ) / 1000.0;
   newState.vel = CoordConversions::convertECFtoECIVel( state.pos, state.vel, state.validityTime ) / 1000.0;
   newState.acc = CoordConversions::convertECFtoECIAcc( state.pos, state.vel, state.acc, state.validityTime ) / 1000.0;
   
   double integrationTime = 0.0;
   double intStepSize = INT_RATE_32Hz * ( time < 0.0 ? -1.0 : 1.0 );
   double newStepSize = 0.0;
   while( fabs( integrationTime ) <= fabs( time ) ) // loop until we've integrated over enough time
   {
      if( fabs( integrationTime + intStepSize ) > fabs( time ) )
         intStepSize = time - integrationTime;
      
      // integrate and check solution is within error tolerance
      newState = integrateRungeKutta45( newState, intStepSize, model );
      calculateScaleFactors( newState, scale );
      newStepSize = calculateStepSize( scale, intStepSize, RKF45_TOL );
      
      // increment time counter
      integrationTime += intStepSize;
      intStepSize = newStepSize;
   }
   
   // convert ECI state back to ECF and m
   newState.acc = CoordConversions::convertECItoECFAcc( newState.pos, newState.vel, newState.acc, newState.validityTime ) * 1000.0;
   newState.vel = CoordConversions::convertECItoECFVel( newState.pos, newState.vel, newState.validityTime ) * 1000.0;
   newState.pos = CoordConversions::convertECItoECF( newState.pos, newState.validityTime ) * 1000.0;
   
   return newState;
}


      
/*******************************************************************************
 * 
 ******************************************************************************/
double Propagator::interpolateLinear( const double &x1,
                                      const double &x2,
                                      const double &y1,
                                      const double &y2,
                                      const double &x )
{
   double m = ( fabs( x2 - x1 ) > 1.0e-8 ? ( y2 - y1 ) / ( x2 - x1 ) : 0.0 );
   return ( y1 + ( x - x1 ) * m );
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Propagator::getGramData( const double &alt,
                               double &pressure,   // OUTPUT
                               double &density,    // OUTPUT
                               double &northWind,  // OUTPUT
                               double &eastWind )  // OUTPUT
{
   if( numGramPoints > 0 )
   {
      if( alt <= envGramAltitude[0] )
      {
         pressure  = envGramPressure[0];
         density   = envGramDensity[0];
         northWind = envGramNorthWind[0];
         eastWind  = envGramEastWind[0];
      }
      else if( alt > envGramAltitude[numGramPoints-1] )
      {
         pressure  = envGramPressure[numGramPoints-1];
         density   = envGramDensity[numGramPoints-1];
         northWind = envGramNorthWind[numGramPoints-1];
         eastWind  = envGramEastWind[numGramPoints-1];
      }
      else
      {
         for( int i = 0; i < ( numGramPoints - 2 ); i++ )
         {
            if( alt > envGramAltitude[i] && alt <= envGramAltitude[i+1] )
            {
               // use linear interpolation for this alt
               pressure  = interpolateLinear( envGramAltitude[i], envGramAltitude[i+1], envGramPressure[i], envGramPressure[i+1], alt );
               density   = interpolateLinear( envGramAltitude[i], envGramAltitude[i+1], envGramDensity[i], envGramDensity[i+1], alt );
               northWind = interpolateLinear( envGramAltitude[i], envGramAltitude[i+1], envGramNorthWind[i], envGramNorthWind[i+1], alt );
               eastWind  = interpolateLinear( envGramAltitude[i], envGramAltitude[i+1], envGramEastWind[i], envGramEastWind[i+1], alt );
            }
         }
      }
   }
   return;
}

/*******************************************************************************
 * 
 ******************************************************************************/
Vector3D Propagator::calculateGravitationalForces( const ObjectState &state, const GravityModel &model )
{
   double r, r2, r3, r4, sind, sind2, dterm1, dterm2, dterm3;
   double c1, c2, c3;
   double f1, f2, f3;
   
   r = state.pos.magnitude();
   r2 = r * r;
   r3 = r2 * r;
   r4 = r3 * r;
   
   sind  = state.pos.Z() / r;
   sind2 = sind * sind;
   
   switch( model )
   {
      case GRAVITY_SPHERICAL:
         dterm1 = dterm2 = dterm3 = 0.0;
         break;
      case GRAVITY_J2:
         dterm1 = 1.5 * J2 * A_EARTH_2 / r2;
         dterm2 = dterm3 = 0.0;
         break;
      case GRAVITY_J4:
         dterm1 = 0.0;
         dterm2 = 2.5 * J3 * A_EARTH_3 / r3;
         dterm3 = 0.625 * J4 * A_EARTH_4 / r4;
         break;
   }
   
   c1 = 1.0 - 5.0 * sind2;
   c2 = ( 3.0 - 7.0 * sind2 ) * sind;
   c3 = 3.0 - 42.0 * sind2 + 63.0 * sind2 * sind2;
   
   f1 = -1.0 * ( ( GRAV * state.pos.X() ) / r3 ) * ( 1.0 + dterm1 * c1 + dterm2 * c2 - dterm3 * c3 );
   f2 = state.pos.Y() * ( 1.0 / state.pos.X() * f1 );
   
   c1 = 3.0 - 5.0 * sind2;
   c2 = ( 6.0 - 7.0 * sind2 ) * sind;
   c3 = 15.0 - 70.0 * sind2 + 63.0 * sind2 * sind2;
   
   f3 = -1.0 * ( ( GRAV * state.pos.Z() ) / r3 ) * ( 1.0 + dterm1 * c1 + dterm2 * c2 - dterm3 * c3 );
   
   return Vector3D( f1, f2, f3 );
}

/*******************************************************************************
 * 
 ******************************************************************************/
Vector3D Propagator::calculateDragForces( const ObjectState &state )
{
   double pressure, density, northWind, eastWind, speedOfSound, mach, beta, q, cd;
   
   // ECI -> ECF -> LLA
   Vector3D posLLA = CoordConversions::convertECFtoLLA( CoordConversions::convertECItoECF( state.pos, state.validityTime ) );
   
   // save ECF vel
   Vector3D velECF = CoordConversions::convertECItoECFVel( state.pos, state.vel, state.validityTime );
   
   // get GRAM data for this altitude
   getGramData( posLLA.Z(), pressure, density, northWind, eastWind );
   
   // adjust wind
   Vector3D windENU( eastWind, northWind, 0.0 );
   Vector3D windECF = CoordConversions::convertENUtoECF( windENU, state.pos );
   velECF -= windECF;
   
   // compute speed of sound
   speedOfSound = sqrt( COEFF_PRESSURE * pressure / density );
   
   // compute mach
   mach = velECF.magnitude() / speedOfSound;
   
   // use beta or sbeta?
   if( mach <= MACH_1 )                       beta = state.beta;
   else if( mach > MACH_1 && mach <= MACH_2 ) beta = interpolateLinear( MACH_1, MACH_2, state.beta, state.sbeta, mach );
   else                                       beta = state.sbeta;
   
   // dynamic pressure
   q = 0.5 * density * velECF.magnitude() * velECF.magnitude();
   
   // coeff of drag
   cd = -0.5 * density * velECF.magnitude() / beta;
   
   // drag force
   return Vector3D( cd * velECF.X(), cd * velECF.Y(), cd * velECF.Z() );
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Propagator::calculateDerivatives( const ObjectState  &state,
                                       const GravityModel &model,
                                             double       *derivs ) // OUTPUT
{
   Vector3D drag;
   
   // velocity
   derivs[0] = state.vel.X();
   derivs[1] = state.vel.Y();
   derivs[2] = state.vel.Z();
   
   // accel due to gravity
   Vector3D g = calculateGravitationalForces( state, model );
   
   // accel due to drag
   if( state.beta > 1.0e-6 ) drag = calculateDragForces( state );
   
   derivs[3] = g.X() + drag.X();
   derivs[4] = g.Y() + drag.Y();
   derivs[5] = g.Z() + drag.Z();
   
   return;
}

/*******************************************************************************
 * 
 ******************************************************************************/
ObjectState Propagator::integrateRungeKutta45( const ObjectState  &state,
                                               const double       &dt,
                                               const GravityModel &model )
{
   static const double a2  = 0.2;
   static const double a3  = 0.3;
   static const double a4  = 0.6;
   static const double a5  = 1.0;
   static const double a6  = 0.875;
   static const double b21 = 0.2;
   static const double b31 = 3.0 / 40.0;
   static const double b32 = 9.0 / 40.0;
   static const double b41 = 0.3;
   static const double b42 = -0.9;
   static const double b43 = 1.2;
   static const double b51 = -11.0 / 54.0;
   static const double b52 = 2.5;
   static const double b53 = -70.0 / 27.0;
   static const double b54 = 35.0 / 27.0;
   static const double b61 = 1631.0 / 55296.0;
   static const double b62 = 175.0 / 512.0;
   static const double b63 = 575.0 / 13824.0;
   static const double b64 = 44275.0 / 110592.0;
   static const double b65 = 253.0 / 4096.0;
   static const double c1  = 37.0 / 378.0;
   static const double c3  = 250.0 / 621.0;
   static const double c4  = 125.0 / 594.0;
   static const double c6  = 512.0 / 1771.0;
   static const double dc1 = c1 - 2825.0 / 27648.0;
   static const double dc3 = c3 - 18575.0 / 48384.0;
   static const double dc4 = c4 - 13525.0 / 55296.0;
   static const double dc5 = -277.0 / 14336.0;
   static const double dc6 = c6 - 0.25;
   
   double dxdt1[6];
   double dxdt2[6];
   double dxdt3[6];
   double dxdt4[6];
   double dxdt5[6];
   double dxdt6[6];
   double dxdtNew[6];
   
   ObjectState newState( state );
   
   calculateDerivatives( newState, model, dxdt1 );
   newState.pos = state.pos + Vector3D( b21 * dt * dxdt1[0], b21 * dt * dxdt1[1], b21 * dt * dxdt1[2] );
   newState.vel = state.vel + Vector3D( b21 * dt * dxdt1[3], b21 * dt * dxdt1[4], b21 * dt * dxdt1[5] );
   newState.validityTime = state.validityTime + a2 * dt;
   
   calculateDerivatives( newState, model, dxdt2 );
   newState.pos = state.pos + Vector3D( dt * ( b31 * dxdt1[0] + b32 * dxdt2[0] ), dt * ( b31 * dxdt1[1] + b32 * dxdt2[1] ), dt * ( b31 * dxdt1[2] + b32 * dxdt2[2] ) );
   newState.vel = state.vel + Vector3D( dt * ( b31 * dxdt1[3] + b32 * dxdt2[3] ), dt * ( b31 * dxdt1[4] + b32 * dxdt2[4] ), dt * ( b31 * dxdt1[5] + b32 * dxdt2[5] ) );
   newState.validityTime = state.validityTime + a3 * dt;
   
   calculateDerivatives( newState, model, dxdt3 );
   newState.pos = state.pos + Vector3D( dt * ( b41 * dxdt1[0] + b42 * dxdt2[0] + b43 * dxdt3[0] ), dt * ( b41 * dxdt1[1] + b42 * dxdt2[1] + b43 * dxdt3[1] ), dt * ( b41 * dxdt1[2] + b42 * dxdt2[2] + b43 * dxdt3[2] ) );
   newState.vel = state.vel + Vector3D( dt * ( b41 * dxdt1[3] + b42 * dxdt2[3] + b43 * dxdt3[3] ), dt * ( b41 * dxdt1[4] + b42 * dxdt2[4] + b43 * dxdt3[4] ), dt * ( b41 * dxdt1[5] + b42 * dxdt2[5] + b43 * dxdt3[5] ) );
   newState.validityTime = state.validityTime + a4 * dt;
   
   calculateDerivatives( newState, model, dxdt4 );
   newState.pos = state.pos + Vector3D( dt * ( b51 * dxdt1[0] + b52 * dxdt2[0] + b53 * dxdt3[0] + b54 * dxdt4[0] ), dt * ( b51 * dxdt1[1] + b52 * dxdt2[1] + b53 * dxdt3[1] + b54 * dxdt4[1] ), dt * ( b51 * dxdt1[2] + b52 * dxdt2[2] + b53 * dxdt3[2] + b54 * dxdt4[2] ) );
   newState.vel = state.vel + Vector3D( dt * ( b51 * dxdt1[3] + b52 * dxdt2[3] + b53 * dxdt3[3] + b54 * dxdt4[3] ), dt * ( b51 * dxdt1[4] + b52 * dxdt2[4] + b53 * dxdt3[4] + b54 * dxdt4[4] ), dt * ( b51 * dxdt1[5] + b52 * dxdt2[5] + b53 * dxdt3[5] + b54 * dxdt4[5] ) );
   newState.validityTime = state.validityTime + a5 * dt;
   
   calculateDerivatives( newState, model, dxdt5 );
   newState.pos = state.pos + Vector3D( dt * ( b61 * dxdt1[0] + b62 * dxdt2[0] + b63 * dxdt3[0] + b64 * dxdt4[0] + b65 * dxdt5[0] ), dt * ( b61 * dxdt1[1] + b62 * dxdt2[1] + b63 * dxdt3[1] + b64 * dxdt4[1] + b65 * dxdt5[1] ), dt * ( b61 * dxdt1[2] + b62 * dxdt2[2] + b63 * dxdt3[2] + b64 * dxdt4[2] + b65 * dxdt5[2] ) );
   newState.vel = state.vel + Vector3D( dt * ( b61 * dxdt1[3] + b62 * dxdt2[3] + b63 * dxdt3[3] + b64 * dxdt4[3] + b65 * dxdt5[3] ), dt * ( b61 * dxdt1[4] + b62 * dxdt2[4] + b63 * dxdt3[4] + b64 * dxdt4[4] + b65 * dxdt5[4] ), dt * ( b61 * dxdt1[5] + b62 * dxdt2[5] + b63 * dxdt3[5] + b64 * dxdt4[5] + b65 * dxdt5[5] ) );
   newState.validityTime = state.validityTime + a6 * dt;
   
   calculateDerivatives( newState, model, dxdt6 );
   newState.pos = state.pos + Vector3D( dt * ( c1 * dxdt1[0] + c3 * dxdt3[0] + c4 * dxdt4[0] + c6 * dxdt6[0] ), dt * ( c1 * dxdt1[1] + c3 * dxdt3[1] + c4 * dxdt4[1] + c6 * dxdt6[1] ), dt * ( c1 * dxdt1[2] + c3 * dxdt3[2] + c4 * dxdt4[2] + c6 * dxdt6[2] ) );
   newState.vel = state.vel + Vector3D( dt * ( c1 * dxdt1[3] + c3 * dxdt3[3] + c4 * dxdt4[3] + c6 * dxdt6[3] ), dt * ( c1 * dxdt1[4] + c3 * dxdt3[4] + c4 * dxdt4[4] + c6 * dxdt6[4] ), dt * ( c1 * dxdt1[5] + c3 * dxdt3[5] + c4 * dxdt4[5] + c6 * dxdt6[5] ) );
   for( int i = 0; i < 6; i++ )
   {
      X_ERR[i] = dt * ( dc1 * dxdt1[i] + dc3 * dxdt3[i] + dc4 * dxdt4[i] + dc5 * dxdt5[i] + dc6 * dxdt6[i] );
   }
   
   // evaluate derivatives at new state
   newState.validityTime = state.validityTime + dt;
   calculateDerivatives( newState, model, dxdtNew );
   newState.acc = Vector3D( dxdtNew[3], dxdtNew[4], dxdtNew[5] );
   
   return newState;
}

/*******************************************************************************
 * 
 ******************************************************************************/
void Propagator::calculateScaleFactors( const ObjectState &state,
                                              double      *scaleFactors ) // OUTPUT
{
   scaleFactors[0] = fabs( state.pos.X() );
   scaleFactors[1] = fabs( state.pos.Y() );
   scaleFactors[2] = fabs( state.pos.Z() );
   scaleFactors[3] = std::max( 0.01, fabs( state.vel.X() ) );
   scaleFactors[4] = std::max( 0.01, fabs( state.vel.Y() ) );
   scaleFactors[5] = std::max( 0.01, fabs( state.vel.Z() ) );
}

/*******************************************************************************
 * 
 ******************************************************************************/
double Propagator::calculateStepSize( double *scaleFactors, const double &dt, const double &tolerance )
{
   double err;
   double errMax = 0.0;
   double rval;
   
   for( int i = 0; i < 6; i++ )
   {
      err = fabs( X_ERR[i] / scaleFactors[i] );
      if( err > errMax ) errMax = err;
   }
   
   errMax /= tolerance;
   if( errMax <= 1.0 )
   {
      if( errMax > ERR_CON )
         rval = SAFETY * dt * pow( errMax, P_GROW );
      else
         rval = 5.0 * dt;
      
      if( fabs( rval ) > DT_MAX )
         rval = ( rval < 0.0 ? -1.0 : 1.0 ) * DT_MAX;
   }
   else
   {
      if( errMax < 2.7 ) errMax = 2.7;
      rval = SAFETY * dt * pow( errMax, P_SHRINK );
      if( dt >= 0.0 )
      {
         if( rval < 0.1 * dt ) rval = 0.1 * dt;
         if( rval < DT_MIN )   rval = DT_MIN;
      }
      else
      {
         if( rval > 0.1 * dt ) rval = 0.1 * dt;
         if( rval > DT_MIN )   rval = -1.0 * DT_MIN;
      }
   }
   
   return rval;
}
