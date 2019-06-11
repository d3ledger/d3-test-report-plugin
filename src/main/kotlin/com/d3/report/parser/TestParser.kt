/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.parser

import com.d3.report.model.ReportItem
import java.util.*

private const val GIVEN = "@given"
private const val TODO = "TODO"
private const val WHEN = "@when"
private const val THEN = "@then"
private const val TEST = "@Test"
private const val TEST_INSTANCE = "@TestInstance"
private const val FUN = "fun "
private const val CLASS = "class"
private const val DISABLED = "@Disabled"
private const val EXTRA_SYMBOLS_REG_EXP = "$GIVEN|$WHEN|$THEN|$FUN|$DISABLED"

/*
 *  Class that is used to parse tests source code
 */
class TestParser {

    /**
     * Creates report based on test file source code
     *
     * @param linesOfCode list full of test file source code lines
     * @return test file report
     */
    fun createReport(linesOfCode: List<String>): List<ReportItem> {
        val reportItems = ArrayList<ReportItem>()
        val disabledTestClass = isDisabledTestClass(linesOfCode)
        var lineToProcess = 0
        while (lineToProcess < linesOfCode.size) {
            lineToProcess = addReportItem(
                reportItems,
                linesOfCode,
                lineToProcess,
                disabledTestClass
            )
        }
        return reportItems
    }

    /**
     * Returns test cases based on test file source code
     *
     * @param linesOfCode list full of test file source code lines
     * @return test cases
     */
    fun getTestCases(linesOfCode: List<String>): List<String> {
        val testCases = ArrayList<String>()
        var lineToProcess = 0
        while (lineToProcess < linesOfCode.size) {
            lineToProcess = addTestCase(testCases, linesOfCode, lineToProcess)
        }
        return testCases
    }

    private fun isDisabledTestClass(linesOfCode: List<String>): Boolean {
        var line: String
        for (lineToProcess in linesOfCode.indices) {
            line = linesOfCode[lineToProcess]
            if (!line.startsWith(CLASS)) {
                continue
            }
            return codeContains(linesOfCode, lineToProcess, DISABLED)
        }
        return false
    }

    private fun codeContains(lineOfCode: List<String>, lastLine: Int, code: String): Boolean {
        for (i in 0 until lastLine) {
            if (lineOfCode[i].contains(code)) {
                return true
            }
        }
        return false
    }

    //TODO refactor
    private fun addReportItem(
        reportItems: MutableList<ReportItem>,
        linesOfCode: List<String>,
        lineToProcess: Int,
        disabledTestClass: Boolean
    ): Int {
        var lineToProcess = lineToProcess
        var line: String
        while (lineToProcess < linesOfCode.size) {
            line = linesOfCode[lineToProcess]
            if (!line.contains(GIVEN)) {
                lineToProcess++
                continue
            }
            val givenContent = StringBuilder()
            while (!line.contains(WHEN)) {
                givenContent.append(line)
                if (++lineToProcess == linesOfCode.size) {
                    return lineToProcess
                }
                line = linesOfCode[lineToProcess]
            }
            val whenContent = StringBuilder()
            while (!line.contains(THEN)) {
                whenContent.append(line)
                if (++lineToProcess == linesOfCode.size) {
                    return lineToProcess
                }
                line = linesOfCode[lineToProcess]
            }
            val thenContent = StringBuilder()
            var disabled = false
            while (!isTestLine(line)) {
                if (line.contains(DISABLED)) {
                    disabled = true
                } else if (!line.contains(TODO)) {
                    thenContent.append(line)
                }
                if (++lineToProcess == linesOfCode.size) {
                    return lineToProcess
                }
                line = linesOfCode[lineToProcess]
            }
            while (!line.contains(FUN)) {
                if (line.contains(DISABLED)) {
                    disabled = true
                }
                if (++lineToProcess == linesOfCode.size) {
                    return lineToProcess
                }
                line = linesOfCode[lineToProcess]
            }
            val testLine = lineToProcess
            val reportItem = ReportItem()
                .setGiven(removeExtraSymbols(givenContent.toString()))
                .setThen(removeExtraSymbols(thenContent.toString()))
                .setWhen(removeExtraSymbols(whenContent.toString()))
                .setTestCaseName(removeExtraSymbols(line))
                .setDisabled(disabled || disabledTestClass)
                .setLine(testLine + 1)
            reportItems.add(reportItem)
            lineToProcess++
        }
        return ++lineToProcess
    }

    //TODO refactor
    private fun addTestCase(testCases: MutableList<String>, linesOfCode: List<String>, lineToProcess: Int): Int {
        var lineToProcess = lineToProcess
        var line: String
        while (lineToProcess < linesOfCode.size) {
            line = linesOfCode[lineToProcess]
            if (!isTestLine(line)) {
                lineToProcess++
                continue
            }
            while (!line.contains(FUN)) {
                if (++lineToProcess == linesOfCode.size) {
                    return lineToProcess
                }
                line = linesOfCode[lineToProcess]
            }
            testCases.add(removeExtraSymbols(line))
            lineToProcess++
        }
        return ++lineToProcess
    }

    private fun isTestLine(line: String) = line.contains(TEST) && !line.contains(TEST_INSTANCE)

    private fun removeExtraSymbols(line: String): String {
        return line
            .replace(EXTRA_SYMBOLS_REG_EXP.toRegex(), "")
            .replace("[{}]".toRegex(), "")
            .replace("\\*/".toRegex(), "")
            .replace("\\*".toRegex(), "")
            .replace("      ".toRegex(), " ")
            .trim { it <= ' ' }
    }

}
