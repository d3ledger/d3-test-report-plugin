/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.counter

import com.d3.report.model.ReportItem

private const val TEST_ANNOTATION = "@Test"
private const val TEST_INSTANCE_ANNOTATION = "@TestInstance"

/*
 * Class that takes care about counting things.
 */
class TestsCounter {

    /**
     * Counts test cases
     *
     * @param linesOfCode lines of code to count test cases
     * @return amount of test cases in code
     */
    fun countTests(linesOfCode: List<String>) =
        linesOfCode.count { line -> line.contains(TEST_ANNOTATION) && !line.contains(TEST_INSTANCE_ANNOTATION) }

    /**
     * Counts disabled test cases
     *
     * @param reportItems report items to count disabled test cases
     * @return amount of disabled test cases in report items
     */
    fun countDisabledTests(reportItems: List<ReportItem>) =
        reportItems.count { reportItem -> reportItem.isDisabled() }

}
