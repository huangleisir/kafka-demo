#!/usr/bin/env bash

# Read command line arguments
if [[ $# < 1 ]]; then
    echo "Usage: $0 <topic>"
    exit -1
else
    topic=$1
fi

curl -d topic="$topic" -X POST http://localhost:8100/rest/api/v1/topics

echo
