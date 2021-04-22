## 2.0.20
- Implemented code changes to handle the error ConcurrentModificationException which occured when too many
  requests are sent to /generateAndPublish endpoint.
- Uplifted eiffel-remrem-parent version from 2.0.4 to 2.0.5
- Uplifted eiffel-remrem-shared version from 2.0.4 to 2.0.5
- Added two external parameters to send username and password to connect to Messagebus.
- Removed TLS Versions 1 and 1.1 as they are deprecated

## 2.0.19
- Added the lenientValidation parameter(okToLeaveOutInvalidOptionalFields) for /generateAndPublish 
  end point.

## 2.0.18
- Implemented the functionality to read the Jasypt encryption key from jasypt.key file.

## 2.0.17
- REMReM lookups controlled 'failIfNoneFound' and 'failIfMultipleFound' lookups per lookup
  object within an event instead of globally per call.
- Removed Ericsson specific configuration from github.

## 2.0.16
- Uplifted eiffel-remrem-semantics version from 2.0.12 to 2.0.13.

## 2.0.15
- Added the lookupInExternalERs and lookupLimit parameters to ER lookup.

## 2.0.14
- Uplifted eiffel-remrem-semantics version from 2.0.11 to 2.0.12.

## 2.0.13
- Added new property channelCount in application.properties to customize the rabbitmq channels.
- Added documentation for the new property.
- Uplifted eiffel-remrem-parent version from 2.0.2 to 2.0.4.
- Uplifted eiffel-remrem-shared version from 2.0.2 to 2.0.4.
- Uplifted eiffel-remrem-semantics version from 2.0.9 to 2.0.11.

## 2.0.12
- Added documentation for REMReM publish in master branch.
- Uplifted the eiffel-remrem-semantics from 2.0.8 to 2.0.9.

## 2.0.11
- Uplifted eiffel-remrem-semantics version from 2.0.7 to 2.0.8.

## 2.0.10
- Uplifted eiffel-remrem-semantics version from 2.0.6 to 2.0.7.

## 2.0.9
- Implemented code changes to handle error ResponseBody in /generateAndPublish endpoint.

## 2.0.8
- Code to handle the lookup towards ER when generating events using REMReM.

## 2.0.7
- Remove code changes to load config.properties from Tomcat in REMReM-Publish.

## 2.0.6
- Uplifted eiffel-remrem-parent version from 2.0.1 to 2.0.2.
- Uplifted eiffel-remrem-shared version from 2.0.1 to 2.0.2.
- Uplifted eiffel-remrem-semantics version from 2.0.5 to 2.0.6.

## 2.0.5
- Fix manifest main class issue in publish cli. 

## 2.0.4
- Fix Spring and Java properties issues.

## 2.0.3
- Fixed /generateAndPublish Endpoint when the application is started using application.properties.

## 2.0.2
- Uplifted eiffel-remrem-parent version from 2.0.0 to 2.0.1.
- Uplifted eiffel-remrem-shared version from 2.0.0 to 2.0.1.
- Uplifted eiffel-remrem-semantics version from 2.0.4 to 2.0.5.
- Fixed generateAndPublish endpoint to load generate server properties from config.properties file.
- Added functionality to load Generate server properties from JAVA_OPTS.

## 2.0.1
- Fix for Invalid exchange return success. Added a createExchangeIfNotExisting property to create an exchange.
- For CLI Added a option create_exchange or ce to create Exchange.
- Upgraded eiffel-remrem-semantics version from 2.0.3 to 2.0.4.

## 2.0.0
- Upgraded eiffel-remrem-semantics version from 1.0.1 to 2.0.3
- Modified test cases as per agen version and tested with proper data

## 1.0.3
- Fixed broken application properties while encrypting/decrypting open text properties.

## 1.0.2
- Fixed broken expected_parsed json files for some events.

## 1.0.1
- Changed link from http://ericsson.github.io to https://eiffel-community.github.io.
- Upgraded eiffel-remrem-semantics version from 1.0.0 to 1.0.1.

## 1.0.0
- Upgraded eiffel-remrem-parent version from 0.0.8 to 1.0.0.
- Upgraded eiffel-remrem-shared version from 0.4.2 to 1.0.0.
- Upgraded eiffel-remrem-semantics version from 0.5.3 to 1.0.0.

## 0.6.10
- Fixed the publish-Cli logger and lang3 jar issues and property loading errors.  

## 0.6.9
- Upgraded eiffel-remrem-parent version from 0.0.7 to 0.0.8.
- Upgraded eiffel-remrem-shared version from 0.4.1 to 0.4.2.
- Upgraded eiffel-remrem-semantics version from 0.5.2 to 0.5.3.

## 0.6.8
- Adaptation of Spring application to execute without external Tomcat installation.

