
// CppUtils includes
#include "Propagator.h"

// local includes
#include "PropagatorTest.h"

/******************************************************************************
 * 
 *****************************************************************************/
PropagatorTest::PropagatorTest() :
   StarcTest::TestCase( "Propagator Test" )
{
}

/******************************************************************************
 * 
 *****************************************************************************/
PropagatorTest::~PropagatorTest()
{
}

/******************************************************************************
 * 
 *****************************************************************************/
bool PropagatorTest::setUp()
{
   // initialize propagator
   return Propagator::readGramFile( "../../CppUtils/CppUtils/DUMMY_GRAM.txt" );
}

/******************************************************************************
 * 
 *****************************************************************************/
void PropagatorTest::runAllTests()
{
   // initial object state
   ObjectState initial;
   initial.pos = Vector3D( -6321971.120601442643, 1383333.036060876679, 1532687.794541999931 );
   initial.vel = Vector3D(     -282.152677088225,    -265.449533764404,   -2073.579101562500 );
   initial.acc = Vector3D(        0.0,                  0.0,                  0.0            );
   initial.beta = 433.823364257812;
   initial.sbeta = 255.190200805664;

   ObjectState e1s;  // expected values @ 1.0 s
   e1s.pos = Vector3D( -6322249.025910177268, 1383066.673342066817, 1530613.177415576763 );
   e1s.vel = Vector3D(     -273.657748967024,    -267.276088331631,   -2075.654724698442 );
   e1s.acc = Vector3D(        8.495504075205,      -1.827108383093,      -2.074343738176 );

   ObjectState e10s; // expected values @ 10.0 s
   e10s.pos = Vector3D( -6324367.731299082749, 1380587.054621032672, 1511848.583533766912 );
   e10s.vel = Vector3D(     -197.148771898740,    -283.765550554387,   -2094.220758385460 );
   e10s.acc = Vector3D(        8.506770212183,      -1.837279348709,      -2.051497819362 );

   ObjectState e50s; // expected values @ 50.0 s
   e50s.pos = Vector3D( -6325431.685515522026, 1367753.833503478207, 1426465.020847583190 );
   e50s.vel = Vector3D(      144.421885085270,    -358.227380377743,   -2174.305525299871 );
   e50s.acc = Vector3D(        8.577419727044,      -1.887062993911,      -1.953694831041 );

   ObjectState prop = Propagator::propagate3DoF( 1.0, initial, Propagator::GRAVITY_J4 );
   verifyEqual( prop.pos, e1s.pos, "Position @ 1 second" );
   verifyEqual( prop.vel, e1s.vel, "Velocity @ 1 second" );
   verifyEqual( prop.acc, e1s.acc, "Acceleration @ 1 second" );

   prop = Propagator::propagate3DoF( 10.0, initial, Propagator::GRAVITY_J4 );
   verifyEqual( prop.pos, e10s.pos, "Position @ 10 seconds" );
   printf( "pos @ 10 delta = %.12f\n", e10s.pos.distance( prop.pos ) );
   verifyEqual( prop.vel, e10s.vel, "Velocity @ 10 seconds" );
   verifyEqual( prop.acc, e10s.acc, "Acceleration @ 10 seconds" );

   prop = Propagator::propagate3DoF( 50.0, initial, Propagator::GRAVITY_J4 );
   verifyEqual( prop.pos, e50s.pos, "Position @ 50 seconds" );
   verifyEqual( prop.vel, e50s.vel, "Velocity @ 50 seconds" );
   printf( "pos @ 50 delta = %.12f\n", e50s.pos.distance( prop.pos ) );
   printf( "vel @ 50 delta = %.12f\n", e50s.vel.distance( prop.vel ) );
   verifyEqual( prop.acc, e50s.acc, "Acceleration @ 50 seconds" );
}
