import maker.project.Module
import maker.project.ClassicLayout

val project = new Module(
  new java.io.File("."), 
  "fred"
) with ClassicLayout {
  override def resources = List(
   "org.scalatest" %"scalatest_2.10" % "2.2.0",
   "org.eclipse.aether" %"aether-api" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-spi" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-util" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-test-util" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-impl" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-connector-basic" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-transport-file" % "1.0.0.v20140518",
   "org.eclipse.aether" %"aether-transport-http" % "1.0.0.v20140518",
   "org.apache.httpcomponents" %"httpclient" % "4.2.6",
   "org.apache.httpcomponents" %"httpcore" % "4.2.5",
   "commons-codec" %"commons-codec" % "1.6",
   "org.slf4j" %"jcl-over-slf4j" % "1.6.2",
   "org.slf4j" %"slf4j-api" % "1.6.2",
   "org.apache.maven" %"maven-aether-provider" % "3.1.0",
   "org.apache.maven" %"maven-model" % "3.1.0",
   "org.apache.maven" %"maven-model-builder" % "3.1.0",
   "org.codehaus.plexus" %"plexus-interpolation" % "1.16",
   "org.apache.maven" %"maven-repository-metadata" % "3.1.0",
   "org.codehaus.plexus" %"plexus-component-annotations" % "1.5.5",
   "org.codehaus.plexus" %"plexus-utils" % "2.1",
   "org.eclipse.sisu" %"org.eclipse.sisu.plexus" % "0.1.1",
   "org.eclipse.sisu" %"org.eclipse.sisu.inject" % "0.1.1",
   "org.codehaus.plexus" %"plexus-classworlds" % "2.4",
   "org.sonatype.sisu" %"sisu-guice" % "3.1.6",
   "javax.inject" %"javax.inject" % "1",
   "com.google.guava" %"guava" % "11.0.2",
   "ch.qos.logback" %"logback-classic" %"1.0.6",
   "ch.qos.logback" %"logback-core" %"1.0.6"
  )
}

import project._
