package net.minecraft.crash;

import com.google.common.collect.Lists;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import net.minecraft.util.ReportedException;
import net.minecraft.world.gen.layer.IntCache;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CrashReport
{
    private static final Logger LOGGER = LogManager.getLogger();
    private final String description;
    private final Throwable cause;
    private final CrashReportCategory systemDetailsCategory = new CrashReportCategory(this, "System Details");
    private final List<CrashReportCategory> crashReportSections = Lists.<CrashReportCategory>newArrayList();
    private File crashReportFile;
    private boolean firstCategoryInCrashReport = true;
    private StackTraceElement[] stacktrace = new StackTraceElement[0];

    public CrashReport(String descriptionIn, Throwable causeThrowable)
    {
        this.description = descriptionIn;
        this.cause = causeThrowable;
        this.populateEnvironment();
    }

    private void populateEnvironment()
    {
        this.systemDetailsCategory.addDetail("Minecraft Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return "1.12.2";
            }
        });
        this.systemDetailsCategory.addDetail("Operating System", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");
            }
        });
        this.systemDetailsCategory.addDetail("Java Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("java.version") + ", " + System.getProperty("java.vendor");
            }
        });
        this.systemDetailsCategory.addDetail("Java VM Version", new ICrashReportDetail<String>()
        {
            public String call()
            {
                return System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");
            }
        });
        this.systemDetailsCategory.addDetail("Memory", new ICrashReportDetail<String>()
        {
            public String call()
            {
                Runtime runtime = Runtime.getRuntime();
                long i = runtime.maxMemory();
                long j = runtime.totalMemory();
                long k = runtime.freeMemory();
                long l = i / 1024L / 1024L;
                long i1 = j / 1024L / 1024L;
                long j1 = k / 1024L / 1024L;
                return k + " bytes (" + j1 + " MB) / " + j + " bytes (" + i1 + " MB) up to " + i + " bytes (" + l + " MB)";
            }
        });
        this.systemDetailsCategory.addDetail("JVM Flags", new ICrashReportDetail<String>()
        {
            public String call()
            {
                RuntimeMXBean runtimemxbean = ManagementFactory.getRuntimeMXBean();
                List<String> list = runtimemxbean.getInputArguments();
                int i = 0;
                StringBuilder stringbuilder = new StringBuilder();

                for (String s : list)
                {
                    if (s.startsWith("-X"))
                    {
                        if (i++ > 0)
                        {
                            stringbuilder.append(" ");
                        }

                        stringbuilder.append(s);
                    }
                }

                return String.format("%d total; %s", i, stringbuilder.toString());
            }
        });
        this.systemDetailsCategory.addDetail("IntCache", new ICrashReportDetail<String>()
        {
            public String call() throws Exception
            {
                return IntCache.getCacheSizes();
            }
        });
        net.minecraftforge.fml.common.FMLCommonHandler.instance().enhanceCrashReport(this, this.systemDetailsCategory);
    }

    public String getDescription()
    {
        return this.description;
    }

    public Throwable getCrashCause()
    {
        return this.cause;
    }

    public void getSectionsInStringBuilder(StringBuilder builder)
    {
        if ((this.stacktrace == null || this.stacktrace.length <= 0) && !this.crashReportSections.isEmpty())
        {
            this.stacktrace = (StackTraceElement[])ArrayUtils.subarray(((CrashReportCategory)this.crashReportSections.get(0)).getStackTrace(), 0, 1);
        }

        if (this.stacktrace != null && this.stacktrace.length > 0)
        {
            builder.append("-- Head --\n");
            builder.append("Thread: ").append(Thread.currentThread().getName()).append("\n");
            builder.append("Stacktrace:\n");

            for (StackTraceElement stacktraceelement : this.stacktrace)
            {
                builder.append("\t").append("at ").append((Object)stacktraceelement);
                builder.append("\n");
            }

            builder.append("\n");
        }

        for (CrashReportCategory crashreportcategory : this.crashReportSections)
        {
            crashreportcategory.appendToStringBuilder(builder);
            builder.append("\n\n");
        }

        this.systemDetailsCategory.appendToStringBuilder(builder);
    }

    public String getCauseStackTraceOrString()
    {
        StringWriter stringwriter = null;
        PrintWriter printwriter = null;
        Throwable throwable = this.cause;

        if (throwable.getMessage() == null)
        {
            if (throwable instanceof NullPointerException)
            {
                throwable = new NullPointerException(this.description);
            }
            else if (throwable instanceof StackOverflowError)
            {
                throwable = new StackOverflowError(this.description);
            }
            else if (throwable instanceof OutOfMemoryError)
            {
                throwable = new OutOfMemoryError(this.description);
            }

            throwable.setStackTrace(this.cause.getStackTrace());
        }

        String s = throwable.toString();

        try
        {
            stringwriter = new StringWriter();
            printwriter = new PrintWriter(stringwriter);
            throwable.printStackTrace(printwriter);
            s = stringwriter.toString();
        }
        finally
        {
            IOUtils.closeQuietly((Writer)stringwriter);
            IOUtils.closeQuietly((Writer)printwriter);
        }

        return s;
    }

    public String getCompleteReport()
    {
        StringBuilder stringbuilder = new StringBuilder();
        stringbuilder.append("---- Minecraft Crash Report ----\n");
        net.minecraftforge.fml.relauncher.CoreModManager.onCrash(stringbuilder);
        stringbuilder.append("// ");
        stringbuilder.append(getWittyComment());
        stringbuilder.append("\n\n");
        stringbuilder.append("Time: ");
        stringbuilder.append((new SimpleDateFormat()).format(new Date()));
        stringbuilder.append("\n");
        stringbuilder.append("Description: ");
        stringbuilder.append(this.description);
        stringbuilder.append("\n\n");
        stringbuilder.append(this.getCauseStackTraceOrString());
        stringbuilder.append("\n\nA detailed walkthrough of the error, its code path and all known details is as follows:\n");

        for (int i = 0; i < 87; ++i)
        {
            stringbuilder.append("-");
        }

        stringbuilder.append("\n\n");
        this.getSectionsInStringBuilder(stringbuilder);
        return stringbuilder.toString();
    }

    @SideOnly(Side.CLIENT)
    public File getFile()
    {
        return this.crashReportFile;
    }

    public boolean saveToFile(File toFile)
    {
        if (this.crashReportFile != null)
        {
            return false;
        }
        else
        {
            if (toFile.getParentFile() != null)
            {
                toFile.getParentFile().mkdirs();
            }

            Writer writer = null;
            boolean flag1;

            try
            {
                writer = new OutputStreamWriter(new FileOutputStream(toFile), StandardCharsets.UTF_8);
                writer.write(this.getCompleteReport());
                this.crashReportFile = toFile;
                boolean lvt_3_1_ = true;
                return lvt_3_1_;
            }
            catch (Throwable throwable)
            {
                LOGGER.error("Could not save crash report to {}", toFile, throwable);
                flag1 = false;
            }
            finally
            {
                IOUtils.closeQuietly(writer);
            }

            return flag1;
        }
    }

    public CrashReportCategory getCategory()
    {
        return this.systemDetailsCategory;
    }

    public CrashReportCategory makeCategory(String name)
    {
        return this.makeCategoryDepth(name, 1);
    }

    public CrashReportCategory makeCategoryDepth(String categoryName, int stacktraceLength)
    {
        CrashReportCategory crashreportcategory = new CrashReportCategory(this, categoryName);

        if (this.firstCategoryInCrashReport)
        {
            int i = crashreportcategory.getPrunedStackTrace(stacktraceLength);
            StackTraceElement[] astacktraceelement = this.cause.getStackTrace();
            StackTraceElement stacktraceelement = null;
            StackTraceElement stacktraceelement1 = null;
            int j = astacktraceelement.length - i;

            if (j < 0)
            {
                System.out.println("Negative index in crash report handler (" + astacktraceelement.length + "/" + i + ")");
            }

            if (astacktraceelement != null && 0 <= j && j < astacktraceelement.length)
            {
                stacktraceelement = astacktraceelement[j];

                if (astacktraceelement.length + 1 - i < astacktraceelement.length)
                {
                    stacktraceelement1 = astacktraceelement[astacktraceelement.length + 1 - i];
                }
            }

            this.firstCategoryInCrashReport = crashreportcategory.firstTwoElementsOfStackTraceMatch(stacktraceelement, stacktraceelement1);

            if (i > 0 && !this.crashReportSections.isEmpty())
            {
                CrashReportCategory crashreportcategory1 = this.crashReportSections.get(this.crashReportSections.size() - 1);
                crashreportcategory1.trimStackTraceEntriesFromBottom(i);
            }
            else if (astacktraceelement != null && astacktraceelement.length >= i && 0 <= j && j < astacktraceelement.length)
            {
                this.stacktrace = new StackTraceElement[j];
                System.arraycopy(astacktraceelement, 0, this.stacktrace, 0, this.stacktrace.length);
            }
            else
            {
                this.firstCategoryInCrashReport = false;
            }
        }

        this.crashReportSections.add(crashreportcategory);
        return crashreportcategory;
    }

    private static String getWittyComment()
    {
        String[] astring = new String[] {"Who set us up the TNT?", "Everything's going to plan. No, really, that was supposed to happen.", "Uh... Did I do that?", "Oops.", "Why did you do that?", "I feel sad now :(", "My bad.", "I'm sorry, Dave.", "I let you down. Sorry :(", "On the bright side, I bought you a teddy bear!", "Daisy, daisy...", "Oh - I know what I did wrong!", "Hey, that tickles! Hehehe!", "I blame Dinnerbone.", "You should try our sister game, Minceraft!", "Don't be sad. I'll do better next time, I promise!", "Don't be sad, have a hug! <3", "I just don't know what went wrong :(", "Shall we play a game?", "Quite honestly, I wouldn't worry myself about that.", "I bet Cylons wouldn't have this problem.", "Sorry :(", "Surprise! Haha. Well, this is awkward.", "Would you like a cupcake?", "Hi. I'm Minecraft, and I'm a crashaholic.", "Ooh. Shiny.", "This doesn't make any sense!", "Why is it breaking :(", "Don't do that.", "Ouch. That hurt :(", "You're mean.", "This is a token for 1 free hug. Redeem at your nearest Mojangsta: [~~HUG~~]", "There are four lights!", "But it works on my machine."};

        try
        {
            return astring[(int)(System.nanoTime() % (long)astring.length)];
        }
        catch (Throwable var2)
        {
            return "Witty comment unavailable :(";
        }
    }

    public static CrashReport makeCrashReport(Throwable causeIn, String descriptionIn)
    {
        CrashReport crashreport;

        if (causeIn instanceof ReportedException)
        {
            crashreport = ((ReportedException)causeIn).getCrashReport();
        }
        else
        {
            crashreport = new CrashReport(descriptionIn, causeIn);
        }

        return crashreport;
    }
}