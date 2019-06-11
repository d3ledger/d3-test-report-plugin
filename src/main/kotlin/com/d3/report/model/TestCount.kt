/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.model

class TestCount {

    private var testCases: Int = 0
    private var disabledTestCases: Int = 0

    fun getTestCases() = testCases

    fun setTestCases(testCases: Int): TestCount {
        this.testCases = testCases
        return this
    }

    fun getDisabledTestCases() = disabledTestCases

    fun setDisabledTestCases(disabledTestCases: Int): TestCount {
        this.disabledTestCases = disabledTestCases
        return this
    }

    companion object {

        fun noTests(): TestCount {
            return TestCount()
        }
    }
}
