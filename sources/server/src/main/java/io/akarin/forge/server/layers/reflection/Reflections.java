package io.akarin.forge.server.layers.reflection;

public class Reflections {
    private static final TraceSecurityManager SECURITY_MANAGER = new TraceSecurityManager();
    
    public static ClassLoader getCallerClassloader() {
        return Reflections.getCallerClass(3).getClassLoader();
    }

    private static Class<?> getCallerClass(int skip) {
        return SECURITY_MANAGER.getCallerClass(skip);
    }

    private static class TraceSecurityManager extends java.lang.SecurityManager {
        public Class<?> getCallerClass(int skip) {
            return this.getClassContext()[skip + 1];
        }
    }
}
