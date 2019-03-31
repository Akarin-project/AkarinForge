package net.minecraft.network.rcon;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.SERVER)
public interface IServer
{
    int getIntProperty(String key, int defaultValue);

    String getStringProperty(String key, String defaultValue);

    void setProperty(String key, Object value);

    void saveProperties();

    String getSettingsFilename();

    String getHostname();

    int getPort();

    String getMotd();

    String getMinecraftVersion();

    int getCurrentPlayerCount();

    int getMaxPlayers();

    String[] getOnlinePlayerNames();

    String getFolderName();

    String getPlugins();

    String handleRConCommand(String command);

    boolean isDebuggingEnabled();

    void logInfo(String msg);

    void logWarning(String msg);

    void logSevere(String msg);

    void logDebug(String msg);
}