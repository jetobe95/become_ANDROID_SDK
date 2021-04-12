package com.becomedigital.sdk.identity.becomedigitalsdk.utils;

public interface SharedParameters {
     enum typeDocument {
        DNI,
        PASSPORT,
        LICENSE
    }
    String CLIENT_ID_MATI = "CLIENT_ID_MATI";
    String SECRET_KEY_MATY = "SECRET_KEY_MATY";
    String URL_ADD_DATA = "URL_ADD_DATA";
    String URL_AUTH = "URL_ACTIVATION_DATA_SERVER";
    String client_id_mati = "5edaa0346464c3001b5c5832";
    String secretKeyMati = "P2F0WFX7CY87JJVEQDEKZXMPEHGHJA1G";
    String url_auth = "https://becomedigital-api.azurewebsites.net/api/v1/auth";
    String url_add_data = "https://becomedigital-api.azurewebsites.net/api/v1/identity";
}
