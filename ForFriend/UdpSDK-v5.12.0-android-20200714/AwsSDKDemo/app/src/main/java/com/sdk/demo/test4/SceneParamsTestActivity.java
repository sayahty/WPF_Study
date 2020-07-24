package com.sdk.demo.test4;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.games.android.test1.R;
import com.games.sdk.SdkPlatform;

import java.util.HashMap;
import java.util.Map;

public class SceneParamsTestActivity extends AppCompatActivity implements View.OnClickListener {
    TextView result ;
    EditText key,value;
    Map<String,Object> myParams = new HashMap<>();
    String keyStr = "";
    String valueStr = "";
    Button add_param_btn,remove_param_bykey_btn,clear_param_btn,get_all_param_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scene_params_test);
        result = findViewById(R.id.game_test_result);
        key = findViewById(R.id.game_test_key);
        value = findViewById(R.id.game_test_value);
        add_param_btn = findViewById(R.id.game_test_add_param);
        remove_param_bykey_btn = findViewById(R.id.game_test_remove_param);
        clear_param_btn = findViewById(R.id.game_test_clear_param);
        get_all_param_btn = findViewById(R.id.game_test_get_params);
        add_param_btn.setOnClickListener(this);
        remove_param_bykey_btn.setOnClickListener(this);
        clear_param_btn.setOnClickListener(this);
        get_all_param_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        keyStr = key.getText().toString();
        valueStr = value.getText().toString();
        switch (v.getId()){
            case R.id.game_test_add_param:
                if (TextUtils.isEmpty(keyStr)) {
                    Toast.makeText(SceneParamsTestActivity.this, "请输入key值", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(valueStr)) {
                    Toast.makeText(SceneParamsTestActivity.this, "请输入value值", Toast.LENGTH_SHORT).show();
                    return;
                }
                SdkPlatform.setSceneParam(keyStr,valueStr);
                break;
            case R.id.game_test_remove_param:
                if (TextUtils.isEmpty(keyStr)) {
                    Toast.makeText(SceneParamsTestActivity.this, "请输入key值", Toast.LENGTH_SHORT).show();
                    return;
                }
                result.setText("清除"+keyStr+(SdkPlatform.removeSceneParam(keyStr)?"成功":"失败"));
                break;
            case R.id.game_test_clear_param:
                result.setText("清除所有属性:"+(SdkPlatform.clearSceneParams()?"成功":"失败"));
                break;
            case R.id.game_test_get_params:
                showAllParam();
                break;
                default:
                    break;
        }
    }
    private void showAllParam(){
        Map<String,Object> resultMap = SdkPlatform.getSceneParams();
        if (resultMap.isEmpty()) {
            result.setText("当前没有设置param");
            return;
        }
        StringBuilder builder = new StringBuilder("Result:\n");
        for (Map.Entry<String,Object> entry:resultMap.entrySet()){
            builder.append(entry.getKey()+":"+entry.getValue()).append("\n");
        }
        result.setText(builder.toString());
    }
}
