#!/bin/bash
mvn clean package
scp target/ROOT.war root@159.203.28.31:/tmp
