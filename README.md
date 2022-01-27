# cryostat-tools Project

This project is helpful tool for JDK Flight Recorder (JFR) with Cryostat.
You can get all JFR information and files which are managed by Cryostat.

For example, If you use OpenShift, you can get all JFR in the namespace, like this.

```shell
export baseUrl=`oc get route cryostat-sample -o jsonpath='{.spec.host}'`
export token=`oc whoami -t`
export cryostat-config="-Dquarkus.rest-client.cryostat.url=${baseUrl} -Dcryostat.token=${token}"
java -jar quarkus-run.jar ${cryostat-config} dump
```

This project uses Quarkus, the Supersonic Subatomic Java Framework. And you can get this tool as native executable binary.

# Sub-command
This tool has two sub commands.

- list
- dump
- delete

`list` show all JFR in the namespace.

`dump` save all JFR in the namespace.

`delete` delete all JFR in the namespace.

### list sub-command
This sub-command has a parameter
- --pod-name-filter (-f)

`pod-name-filter` If you want to target some pods in the namespace, specify the pod name as a regular expression. Default is ".+".

```shell
java -jar quarkus-run.jar ${cryostat-config} list -f=".+"
```

### dump sub-command
This sub-command has two parameters.
- --pod-name-filter (-f)
- --dump-dir (-d)

`pod-name-filter` If you want to target some pods in the namespace, specify the pod name as a regular expression. Default is ".+".

`dump-dir` Both absolute and relative paths are acceptable. Default is "dump".

```shell
java -jar quarkus-run.jar ${cryostat-config} dump -f=".+" -d="./dump"
```

### delete sub-command
This sub-command has a parameter
- --pod-name-filter (-f)

`pod-name-filter` If you want to target some pods in the namespace, specify the pod name as a regular expression. Default is ".+".

```shell
java -jar quarkus-run.jar ${cryostat-config} delete -f=".+"
```

# Required parameters
To use this tool, two parameters are mandatory.
You have to set these parameters as VM parameter with -D or set those in $PWD/config/application.properties.

- quarkus.rest-client.cryostat.url
- cryostat.token

`quarkus.rest-client.cryostat.url` is where you specify the URL to connect to Cryostat. You can obtain this URL with this command. `kubectl get route <cryostat instance> -o jsonpath='{.spec.host}'`

`cryostat.token` specifies the token used to login to Cryostat. You can obtain this token with this command. `kubectl whoami -t`

```shell
export baseUrl=`https://xxxxxx`
export token=`sha256~xxxxxx`
export cryostat-config="-Dquarkus.rest-client.cryostat.url=${baseUrl} -Dcryostat.token=${token}"
java -jar quarkus-run.jar ${cryostat-config} dump
```
or
```shell
cat $PWD/config/application.properties
quarkus.rest-client.cryostat.url=https://xxxxx
cryostat.token=sha256~xxxxx

java -jar quarkus-run.jar dump
```

## Running the application in dev mode

You can run your application in Quarkus dev mode that enables live coding using:
```shell script
./mvnw compile quarkus:dev
```

## Packaging and running the application

The application can be packaged using:
```shell script
./mvnw package
```
It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:
```shell script
./mvnw package -Dquarkus.package.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using: 
```shell script
./mvnw package -Pnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using: 
```shell script
./mvnw package -Pnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/jfr-downloader-1.0.0-SNAPSHOT-runner`

