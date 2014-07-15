
#ifndef __Callback__
#define __Callback__

enum CallbackType { STATIC_FUNCTION, MEMBER_METHOD };

/******************************************************************************
* Simple abstract callback.
******************************************************************************/
template <typename Return, typename... Args>
class AbstractCallback
{
public:
   virtual Return invoke( const Args&... ) = 0;
   virtual AbstractCallback<Return, Args...> *clone() = 0;
   virtual bool operator==( const AbstractCallback<Return, Args...> & ) const = 0;
   virtual bool operator!=( const AbstractCallback<Return, Args...> &that ) const { return !( *this == that ); }
   virtual CallbackType getType() const = 0;
private:
   // no assignment
   AbstractCallback<Return, Args...> &operator=( const AbstractCallback<Return, Args...> & );
};

/******************************************************************************
* Static callback for regular functions and static class member functions.
******************************************************************************/
template <typename Return, typename... Args>
class StaticCallback : public AbstractCallback<Return, Args...>
{
public:

   /***************************************************************************
   * Constructor
   ***************************************************************************/
   StaticCallback( Return( *function )( const Args&... ) ) :
      function( function )
   {
   }

   /***************************************************************************
   * Copy constructor
   ***************************************************************************/
   StaticCallback( const StaticCallback &that ) : function( that.function )
   {
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual Return invoke( const Args&... args )
   {
      return ( *function )( args... );
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual AbstractCallback<Return, Args...> *clone()
   {
      return new StaticCallback( function );
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual bool operator==( const AbstractCallback<Return, Args...> &that ) const
   {
      if( this == &that ) return true;
      if( getType() != that.getType() ) return false;

      // safe to cast
      const StaticCallback<Return, Args...> *c = reinterpret_cast<const StaticCallback<Return, Args...> *>( &that );
      return ( function == c->function );
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual CallbackType getType() const
   {
      return STATIC_FUNCTION;
   }

private:

   // pointer to static function
   Return( *function )( const Args&... );
};

/******************************************************************************
* Member callback for class member methods.
******************************************************************************/
template <typename T, typename Method, typename Return, typename... Args>
class MemberCallback : public AbstractCallback<Return, Args...>
{
public:

   /***************************************************************************
   * Constructor
   ***************************************************************************/
   MemberCallback( void *object, Method method ) :
      object( object ), method( method )
   {
   }

   /***************************************************************************
   * Copy constructor
   ***************************************************************************/
   MemberCallback( const MemberCallback &that ) :
      object( that.object ),
      method( that.method )
   {
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual Return invoke( const Args&... args )
   {
      T *obj = static_cast<T *>( object );
      return ( obj->*method )( args... );
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual AbstractCallback<Return, Args...> *clone()
   {
      return new MemberCallback( object, method );
   }

   /***************************************************************************
   *
   ***************************************************************************/
   virtual bool operator==( const AbstractCallback<Return, Args...> &that ) const
   {
      if( this == &that ) return true;
      if( getType() != that.getType() ) return false;

      // safe to cast
      const MemberCallback<T, Method, Return, Args...> *c = reinterpret_cast<const MemberCallback<T, Method, Return, Args...> *>( &that );
      return ( object == c->object && method == c->method );
   }

   /***************************************************************************
   *
   ***************************************************************************/
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
******************************************************************************/
template <typename Return, typename... Args>
class Callback
{
public:

   /***************************************************************************
   * Constructor for static callbacks.
   ***************************************************************************/
   Callback( Return( *function )( const Args&... ) ) :
      callback( new StaticCallback<Return, Args...>( function ) )
   {
   }

   /***************************************************************************
   * Constructor for memeber callbacks.
   ***************************************************************************/
   template <typename T, typename Method>
   Callback( T *object, Method method ) :
      callback( new MemberCallback<T, Method, Return, Args...>( object, method ) )
   {
   }

   /***************************************************************************
   * Copy constructor.
   ***************************************************************************/
   Callback( const Callback<Return, Args...> &that ) :
      callback( that.callback->clone() )
   {
   }

   /***************************************************************************
   * Destructor
   ***************************************************************************/
   virtual ~Callback()
   {
      delete callback;
      callback = 0;
   }

   /***************************************************************************
   * Convenience overload so this callback can be called like the function
   * it encapsulates.
   ***************************************************************************/
   Return operator()( const Args&... args )
   {
      return callback->invoke( args... );
   }

   /***************************************************************************
   * Assignment operator.
   ***************************************************************************/
   Callback &operator=( const Callback &that )
   {
      delete callback;
      callback = 0;
      callback = that.callback->clone();
   }

   /***************************************************************************
   * Equality operator.
   ***************************************************************************/
   bool operator==( const Callback<Return, Args...> &that ) const
   {
      if( this == &that ) return true;
      return *callback == *that.callback;
   }

private:

   // handle to our callback
   AbstractCallback<Return, Args...> *callback;
};

#endif
