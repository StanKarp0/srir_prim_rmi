#!/usr/bin/env bash

javac server/*.java
javac client/*.java
jar cvf client.jar client/*.class

rmiregistry &

sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 0 &
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 1 &
sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy client/PrimClientComponent ../resources/nodes10edges20.csv 2

pkill rmiregistry

