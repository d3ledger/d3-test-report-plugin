/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.service

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TestReportServiceTest {
    private val userDir = System.getProperty("user.dir")!!
    private val testReportService = TestReportService()

    /**
     * @given instance of TestReportService
     * @when create() is called against the current test folder
     * @then create() returns valid test report
     */
    @Test
    fun testCreateOwnTests() {
        val report = testReportService.create(listOf("$userDir/src/test/kotlin/com/d3/report/service"))
        assertEquals(1, report.noDescriptionTestCases.size)
        assertEquals("testNoDescriptionTest()", report.noDescriptionTestCases[0])
        assertEquals(1, report.testFiles.size)
        assertTrue(report.testFiles[0].endsWith("TestReportServiceTest.kt"))
        assertTrue(report.hasLacksOfDescription())
    }

    @Test
    fun testNoDescriptionTest() {
        /*
        This test doesn't have description nor body on purpose
         */
    }
}