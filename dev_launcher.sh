#!/bin/bash

cd docker/database
docker-compose up -d
cd ../..

sbt run
