package br.com.vinicius.vaultcore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class VaultcoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(VaultcoreApplication.class, args);
    }

}
