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

import java.util.ArrayList;
import java.util.Map;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.apache.commons.cli.Options;
import org.apache.commons.lang3.ArrayUtils;

import com.ericsson.eiffel.remrem.publish.config.PropertiesConfig;
import com.ericsson.eiffel.remrem.shared.VersionService;

public class CliOptions {

    static private Options options=null;
    static private CommandLine commandLine;

    //Used for testing purposes
    private static ArrayList<Integer> testErrorCodes = new ArrayList<>();

    public static ArrayList getErrorCodes() {
    return testErrorCodes;
    }

    public static void addErrorCode(int errorCode) {
        testErrorCodes.add(errorCode);
    }

    public static void cleanErrorCodes() {
        testErrorCodes.clear();
    }

    private static OptionGroup contentGroup = null;

    public static CommandLine getCommandLine() {
        return commandLine;
    }

    public static Options createHelpOptions() {
        Options hOptions = new Options();
        hOptions.addOption(createHelpOption());
        return hOptions;
    }

    /**
     * Creates the options needed by command line interface
     * @return the options this CLI can handle
     */
    public static void createCLIOptions() {
        options = new Options();
        options.addOption(createHelpOption());
        options.addOption("d", "debug", false, "enable debug traces");
        options.addOption("mb", "message_bus", true, "host of message bus to use");
        options.addOption("en", "exchange_name", true, "exchange name");
        options.addOption("ce", "create_exchange", true, "option to denote if we need to create an exchange eg: -ce true or --create_exchange true");
        options.addOption("np", "non_persistent", false, "remove persistence from message sending");
        options.addOption("port", "port", true, "port to connect to message bus, default is 5672");
        options.addOption("vh", "virtual_host", true, "virtual host to connect to (optional)");
        options.addOption("tls", "tls", true, "tls version, specify a valid tls version: '1.2' or 'default'. It is required for RabbitMq secured port.");
        options.addOption("mp", "messaging_protocol", true, "name of messaging protocol to be used, e.g. eiffel3, eiffelsemantics, default is eiffelsemantics");
        options.addOption("domain", "domainId", true, "identifies the domain that produces the event");
        options.addOption("cc", "channelsCount", true, "Number of channels connected to message bus, default is 1");
        options.addOption("ud", "user_domain_suffix", true, "user domain suffix");
        options.addOption("v", "lists the versions of publish and all loaded protocols");
        options.addOption("tag", "tag", true, "tag to be used in routing key");
        options.addOption("un", "username", true, "username to connect to message bus");
        options.addOption("pwd", "password", true, "password to connect to message bus");
        options.addOption("rk", "routing_key", true, "routing key of the eiffel message. When provided routing key is not generated and the value provided is used.");

        contentGroup = createContentGroup();
        options.addOptionGroup(contentGroup);
    }

    private static Option createJsonOption() {
        return new Option("json", "json_content", true, "event content in json string. The value can also be a dash(-) and the json will be read from the output of other programs if piped.");
    }

    private static Option createFileOption() {
        return new Option("f", "content_file", true, "event content file");
    }

    private static Option createHelpOption() {
        return new Option("h", "help", false, "show help");
    }

    private static OptionGroup createContentGroup() {
        OptionGroup group = new OptionGroup();
        group.addOption(createFileOption());
        group.addOption(createJsonOption());
        return group;
    }

    /**
     * Parse the given arguments and act on them
     * @param args command line arguments
     * @return if the service should start or not
     */
    public static void parse(String[] args) {
            createCLIOptions();
            CommandLineParser parser = new DefaultParser();
            try {
                commandLine = parser.parse(options, args);
                afterParseChecks();
                handleMessageBusOptions();
                handleDebugOptions();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                help(CLIExitCodes.CLI_MISSING_OPTION_EXCEPTION);
            }
    }

    public static void afterParseChecks() throws MissingOptionException {
        if (commandLine.hasOption("h")) {
            System.out.println("You passed help flag.");
            help(0);
        } else if (commandLine.hasOption("v")) {
            printVersions();
        }else {
            checkRequiredOptions();
        }
    }

    public static void checkRequiredOptions() throws MissingOptionException {
        OptionGroup[] groups = {contentGroup};
        for(OptionGroup group : groups) {
            ArrayList<Option> groupOptions = new ArrayList<Option>(group.getOptions());
            boolean groupIsGiven = false;
            for (Option option : groupOptions){
                if (commandLine.hasOption(option.getOpt())) {
                    groupIsGiven = true;
                    break;
                }
            }
            if (!groupIsGiven){
                throw new MissingOptionException(groupOptions);
            }
        }
    }

    /**
     * Checks if any options that CLI can handle have been passed
     * @return true if any valid options have been 
     *             passed as arguments otherwise false
     */
    public static boolean hasParsedOptions() {
        if (commandLine == null)
            return false;

        Option[] existingOptions = commandLine.getOptions(); 
        return existingOptions.length > 0;
    }

    /**
     * Prints the help for this application and exits.
     * @param options the options to print usage help for
     */
    public static void help(int errorCode) {
        CliOptions.clearSystemProperties();
        // This prints out some help
        HelpFormatter formater = new HelpFormatter();
        formater.printHelp("java -jar", options);
        exit(errorCode);
    }

