package com.ericsson.eiffel.remrem.publish.config;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.stereotype.Component;

import com.ericsson.eiffel.remrem.publish.helper.RabbitMqProperties;

@Component
public class RabbitMqPropertiesConfig {

    @Autowired
    Environment env;

    private Map<String, RabbitMqProperties> rabbitMqPropertiesMap = new HashMap<String, RabbitMqProperties>();

    /***
     * This method is used to give RabbitMq properties based on protocol
     * @return protocol specific RabbitMq properties in map
     */
    public Map<String, RabbitMqProperties> getRabbitMqProperties() {
        Map<String, Object> map = new HashMap<String, Object>();
        for(Iterator it = ((AbstractEnvironment) env).getPropertySources().iterator(); it.hasNext(); ) {
            PropertySource propertySource = (PropertySource) it.next();
            if (propertySource instanceof MapPropertySource) {
                if(propertySource.getName().equalsIgnoreCase("URL [file:C:/Users/846964/Eiffel_SW/apache-tomcat-7.0.50-windows-x64/apache-tomcat-7.0.50/conf/config.properties]")) {
                    map.putAll(((MapPropertySource) propertySource).getSource());
                }
            }
        }
        for (Entry<String, Object> entry : map.entrySet())
        {
            String key = entry.getKey();
            if (key.contains("rabbitmq")) {
                String protocol = key.split("\\.")[0];
                if (rabbitMqPropertiesMap.get(protocol) == null) {
                    rabbitMqPropertiesMap.put(protocol, new RabbitMqProperties());
                }
                if (key.contains("rabbitmq.host")) {
                    rabbitMqPropertiesMap.get(protocol).setHost(entry.getValue().toString());
                } else if (key.contains("rabbitmq.port")) {
                    rabbitMqPropertiesMap.get(protocol).setPort(Integer.getInteger(entry.getValue().toString()));
                } else if (key.contains("rabbitmq.username")) {
                    rabbitMqPropertiesMap.get(protocol).setUsername(entry.getValue().toString());
                } else if (key.contains("rabbitmq.password")) {
                    rabbitMqPropertiesMap.get(protocol).setPassword(entry.getValue().toString());
                } else if (key.contains("rabbitmq.tls")) {
                    rabbitMqPropertiesMap.get(protocol).setTlsVer(entry.getValue().toString());
                } else if (key.contains("rabbitmq.exchangeName")) {
                    rabbitMqPropertiesMap.get(protocol).setExchangeName(entry.getValue().toString());
                } else if (key.contains("rabbitmq.domainId")) {
                    rabbitMqPropertiesMap.get(protocol).setDomainId(entry.getValue().toString());
                }
            }
        }
        return rabbitMqPropertiesMap;
    }
}
