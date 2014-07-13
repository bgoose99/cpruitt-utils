
#ifndef __ObjectState__
#define __ObjectState__

// local includes
#include "ISerializable.h"
#include "Vector3D.h"

/******************************************************************************
 * Generic object state class.
 *****************************************************************************/
class ObjectState : public ISerializable<ObjectState>
{
   public:
      
      static const int SIZEOF;
      
      double validityTime; // validity time for this object state (s)
      Vector3D pos;        // position (m)
      Vector3D vel;        // velocity (m/s)
      Vector3D acc;        // acceleration (m/s^2)
      Vector3D euler;      // Euler angles (radians)
      Vector3D eulerRate;  // Euler angle rates (radians/s)
      double beta;         // ballistic coefficient
      double sbeta;        // supersonic ballistic coefficient
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      ObjectState();
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~ObjectState();
      
      /************************************************************************
       * Copy constructor
       ***********************************************************************/
      ObjectState( const ObjectState &that );
      
      /************************************************************************
       * Assignment operator
       ***********************************************************************/
      ObjectState &operator=( const ObjectState &that );
      
      /************************************************************************
       * Serialization functions.
       ***********************************************************************/
      virtual void toBytes( char *buf ) const;
      virtual ObjectState &fromBytes( const char *buf );
      virtual int binarySize() const;
      virtual void toBinaryStream( std::ostream &out ) const;
      virtual ObjectState &fromBinaryStream( std::istream &in );
};

#endif