    /**
     * Sets the system properties with values passed for 
     * message bus host and exchange name
     * @param commandLine command line arguments
     */
    public static void handleMessageBusOptions() throws HandleMessageBusException {
        if (commandLine.hasOption("mb")) {
            String messageBusHost = commandLine.getOptionValue("mb");
            String key = PropertiesConfig.MESSAGE_BUS_HOST;
            System.setProperty(key, messageBusHost);
        }

        if (commandLine.hasOption("en")) {
            String exchangeName = commandLine.getOptionValue("en");
            String key = PropertiesConfig.EXCHANGE_NAME;
            System.setProperty(key, exchangeName);
        }

        if (commandLine.hasOption("port")) {
            String port = commandLine.getOptionValue("port");
            String key = PropertiesConfig.MESSAGE_BUS_PORT;
            System.setProperty(key, port);
        }

        if (commandLine.hasOption("vh")) {
            String virtualHost = commandLine.getOptionValue("vh");
            System.setProperty(PropertiesConfig.VIRTUAL_HOST, virtualHost);
        }

        if (commandLine.hasOption("ce")) {
            String createExchange = commandLine.getOptionValue("ce");
            String key = PropertiesConfig.CREATE_EXCHANGE_IF_NOT_EXISTING;
            System.setProperty(key, createExchange);
        }

        if (commandLine.hasOption("domain")) {
            String domain = commandLine.getOptionValue("domain");
            String key = PropertiesConfig.DOMAIN_ID;
            System.setProperty(key, domain);
        }
        if (commandLine.hasOption("channelsCount")) {
            String channelsCount = commandLine.getOptionValue("channelsCount");
            String key = PropertiesConfig.CHANNELS_COUNT;
            System.setProperty(key, channelsCount);
        }
 
        if (commandLine.hasOption("username")) {
            String usernm = commandLine.getOptionValue("username");
            String key = PropertiesConfig.USERNAME;
            System.setProperty(key, usernm);
        }

        if (commandLine.hasOption("password")) {
            String passwd = commandLine.getOptionValue("password");
            String key = PropertiesConfig.PASSWORD;
            System.setProperty(key, passwd);
        }

        if (commandLine.hasOption("tls")) {
            String tls_ver = commandLine.getOptionValue("tls");
            if (tls_ver == null) {
                tls_ver = "NULL";
            }
            String[] validTlsVersions = new String[]{"1.2", "default"};
            if (!ArrayUtils.contains(validTlsVersions, tls_ver)) {
                throw new HandleMessageBusException("Specified TLS version is not valid! Specify a valid TLS version!");
            }
            String key = PropertiesConfig.TLS;
            System.setProperty(key, tls_ver);
        }

        String usePersistance = "true";
        if (commandLine.hasOption("np")) {
            usePersistance = "false";
        }
        String key = PropertiesConfig.USE_PERSISTENCE;
        System.setProperty(key, usePersistance);
        key = PropertiesConfig.CLI_MODE;
        System.setProperty(key, "true");
    }
    
    /**
     * Sets the system property with "false" value if debug option is not present"
     * @param commandLine command line arguments
     */
    public static void handleDebugOptions()
    {
        String key = PropertiesConfig.DEBUG;
        if (!commandLine.hasOption("d"))
            System.setProperty(key, "false");
    }

    /**
     * Remove the system properties added by this application 
     */
    public static void clearSystemProperties() {
        System.clearProperty(PropertiesConfig.MESSAGE_BUS_HOST);
        System.clearProperty(PropertiesConfig.EXCHANGE_NAME);
        System.clearProperty(PropertiesConfig.USE_PERSISTENCE);
        System.clearProperty(PropertiesConfig.CLI_MODE);
        System.clearProperty(PropertiesConfig.MESSAGE_BUS_PORT);
        System.clearProperty(PropertiesConfig.VIRTUAL_HOST);
        System.clearProperty(PropertiesConfig.TLS);
        System.clearProperty(PropertiesConfig.DOMAIN_ID);
        System.clearProperty(PropertiesConfig.CHANNELS_COUNT);
        System.clearProperty(PropertiesConfig.USERNAME);
        System.clearProperty(PropertiesConfig.PASSWORD);
    }

    /**
     * Wrapper to call system exit making class easier to test.
     * @param errorCode
     */
    public static void exit(int errorCode) {
      boolean testMode = Boolean.getBoolean(PropertiesConfig.TEST_MODE);
      if (testMode)
        addErrorCode(errorCode);
      else
        System.exit(errorCode);
    }

    /**
     * Lists the versions of publish and all loaded protocols  
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private static void printVersions() {
        Map versions = new VersionService().getMessagingVersions();
        Map<String, String> endpointVersions = (Map<String, String>) versions.get("endpointVersions");
        Map<String, String> serviceVersion = (Map<String, String>) versions.get("serviceVersion");

        if(serviceVersion != null) {
            System.out.print("REMReM Publish version ");
            for (String version: serviceVersion.values()) {
                System.out.println(version);
            }
        }
        if(endpointVersions != null) {
            System.out.println("Available endpoints");
            for (Map.Entry<String, String> entry : endpointVersions.entrySet()) {
                System.out.println(entry);
            }
        }
        exit(0);
    }
}
