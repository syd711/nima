package com.starsailor.util;

import java.io.*;
import java.util.Date;

/**
 * Used for the libgdx logging
 */
public class LogInterceptor extends PrintStream {
  public LogInterceptor(OutputStream out) {
    super(out, true);
  }

  @Override
  public void print(String s) {//do what ever you like
    super.print(new Date() + " "  + s);
  }

  public static void interceptSystemOut(String filename) {
    try {
      PrintStream interceptor = new LogInterceptor(new BufferedOutputStream(new FileOutputStream(filename)));
      System.setOut(interceptor);// just add the interceptor
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }

}
