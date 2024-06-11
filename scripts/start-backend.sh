#!/bin/bash

cd ..

./gradlew clean build -x test -Dorg.gradle.daemon=false

./gradlew bootRun