package tugasbesarpbp_f_keld.com.tugasbesarpbp_f_keld;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail;
    private EditText editTextPassword;
    private EditText etName;
    private EditText etUsername;
    private EditText etPhone;
    private Button buttonSignup;
    private Button buttonBack;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;
    //RegisterActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //initializing firebase auth object
        firebaseAuth = firebaseAuth.getInstance();

        //if getCurrentUser does not returns null
        if(firebaseAuth.getCurrentUser() != null){
            //that means user is already logged in
            //so close this activity
            finish();

            //and open profile activity
            startActivity(new Intent(getApplicationContext(), MenuUtama.class));
        }

        //initializing views
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        etName = (EditText) findViewById(R.id.etName);
        etPhone = (EditText) findViewById(R.id.etPhone);
        etUsername = (EditText) findViewById(R.id.etUsername);

        buttonBack = (Button) findViewById(R.id.btnBack);
        buttonSignup = (Button) findViewById(R.id.buttonSignup);

        progressDialog = new ProgressDialog(this);

        //attaching listener to button
//        buttonSignup.setOnClickListener(this);
//        buttonBack.setOnClickListener(this);

        buttonSignup.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                final String email = editTextEmail.getText().toString();
                final String password  = editTextPassword.getText().toString();
                final String username = etUsername.getText().toString();
                Query usernameQuery = FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("username").equalTo(username);
                usernameQuery.addListenerForSingleValueEvent(new ValueEventListener() {

                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getChildrenCount()>0){
                            Toast.makeText(RegisterActivity.this, "Username telah terdaftar, silahkan menggunakan username yang lain", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(!task.isSuccessful()) {
                                        Toast.makeText(RegisterActivity.this,"Sign Up Error", Toast.LENGTH_SHORT).show();
                                    }
                                    else
                                    {
                                        progressDialog.setMessage("Registering Please Wait...");
                                        progressDialog.show();
                                        String user_id = firebaseAuth.getCurrentUser().getUid();
                                        DatabaseReference current_user_db = FirebaseDatabase.getInstance().getReference().child("Users").child(user_id);

                                        String name = etName.getText().toString();
                                        String phone  = etPhone.getText().toString();
                                        String username = etUsername.getText().toString();
                                        Map newPost = new HashMap();
                                        newPost.put("name",name);
                                        newPost.put("phone",phone);
                                        newPost.put("username",username);


                                        current_user_db.setValue(newPost);
                                        Toast.makeText(RegisterActivity.this,"Sign Up Success, Redirect to Login", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


//    private void registerUser(){
//        String email = editTextEmail.getText().toString().trim();
//        String password  = editTextPassword.getText().toString().trim();
//
//        //checking if email and passwords are empty
//        if(TextUtils.isEmpty(email)){
//            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if(TextUtils.isEmpty(password)){
//            Toast.makeText(this,"Please enter password",Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        //if the email and password are not empty
//        //displaying a progress dialog
//
//        progressDialog.setMessage("Registering Please Wait...");
//        progressDialog.show();
//
//        //creating a new user
//        firebaseAuth.createUserWithEmailAndPassword(email, password)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        //checking if success
//                        if(task.isSuccessful()){
//                            finish();
//                            startActivity(new Intent(getApplicationContext(), MenuUtama.class));
//                        }else{
//                            //display some message here
//                            Toast.makeText(RegisterActivity.this,"Registration Error",Toast.LENGTH_LONG).show();
//                        }
//                        progressDialog.dismiss();
//                    }
//                });
//
    }

       public void Back(View view) {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
    }
//
//    @Override
//    public void onClick(View v) {
//        if(v == buttonSignup){
//            registerUser();
//        }
//
//        if(v == buttonBack){
//            //open login activity when user taps on the already registered textview
//            startActivity(new Intent(this, MainActivity.class));
//        }
//    }
}
