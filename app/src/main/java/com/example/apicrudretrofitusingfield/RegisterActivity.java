package com.example.apicrudretrofitusingfield;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apicrudretrofitusingfield.apiemployee.EmployeeApi;
import com.example.apicrudretrofitusingfield.model.ImageResponse;
import com.example.apicrudretrofitusingfield.url.Url;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RegisterActivity extends AppCompatActivity {


    private EditText txtname,txtsalary,txtage;
    private Button btnregister;
    private ImageView img;
    String imagePath;
    String imageName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        txtname=findViewById(R.id.txtname);
        txtsalary=findViewById(R.id.txtsalary);
        txtage=findViewById(R.id.txtage);
        btnregister=findViewById(R.id.btnregister);
        img=findViewById(R.id.img);

        btnregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    Register();
                }
            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });
    }

    private void BrowseImage() {
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode==RESULT_OK){
            if(data == null){
                Toast.makeText(this,"Please Select Image",Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri =data.getData();
        imagePath = getRealPathFromUri(uri);

        previewImage(imagePath);



    }



    private String getRealPathFromUri(Uri uri){
        String[] projection ={MediaStore.Images.Media.DATA};

        CursorLoader loader = new CursorLoader(getApplicationContext(),uri,projection,null,null,null);
        Cursor cursor =loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }
    private void previewImage(String imagePath){
        File imgFile= new File(imagePath);
        if(imgFile.exists()){
            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            img.setImageBitmap(myBitmap);



        }
    }
    private void Register() {
        SaveImageOnly();

        String name=txtname.getText().toString();
        String salary=txtsalary.getText().toString();
        String age=txtage.getText().toString();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmployeeApi employeeAPI =retrofit.create(EmployeeApi.class);
        Map<String,String> map=new HashMap<>();
        map.put("name",name);
        map.put("age",age);
        map.put("salary",salary);
        map.put("image","");

        //Call<Void> voidCall = employeeAPI.registerEmployee(name,age,salary,imageName);
        Call<Void> voidCall = employeeAPI.register(map);

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(RegisterActivity.this,"Registered Successfully",Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,"Error"+t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    private boolean validate(){
        boolean valid=true;
        if(TextUtils.isEmpty(txtname.getText())){
            txtname.setError("Required");
            valid=false;
        }
        if(TextUtils.isEmpty(txtsalary.getText())){
            txtsalary.setError("Required");
            valid=false;
        }
        if(TextUtils.isEmpty(txtage.getText())){
            txtage.setError("Required");
            valid=false;
        }


        return valid;

    }

    private void StrictMode(){
        StrictMode.ThreadPolicy policy=new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    }

    private void SaveImageOnly(){
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),file);
        MultipartBody.Part body= MultipartBody.Part.createFormData("imageFile",file.getName(),requestBody);
       EmployeeApi employeeApi=Url.getInstance().create(EmployeeApi.class);
        Call<ImageResponse> responseBodyCall = employeeApi.uploadImage(body);
        StrictMode();
        try{
            Response<ImageResponse> imageResponseResponse =responseBodyCall.execute();
            imageName=imageResponseResponse.body().getFilename();
            Toast.makeText(this, imageName, Toast.LENGTH_SHORT).show();
        }catch (IOException e){
            Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }


}
