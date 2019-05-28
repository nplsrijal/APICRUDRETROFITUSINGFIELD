package com.example.apicrudretrofitusingfield;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.apicrudretrofitusingfield.apiemployee.EmployeeApi;
import com.example.apicrudretrofitusingfield.model.Employee;
import com.example.apicrudretrofitusingfield.url.Url;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdateActivity extends AppCompatActivity {
    private EditText txtsearchid, txtename, txtesalary, txteage;
    private Button btnsearchemp, btnupdate, btndelete;


    Retrofit retrofit;
    EmployeeApi employeeAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        txtsearchid = findViewById(R.id.txtsearchid);
        txtename = findViewById(R.id.txtename);
        txtesalary = findViewById(R.id.txtesalary);
        txteage = findViewById(R.id.txteage);
        btnsearchemp = findViewById(R.id.btnsearchemp);
        btnupdate = findViewById(R.id.btnupdate);
        btndelete = findViewById(R.id.btndelete);

        btnsearchemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadData();

            }
        });

        btnupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateEmployee();
            }
        });

        btndelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteEmployee();
            }
        });
    }

    private void CreateInstance() {
        retrofit = new Retrofit.Builder()
                .baseUrl(Url.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        employeeAPI = retrofit.create(EmployeeApi.class);
    }

    private void updateEmployee() {
        CreateInstance();
           int id=Integer.parseInt(txtsearchid.getText().toString());
           String name=txtename.getText().toString();
           int age=Integer.parseInt(txteage.getText().toString());
           float salary= Float.parseFloat(txtesalary.getText().toString());

        Call<Void> voidCall = employeeAPI.updateEmployee(id,name,salary,age);

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateActivity.this, "Updated Successfully", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void deleteEmployee() {
        CreateInstance();
        Call<Void> voidCall = employeeAPI.deleteEmployee(Integer.parseInt(txtsearchid.getText().toString()));

        voidCall.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                Toast.makeText(UpdateActivity.this, "Deleted Successfully", Toast.LENGTH_SHORT).show();
                txtename.setText("");
                txtesalary.setText("");
                txteage.setText("");
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void loadData() {
        CreateInstance();
        Call<Employee> listCall = employeeAPI.getEmployeeByID(Integer.parseInt(txtsearchid.getText().toString()));

        listCall.enqueue(new Callback<Employee>() {
            @Override
            public void onResponse(Call<Employee> call, Response<Employee> response) {

                if (!response.isSuccessful()) {
                    Toast.makeText(UpdateActivity.this, "" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                txtename.setText(response.body().getEmployee_name());
                float salary = response.body().getEmployee_salary();
                txtesalary.setText(Float.toString(salary));
               txteage.setText(Integer.toString(response.body().getEmployee_age()));
            }

            @Override
            public void onFailure(Call<Employee> call, Throwable t) {
                Toast.makeText(UpdateActivity.this, "Error" + t.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }


}
