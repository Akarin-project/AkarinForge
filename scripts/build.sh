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
		echo "[Akarin] Setup Forge.."
		./gradlew setupForge

		echo "[Akarin] Touch workspace.."
		\cp -rf "$forgebasedir/projects/Forge/.settings" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/.project" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/.classpath" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/.settings" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/Forge.iml" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/Forge Client.launch" "$basedir/"
		\cp -rf "$forgebasedir/projects/Forge/Forge Server.launch" "$basedir/"
	)
fi

echo "[Akarin] Ready to build"
(
	echo "[Akarin] Touch sources.."
	\cp -rf "$basedir/jsons" "$forgebasedir/"

	cd "$forgebasedir"
	echo "[Akarin] Setup/Clean Forge.."
	./gradlew setupForge

	echo "[Akarin] Touch Forge.."
	\cp -rf "$basedir/src" "$forgebasedir/projects/Forge/"
	\cp -rf "$basedir/icon.ico" "$forgebasedir/"

	cd "$forgebasedir"
	echo "[Akarin] Build Forge.."
	./gradlew genPatches
	./gradlew build

	build="$forgebasedir/build/distributions"
	\cp -rf "$build" "$basedir/"

	echo ""
	echo "[Akarin] Build successful"
	echo "[Akarin] Migrated final build to $basedir/build"
)

)
