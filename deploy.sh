#!/bin/sh
mvn clean package
scp target/ROOT.war root@playground.jagsits.com:/tmp
