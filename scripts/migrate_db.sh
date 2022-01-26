#!/bin/bash
set -e -x

# $1 - classpath
# $2 - directory for db
# $3 - name for db
# $4 - java binary
# $5 - flyway.conf location

mkdir -p $2
sqlite3 $2/$3 "VACUUM;"
mkdir sql && cp resources/db/sql/* sql
$4 -cp $1 org.flywaydb.commandline.Main -color=never -configFiles=$5 \
-url=jdbc:sqlite:$2/$3 -locations=filesystem:sql -jarDirs=sql migrate
