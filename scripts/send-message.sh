#!/usr/bin/env bash

# Read command line arguments
if [[ $# < 2 ]]; then
    echo "Usage: $0 <topic> <message>"
    exit -1
else
    topic=$1
    message=$2
fi

echo $topic
echo $message

curl -X POST -d topic="$topic" -d message="$message" http://localhost:8100/rest/api/v1/messages

echo
