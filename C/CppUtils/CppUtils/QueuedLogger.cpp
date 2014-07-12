
// system includes
#include <algorithm>
#include <sstream>
#include <stdexcept>

// local includes
#include "QueuedLogger.h"
#include "ScopedLock.h"
#include "StringUtils.h"

using namespace std;
using namespace StringUtils;

const int QueuedLogger::TIME_PAD = 10;
const int QueuedLogger::PREF_PAD = 20;

/******************************************************************************
 * 
 *****************************************************************************/
QueuedLogger::QueuedLogger( const std::string &filename,
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
      throw runtime_error( "QueuedLogger: Messages to process per iteration must be greater than zero." );
   if( condWaitMsec < 1 )
      throw runtime_error( "QueuedLogger: Conditioned wait must be greater than 0 milliseconds." );
   
   out.open( filename.c_str() );
   if( !out.is_open() )
      throw runtime_error( "QueuedLogger: Unable to open output file: " + filename );
   
   start();
}

/******************************************************************************
 * 
 *****************************************************************************/
QueuedLogger::~QueuedLogger()
{
   shutdown();
   queueMutex.lock();
   emptyQueue();
   out.close();
}

/******************************************************************************
 * 
 *****************************************************************************/
void QueuedLogger::threadFunction()
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
void QueuedLogger::log( const std::string &msg, const std::string &prefix, const double &timestamp )
{
   if( isRunning )
   {
      stringstream ss;
      ss << padStringLeft( TIME_PAD, ' ', toString( timestamp ) ) << ": [" <<
         padStringLeft( PREF_PAD, ' ', prefix ) << "] - " << msg << endl;
      
      ScopedLock lock( queueMutex );
      msgQueue.push( ss.str() );
      condition.signal();
   }
}

/******************************************************************************
 * 
 *****************************************************************************/
void QueuedLogger::emptyQueue()
{
   queue<string> msgs;
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
         out << msgs.front();
         msgs.pop();
      }
      
      queueMutex.lock();
   }
}

