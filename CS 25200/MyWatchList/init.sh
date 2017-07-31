#!/bin/bash

cf push MyWatchList -b https://github.com/cloudfoundry-community/cf-meteor-buildpack.git --no-start
cf create-service mongodb 100 MyWatchList-Mongo 
cf bind-service MyWatchList MyWatchList-Mongo

cf start MyWatchList