package org.jivesoftware.openfire;


public class BasicStreamID
  implements StreamID
{
  String id;

  public BasicStreamID(String id)
  {
    this.id = id;
  }

  @Override
public String getID() {
    return this.id;
  }

  @Override
public String toString() {
    return this.id;
  }

  @Override
public int hashCode() {
    return this.id.hashCode();
  }
}
