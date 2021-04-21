package com.becomedigital.sdk.identity.becomedigitalsdk;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.becomedigital.sdk.identity.becomedigitalsdk.models.BDIVConfig;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.CompressImage;
import com.becomedigital.sdk.identity.becomedigitalsdk.utils.SharedParameters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static androidx.core.content.ContextCompat.checkSelfPermission;
import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class IntroductionCaptureDocumentFragment extends Fragment {
    private BDIVConfig config;
    private String selectedCountry,
            selectedCountyCo2,
            urlVideoFile = "",
            urlDocFront = "",
            urlDocBack = "";
    private boolean isFront;

    private SharedParameters.typeDocument typeDocument;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_capture_introduction_document, container, false);
    }

    private final int RESULT_LOAD_IMG = 100;

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((MainBDIV) getActivity()).changeColorToolbar(false);
        Button btnCaptureDoc = getActivity().findViewById(R.id.btnCaptureDoc);

        btnCaptureDoc.setOnClickListener(view1 -> {
            if (arePermissionsGranted()) {
                requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS);
            } else {
                goToDocumentCapture();
            }
        });
        Bundle arguments = getArguments();
        if (arguments != null) {
            config = (BDIVConfig) arguments.getSerializable("config");
            typeDocument = (SharedParameters.typeDocument) arguments.getSerializable("typeDocument");
            if (arguments.containsKey("urlVideoFile"))
                urlVideoFile = arguments.getString("urlVideoFile");
            selectedCountyCo2 = arguments.getString("selectedCountyCo2");
            selectedCountry = arguments.getString("selectedCountry");
            if (arguments.containsKey("urlDocBack"))
                urlDocBack = arguments.getString("urlDocBack");
            if (arguments.containsKey("urlDocFront"))
                urlDocFront = arguments.getString("urlDocFront");
            isFront = getArguments().getBoolean("isFront");
        }

        Button btnGalery = getActivity().findViewById(R.id.btnGalery);
        if (!config.isAllowLibraryLoading()) {
            btnGalery.setEnabled(false);
            btnGalery.setTextColor(getResources().getColor(R.color.grayLigth));
        } else {
            btnGalery.setOnClickListener(view12 -> {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                int RESULT_LOAD_IMG = 100;
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            });
        }

        ImageView imgReference = getActivity().findViewById(R.id.imgReference);
        TextView textDocType = getActivity().findViewById(R.id.textDocType);
        // valida el tipo de selccion y carga la introduccion
        if (typeDocument == SharedParameters.typeDocument.DNI || typeDocument == SharedParameters.typeDocument.LICENSE) {
            if (!isFront) {
                imgReference.setImageResource(R.drawable.document_reference_back);
            }
            if (typeDocument == SharedParameters.typeDocument.DNI)
                textDocType.setText(getString(R.string.text_dni_selec_document));
            else
                textDocType.setText(getString(R.string.text_license));

        } else if (typeDocument == SharedParameters.typeDocument.PASSPORT) {
            imgReference.setImageResource(R.drawable.passport_reference);
            textDocType.setText(getString(R.string.text_passport));
        }

        TextView textTittleIntro = getActivity().findViewById(R.id.textTittleIntro);
        if (!isFront) {
            textTittleIntro.setText(getString(R.string.text_tittle_intro_doc));
        }

        TextView textCountry = getActivity().findViewById(R.id.textCountry);
        textCountry.setText(selectedCountry);
    }

    private final int REQUEST_PERMISSIONS = 34;
    private static final String[] PERMISSIONS = {
            Manifest.permission.CAMERA,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    private Boolean arePermissionsGranted() {
        for (int i = 0; i < PERMISSIONS.length; i++) {
            if (checkSelfPermission(getActivity(), PERMISSIONS[i]) != PackageManager.PERMISSION_GRANTED) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            if (requestCode == RESULT_LOAD_IMG) {
                Uri imageUri = data.getData();
                String pathFile = CompressImage.getRealPathFromURIData(imageUri, getActivity());
                if (!pathFile.isEmpty()) {
                    Bundle bundle = new Bundle();
                    bundle.putString("pathImage", pathFile);
                    bundle.putString("selectedCountry", selectedCountry);
                    bundle.putString("selectedCountyCo2", selectedCountyCo2);
                    bundle.putSerializable("typeDocument", typeDocument);
                    bundle.putSerializable("config", config);
                    bundle.putBoolean("isFront", isFront);
                    bundle.putString("urlVideoFile", urlVideoFile);
                    bundle.putString("urlDocBack", urlDocBack);
                    bundle.putString("urlDocFront", urlDocFront);
                    findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.actionPreviewPiker, bundle);
                } else {
                    ((MainBDIV) getActivity()).setResultError(getString(R.string.general_error));
                }
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSIONS && grantResults.length > 0) {
            if (arePermissionsGranted()) {
                ((MainBDIV) getActivity()).setResultError("denied permits for the camera");
            } else {
                getActivity().runOnUiThread(this::goToDocumentCapture);
            }
        }
    }

    private void goToDocumentCapture() {
        Bundle bundle = new Bundle();
        bundle.putBoolean("isFront", isFront);
        bundle.putBoolean("isVideoCapture", false);// salta a la captura
        bundle.putString("selectedCountry", selectedCountry);
        bundle.putString("selectedCountyCo2", selectedCountyCo2);
        bundle.putSerializable("config", config);
        bundle.putSerializable("typeDocument", typeDocument);
        bundle.putString ("urlVideoFile", urlVideoFile);
        bundle.putString ("urlDocBack", urlDocBack);
        bundle.putString ("urlDocFront", urlDocFront);
        findNavController(getActivity(), R.id.nav_host_fragment).navigate(R.id.captureVideoAction, bundle);
    }

    @Override
    public void onResume() {
        super.onResume();
        ((MainBDIV) getActivity()).changeColorToolbar(false);
    }
}
