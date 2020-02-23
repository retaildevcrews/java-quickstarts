package com.keyvault.quickstart;

import com.microsoft.azure.AzureEnvironment;
import com.microsoft.azure.credentials.*;
import com.microsoft.azure.keyvault.*;
import com.microsoft.azure.keyvault.models.*;
import com.microsoft.azure.keyvault.requests.*;

public class App {
    public static void main(String[] args) throws InterruptedException, IllegalArgumentException {

        String keyVaultName = System.getenv("jqs_KeyVaultName");

        if (keyVaultName == null || keyVaultName.isBlank()) {
            System.out.println("Set the jqs_KeyVaultName environment variable");
            System.exit(-1);
        }

        // build key vault URL
        String kvUrl = "https://" + keyVaultName.trim() + ".vault.azure.net";
        System.out.println(kvUrl);

        try {
            AzureTokenCredentials cred = null;

            // check for jqs_AuthType env var
            String isMsi = System.getenv("MSI");
            if (isMsi != null && isMsi.equals("MSI")) {
                // use Managed Identity
                cred = new MSICredentials(AzureEnvironment.AZURE);
            } else {
                // use Azure CLI cache
                cred = AzureCliCredentials.create();
            }

            // can't continue
            if (cred == null || cred.domain() == null){
                System.out.println("Unable to get credentials");
                System.exit(-1);
            }
            System.out.println(cred.domain());

            // create Key Vault client
            KeyVaultClient kvClient = new KeyVaultClient(cred);

            // get mySecret
            // this will fail without proper permissions so make sure to check results
            SecretBundle secret = kvClient.getSecret(kvUrl, "mySecret");
            System.out.println(secret.value());

            // create myNewSecret
            // this will fail without proper permissions so make sure to check results
            SetSecretRequest req = new SetSecretRequest.Builder(kvUrl, "myNewSecret", "My new secret value").build();
            secret = kvClient.setSecret(req);
            System.out.println(secret.id());

            // get myNewSecret
            // this will fail without proper permissions so make sure to check results
            secret = kvClient.getSecret(kvUrl, "myNewSecret");
            System.out.println(secret.value());

            // delete myNewSecret
            // this will fail without proper permissions so make sure to check results
            secret = kvClient.deleteSecret(kvUrl, "myNewSecret");
            System.out.println(secret.id());

            // myNewSecret should be null
            secret = kvClient.getSecret(kvUrl, "myNewSecret");
            System.out.println(secret == null);

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        // keeps mvn exec:java from waiting for ctl-c
        System.exit(0);
    }
}
