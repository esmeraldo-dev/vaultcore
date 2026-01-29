package br.com.vinicius.vaultcore.client;

import br.com.vinicius.vaultcore.dto.NotificationDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notification-client", url = "https://util.devi.tools/api/v1/notify")
public interface NotificationClient {

    @PostMapping
    void sendNotification(@RequestBody NotificationDTO notification);
}
