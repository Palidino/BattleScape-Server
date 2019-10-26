package com.palidinodh.nio;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NioServer implements Runnable {
  private List<Session> sessions = new ArrayList<>();
  private Map<String, Integer> connectionCounts = new HashMap<>();
  private InetSocketAddress hostAddress;
  private ServerSocketChannel serverSocketChannel;
  private Selector selector;
  private SessionHandler sessionHandler;
  private boolean running;

  private ByteBuffer directBuffer;
  private byte[] bufferBytes;

  private int sessionIdleTimeout;
  private int maxConnectionsPerIPAddress;
  private int socketBufferSize = 32768;

  public NioServer() throws IOException {
    selector = Selector.open();
    serverSocketChannel = ServerSocketChannel.open();
    serverSocketChannel.configureBlocking(false);
  }

  public void start(String remoteAddress, int port) throws IOException {
    if (hostAddress != null) {
      throw new IllegalStateException("Server already started");
    }
    if (sessionHandler == null) {
      throw new IllegalStateException("SsessionHandler can't be null");
    }
    directBuffer = ByteBuffer.allocateDirect(socketBufferSize);
    bufferBytes = new byte[socketBufferSize];
    hostAddress = new InetSocketAddress(remoteAddress, port);
    serverSocketChannel.socket().setReceiveBufferSize(socketBufferSize);
    serverSocketChannel.socket().bind(hostAddress);
    serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
    System.out.println("Starting server on " + remoteAddress + ":" + port);
    new Thread(this, "NioServer").start();
  }

  public void stop() {
    try {
      if (serverSocketChannel != null) {
        serverSocketChannel.close();
        serverSocketChannel = null;
      }
    } catch (IOException ioe) {
      ioe.printStackTrace();
    }
  }

  public void setSessionHandler(SessionHandler sessionHandler) {
    this.sessionHandler = sessionHandler;
  }

  public void setSessionIdleTimeout(int seconds) {
    if (hostAddress != null) {
      throw new IllegalStateException("Server already started");
    }
    if (seconds <= 0) {
      throw new IllegalArgumentException("seconds must be greater than 0");
    }
    sessionIdleTimeout = seconds * 1000;
  }

  public void setMaxConnectionsPerIPAddress(int maxConnectionsPerIPAddress) {
    if (hostAddress != null) {
      throw new IllegalStateException("Server already started");
    }
    if (maxConnectionsPerIPAddress <= 0) {
      throw new IllegalArgumentException("maxConnectionsPerIPAddress must be greater than 0");
    }
    this.maxConnectionsPerIPAddress = maxConnectionsPerIPAddress;
  }

  public void setSocketBufferSize(int socketBufferSize) throws IOException {
    if (hostAddress != null) {
      throw new IllegalStateException("Server already started");
    }
    if (socketBufferSize <= 0) {
      throw new IllegalArgumentException("size must be greater than 0");
    }
    this.socketBufferSize = socketBufferSize;
  }

  @Override
  public void run() {
    if (running) {
      throw new IllegalStateException("Server is already running");
    }
    running = true;
    while (serverSocketChannel != null && serverSocketChannel.isOpen()) {
      cycle();
    }
    running = false;
  }

  private void cycle() {
    try {
      selector.select();
      for (Iterator<SelectionKey> it = selector.selectedKeys().iterator(); it.hasNext();) {
        SelectionKey selectionKey = it.next();
        it.remove();
        Session session = null;
        try {
          if (serverSocketChannel == null || !serverSocketChannel.isOpen()) {
            break;
          }
          session = (Session) selectionKey.attachment();
          if (selectionKey.isValid() && selectionKey.isAcceptable()) {
            session = accept(selectionKey);
          }
          if (session == null) {
            continue;
          }
          if (selectionKey.isValid() && selectionKey.isReadable()) {
            read(selectionKey);
          }
          if (selectionKey.isValid() && selectionKey.isWritable()) {
            write(selectionKey);
          }
        } catch (Exception socketError) {
          error(socketError, session);
        }
      }
      checkSessions();
      Thread.sleep(10);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private Session accept(SelectionKey selectionKey) throws IOException {
    Session session = null;
    ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
    SocketChannel socketChannel = serverSocketChannel.accept();
    socketChannel.socket().setSendBufferSize(socketBufferSize);
    socketChannel.configureBlocking(false);
    String remoteAddress = socketChannel.socket().getInetAddress().getHostAddress();
    int connectionCount = getConnectionCount(remoteAddress);
    if (maxConnectionsPerIPAddress > 0 && connectionCount >= maxConnectionsPerIPAddress) {
      socketChannel.close();
    } else {
      connectionCounts.put(remoteAddress, connectionCount + 1);
      session = new Session(socketChannel, remoteAddress,
          socketChannel.register(selector, SelectionKey.OP_READ));
      sessionHandler.accept(session);
      sessions.add(session);
    }
    return session;
  }

  private void read(SelectionKey selectionKey) throws IOException {
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
    if (!socketChannel.isOpen()) {
      return;
    }
    Session session = (Session) selectionKey.attachment();
    directBuffer.clear();
    int numberBytesRead;
    ByteArrayOutputStream readStream = new ByteArrayOutputStream();
    while ((numberBytesRead = socketChannel.read(directBuffer)) > 0) {
      directBuffer.flip();
      directBuffer.get(bufferBytes, 0, numberBytesRead);
      readStream.write(bufferBytes, 0, numberBytesRead);
      directBuffer.clear();
      session.updateLastRead();
    }
    if (readStream.size() > 0) {
      sessionHandler.read(session, readStream.toByteArray());
    }
    if (numberBytesRead == -1) {
      session.close();
    }
  }

  private void write(SelectionKey selectionKey) throws IOException {
    SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
    if (!socketChannel.isOpen()) {
      return;
    }
    Session session = (Session) selectionKey.attachment();
    if (session.getWriteEvents().isEmpty()) {
      return;
    }
    try {
      while (!session.getWriteEvents().isEmpty()) {
        WriteEvent writeEvent = session.getWriteEvents().peek();
        ByteBuffer fromBuffer = writeEvent.getBuffer();
        int position = fromBuffer.position();
        int limit = fromBuffer.limit();
        try {
          do {
            directBuffer.clear();
            if (fromBuffer.remaining() > directBuffer.remaining()) {
              fromBuffer.limit(fromBuffer.remaining() - directBuffer.remaining());
            }
            directBuffer.put(fromBuffer);
            directBuffer.flip();
            position += socketChannel.write(directBuffer);
            fromBuffer.position(position);
            fromBuffer.limit(limit);
          } while (directBuffer.remaining() == 0 && fromBuffer.remaining() > 0);
          if (fromBuffer.remaining() > 0) {
            break;
          }
          if (writeEvent.getHandler() != null) {
            writeEvent.getHandler().complete(session, true);
          }
          session.getWriteEvents().poll();
        } catch (Exception socketError) {
          fromBuffer.position(position);
          fromBuffer.limit(limit);
          throw socketError;
        }
      }
    } catch (Exception e) {
      error(e, session);
    }
    if (selectionKey.isValid() && session.getWriteEvents().isEmpty()) {
      selectionKey.interestOps(SelectionKey.OP_READ);
    }
  }

  private void error(Exception exception, Session session) throws IOException {
    try {
      sessionHandler.error(exception, session);
    } catch (Exception e) {
      if (session != null) {
        session.close();
      }
      e.printStackTrace();
    }
  }

  private void checkSessions() {
    if (sessions.isEmpty()) {
      return;
    }
    for (Iterator<Session> it = sessions.iterator(); it.hasNext();) {
      Session session = it.next();
      SelectionKey selectionKey = session.getSelectionKey();
      if (selectionKey.isValid() && !session.getWriteEvents().isEmpty()) {
        selectionKey.interestOps(SelectionKey.OP_READ | SelectionKey.OP_WRITE);
      }
      if (session.idleTimeout(sessionIdleTimeout)) {
        session.close();
      }
      if (session.isOpen()) {
        continue;
      }
      String remoteAddress = session.getRemoteAddress();
      int connectionCount = getConnectionCount(remoteAddress);
      if (connectionCount > 1) {
        connectionCounts.put(remoteAddress, connectionCount - 1);
      } else {
        connectionCounts.remove(remoteAddress);
      }
      if (sessionHandler != null) {
        sessionHandler.closed(session);
      }
      if (selectionKey.isValid()) {
        selectionKey.cancel();
      }
      while (!session.getWriteEvents().isEmpty()) {
        WriteEvent writeEvent = session.getWriteEvents().poll();
        if (writeEvent.getHandler() != null) {
          writeEvent.getHandler().complete(session, false);
        }
      }
      it.remove();
    }
  }

  private int getConnectionCount(String remoteAddress) {
    return connectionCounts.containsKey(remoteAddress) ? connectionCounts.get(remoteAddress) : 0;
  }

  public void printStats() {
    System.out.println("NioServer: sessions: " + sessions.size() + "; connectionCounts: "
        + connectionCounts.size());
  }
}
