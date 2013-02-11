## sbt-gitflow

Slight extensions to the [sbt-release](https://github.com/sbt/sbt-release) plugin to make it more compatible with [git flow](https://github.com/nvie/gitflow).

## Requirements
 * sbt 0.12.0 for *sbt-gitflow* 0.1.
 * The version of the project should follow the semantic versioning scheme on [semver.org](http://www.semver.org) with the following additions:
   * The minor and bugfix part of the version are optional.
   * The appendix after the bugfix part must be alphanumeric (`[0-9a-zA-Z]`) but may also contain dash characters `-`.
   * These are all valid version numbers:
     * 1.2.3
     * 1.2.3-SNAPSHOT
     * 1.2beta1
     * 1.2
     * 1
     * 1-BETA17
 * A [publish repository](https://github.com/harrah/xsbt/wiki/Publishing) configured. (Required only for the default release process. See further below for release process customizations.)
 * git [optional]

## Usage

For anything other than the basic instructions provided here, refer to the [sbt-release](https://github.com/sbt/sbt-release) documentation.

### Adding the plugin dependency

Add the following line to `./project/build.sbt`. See the section [Using Plugins](https://github.com/harrah/xsbt/wiki/Getting-Started-Using-Plugins) in the xsbt wiki for more information.

    addSbtPlugin("com.twitter" % "sbt-gitflow" % "0.1")

### Including sbt-release settings
**Important:** The settings `releaseSettings` need to be mixed into every sub-projects `settings`.
This is usually achieved by extracting common settings into a `val standardSettings: Seq[Setting[_]]` which is then included in all sub-projects.

Setting/task keys are defined in `sbtrelease.ReleasePlugin.ReleaseKeys`.

#### build.sbt (simple build definition)

    releaseSettings

#### build.scala (full build definition)

    import sbgitflow.ReleasePlugin._

    object MyBuild extends Build {
      lazy val MyProject(
        id = "myproject",
        base = file("."),
        settings = Defaults.defaultSettings ++ releaseSettings ++ Seq( /* custom settings here */ )
      )
    }

## Author

* Sam Ritchie <http://twitter.com/sritchie>

## License

Copyright 2012 Twitter, Inc.

Licensed under the Apache License, Version 2.0: http://www.apache.org/licenses/LICENSE-2.0
