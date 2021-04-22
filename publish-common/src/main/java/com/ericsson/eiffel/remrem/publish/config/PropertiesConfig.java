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
package com.ericsson.eiffel.remrem.publish.config;

public class PropertiesConfig {
    public static final String MESSAGE_BUS_HOST = "com.ericsson.eiffel.remrem.publish.messagebus.host";
    public static final String MESSAGE_BUS_PORT = "com.ericsson.eiffel.remrem.publish.messagebus.port";
    public static final String VIRTUAL_HOST = "com.ericsson.eiffel.remrem.publish.messagebus.virtualhost";
    public static final String CHANNELS_COUNT = "com.ericsson.eiffel.remrem.publish.messagebus.channels";
    public static final String USERNAME = "com.ericsson.eiffel.remrem.publish.messagebus.username";
    public static final String PASSWORD = "com.ericsson.eiffel.remrem.publish.messagebus.password";
    public static final String TLS = "com.ericsson.eiffel.remrem.publish.messagebus.tls";
    public static final String EXCHANGE_NAME = "com.ericsson.eiffel.remrem.publish.exchange.name";
    public static final String USE_PERSISTENCE = "com.ericsson.eiffel.remrem.publish.use.persistence";
    public static final String CLI_MODE = "com.ericsson.eiffel.remrem.publish.cli.mode";
    public static final String TEST_MODE = "com.ericsson.eiffel.remrem.publish.cli.test.mode";
    public static final String DEBUG = "Debug";
    public static final String DOMAIN_ID = "com.ericsson.eiffel.remrem.publish.domain";

    public static final String INVALID_EVENT_CONTENT = "Invalid event content, client need to fix problem in event before submitting again";
    public static final String INVALID_MESSAGE = "Bad Request";
    public static final String SUCCESS = "SUCCESS";

    public static final String EVENT_ID = "eventId";
    public static final String ID = "id";
    public static final String META = "meta";
    public static final String EIFFEL_MESSAGE_VERSIONS = "eiffelMessageVersions";

    public static final String SUCCESS_MESSAGE = "Event sent successfully";
    public static final String SERVICE_UNAVAILABLE = "Service Unavailable";

    public static final String CREATE_EXCHANGE_IF_NOT_EXISTING = "com.ericsson.eiffel.remrem.publish.messagebus.createExchange";
    public static final String INVALID_EXCHANGE = "Exchange not found, Please check exchange configuration and try again";
    public static final String INVALID_EXCHANGE_MESSAGE_CLI = " Unavailable. To create the exchange specify -ce or --create_exchange to true )";
    public static final String INVALID_EXCHANGE_MESSAGE_SERVICE = " ExchangeName is not present, To create the exchange specify createExchangeIfNotExisting in application configuration";

    public static final String SERVER_DOWN = "Internal Server Error";
    public static final String SERVER_DOWN_MESSAGE = "RabbitMQ is down. Please try later";
    public static final String ROUTING_KEY_GENERATION_FAILED_CONTENT = "Could not prepare Routing key to publish message";
    public static final String UNSUCCESSFUL_EVENT_CONTENT = "Please check previous event and try again later";
    public static final String RABBITMQ_PROPERTIES_NOT_FOUND = "RabbitMQ properties not found";
    public static final String RABBITMQ_PROPERTIES_NOT_FOUND_CONTENT = "RabbitMQ properties not configured for the protocol ";
}
