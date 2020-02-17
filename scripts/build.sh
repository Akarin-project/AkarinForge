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

	echo "[Akarin] Touch forge.."
	\rm -rf "$forgebasedir/projects/Forge/src"
	\cp -rf "$basedir/sources/server/src" "$forgebasedir/projects/Forge/"
	
	echo "[Akarin] Touch server.."
	\rm -rf "$forgebasedir/src"
	\cp -rf "$basedir/sources/forge/src" "$forgebasedir/"

	cd "$forgebasedir"
	echo "[Akarin] Build Forge.. (1/2)"
	./gradlew genPatches
	echo "[Akarin] Build Forge.. (2/2)"
	./gradlew launch4j

	build="$forgebasedir/build/distributions"
	\cp -rf "$build" "$basedir/"

	echo ""
	echo "[Akarin] Build successful"
	echo "[Akarin] Migrated final build to $basedir/distributions"
)

)
