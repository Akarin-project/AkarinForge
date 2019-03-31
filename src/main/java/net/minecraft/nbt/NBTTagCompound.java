package net.minecraft.nbt;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.annotation.Nullable;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.crash.ICrashReportDetail;
import net.minecraft.util.ReportedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class NBTTagCompound extends NBTBase
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final Pattern SIMPLE_VALUE = Pattern.compile("[A-Za-z0-9._+-]+");
    private final Map<String, NBTBase> tagMap = Maps.<String, NBTBase>newHashMap();

    void write(DataOutput output) throws IOException
    {
        for (String s : this.tagMap.keySet())
        {
            NBTBase nbtbase = this.tagMap.get(s);
            writeEntry(s, nbtbase, output);
        }

        output.writeByte(0);
    }

    void read(DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(384L);

        if (depth > 512)
        {
            throw new RuntimeException("Tried to read NBT tag with too high complexity, depth > 512");
        }
        else
        {
            this.tagMap.clear();
            byte b0;

            while ((b0 = readType(input, sizeTracker)) != 0)
            {
                String s = readKey(input, sizeTracker);
                sizeTracker.read((long)(224 + 16 * s.length()));
                NBTBase nbtbase = readNBT(b0, s, input, depth + 1, sizeTracker);

                if (this.tagMap.put(s, nbtbase) != null)
                {
                    sizeTracker.read(288L);
                }
            }
        }
    }

    public Set<String> getKeySet()
    {
        return this.tagMap.keySet();
    }

    public byte getId()
    {
        return 10;
    }

    public int getSize()
    {
        return this.tagMap.size();
    }

    public void setTag(String key, NBTBase value)
    {
        if (value == null) throw new IllegalArgumentException("Invalid null NBT value with key " + key);
        this.tagMap.put(key, value);
    }

    public void setByte(String key, byte value)
    {
        this.tagMap.put(key, new NBTTagByte(value));
    }

    public void setShort(String key, short value)
    {
        this.tagMap.put(key, new NBTTagShort(value));
    }

    public void setInteger(String key, int value)
    {
        this.tagMap.put(key, new NBTTagInt(value));
    }

    public void setLong(String key, long value)
    {
        this.tagMap.put(key, new NBTTagLong(value));
    }

    public void setUniqueId(String key, UUID value)
    {
        this.setLong(key + "Most", value.getMostSignificantBits());
        this.setLong(key + "Least", value.getLeastSignificantBits());
    }

    @Nullable
    public UUID getUniqueId(String key)
    {
        return new UUID(this.getLong(key + "Most"), this.getLong(key + "Least"));
    }

    public boolean hasUniqueId(String key)
    {
        return this.hasKey(key + "Most", 99) && this.hasKey(key + "Least", 99);
    }

    public void setFloat(String key, float value)
    {
        this.tagMap.put(key, new NBTTagFloat(value));
    }

    public void setDouble(String key, double value)
    {
        this.tagMap.put(key, new NBTTagDouble(value));
    }

    public void setString(String key, String value)
    {
        this.tagMap.put(key, new NBTTagString(value));
    }

    public void setByteArray(String key, byte[] value)
    {
        this.tagMap.put(key, new NBTTagByteArray(value));
    }

    public void setIntArray(String key, int[] value)
    {
        this.tagMap.put(key, new NBTTagIntArray(value));
    }

    public void setBoolean(String key, boolean value)
    {
        this.setByte(key, (byte)(value ? 1 : 0));
    }

    public NBTBase getTag(String key)
    {
        return this.tagMap.get(key);
    }

    public byte getTagId(String key)
    {
        NBTBase nbtbase = this.tagMap.get(key);
        return nbtbase == null ? 0 : nbtbase.getId();
    }

    public boolean hasKey(String key)
    {
        return this.tagMap.containsKey(key);
    }

    public boolean hasKey(String key, int type)
    {
        int i = this.getTagId(key);

        if (i == type)
        {
            return true;
        }
        else if (type != 99)
        {
            return false;
        }
        else
        {
            return i == 1 || i == 2 || i == 3 || i == 4 || i == 5 || i == 6;
        }
    }

    public byte getByte(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getByte();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public short getShort(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getShort();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public int getInteger(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getInt();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0;
    }

    public long getLong(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getLong();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0L;
    }

    public float getFloat(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getFloat();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0F;
    }

    public double getDouble(String key)
    {
        try
        {
            if (this.hasKey(key, 99))
            {
                return ((NBTPrimitive)this.tagMap.get(key)).getDouble();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return 0.0D;
    }

    public String getString(String key)
    {
        try
        {
            if (this.hasKey(key, 8))
            {
                return ((NBTBase)this.tagMap.get(key)).getString();
            }
        }
        catch (ClassCastException var3)
        {
            ;
        }

        return "";
    }

    public byte[] getByteArray(String key)
    {
        try
        {
            if (this.hasKey(key, 7))
            {
                return ((NBTTagByteArray)this.tagMap.get(key)).getByteArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 7, classcastexception));
        }

        return new byte[0];
    }

    public int[] getIntArray(String key)
    {
        try
        {
            if (this.hasKey(key, 11))
            {
                return ((NBTTagIntArray)this.tagMap.get(key)).getIntArray();
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 11, classcastexception));
        }

        return new int[0];
    }

    public NBTTagCompound getCompoundTag(String key)
    {
        try
        {
            if (this.hasKey(key, 10))
            {
                return (NBTTagCompound)this.tagMap.get(key);
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 10, classcastexception));
        }

        return new NBTTagCompound();
    }

    public NBTTagList getTagList(String key, int type)
    {
        try
        {
            if (this.getTagId(key) == 9)
            {
                NBTTagList nbttaglist = (NBTTagList)this.tagMap.get(key);

                if (!nbttaglist.hasNoTags() && nbttaglist.getTagType() != type)
                {
                    return new NBTTagList();
                }

                return nbttaglist;
            }
        }
        catch (ClassCastException classcastexception)
        {
            throw new ReportedException(this.createCrashReport(key, 9, classcastexception));
        }

        return new NBTTagList();
    }

    public boolean getBoolean(String key)
    {
        return this.getByte(key) != 0;
    }

    public void removeTag(String key)
    {
        this.tagMap.remove(key);
    }

    public String toString()
    {
        StringBuilder stringbuilder = new StringBuilder("{");
        Collection<String> collection = this.tagMap.keySet();

        if (LOGGER.isDebugEnabled())
        {
            List<String> list = Lists.newArrayList(this.tagMap.keySet());
            Collections.sort(list);
            collection = list;
        }

        for (String s : collection)
        {
            if (stringbuilder.length() != 1)
            {
                stringbuilder.append(',');
            }

            stringbuilder.append(handleEscape(s)).append(':').append(this.tagMap.get(s));
        }

        return stringbuilder.append('}').toString();
    }

    public boolean hasNoTags()
    {
        return this.tagMap.isEmpty();
    }

    private CrashReport createCrashReport(final String key, final int expectedType, ClassCastException ex)
    {
        CrashReport crashreport = CrashReport.makeCrashReport(ex, "Reading NBT data");
        CrashReportCategory crashreportcategory = crashreport.makeCategoryDepth("Corrupt NBT tag", 1);
        crashreportcategory.addDetail("Tag type found", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.NBT_TYPES[((NBTBase)NBTTagCompound.this.tagMap.get(key)).getId()];
            }
        });
        crashreportcategory.addDetail("Tag type expected", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return NBTBase.NBT_TYPES[expectedType];
            }
        });
        crashreportcategory.addCrashSection("Tag name", key);
        return crashreport;
    }

    public NBTTagCompound copy()
    {
        NBTTagCompound nbttagcompound = new NBTTagCompound();

        for (String s : this.tagMap.keySet())
        {
            nbttagcompound.setTag(s, ((NBTBase)this.tagMap.get(s)).copy());
        }

        return nbttagcompound;
    }

    public boolean equals(Object p_equals_1_)
    {
        return super.equals(p_equals_1_) && Objects.equals(this.tagMap.entrySet(), ((NBTTagCompound)p_equals_1_).tagMap.entrySet());
    }

    public int hashCode()
    {
        return super.hashCode() ^ this.tagMap.hashCode();
    }

    private static void writeEntry(String name, NBTBase data, DataOutput output) throws IOException
    {
        output.writeByte(data.getId());

        if (data.getId() != 0)
        {
            output.writeUTF(name);
            data.write(output);
        }
    }

    private static byte readType(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(8);
        return input.readByte();
    }

    private static String readKey(DataInput input, NBTSizeTracker sizeTracker) throws IOException
    {
        return input.readUTF();
    }

    static NBTBase readNBT(byte id, String key, DataInput input, int depth, NBTSizeTracker sizeTracker) throws IOException
    {
        sizeTracker.read(32); //Forge: 4 extra bytes for the object allocation.
        NBTBase nbtbase = NBTBase.createNewByType(id);

        try
        {
            nbtbase.read(input, depth, sizeTracker);
            return nbtbase;
        }
        catch (IOException ioexception)
        {
            CrashReport crashreport = CrashReport.makeCrashReport(ioexception, "Loading NBT data");
            CrashReportCategory crashreportcategory = crashreport.makeCategory("NBT Tag");
            crashreportcategory.addCrashSection("Tag name", key);
            crashreportcategory.addCrashSection("Tag type", Byte.valueOf(id));
            throw new ReportedException(crashreport);
        }
    }

    public void merge(NBTTagCompound other)
    {
        for (String s : other.tagMap.keySet())
        {
            NBTBase nbtbase = other.tagMap.get(s);

            if (nbtbase.getId() == 10)
            {
                if (this.hasKey(s, 10))
                {
                    NBTTagCompound nbttagcompound = this.getCompoundTag(s);
                    nbttagcompound.merge((NBTTagCompound)nbtbase);
                }
                else
                {
                    this.setTag(s, nbtbase.copy());
                }
            }
            else
            {
                this.setTag(s, nbtbase.copy());
            }
        }
    }

    protected static String handleEscape(String p_193582_0_)
    {
        return SIMPLE_VALUE.matcher(p_193582_0_).matches() ? p_193582_0_ : NBTTagString.quoteAndEscape(p_193582_0_);
    }
}