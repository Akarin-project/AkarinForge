package net.minecraft.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.BufferUtils;

@SideOnly(Side.CLIENT)
public class ScreenShotHelper
{
    private static final Logger LOGGER = LogManager.getLogger();
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd_HH.mm.ss");
    private static IntBuffer pixelBuffer;
    private static int[] pixelValues;

    public static ITextComponent saveScreenshot(File gameDirectory, int width, int height, Framebuffer buffer)
    {
        return saveScreenshot(gameDirectory, (String)null, width, height, buffer);
    }

    public static ITextComponent saveScreenshot(File gameDirectory, @Nullable String screenshotName, int width, int height, Framebuffer buffer)
    {
        try
        {
            File file1 = new File(gameDirectory, "screenshots");
            file1.mkdir();
            BufferedImage bufferedimage = createScreenshot(width, height, buffer);
            File file2;

            if (screenshotName == null)
            {
                file2 = getTimestampedPNGFileForDirectory(file1);
            }
            else
            {
                file2 = new File(file1, screenshotName);
            }

            file2 = file2.getCanonicalFile(); // FORGE: Fix errors on Windows with paths that include \.\
            net.minecraftforge.client.event.ScreenshotEvent event = net.minecraftforge.client.ForgeHooksClient.onScreenshot(bufferedimage, file2);
            if (event.isCanceled()) return event.getCancelMessage(); else file2 = event.getScreenshotFile();
            ImageIO.write(bufferedimage, "png", file2);
            ITextComponent itextcomponent = new TextComponentString(file2.getName());
            itextcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_FILE, file2.getAbsolutePath()));
            itextcomponent.getStyle().setUnderlined(Boolean.valueOf(true));
            if (event.getResultMessage() != null) return event.getResultMessage();
            return new TextComponentTranslation("screenshot.success", new Object[] {itextcomponent});
        }
        catch (Exception exception)
        {
            LOGGER.warn("Couldn't save screenshot", (Throwable)exception);
            return new TextComponentTranslation("screenshot.failure", new Object[] {exception.getMessage()});
        }
    }

    public static BufferedImage createScreenshot(int width, int height, Framebuffer framebufferIn)
    {
        if (OpenGlHelper.isFramebufferEnabled())
        {
            width = framebufferIn.framebufferTextureWidth;
            height = framebufferIn.framebufferTextureHeight;
        }

        int i = width * height;

        if (pixelBuffer == null || pixelBuffer.capacity() < i)
        {
            pixelBuffer = BufferUtils.createIntBuffer(i);
            pixelValues = new int[i];
        }

        GlStateManager.glPixelStorei(3333, 1);
        GlStateManager.glPixelStorei(3317, 1);
        pixelBuffer.clear();

        if (OpenGlHelper.isFramebufferEnabled())
        {
            GlStateManager.bindTexture(framebufferIn.framebufferTexture);
            GlStateManager.glGetTexImage(3553, 0, 32993, 33639, pixelBuffer);
        }
        else
        {
            GlStateManager.glReadPixels(0, 0, width, height, 32993, 33639, pixelBuffer);
        }

        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, width, height);
        BufferedImage bufferedimage = new BufferedImage(width, height, 1);
        bufferedimage.setRGB(0, 0, width, height, pixelValues, 0, width);
        return bufferedimage;
    }

    private static File getTimestampedPNGFileForDirectory(File gameDirectory)
    {
        String s = DATE_FORMAT.format(new Date()).toString();
        int i = 1;

        while (true)
        {
            File file1 = new File(gameDirectory, s + (i == 1 ? "" : "_" + i) + ".png");

            if (!file1.exists())
            {
                return file1;
            }

            ++i;
        }
    }
}