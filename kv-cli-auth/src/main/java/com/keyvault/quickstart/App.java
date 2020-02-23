package com.keyvault.quickstart;

import com.microsoft.azure.credentials.*;
import com.microsoft.azure.keyvault.*;
import com.microsoft.azure.keyvault.models.*;
import com.microsoft.azure.keyvault.requests.*;

public class App {
    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {

        String keyVaultName = System.getenv("jkv_Name");

        if (keyVaultName == null || keyVaultName.isBlank()) {
            System.out.println("Set the jkv_Name environment variable");
            System.exit(-1);
        }

        // build key vault URL
        String kvUrl = "https://" + keyVaultName.trim() + ".vault.azure.net";
        System.out.println(kvUrl);

        try {
            // get credentials from Azure CLI cache
            AzureTokenCredentials cred = AzureCliCredentials.create();

            // create Key Vault client
            KeyVaultClient kvClient = new KeyVaultClient(cred);

            // get mySecret
            SecretBundle secret = kvClient.getSecret(kvUrl, "mySecret");
            System.out.println(secret.value());

            // create myNewSecret
            SetSecretRequest req = new SetSecretRequest.Builder(kvUrl, "myNewSecret", "My new secret value").build();
            kvClient.setSecret(req);

            // get myNewSecret
            SecretBundle newSecret = kvClient.getSecret(kvUrl, "myNewSecret");
            System.out.println(newSecret.value());

            // delete myNewSecret
            kvClient.deleteSecret(kvUrl, "myNewSecret");

            // myNewSecret should be null
            newSecret = kvClient.getSecret(kvUrl, "myNewSecret");
            System.out.println(newSecret == null);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // keeps mvn exec:java from waiting for ctl-c
        System.exit(0);
    }
}
