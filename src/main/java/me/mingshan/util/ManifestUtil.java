package me.mingshan.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

/**
 * ManifestUtil 工具类，读取jar中 MANIFEST.MF文件内容
 */
public class ManifestUtil {

  /**
   * 读取Manifest
   *
   * @param clazz
   * @return
   * @throws IOException
   */
  public static Manifest getManifest(Class clazz) throws IOException {
    if (clazz == null) {
      return null;
    }

    Manifest mf = getManifestFromJar(clazz);
    if (mf != null) {
      return mf;
    }
    mf = getManifestFromFile(clazz);

    return mf;
  }

  /**
   * 尝试从jar中取得Manifest。
   *
   * @return 若取不到，例如指定类不位于jar中，将返回null。
   */
  private static Manifest getManifestFromJar(Class clazz) throws IOException {
    assert clazz != null;
    String resource = "/" + clazz.getName().replace('.', '/') + ".class";
    URL url = clazz.getResource(resource);
    URLConnection conn = url.openConnection();
    if (conn instanceof JarURLConnection) {
      JarURLConnection jconn = (JarURLConnection) conn;
      return jconn.getManifest();
    } else {
      return null;
    }
  }

  /**
   * 尝试搜索文件，以取得Manifest。
   *
   * @return 若找不到将返回null。
   */
  private static Manifest getManifestFromFile(Class clazz) throws IOException {
    assert clazz != null;
    String resource = "/" + clazz.getName().replace('.', '/') + ".class";
    URL url = clazz.getResource(resource);
    // 将url转换为文件路径。
    File baseFile;
    try {
      baseFile = new File(url.toURI());
    } catch (Exception e) {
      // 转换失败，意味着无法使用文件方式
      return null;
    }

    // 逐级向上查找
    while (baseFile != null) {
      File file = new File(baseFile.getParent(), "META-INF/MANIFEST.MF");
      if (file.exists()) {
        return new Manifest(new FileInputStream(file));
      }
      baseFile = baseFile.getParentFile();
    }
    return null;
  }

  /**
   * 通过给定的class，获取该class对应jar的版本号
   *
   * @param clazz 指定class
   * @return jar 版本
   * @throws IOException
   */
  public static String getVersion(Class<?> clazz) throws IOException {
    Manifest manifest = getManifest(clazz);
    if (manifest == null) {
      return null;
    }

    return getVersion(manifest);
  }

  /**
   * 获取版本号
   *
   * @param manifest
   * @return
   */
  public static String getVersion(Manifest manifest) {
    if (manifest == null) {
      return null;
    }
    Attributes attrs = manifest.getMainAttributes();
    String versionText = attrs.getValue(Attributes.Name.IMPLEMENTATION_VERSION);
    if (versionText == null || "".equals(versionText.trim())) {
      versionText = attrs.getValue(Attributes.Name.SPECIFICATION_VERSION);
    }
    if (versionText == null || "".equals(versionText.trim())) {
      versionText = attrs.getValue("Bundle-Version");
    }
    if (versionText == null || "".equals(versionText.trim())) {
      return null;
    } else {
      return versionText;
    }
  }

  /**
   * 获取groupId
   *
   * @param manifest
   * @return
   */
  public static String getGroupId(Manifest manifest) {
    if (manifest == null) {
      return null;
    }
    Attributes attrs = manifest.getMainAttributes();
    return attrs.getValue(Attributes.Name.IMPLEMENTATION_VENDOR_ID);
  }
}
