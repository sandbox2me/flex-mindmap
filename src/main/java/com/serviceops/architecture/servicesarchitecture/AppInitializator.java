package com.serviceops.architecture.servicesarchitecture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
@Component
public class AppInitializator {
    private static final Logger log = LoggerFactory.getLogger(AppInitializator.class);

    @Autowired
    ServiceI service;

    @PostConstruct
    private void init() {
        service.load();
    }
}
