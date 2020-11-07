package com.paweldev.maszynypolskie.ui.signaturepad;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.paweldev.maszynypolskie.R;
import com.paweldev.maszynypolskie.model.FileModel;
import com.paweldev.maszynypolskie.model.Service;
import com.paweldev.maszynypolskie.model.enums.FileType;
import com.paweldev.maszynypolskie.repository.CustomerRepository;
import com.paweldev.maszynypolskie.repository.FileRepository;
import com.paweldev.maszynypolskie.repository.ServiceRepository;
import com.paweldev.maszynypolskie.repository.StorageRepository;
import com.paweldev.maszynypolskie.ui.devices.service.DeviceAddNewServiceFragment;
import com.paweldev.maszynypolskie.ui.devices.service.enums.EnumServiceState;
import com.paweldev.maszynypolskie.ui.info.MessageFragment;
import com.paweldev.maszynypolskie.utils.ConnectionUtils;
import com.paweldev.maszynypolskie.utils.DialogUtils;
import com.paweldev.maszynypolskie.utils.FragmentUtils;
import com.paweldev.maszynypolskie.utils.pdfUtils.ProtokolSerwisowyCreatePDF;
import com.paweldev.maszynypolskie.utils.signatureutils.views.SignaturePad;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import static android.content.ContentValues.TAG;

public class SignaturePadFragment extends Fragment {

    View v;
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private SignaturePad mSignaturePad;
    private Button mClearButton;
    private Button mSaveButton;
    private Service currentService;
    private DeviceAddNewServiceFragment previousFragment;
    private Signatory signatory;
    private Bitmap signatureBitmapUser;
    private Bitmap signatureBitmapCustomer;
    private TextView nameOfTheSignatory;

