package nl.Naomiora.privateproject.wandsmodule.wands.loader;

import sun.reflect.ReflectionFactory;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WandAPILoader<T> {

    public List<T> loadClass(Class<?> classType, File directory, String folderName) {
        ArrayList<T> loadableAddons = new ArrayList<>();
        File addonsDirector = new File(directory, folderName);
        if (!addonsDirector.exists())
            addonsDirector.mkdirs();
        ArrayList<File> files = new ArrayList<>(Arrays.asList(Objects.requireNonNull(addonsDirector.listFiles(new FileExtensionFilter(".jar")))));
        List<URL> urls = new ArrayList<>();
        for (File file : files) {
            try {
                urls.add(file.toURI().toURL());
            }
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }
        ClassLoader loader = URLClassLoader.newInstance(urls.toArray(new URL[0]), this.getClass().getClassLoader());

        try (Stream<Path> walk = Files.walk(Paths.get(addonsDirector.toURI()))) {
            List<String> addonFilePaths = walk.map(Path::toString)
                    .filter(f -> f.endsWith(".jar")).collect(Collectors.toList());
            addonFilePaths.forEach(addonFiles -> {
                try {
                    JarFile jarFile = new JarFile(new File(addonFiles));
                    Enumeration<JarEntry> entries = jarFile.entries();

                    while (entries.hasMoreElements()) {
                        JarEntry jarEntry = entries.nextElement();
                        if (!jarEntry.getName().endsWith(".class")) continue;
                        String className = jarEntry.getName().replace("/", ".").substring(0, jarEntry.getName().length() - 6);
                        Class<?> clazz = null;
                        try {
                            clazz = Class.forName(className, true, loader);
                        } catch(Exception e) {
                            e.printStackTrace();
                        }

                        if (clazz == null || !classType.isAssignableFrom(clazz) || clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers()))
                            continue;

                        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
                        Constructor<?> objDef = classType.getDeclaredConstructor();
                        Constructor<?> intConstr = rf.newConstructorForSerialization(clazz, objDef);
                        T loadable = (T) clazz.cast(intConstr.newInstance());

                        loadableAddons.add(loadable);
                    }
                } catch (IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return loadableAddons;
    }

    public final class FileExtensionFilter implements FileFilter {

        private final String extension;

        /**
         * Creates a new FileExtensionFilter.
         *
         * @param extension the extension to filter for
         */
        public FileExtensionFilter(String extension) {
            this.extension = extension;
        }

        @Override
        public boolean accept(File file) {
            return file.getName().endsWith(extension);
        }
    }
}
