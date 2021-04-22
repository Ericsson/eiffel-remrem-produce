# REMReM Publish CLI

## CLI

For using Eiffel REMReM Publish CLI self executing **publish-cli.jar** file should be executed in command line with parameters described below.

```
$ java -jar publish-cli.jar -h

You passed help flag.
usage: java -jar

-d,--debug                        Enable debug traces.

-domain,--domainId <arg>          Identifies the domain that produces the event.

 -cc,--channelsCount <arg>        Number of channels connected to message
                                  bus, default is 1

-en,--exchange_name <arg>         Exchange name.

-ce,--create_exchange <arg>       option to denote if we need to create an exchange
                                  create_exchange or ce to true eg: -ce true or --create_exchange false.

-f,--content_file <arg>           Event content file.

-h,--help                         Show help.

-json,--json_content <arg>        Event content in JSON string. The value can also be a dash (-)
                                  and the JSON will be read from the output of other programs if piped.
                                  Multiple JSON events are also supported.

-mb,--message_bus <arg>           Host of message bus to use.

-mp,--message_protocol <arg>      Name of messaging protocol to be used, e.g: eiffelsemantics.
                                  Default is eiffelsemantics.

-un,--username <arg>              Username to connect to Messagebus

-pwd,--password <arg>             Encrypted Password to connect to MessageBus. Plaintext password
                                  should be encrypted using Base64 Encryption Mechanism.

-np,--non_persistent              Remove persistence from message sending.

-tls,--tls <arg>                  tls version, specify a valid tls version: '1.2' or default.
                                  It is required for RabbitMQ secured port (5671).

-port,--port <arg>                Port to connect to message bus, default is 5672.

-vh,--virtual_host <arg>          Virtual host to connect to (optional).

-ud,--user_domain_suffix <arg>    User domain suffix.

-rk,--routing_key <arg>           Routing key of the eiffel message.
                                  When provided routing key is not generated and the value provided is used.

-tag,--tag <arg>                  Tag to be used in routing key.

-v,--list_versions                Lists the versions of publish and all loaded protocols.
```

For publish we have input only from file that can contain one or more messages in a JSON array (surrounded with square brackets) separated by comma.

The routing key is generated in the protocol library based on the event type.
To get more information on routing key see [here](https://github.com/eiffel-community/eiffel-remrem-semantics).

## Examples

Typical examples of usage Eiffel REMReM Publish CLI are described below.

**Publish on a given host, given exchange, given domain and given user domain suffix:**

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -ud messageQueue -mp eiffelsemantics -un username -pwd password
```

**If you want to have the message non persistent add np flag:**

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -np -mp eiffelsemantics -un username -pwd password 
```

**For loading protocol jars other than eiffelsemantics:**

```
java -Djava.ext.dirs="/path/to/jars/" -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp protocolType -un username -pwd password
```

**NOTE:** in the above example, protocol jar file must be present inside *"/path/to/jars/"* folder.

**NOTE:** `-Djava.ext.dirs` is no longer working in some JAVA8 versions and in JAVA9. So users should create a wrapper project to include both Generate/Publish and their protocol in or place the protocol library in the folder for external dependencies of their JVM installation.

**If you want to have the message publishing logs add d flag:**

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp eiffelsemantics -un username -pwd password -d
```

**If you want to have the message publishing on RabbitMQ secured port (5671):**
-tls option is used to run REMReM on RabbitMQ secured port (5671).
-tls option value is either 'default' or <version> for secured port.

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp eiffelsemantics -un username -pwd password -tls 1.2
```

**NOTE:** for RabbitMQ default port 5672, no need to pass -tls option.

**If you want to have the message publishing on RabbitMQ with given routing key:**

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp eiffelsemantics -rk myroutingkey -un username -pwd password
```

**If you want to change the number of channels connected to RabbitMQ to publish messages:**
channelsCount default value is 1.
If the number of channels increases then the CPU load and memory usage on the RabbitMq increases.

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp eiffelsemantics -cc numberof-channels -un username -pwd password
```

**If you want to have the message publishing on RabbitMQ with given tag:**

```
java -jar publish-cli.jar -f publishMessages.json -en mb-exchange -mb hostname -domain publish-domain -mp eiffelsemantics -un username -pwd password -tag production
```

**To create an exchange passed while publishing the messages use --create_exchange or -ce property to true. Example:**

```
java -jar publish-cli.jar -f EiffelActivityStartedEvent.json -en mb-exchange -ce true -mb hostname -domain publish-domain -mp eiffelsemantics -un username -pwd password
```

Output for these Example:

```
[
  {
    "id": "963da060-f2cf-4370-a68d-67fd872def36",
    "status_code": 200,
    "result": "SUCCESS",
    "message": "Event sent successfully"
  }
]
```

Output when exchange is not found in RabbitMQ, --create_exchange is set to false , Example:

```
[
  {
    "status_code": 404,
    "message": " Unavailable. To create the exchange specify -ce or --create_exchange to true
  }
]
```

## Exit Codes
If CLI fails internally before trying to publish Eiffel message, user will get exit code. Exit codes are described in the table below.

| Exit code | Description                                                                    |
|-----------|--------------------------------------------------------------------------------|
| 1         | User will get this exit code in case of some error that is not described below |
| 2         | Some CLI options are missed                                                    |
| 3         | Passed message protocol or message bus configuration are not correct           |
| 4         | Passed file path is wrong. File is not found                                   |
| 5         | Unable to read content from passed file path                                   |
| 6         | Unable to read passed JSON string from command line                            |

## Status Codes
For each user request Eiffel REMReM Publish generate response in JSON with internal status code and message, when publishing takes place.

To get information about internal status codes see [here](../statusCodes.md).