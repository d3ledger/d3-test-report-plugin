/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.model

/*
 * Brief report information
 */ class Summary {
    //Title of report
    var title: String = ""
    // Date of report generation
    var generationDate: String = ""
    // Total amount of test files
    var filesWithTests: Int = 0
    // Total amount of test cases
    var testCases: Int = 0
    // Total amount of test cases with no properly formed description
    var lacksInDescription: Int = 0
    // Total amount of disabled test cases
    var disabledCases: Int = 0

}
