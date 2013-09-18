
// system includes
#include <algorithm>
#include <stdexcept>

// local includes
#include "QueuedBinaryLogger.h"
#include "ScopedLock.h"

using namespace std;

/******************************************************************************
 * 
 *****************************************************************************/
QueuedBinaryLogger::QueuedBinaryLogger( const std::string &filename,
                                        const unsigned int &queueThreshold,
                                        const unsigned int &msgsPerIteration,
                                        const unsigned int &condWaitMsec ) :
   AbstractThread(),
   queueThreshold( queueThreshold ),
   msgsPerIteration( msgsPerIteration ),
   condWaitMsec( condWaitMsec ),
   queueMutex(),
   condition( queueMutex )
{
   if( msgsPerIteration < 1 )
      throw runtime_error( "QueuedBinaryLogger: Messages to process per iteration must be greater than zero." );
   if( condWaitMsec < 1 )
      throw runtime_error( "QueuedBinaryLogger: Conditioned wait must be greater than 0 milliseconds." );
   
   out.open( filename.c_str(), ios::binary );
   if( !out.is_open() )
      throw runtime_error( string( "QueuedBinaryLogger: Unable to open output file: " ).append( filename ) );
   
   start();
}

/******************************************************************************
 * 
 *****************************************************************************/
QueuedBinaryLogger::~QueuedBinaryLogger()
{
   queueMutex.lock();
   emptyQueue();
   out.close();
}

/******************************************************************************
 * 
 *****************************************************************************/
void QueuedBinaryLogger::threadFunction()
{
   while( isRunning )
   {
      queueMutex.lock();
      
      condition.wait( condWaitMsec );
      if( condition.isMet() )
      {
         condition.reset();
         if( msgQueue.size() >= queueThreshold )
            emptyQueue();
      }
      
      queueMutex.unlock();
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void QueuedBinaryLogger::log( const char *msg, const int &size )
{
   if( isRunning )
   {
      ScopedLock lock( queueMutex );
      BinaryMessage *qm = new BinaryMessage( msg, size );
      msgQueue.push( qm );
      condition.signal();
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void QueuedBinaryLogger::emptyQueue()
{
   queue<BinaryMessage *> msgs;
   unsigned int size;
   while( !msgQueue.empty() )
   {
      size = msgQueue.size();
      
      for( unsigned int i = 0; i < ( min( size, msgsPerIteration ) ); i++ )
      {
         msgs.push( msgQueue.front() );
         msgQueue.pop();
      }
      
      queueMutex.unlock(); // allow other threads to add to queue while we write to disk
      
      while( !msgs.empty() )
      {
         out.write( msgs.front()->getMessage(), msgs.front()->getSize() );
         delete msgs.front();
         msgs.pop();
      }
      
      queueMutex.lock();
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
QueuedBinaryLogger::BinaryMessage::BinaryMessage( const char *message, const int &size ) :
   msg( NULL ), size( size )
{
   msg = (char *)malloc( size );
   if( message != NULL )
      memcpy( msg, message, size );
}

/******************************************************************************
 * 
 *****************************************************************************/
QueuedBinaryLogger::BinaryMessage::~BinaryMessage()
{
   if( msg != NULL )
   {
      free( msg );
      msg = NULL;
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
const char *QueuedBinaryLogger::BinaryMessage::getMessage() const
{
   return msg;
}

/******************************************************************************
 * 
 *****************************************************************************/
const int QueuedBinaryLogger::BinaryMessage::getSize() const
{
   return size;
}

