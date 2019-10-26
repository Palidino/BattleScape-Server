package com.palidinodh.rs.communication.request;

import com.palidinodh.rs.communication.ServerSession;
import com.palidinodh.rs.communication.response.Response;
import com.palidinodh.util.PTime;

public class Request {
  public static final int TIMEOUT = 16000;

  private State state;
  private long lastSendTime;
  private int sendAttempts;
  private ServerSession session;
  private int key;
  private Object attachment;
  private Response response;
  private boolean logged;

  public enum State {
    PENDING_SEND, PENDING_RECEIVE, COMPLETE, PENDING, ENCODED, ERROR
  }

  public Request(ServerSession session, int key) {
    this.session = session;
    state = State.PENDING_SEND;
    this.key = key;
    updateLastSendTime();
  }

  public State getState() {
    return state;
  }

  public void setState(State state) {
    this.state = state;
  }

  public long getLastSendTime() {
    return lastSendTime;
  }

  public void updateLastSendTime() {
    lastSendTime = PTime.currentTimeMillis();
    sendAttempts++;
  }

  public void resetSendAttempts() {
    sendAttempts = 0;
  }

  public boolean needResend() {
    return PTime.currentTimeMillis() - lastSendTime >= TIMEOUT;
  }

  public ServerSession getSession() {
    return session;
  }

  public int getKey() {
    return key;
  }

  public Object getAttachment() {
    return attachment;
  }

  public void setAttachment(Object attachment) {
    this.attachment = attachment;
  }

  public void setResponse(Response response) {
    this.response = response;
    state = State.COMPLETE;
  }

  public Response getResponse() {
    return response;
  }

  public int getSendAttempts() {
    return sendAttempts;
  }

  public boolean getLogged() {
    return logged;
  }

  public void setLogged(boolean logged) {
    this.logged = logged;
  }
}
