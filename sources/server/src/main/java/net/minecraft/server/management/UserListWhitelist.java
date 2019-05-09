package net.minecraft.server.management;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import java.io.File;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class UserListWhitelist extends UserList<GameProfile, UserListWhitelistEntry>
{
    public UserListWhitelist(File p_i1132_1_)
    {
        super(p_i1132_1_);
    }

    protected UserListEntry<GameProfile> createEntry(JsonObject entryData)
    {
        return new UserListWhitelistEntry(entryData);
    }

    public String[] getKeys()
    {
        String[] astring = new String[this.getValues().size()];
        int i = 0;

        for (UserListWhitelistEntry userlistwhitelistentry : this.getValues().values())
        {
            astring[i++] = ((GameProfile)userlistwhitelistentry.getValue()).getName();
        }

        return astring;
    }

    @SideOnly(Side.SERVER)
    public boolean isWhitelisted(GameProfile profile)
    {
        return this.hasEntry(profile);
    }

    protected String getObjectKey(GameProfile obj)
    {
        return obj.getId().toString();
    }

    public GameProfile getByName(String profileName)
    {
        for (UserListWhitelistEntry userlistwhitelistentry : this.getValues().values())
        {
            if (profileName.equalsIgnoreCase(((GameProfile)userlistwhitelistentry.getValue()).getName()))
            {
                return (GameProfile)userlistwhitelistentry.getValue();
            }
        }

        return null;
    }
}