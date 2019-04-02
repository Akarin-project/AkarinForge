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
	\cp -rf "$basedir/src" "$forgebasedir/projects/Forge/"
	\cp -rf "$basedir/build.gradle" "$forgebasedir/projects/Forge/"
	cd "projects/Forge"
	git commit -m '[Akarin] Gen patches for build'
	gradle build
	
	build="$forgebasedir/build/distributions"
	\cp -rf "$build" "$basedir/"
	
	echo ""
	echo "[Akarin] Build successful"
	echo "[Akarin] Migrated final build to $basedir/build"
)

)
