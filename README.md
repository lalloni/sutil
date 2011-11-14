Welcome to Sutil
================

Sutil is just a small library of utilities not found (by the author) in other packages, written for the scala programming language.

Some of the features include (in no particular order):

Glob support & File enhancements
--------------------------------

Globbing:

```scala
Seq("aalbla", "bla", "blo", "cla", "xlbasj") filter Glob("?l[ab]*") should be (Seq("bla", "cla", "xlbasj"))
```

File path manipulation & listing:

```scala
new File("/tmp") / "some" / "file.txt"        // returns a new File("/tmp/some/file.txt")
new File("/tmp/file.txt") :+ ".gz"            // returns a new File("/tmp/file.txt.gz")
new File("/tmp") / Glob("*.txt")              // returns a Seq[File] listing matching files in /tmp
new File("/tmp").ls                           // returns a Seq[File] listing all files (and dirs) in /tmp
new File("/tmp").lsr                          // returns a Seq[File] listing all files (and dirs) in /tmp and subdirs
new File("/tmp/bla").parent                   // returns a new File("/tmp")
new File("/etc") / Glob("*.d") / Glob("*.sh") // returns all "*.sh" files present in all "*.d" directories in "/etc"
new File("/etc") / Glob("*.d") / "a_file"     // returns all "a_file" files present in all "*.d" directories in "/etc"
```

Note that all ```Glob("something")``` can be replaced by ```"something".g```.

Version string parsing and matching
-----------------------------------

A set of classes to parse and match version strings in "common" formats.

Classes modeling version strings and it components: 

```scala
Version(VersionNumber(2, 1), VersionModifier("beta", VersionNumber(5)))   // version "2.1-beta5"
```

with shorthand synonyms:

```scala
V(N(2, 1), M("beta", N(5)))   // version "2.1-beta5"
```

Nice version parsing from strings:

```scala
Version("4.1.2-beta2-ubuntu1") == V(N(4, 1, 2), M("beta", N(2)), M("ubuntu", N(1)))
```

and the other way aroung using "toString":

```scala
V(N(4, 1, 2), M("beta", N(2)), M("ubuntu", N(1))).toString == "4.1.2-beta2-ubuntu1"
```

All three Version, VersionNumber and VersionModifier implements Ordered, so it gets useful:

```scala
Seq[Version]("3-sp4", "1", "1.0", "1.1", "2", "0.1", "2-snapshot", "3").sorted    
  // returns Seq[Version]("0.1", "1", "1.0", "1.1", "2-snapshot", "2", "3", "3-sp4")
```

(there's an implicit conversion from String to Version inplace).

Version modifier tags compare impl knows some "common" (case insensitive) version tags:

```scala
Seq[VersionModifier]("beta2", "Final", "ALPHA3", "sp2", "snapshot", "cr1", "beta").sorted 
  // returns Seq[VersionModifier]("snapshot", "ALPHA3", "beta", "beta2", "cr1", "Final", "sp2")
```

And there's a Version Range too:

```scala
V("2.1") to V("3.0") contains V("2.5-beta2")            // evaluates to true
V("2.1") until V("3.0") contains V("3.0")               // evaluates to false
V("2.1") until V("3.0") contains V("3.0-snapshot")      // evaluates to true (tricky!)
```

(version and versionnumber ranges are not traversable since they are continuous).

Some more features like version increments, version number positions, etc.

To be continued...