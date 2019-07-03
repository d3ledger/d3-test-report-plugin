/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.model

/*
 * Main report class
 */
class Report {
    // Brief information: total amount of test cases, disabled tests and etc
    var summary: Summary = Summary()
    // Test files
    var testFiles: MutableList<String> = ArrayList()
    // Test report data with more detailed information divided by test files
    var reportData: MutableList<TestFileReport> = ArrayList()
    // Test cases with no description
    var noDescriptionTestCases: MutableList<String> = ArrayList()

    fun hasLacksOfDescription() = !noDescriptionTestCases.isEmpty()
}

