package net.minecraft.util;

import net.minecraft.crash.CrashReport;

public class ReportedException extends RuntimeException
{
    private final CrashReport crashReport;

    public ReportedException(CrashReport report)
    {
        this.crashReport = report;
    }

    public CrashReport getCrashReport()
    {
        return this.crashReport;
    }

    public Throwable getCause()
    {
        return this.crashReport.getCrashCause();
    }

    public String getMessage()
    {
        return this.crashReport.getDescription();
    }
}