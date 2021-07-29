package com.sensing.acoustic;

/* 下面内容来自 https://blog.csdn.net/wwb1990/article/details/104053465 */

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.chaquo.python.Kwarg;
import com.chaquo.python.PyObject;
import com.chaquo.python.android.AndroidPlatform;
import com.chaquo.python.Python;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView text, resultText, resultSqrt, randomInt;
    private Button calculate, doSqrt, newRandom;
    private EditText input1, input2;
    private Spinner operators;
    private String operator;
    private Python py;
    private Integer randInt;

    static final String TAG = "zhongen";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text = findViewById(R.id.text);
        resultText = findViewById(R.id.result);
        input1 = findViewById(R.id.input1);
        input2 = findViewById(R.id.input2);
        operators = findViewById(R.id.spinner);
        operators.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                operator = adapterView.getItemAtPosition(i).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        calculate = findViewById(R.id.calculate);
        randomInt = findViewById(R.id.randomInt);
        resultSqrt = findViewById(R.id.result_sqrt);
        doSqrt = findViewById(R.id.do_sqrt);
        newRandom = findViewById(R.id.getRandom);

        initPython();
        callPythonCode();

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double val1 = Double.valueOf(input1.getText().toString());
                double val2 = Double.valueOf(input2.getText().toString());
                double res = 0;
                PyObject obj = py.getModule("test").callAttr("calculator", new Kwarg("x", val1), new Kwarg("y", val2), new Kwarg("ope", operator));
                Double result = obj.toJava(Double.class);
                Log.d("zhongen", "result = " + result);
                resultText.setText("Result: " + result);
            }
        });

        PyObject obj1 = py.getModule("test").callAttr("get_random");
        randInt = obj1.toJava(Integer.class);
        Log.d(TAG,"RandInt: "+ randInt.toString());
        randomInt.setText("RandInt: " + randInt);

        newRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PyObject obj1 = py.getModule("test").callAttr("get_random");
                randInt = obj1.toJava(Integer.class);
                Log.d(TAG,"RandInt: "+ randInt.toString());
                randomInt.setText("RandInt: " + randInt);
            }
        });

        doSqrt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PyObject obj2 = py.getModule("test").callAttr("get_sqrt", new Kwarg("x", randInt));
                Double sqrt = obj2.toJava(Double.class);
                Log.d(TAG,"Sqrt: "+ sqrt.toString());

                resultSqrt.setText(sqrt.toString());
            }
        });

    }
    // 初始化Python环境
    void initPython(){
        if (! Python.isStarted()) {
            Python.start(new AndroidPlatform(this));
        }
        py = Python.getInstance();
    }
    // 调用python代码
    void callPythonCode(){
        // 调用hello.py模块中的greet函数，并传一个参数
        // 等价用法：py.getModule("hello").get("greet").call("Android");
        py.getModule("hello").callAttr("greet", "Android");

        // 调用python内建函数help()，输出了帮助信息
        py.getBuiltins().get("help").call();

        PyObject obj1 = py.getModule("hello").callAttr("add", 2,3);
        // 将Python返回值换为Java中的Integer类型
        Integer sum = obj1.toJava(Integer.class);
        Log.d(TAG,"add = "+sum.toString());

        // 调用python函数，命名式传参，等同 sub(10,b=1,c=3)
        PyObject obj2 = py.getModule("hello").callAttr("sub", 10,new Kwarg("b", 1), new Kwarg("c", 3));
        Integer result = obj2.toJava(Integer.class);
        Log.d(TAG,"sub = "+result.toString());

        // 调用Python函数，将返回的Python中的list转为Java的list
        PyObject obj3 = py.getModule("hello").callAttr("get_list", 10,"xx",5.6,'c');
        List<PyObject> pyList = obj3.asList();
        Log.d(TAG,"get_list = "+pyList.toString());

        // 将Java的ArrayList对象传入Python中使用
        List<PyObject> params = new ArrayList<PyObject>();
        params.add(PyObject.fromJava("alex"));
        params.add(PyObject.fromJava("bruce"));
        py.getModule("hello").callAttr("print_list", params);

        // Python中调用Java类
        PyObject obj4 = py.getModule("hello").callAttr("get_java_bean");
        JavaBean data = obj4.toJava(JavaBean.class);
        data.print();
    }
}