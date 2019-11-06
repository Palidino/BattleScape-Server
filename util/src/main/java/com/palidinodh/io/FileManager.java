package com.palidinodh.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;
import com.google.common.reflect.ClassPath.ClassInfo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PCollection;
import com.palidinodh.util.PLogger;
import com.palidinodh.util.PTime;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.reflection.PureJavaReflectionProvider;

public class FileManager implements Runnable {
  public static final String JSON_DIR = "/json";
  public static final String XML_DIR = "/xml";
  public static final String JS_DIR = "/javascript";
  private static final XStream XSTREAM_IN, XSTREAM_OUT;

  private static FileManager instance = new FileManager();
  private static Connection sqlConnection;
  private static Properties sqlProperties = new Properties();
  private static Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();

  private List<Request> requests = new ArrayList<>();

  @Override
  public void run() {
    List<Request> active = new ArrayList<>();
    while (true) {
      try {
        active.clear();
        if (!requests.isEmpty()) {
          synchronized (this) {
            active.addAll(requests);
            requests.clear();
          }
        }
        for (Request request : active) {
          if (request instanceof SaveRequest) {
            SaveRequest saveRequest = (SaveRequest) request;
            FileManager.writeFile(saveRequest.fileName, saveRequest.fileBytes);
          } else if (request instanceof DeleteRequest) {
            DeleteRequest deleteRequest = (DeleteRequest) request;
            new File(deleteRequest.fileName).delete();
          }
        }
        synchronized (this) {
          try {
            wait(600);
          } catch (InterruptedException ie) {
          }
        }
      } catch (Exception e) {
        PLogger.error(e);
      }
    }
  }

  public void addSaveRequest(File file, byte[] fileBytes) {
    synchronized (this) {
      requests.add(new SaveRequest(file.getPath(), fileBytes));
      notify();
    }
  }

  public void addDeleteRequest(File file) {
    synchronized (this) {
      requests.add(new DeleteRequest(file.getPath()));
      notify();
    }
  }

  public void printStats() {
    PLogger.println("FileManager: requests: " + requests.size());
  }

  public static void loadSql() {
    sqlProperties.setProperty("user", Settings.getInstance().getSqlConnection().getUsername());
    sqlProperties.setProperty("password", Settings.getInstance().getSqlConnection().getPassword());
    sqlProperties.setProperty("connectTimeout", "5000");
    sqlProperties.setProperty("socketTimeout", "5000");
    getSqlConnection();
  }

