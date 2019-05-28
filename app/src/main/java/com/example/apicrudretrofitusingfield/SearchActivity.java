package com.example.apicrudretrofitusingfield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.apicrudretrofitusingfield.apiemployee.EmployeeApi;
import com.example.apicrudretrofitusingfield.model.Employee;
import com.example.apicrudretrofitusingfield.url.Url;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SearchActivity extends AppCompatActivity {

    private EditText txtempid;
    private TextView tvData;
    private Button btnsearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        txtempid=findViewById(R.id.txtempid);
        tvData=findViewById(R.id.tvData1);
        btnsearch=findViewById(R.id.btnsearch1);


        btnsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    loadData();

                }
            }
        });
    }


    private void loadData() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        EmployeeApi employeeAPI =retrofit.create(EmployeeApi.class);
        Call<Employee> listCall = employeeAPI.getEmployeeByID(Integer.parseInt(txtempid.getText().toString()));

        listCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {

                if(!response.isSuccessful())
                {
                    Toast.makeText(SearchActivity.this, "Error" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }

                String content="";
                content +="ID: "+response.body().getId() + "\n";
                content +="Employee Name: "+response.body().getEmployee_name() + "\n";
                content +="Employee Age: "+response.body().getEmployee_age() + "\n";
                content +="Employee Salary: "+response.body().getEmployee_salary() + "\n";
                content +="........................."+ "\n";
                tvData.append(content);
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(SearchActivity.this,t.getLocalizedMessage(),Toast.LENGTH_SHORT).show();

                tvData.setText(t.getLocalizedMessage());
            }
        });
    }

    private boolean validate(){
        boolean valid=true;
        if(TextUtils.isEmpty(txtempid.getText())){
            txtempid.setError("Required");
            valid=false;
        }
        return valid;

    }
}
