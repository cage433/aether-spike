package single

import java.util.{List, LinkedList, Arrays}
import org.eclipse.aether.util.graph.selector._
import org.eclipse.aether.{RepositorySystem, RepositorySystemSession}
import org.eclipse.aether.artifact.{Artifact, DefaultArtifact}
import org.eclipse.aether.collection.{CollectRequest, CollectResult}
import org.eclipse.aether.graph._
import org.eclipse.aether.resolution.{ArtifactResult, DependencyRequest}
import org.eclipse.aether.util.artifact.JavaScopes
import org.eclipse.aether.util.filter._
import scala.collection.JavaConversions._
import org.eclipse.aether.repository.RemoteRepository
import org.eclipse.aether.internal.impl.DefaultRepositorySystem
import org.eclipse.aether.internal.test.util.DependencyGraphParser

object Pimps{
  class OrgAndArtifact(org : String, artifact : String){
    def %(version : String) = new Dependency(new DefaultArtifact(s"$org:$artifact:$version"), JavaScopes.COMPILE)
  }
  implicit class Organization(name : String){
    def %(artifact : String) = new OrgAndArtifact(name, artifact)
  }
  implicit class PimpedDependency(dependency : Dependency){
    def withScope(scope : String) = new Dependency(
      dependency.getArtifact,
      scope,
      dependency.getOptional,
      dependency.getExclusions
    )
    def optional = new Dependency(
      dependency.getArtifact,
      dependency.getScope,
      true,
      dependency.getExclusions
    )
    def withExclusions(groupAndArtifacts : String*) = {
      val exclusions = groupAndArtifacts.map{
        gAndA => 
          gAndA.split(":") match {
            case Array(group, artifact) => 
              new Exclusion(group, artifact, "*", "*")
            case Array(group) => 
              new Exclusion(group, "*", "*", "*")
            case other =>
              ???
          }
      }

      new Dependency(
        dependency.getArtifact,
        dependency.getScope,
        dependency.getOptional,
        exclusions
      )
    }
  }
}

/**
 * Resolves the transitive (compile) dependencies of an artifact.
 */
object Aether extends App
{

  import Pimps._
  val akkaV = "2.3.9"
  val sprayV = "1.3.2"
  val dependencies = Vector(
    //"com.github.stacycurl" % "pimpathon-core_2.11" % "1.1.0",
    //"org.scalaz" % "scalaz-core_2.11" % "7.1.1",
    //"com.chuusai" % "shapeless_2.11" % "2.0.0",
    "org.scalatest" % "scalatest_2.11" % "2.2.0" withScope(JavaScopes.TEST) withExclusions("org.scala-lang", "org.scala-lang.modules"),
    //"org.scalacheck" % "scalacheck_2.11" % "1.12.2",
    //"ch.qos.logback" % "logback-classic" % "1.0.11",

    //"org.apache.commons" % "commons-math3" % "3.4.1",
    //"org.apache.commons" % "commons-exec" % "1.3",
    //"com.github.scopt" % "scopt_2.11" % "3.3.0" withScope(JavaScopes.TEST),

    //"org.apache.commons" % "commons-math3" % "3.3",
    Organization("org.scalanlp") % "breeze_2.11" % "0.10" withExclusions(
      "junit:junit", 
      "org.scalanlp:breeze-macros_2.11", 
      "org.scala-lang"
    )
    //"org.scalanlp" % "breeze-natives_2.11" % "0.10" withExclusions("junit:junit"),

    //"io.spray" % s"spray-json_2.11" % "1.3.1",

    //"org.apache.kafka" % s"kafka_2.11" % "0.8.2.0",

    //"com.h2database" % "h2" % "1.3.176" withScope(JavaScopes.TEST),

    //"com.typesafe" % "config" % "1.2.1",
    //"org.scaldi" % "scaldi_2.11" % "0.5.3",
    //"org.scaldi" % "scaldi-akka_2.11" % "0.5.3",

    //"io.spray" % "spray-can_2.11" % sprayV,
    //"io.spray" % "spray-routing-shapeless2_2.11" % sprayV,
    //"com.typesafe.akka" % "akka-actor_2.11" % akkaV,
    //"org.scalamock" % "scalamock-scalatest-support_2.11" % "3.2.1",
    //"io.spray" % "spray-testkit_2.11" % sprayV 
 
  )
                 
  System.out.println( "------------------------------------------------------------" )

  val system = Booter.newRepositorySystem()
  val session = Booter.newRepositorySystemSession( system )
    
  session.setDependencySelector(
    new AndDependencySelector(
      new OptionalDependencySelector(),
      new ExclusionDependencySelector(),
      new ScopeDependencySelector(
        Arrays.asList(JavaScopes.COMPILE),
        Arrays.asList(JavaScopes.TEST, JavaScopes.SYSTEM, JavaScopes.PROVIDED)
      )
    )
  )
  val collectRequest = new CollectRequest(dependencies, new java.util.LinkedList[Dependency](), new java.util.LinkedList[RemoteRepository]())
         
  val l = new LinkedList[RemoteRepository]()
  l.add(Booter.newCentralRepository)
  collectRequest.setRepositories(l)

  val collectResult : CollectResult = system.collectDependencies(session, collectRequest)
  val dependencyNode : DependencyNode = collectResult.getRoot
  val parser = new DependencyGraphParser()
  println("GRAPH")
  println(parser.dump(dependencyNode))


  val filter: DependencyFilter = new AndDependencyFilter(
    DependencyFilterUtils.classpathFilter(JavaScopes.TEST)
  )
  val dependencyRequest = new DependencyRequest( collectRequest, filter)
  val artifactResults =
      system.resolveDependencies( session, dependencyRequest ).getArtifactResults()

  println("RESULTS")
  for (artifactResult <- artifactResults )
  {
    println(artifactResult)
  }
}
