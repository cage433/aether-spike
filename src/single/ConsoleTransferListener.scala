/*******************************************************************************
 * Copyright (c) 2010, 2013 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package single

import java.io.PrintStream
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale
import java.util.Map
import java.util.concurrent.ConcurrentHashMap

import org.eclipse.aether.transfer.AbstractTransferListener
import org.eclipse.aether.transfer.MetadataNotFoundException
import org.eclipse.aether.transfer.TransferEvent
import org.eclipse.aether.transfer.TransferResource
import scala.collection.JavaConversions._
/**
 * A simplistic transfer listener that logs uploads/downloads to the console.
 */
class ConsoleTransferListener
    extends AbstractTransferListener
{

  val out : PrintStream = System.out

    private val downloads = new ConcurrentHashMap[TransferResource, Long]()

    private var lastLength  : Int = 0


    override def transferInitiated( event : TransferEvent ) {
        val message = if (event.getRequestType() == TransferEvent.RequestType.PUT) "Uploading" else "Downloading"

        out.println( message + ": " + event.getResource().getRepositoryUrl() + event.getResource().getResourceName() )
    }

    override def transferProgressed( event : TransferEvent ) {
        val resource = event.getResource()
//        downloads.put( resource, Long.valueOf( event.getTransferredBytes() ) )
        downloads.put( resource, event.getTransferredBytes().toLong  )

        val buffer = new StringBuilder( 64 )

        downloads.foreach{
          case (total, complete) => 
            buffer.append( getStatus( complete, total.getContentLength() ) ).append( "  " )
        }
//        for (entry <- downloads.entrySet() )
//        {
//            val total = entry.getKey().getContentLength()
//            val complete = entry.getValue().longValue()
//
//            buffer.append( getStatus( complete, total ) ).append( "  " )
//        }

        val pad_ = lastLength - buffer.length()
        lastLength = buffer.length()
        pad( buffer, pad_ )
        buffer.append( '\r' )

        out.print( buffer )
    }

    private def getStatus( complete : Long, total : Long ) : String = {
        if ( total >= 1024 )
        {
            toKB( complete ) + "/" + toKB( total ) + " KB "
        }
        else if ( total >= 0 )
        {
            complete + "/" + total + " B "
        }
        else if ( complete >= 1024 )
        {
            toKB( complete ) + " KB "
        }
        else
        {
            complete + " B "
        }
    }

    private def pad( buffer : StringBuilder , spaces_ : Int )
    {
      var spaces = spaces_
        val block = "                                        "
        while ( spaces > 0 )
        {
            val n = Math.min( spaces, block.length() )
            buffer.append( block, 0, n )
            spaces = spaces - n
        }
    }

    override def transferSucceeded( event : TransferEvent )
    {
        transferCompleted( event )

        val resource = event.getResource()
        val contentLength = event.getTransferredBytes()
        if ( contentLength >= 0 )
        {
            val type_ = if ( event.getRequestType() == TransferEvent.RequestType.PUT ) "Uploaded" else "Downloaded" 
            val len = if (contentLength >= 1024) toKB( contentLength ) + " KB" else contentLength + " B"

            var throughput = ""
            val duration = System.currentTimeMillis() - resource.getTransferStartTime()
            if ( duration > 0 )
            {
                val bytes = contentLength - resource.getResumeOffset()
                val format = new DecimalFormat( "0.0", new DecimalFormatSymbols( Locale.ENGLISH ) )
                val kbPerSec = ( bytes / 1024.0 ) / ( duration / 1000.0 )
                throughput = " at " + format.format( kbPerSec ) + " KB/sec"
            }

            out.println( type_ + ": " + resource.getRepositoryUrl() + resource.getResourceName() + " (" + len
                + throughput + ")" )
        }
    }

    override def transferFailed( event : TransferEvent )
    {
        transferCompleted( event )

        if ( !( event.getException().isInstanceOf[MetadataNotFoundException] ) )
        {
            event.getException().printStackTrace( out )
        }
    }

    def transferCompleted( event : TransferEvent )
    {
        downloads.remove( event.getResource() )

        val buffer = new StringBuilder( 64 )
        pad( buffer, lastLength )
        buffer.append( '\r' )
        out.print( buffer )
    }

    override def transferCorrupted( event : TransferEvent )
    {
        event.getException().printStackTrace( out )
    }

    def toKB( bytes : Long ) : Long = {
        return ( bytes + 1023 ) / 1024
    }

}