    public SignaturePadFragment(Service currentService, DeviceAddNewServiceFragment previousFragment, Signatory signatory) {
        this.currentService = currentService;
        this.previousFragment = previousFragment;
        this.signatory = signatory;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        v = inflater.inflate(R.layout.signature_pad_fragment, container, false);
        verifyStoragePermissions(getActivity());

        Button previousButton = v.findViewById(R.id.prev_signature_pad);
        previousButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
        nameOfTheSignatory = v.findViewById(R.id.customer_name_textView_signature_pad);
        mSignaturePad = (SignaturePad) v.findViewById(R.id.signature_pad2);
        mSignaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
            @Override
            public void onStartSigning() {
            }

            @Override
            public void onSigned() {
                mSaveButton.setEnabled(true);
                mClearButton.setEnabled(true);
            }

            @Override
            public void onClear() {
                mSaveButton.setEnabled(false);
                mClearButton.setEnabled(false);
            }
        });

        mClearButton = (Button) v.findViewById(R.id.clear_button);
        mSaveButton = (Button) v.findViewById(R.id.save_button);

        if (signatory == Signatory.USER) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Podpis Serwisanta");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
            mSignaturePad.setPenColor(Color.BLUE);
        } //blue
        else if (signatory == Signatory.CUSTOMER) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Podpis osoby upoważnionej");
            ((AppCompatActivity) getActivity()).getSupportActionBar().setSubtitle("");
            mSignaturePad.setPenColor(Color.BLUE);
            DialogUtils.getCustomerNameDialog(getContext(), nameOfTheSignatory);
        }
        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignaturePad.clear();
            }
        });
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {

                final Bitmap signatureBitmap = mSignaturePad.getSignatureBitmap();
                if (addJpgSignatureToGallery(signatureBitmap)) {
                } else {
                    Toast.makeText(getContext(), "Błąd zapisu", Toast.LENGTH_SHORT).show();
                }
                if (signatory == Signatory.USER) {

                    signatureBitmapUser = signatureBitmap;
                    SignaturePadFragment signaturePadFragment = new SignaturePadFragment(currentService, previousFragment, Signatory.CUSTOMER);
                    signaturePadFragment.setSignatureBitmapUser(signatureBitmapUser);
                    FragmentUtils.replaceFragment(signaturePadFragment, getFragmentManager());
                } else if (signatory == Signatory.CUSTOMER) {

                    if (nameOfTheSignatory.getText().length() < 3) {
                        DialogUtils.getCustomerNameDialog(getContext(), nameOfTheSignatory);
                    } else {

                        new AlertDialog.Builder(getContext())
                                .setTitle("Uwaga!")
                                .setMessage("Czy napewno chcesz zatwierdzić serwis? Nie będzie już możliwości edycji")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with OK
                                        final ProgressDialog progressDialog = ProgressDialog.show(getContext(), "Proszę czekać", "Tworzę pdf-a i przesyłam go na serwer.", true);
                                        progressDialog.show(); // Display Progress Dialog
                                        progressDialog.setCancelable(false);
                                        new Thread(new Runnable() {
                                            @Override
                                            public void run() {
                                                try {
                                                    if (ConnectionUtils.isInternetAvailable(getContext())) {
                                                        currentService.setState(EnumServiceState.PODPISANY.toString());
                                                        signatureBitmapCustomer = signatureBitmap;
                                                        ServiceRepository.updateService(currentService, true);
                                                        currentService = ServiceRepository.findServiceAfterId(currentService.getId());
                                                        File pdfFile = ProtokolSerwisowyCreatePDF.createPdfServiceProtocol(getActivity(), currentService, signatureBitmapUser, signatureBitmapCustomer, nameOfTheSignatory.getText().toString());
                                                        FileModel fileModel = new FileModel(
                                                                null,
                                                                null,
                                                                currentService.getId(),
                                                                currentService.getDate(),
                                                                FileType.PROTOKOL_SERWISOWY,
                                                                null,
                                                                null
                                                        );
                                                        FileRepository.insertNewFile(fileModel);
                                                        String filename = String.format("%07d", Integer.parseInt(fileModel.getId()))+"~"+ CustomerRepository.findCustomerById(currentService.getCustomer().getId()).getShortName()+"~"+currentService.getType()+".pdf";
                                                        Log.i(TAG, "File name = "+filename);
                                                        fileModel.setFilename(filename);
                                                        FileRepository.updateDeviceFile(fileModel);
                                                        StorageRepository.uploadFile(pdfFile, fileModel);
                                                        previousFragment.setCurrentService(currentService);
                                                        previousFragment.setSingnedState();
                                                        getFragmentManager().popBackStack();
                                                        getFragmentManager().popBackStack();
                                                        previousFragment.setShowAfterSigning(true);
                                                        progressDialog.dismiss();
                                                    } else {
                                                        MessageFragment messageFragment = new MessageFragment();
                                                        FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                                        messageFragment.setMessage("Brak połączenia z internetem");
                                                    }

                                                } catch (Exception e) {
                                                    MessageFragment messageFragment = new MessageFragment();
                                                    messageFragment.setMessage(e.getMessage());
                                                    FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
                                                    e.printStackTrace();
                                                }
                                            }
                                        }).start();
                                    }
                                })
                                .setNegativeButton("Anuluj", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // do nothing
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();
                    }
                }
            }
        });

        return v;
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onPause() {
        super.onPause();
        if (signatory == Signatory.CUSTOMER) {

            getActivity().setRequestedOrientation(
                    ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length <= 0
                        || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), "Cannot write images to external storage", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }

    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();
    }

    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {
            File photo = new File(Environment.getExternalStorageDirectory().toString(), "Signature.jpg");
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage(getString(R.string.no_connection) + "\n\n" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }

    public boolean addSvgSignatureToGallery(String signatureSvg) {
        boolean result = false;
        try {
            File svgFile = new File(getAlbumStorageDir("SignaturePad"), String.format("Signature_%d.svg", System.currentTimeMillis()));
            OutputStream stream = new FileOutputStream(svgFile);
            OutputStreamWriter writer = new OutputStreamWriter(stream);
            writer.write(signatureSvg);
            writer.close();
            stream.flush();
            stream.close();
            scanMediaFile(svgFile);
            result = true;
        } catch (Exception e) {
            MessageFragment messageFragment = new MessageFragment();
            FragmentUtils.replaceFragment(messageFragment, getFragmentManager());
            messageFragment.setMessage(getString(R.string.no_connection) + "\n\n" + e.getMessage());
            e.printStackTrace();
        }
        return result;
    }

    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public DeviceAddNewServiceFragment getPreviousFragment() {
        return previousFragment;
    }

    public void setPreviousFragment(DeviceAddNewServiceFragment previousFragment) {
        this.previousFragment = previousFragment;
    }

    public Bitmap getSignatureBitmapUser() {
        return signatureBitmapUser;
    }

    public void setSignatureBitmapUser(Bitmap signatureBitmapUser) {
        this.signatureBitmapUser = signatureBitmapUser;
    }
}
