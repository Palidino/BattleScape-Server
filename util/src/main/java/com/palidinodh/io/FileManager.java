package com.palidinodh.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
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
            Writers.writeFile(new File(saveRequest.fileName), saveRequest.fileBytes);
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
    XSTREAM_IN.allowTypesByWildcard(new String[] { "com.palidinodh.**" });
    XSTREAM_OUT = new XStream(new PureJavaReflectionProvider());
    XSTREAM_OUT.allowTypesByWildcard(new String[] { "com.palidinodh.**" });
  }
}
