package me.mingshan.util;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * @author mingshan
 */
public class ExceptionUtil {

  /**
   * Gets full stack trace
   *
   * @param throwable the throwable
   * @return throwable str
   */
  public static String getFullStackTrace(Throwable throwable) throws IOException {
    try (StringWriter sw = new StringWriter();
         PrintWriter pw  = new PrintWriter(sw)) {
      throwable.printStackTrace(pw);
      pw.flush();
      sw.flush();
      return sw.toString();
    } catch (IOException e) {
      throw e;
    }

  }
}
