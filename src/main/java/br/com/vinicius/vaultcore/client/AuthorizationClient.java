package br.com.vinicius.vaultcore.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Map;

@FeignClient(name = "authorization-client", url = "https://util.devi.tools/api/v2/authorize")
public interface AuthorizationClient {

    @GetMapping
    Map<String, Object> isAuthorized();

}
