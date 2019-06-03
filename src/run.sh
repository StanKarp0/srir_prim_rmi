#!/usr/bin/env bash

javac server/*.java
javac client/*.java
jar cvf client.jar client/*.class

rmiregistry &

sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 0 &
p1=$!
echo $p1
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 1 &
p2=$!
echo $p2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 2 &
p3=$!
echo $p3
sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy client/PrimClientComponent ../resources/nodes50edges100.csv 3

pkill rmiregistry
kill $p1
kill $p2
kill $p3
