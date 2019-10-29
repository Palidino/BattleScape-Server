package com.palidinodh.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import com.palidinodh.util.PLogger;

public class DecompressibleInputStream extends ObjectInputStream {
  public DecompressibleInputStream(InputStream in) throws IOException {
    super(in);
    try {
      Field enableOverrideField = ObjectInputStream.class.getDeclaredField("enableOverride");
      enableOverrideField.setAccessible(true);
      Field fieldModifiersField = Field.class.getDeclaredField("modifiers");
      fieldModifiersField.setAccessible(true);
      fieldModifiersField.setInt(enableOverrideField,
          enableOverrideField.getModifiers() & ~Modifier.FINAL);
      enableOverrideField.set(this, true);
    } catch (Exception e) {
    }
  }

  @Override
  public void defaultReadObject() throws IOException, ClassNotFoundException {
    try {
      super.defaultReadObject();
    } catch (ClassNotFoundException e) {
    }
  }

  @Override
  protected ObjectStreamClass readClassDescriptor() throws IOException, ClassNotFoundException {
    ObjectStreamClass resultClassDescriptor = super.readClassDescriptor(); // initially streams
                                                                           // descriptor
    Class<?> localClass; // the class in the local JVM that this descriptor represents.
    try {
      String name = resultClassDescriptor.getName();
      String originalName = name;
      name = name.replace("com.palidino.osrs", "com.palidinodh.osrscore");
      name = name.replace("com.palidino.rs", "com.palidinodh.rs.adaptive");

      if (!originalName.equals(name)) {
        Field nameField = ObjectStreamClass.class.getDeclaredField("name");
        nameField.setAccessible(true);
        nameField.set(resultClassDescriptor, name);
      }

      localClass = Class.forName(name);
    } catch (Exception e) {
      return resultClassDescriptor;
    }
    ObjectStreamClass localClassDescriptor = ObjectStreamClass.lookup(localClass);
    if (localClassDescriptor != null) { // only if class implements serializable
      final long localSUID = localClassDescriptor.getSerialVersionUID();
      final long streamSUID = resultClassDescriptor.getSerialVersionUID();
      if (streamSUID != localSUID) { // check for serialVersionUID mismatch.
        final StringBuffer s = new StringBuffer("Overriding serialized class version mismatch: ");
        s.append("local serialVersionUID = ").append(localSUID);
        s.append(" stream serialVersionUID = ").append(streamSUID);
        // Exception e = new InvalidClassException(s.toString());
        // PLogger.error("Potentially Fatal Deserialization Operation.", e);
        resultClassDescriptor = localClassDescriptor; // Use local class descriptor for
                                                      // deserialization
      }
    }
    return resultClassDescriptor;
  }

  @Override
  protected Object readObjectOverride() throws IOException, ClassNotFoundException {
    try {
      int outerHandle = getObjectInputStreamFieldValue("passHandle");
      long depth = getObjectInputStreamFieldValue("depth");
      try {
        Object obj =
            callObjectInputStreamMethod("readObject0", new Class<?>[] {boolean.class}, false);
        Object handles = getObjectInputStreamFieldValue("handles");
        Object passHandle = getObjectInputStreamFieldValue("passHandle");
        callMethod(handles, "markDependency", new Class<?>[] {int.class, int.class}, outerHandle,
            passHandle);
        callMethod(handles, "lookupException", new Class<?>[] {int.class}, passHandle);
        if (depth == 0) {
          callMethod(getObjectInputStreamFieldValue("vlist"), "doCallbacks", new Class<?>[] {});
        }
        return obj;
      } finally {
        getObjectInputStreamField("passHandle").setInt(this, outerHandle);
        boolean closed = getObjectInputStreamFieldValue("closed");
        if (closed && depth == 0) {
          try {
            callObjectInputStreamMethod("clear", new Class<?>[] {});
          } catch (Exception ignore) {
          }
        }
      }
    } catch (Throwable e) {
      PLogger.error(e.getMessage());
      return null;
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T getObjectInputStreamFieldValue(String fieldName) throws NoSuchFieldException,
      SecurityException, IllegalArgumentException, IllegalAccessException {
    Field declaredField = getObjectInputStreamField(fieldName);
    return (T) declaredField.get(this);
  }

  private Field getObjectInputStreamField(String fieldName) throws NoSuchFieldException {
    Field declaredField = ObjectInputStream.class.getDeclaredField(fieldName);
    declaredField.setAccessible(true);
    return declaredField;
  }

  @SuppressWarnings("unchecked")
  private <T> T callObjectInputStreamMethod(String methodName, Class<?>[] parameterTypes,
      Object... args) throws Throwable {
    Method declaredMethod = ObjectInputStream.class.getDeclaredMethod(methodName, parameterTypes);
    declaredMethod.setAccessible(true);
    try {
      return (T) declaredMethod.invoke(this, args);
    } catch (Exception e) {
      throw e.getCause();
    }
  }

  @SuppressWarnings("unchecked")
  private <T> T callMethod(Object object, String methodName, Class<?>[] parameterTypes,
      Object... args) throws Throwable {
    Method declaredMethod = object.getClass().getDeclaredMethod(methodName, parameterTypes);
    declaredMethod.setAccessible(true);
    try {
      return (T) declaredMethod.invoke(object, args);
    } catch (Exception e) {
      throw e.getCause();
    }
  }
}
