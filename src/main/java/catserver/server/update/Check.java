/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  catserver.server.very.SSLManager
 *  org.apache.commons.io.FileUtils
 *  org.apache.commons.io.IOUtils
 */
package catserver.server.update;

import catserver.server.CatServer;
import catserver.server.very.SSLManager;
import catserver.server.very.VeryConfig;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLConnection;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

public class Check
extends TimerTask {
    private String server = "https://www.gamehh.cn/cussssss/";

    @Override
    public void run() {
        try {
            String n2 = this.sendRequest("action=buildTime");
            int buildTime = Integer.parseInt(n2);
            if (buildTime > CatServer.buildTime) {
                System.out.println("\u68c0\u6d4b\u5230CatServer\u7248\u672c\u66f4\u65b0,\u8bf7\u91cd\u65b0\u6253\u5f00\u6784\u5efa\u5de5\u5177\u8fdb\u884c\u6784\u5efa\u5373\u53ef,\u6700\u65b0\u7248: " + n2);
            }
        }
        catch (Exception e2) {
            e2.printStackTrace();
        }
    }

    public String sendRequest(String parms) throws Exception {
        String line;
        HttpsURLConnection connection = (HttpsURLConnection)new URL(this.server + "t" + parms).openConnection();
        connection.setSSLSocketFactory(SSLManager.getSocketFactory());
        connection.setRequestProperty("accept", "*/*");
        connection.setRequestProperty("connection", "Close");
        connection.setRequestProperty("user-agent", "CatServer/VeryClient");
        connection.connect();
        BufferedReader in2 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String result = "";
        while ((line = in2.readLine()) != null) {
            result = result + line;
        }
        return result;
    }

    public void check() {
        this.run();
    }

    public static native byte[] updateVersion(byte[] var0);

    static {
        try {
            InputStream reader = VeryConfig.class.getClassLoader().getResourceAsStream("libs/libCatVLib.dll");
            byte[] dllBuff = IOUtils.readFully((InputStream)reader, (int)reader.available());
            File dllFile = new File("libCatVLib.dll");
            FileUtils.writeByteArrayToFile((File)dllFile, (byte[])dllBuff);
            reader = VeryConfig.class.getClassLoader().getResourceAsStream("libs/libCatVLib.so");
            dllBuff = IOUtils.readFully((InputStream)reader, (int)reader.available());
            dllFile = new File("libCatVLib.so");
            FileUtils.writeByteArrayToFile((File)dllFile, (byte[])dllBuff);
        }
        catch (IOException e2) {
            e2.printStackTrace();
        }
        try {
            String os2 = System.getProperty("os.name");
            String fName = os2.toLowerCase().startsWith("win") ? ".dll" : ".so";
            File file = new File("libCatVLib" + fName);
            System.load(file.getCanonicalPath());
        }
        catch (Throwable fName) {
            // empty catch block
        }
    }
}

