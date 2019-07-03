/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.service

import com.d3.report.counter.TestsCounter
import com.d3.report.filter.TestsFilter
import com.d3.report.model.Report
import com.d3.report.model.Summary
import com.d3.report.model.TestCount
import com.d3.report.model.TestFileReport
import com.d3.report.parser.TestParser
import com.d3.report.walker.TestsWalker
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

private const val DATE_FORMAT = "dd.MM.yyyy HH:mm"
private const val D3_PROJECT_TITLE = "D3 test report"

/*
 * Test report creating service
 */
class TestReportService {

    private val testParser = TestParser()
    private val testsWalker = TestsWalker()
    private val testsCounter = TestsCounter()
    private val testsFilter = TestsFilter()

    /**
     * Creates complete report for all test files located in given folders
     *
     * @param rootFolders root folder where all the tests are stored
     * @return complete report
     */
    fun create(rootFolders: List<String>) = merge(rootFolders.map { rootFolder -> create(rootFolder) })

    /**
     * Creates complete report for all test files located in given folder
     *
     * @param rootFolder root folder where all the tests are stored
     * @return complete report
     */
    private fun create(rootFolder: String): Report {
        val report = Report()
        val testFiles = testsWalker.getTestFiles(rootFolder)
        val summary = initSummary()
        report.summary = summary
        if (testFiles.isEmpty()) {
            return report
        }
        val testFilesPretty = ArrayList<String>(testFiles.size)
        val testFileReports = ArrayList<TestFileReport>()
        val noDescriptionTestCases = ArrayList<String>()
        var testCasesCount = 0
        var disabledCasesCount = 0
        testFiles.forEach { testFile ->
            val testCount = collectReports(
                testFile,
                rootFolder,
                testFilesPretty,
                testFileReports,
                noDescriptionTestCases
            )
            testCasesCount += testCount.getTestCases()
            disabledCasesCount += testCount.getDisabledTestCases()
        }
        summary.filesWithTests = testFilesPretty.size
        summary.testCases = testCasesCount
        summary.disabledCases = disabledCasesCount
        report.reportData = testFileReports
        report.testFiles = testFilesPretty
        report.noDescriptionTestCases = noDescriptionTestCases
        summary.lacksInDescription = noDescriptionTestCases.size
        return report
    }

    /**
     * Collects test reports
     * @param testFile - test file
     * @param rootFolder - root folder with tests
     * @param testFiles - list that is used to collect test files
     * @param testFileReports - list that is used to collect test reports
     * @param noDescriptionTestCases - list that is used to collect test cases with no description
     */
    private fun collectReports(
        testFile: String,
        rootFolder: String,
        testFiles: MutableList<String>,
        testFileReports: MutableList<TestFileReport>,
        noDescriptionTestCases: MutableList<String>
    ): TestCount {
        val linesOfCode = File(testFile).readLines()
        val testCases = testsCounter.countTests(linesOfCode)
        if (testCases == 0) {
            return TestCount.noTests()
        }
        val reportItems = testParser.createReport(linesOfCode)
        val allTestCases = testParser.getTestCases(linesOfCode)
        noDescriptionTestCases.addAll(testsFilter.getNoDescriptionTests(allTestCases, reportItems))
        val testFileName = prettifyFileName(testFile, rootFolder)
        testFiles.add(testFileName)
        testFileReports.add(TestFileReport(testFileName, reportItems))
        return TestCount()
            .setDisabledTestCases(testsCounter.countDisabledTests(reportItems))
            .setTestCases(testCases)
    }

    private fun initSummary(): Summary {
        val summary = Summary()
        summary.generationDate = getCurrentDate()
        summary.title = D3_PROJECT_TITLE
        return summary
    }

    private fun prettifyFileName(filePath: String, rootFolder: String) =
        filePath.substring(filePath.indexOf(rootFolder) + rootFolder.length)

    /**
     * Merges all reports in one
     * @param reports - reports to merge
     * @return one big report
     */
    private fun merge(reports: List<Report>): Report {
        if (reports.size == 1) {
            return reports[0]
        }
        val mainReport = reports[0]
        val mainReportSummary = mainReport.summary
        reports.forEach { report ->
            mainReport.testFiles.addAll(report.testFiles)
            mainReport.noDescriptionTestCases.addAll(report.noDescriptionTestCases)
            mainReport.reportData.addAll(report.reportData)

            mainReportSummary.disabledCases = mainReportSummary.disabledCases + report.summary.disabledCases
            mainReportSummary.filesWithTests = mainReportSummary.filesWithTests + report.summary.filesWithTests
            mainReportSummary.lacksInDescription = mainReportSummary.lacksInDescription +
                    report.summary.lacksInDescription
            mainReportSummary.testCases = mainReportSummary.testCases + report.summary.testCases
        }
        return mainReport
    }

    /**
     * Returns pretty formatted current date
     */
    private fun getCurrentDate(): String {
        val simpleDateFormat = SimpleDateFormat(DATE_FORMAT)
        return simpleDateFormat.format(Date())
    }
}
