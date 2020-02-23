package com.keyvault.quickstart;

import java.io.Console;   

import com.azure.identity.DefaultAzureCredentialBuilder;
import com.azure.identity.SharedTokenCacheCredential;

import com.azure.security.keyvault.secrets.SecretClient;
import com.azure.security.keyvault.secrets.SecretClientBuilder;
import com.azure.security.keyvault.secrets.models.KeyVaultSecret;

public class App {

    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {

        String keyVaultName = System.getenv("KEY_VAULT_NAME");
        String kvUri = "https://" + "bartr-kv-qs" + ".vault.azure.net";

        SecretClient secretClient = new SecretClientBuilder()
            .vaultUrl(kvUri)
            .credential(new DefaultAzureCredentialBuilder().build())
            .buildClient();


        String secretName = "mySecret";

        KeyVaultSecret retrievedSecret = secretClient.getSecret(secretName);

        System.out.println("mySecret: " + retrievedSecret.getValue());
    }
}
