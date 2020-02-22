#!/usr/bin/env bash

(
set -e
basedir="$(cd "$1" && pwd -P)"
workdir="$basedir/work"
forgebasedir="$basedir/work/MinecraftForge"

if [ "$2" == "--setup" ] || [ "$3" == "--setup" ] || [ "$4" == "--setup" ]; then
	echo "[Akarin] Setup workspace.."
	(	
		cd "$forgebasedir"
		\cp -rf "$basedir/jsons" "$forgebasedir/"
		echo "[Akarin] Setup forge.."
		./gradlew setupForge
		
		echo "[Akarin] Touch workspace.."
		#\cp -rf "$forgebasedir/projects/Forge/.settings" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/.project" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/.classpath" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/.settings" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/Forge.iml" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/Forge Client.launch" "$basedir/"
		#\cp -rf "$forgebasedir/projects/Forge/Forge Server.launch" "$basedir/"
	)
fi
echo "[Akarin] Ready to build"
(
	echo "[Akarin] Touch resources.."
	\cp -rf "$basedir/jsons" "$forgebasedir/"
	\cp -rf "$basedir/icon.ico" "$forgebasedir/"

	echo "[Akarin] Touch server.."
	if [ "$2" == "--nodelsrc" ] || [ "$3" == "--nodelsrc" ] || [ "$4" == "--nodelsrc" ]; then
		\cp -rf "$basedir/sources/server/src" "$forgebasedir/projects/Forge/"
	else
		\rm -rf "$forgebasedir/projects/Forge/src"
		\cp -rf "$basedir/sources/server/src" "$forgebasedir/projects/Forge/"
	fi
	
	echo "[Akarin] Touch forge.."
	if [ "$2" == "--nodelsrc" ] || [ "$3" == "--nodelsrc" ] || [ "$4" == "--nodelsrc" ]; then
		\cp -rf "$basedir/sources/forge/src" "$forgebasedir/"
	else
		\rm -rf "$forgebasedir/src"
		\cp -rf "$basedir/sources/forge/src" "$forgebasedir/"
	fi

	cd "$forgebasedir"
	echo "[Akarin] Build Forge.. (1/2)"
	./gradlew genPatches
	if [ "$2" == "--nobrute" ] || [ "$3" == "--nobrute" ] || [ "$4" == "--nobrute" ]; then
		echo "[Akarin] Build Forge.. (2/2)"
		./gradlew createExe
	else
		echo "[Akarin] Brute Forcing"
		\cp "$basedir/changelog_new.txt" "$forgebasedir/build/"
		echo "[Akarin] Build Forge.. (2/2)"
		./gradlew createExe
	fi

	build="$forgebasedir/build/distributions"
	\cp -rf "$build" "$basedir/"

	echo ""
	echo "[Akarin] Build successful"
	echo "[Akarin] Migrated final build to $basedir/distributions"
)

)
