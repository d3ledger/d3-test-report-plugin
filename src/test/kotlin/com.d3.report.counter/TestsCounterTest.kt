/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.counter

import com.d3.report.model.ReportItem
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestsCounterTest {

    private val testsCounter = TestsCounter()

    /**
     * @given instance of TestsCounter
     * @when countTests() is called against empty list
     * @then countTests() returns 0
     */
    @Test
    fun testCountTestsEmpty() {
        assertEquals(0, testsCounter.countTests(emptyList()))
    }

    /**
     * @given instance of TestsCounter
     * @when countTests() is called against code with two tests and one function
     * @then countTests() returns 2
     */
    @Test
    fun testCountTests() {
        val testCode = """
            @TestInstance(TestInstance.Lifecycle.PER_CLASS)
            class Test{
            
                fun function(){
                
                }
                
                @Test
                fun test1(){
                
                }
                
                @Test
                fun test2(){
                
                }
                
            }
        """.trimIndent()
        assertEquals(2, testsCounter.countTests(testCode.split("\n")))
    }

    /**
     * @given instance of TestsCounter
     * @when countTests() is called against code with no tests
     * @then countTests() returns 0
     */
    @Test
    fun testCountTestsNoTests() {
        val testCode = """
            @TestInstance(TestInstance.Lifecycle.PER_CLASS)
            class Test{
            
                fun function(){
                
                }

                fun test1(){
                
                }
                
                fun test2(){
                
                } 
            }
        """.trimIndent()
        assertEquals(0, testsCounter.countTests(testCode.split("\n")))
    }

    /**
     * @given instance of TestsCounter
     * @when countDisabledTests() is called against empty list
     * @then countDisabledTests() returns 0
     */
    @Test
    fun testCountDisabledTestsEmpty() {
        assertEquals(0, testsCounter.countDisabledTests(emptyList()))
    }

    /**
     * @given instance of TestsCounter
     * @when countDisabledTests() is called against list with 3 enabled and 2 disabled tests
     * @then countDisabledTests() returns 2
     */
    @Test
    fun testCountDisabledTests() {
        val disabled = ReportItem()
        disabled.setDisabled(true)
        val normal = ReportItem()
        normal.setDisabled(false)
        assertEquals(2, testsCounter.countDisabledTests(listOf(normal, disabled, normal, disabled, normal)))
    }

}