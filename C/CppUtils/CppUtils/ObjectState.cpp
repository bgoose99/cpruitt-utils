
// local includes
#include "ObjectState.h"

const int ObjectState::SIZEOF = sizeof(double)* 3 + Vector3D::SIZEOF * 5;

/******************************************************************************
 *
 *****************************************************************************/
ObjectState::ObjectState() :
   validityTime( 0.0 ),
   beta( 0.0 ),
   sbeta( 0.0 )
{
}

/******************************************************************************
 *
 *****************************************************************************/
ObjectState::~ObjectState()
{
}

/******************************************************************************
 *
 *****************************************************************************/
ObjectState::ObjectState( const ObjectState &that ) :
   validityTime( that.validityTime ),
   pos( that.pos ),
   vel( that.vel ),
   acc( that.acc ),
   euler( that.euler ),
   eulerRate( that.eulerRate ),
   beta( that.beta ),
   sbeta( that.sbeta )
{
}

/******************************************************************************
 *
 *****************************************************************************/
ObjectState &ObjectState::operator=( const ObjectState &that )
{
   if( this != &that )
   {
      validityTime = that.validityTime;
      pos = that.pos;
      vel = that.vel;
      acc = that.acc;
      euler = that.euler;
      eulerRate = that.eulerRate;
      beta = that.beta;
      sbeta = that.sbeta;
   }

   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
void ObjectState::toBytes( char *buf ) const
{
   int offset = 0;
   memcpy( &buf[offset], &validityTime, sizeof( validityTime ) ); offset += sizeof( validityTime );
   pos.toBytes( &buf[offset] );                                   offset += pos.binarySize();
   vel.toBytes( &buf[offset] );                                   offset += vel.binarySize();
   acc.toBytes( &buf[offset] );                                   offset += acc.binarySize();
   euler.toBytes( &buf[offset] );                                 offset += euler.binarySize();
   eulerRate.toBytes( &buf[offset] );                             offset += eulerRate.binarySize();
   memcpy( &buf[offset], &beta, sizeof( beta ) );                 offset += sizeof( beta );
   memcpy( &buf[offset], &sbeta, sizeof( sbeta ) );
}

/******************************************************************************
 *
 *****************************************************************************/
ObjectState &ObjectState::fromBytes( const char *buf )
{
   int offset = 0;
   memcpy( &validityTime, &buf[offset], sizeof( validityTime ) ); offset += sizeof( validityTime );
   pos.fromBytes( &buf[offset] );                                 offset += pos.binarySize();
   vel.fromBytes( &buf[offset] );                                 offset += vel.binarySize();
   acc.fromBytes( &buf[offset] );                                 offset += acc.binarySize();
   euler.fromBytes( &buf[offset] );                               offset += euler.binarySize();
   eulerRate.fromBytes( &buf[offset] );                           offset += eulerRate.binarySize();
   memcpy( &beta, &buf[offset], sizeof( beta ) );                 offset += sizeof( beta );
   memcpy( &sbeta, &buf[offset], sizeof( sbeta ) );
   return *this;
}

/******************************************************************************
 *
 *****************************************************************************/
int ObjectState::binarySize() const
{
   return ObjectState::SIZEOF;
}

/******************************************************************************
 *
 *****************************************************************************/
void ObjectState::toBinaryStream( std::ostream &out ) const
{
   out.write( (char *)&validityTime, sizeof( validityTime ) );
   pos.toBinaryStream( out );
   vel.toBinaryStream( out );
   acc.toBinaryStream( out );
   euler.toBinaryStream( out );
   eulerRate.toBinaryStream( out );
   out.write( (char *)&beta, sizeof( beta ) );
   out.write( (char *)&sbeta, sizeof( sbeta ) );
}

/******************************************************************************
 *
 *****************************************************************************/
ObjectState &ObjectState::fromBinaryStream( std::istream &in )
{
   in.read( (char *)&validityTime, sizeof( validityTime ) );
   pos.fromBinaryStream( in );
   vel.fromBinaryStream( in );
   acc.fromBinaryStream( in );
   euler.fromBinaryStream( in );
   eulerRate.fromBinaryStream( in );
   in.read( (char *)&beta, sizeof( beta ) );
   in.read( (char *)&sbeta, sizeof( sbeta ) );
   return *this;
}
