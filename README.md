# sbt-slick-codegen

[![Continuous Integration](https://github.com/sbt/sbt-slick-codegen/actions/workflows/ci.yml/badge.svg)](https://github.com/sbt/sbt-slick-codegen/actions/workflows/ci.yml)

slick-codegen compile hook for sbt

## Install

|Slick version|slick-codegen version|sbt version|
|-------------|---------------------|-----------|
|        3.5.x|                2.2.0|        1.x|
|        3.4.x|                2.1.0|        1.x|
|        3.3.x|                2.0.0|        1.x|
|        3.3.x|                1.4.0|        1.x|
|        3.2.x|                1.3.0|        1.x|
|        3.1.x|                1.2.1|     0.13.x|
|        3.0.x|                1.1.1|     0.13.x|


```scala
// plugins.sbt

addSbtPlugin("com.github.sbt" % "sbt-slick-codegen" % slickCodegenVersion)

// Database driver
// For example, when you are using PostgreSQL
libraryDependencies += "org.postgresql" % "postgresql" % "9.4-1201-jdbc41"
```

Releases through 2.2.0 use the previous group ID, `com.github.tototoshi`.

## Configuration

```scala
// build.sbt
import slick.codegen.SourceCodeGenerator
import slick.{ model => m }

// required
enablePlugins(CodegenPlugin)

// required
// Register codegen hook
Compile / sourceGenerators += slickCodegen

// required
slickCodegenDatabaseUrl := "jdbc:postgresql://localhost/example"

// required
slickCodegenDatabaseUser := "dbuser"

// required
slickCodegenDatabasePassword := "dbpassword"

// required (If not set, postgresql driver is choosen)
slickCodegenDriver := slick.jdbc.PostgresProfile

// required (If not set, postgresql driver is choosen)
slickCodegenJdbcDriver := "org.postgresql.Driver"

// optional but maybe you want
slickCodegenOutputPackage := "com.example.models"

// optional, pass your own custom source code generator
slickCodegenCodeGenerator := { (model: m.Model) => new SourceCodeGenerator(model) }

// optional
// For example of all the tables in a database we only would like to take table named "users"
Compile / slickCodegenIncludedTables := Seq("users")

// optional
// For example, to exclude flyway's schema_version table from the target of codegen. This still applies after slickCodegenIncludedTables.
Compile / slickCodegenExcludedTables := Seq("schema_version")

//optional
slickCodegenOutputDir := (Compile / sourceManaged).value

//optional. Generate one Scala file per table.
slickCodegenOutputToMultipleFiles := false
```

## Example

https://github.com/tototoshi/sbt-slick-codegen-example


## License

Apache 2.0
