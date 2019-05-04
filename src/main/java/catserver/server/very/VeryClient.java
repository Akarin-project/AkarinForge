/*
 * Decompiled with CFR 0_119.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.google.gson.Gson
 */
package catserver.server.very;

import catserver.server.remapper.ReflectionUtils;
import catserver.server.very.UserInfo;
import catserver.server.very.VeryConfig;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.ProtectionDomain;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Timer;
import java.util.TimerTask;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

public final class VeryClient {
    public static VeryClient instance;
    private String server = "https://www.gamehh.cn/cussssss/";

    private int auth() {
        try {
            String string = "action=authsuserid=" + VeryConfig.userid + "skey=" + VeryConfig.key + "smac=";
            UserInfo userInfo = (UserInfo)new Gson().fromJson(this.sendRequest(string), UserInfo.class);
            if (UserInfo.instance == null) {
                UserInfo.instance = userInfo;
            }
            return userInfo.code;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return -1;
        }
    }

    private boolean keepAlive() {
        try {
            String string = this.sendRequest("action=keepAlive&token=" + UserInfo.instance.token);
            if (string.contains("invalidtoken")) {
                this.safeStopServer();
            }
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    private boolean logout() {
        try {
            this.sendRequest("action=logout&token=" + UserInfo.instance.token);
            return true;
        }
        catch (Exception exception) {
            exception.printStackTrace();
            return false;
        }
    }

    public static void startVeryService() throws Exception {
        instance = new VeryClient();
        VeryConfig.load();
        int n2 = instance.auth();
        switch (n2) {
            case 100: {
                if (UserInfo.instance.message != null && !UserInfo.instance.message.equals("")) {
                    System.out.println(UserInfo.instance.message);
                }
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    instance.logout();
                }
                ));
                new Timer().schedule(new TimerTask(){
                    int failCount = 0;

                    @Override
                    public void run() {
                        if (!VeryClient.instance.keepAlive()) {
                            ++this.failCount;
                            System.out.println("\u6388\u6743\u670d\u52a1\u5668\u5fc3\u8df3\u5305\u8fde\u63a5\u5931\u8d25,\u91cd\u8bd5\u6b21\u6570: %c/15".replace("%c", String.valueOf(this.failCount)));
                        } else {
                            this.failCount = 0;
                        }
                        if (this.failCount >= 15) {
                            VeryClient.instance.safeStopServer();
                        }
                    }
                }, 300000, 300000);
                byte[] arrby = Base64.getDecoder().decode(UserInfo.instance.clazz);
                Class class_ = ReflectionUtils.getUnsafe().defineClass("catserver.server.very.LaunchServer", arrby, 0, arrby.length, Thread.currentThread().getContextClassLoader(), null);
                class_.getMethod("launchServer", String.class).invoke(null, UserInfo.instance.token);
                break;
            }
            case 101: {
                System.out.println("\u6388\u6743\u5df2\u5230\u671f\u6216\u88ab\u9650\u5236!");
                break;
            }
            case 102: {
                System.out.println("\u8be5\u6388\u6743\u5df2\u5728\u5176\u4ed6IP\u4f7f\u7528,\u66f4\u6362IP\u8bf7\u7b49\u5f85\u4e00\u6bb5\u65f6\u95f4!");
                break;
            }
            default: {
                System.out.println("\u9a8c\u8bc1\u5931\u8d25,\u8bf7\u68c0\u67e5\u7f51\u7edc: " + n2);
                FMLCommonHandler.instance().exitJava(0, true);
            }
        }
    }

    private String getMACAddress() {
        ArrayList arrayList = Lists.newArrayList();
        try {
            Enumeration<NetworkInterface> enumeration = NetworkInterface.getNetworkInterfaces();
            while (enumeration.hasMoreElements()) {
                NetworkInterface networkInterface = enumeration.nextElement();
                if (networkInterface.isVirtual() || networkInterface.isLoopback() || networkInterface.isPointToPoint() || !networkInterface.isUp()) continue;
                try {
                    byte[] arrby = networkInterface.getHardwareAddress();
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i2 = 0; i2 < arrby.length; ++i2) {
                        String string = Integer.toHexString(arrby[i2] & 255);
                        stringBuffer.append(string.length() == 1 ? "" + 0 + string : string);
                    }
                    arrayList.add(stringBuffer.toString().toUpperCase());
                }
                catch (Exception exception) {}
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
        Collections.sort(arrayList);
        return Arrays.toString(arrayList.toArray(new String[0]));
    }

    public static SSLContext sslContextForTrustedCertificates() {
        TrustManager[] arrtrustManager = new TrustManager[1];
        miTM miTM2 = new miTM();
        arrtrustManager[0] = miTM2;
        SSLContext sSLContext = null;
        try {
            sSLContext = SSLContext.getInstance("SSL");
            sSLContext.init(null, arrtrustManager, null);
        }
        finally {
            return sSLContext;
        }
    }

    public String sendRequest(String string) throws Exception {
        String string2;
        HttpsURLConnection httpsURLConnection = (HttpsURLConnection)new URL(this.server + "t" + string).openConnection();
        SSLContext sSLContext = VeryClient.sslContextForTrustedCertificates();
        httpsURLConnection.setSSLSocketFactory(sSLContext.getSocketFactory());
        httpsURLConnection.setRequestProperty("accept", "*/*");
        httpsURLConnection.setRequestProperty("connection", "Close");
        httpsURLConnection.setRequestProperty("user-agent", "CatServer/VeryClient");
        httpsURLConnection.connect();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream(), StandardCharsets.UTF_8));
        String string3 = "";
        while ((string2 = bufferedReader.readLine()) != null) {
            string3 = string3 + string2;
        }
        return string3;
    }

    private void safeStopServer() {
        try {
            FMLLaunchHandler fMLLaunchHandler = (FMLLaunchHandler)ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, null, "INSTANCE");
            ClassLoader classLoader = (ClassLoader)ReflectionHelper.getPrivateValue(FMLLaunchHandler.class, fMLLaunchHandler, "classLoader");
            Class class_ = Class.forName("net.minecraft.server.MinecraftServer", false, classLoader);
            Object object = class_.getMethod("getServerInst", new Class[0]).invoke(null, new Object[0]);
        }
        catch (Exception exception) {
            exception.printStackTrace();
            FMLCommonHandler.instance().exitJava(0, false);
        }
    }

    static class miTM
    implements TrustManager,
    X509TrustManager {
        miTM() {
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return null;
        }

        public boolean isServerTrusted(X509Certificate[] arrx509Certificate) {
            return true;
        }

        public boolean isClientTrusted(X509Certificate[] arrx509Certificate) {
            return true;
        }

        @Override
        public void checkServerTrusted(X509Certificate[] arrx509Certificate, String string) throws CertificateException {
        }

        @Override
        public void checkClientTrusted(X509Certificate[] arrx509Certificate, String string) throws CertificateException {
        }
    }

}

