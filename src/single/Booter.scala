/*******************************************************************************
 * Copyright (c) 2010, 2014 Sonatype, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Sonatype, Inc. - initial API and implementation
 *******************************************************************************/
package single


import org.apache.maven.repository.internal.MavenRepositorySystemUtils
import org.eclipse.aether.DefaultRepositorySystemSession
import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.repository.LocalRepository
import org.eclipse.aether.repository.RemoteRepository
import java.util.LinkedList
import org.eclipse.aether.repository.Proxy

/**
 * A helper to boot the repository system and a repository system session.
 */
object Booter
{

    def newRepositorySystem() : RepositorySystem = {
      ManualRepositorySystemFactory.newRepositorySystem()
    }

    def newRepositorySystemSession( system : RepositorySystem ) = 
    {
        val session = MavenRepositorySystemUtils.newSession()

        //val localRepo = new LocalRepository( "target/local-repo" )
        val localRepo = new LocalRepository( "/home/alex/tmp/resource-cache/" )
        session.setLocalRepositoryManager( system.newLocalRepositoryManager( session, localRepo ) )

        session.setTransferListener( new ConsoleTransferListener() )
        session.setRepositoryListener( new ConsoleRepositoryListener() )

        // uncomment to generate dirty trees
        // session.setDependencyGraphTransformer( null )

        session
    }

    //def newRepositories( system : RepositorySystem , session : RepositorySystemSession ) : java.util.List[RemoteRepository] = 
    //{
      //val l = new LinkedList[RemoteRepository]()
      //val repo = newCentralRepository()
      //val bldr = new RemoteRepository.Builder(repo)
      //println("Not setting proxy")
      //bldr.setProxy(new Proxy(Proxy.TYPE_HTTP, "localhost", 4128))
      //val repo2 = bldr.build()
      //l.add(repo2)
      //l
////      List(newCentralRepository())
    //}

    def newCentralRepository() = {
        new RemoteRepository.Builder( "central", "default", "http://central.maven.org/maven2/" ).build()
    }

}
