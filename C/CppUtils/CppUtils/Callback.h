
#ifndef __Callback__
#define __Callback__

enum CallbackType { STATIC_FUNCTION, MEMBER_METHOD };

/******************************************************************************
 * Simple abstract callback.
 *****************************************************************************/
template <typename Return, typename Arg>
class AbstractCallback
{
   public:
      virtual Return invoke( const Arg & ) = 0;
      virtual AbstractCallback<Return, Arg> *clone() = 0;
      virtual bool operator==( const AbstractCallback<Return, Arg> & ) const = 0;
      virtual bool operator!=( const AbstractCallback<Return, Arg> &that ) const { return !( *this == that ); }
      virtual CallbackType getType() const = 0;
   private:
      // no assignment
      AbstractCallback<Return, Arg> &operator=( const AbstractCallback<Return, Arg> & );
};

/******************************************************************************
 * Static callback for regular functions and static class member functions.
 *****************************************************************************/
template <typename Return, typename Arg>
class StaticCallback : public AbstractCallback<Return, Arg>
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      StaticCallback( Return (*function)( const Arg & ) ) :
         function( function )
      {
      }
      
      /************************************************************************
       * Copy constructor
       ***********************************************************************/
      StaticCallback( const StaticCallback &that ) : function( that.function )
      {
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual Return invoke( const Arg &arg )
      {
         return (*function)( arg );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual AbstractCallback<Return, Arg> *clone()
      {
         return new StaticCallback( function );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual bool operator==( const AbstractCallback<Return, Arg> &that ) const
      {
         if( this == &that ) return true;
         if( getType() != that.getType() ) return false;
         
         // safe to cast
         const StaticCallback<Return, Arg> *c = reinterpret_cast<const StaticCallback<Return, Arg> *>( &that );
         return ( function == c->function );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual CallbackType getType() const
      {
         return STATIC_FUNCTION;
      }
      
   private:
      
      // pointer to static function
      Return (*function)( const Arg & );
};

/******************************************************************************
 * Member callback for class member methods.
 *****************************************************************************/
template <typename Return, typename Arg, typename T, typename Method>
class MemberCallback : public AbstractCallback<Return, Arg>
{
   public:
      
      /************************************************************************
       * Constructor
       ***********************************************************************/
      MemberCallback( void *object, Method method ) :
         object( object ), method( method )
      {
      }
      
      /************************************************************************
       * Copy constructor
       ***********************************************************************/
      MemberCallback( const MemberCallback &that ) :
         object( that.object ),
         method( that.method )
      {
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual Return invoke( const Arg &arg )
      {
         T *obj = static_cast<T *>( object );
         return (obj->*method)( arg );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual AbstractCallback<Return, Arg> *clone()
      {
         return new MemberCallback( object, method );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual bool operator==( const AbstractCallback<Return, Arg> &that ) const
      {
         if( this == &that ) return true;
         if( getType() != that.getType() ) return false;
         
         // safe to cast
         const MemberCallback<Return, Arg, T, Method> *c = reinterpret_cast<const MemberCallback<Return, Arg, T, Method> *>( &that );
         return ( object == c->object && method == c->method );
      }
      
      /************************************************************************
       * 
       ***********************************************************************/
      virtual CallbackType getType() const
      {
         return MEMBER_METHOD;
      }
      
   private:
      
      // handles to object and method
      void *object;
      Method method;
};

/******************************************************************************
 * Main callback class.
 *****************************************************************************/
template <typename Return, typename Arg>
class Callback
{
   public:
      
      /************************************************************************
       * Constructor for static callbacks.
       ***********************************************************************/
      Callback( Return (*function)( const Arg & ) ) :
         callback( new StaticCallback<Return, Arg>( function ) )
      {
      }
      
      /************************************************************************
       * Constructor for memeber callbacks.
       ***********************************************************************/
      template <typename T, typename Method>
      Callback( T *object, Method method ) :
         callback( new MemberCallback<Return, Arg, T, Method>( object, method ) )
      {
      }
      
      /************************************************************************
       * Copy constructor.
       ***********************************************************************/
      Callback( const Callback<Return, Arg> &that ) :
         callback( that.callback->clone() )
      {
      }
      
      /************************************************************************
       * Destructor
       ***********************************************************************/
      virtual ~Callback()
      {
         delete callback;
         callback = 0;
      }
      
      /************************************************************************
       * Convenience overload so this callback can be called like the function
       * it encapsulates.
       ***********************************************************************/
      Return operator()( const Arg &arg )
      {
         return callback->invoke( arg );
      }
      
      /************************************************************************
       * Assignment operator.
       ***********************************************************************/
      Callback &operator=( const Callback &that )
      {
         delete callback;
         callback = 0;
         callback = that.callback->clone();
      }
      
      /************************************************************************
       * Equality operator.
       ***********************************************************************/
      bool operator==( const Callback<Return, Arg> &that ) const
      {
         if( this == &that ) return true;
         return *callback == *that.callback;
      }
      
   private:
      
      // handle to our callback
      AbstractCallback<Return, Arg> *callback;
};

#endif
