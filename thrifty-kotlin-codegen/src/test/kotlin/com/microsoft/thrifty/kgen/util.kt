package com.microsoft.thrifty.kgen

import com.squareup.kotlinpoet.FileSpec
import com.tschuchort.compiletesting.KotlinCompilation
import com.tschuchort.compiletesting.KotlinCompilation.ExitCode
import com.tschuchort.compiletesting.KotlinCompilation.Result
import com.tschuchort.compiletesting.SourceFile
import io.kotest.matchers.Matcher
import io.kotest.matchers.MatcherResult
import io.kotest.matchers.should
import java.io.ByteArrayOutputStream
import java.nio.file.Files
import kotlin.streams.toList

fun List<FileSpec>.shouldCompile() {
    this should compiles
}

fun FileSpec.shouldCompile() {
    listOf(this) should compiles
}

private fun compileCodeSnippet(codeSnippet: List<FileSpec>): Result {
    val kotlinCompilation = KotlinCompilation()
        .apply {
            inheritClassPath = true
            verbose = false
            messageOutputStream = ByteArrayOutputStream()
        }
    codeSnippet.forEach { it.writeTo(kotlinCompilation.workingDir) }
    kotlinCompilation.sources = Files.find(
        kotlinCompilation.workingDir.toPath(),
        20,
        { _, basicFileAttributes -> basicFileAttributes.isRegularFile })
        .map { SourceFile.fromPath(it.toFile()) }.toList()

    return kotlinCompilation.compile()
}

private val compiles = object : Matcher<List<FileSpec>> {
    override fun test(value: List<FileSpec>): MatcherResult {
        val compilationResult = compileCodeSnippet(value)
        return MatcherResult(
            compilationResult.exitCode == ExitCode.OK,
            {
                "Expected code to compile, but it failed to compile with error: \n${compilationResult.messages}"
            },
            { "Expected code to fail to compile, but it compiled" }
        )
    }
}
