package com.example.sun.keepfit.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.keepfit.Bean.user;
import com.example.sun.keepfit.R;

import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView textView_signin;
    private TextView textView_forget_password;
    private Button button_login;
    private EditText editText_account;
    private EditText editText_password;

    private static final String BMOB_APP_ID = "eae319e214f8fdddbce305ef4e485465";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        Bmob.initialize(getApplicationContext(),BMOB_APP_ID);
        initView();
        bindingEvent();
    }

    private void initView(){
        textView_signin = (TextView) findViewById(R.id.textView_signin);
        textView_forget_password = (TextView) findViewById(R.id.textView_forget_password);
        button_login = (Button) findViewById(R.id.button_login);
        editText_account = (EditText) findViewById(R.id.editText_account);
        editText_password = (EditText) findViewById(R.id.editText_login_passwpord);
    }
    private void  bindingEvent()
    {
        button_login.setOnClickListener(this);
        textView_forget_password.setOnClickListener(this);
        textView_signin.setOnClickListener(this);
    }    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_login:
                login();
                break;
            case R.id.textView_forget_password:
                break;
            case R.id.textView_signin:
                Intent intent = new Intent(this,SigninActivity.class);
                startActivity(intent);
//                finish();
                break;
        }
    }
    public void login(){
        String phone = editText_account.getText().toString().trim();
//        System.out.println(password+phone);
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("account",phone);
        query.findObjects(getApplicationContext(), new FindListener<user>() {
            @Override
            public void onSuccess(List<user> list) {
                if(list.size()==0) {
                    System.out.println("用户为空");
                    showToast("用户不存在！请注册！");
                }
                else{
                    System.out.println("用户不为空！");
                    String password = editText_password.getText().toString().trim();
                    user user = list.get(0);
                    if(user.getPassword().equals(password))
                    {
                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else
                        showToast("密码错误");
                }
//
            }
            @Override
            public void onError(int i, String s) {
//                                    Toast.makeText(context,"查询失败！"+s,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void showToast(String s){
        Toast.makeText(getApplicationContext(),s,Toast.LENGTH_SHORT).show();
    }
}
