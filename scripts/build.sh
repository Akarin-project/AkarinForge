#!/usr/bin/env bash

(
set -e
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
forgebasedir="$basedir/work/MinecraftForge"

if [ "$2" == "--setup" ] || [ "$3" == "--setup" ] || [ "$4" == "--setup" ]; then
	echo "[Akarin] Setup Forge.."
	(
		cd "$forgebasedir"
		./gradlew build
	)
fi

echo "[Akarin] Ready to build"
(
	cd "$forgebasedir"
	echo "[Akarin] Touch sources.."
	
	cd "$forgebasedir"
	if [ "$2" == "--fast" ] || [ "$3" == "--fast" ] || [ "$4" == "--fast" ]; then
		echo "[Akarin] Repatch has been skipped"
		\cp -rf "$basedir/src" "$forgebasedir/projects/Forge/"
		\cp -rf "$basedir/build.gradle" "$forgebasedir/projects/Forge/"
		mvn clean install -DskipTests
	else
		rm -rf projects/Forge
		./gradlew build
		\cp -rf "$basedir/src" "$forgebasedir/projects/Forge/"
		\cp -rf "$basedir/build.gradle" "$forgebasedir/projects/Forge/"
		mvn clean install -DskipTests
	fi
	
	minecraftversion="1.12.2"
	build="$forgebasedir/build/distributions"
	\cp -rf "$build" "$basedir/"
	
	echo ""
	echo "[Akarin] Build successful"
	echo "[Akarin] Migrated final build to $basedir/build"
)

)
