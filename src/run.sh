#!/usr/bin/env bash

javac server/*.java
javac client/*.java
jar cvf client.jar client/*.class

rmiregistry &

sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/HelloServerComponent &
sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy client/HelloClientComponent 127.0.0.1

pkill rmiregistry
