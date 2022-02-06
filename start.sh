#!/bin/bash -x

sbt cache/package

mkdir -p web/lib
mv cache/target/scala-2.13/cache_2.13-0.1.0-SNAPSHOT.jar web/lib/

sbt web/run

