/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.apiman.servers.gateway_es;

import io.apiman.gateway.platforms.war.micro.GatewayMicroService;
import io.apiman.gateway.platforms.war.micro.Users;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

/**
 * Start up a micro gateway going against an external ES cluster.
 *
 * @author eric.wittmann@redhat.com
 */
public class Starter {
    /**
     * Main entry point for the API Gateway micro service.
     * @param args the arguments
     * @throws Exception when any unhandled exception occurs
     */
    public static final void main(String [] args) throws Exception {
        System.setProperty(Users.USERS_FILE_PROP, "src/main/resources/users.list"); //$NON-NLS-1$

        GatewayMicroService microService = new GatewayMicroService();
        loadProperties();
        microService.start();
        microService.join();
    }

    /**
     * Loads properties from a file and puts them into system properties.
     */
    @SuppressWarnings({ "nls", "unchecked" })
    protected static void loadProperties() {
        File f = new File("src/main/resources/gateway_es-apiman.properties");
        if (!f.exists()) {
            throw new RuntimeException("Failed to find properties file (see README.md): " + f);
        }
        InputStream is = null;
        try {
            is = new FileInputStream(f);
            Properties props = new Properties();
            props.load(is);
            Enumeration<String> names = (Enumeration<String>) props.propertyNames();
            while (names.hasMoreElements()) {
                String name = names.nextElement();
                String value = props.getProperty(name);
                System.setProperty(name, value);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            IOUtils.closeQuietly(is);
        }
    }

}
