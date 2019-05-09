package net.minecraft.util.text.translation;

import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import com.google.common.collect.Maps;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.IllegalFormatException;
import java.util.Map;
import java.util.regex.Pattern;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;

public class LanguageMap
{
    private static final Pattern NUMERIC_VARIABLE_PATTERN = Pattern.compile("%(\\d+\\$)?[\\d\\.]*[df]");
    private static final Splitter EQUAL_SIGN_SPLITTER = Splitter.on('=').limit(2);
    private static final LanguageMap instance = new LanguageMap();
    private final Map<String, String> languageList = Maps.<String, String>newHashMap();
    private long lastUpdateTimeInMilliseconds;

    public LanguageMap()
    {
        InputStream inputstream = LanguageMap.class.getResourceAsStream("/assets/minecraft/lang/en_us.lang");
        inject(this, inputstream);
    }

    public static void inject(InputStream inputstream)
    {
        inject(instance, inputstream);
    }

    private static void inject(LanguageMap inst, InputStream inputstream)
    {
        Map<String, String> map = parseLangFile(inputstream);
        inst.languageList.putAll(map);
        inst.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public static Map<String, String> parseLangFile(InputStream inputstream)
    {
        Map<String, String> table = Maps.newHashMap();
        try
        {
            inputstream = net.minecraftforge.fml.common.FMLCommonHandler.instance().loadLanguage(table, inputstream);
            if (inputstream == null) return table;

            for (String s : IOUtils.readLines(inputstream, StandardCharsets.UTF_8))
            {
                if (!s.isEmpty() && s.charAt(0) != '#')
                {
                    String[] astring = (String[])Iterables.toArray(EQUAL_SIGN_SPLITTER.split(s), String.class);

                    if (astring != null && astring.length == 2)
                    {
                        String s1 = astring[0];
                        String s2 = NUMERIC_VARIABLE_PATTERN.matcher(astring[1]).replaceAll("%$1s");
                        table.put(s1, s2);
                    }
                }
            }

        }
        catch (IOException var7)
        {
            ;
        }
        catch (Exception ex) {}
        return table;
    }

    static LanguageMap getInstance()
    {
        return instance;
    }

    @SideOnly(Side.CLIENT)

    public static synchronized void replaceWith(Map<String, String> p_135063_0_)
    {
        instance.languageList.clear();
        instance.languageList.putAll(p_135063_0_);
        instance.lastUpdateTimeInMilliseconds = System.currentTimeMillis();
    }

    public synchronized String translateKey(String key)
    {
        return this.tryTranslateKey(key);
    }

    public synchronized String translateKeyFormat(String key, Object... format)
    {
        String s = this.tryTranslateKey(key);

        try
        {
            return String.format(s, format);
        }
        catch (IllegalFormatException var5)
        {
            return "Format error: " + s;
        }
    }

    private String tryTranslateKey(String key)
    {
        String s = this.languageList.get(key);
        return s == null ? key : s;
    }

    public synchronized boolean isKeyTranslated(String key)
    {
        return this.languageList.containsKey(key);
    }

    public long getLastUpdateTimeInMilliseconds()
    {
        return this.lastUpdateTimeInMilliseconds;
    }
}