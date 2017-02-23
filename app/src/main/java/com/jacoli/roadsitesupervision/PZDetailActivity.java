package com.jacoli.roadsitesupervision;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import java.util.HashMap;
import java.util.Map;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.OperatorListModel;
import com.jacoli.roadsitesupervision.services.PZDetailModel;
import com.jacoli.roadsitesupervision.services.Utils;
import com.jacoli.roadsitesupervision.views.MyToast;
import org.apmem.tools.layouts.FlowLayout;

public class PZDetailActivity extends CommonActivity {

    private String modelID;
    private PZDetailModel model;
    private OperatorListModel operatorListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pzdetail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");
        titleBar.setLeftClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        Intent intent = getIntent();
        modelID = intent.getStringExtra("id");
        if (MainService.getInstance().sendPZDetailQuery(modelID, handler)) {
            Toast.makeText(getBaseContext(), "获取旁站详情中", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "获取旁站详情失败", Toast.LENGTH_SHORT).show();
        }

        MainService.getInstance().sendOperatorListQuery(handler);

        Button submitBtn = (Button) findViewById(R.id.submit_btn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            submit();
            }
        });

        String name = "旁站项目： " + intent.getStringExtra("name");
        TextView textView = (TextView) findViewById(R.id.name_text);
        textView.setText(name);

        String projectName = "工序部位： " + intent.getStringExtra("project_name");
        TextView projectNameTextView = (TextView) findViewById(R.id.project_name_text);
        projectNameTextView.setText(projectName);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("旁站数据可能未保存，是否仍要返回上一页");

        builder.setPositiveButton("返回上一页", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.setNegativeButton("留在当前页面", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.create().show();
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_OPERATOR_LIST_SUCCESS:
                operatorListModel = (OperatorListModel) obj;
                initSpinerAfterFetchOperatorListSuccess();
                break;
            case MainService.MSG_QUERY_OPERATOR_LIST_FAILED:
                Toast.makeText(getBaseContext(), "获取操作人员列表失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_QUERY_PZ_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "获取旁站详情成功");
                model = (PZDetailModel) obj;
                initSubviewsAfterFetchSuccess();
                initSpinerAfterFetchOperatorListSuccess();
                break;
            case MainService.MSG_QUERY_PZ_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取旁站详情失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_SUBMIT_PZ_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "提交旁站详情成功");
                onSubmitSuccess();
                break;
            case MainService.MSG_SUBMIT_PZ_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "提交旁站详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }

    public void onSubmitSuccess() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示");
        builder.setMessage("提交旁站数据成功，是否留在当前页面");

        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
            }
        });

        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                finish();
            }
        });

        builder.create().show();
    }

    public void submit() {
        if (model == null || modelID.length() <= 0) {
            return;
        }

        try {
            Map<String, String> params = new HashMap<>();
            params.put("ZJY", model.getZJY());
            params.put("ZZAQY", model.getZZAQY());
            params.put("SYRY", model.getSYRY());

            for (PZDetailModel.PZRowModel rowModel : model.getItems()) {
                params.put(rowModel.getID(), rowModel.getValue());
            }

            // TODO other params

            MainService.getInstance().sendSubmitPZContentDetail(modelID, params, handler);
        }
        catch (Exception ex) {
            Log.e("PZDetailActivity", ex.toString());
        }
    }

    public void initSpinerAfterFetchOperatorListSuccess() {
        if (operatorListModel == null || model == null) {
            return;
        }

        if (!operatorListModel.getZJY().isEmpty()) {
            // 初始化控件
            Spinner spinner = (Spinner) findViewById(R.id.spinner0);

            // 建立Adapter并且绑定数据源
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, operatorListModel.getZJY());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //绑定 Adapter到控件
            spinner .setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    model.setZJY(operatorListModel.getZJY().get(pos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

            try {
                int index = operatorListModel.getZJY().indexOf(model.getZJY());
                if (index >= 0 && index < spinner.getCount())
                spinner.setSelection(index);
            }
            catch (Exception ex) {
                Log.e("PZDetailActivity", ex.toString());
            }
        }

        if (!operatorListModel.getZZAQY().isEmpty()) {
            // 初始化控件
            Spinner spinner = (Spinner) findViewById(R.id.spinner1);

            // 建立Adapter并且绑定数据源
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, operatorListModel.getZZAQY());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //绑定 Adapter到控件
            spinner .setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    model.setZZAQY(operatorListModel.getZZAQY().get(pos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

            try {
                int index = operatorListModel.getZJY().indexOf(model.getZZAQY());
                if (index >= 0 && index < spinner.getCount())
                    spinner.setSelection(index);
            }
            catch (Exception ex) {
                Log.e("PZDetailActivity", ex.toString());
            }
        }

        if (!operatorListModel.getSYRY().isEmpty()) {
            // 初始化控件
            Spinner spinner = (Spinner) findViewById(R.id.spinner2);

            // 建立Adapter并且绑定数据源
            ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item, operatorListModel.getSYRY());
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //绑定 Adapter到控件
            spinner .setAdapter(adapter);
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view,
                                           int pos, long id) {
                    model.setSYRY(operatorListModel.getSYRY().get(pos));
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Another interface callback
                }
            });

            try {
                int index = operatorListModel.getZJY().indexOf(model.getSYRY());
                if (index >= 0 && index < spinner.getCount())
                    spinner.setSelection(index);
            }
            catch (Exception ex) {
                Log.e("PZDetailActivity", ex.toString());
            }
        }
    }

    public void initSubviewsAfterFetchSuccess() {
        if (model == null) {
            return;
        }

        String sectionHeaderTitle = "";

        TableLayout tableLayout = (TableLayout) findViewById(R.id.table_layout);

        LayoutInflater inflater = getLayoutInflater();

        for (PZDetailModel.PZRowModel rowModel : model.getItems()) {
            // header
            if (rowModel.getName().equals(sectionHeaderTitle)) {
                // do nothing here
            }
            else {
                // add a section header
                TextView textView = (TextView) inflater.inflate(R.layout.pz_detail_section_header_layout, null);
                textView.setText(rowModel.getName());
                tableLayout.addView(textView);
            }
            sectionHeaderTitle = rowModel.getName();

            // rows
            switch (rowModel.getDataFormat()) {
                case "0":
                    addIntEditableToLayout(tableLayout, rowModel);
                    break;
                case "1":
                    addDoubleEditableToLayout(tableLayout, rowModel);
                    break;
                case "2":
                    addCheckToLayout(tableLayout, rowModel);
                    break;
                case "3":
                    addTextToLayout(tableLayout, rowModel);
                    break;
                case "4":
                    addIntAryToLayout(tableLayout, rowModel);
                    break;
                case "5":
                    addDoubleAryToLayout(tableLayout, rowModel);
                    break;
                default:
                    break;
            }
        }
    }

    public void addNumberInputToLayout(TableLayout layout, final PZDetailModel.PZRowModel rowModel, boolean isDecimal) {
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.pz_detail_int_input_row_layout, null);
        TextView titleView = (TextView) tableRow.findViewById(R.id.title_text_view);
        titleView.setText(rowModel.getSubName());
        TextView unitView = (TextView) tableRow.findViewById(R.id.unit_text_view);
        unitView.setText(rowModel.getDescription());

        EditText editText = (EditText)tableRow.findViewById(R.id.edit_text);
        if (isDecimal) {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }
        else {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        }
        editText.setText(rowModel.getValue());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                rowModel.setValue(s.toString());
            }
        });

        editText.clearFocus();

        layout.addView(tableRow);
    }

    public void addIntEditableToLayout(TableLayout layout, final PZDetailModel.PZRowModel rowModel) {
        addNumberInputToLayout(layout, rowModel, false);
    }

    public void addDoubleEditableToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        addNumberInputToLayout(layout, rowModel, true);
    }

    public void addCheckToLayout(TableLayout layout, final PZDetailModel.PZRowModel rowModel) {
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.pz_detail_check_row_layout, null);
        TextView titleView = (TextView) tableRow.findViewById(R.id.title_text_view);
        titleView.setText(rowModel.getSubName());

        // "#相符#不相符#" -> ["", "相符", "不相符"]
        String[] checkDescs = rowModel.getDescription().split("#");

        final CheckBox yesCheckBox = (CheckBox) tableRow.findViewById(R.id.yes_checkBox);
        if (checkDescs.length >= 2) {
            yesCheckBox.setText(checkDescs[checkDescs.length - 2]);
        }

        final CheckBox noCheckBox = (CheckBox) tableRow.findViewById(R.id.no_checkBox);
        if (checkDescs.length >= 2) {
            noCheckBox.setText(checkDescs[checkDescs.length - 1]);
        }

        if (rowModel.getValue().equalsIgnoreCase("Y")) {
            yesCheckBox.setChecked(true);
        }
        else if (rowModel.getValue().equalsIgnoreCase("N")) {
            noCheckBox.setChecked(true);
        }

        yesCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    noCheckBox.setChecked(false);
                    rowModel.setValue("Y");
                }
                else {
                    noCheckBox.setChecked(true);
                    rowModel.setValue("N");
                }
            }
        });

        noCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton button, boolean isChecked) {
                if (isChecked) {
                    yesCheckBox.setChecked(false);
                    rowModel.setValue("N");
                }
                else {
                    yesCheckBox.setChecked(true);
                    rowModel.setValue("Y");
                }
            }
        });

        layout.addView(tableRow);
    }

    public void addTextToLayout(TableLayout layout, final PZDetailModel.PZRowModel rowModel) {

        LinearLayout tableRow = (LinearLayout) getLayoutInflater().inflate(R.layout.pz_detail_text_row_layout, null);
        TextView titleView = (TextView) tableRow.findViewById(R.id.title_text_view);
        titleView.setText(rowModel.getSubName());

        EditText editText = (EditText)tableRow.findViewById(R.id.edit_text);
        editText.setText(rowModel.getValue());
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                rowModel.setValue(s.toString());

//                String text = s.toString();
//
//                boolean isValid = true;
//
//                try {
//                    double value = Double.valueOf(text);
//
//                    if (item.getMinValue() != null) {
//                        double minValue = Double.valueOf(item.getMinValue());
//                        if (value < minValue) {
//                            isValid = false;
//                        }
//                    }
//
//                    if (item.getMaxValue() != null) {
//                        double maxValue = Integer.valueOf(item.getMaxValue());
//                        if (value > maxValue) {
//                            isValid = false;
//                        }
//                    }
//
//                } catch (NumberFormatException e) {
//                    Log.e("", e.toString());
//                    isValid = false;
//                } finally {
//                }
//
//                if (isValid) {
//                model.getParams().put(item.getItemKey(), s.toString());
//                } else {
//                    MyToast.showMessage(getBaseContext(), "参数错误");
//                }
            }
        });

        editText.clearFocus();

        layout.addView(tableRow);
    }

    public void addAryInputToLayout(TableLayout layout, final PZDetailModel.PZRowModel rowModel, boolean isDecimal) {
        FlowLayout flowLayout = (FlowLayout) getLayoutInflater().inflate(R.layout.pz_detail_array_input_row_layout, null);

        TextView textView = new TextView(this);
        FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setNewLine(true);
        textView.setLayoutParams(layoutParams);
        int padding = getResources().getDimensionPixelSize(R.dimen.text_view_padding);
        textView.setPadding(padding, padding, padding, padding);
        textView.setTextSize(18);
        String text = rowModel.getSubName() + " (单位：" + rowModel.getDescription() + ")";
        textView.setText(text);

        flowLayout.addView(textView);

        int maxNum = 0;
        String[] values = {};
        try {
            maxNum = Integer.parseInt(rowModel.getArrayMaxNum());
            values = rowModel.getValue().split(Utils.MultipartSeparator);
        }
        catch (Exception ex) {
            Log.e("", ex.toString());
        }

        for (int i = 0; i < maxNum; ++i) {
            EditText editText = (EditText) getLayoutInflater().inflate(R.layout.pz_detail_array_edit_view, null);
            if (isDecimal) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            }
            else {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            }

            if (i < values.length) {
                editText.setText(values[i]);
            }

            final int index = i;

            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    rowModel.setMultiValue(s.toString(), index);
                }
            });

            flowLayout.addView(editText);
        }

        layout.addView(flowLayout);
    }

    public void addIntAryToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        addAryInputToLayout(layout, rowModel, false);
    }

    public void addDoubleAryToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        addAryInputToLayout(layout, rowModel, true);
    }
}
