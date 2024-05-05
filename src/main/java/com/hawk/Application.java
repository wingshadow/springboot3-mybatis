package com.hawk;

import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

import java.net.InetAddress;

@Slf4j
@SpringBootApplication
public class Application implements ApplicationListener<WebServerInitializedEvent> {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

    @Override
    @SneakyThrows(value = Exception.class)
    public void onApplicationEvent(@NonNull WebServerInitializedEvent event) {
        int port = event.getWebServer().getPort();
        String ip = InetAddress.getLocalHost().getHostAddress();
        log.info("\n---------------------------------------------------------\n" +
                "\t本地地址:\t{}" +
                "\n---------------------------------------------------------", ip + ":" + port);
    }
}