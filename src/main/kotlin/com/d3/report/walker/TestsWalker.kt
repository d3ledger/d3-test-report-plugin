/*
 * Copyright D3 Ledger, Inc. All Rights Reserved.
 * SPDX-License-Identifier: Apache-2.0
 */

package com.d3.report.walker

import java.io.File

private const val TEST_FILE_EXT = ".kt"
private const val TEST_POSTFIX = "test"

/*
 * This class is used to collect test files in a test folder
 */
class TestsWalker {

    /**
     * Returns all test files a given folder
     *
     * @param rootFolder root folder where all the tests are stored
     * @return test files
     */
    fun getTestFiles(rootFolder: String) = File(rootFolder).walkTopDown()
        .filter { file ->
            file.absolutePath.endsWith(TEST_FILE_EXT) &&
                    file.name.toLowerCase().contains(TEST_POSTFIX)
        }
        .map { file -> file.absolutePath }.toList()
}
