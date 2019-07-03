/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report

import com.d3.report.service.TestReportService
import com.github.mustachejava.DefaultMustacheFactory
import org.gradle.api.Plugin
import org.gradle.api.Project
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files

private const val REPORT_TEMPLATE_FILE = "report.html"
private const val REPORT_CSS_FILE = "semantic.min.css"
private const val REPORT_FOLDER = "build/reports/"
private const val REPORT_FILE_NAME = "d3-test-report.html"

private val reportService = TestReportService()

/**
 * D3 test report plugin
 */
class ReportPlugin : Plugin<Project> {
    /**
     * The main logic of the plugin
     */
    override fun apply(project: Project) {
        project.task("d3TestReport") {
            // Get plugin config
            val extension = project.extensions.create(
                "testReport", ReportExtension::class.java
            )
            it.doLast {
                createReport(extension)
            }
        }
    }
}

/**
 * Creates report files
 * @param extension - report configuration from build.gradle
 **/
private fun createReport(extension: ReportExtension) {
    val reportFolder = File(REPORT_FOLDER)
    // Create the report folder
    if (!reportFolder.exists() && !reportFolder.mkdirs()) {
        throw IllegalStateException("Cannot create report folder $REPORT_FOLDER")
    }
    copyCssFile()
    createReportHtml(extension.testFolders)
}

/**
 * Copies report CSS file to the report folder
 */
private fun copyCssFile() {
    val cssFile = File("$REPORT_FOLDER$REPORT_CSS_FILE")
    if (cssFile.exists()) {
        return
    }
    Files.copy(
        Thread.currentThread().contextClassLoader.getResourceAsStream(REPORT_CSS_FILE)!!,
        cssFile.toPath()
    )
}

/**
 * Creates HTML report file in the report folder
 * @param testFolders - folders with tests
 */
private fun createReportHtml(testFolders: List<String>) {
    val mf = DefaultMustacheFactory()
    val mustache = mf.compile(REPORT_TEMPLATE_FILE)
    BufferedWriter(FileWriter("$REPORT_FOLDER$REPORT_FILE_NAME"))
        .use { writer ->
            mustache.execute(
                writer,
                reportService.create(testFolders)
            ).flush()
            println(
                "ReportExtension was successfully created. " +
                        "Take a look at $REPORT_FOLDER$REPORT_FILE_NAME"
            )
        }
}
