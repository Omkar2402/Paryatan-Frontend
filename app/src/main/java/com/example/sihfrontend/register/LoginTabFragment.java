package com.example.sihfrontend.register;

        import android.content.Intent;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.fragment.app.Fragment;

        import com.example.sihfrontend.R;

        import org.json.JSONException;
        import org.json.JSONObject;

        import java.io.IOException;

        import okhttp3.Call;
        import okhttp3.Callback;
        import okhttp3.FormBody;
        import okhttp3.MediaType;
        import okhttp3.MultipartBody;
        import okhttp3.OkHttpClient;
        import okhttp3.Request;
        import okhttp3.RequestBody;
        import okhttp3.Response;

// Inflater: convert xml file into a view
//View Group : Adding fragment to activity parent class for all views
public class LoginTabFragment extends Fragment {

    private EditText email;
    private EditText password;
    private TextView forgotPassword;
    private Button login;
    private OkHttpClient client;
    loginhttp loginHttp= new loginhttp();

    float v =0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        client = new OkHttpClient();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.login_tab_fragment,container,false);
        email = root.findViewById(R.id.etEmail);
        password = root.findViewById(R.id.etPassword);
        forgotPassword = root.findViewById(R.id.tvForgotPassword);
        login = root.findViewById(R.id.btnLogin);

        email.setTranslationX(800);
        password.setTranslationX(800);
        forgotPassword.setTranslationX(800);
        login.setTranslationX(800);

        email.setAlpha(v);
        password.setAlpha(v);
        forgotPassword.setAlpha(v);
        login.setAlpha(v);

        email.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(300).start();
        password.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        forgotPassword.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(500).start();
        login.animate().translationX(0).alpha(1).setDuration(1000).setStartDelay(800).start();


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Clicked","Login BTN");
                String auth=null;
                loginHttp.login(email.getText().toString(),password.getText().toString(),"admin",auth);
                Log.d("Stop","Left Login");
            }
        });

        return  root;


    }
}