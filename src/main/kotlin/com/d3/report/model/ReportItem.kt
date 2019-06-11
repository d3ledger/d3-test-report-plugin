/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.model

/*
 * ReportExtension data of single test case
 */
class ReportItem {

    // Number of line of code where test case appears
    private var line = 0
    // Name of test case(function name)
    private var testCaseName = ""
    // '@given' comment section
    private var given = ""
    // '@when' comment section
    private var `when` = ""
    // '@then' comment section
    private var then = ""
    // Is test marked with @Disabled annotation
    private var disabled = false

    fun getLine() = line

    fun setLine(line: Int): ReportItem {
        this.line = line
        return this
    }

    fun getTestCaseName() = testCaseName

    fun setTestCaseName(testCaseName: String): ReportItem {
        this.testCaseName = testCaseName
        return this
    }

    fun getGiven() = given

    fun setGiven(given: String): ReportItem {
        this.given = given
        return this
    }

    fun getWhen() = `when`

    fun setWhen(`when`: String): ReportItem {
        this.`when` = `when`
        return this
    }

    fun getThen() = then

    fun setThen(then: String): ReportItem {
        this.then = then
        return this
    }

    fun isDisabled() = disabled

    fun setDisabled(disabled: Boolean): ReportItem {
        this.disabled = disabled
        return this
    }
}