  public static Connection getSqlConnection() {
    try {
      DriverManager.setLoginTimeout(10);
      if (sqlConnection == null || !sqlConnection.isValid(10)) {
        sqlConnection = DriverManager.getConnection(
            "jdbc:mysql://" + Settings.getInstance().getSqlConnection().getConnectionIP() + ":"
                + Settings.getInstance().getSqlConnection().getConnectionPort() + "/"
                + Settings.getInstance().getSqlConnection().getDatabaseName()
                + "?zeroDateTimeBehavior=convertToNull&serverTimezone=America/New_York",
            sqlProperties);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return sqlConnection;
  }

  public static boolean fileExists(String file) {
    return new File(file).exists();
  }

  public static byte[] readFile(String file) {
    return readFile(new File(file));
  }

  public static byte[] readFile(File f) {
    byte[] bytes = null;
    DataInputStream in = null;
    try {
      in = new DataInputStream(new BufferedInputStream(new FileInputStream(f)));
      bytes = new byte[(int) f.length()];
      in.readFully(bytes);
    } catch (FileNotFoundException fne) {
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        in.close();
      } catch (Exception e) {
      }
    }
    return bytes;
  }

  public static void writeFile(String file, byte[] bytes) {
    writeFile(new File(file), bytes);
  }

  public static void writeFile(File f, byte[] bytes) {
    DataOutputStream out = null;
    try {
      if (f.getParentFile() != null) {
        f.getParentFile().mkdirs();
      }
      out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(f)));
      out.write(bytes);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        out.close();
      } catch (Exception e) {
      }
    }
  }

  public static String readTextFile(String file) {
    return readTextFile(new File(file));
  }

  public static String readTextFile(File f) {
    byte[] bytes = readFile(f);
    return bytes != null ? new String(bytes) : null;
  }

  public static String[] readTextFileArray(String file) {
    return readTextFileArray(new File(file));
  }

  public static String[] readTextFileArray(File f) {
    return readTextFileList(f).toArray(new String[0]);
  }

  public static List<String> readTextFileList(String file) {
    return readTextFileList(new File(file));
  }

  public static List<String> readTextFileList(File f) {
    String text = readTextFile(f);
    if (text == null) {
      return null;
    }
    return PCollection.toList(text.replace("\r", "").split("\n"));
  }

  public static void writeTextFile(String file, List<String> lines) {
    writeTextFile(new File(file), false, lines.toArray(new String[0]));
  }

  public static void writeTextFile(File f, List<String> lines) {
    writeTextFile(f, false, lines.toArray(new String[0]));
  }

  public static void writeTextFile(String file, String... lines) {
    writeTextFile(new File(file), false, lines);
  }

  public static void writeTextFile(File f, String... lines) {
    writeTextFile(f, false, lines);
  }

  public static void appendTextFile(String file, List<String> lines) {
    writeTextFile(new File(file), true, lines.toArray(new String[0]));
  }

  public static void appendTextFile(File f, List<String> lines) {
    writeTextFile(f, true, lines.toArray(new String[0]));
  }

  public static void appendTextFile(String file, String... lines) {
    writeTextFile(new File(file), true, lines);
  }

  public static void appendTextFile(File f, String... lines) {
    writeTextFile(f, true, lines);
  }

  public static void writeTextFile(File f, boolean append, String... lines) {
    BufferedWriter out = null;
    try {
      out = new BufferedWriter(new FileWriter(f, append));
      for (String line : lines) {
        if (line == null) {
          continue;
        }
        out.write(line);
        out.newLine();
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (Exception e) {
      }
    }
  }

  public static void writeLog(File baseDirectory, String filename, String line) {
    if (Settings.getInstance().isLocal()) {
      return;
    }
    if (baseDirectory == null || filename == null) {
      return;
    }
    line = PTime.getFullDate() + " " + line + System.getProperty("line.separator");
    RandomAccessFile out = null;
    try {
      File fullFile = new File(new File(baseDirectory, PTime.getYearMonthDay()), filename);
      if (fullFile.getParentFile() != null) {
        fullFile.getParentFile().mkdirs();
      }
      out = new RandomAccessFile(fullFile, "rw");
      out.seek(out.length());
      out.write(line.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (out != null) {
          out.close();
        }
      } catch (Exception e) {
      }
    }
  }

  public static Object readObject(byte[] bytes) {
    if (bytes == null) {
      return null;
    }
    Object object = null;
    DecompressibleInputStream oStream = null;
    try {
      oStream = new DecompressibleInputStream(new ByteArrayInputStream(bytes));
      object = oStream.readObject();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        if (oStream != null) {
          oStream.close();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return object;
  }

  public static byte[] objectStreamBuffer(Object object) {
    byte[] bytes = null;
    ByteArrayOutputStream bStream = null;
    ObjectOutputStream oStream = null;
    try {
      bStream = new ByteArrayOutputStream();
      oStream = new ObjectOutputStream(bStream);
      oStream.writeObject(object);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (oStream != null) {
          oStream.close();
        }
        if (bStream != null) {
          bStream.close();
          bytes = bStream.toByteArray();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return bytes;
  }

  public static boolean isSerializable(Object object) {
    boolean hasFailed = false;
    if (object instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) object;
      for (Map.Entry<?, ?> e : map.entrySet()) {
        byte[] serialized = objectStreamBuffer(e.getValue());
        if (serialized == null) {
          PLogger.println("Serialization failure: " + e.getKey() + ", " + e.getValue());
          hasFailed = true;
        }
      }
    } else if (object instanceof List) {
      List<?> list = (List<?>) object;
      for (Object e : list) {
        byte[] serialized = objectStreamBuffer(e);
        if (serialized == null) {
          PLogger.println("Serialization failure: " + e);
          hasFailed = true;
        }
      }
    } else {
      byte[] serialized = objectStreamBuffer(object);
      if (serialized == null) {
        PLogger.println("Serialization failure: " + object);
        hasFailed = true;
      }
    }
    return !hasFailed;
  }

  public static byte[] gzDecompress(byte[] b) {
    byte[] decompressed = null;
    ByteArrayOutputStream bStream = null;
    GZIPInputStream gStream = null;
    try {
      bStream = new ByteArrayOutputStream();
      gStream = new GZIPInputStream(new ByteArrayInputStream(b));
      byte[] buffer = new byte[1024];
      int length;
      while ((length = gStream.read(buffer)) != -1) {
        bStream.write(buffer, 0, length);
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (gStream != null) {
          gStream.close();
        }
        if (bStream != null) {
          bStream.close();
          decompressed = bStream.toByteArray();
        }
      } catch (Exception e) {
      }
    }
    return decompressed;
  }

  public static byte[] gzCompress(byte[] b) {
    byte[] compressed = null;
    ByteArrayOutputStream bStream = null;
    GZIPOutputStream gStream = null;
    try {
      bStream = new ByteArrayOutputStream();
      gStream = new GZIPOutputStream(bStream);
      gStream.write(b, 0, b.length);
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (gStream != null) {
          gStream.close();
        }
        if (bStream != null) {
          bStream.close();
          compressed = bStream.toByteArray();
        }
      } catch (Exception e) {
      }
    }
    return compressed;
  }

  public static void zip(File zipFile, File... files) {
    try {
      Path zipFilePath = zipFile.toPath();
      if (Files.exists(zipFilePath)) {
        Files.delete(zipFilePath);
      }
      Path p = Files.createFile(zipFilePath);
      try (ZipOutputStream zs = new ZipOutputStream(Files.newOutputStream(p))) {
        for (File file : files) {
          if (!file.exists()) {
            continue;
          }
          Path pp = file.toPath();
          Files.walk(pp).filter(path -> !Files.isDirectory(path)).forEach(path -> {
            if (pp.relativize(path).toString().startsWith(".")) {
              return;
            }
            ZipEntry zipEntry =
                new ZipEntry(pp.getFileName().toString() + "/" + pp.relativize(path).toString());
            try {
              zs.putNextEntry(zipEntry);
              Files.copy(path, zs);
              zs.closeEntry();
            } catch (IOException e2) {
              e2.printStackTrace();
            }
          });
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static <T> T fromJson(File file, Class<T> type) {
    try (Reader reader = new FileReader(file)) {
      return fromJson(reader, type);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T fromJson(File file, TypeToken<T> type) {
    try (Reader reader = new FileReader(file)) {
      return fromJson(reader, type);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T fromJson(InputStream in, Class<T> type) {
    try (InputStreamReader reader = new InputStreamReader(in, "UTF-8")) {
      return fromJson(reader, type);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T fromJson(InputStream in, TypeToken<T> type) {
    try (InputStreamReader reader = new InputStreamReader(in, "UTF-8")) {
      return fromJson(reader, type);
    } catch (IOException e) {
      return null;
    }
  }

  public static <T> T fromJson(Reader reader, Class<T> type) {
    return gson.fromJson(reader, type);
  }

  public static <T> T fromJson(Reader reader, TypeToken<T> type) {
    return gson.fromJson(reader, type.getType());
  }

  public static void toJson(File file, Object src) {
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }
    try (Writer writer = new FileWriter(file)) {
      gson.toJson(src, writer);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static <T> T fromJson(String src, Class<T> type) {
    return gson.fromJson(src, (Type) type);
  }

  public static String toJson(Object src) {
    return gson.toJson(src);
  }

  public static String toJson(Object src, Type type) {
    return gson.toJson(src, type);
  }

  public static Object loadXML(String file) {
    return loadXML(new File(file));
  }

  public static Object loadXML(File file) {
    try {
      return XSTREAM_IN.fromXML(new BufferedInputStream(new FileInputStream(file)));
    } catch (FileNotFoundException fne) {
      return null;
    }
  }

  public static Object loadXML(InputStream in) {
    try {
      return XSTREAM_IN.fromXML(new BufferedInputStream(in));
    } catch (Exception fne) {
      return null;
    }
  }

  public static void saveXML(String file, Object o) {
    saveXML(new File(file), o);
  }

  public static void saveXML(File file, Object o) {
    try {
      XSTREAM_OUT.toXML(o, new BufferedOutputStream(new FileOutputStream(file)));
    } catch (Exception e) {
    }
  }

  public static void addXMLAlias(String name, Class<?> c) {
    XSTREAM_IN.alias(name, c);
    XSTREAM_OUT.alias(name, c);
  }

  public static String openURL(String urlS) {
    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    StringBuilder builder = new StringBuilder("");
    try {
      URL url = new URL(urlS);
      urlConnection = (HttpURLConnection) url.openConnection();
      urlConnection.setRequestMethod("GET");
      urlConnection.setRequestProperty("Connection", "close");
      urlConnection.setConnectTimeout(10000);
      urlConnection.setReadTimeout(10000);
      urlConnection.addRequestProperty("User-Agent",
          "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:49.0) Gecko/20100101 Firefox/49.0");
      urlConnection.connect();
      reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
      String line = null;
      while ((line = reader.readLine()) != null) {
        builder.append(line + "\\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      if (reader != null) {
        try {
          reader.close();
        } catch (Exception e) {
        }
      }
      if (urlConnection != null) {
        urlConnection.disconnect();
      }
    }
    return builder.toString();
  }

  public static List<String> getResourceFiles(Class<?> fromClass, String directory) {
    List<String> filenames = new ArrayList<>();
    try {
      URL url = fromClass.getResource(directory);
      url = url == null ? FileManager.class.getResource(directory) : url;
      URI uri = url.toURI();
      if ("jar".equals(uri.getScheme())) {
        URL jar = FileManager.class.getProtectionDomain().getCodeSource().getLocation();
        Path jarFile = Paths.get(jar.toURI());
        try (FileSystem fileSystem = FileSystems.newFileSystem(jarFile, null)) {
          DirectoryStream<Path> directoryStream =
              Files.newDirectoryStream(fileSystem.getPath(directory));
          for (Path path : directoryStream) {
            filenames.add(path.toString());
          }
        }
      } else {
        try (InputStream in = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"))) {
          String resource;
          while ((resource = br.readLine()) != null) {
            filenames.add(new File(directory, resource).getPath());
          }
        }
      }
    } catch (Exception e) {
    }
    return filenames;
  }

  public static List<Class<?>> getClasses(String packageName) {
    List<Class<?>> matchedClasses = new ArrayList<>();
    try {
      ClassPath classPath = ClassPath.from(FileManager.class.getClassLoader());
      ImmutableSet<ClassInfo> classes = packageName == null ? classPath.getAllClasses()
          : classPath.getTopLevelClassesRecursive(packageName);
      for (ClassInfo classInfo : classes) {
        Class<?> clazz = classInfo.load();
        matchedClasses.add((Class<?>) clazz);
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
    return matchedClasses;
  }

  @SuppressWarnings("unchecked")
  public static <T> List<Class<T>> getClasses(Class<T> fromClass, String packageName) {
    List<Class<T>> matchedClasses = new ArrayList<>();
    try {
      ClassPath classPath = ClassPath.from(fromClass.getClassLoader());
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
    return matchedClasses;
  }

  public static byte[] readStream(InputStream in) {
    byte[] bytes = new byte[128];
    ByteArrayOutputStream bStream = null;
    try {
      bStream = new ByteArrayOutputStream();
      while (in.available() != -1) {
        int length = in.read(bytes);
        if (length < 0) {
          break;
        }
        bStream.write(bytes, 0, length);
      }
    } catch (FileNotFoundException fne) {
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      try {
        if (in != null) {
          in.close();
        }
        if (bStream != null) {
          bStream.close();
          bytes = bStream.toByteArray();
        }
      } catch (Exception e) {
      }
    }
    return bytes;
  }

  public static FileManager getInstance() {
    return instance;
  }

  public static void setGson(Gson _gson) {
    gson = _gson;
  }

  public interface Request {
  }

  public static class SaveRequest implements Request {
    String fileName;
    byte[] fileBytes;

    public SaveRequest(String fileName, byte[] fileBytes) {
      this.fileName = fileName;
      this.fileBytes = fileBytes;
    }
  }

  public static class DeleteRequest implements Request {
    String fileName;

    public DeleteRequest(String fileName) {
      this.fileName = fileName;
    }
  }

  static {
    XSTREAM_IN = new XStream(new PureJavaReflectionProvider());
    XSTREAM_IN.allowTypesByWildcard(new String[] {"com.palidinodh.**"});
    XSTREAM_OUT = new XStream(new PureJavaReflectionProvider());
    XSTREAM_OUT.allowTypesByWildcard(new String[] {"com.palidinodh.**"});
  }
}
