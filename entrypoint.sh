#!/bin/sh

# for debug purpose
echo "Running generator in $(pwd)"
echo "Working directory: "
ls -la

java -jar /gpg.jar "$@"
