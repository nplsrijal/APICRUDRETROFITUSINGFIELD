package com.example.apicrudretrofitusingfield.apiemployee;

import com.example.apicrudretrofitusingfield.model.Employee;
import com.example.apicrudretrofitusingfield.model.ImageResponse;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EmployeeApi {

    @GET("employee")
    Call<List<Employee>> getEmployee();

    @GET("employee/{empID}")
    Call<Employee> getEmployeeByID(@Path("empID") int empId);

    @FormUrlEncoded
    @POST("employee/create")
    Call<Void>registerEmployee(@Field("name") String name,@Field("age") int age,@Field("salary") float salary,@Field("imageName") String imageName);

    @FormUrlEncoded
    @POST("employee/update")
    Call<Void>updateEmployee(@Field("id") int id,@Field("name") String name,@Field("salary") float salary,@Field("age") int age);

    @DELETE("employee/delete/{empID}")
    Call<Void> deleteEmployee(@Path("empID") int empId);

    @Multipart
    @POST("upload")
    Call<ImageResponse> uploadImage(@Part MultipartBody.Part img);

    @FormUrlEncoded
    @POST("employee/create")
    Call<Void> register(@FieldMap Map<String,String> map);

}
