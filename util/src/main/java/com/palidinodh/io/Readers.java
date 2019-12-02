package com.palidinodh.io;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Constructor;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PLogger;

public class Readers {
  public static byte[] readFile(File file) {
    if (!file.exists()) {
      return null;
    }
    byte[] bytes = null;
    try (DataInputStream in =
        new DataInputStream(new BufferedInputStream(new FileInputStream(file)))) {
      bytes = new byte[(int) file.length()];
      in.readFully(bytes);
    } catch (Exception e) {
      PLogger.error(e);
    }
    return bytes;
  }

  public static String readTextFile(File file) {
    byte[] bytes = readFile(file);
    return bytes != null ? new String(bytes) : null;
  }

  public static String[] readTextFileArray(File file) {
    List<String> list = readTextFileList(file);
    return list != null ? list.toArray(new String[0]) : null;
  }

  public static List<String> readTextFileList(File file) {
    String text = readTextFile(file);
    return text != null ? PCollection.toList(text.replace("\r", "").split("\n")) : null;
  }

  public static Object readObject(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Object object = null;
    try (DecompressibleInputStream in =
        new DecompressibleInputStream(new ByteArrayInputStream(bytes))) {
      object = in.readObject();
    } catch (Exception e) {
      PLogger.error(e);
    }
    return object;
  }

  public static byte[] gzDecompress(byte[] bytes) {
    try (GZIPInputStream gIn = new GZIPInputStream(new ByteArrayInputStream(bytes));
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = gIn.read(buffer)) != -1) {
        out.write(buffer, 0, length);
      }
      out.flush();
      return out.toByteArray();
    } catch (Exception e) {
      PLogger.error(e);
    }
    return null;
  }

  public static byte[] readStream(InputStream fromStream) {
    try (InputStream in = new BufferedInputStream(fromStream);
        ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      byte[] buffer = new byte[1024];
      int length;
      while ((length = in.read(buffer)) != -1) {
        out.write(buffer, 0, length);
      }
      out.flush();
      return out.toByteArray();
    } catch (Exception e) {
      PLogger.error(e);
    }
    return null;
  }

  public static String readUrl(String string) {
    HttpURLConnection urlConnection;
    try {
      urlConnection = (HttpURLConnection) new URL(string).openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setRequestProperty("Connection", "close");
      urlConnection.setConnectTimeout(10000);
      urlConnection.setReadTimeout(10000);
      urlConnection.addRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
      urlConnection.connect();
    } catch (Exception e) {
      PLogger.error(e);
      return null;
    }
    try (BufferedReader reader =
        new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
      String line = null;
      StringBuilder builder = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        builder.append(line + "\\n");
      }
      return builder.toString();
    } catch (Exception e) {
      PLogger.error(e);
    }
    return null;
  }

  public static InputStream getResourceAsStream(String filename) {
    return getResourceAsStream(Readers.class, filename);
  }

  public static InputStream getResourceAsStream(Class<?> fromClass, String filename) {
    return fromClass.getResourceAsStream(filename.replace("\\", "/"));
  }

  public static List<String> getResourceList(String pathName) {
    return getResourceList(Readers.class, pathName);
  }

  public static List<String> getResourceList(Class<?> fromClass, String pathName) {
    List<String> filenames = new ArrayList<>();
    try {
      URL url = fromClass.getResource(pathName);
      url = url == null ? Readers.class.getClassLoader().getResource(pathName) : url;
      URI uri = url.toURI();
      if ("jar".equals(uri.getScheme())) {
        String jarUrlString = url.toString().replace("jar:", "");
        jarUrlString = jarUrlString.substring(0, jarUrlString.indexOf("!"));
        URL jar = new URL(jarUrlString);
        Path jarFile = Paths.get(jar.toURI());
        try (FileSystem fileSystem = FileSystems.newFileSystem(jarFile, null)) {
          DirectoryStream<Path> directoryStream =
              Files.newDirectoryStream(fileSystem.getPath(pathName));
          for (Path path : directoryStream) {
            filenames.add(path.toString());
          }
        }
      } else {
        try (InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
          String resource;
          while ((resource = br.readLine()) != null) {
            filenames.add(new File(pathName, resource).getPath());
          }
        }
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
    return filenames;
  }

  public static <T> T newInstance(Class<T> fromClass) {
    return AccessController.doPrivileged(new PrivilegedAction<T>() {
      @Override
      public T run() {
        try {
          Constructor<T> constructor = fromClass.getDeclaredConstructor();
          constructor.setAccessible(true);
          return constructor.newInstance();
        } catch (Exception e) {
          PLogger.error(fromClass.getName(), e);
          return null;
        }
      }
    });
  }

  public static Class<?> getClass(String className) {
    return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
      @Override
      public Class<?> run() {
        try {
          return Class.forName(className);
        } catch (Exception e) {
          PLogger.error(e);
          return null;
        }
      }
    });
  }

  public static Class<?> getScriptClass(String className) {
    return getClass(Settings.getInstance().getScriptPackage() + "." + className);
  }

  @SuppressWarnings("unchecked")
  public static <T> List<Class<T>> getClasses(Class<T> fromClass, String packageName) {
    List<Class<T>> matchedClasses = new ArrayList<>();
    AccessController.doPrivileged(new PrivilegedAction<Boolean>() {
      @Override
      public Boolean run() {
        try {
          ClassPath classPath =
              ClassPath.from(fromClass == Object.class ? Readers.class.getClassLoader()
                  : fromClass.getClassLoader());
          ImmutableSet<ClassInfo> classes = packageName == null ? classPath.getAllClasses()
              : classPath.getTopLevelClassesRecursive(packageName);
          for (ClassInfo classInfo : classes) {
            Class<?> clazz = classInfo.load();
            if (fromClass != Object.class && !fromClass.isAssignableFrom(clazz)) {
              continue;
            }
            matchedClasses.add((Class<T>) clazz);
          }
        } catch (IOException ioe) {
          ioe.printStackTrace();
        }
        return Boolean.TRUE;
      }
    });
    return matchedClasses;
  }

  public static List<Class<Object>> getClasses(String packageName) {
    return getClasses(Object.class, packageName);
  }

  public static <T> List<Class<T>> getScriptClasses(Class<T> fromClass, String packageName) {
    if (!packageName.startsWith(Settings.getInstance().getScriptPackage())) {
      packageName = Settings.getInstance().getScriptPackage() + "." + packageName;
    }
    return getClasses(fromClass, packageName);
  }
}
