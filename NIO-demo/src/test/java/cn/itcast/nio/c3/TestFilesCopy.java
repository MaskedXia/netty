package cn.itcast.nio.c3;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestFilesCopy {

    public static void main(String[] args) throws IOException {
        long start = System.currentTimeMillis();
        String source = "C:\\Users\\pipe\\Desktop\\java_base\\netty\\test\\test_1";
        String target = "C:\\Users\\pipe\\Desktop\\java_base\\netty\\test\\test_3";

        // 遍历所有文件夹和文件
        Files.walk(Paths.get(source)).forEach(path -> {
            try {
                String targetName = path.toString().replace(source, target);
                System.out.println(targetName);
                // 是目录
                if (Files.isDirectory(path)) {
                    Files.createDirectory(Paths.get(targetName));
                }
                // 是普通文件
                else if (Files.isRegularFile(path)) {
                    Files.copy(path, Paths.get(targetName));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }
}
