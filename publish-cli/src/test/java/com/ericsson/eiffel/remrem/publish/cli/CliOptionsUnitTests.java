/*
    Copyright 2018 Ericsson AB.
    For a full list of individual contributors, please see the commit history.
    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0
    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
*/
package com.ericsson.eiffel.remrem.publish.cli;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ericsson.eiffel.remrem.publish.config.PropertiesConfig;
import com.ericsson.eiffel.remrem.publish.cli.CliOptions;;

public class CliOptionsUnitTests {
    private PrintStream console;
    private ByteArrayOutputStream bytes;

    @Before public void setUp() throws Exception {
        String key = PropertiesConfig.TEST_MODE;
        System.setProperty(key, "true");
        //Switch std out to another stream
        bytes   = new ByteArrayOutputStream();
        console = System.out;
        System.setOut(new PrintStream(bytes));
    }

    @After
    public void tearDown() {
        System.clearProperty(PropertiesConfig.TEST_MODE);
        System.setOut(console);
        // reset error code since it is static
        CliOptions.cleanErrorCodes();
    }

    @Test
    public void testParseEmptyCLIOptionsFails() throws Exception {
        String[] args = new String[0];

        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    @Test
    public void testHelpOptionOnlyWorks() throws Exception {
        String[] args = {"-h"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().contains(0));
        assertTrue(CliOptions.getErrorCodes().size() == 1);
    }

    @Test
    public void testTlsOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-tls", "1.2"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testTlsOptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-tlsa", "1.2"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    @Test
    public void testTlsVer13OptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-tls", "1.3"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    @Test
    public void testMbOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-mb", "MbInstance1"};
        CliOptions.parse(args);
        System.out.println(CliOptions.getErrorCodes());
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testCreateExchangeOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-ce", "false"};
        CliOptions.parse(args);
        System.out.println(CliOptions.getErrorCodes());
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testMbOptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-mbe", "MbInstance1"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    public void testEnOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-en", "exchange_name1"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testEnOptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-enf", "exchange_name1"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    public void testNpOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-np", "non_persistent"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testNpOptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-npf", "non_persistent"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    public void testPortOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-port", "portA"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testPortOptionFails() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-portf", "portB"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }

    public void testFOption() throws Exception {
        String[] args = {"-f", "/a/b/c/test.file",  "test", "-port", "portA"};
        CliOptions.parse(args);
        assertTrue(CliOptions.getErrorCodes().isEmpty());
    }

    @Test
    public void testFOptionFails() throws Exception {
        String[] args = {"-fab", "/a/b/c/test.file",  "test", "-portf", "portB"};
        CliOptions.parse(args);
        int code = CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION;
        assertTrue(CliOptions.getErrorCodes().contains(code));
    }
}
