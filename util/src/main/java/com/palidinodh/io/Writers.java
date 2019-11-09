package com.palidinodh.io;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import com.palidinodh.rs.setting.Settings;
import com.palidinodh.util.PLogger;
import com.palidinodh.util.PTime;

public class Writers {
  public static void writeFile(File file, byte[] bytes) {
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }
    try (DataOutputStream out =
        new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)))) {
      out.write(bytes);
    } catch (Exception e) {
      PLogger.error(e);
    }
  }

  public static void writeTextFile(File file, boolean append, String... lines) {
    if (file.getParentFile() != null) {
      file.getParentFile().mkdirs();
    }
    try (BufferedWriter out = new BufferedWriter(new FileWriter(file, append))) {
      for (String line : lines) {
        if (line == null) {
          continue;
        }
        out.write(line);
        out.newLine();
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
  }

  public static void writeTextFile(File file, String... lines) {
    writeTextFile(file, false, lines);
  }

  public static void appendTextFile(File file, String... lines) {
    writeTextFile(file, true, lines);
  }

  public static void writeLog(File baseDirectory, String filename, String line) {
    if (Settings.getInstance().isLocal()) {
      return;
    }
    appendTextFile(new File(new File(baseDirectory, PTime.getYearMonthDay()), filename),
        PTime.getFullDate() + " " + line + System.getProperty("line.separator"));
  }

  public static void writePlayerLog(String filename, String line) {
    writeLog(Settings.getInstance().getPlayerLogsDirectory(), filename, line);
  }

  public static byte[] serialize(Object object) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream out2 = new ObjectOutputStream(out)) {
      out2.writeObject(object);
      return out.toByteArray();

    } catch (Exception e) {
      PLogger.error(e);
    }
    return null;
  }

  public static boolean isSerializable(Object object) {
    boolean success = true;
    if (object instanceof Map) {
      Map<?, ?> map = (Map<?, ?>) object;
      for (Map.Entry<?, ?> e : map.entrySet()) {
        byte[] serialized = serialize(e.getValue());
        if (serialized == null) {
          PLogger.error("Serialization failure: " + e.getKey() + ", " + e.getValue());
          success = false;
        }
      }
    } else if (object instanceof List) {
      List<?> list = (List<?>) object;
      for (Object e : list) {
        byte[] serialized = serialize(e);
        if (serialized == null) {
          PLogger.error("Serialization failure: " + e);
          success = false;
        }
      }
    } else {
      byte[] serialized = serialize(object);
      if (serialized == null) {
        PLogger.error("Serialization failure: " + object);
        success = false;
      }
    }
    return success;
  }

  public static byte[] gzCompress(byte[] bytes) {
    try (ByteArrayOutputStream out = new ByteArrayOutputStream();
        GZIPOutputStream gOut = new GZIPOutputStream(out)) {
      out.write(bytes);
      return out.toByteArray();
    } catch (Exception e) {
      PLogger.error(e);
    }
    return null;
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
              PLogger.error(e2);
            }
          });
        }
      }
    } catch (Exception e) {
      PLogger.error(e);
    }
  }
}
