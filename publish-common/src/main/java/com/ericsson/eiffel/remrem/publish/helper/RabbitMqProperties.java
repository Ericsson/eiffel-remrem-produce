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
package com.ericsson.eiffel.remrem.publish.helper;

import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;

import org.slf4j.LoggerFactory;

import com.ericsson.eiffel.remrem.publish.config.PropertiesConfig;
import com.ericsson.eiffel.remrem.publish.exception.RemRemPublishException;
import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.MessageProperties;
import com.rabbitmq.client.ShutdownListener;
import com.rabbitmq.client.ShutdownSignalException;

import ch.qos.logback.classic.Logger;

public class RabbitMqProperties {

    private RMQBeanConnectionFactory factory = new RMQBeanConnectionFactory();
    private static final Random random = new Random();
    private boolean usePersitance = true;

    private String host;
    private String exchangeName;
    private Integer port;
    private String tlsVer;
    private String virtualHost;
    private String username;
    private String password;
    private String domainId;
    private Integer channelsCount;
    private boolean createExchangeIfNotExisting;

    private Connection rabbitConnection;
    private String protocol;

    private List<Channel> rabbitChannels;

    Logger log = (Logger) LoggerFactory.getLogger(RMQHelper.class);

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getExchangeName() {
        return exchangeName;
    }

