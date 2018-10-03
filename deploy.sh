#!/bin/sh
if ./gradlew prepareDeployment; then
  	echo "Deployment successfully prepared" >&2
  	echo "Uploading to Zeit..." >&2
 	parent_path=$( cd "$(dirname "${BASH_SOURCE[0]}")" ; pwd -P )
	cd "$parent_path/build/strydal-backend"
	now -T strydal
else
  	echo "Could not prepare deployment, check the output above!" >&2
fi


