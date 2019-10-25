package com.palidinodh.io;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.Invocable;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import com.palidinodh.util.PLogger;

public class ScriptManager {
  public static final String FILE_EXTENSION = ".js";
  private static final Object INVOKE_FAILED = new Object();
  private static final ScriptEngineManager MANAGER = new ScriptEngineManager();
  private static final ScriptEngine ENGINE = MANAGER.getEngineByName("JavaScript");
  private static final Compilable COMPILABLE = (Compilable) ENGINE;
  private static final Invocable INVOCABLE = (Invocable) ENGINE;
  private static final Map<String, CompiledScript> SCRIPTS = new Hashtable<>();

  private static final List<String> POLYFILLS = Arrays.asList(
      "if (!Array.prototype.includes) {  Object.defineProperty(Array.prototype, 'includes', {    value: function(searchElement, fromIndex) {      if (this == null) {        throw new TypeError('\"this\" is null or not defined');      }      var o = Object(this);      var len = o.length >>> 0;      if (len === 0) {        return false;      }      var n = fromIndex | 0;      var k = Math.max(n >= 0 ? n : len - Math.abs(n), 0);      function sameValueZero(x, y) {        return x === y || (typeof x === 'number' && typeof y === 'number' && isNaN(x) && isNaN(y));      }      while (k < len) {        if (sameValueZero(o[k], searchElement)) {          return true;        }        k++;      }      return false;    }  });}");

  public static <T> T getClass(File file, Class<T> asClass) {
    return getClass(file.getAbsolutePath(), asClass);
  }

  public static <T> T getClass(String file, Class<T> asClass) {
    try {
      if (!file.endsWith(FILE_EXTENSION)) {
        file += FILE_EXTENSION;
      }
      CompiledScript script = SCRIPTS.get(file);
      if (script == null) {
        String contents = "";
        for (String polyfill : POLYFILLS) {
          contents += polyfill;
        }
        contents +=
            new String(FileManager.readStream(ScriptManager.class.getResourceAsStream(file)));
        script = COMPILABLE.compile(contents);
        SCRIPTS.put(file, script);
      }
      if (asClass == null) {
        return null;
      }
      Object result = script.eval(ENGINE.createBindings());
      return asClass.isInstance(result) ? asClass.cast(result) : null;
    } catch (Exception e) {
      PLogger.println(file + ", " + asClass);
      e.printStackTrace();
    }
    return null;
  }

  public static Object invokeFunction(String name, Object... args) {
    try {
      return INVOCABLE.invokeFunction(name, args);
    } catch (Exception e) {
    }
    return INVOKE_FAILED;
  }

  public static boolean invokedFunction(String name, Object... args) {
    return invokeFunction(name, args) != INVOKE_FAILED;
  }

