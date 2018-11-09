# jaxrs-autoconfig

[![Build Status](https://travis-ci.org/amutsch/jaxrs-autoconfig.svg?branch=master)](https://travis-ci.org/amutsch/jaxrs-autoconfig) 

Use annotations to setup and declare JAXRS endpoints.  Includes factory to create spring beans and auto setup the jax-rs servers.  
This alleviates the problem of manually exposing the endpoint and in complex apps exposing the endpoint under the right Jax-RS server.

The goal of the project is to include only Core Java, Spring projects necessary for configuration and scanning and JAX-RS projects.  Additional smaller projects may come along later for like CXF if there are needs for auto configuration with CXF specific items.

The implementation information should try to be left to the user to pass in.
