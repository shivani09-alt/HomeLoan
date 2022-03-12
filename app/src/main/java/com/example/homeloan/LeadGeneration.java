package com.example.homeloan;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.loader.content.CursorLoader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LeadGeneration extends Fragment implements ImagesRecyclerView.EventListener{


     ArrayList arrayList=new ArrayList();
     AutoCompleteTextView state,city;
     ImagesRecyclerView adpter;
     TextView addDetails,phoneNumberError,addDetailsError;
     TextInputEditText fName,lName,contact,projectName,flatDetails,propertCost,loanRequirements;
     LinearLayout layout;
     public static final int GALLERY = 0;
     ArrayList<String> urlList=new ArrayList<>();
     int num=0;
     ImageView uploadPhotos;
    ArrayList<String> mArrayUri = new ArrayList<String>();
     RecyclerView recyclerView;
    DataBaseHelper dataBaseHelper;
    private static String auth_token="";
    private static String Accept="[{\"key\":\"Accept\",\"value\":\"application/json\",\"description\":\"\"}]";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_lead_generation, container, false);
        state=view.findViewById(R.id.state);
        city=view.findViewById(R.id.city);
        addDetails=view.findViewById(R.id.addDetails);
        fName=view.findViewById(R.id.Fname);
        lName=view.findViewById(R.id.Lname);
        contact=view.findViewById(R.id.phoneNumber);
        uploadPhotos=view.findViewById(R.id.uploadPhotos);
        projectName=view.findViewById(R.id.projectName);
        flatDetails=view.findViewById(R.id.flatDetails);
        propertCost=view.findViewById(R.id.propertCost);
        phoneNumberError=view.findViewById(R.id.phoneNumberError);
        addDetailsError=view.findViewById(R.id.addDetailsError);
        layout=view.findViewById(R.id.layout);
        loanRequirements=view.findViewById(R.id.loanRequirements);
        recyclerView=view.findViewById(R.id.recyclerview);
        dataBaseHelper=new DataBaseHelper(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        getAccessToken();
        state.setOnItemClickListener((adapterView, view1, i, l) -> {
            String stateName = String.valueOf(adapterView.getItemAtPosition(i));
             Log.d("stateName",stateName);

             getCityList(stateName);
        });
        city.setOnItemClickListener((adapterView, view1, i, l) -> {
            String cityName = String.valueOf(adapterView.getItemAtPosition(i));
            Log.d("cityName",cityName);

        });

        city.setOnTouchListener((v, event) -> {
            city.showDropDown();
            return false;
        });

        state.setOnTouchListener((v, event) -> {
            state.showDropDown();
            return false;
        });

        state.addTextChangedListener( new CustomTextWatcher(state,null,getActivity()));
        city.addTextChangedListener( new CustomTextWatcher(city,null,getActivity()));
        lName.addTextChangedListener( new CustomTextWatcher(null,lName,getActivity()));
        contact.addTextChangedListener(new CustomTextWatcher(null,contact,getActivity()));
        loanRequirements.addTextChangedListener(new CustomTextWatcher(null,loanRequirements,getActivity()));
        propertCost.addTextChangedListener( new CustomTextWatcher(null,propertCost,getActivity()));
        flatDetails.addTextChangedListener( new CustomTextWatcher(null,flatDetails,getActivity()));
        projectName.addTextChangedListener( new CustomTextWatcher(null,projectName,getActivity()));
        contact.addTextChangedListener(new CustomTextWatcher(null,contact,getActivity()));
        fName.addTextChangedListener( new CustomTextWatcher(null,fName,getActivity()));




        addDetails.setOnClickListener(view12 -> {
            if(checkLength(fName) && checkLength(lName)&& checkLength(contact) && checkLength(projectName)&& checkLength(flatDetails)
                    && checkLength(propertCost)&& checkLength(loanRequirements) && checkLength(state)&& checkLength(city)) {
                addDetailsError.setVisibility(View.GONE);
                if(contact.length()<10){
                    contact.setBackgroundResource(R.drawable.error_box);
                    addDetailsError.setVisibility(View.VISIBLE);
                }
                else if( mArrayUri.size()==0){
                 addDetailsError.setVisibility(View.VISIBLE);
                }
                else if(!isNetworkAvailable())
                {
                    Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
                else {
                    DataBaseHelper dataBaseHelper=new DataBaseHelper(getActivity());
                    if((!dataBaseHelper.getPhoneNumberList(Objects.requireNonNull(contact.getText()).toString() ) ) ) {
                        contact.setBackgroundResource(R.drawable.backfround_shape);
                        phoneNumberError.setVisibility(View.GONE);
                       addDetailsError.setVisibility(View.GONE);
                        alertDialog("addUser");
                    }
                    else if(dataBaseHelper.getPhoneNumberList(contact.getText().toString())) {
                        contact.setBackgroundResource(R.drawable.error_box);
                        phoneNumberError.setVisibility(View.VISIBLE);
                    }



                }
            }

            else {
                addDetailsError.setVisibility(View.VISIBLE);
            }



        });
        uploadPhotos.setOnClickListener(view13 -> {
            if(urlList.size()==0) {
                num = 0;
            }
            uploadPhotos.setEnabled(false);
            getPhotoVideo();
        });

        return view;
    }
    public void getPhotoVideo() {


        if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            requestStoragePermission(new String[]{  Manifest.permission.READ_EXTERNAL_STORAGE });
        }
        else {
            String[] mimetypes = {"image/"};

            Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
            galleryIntent.setType("image/*");
            galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
            galleryIntent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);

            startActivityForResult(galleryIntent, GALLERY);
        }
        uploadPhotos.setEnabled(true);
    }
    private void requestStoragePermission(final String[] permission) {

        requestPermissions(permission, GALLERY);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY) {
            Uri contentURI;
            String contenturi, filePath;

            if (data != null) {
                num++;
                if (data.getClipData() != null) {
                    ClipData mClipData = data.getClipData();

                    for (int i = 0; i < mClipData.getItemCount(); i++) {
                        ClipData.Item item = mClipData.getItemAt(i);
                        contentURI = item.getUri();
                        contenturi = contentURI.toString();
                        Log.d("contentUri", contentURI.toString());
                        if (isGoogleDriveUri(contentURI)) {
                            filePath = getDriveFilePath(contentURI, getActivity());

                        } else if (contenturi.toLowerCase().startsWith("file://")) {
                            filePath = (new File(URI.create(contenturi))).getAbsolutePath();
                        } else {
                            filePath = getPath(contentURI);
                            if (filePath == null) {
                                Log.d("filePath", "null");
                                filePath = getRealPathImageFromUri(contentURI);
                            }

                        }
                        mArrayUri.add(filePath);
                        File file = new File(filePath);
                        long fileSizeInBytes = file.length();
                        int digitGroups = (int) (Math.log10(fileSizeInBytes) / Math.log10(1024));
                        final String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
                        final String fileSize = new DecimalFormat("#,##0.#").format(fileSizeInBytes / Math.pow(1024, digitGroups)) + " " + units[digitGroups];

                        Log.d("fileSizeInBytes", String.valueOf(fileSizeInBytes));
                        long fileSizeInKB = fileSizeInBytes / 1024;
                        long fileSizeInMB = fileSizeInKB / 1024;
                        Log.d("fileSizeInMB", String.valueOf(fileSizeInMB));
                    }

                }
                else{
                    contentURI = data.getData();
                    contenturi = contentURI.toString();
                    Log.d("content Uri", contentURI.toString());
                    if(isGoogleDriveUri(contentURI)){
                        filePath=getDriveFilePath(contentURI,getActivity());
                    }
                    else if (contenturi.toLowerCase().startsWith("file://")) {
                        Log.d("file Start with", "file://");
                        filePath = (new File(URI.create(contenturi))).getAbsolutePath();
                    } else {
                        filePath = getPath(contentURI);
                        if (filePath == null) {
                            Log.d("path", "null");
                            filePath = getRealPathImageFromUri(contentURI);
                        }
                    }
                    File file = new File(filePath);
                    long fileSizeInBytes = file.length();
                    Log.d("fileSizeInBytes", String.valueOf(fileSizeInBytes));
                    long fileSizeInKB = fileSizeInBytes / 1024;
                    long fileSizeInMB = fileSizeInKB / 1024;
                    Log.d("fileSizeInMB", String.valueOf(fileSizeInMB));
                    mArrayUri.add(filePath);
                }
                if(adpter==null) {
                    adpter = new ImagesRecyclerView(getActivity(), mArrayUri, LeadGeneration.this);
                    recyclerView.setAdapter(adpter);
                }
                else{
                    adpter.notifyDataSetChanged();
                }
            }
        }
    }
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Video.Media.DATA };
        Cursor cursor = getActivity().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    public  String getRealPathImageFromUri(Uri uri) {
        String realPath="";
// SDK < API11
        if (Build.VERSION.SDK_INT < 11) {
            String[] proj = { MediaStore.Images.Media.DATA };
            @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(uri, proj, null, null, null);
            int column_index = 0;
            String result="";
            if (cursor != null) {
                column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                realPath=cursor.getString(column_index);
            }
        }
        // SDK >= 11 && SDK < 19
        else if (Build.VERSION.SDK_INT < 19){
            String[] proj = { MediaStore.Images.Media.DATA };
            CursorLoader cursorLoader = new CursorLoader(getActivity(), uri, proj, null, null, null);
            Cursor cursor = cursorLoader.loadInBackground();
            if(cursor != null){
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                realPath = cursor.getString(column_index);
            }
        }
        // SDK > 19 (Android 4.4)
        else{
            final String docId = DocumentsContract.getDocumentId(uri);
            final String[] split = docId.split(":");
            final String type = split[0];
            if ("image".equals(type)) {
                Log.d("contentUriType","image");
                String wholeID = DocumentsContract.getDocumentId(uri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                String[] column = { MediaStore.Images.Media.DATA };
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);
                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        realPath = cursor.getString(columnIndex);
                        Log.d("realPath",realPath);
                    }
                    cursor.close();
                }
            } else if ("video".equals(type)) {
                Log.d("contentUriType","video");
                String wholeID = DocumentsContract.getDocumentId(uri);
                // Split at colon, use second item in the array
                String id = wholeID.split(":")[1];
                String[] column = { MediaStore.Video.Media.DATA };
                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";
                Cursor cursor = getActivity().getContentResolver().query(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, column, sel, new String[]{ id }, null);
                int columnIndex = 0;
                if (cursor != null) {
                    columnIndex = cursor.getColumnIndex(column[0]);
                    if (cursor.moveToFirst()) {
                        realPath = cursor.getString(columnIndex);
                        Log.d("realPath",realPath);
                    }
                    cursor.close();
                }
            }


        }
        return realPath;
    }
    private static boolean isGoogleDriveUri(Uri uri) {
        return "com.google.android.apps.docs.storage".equals(uri.getAuthority()) || "com.google.android.apps.docs.storage.legacy".equals(uri.getAuthority());
    }
    private static String getDriveFilePath(Uri uri, Context context) {
        Uri returnUri = uri;
        Cursor returnCursor = context.getContentResolver().query(returnUri, null, null, null, null);

        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();
        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));
        File file = new File(context.getCacheDir(), name);
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);
            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = inputStream.available();

            //int bufferSize = 1024;
            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            inputStream.close();
            outputStream.close();
            Log.e("File Path", "Path " + file.getPath());
            Log.e("File Size", "Size " + file.length());
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
        return file.getPath();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==GALLERY) {
            {
                if ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                    galleryIntent.setType("image/*");
                    galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                    galleryIntent.setAction(Intent.ACTION_GET_CONTENT);

                    startActivityForResult(galleryIntent, GALLERY);
                    Log.d("Permission", "Granted");
                } else {

                    Log.d("Permission", "Denied");
                    for (int i = 0, len = permissions.length; i < len; i++) {
                        String permission = permissions[i];
                        if (i == 0) {
                            if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                                boolean showRationale = shouldShowRequestPermissionRationale(permission);
                                if (!showRationale) {

                                    Log.d("NEVER ASK", "AGAIN CHECKED");
                                    alertDialog("AppPermission");

                                } else if (Manifest.permission.CAMERA.equals(permission)) {
                                    Log.d("NEVER ASK", "AGAIN NOT CHECKED");

                                } else {
                                    Log.d("GRANTED", "PERMISSION");

                                }
                            }
                        }


                    }
                }
            }
        }

    }
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void alertDialog(String type){
        final AlertDialog alertDialog;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = Objects.requireNonNull(getActivity()).getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        dialogBuilder.setView(dialogView);

        TextView yes=dialogView.findViewById(R.id.yesText);
        TextView no=dialogView.findViewById(R.id.noText);


        alertDialog = dialogBuilder.create();
        no.setOnClickListener(v -> alertDialog.dismiss());
        TextView msg=dialogView.findViewById(R.id.msg);


        if(type.equals("AppPermission")){
            msg.setText("Require Permission to your storage");
            yes.setText("Permission");
        }

        alertDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        yes.setOnClickListener(v -> {
            if(type.equals("addUser")){
            alertDialog.dismiss(); //use to dismiss the dialog box

            boolean val=dataBaseHelper.addUser(Objects.requireNonNull(fName.getText()).toString(),
                    Objects.requireNonNull(lName.getText()).toString(),
                    Objects.requireNonNull(contact.getText()).toString(), Objects.requireNonNull(projectName.getText()).toString()
                    , Objects.requireNonNull(flatDetails.getText()).toString(), Objects.requireNonNull(propertCost.getText()).toString()
                    , Objects.requireNonNull(loanRequirements.getText()).toString(),state.getText().toString(),city.getText().toString());
            if(val) {

                addImages();
            }
            }
            else{
                final String appPackageName = getActivity().getPackageName();
                alertDialog.dismiss();
                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                intent.setData(Uri.parse("package:" + appPackageName));
                startActivity(intent);
            }
        });
        no.setOnClickListener(v -> {
            alertDialog.dismiss(); //use to dismiss the dialog box
        });
        alertDialog.show();

    }
   private void addImages(){
        boolean val;
        for(int i=0;i<mArrayUri.size();i++){
          val=dataBaseHelper.addImages(mArrayUri.get(i));

        }
       fName.setText("");
       lName.setText("");
       contact.setText("");
       projectName.setText("");
       flatDetails.setText("");
       propertCost.setText("");
       loanRequirements.setText("");
       state.setText("");
       city.setText("");
       mArrayUri.clear();
       adpter.notifyDataSetChanged();
       adpter=null;

       Snackbar snackbar1 = Snackbar.make(layout, "Profile Saved SuccessFully", Snackbar.LENGTH_SHORT);
       snackbar1.show();
   }
   private  void getAccessToken(){
       Call<JsonObject> call = RetrofitInstances.getInstance().getMyApi().getaccesstoken();
       call.enqueue(new Callback<JsonObject>() {
           @Override
           public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
               if(response.isSuccessful()&& response.body()!=null){
                  JsonObject object=response.body();
                  auth_token=object.get("auth_token").toString().replaceAll("\"", "");
                  getStateList();
               }
               else{
                   Toast.makeText(getActivity(), "Error is app"+response.message(), Toast.LENGTH_SHORT).show();

               }
           }

           @Override
           public void onFailure(Call<JsonObject> call, Throwable t) {
               Toast.makeText(getActivity(), "Error is app"+t.toString(), Toast.LENGTH_SHORT).show();

           }
       });

   }
    private void  getStateList(){
        arrayList=new ArrayList();
        auth_token="Bearer "+auth_token;
        Call<JsonArray> call = RetrofitInstances.getInstance().getMyApi().getStateList(auth_token,Accept);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if(response.isSuccessful()&&response.body()!=null) {
                    JsonArray jsonArray = response.body();
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                        String state_name = jsonObject.get("state_name").toString().replaceAll("\"", "");
                        arrayList.add(state_name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getActivity(), R.layout.textview_layout, arrayList);
                    state.setAdapter(adapter);
                    city.setEnabled(true);

                }
                else{
                    Toast.makeText(getActivity(), "Error is app"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error is app"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void  getCityList(String stateName){
         stateName="cities/"+stateName;
         arrayList=new ArrayList();
        Call<JsonArray> call = RetrofitInstances.getInstance().getMyApi().getCityList(stateName,auth_token,Accept);
        call.enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(@NonNull Call<JsonArray> call, @NonNull Response<JsonArray> response) {
                if(response.isSuccessful()&&response.body()!=null) {
                    JsonArray jsonArray = response.body();
                    Log.d("getCityList", String.valueOf(jsonArray));
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JsonObject jsonObject = (JsonObject) jsonArray.get(i);
                        String city_name = jsonObject.get("city_name").toString().replaceAll("\"", "");
                        arrayList.add(city_name);
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>
                            (getActivity(), R.layout.textview_layout, arrayList);
                    city.setAdapter(adapter);

                }
                else{
                    Toast.makeText(getActivity(), "Error is app"+response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<JsonArray> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Error is app"+t.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private Boolean checkLength(TextInputEditText editText) {
            if (editText.length() == 0) {
//                editText.setBackground(getActivity().getDrawable(R.drawable.error_box));
                addDetailsError.setVisibility(View.VISIBLE);
                return false;
            } else {
                return true;
            }


    }
    private Boolean checkLength(AutoCompleteTextView autoCompleteTextView) {
        if (autoCompleteTextView.length() == 0) {
//            autoCompleteTextView.setBackground(getActivity().getDrawable(R.drawable.error_box));

            addDetailsError.setVisibility(View.VISIBLE);
            return false;
        } else {
            return true;
        }


    }

    @Override
    public void deleteImage(int position) {
       mArrayUri.remove(position);
       adpter.notifyItemRemoved(position);
       adpter.notifyItemRangeChanged(position,mArrayUri.size());
    }
}