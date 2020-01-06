package me.mingshan.util.net;

import ch.ethz.ssh2.*;
import org.apache.commons.io.IOUtils;
import java.io.*;
import java.nio.charset.Charset;

public class RemoteShellExecutor {

  private Connection conn;
  /**
   * 远程机器IP
   */
  private String ip;
  /**
   * 用户名
   */
  private String osUsername;
  /**
   * 密码
   */
  private String password;
  private String charset = Charset.defaultCharset().toString();

  private static final int TIME_OUT = 1000 * 5 * 60;

  public RemoteShellExecutor(String ip, String usr, String pasword) {
    this.ip = ip;
    this.osUsername = usr;
    this.password = pasword;
  }

  /**
   * 登录
   *
   * @return
   * @throws IOException
   */
  private boolean login() throws IOException {
    conn = new Connection(ip);
    conn.connect();
    return conn.authenticateWithPassword(osUsername, password);
  }

  /**
   * 执行脚本
   *
   * @param cmds
   * @return
   * @throws Exception
   */
  public int exec(String cmds) throws Exception {
    InputStream stdOut = null;
    InputStream stdErr = null;
    String outStr = "";
    String outErr = "";
    int ret = -1;
    try {
      if (login()) {
        // Open a new {@link Session} on this connection
        Session session = conn.openSession();
        // Execute a command on the remote machine.
        session.execCommand(cmds);
        stdOut = new StreamGobbler(session.getStdout());
        outStr = processStream(stdOut, charset);

        stdErr = new StreamGobbler(session.getStderr());
        outErr = processStream(stdErr, charset);

        System.out.println(outStr);
        System.out.println(outErr);
        session.waitForCondition(ChannelCondition.EXIT_STATUS, TIME_OUT);

        ret = session.getExitStatus();
      } else {
        loginErrorReport();
      }
    } finally {
      if (conn != null) {
        conn.close();
      }
      IOUtils.closeQuietly(stdOut);
      IOUtils.closeQuietly(stdErr);
    }
    return ret;
  }

  private String processStream(InputStream in, String charset) throws Exception {
    byte[] buf = new byte[1024];
    StringBuilder sb = new StringBuilder();
    while (in.read(buf) != -1) {
      sb.append(new String(buf, charset));
    }
    return sb.toString();
  }

  /**
   * 将远程主机的文件下载到指定本机目录
   *
   * @param remoteFile
   * @param localTargetDirectory
   * @throws Exception
   */
  public void getFile(String remoteFile, String localTargetDirectory) throws IOException {
      if (login()) {
        SCPClient client = new SCPClient(conn);

        try (SCPInputStream fis = client.get(remoteFile);
             FileOutputStream fos = new FileOutputStream(localTargetDirectory)) {
          // 构造一个长度为1024的字节数组
          byte[] buffer = new byte[1024];
          //读取
          while (fis.read(buffer) != -1) {
            //写入另一个文件
            fos.write(buffer);
          }
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        // ("登录远程机器失败" + ip);
        // 自定义异常类 实现略
        loginErrorReport();
      }
  }

  /**
   * 将本地文件上传到远程主机指定目录
   *
   * @param localFile
   * @param remoteTargetDirectory
   * @param mode
   */
  public void uploadFile(String localFile, String remoteTargetDirectory, String mode) throws IOException {
      if (login()) {
        File file = new File(localFile);
        SCPClient client = new SCPClient(conn);
        byte[] b = new byte[4096];
        try (FileInputStream fis = new FileInputStream(file);
             SCPOutputStream os = client.put(file.getName(), file.length(), remoteTargetDirectory, mode)) {
          int i;
          while ((i = fis.read(b)) != -1) {
            os.write(b, 0, i);
          }
          os.flush();
        } catch (IOException e) {
          e.printStackTrace();
        }
      } else {
        // ("登录远程机器失败" + ip);
        // 自定义异常类 实现略
        loginErrorReport();
      }
  }

  private static void loginErrorReport() {
    System.out.println("登录失败，请确认远程ip，用户名和密码");
  }
}