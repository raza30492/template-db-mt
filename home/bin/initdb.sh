#!/bin/bash

if [ "$#" -ne 3 ]; then
        echo "Usage: createdb <mysql|postgresql> <database name> <init script location>"
        exit 1
elif [[  "$1" != "mysql"  &&  "$1" != "postgresql"  ]]; then
        echo "Usage: createdb <mysql|postgresql> <database name> <init script location>"
        exit 1
elif [ ! -f "$3" ]; then
        echo "File $3 does not exist"
        exit 1
fi

if [ "$1" = "postgresql" ]; then
    #echo "postgresql database selected."
    if psql -lqt | cut -d \| -f 1 | grep -qw "$2"; then
        echo "database $2 already exists."
    else
        #echo "database $2 does not exist. creating database"
        createdb "$2"
        if [ "$?" -eq 0 ]; then
                echo "$2 database created successfully"
                psql $2 < $3
        else
                echo "Failed to create $2 database"
        fi
    fi
else
    echo "mysql database selected."

fi