    public void setExchangeName(String exchangeName) {
        this.exchangeName = exchangeName;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getVirtualHost() { return virtualHost; }

    public void setVirtualHost(String virtualHost) { this.virtualHost = virtualHost; }

    public String getTlsVer() {
        return tlsVer;
    }

    public void setTlsVer(String tlsVer) {
        this.tlsVer = tlsVer;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String user) {
        this.username = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDomainId() {
        return domainId;
    }

    public void setDomainId(String domainId) {
        this.domainId = domainId;
    }

    public boolean isCreateExchangeIfNotExisting() {
        return createExchangeIfNotExisting;
    }

    public void setCreateExchangeIfNotExisting(boolean createExchangeIfNotExisting) {
        this.createExchangeIfNotExisting = createExchangeIfNotExisting;
    }

    public Integer getChannelsCount() {
        return channelsCount;
    }

    public void setChannelsCount(Integer channelsCount) {
        this.channelsCount = channelsCount;
    }

    public RMQBeanConnectionFactory getFactory() {
        return factory;
    }

    public void setFactory(RMQBeanConnectionFactory factory) {
        this.factory = factory;
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
    }

    public Connection getRabbitConnection() {
        return rabbitConnection;
    }

    public void setRabbitConnection(Connection rabbitConnection) {
        this.rabbitConnection = rabbitConnection;
    }

    public void init() throws RemRemPublishException {
        log.info("RabbitMqProperties init ...");
        if (Boolean.getBoolean(PropertiesConfig.CLI_MODE)) {
            initCli();
        } else {
            initService();
        }
        madatoryParametersCheck();
        try {
            factory.setHost(host);
            log.info("Host address: " + host);

            if (port != null) {
                factory.setPort(port);
                log.info("Port is: " + port);
            } else {
                log.info("Using default rabbit mq port.");
            }

            if (virtualHost != null && !virtualHost.isEmpty()) {
                factory.setVirtualHost(virtualHost);
                log.info("Virtual host is: " + virtualHost);
            } else {
                log.info("Using default virtual host");
            }

            log.info("Exchange is: " + exchangeName);

            if((username != null && !username.isEmpty()) && (password != null && !password.isEmpty())) {
                factory.setUsername(username);
                factory.setPassword(password);
            }
            
            
           

            if (tlsVer != null && !tlsVer.isEmpty()) {
                if (tlsVer.contains("default")) {
                    log.info("Using default TLS version connection to RabbitMQ.");
                    factory.useSslProtocol();
                }
                else {
                    log.info("Using TLS version " + tlsVer + " connection to RabbitMQ.");
                    factory.useSslProtocol("TLSv" + tlsVer);
                }
            }
            else{
                log.info("Using standard connection method to RabbitMQ.");
            }

        } catch (KeyManagementException e) {
            log.error(e.getMessage(), e);
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);            
        }
        checkAndCreateExchangeIfNeeded();
    }

    /**
     * This method is used to create Rabbitmq connection and channels
     */
    public void createRabbitMqConnection() {
        try {
            rabbitConnection = factory.newConnection();
            log.info("Connected to RabbitMQ.");
            rabbitChannels = new ArrayList<>();
            if(channelsCount == null || channelsCount == 0 ) {
                channelsCount = 1;
            }
            for (int i = 0; i < channelsCount; i++) {
                rabbitChannels.add(rabbitConnection.createChannel());
            }
        } catch (IOException | TimeoutException e) {
            log.error(e.getMessage(), e);
        }
    }

    private void initCli() {
        setValues();
    }

    private void initService() {
        if (host == null) {
            host = getValuesFromSystemProperties(protocol + ".rabbitmq.host");
        }

        if (port == null) {
            port = Integer.getInteger(getValuesFromSystemProperties(protocol + ".rabbitmq.port"));
        }

        if (virtualHost == null) {
            virtualHost = getValuesFromSystemProperties(protocol + ".rabbitmq.virtualHost");
        }

        if (domainId == null) {
            domainId = getValuesFromSystemProperties(protocol + ".rabbitmq.domainId");
        }

        if (tlsVer == null) {
            tlsVer = getValuesFromSystemProperties(protocol + ".rabbitmq.tls");
        }

        if (exchangeName == null) {
            exchangeName = getValuesFromSystemProperties(protocol + ".rabbitmq.exchangeName");
        }

        if (username == null) {
            username = getValuesFromSystemProperties(protocol + ".rabbitmq.username");
        }

        if (password == null) {
            password = getValuesFromSystemProperties(protocol + ".rabbitmq.password");
        }

        if (channelsCount == null ) {
            channelsCount = Integer.getInteger(getValuesFromSystemProperties(protocol + ".rabbitmq.channelsCount"));
        }
    }
    

    private void setValues() {
        host = getValuesFromSystemProperties(PropertiesConfig.MESSAGE_BUS_HOST);
        port = Integer.getInteger(PropertiesConfig.MESSAGE_BUS_PORT);
        virtualHost = getValuesFromSystemProperties(PropertiesConfig.VIRTUAL_HOST);
        domainId = getValuesFromSystemProperties(PropertiesConfig.DOMAIN_ID);
        username = getValuesFromSystemProperties(PropertiesConfig.USERNAME);
        password = decryptString(getValuesFromSystemProperties(PropertiesConfig.PASSWORD));
        channelsCount = Integer.getInteger(PropertiesConfig.CHANNELS_COUNT);
        tlsVer = getValuesFromSystemProperties(PropertiesConfig.TLS);
        exchangeName = getValuesFromSystemProperties(PropertiesConfig.EXCHANGE_NAME);
        usePersitance = Boolean.getBoolean(PropertiesConfig.USE_PERSISTENCE);
        createExchangeIfNotExisting = Boolean.parseBoolean(getValuesFromSystemProperties(PropertiesConfig.CREATE_EXCHANGE_IF_NOT_EXISTING));
    }

    private String decryptString(String password) {
        return new String(Base64.getDecoder().decode(password));
    }

    private String getValuesFromSystemProperties(String propertyName) {
        return System.getProperty(propertyName);
    }

    /**
     * This method is used to check mandatory RabbitMQ properties.
     */
    private void madatoryParametersCheck() {
        if(host == null || exchangeName == null) {
            if (Boolean.getBoolean(PropertiesConfig.CLI_MODE)) {
                System.err.println("Mandatory RabbitMq properties missing");
                System.exit(-1);
            }
        }
    }

    /**
     * This method is used to check for checking exchange availability, if
     * exchange is not available creates a new exchange based on isCreateExchangeIfNotExisting true boolean property  .
     * @throws RemRemPublishException
     * @throws TimeoutException
     * @throws IOException
     */
    public void checkAndCreateExchangeIfNeeded() throws RemRemPublishException {
        final boolean exchangeAlreadyExist = hasExchange();
        if (!exchangeAlreadyExist) {
            if (isCreateExchangeIfNotExisting()) {
                Connection connection = null;
                try {
                    connection = factory.newConnection();
                } catch (final IOException | TimeoutException e) {
                    throw new RemRemPublishException("Exception occurred while creating Rabbitmq connection ::" + factory.getHost() + ":" + factory.getPort() + e.getMessage());
                }
                Channel channel = null;
                try {
                    channel = connection.createChannel();
                } catch (final IOException e) {
                    throw new RemRemPublishException("Exception occurred while creating Channel with Rabbitmq connection ::" + factory.getHost() + ":" + factory.getPort() + e.getMessage());
                }
                try {
                    channel.exchangeDeclare(exchangeName, "topic", true);
                } catch (final IOException e) {
                    log.info(exchangeName + "failed to create an exchange");
                    throw new RemRemPublishException("Unable to create Exchange with Rabbitmq connection " + exchangeName + factory.getHost() + ":" + factory.getPort() + e.getMessage());
                } finally {
                    if (channel == null || channel.isOpen()) {
                        try {
                            channel.close();
                            connection.close();
                        } catch (IOException | TimeoutException e) {
                            log.warn("Exception occurred while closing the channel" + e.getMessage());
                        }
                    }
                }
            } else {
                if (!Boolean.getBoolean(PropertiesConfig.CLI_MODE)) {
                    throw new RemRemPublishException(exchangeName + PropertiesConfig.INVALID_EXCHANGE_MESSAGE_SERVICE);
                } else {
                    throw new RemRemPublishException("Exchange " + exchangeName + PropertiesConfig.INVALID_EXCHANGE_MESSAGE_CLI);
                }
            }
        }
    }

    /**
     * This method is used to check exchange exists or not
     * @return Boolean
     * @throws RemRemPublishException
     * @throws TimeoutException
     * @throws IOException
     */
    private boolean hasExchange() throws RemRemPublishException {
        log.info("Exchange is: " + exchangeName);
        Connection connection;
        try {
            connection = factory.newConnection();
        } catch (final IOException | TimeoutException e) {
            throw new RemRemPublishException("Exception occurred while creating Rabbitmq connection ::" + factory.getHost() + factory.getPort() + e.getMessage());
        }
        Channel channel = null;
        try {
            channel = connection.createChannel();
        } catch (final IOException e) {
            log.info("Exchange " + exchangeName + " does not Exist");
            throw new RemRemPublishException("Exception occurred while creating Channel with Rabbitmq connection ::" + factory.getHost() + factory.getPort() + e.getMessage());
        }
        try {
            channel.exchangeDeclarePassive(exchangeName);
            return true;
        } catch (final IOException e) {
            log.info("Exchange " + exchangeName + " does not Exist");
            return false;
        } finally {
            if (channel != null && channel.isOpen()) {
                try {
                    channel.close();
                    connection.close();
                } catch (IOException | TimeoutException e) {
                    log.warn("Exception occurred while closing the channel" + e.getMessage());
                }
            }
        }
    }

    /**
     * This method is used to publish the message to RabbitMQ
     * @param routingKey
     * @param msg is Eiffel Event
     * @throws IOException
     */
    public void send(String routingKey, String msg) throws IOException {

        Channel channel = giveMeRandomChannel();
        channel.addShutdownListener(new ShutdownListener() {
            public void shutdownCompleted(ShutdownSignalException cause) {
                // Beware that proper synchronization is needed here
                if (cause.isInitiatedByApplication()) {
                    log.debug("Shutdown is initiated by application. Ignoring it.");
                } else {
                    log.error("Shutdown is NOT initiated by application.");
                    log.error(cause.getMessage());
                    boolean cliMode = Boolean.getBoolean(PropertiesConfig.CLI_MODE);
                    if (cliMode) {
                        System.exit(-3);
                    }
                }
            }
        });

        BasicProperties msgProps = MessageProperties.BASIC;
        if (usePersitance)
            msgProps = MessageProperties.PERSISTENT_BASIC;

        channel.basicPublish(exchangeName, routingKey, msgProps, msg.getBytes());
        log.info("Published message with size {} bytes on exchange '{}' with routing key '{}'", msg.getBytes().length,
                exchangeName, routingKey);
    }

    /**
     * This method is used to give random channel
     * @return channel
     */
    private Channel giveMeRandomChannel() {
        if ((rabbitConnection == null || !rabbitConnection.isOpen())) {
            createRabbitMqConnection();
        }
        return rabbitChannels.get(random.nextInt(rabbitChannels.size()));
    }

}
