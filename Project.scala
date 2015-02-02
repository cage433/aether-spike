import maker.project.Module
import maker.project.ClassicLayout

val project = new Module(
  new java.io.File("."), 
  new java.io.File("."), 
  "fred"
) with ClassicLayout
