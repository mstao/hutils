/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package me.mingshan.util.file;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Objects;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileUtil {
  private FileUtil() {
    throw new UnsupportedOperationException("It's prohibited to create instances of the class.");
  }

  public static void main(String[] args) throws IOException {
    covertEncode("D:\\c\\demo", UTF_8);
  }

  public static void covertEncode(String fileFolderPath, Charset charset) throws IOException {
    File file = new File(fileFolderPath);
    filePrintln(file, charset);
  }

  /**
   * 递归当前目录下所有文件
   */
  public static void filePrintln(File file, Charset charset) throws IOException {
    for (File f : Objects.requireNonNull(file.listFiles())) {
      if (f.isDirectory()) {
        filePrintln(f, charset);
      } else {
        System.out.println(f);
        codeConvert(f, charset);
      }
    }
  }

  public static void codeConvert(File file, Charset charset) throws IOException {
    try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), charset))) {
      StringBuilder sb = new StringBuilder();
      String str;
      while ((str = br.readLine()) != null) {
        sb.append(str);
        sb.append("\n");
      }
      bw.write(sb.toString());
    }
  }

  private static void covertEncode(File file, Charset charset) throws IOException {
    try (FileInputStream fis = new FileInputStream(file);
         FileOutputStream fos = new FileOutputStream(file);
         FileChannel inChannel = fis.getChannel();
         FileChannel outChannel = fos.getChannel()) {
      //创建缓冲区
      ByteBuffer buffer = ByteBuffer.allocate(1024);
      while (inChannel.read(buffer) != -1) {
        //切换到读数据模式
        buffer.flip();
        //将缓冲区的内容写入通道
        CharBuffer decode = charset.decode(buffer);
        outChannel.write(charset.encode(decode));
        //清空缓冲区
        buffer.clear();
      }
    }
  }

}
