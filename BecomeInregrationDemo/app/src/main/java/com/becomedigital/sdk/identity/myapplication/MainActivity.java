package com.becomedigital.sdk.identity.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;


import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeCallBackManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeInterfaseCallback;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.BecomeResponseManager;
import com.becomedigital.sdk.identity.becomedigitalsdk.callback.LoginError;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.models.ResponseIV;

public class MainActivity extends AppCompatActivity {
    private BecomeCallBackManager mCallbackManager = BecomeCallBackManager.createNew ( );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);


    }

    String validatiopnTypes =  "PASSPORT/LICENSE/DNI/VIDEO" ;
    String clientSecret =  "FKLDM63GPH89TISBXNZ4YJUE57WRQA25" ;
    String clientId =  "acc_demo" ;
    String contractId =  "2";

    @Override
    protected void onResume() {
        super.onResume ( );
        BecomeResponseManager.getInstance ( ).startAutentication (MainActivity.this,
                new BDIVConfig (clientId,
                        clientSecret,
                        contractId,
                        validatiopnTypes,
                        true,
                        null
                ));
        BecomeResponseManager.getInstance ( ).registerCallback (mCallbackManager, new BecomeInterfaseCallback ( ) {
            @Override
            public void onSuccess(final ResponseIV responseIV) {
                String id = responseIV.getId ( );
                String created_at = responseIV.getCreated_at ( );
                String company = responseIV.getCompany ( );
                String fullname = responseIV.getFullname ( );
                String birth = responseIV.getBirth ( );
                String document_type = responseIV.getDocument_type ( );
                String document_number = responseIV.getDocument_number ( );
                Boolean face_match = responseIV.getFace_match ( );
                Boolean template = responseIV.getTemplate ( );
                Boolean alteration = responseIV.getAlteration ( );
                Boolean watch_list = responseIV.getWatch_list ( );
                String comply_advantage_result = responseIV.getComply_advantage_result ( );
                String comply_advantage_url = responseIV.getComply_advantage_url ( );
                String verification_status = responseIV.getVerification_status ( );
                String message = responseIV.getMessage ( );
                Integer responseStatus = responseIV.getResponseStatus ( );
                String textFinal = "id: "  +
                        "\ncreated_at: " + created_at +
                        "\ncompany: " + company +
                        "\nfullname: " + fullname +
                        "\nbirth: " + birth +
                        "\ndocument_type: " + document_type +
                        "\ndocument_number: " + document_number +
                        "\nface_match: " + face_match +
                        "\ntemplate: " + template +
                        "\nalteration: " + alteration +
                        "\nwatch_list: " + watch_list +
                        "\ncomply_advantage_result: " + comply_advantage_result +
                        "\ncomply_advantage_url: " + comply_advantage_url +
                        "\nverification_status: " + verification_status +
                        "\nmessage: " + message +
                        "\nresponseStatus: " + responseStatus;

                Log.d ("responseIV", textFinal);
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(LoginError pLoginError) {
                Log.d ("Error", pLoginError.getMessage ( ));
            }

        });
    }
}