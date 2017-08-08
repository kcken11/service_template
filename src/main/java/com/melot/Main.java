/**
 * This document and its contents are protected by copyright 2012 and owned by Melot Inc.
 * The copying and reproduction of this document and/or its content (whether wholly or partly) or any
 * incorporation of the same into any other material in any media or format of any kind is strictly prohibited.
 * All rights are reserved.
 *
 * Copyright (c) Melot Inc. 2015
 */
package com.melot;

public class Main {
    
    /**
     * @param args
     */
    public static void main(String[] args) {
        com.melot.module.kkrpc.starter.Main.startServer(new String[] { "classpath*:conf/spring-bean-container-disconf.xml","classpath*:conf/spring-bean-container-services.xml","classpath*:conf/spring-bean-container-datasource.xml","classpath*:conf/server-spring-config.xml","classpath*:conf/spring-bean-container-task.xml","classpath*:conf/spring-bean-container-cache.xml"}); 
    }
}
