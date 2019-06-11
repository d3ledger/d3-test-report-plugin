/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.filter

import com.d3.report.model.ReportItem

/**
 * Class for filtering tests and test cases
 */
class TestsFilter {

    /**
     * Collects test cases with no description
     * @param testCases   list full of test cases.
     * @param reportItems test cases with description
     * @return test cases with no description
     */
    fun getNoDescriptionTests(testCases: List<String>, reportItems: List<ReportItem>) =
        testCases.filter { testCase -> !testCaseIsWellReported(testCase, reportItems) }

    private fun testCaseIsWellReported(testCase: String, reportItems: List<ReportItem>) =
        reportItems.any { reportItem -> reportItem.getTestCaseName() == testCase }

}
