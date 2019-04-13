package me.mingshan.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author mingshan
 */
public class RegexUtil {

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executor = Executors.newCachedThreadPool();
        // 会议开始的锁，只是一个简单的开关，所以初始化计数器为1
        final CountDownLatch conference = new CountDownLatch(1);
        // 参会人员的锁，有5个人，所以初始化计数器为5
        final CountDownLatch participants = new CountDownLatch(5);

        for (int i = 0; i < 5; i++) {
            final int no = i + 1;
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    try {
                        // 一直阻塞，等待会议开始
                        conference.await();
                        //Thread.sleep((long) (Math.random() * 10 * 1000));
                        System.out.println("No." + no + " 进入视频会议");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        participants.countDown();
                    }

                }
            };

            executor.execute(runnable);
        }

        System.out.println("会议开始，等待参会人员加入...");
        // 会议开始，参会人员可以加入了
        conference.countDown();

        // 一直阻塞，直到全部参会人员加入
        participants.await();
        System.out.println("人员到齐，会议正式开始");
        executor.shutdown();
    }
}
