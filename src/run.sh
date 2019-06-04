#!/usr/bin/env bash

rmiregistry &

sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 0 &
p1=$!
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 1 &
p2=$!
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy server/PrimServerComponent 2 &
p3=$!
sleep 2
java -cp . -Djava.rmi.server.codebase=file:client.jar -Djava.security.policy=java.policy client/PrimClientComponent $1 3

pkill rmiregistry
kill $p1
kill $p2
kill $p3
