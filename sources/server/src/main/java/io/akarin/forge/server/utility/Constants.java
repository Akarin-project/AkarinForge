package io.akarin.forge.server.utility;

public class Constants {
    public static final String GAME_VERSION = "1.12.2";
    
    public static final String NMS_VERSION = "v1_12_R1";
    
    public static final String MAPPING_PATH = "mappings/".concat(NMS_VERSION).concat("/spigot.asrg");
    
    public static final String NMS_PREFIX_PATH = "net/minecraft/server/";
    
    public static final String NMS_PATH = NMS_PREFIX_PATH.concat(NMS_VERSION);
    
    public static final String NMS_PREFIX_DOMAIN = "net.minecraft.server";
    
    public static final String NMS_DOMAIN = NMS_PREFIX_DOMAIN.concat(".").concat(NMS_VERSION);
    
    public static final String OBC_PREFIX_PATH = "org/bukkit/craftbukkit/";
    
    public static final String OBC_PREFIX_DOMAIN = "org.bukkit.craftbukkit";
    
    public static final String OBC_PATH = OBC_PREFIX_PATH.concat(NMS_VERSION);
    
    public static final String OBC_DOMAIN = OBC_PREFIX_DOMAIN.concat(".").concat(NMS_VERSION);
    
    public static final String MINECRAFT_DOMAIN = "net.minecraft.";
}
