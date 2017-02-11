package com.jacoli.roadsitesupervision;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.jacoli.roadsitesupervision.services.ActiveComponetResp;
import com.jacoli.roadsitesupervision.services.MainService;
import com.jacoli.roadsitesupervision.services.OperatorListModel;
import com.jacoli.roadsitesupervision.services.PZDetailModel;
import com.jacoli.roadsitesupervision.services.ProjectDetailModel;
import com.jacoli.roadsitesupervision.services.UnitProjectModel;
import com.jacoli.roadsitesupervision.views.MyToast;

import org.apmem.tools.layouts.FlowLayout;

public class PZDetailActivity extends CommonActivity {

    private PZDetailModel model;
    private OperatorListModel operatorListModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pzdetail);

        createTitleBar();
        titleBar.setLeftText("返回");
        titleBar.setTitle("施工旁站");

        Intent intent = getIntent();
        String id = intent.getStringExtra("id");
        if (MainService.getInstance().sendPZDetailQuery(id, handler)) {
            Toast.makeText(getBaseContext(), "获取旁站详情中", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getBaseContext(), "获取旁站详情失败", Toast.LENGTH_SHORT).show();
        }

        MainService.getInstance().sendOperatorListQuery(handler);
    }

    @Override
    public void onResponse(int msgCode, Object obj) {
        switch (msgCode) {
            case MainService.MSG_QUERY_OPERATOR_LIST_SUCCESS:
                operatorListModel = (OperatorListModel) obj;
                break;
            case MainService.MSG_QUERY_OPERATOR_LIST_FAILED:
                Toast.makeText(getBaseContext(), "获取操作人员列表失败", Toast.LENGTH_SHORT).show();
                break;
            case MainService.MSG_QUERY_PZ_DETAIL_SUCCESS:
                MyToast.showMessage(getBaseContext(), "获取旁站详情成功");
                model = (PZDetailModel) obj;
                initSubviewsAfterFetchSuccess();
                break;
            case MainService.MSG_QUERY_PZ_DETAIL_FAILED:
                Toast.makeText(getBaseContext(), "获取旁站详情失败", Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
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

    public void addIntEditableToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        TableRow tableRow = (TableRow) getLayoutInflater().inflate(R.layout.pz_detail_int_input_row_layout, null);
        TextView titleView = (TextView) tableRow.findViewById(R.id.title_text_view);
        titleView.setText(rowModel.getSubName());
        TextView unitView = (TextView) tableRow.findViewById(R.id.unit_text_view);
        unitView.setText(rowModel.getDescription());

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

    public void addDoubleEditableToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        addIntEditableToLayout(layout, rowModel);
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

    public void addTextToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {

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

    public void addIntAryToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        // TODO
    }

    public void addDoubleAryToLayout(TableLayout layout, PZDetailModel.PZRowModel rowModel) {
        // TODO
    }
}
