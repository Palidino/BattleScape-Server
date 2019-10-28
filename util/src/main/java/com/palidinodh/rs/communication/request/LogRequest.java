package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;

public class LogRequest extends Request {
  private String directory1;
  private String directory2;
  private String line;

  public LogRequest(ServerSession session, int key, String directory1, String directory2,
      String line) {
    super(session, key);
    this.directory1 = directory1;
    this.directory2 = directory2;
    this.line = line;
  }

  public String getDirectory1() {
    return directory1;
  }

  public String getDirectory2() {
    return directory2;
  }

  public String getLine() {
    return line;
  }
}
