package com.example.sun.keepfit.View;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sun.keepfit.Bean.user;
import com.example.sun.keepfit.R;

import java.util.List;
import java.util.regex.Pattern;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class SigninActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String APP_KEY = "19278abdfb0d8";
    private static final String APP_SECRECT = "ba5b4bbebcafb68382471bba4758795f";

    private static final String BMOB_APP_ID = "eae319e214f8fdddbce305ef4e485465";
    private ImageButton back;
    private Button button_signin;
    private TextView textView_sendcode;
    private TextView textView_goto_login;
    private EditText editText_phone;
    private EditText editText_password;
    private EditText editText_code;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_signin);
        Bmob.initialize(getApplicationContext(),BMOB_APP_ID);
        initSMSSDK();
        initView();
        bindingEvent();
    }
    /**
     * 使用计时器来限定验证码
     * 在发送验证码的过程 不可以再次申请获取验证码 在指定时间之后没有获取到验证码才能重新进行发送
     * 这里限定的时间是60s
     */
    private CountDownTimer timer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            textView_sendcode.setEnabled(false);
            textView_sendcode.setText((millisUntilFinished / 1000) + "秒后可重发");
        }
        @Override
        public void onFinish() {
            textView_sendcode.setEnabled(true);
            textView_sendcode.setText("获取验证码");
        }
    };
    private void initSMSSDK(){
        SMSSDK.initSDK(this,APP_KEY,APP_SECRECT);
        //注册短信回调
        SMSSDK.registerEventHandler(new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                switch (event) {
                    case SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
                            System.out.println("验证成功");
                            signin();
                        } else {
                            System.out.println("验证失败");
                        }
                        break;
                    case SMSSDK.EVENT_GET_VERIFICATION_CODE:
                        if (result == SMSSDK.RESULT_COMPLETE) {
//                            Toast.makeText(getApplicationContext(),"验证码已发到你的手机",Toast.LENGTH_SHORT).show();
                        } else {
//                            Toast.makeText(getApplicationContext(),"获取验证码失败！",Toast.LENGTH_SHORT).show();
                        }
                        break;
                }
            }
        });
    }

    private void initView(){
        back = (ImageButton) findViewById(R.id.imageButton_signin_go_back);
        button_signin = (Button) findViewById(R.id.button_signin);
        textView_sendcode = (TextView) findViewById(R.id.textView_send_code);
        textView_goto_login = (TextView) findViewById(R.id.textView_login);
        editText_phone = (EditText) findViewById(R.id.editText_phone_num);
        editText_password = (EditText) findViewById(R.id.editText_password);
        editText_code = (EditText) findViewById(R.id.editText_code);
    }
    private void bindingEvent(){
        button_signin.setOnClickListener(this);
        textView_goto_login.setOnClickListener(this);
        textView_sendcode.setOnClickListener(this);
        back.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button_signin:
//                login();
                submitInfo();
                //注册
                break;
            case R.id.textView_login:
            case R.id.imageButton_signin_go_back:
                finish();
                //跳转到登录界面
                break;
            case R.id.textView_send_code:
                String phone = editText_phone.getText().toString().trim();
                if(!isPhoneNumber(phone)) {
                    showToast("请输入有效的手机号码！");
                    break;
                }
                //发送验证码
                textView_sendcode.requestFocus();
                //启动获取验证码 86是中国
//                String phone = editText_phone.getText().toString().trim();
                SMSSDK.getVerificationCode("86", phone);//发送短信验证码到手机号
                timer.start();//使用计时器 设置验证码的时间限制
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterAllEventHandler();
    }
    /**
     * 验证用户的其他信息
     * 这里验证两次密码是否一致 以及验证码判断
     */
    private void submitInfo() {
        //密码验证
        String phone = editText_phone.getText().toString().trim();
        String code = editText_code.getText().toString().trim();
        SMSSDK.submitVerificationCode("86", phone, code);//提交验证码  在eventHandler里面查看验证结果
    }
    public void signin(){
        final String phone = editText_phone.getText().toString().trim();
        final String password = editText_password.getText().toString().trim();
        System.out.println(password);
        BmobQuery<user> query = new BmobQuery<user>();
        query.addWhereEqualTo("account",phone);
        query.findObjects(getApplicationContext(), new FindListener<user>() {
            @Override
            public void onSuccess(List<user> list) {
                if(list.size()==0){
                    System.out.println("用户为空");
                    user user = new user();
                    user.setAccount(phone);
                    user.setPassword(password);
                    user.save(getApplicationContext(), new SaveListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onFailure(int i, String s) {

                        }
                    });
                }
                else{
                    System.out.println("用户不为空！");
                    showToast("该手机号码已经被注册！");
                }
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
    //判断是否为电话号码，返回布尔值
    private static boolean isPhoneNumber(String input) {
//        String regex_0 = "^(\\(\\d{3,4}\\)|\\d{3,4}-)?\\d{7,8}$";//电话号码
        String regex_1 = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";//手机号码
//        Pattern p = Pattern.compile(regex_0);
//        if (!p.matcher(input).matches())
            return Pattern.compile(regex_1).matcher(input).matches();
//        return p.matcher(input).matches();
    }
}
