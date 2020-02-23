package com.keyvault.quickstart;

import java.io.IOException;
import com.microsoft.azure.credentials.*;
import com.microsoft.azure.keyvault.*;
import com.microsoft.azure.keyvault.models.*;

public class App {

    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {

        String keyVaultName = System.getenv("jkv_Name");

        if (keyVaultName == null || keyVaultName.isBlank()) {
            System.out.println("Set the KEY_VAULT_NAME environment variable");
            System.exit(-1);
        }

        String kvUri = "https://" + keyVaultName.trim() + ".vault.azure.net";

        System.out.println(kvUri);

        try {
            AzureTokenCredentials cred = AzureCliCredentials.create();

            KeyVaultClient kvClient = new KeyVaultClient(cred);

            SecretBundle secret = kvClient.getSecret(kvUri, "mySecret");

            System.out.println(secret.value());

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }

        // keeps mvn exec:java from waiting for ctl-c
        System.exit(0);
    }
}
