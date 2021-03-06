# jaxrs-autoconfig

[![Build Status](https://travis-ci.org/amutsch/jaxrs-autoconfig.svg?branch=master)](https://travis-ci.org/amutsch/jaxrs-autoconfig) [![codecov](https://codecov.io/gh/amutsch/jaxrs-autoconfig/branch/master/graph/badge.svg)](https://codecov.io/gh/amutsch/jaxrs-autoconfig)[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/amutsch/jaxrs-autoconfig/blob/master/LICENSE)

Use annotations to setup and declare JAXRS endpoints.  Includes factory to create spring beans and auto setup the jax-rs servers.  
This alleviates the problem of manually exposing the endpoint and in complex apps exposing the endpoint under the right Jax-RS server.

The goal of the project is to include only Core Java, Spring projects necessary for configuration and scanning of endpoint annotation.  Factory classes will be included for some of the major implementations.
