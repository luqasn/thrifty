Gradle Plugin
-------------------

Thrifty's Gradle plugin allows one to incorporate .thrift source files into a Java/Kotlin project
and have Thrifty generate Java/Kotlin code from them as part of regular builds.

Incorporate it into your build like so:

```gradle
apply plugin: 'kotlin'
apply plugin: 'com.microsoft.thrifty'

// usual gradle stuff

// Configuration is optional as there are generally sane defaults
thrifty {
    // By default, thrifty will use all thrift files in 'src/main/thrift'.
    // You can override this in the following ways:

    // Specify one or more directories containing thrift tiles.
    // Note that if you do, 'src/main/thrift' is ignored.
    sourceDir 'path/to/thrift/files', 'path/to/other/thrift/files'

    // Augment the thrift include path with one or more directories:
    includeDir 'path/to/include/dir', 'path/to/other/dir'

    // By default, Java sources will be generated.  You can provide a few options to the Java
    // code generator (all options shown with their default values):
    java {
        // Thrift service implementations are generated by default.  Set 'false'
        // here to prevent them from being generated.
        emitServiceClients true

        // By default, struct fields are named exactly as written in the .thrift IDL.
        // Other choices are 'java' and 'pascal' - 'someField' and 'SomeField', respectively.
        nameStyle 'default'

        // Standard Java collections are used by default.  If you want something custom, provide
        // that here.
        // For example, you might choose 'android.util.ArrayMap' for maps.
        listType 'java.util.ArrayList'
        setType 'java.util.LinkedHashSet'
        mapType 'java.util.LinkedHashMap'

        // Specify an annotation type name if you want Thrifty to tag generated classes
        // with it.  Known-compatible annotation types are 'javax.annotation.Generated'
        // (pre JDK-9) and 'javax.annotation.processing.Generated' (post JDK-9).
        //
        // The default behavior is that no annotation is supplied.
        generatedAnnotationType null

        // All of the options above are applicable to Kotlin.  Options below are Java-specific.

        // Specifies which kind of nullability annotations to add to generated code, if any.
        //
        // valid values are 'none' (the default), 'android-support', and 'androidx'.
        nullabilityAnnotationKind 'none'
    }

    // On the other hand, if you want Kotlin sources, then _don't_ add the java block.  Add at
    // least an empty 'kotlin' block - all options shown here may be left out entirely.
    kotlin {
        // Most of the options shown in the java block above also apply here.

        // Kotlin-specific options follow.

        // When true, generated structs will be represented as pure data classes, without
        // associated nested 'Builder' types.  This is experimental.
        builderlessDataClasses = false

        // The Kotlin code generator supports several different service-client API styles.
        // Vaid values are 'default' (the default), 'coroutine', and 'none'.
        serviceClientStyle 'default'
    {
}
```