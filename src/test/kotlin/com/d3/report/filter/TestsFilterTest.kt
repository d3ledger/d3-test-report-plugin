/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.filter

import com.d3.report.model.ReportItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestsFilterTest {

    private val testsFilter = TestsFilter()

    /**
     * @given instance of TestsFilter
     * @when getNoDescriptionTests() is called against empty lists
     * @then getNoDescriptionTests() returns empty list
     */
    @Test
    fun testGetNoDescriptionTestsEmpty() {
        assertTrue(testsFilter.getNoDescriptionTests(emptyList(), emptyList()).isEmpty())
    }

    /**
     * @given instance of TestsFilter
     * @when getNoDescriptionTests() is called against list of 3 cases and list of 2 reported cases
     * @then getNoDescriptionTests() returns list with one test case that has no description
     */
    @Test
    fun testGetNoDescriptionTests() {
        val testCases = listOf("Test case 1", "Test case 2", "No description test case")
        val reportItemWithDescription1 = ReportItem()
        reportItemWithDescription1.setTestCaseName("Test case 1")
        val reportItemWithDescription2 = ReportItem()
        reportItemWithDescription2.setTestCaseName("Test case 2")
        val noDescriptionTestCases = testsFilter.getNoDescriptionTests(
            testCases,
            listOf(reportItemWithDescription1, reportItemWithDescription2)
        )
        assertEquals(1, noDescriptionTestCases.size)
        assertEquals("No description test case", noDescriptionTestCases[0])
    }
}