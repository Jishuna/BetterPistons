package me.jishuna.betterpistons.nms.v1_20_R1;

import java.io.File;
import java.io.FileOutputStream;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.instrument.Instrumentation;
import java.security.ProtectionDomain;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

import me.jishuna.betterpistons.Utils;

public class InjectBootstrapTransformer implements ClassFileTransformer {
    private static final String BOOTSTRAP_NAME = Bootstraper.class.getCanonicalName().replace('.', '/');

    private final Instrumentation instrumentation;
    private final File folder;

    public InjectBootstrapTransformer(Instrumentation instrumentation, File folder) {
        this.instrumentation = instrumentation;
        this.folder = folder;
    }

    @Override
    public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined, ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
        if (loader.equals(ClassLoader.getSystemClassLoader())) {
            return classfileBuffer;
        }

        if (className.equals(BOOTSTRAP_NAME)) {
            injectClass(className, classfileBuffer);
        }
        return classfileBuffer;
    }

    private void injectClass(String name, byte[] b) {
        try {
            if (!this.folder.exists()) {
                this.folder.mkdirs();
            }

            File jar = new File(folder, "bootstrap.jar");
            jar.createNewFile();

            JarOutputStream jarOutputStream = new JarOutputStream(new FileOutputStream(jar));
            jarOutputStream.putNextEntry(new JarEntry(name + ".class"));
            jarOutputStream.write(b);
            jarOutputStream.close();

            JarFile jarFile = new JarFile(jar, false);
            this.instrumentation.appendToSystemClassLoaderSearch(jarFile);

            if (Utils.isPaper()) { // Only required on paper
                this.instrumentation.appendToBootstrapClassLoaderSearch(jarFile);
            }
            jarFile.close();

            folder.deleteOnExit();
            jar.deleteOnExit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