  public static void loadClasses(String dirPath, boolean recursive) {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      return;
    }
    List<File> children = new ArrayList<>();
    Collections.addAll(children, dir.listFiles());
    Collections.sort(children,
        (f1, f2) -> f1.isDirectory() && f2.isDirectory() ? 0 : f1.isDirectory() ? 1 : -1);
    for (File child : children) {
      if (child.isHidden()) {
        continue;
      }
      if (child.isDirectory()) {
        if (recursive) {
          loadClasses(child.getPath(), recursive);
        }
        continue;
      }
      if (!child.isFile() || !child.getName().endsWith(FILE_EXTENSION)) {
        continue;
      }
      getClass(child.getPath(), null);
    }
  }

  public static void loadScript(File file) {
    try {
      ENGINE.eval(FileManager.readTextFile(file));
    } catch (Exception e) {
      PLogger.println(file.toString());
      e.printStackTrace();
    }
  }

  public static void loadScripts(String dirPath, boolean recursive) {
    File dir = new File(dirPath);
    if (!dir.exists() || !dir.isDirectory()) {
      return;
    }
    List<File> children = new ArrayList<>();
    Collections.addAll(children, dir.listFiles());
    Collections.sort(children,
        (a, b) -> a.isDirectory() && b.isDirectory() ? 0 : a.isDirectory() ? 1 : -1);
    for (File child : children) {
      if (child.isHidden()) {
        continue;
      }
      if (child.isDirectory()) {
        if (recursive) {
          loadScripts(child.getPath(), recursive);
        }
        continue;
      }
      if (!child.isFile() || !child.getName().endsWith(FILE_EXTENSION)) {
        continue;
      }
      String[] lines = FileManager.readTextFileArray(child);
      StringBuilder fileText = new StringBuilder();
      for (String line : lines) {
        if (line.contains("prototype") && line.contains("function")
            && (line.contains("{ }") || line.contains("{}"))) {
          continue;
        }
        fileText.append(line + "\n");
      }
      try {
        ENGINE.eval(fileText.toString());
      } catch (Exception e) {
        PLogger.println(child.getName());
        e.printStackTrace();
      }
    }
  }

  public static void importJavaTypes() {
    String[] langClasses = new String[] {"Boolean", "Byte", "Character", "Double", "Float",
        "Integer", "Long", "Short", "String", "Math", "System", "StringBuilder"};
    for (String s : langClasses) {
      importClass("java.lang." + s);
    }
  }

  public static void importJavaUtils() {
    String[] utilClasses = new String[] {"Comparator", "ArrayList", "Arrays", "Collections",
        "HashMap", "LinkedList", "Objects", "TreeMap"};
    for (String s : utilClasses) {
      importClass("java.util." + s);
    }
  }

  public static void importClass(String name) {
    try {
      Class<?> c = Class.forName(name);
      if (c.getSimpleName() == null || c.getName().contains("$")) {
        return;
      }
      ENGINE.getBindings(ScriptContext.GLOBAL_SCOPE).put(c.getSimpleName(),
          ENGINE.eval("Java.type('" + c.getName() + "')"));
    } catch (Exception e) {
      PLogger.println(name);
      e.printStackTrace();
    }
  }

  public static void importPackage(String name) {
    try {
      List<Class<?>> classes = findPackageClasses(name);
      for (Class<?> c : classes) {
        if (c.getSimpleName() == null) {
          PLogger.println(c.toString() + " is null");
          continue;
        } else if (c.getName().contains("$")) {
          continue;
        }
        ENGINE.getBindings(ScriptContext.GLOBAL_SCOPE).put(c.getSimpleName(),
            ENGINE.eval("Java.type('" + c.getName() + "')"));
      }
    } catch (Exception e) {
      PLogger.println(name);
      e.printStackTrace();
    }
  }

  private static List<Class<?>> findPackageClasses(String name) {
    try {
      ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
      String path = name.replace('.', '/');
      Enumeration<URL> resources = classLoader.getResources(path);
      List<File> dirs = new ArrayList<>();
      while (resources.hasMoreElements()) {
        URL resource = resources.nextElement();
        dirs.add(new File(resource.toURI()));
      }
      List<Class<?>> classes = new ArrayList<>();
      for (File directory : dirs) {
        List<Class<?>> foundClasses = findPackageClasses(directory, name, classLoader);
        if (foundClasses == null) {
          continue;
        }
        classes.addAll(foundClasses);
      }
      return classes;
    } catch (Exception e) {
      PLogger.println(name);
      e.printStackTrace();
    }
    return null;
  }

  private static List<Class<?>> findPackageClasses(File directory, String name,
      ClassLoader classLoader) {
    try {
      if (!directory.exists()) {
        return null;
      }
      List<Class<?>> classes = new ArrayList<>();
      File[] files = directory.listFiles();
      for (File file : files) {
        if (file.getName().endsWith(".class")) {
          String className = file.getName().substring(0, file.getName().length() - 6);
          classes.add(Class.forName(name + '.' + className, false, classLoader));
          continue;
        }
        if (!file.isDirectory() || file.getName().contains(".")) {
          continue;
        }
        List<Class<?>> foundClasses =
            findPackageClasses(file, name + "." + file.getName(), classLoader);
        if (foundClasses == null) {
          continue;
        }
        classes.addAll(foundClasses);
      }
      return classes;
    } catch (Exception e) {
      PLogger.println(directory.toString() + ", " + name);
      e.printStackTrace();
    }
    return null;
  }

  public static void printStats() {
    PLogger.println("ScriptManager: scripts: " + SCRIPTS.size());
  }

  static {
    try {
      for (String polyfill : POLYFILLS) {
        ENGINE.eval(polyfill);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
