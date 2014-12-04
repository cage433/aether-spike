import maker.project.Module
import maker.project.ClassicLayout

val project = new Module(
  root = new java.io.File("."), 
  name = "fred"
) with ClassicLayout
