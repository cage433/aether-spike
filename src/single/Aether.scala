package single

import java.util.List

import org.eclipse.aether.RepositorySystem
import org.eclipse.aether.RepositorySystemSession
import org.eclipse.aether.artifact.Artifact
import org.eclipse.aether.artifact.DefaultArtifact
import org.eclipse.aether.collection.CollectRequest
import org.eclipse.aether.graph.Dependency
import org.eclipse.aether.graph.DependencyFilter
import org.eclipse.aether.resolution.ArtifactResult
import org.eclipse.aether.resolution.DependencyRequest
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter.DependencyFilterUtils
import scala.collection.JavaConversions._

/**
 * Resolves the transitive (compile) dependencies of an artifact.
 */
object Aether extends App
{

  System.out.println( "------------------------------------------------------------" )

  val system = Booter.newRepositorySystem()

  val session = Booter.newRepositorySystemSession( system )

  val artifact = new DefaultArtifact( "org.eclipse.aether:aether-impl:1.0.0.v20140518" )

  val classpathFlter = DependencyFilterUtils.classpathFilter( JavaScopes.COMPILE )

  val collectRequest = new CollectRequest()
  collectRequest.setRoot( new Dependency( artifact, JavaScopes.COMPILE ) )
  collectRequest.setRepositories( Booter.newRepositories( system, session ) )

  val dependencyRequest = new DependencyRequest( collectRequest, classpathFlter )

  println(" system = " + system)
  println(" session = " + session)
  val artifactResults =
      system.resolveDependencies( session, dependencyRequest ).getArtifactResults()

  for (artifactResult <- artifactResults )
  {
      println( artifactResult.getArtifact() + " resolved to " + artifactResult.getArtifact().getFile() )
  }
}
