#!/usr/bin/env bash

(
set -e
basedir="$(pwd -P)"

(git submodule update --init --remote && cd "$basedir" && chmod +x scripts/build.sh && ./scripts/build.sh "$basedir" "$1" "$2" "$3") || (
	echo "Failed to build AkarinForge"
	exit 1
) || exit 1

)