## 0.6.7
- Upgraded eiffel-remrem-parent version from 0.0.6 to 0.0.7.
- Upgraded eiffel-remrem-shared version from 0.4.0 to 0.4.1.
- Upgraded eiffel-remrem-semantics version from 0.5.1 to 0.5.2.

## 0.6.6
- Updated parent, shared and semantics version
- Updated amqp-client version from 5.0.0 to 5.4.0

## 0.6.5
- Updated parent, shared and semantics version

## 0.6.4
- Updated shared and semantics version

## 0.6.3
- Updated parent version

## 0.6.2
- migrated from gradle to maven

## 0.6.1
- Updated versions of few dependencies

## 0.6.0
- Removed based64 encryption mechanism for ldap manager password

## 0.5.9
- Added jasypt-spring-boot-starter dependency to support open text encryption

## 0.5.8
- EventParser functionality added to endpoint /generateAndPublish (@param parseData added) 

## 0.5.7
- Changed way of passing REMReM Generate Service uri and path from configs

## 0.5.6
- Removed Protocol Interface dependency
- Uplifted semantics version to 0.4.1

## 0.5.5
- Fixed generate service link in /generateAndPublish endpoint
- Added few messages in case error in generate happen
- Uplifted semantics version to 0.4.0

## 0.5.4
- Added swagger for publish service
- Changed year in copyright headers from 2017 to 2018

## 0.5.3
- added logback support to remrem publish

## 0.5.2
- Added a common REST API for Generate and Publish
- Moved ldap related functionality from shared
- Added functionality to disable the Remrem Publish Authentication.
- Corrected the name of endpoint "version" back to "versions"
- Updated github pages for common REST api for generate and publish
- Uplifted remrem-semantics version to 0.3.7

## 0.5.1
- This version is the same as 0.4.9. The intended changes were missed to be merged.
- Updated remrem-generator version to 0.5.1

## 0.5.0
- This version is the same as 0.4.9. The intended changes were missed to be merged.
- Uplifted remrem-semantics version to the latest version 0.3.2 to support continuous operation events

## 0.4.9
- Modified rk and tag to query parameters in Service.

## 0.4.8
- Uplifted remrem-semantics to 0.3.1 to override given input meta.source.serializer GAV information

## 0.4.7
- Added rk(Routing key) and tag option in CLI.
- Added routingKey and tag parameter in Service.
- Moved Routing key generation to corresponding protocol.

## 0.4.6
- Uplifted remrem-semantics version to 0.2.9

## 0.4.5
- Uplifted remrem-semantics version to 0.2.8.

## 0.4.4
- Uplifted remrem-semantics version to 0.2.7.

## 0.4.3
- Removed spring configurations from config.properties file and handled through code.

## 0.4.2
- Changed REMReM publish to read required properties from CLI options for CLI and from JAVA_OPTS, tomcat/conf/config.properties for service.

## 0.4.1
- Implemented functionality to get properties from java opts for publish-service

## 0.4.0
- Implemented one MB for each protocol in REMREM publish.

## 0.3.9
- Uplifted remrem-semantics version to 0.2.6 to support links validation.

## 0.3.8
- Added copyright headers to the source code.
- Uplifted remrem-shared version to 0.3.3 to get the versions of publish and all loaded protocols.

## 0.3.7
- Updated remrem-shared version to 0.3.2 to support base64 encryption functionality for Ldap manager password.

## 0.3.6
- Added validation for 503 status code to check routing key as null.

## 0.3.5
- Moved ldap related functionality to shared

## 0.3.4
- Updated Version for dependency Commons-Lang in REMREM publish

## 0.3.3
- Implemented RoutingKey functionality for REMREM Semantics.

## 0.3.2
- Added comments and removed unnecessary dependencies in build.gradle.

## 0.3.1
- Added changes in build.gradle.

## 0.3.0
- Split REMREM Publish component into cli, service and common.

## 0.2.9
- Updated the RemRem shared version to latest.

## 0.2.8
- Implemented RabbitMQ connection retry in Service.

## 0.2.7
- upgraded semantics version in build.gradle to support all
  EiffelEvents in the eiffel repo from topic-drop4 branch 

## 0.2.6
- Added status codes for output generate from publish.

## 0.2.5
- Added domain option in CLI

## 0.2.4
- Removed unused libraries and updated a few library versions.

## 0.2.3
- Implemented array of Json objects as input and output in Json format in CLI
- Added Debug flag

## 0.2.2
- Added eventId to output format in REMREM publish
- Implemented get route key from messaging library.

## 0.2.1
- Fix authentication required even when activedirectory.enabled=false

## 0.2.0
- Added SSL support for the RabbitMq connection.
- Improved Cli
- Added unit tests.

## 0.1.9
- added optional authentication to an Active Directory server for all 
  REST endpoints


## 0.1.8
- Improved error handling/logging
