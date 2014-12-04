/*******************************************************************************
 * Copyright (c) 2010, 2011 Sonatype, Inc.
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

import org.eclipse.aether.AbstractRepositoryListener
import org.eclipse.aether.RepositoryEvent

/**
 * A simplistic repository listener that logs events to the console.
 */
class ConsoleRepositoryListener()
    extends AbstractRepositoryListener
{

    val out : PrintStream = System.out


    override def artifactDeployed( event : RepositoryEvent)
    {
        out.println( "Deployed " + event.getArtifact() + " to " + event.getRepository() )
    }

    override def artifactDeploying( event : RepositoryEvent)
    {
        out.println( "Deploying " + event.getArtifact() + " to " + event.getRepository() )
    }

    override def artifactDescriptorInvalid( event : RepositoryEvent)
    {
        out.println( "Invalid artifact descriptor for " + event.getArtifact() + ": "
            + event.getException().getMessage() )
    }

    override def artifactDescriptorMissing( event : RepositoryEvent)
    {
        out.println( "Missing artifact descriptor for " + event.getArtifact() )
    }

    override def artifactInstalled( event : RepositoryEvent)
    {
        out.println( "Installed " + event.getArtifact() + " to " + event.getFile() )
    }

    override def artifactInstalling( event : RepositoryEvent)
    {
        out.println( "Installing " + event.getArtifact() + " to " + event.getFile() )
    }

    override def artifactResolved( event : RepositoryEvent)
    {
        out.println( "Resolved artifact " + event.getArtifact() + " from " + event.getRepository() )
    }

    override def artifactDownloading( event : RepositoryEvent)
    {
        out.println( "Downloading artifact " + event.getArtifact() + " from " + event.getRepository() )
    }

    override def artifactDownloaded( event : RepositoryEvent)
    {
        out.println( "Downloaded artifact " + event.getArtifact() + " from " + event.getRepository() )
    }

    override def artifactResolving( event : RepositoryEvent)
    {
        out.println( "Resolving artifact " + event.getArtifact() )
    }

    override def metadataDeployed( event : RepositoryEvent)
    {
        out.println( "Deployed " + event.getMetadata() + " to " + event.getRepository() )
    }

    override def metadataDeploying( event : RepositoryEvent)
    {
        out.println( "Deploying " + event.getMetadata() + " to " + event.getRepository() )
    }

    override def metadataInstalled( event : RepositoryEvent)
    {
        out.println( "Installed " + event.getMetadata() + " to " + event.getFile() )
    }

    override def metadataInstalling( event : RepositoryEvent)
    {
        out.println( "Installing " + event.getMetadata() + " to " + event.getFile() )
    }

    override def metadataInvalid( event : RepositoryEvent)
    {
        out.println( "Invalid metadata " + event.getMetadata() )
    }

    override def metadataResolved( event : RepositoryEvent)
    {
        out.println( "Resolved metadata " + event.getMetadata() + " from " + event.getRepository() )
    }

    override def metadataResolving( event : RepositoryEvent)
    {
        out.println( "Resolving metadata " + event.getMetadata() + " from " + event.getRepository() )
    }

}
