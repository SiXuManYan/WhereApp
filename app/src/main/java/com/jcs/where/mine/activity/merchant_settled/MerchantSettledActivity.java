package com.jcs.where.mine.activity.merchant_settled;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.blankj.utilcode.util.GsonUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.request.MerchantSettledRequest;
import com.jcs.where.api.response.SuccessResponse;
import com.jcs.where.api.response.UploadFileResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.hotel.activity.CityPickerActivity;
import com.jcs.where.mine.model.merchant_settled.MerchantSettledModel;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.RequestResultCode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * 页面-商家入驻
 * create by zyf on 2021/2/22 7:30 下午
 */
public class MerchantSettledActivity extends BaseActivity {
    private final String ADD_PIC_ACTION = "action";

    private RecyclerView mLicenceRecycler;
    private AddPictureAdapter mAdapter;
    private EditText mFirstNameEt, mMiddleNameEt, mLastNameEt, mPhoneEt, mEmailEt;
    private EditText mMerchantNameEt, mMerchantAddressEt, mMerchantPhoneEt;
    private RadioGroup mRadioGroup;
    private View mToChooseTypeView, mToChoseCityView;
    private TextView mMerchantTypeTv, mMerchantCityTv;
    private TextView mCommitTv;
    private MerchantSettledRequest mRequest;
    private MerchantSettledModel mModel;
    private int mUploadCompleted = 0;
    private int mToUploadCount = 0;
    // 接受上传图片得到的链接
    private String[] mUploadLinks;


    @Override
    protected void initView() {
        defaultSoftInputHidden();
        mLicenceRecycler = findViewById(R.id.licenceRecycler);
        mAdapter = new AddPictureAdapter();
        mLicenceRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mLicenceRecycler.setAdapter(mAdapter);

        mFirstNameEt = findViewById(R.id.firstNameEt);
        mMiddleNameEt = findViewById(R.id.middleNameEt);
        mLastNameEt = findViewById(R.id.lastNameEt);
        mPhoneEt = findViewById(R.id.phoneEt);
        mEmailEt = findViewById(R.id.emailEt);
        mMerchantNameEt = findViewById(R.id.merchantNameEt);
        mMerchantAddressEt = findViewById(R.id.merchantAddressEt);
        mMerchantPhoneEt = findViewById(R.id.merchantPhoneEt);

        mRadioGroup = findViewById(R.id.merchantRadio);

        mToChooseTypeView = findViewById(R.id.toChoseTypeView);
        mToChoseCityView = findViewById(R.id.toChoseCityView);
        mMerchantTypeTv = findViewById(R.id.merchantTypeTv);
        mMerchantCityTv = findViewById(R.id.merchantCityTv);

        mCommitTv = findViewById(R.id.commitTv);
    }

    @Override
    protected void initData() {
        mModel = new MerchantSettledModel();
        mRequest = new MerchantSettledRequest();
        mAdapter.getData().clear();
        mAdapter.addData(ADD_PIC_ACTION);
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onAddPicClicked);
        mAdapter.addChildClickViewIds(R.id.delPicIv);
        mAdapter.setOnItemChildClickListener(this::onDelPicClicked);
        mToChooseTypeView.setOnClickListener(this::onToChooseTypeView);
        mToChoseCityView.setOnClickListener(this::onToChooseCityView);

        mCommitTv.setOnClickListener(this::onCommitClicked);

        mRadioGroup.setOnCheckedChangeListener(this::onRadioChanged);
    }

    private void onRadioChanged(RadioGroup radioGroup, int childId) {
        if (childId == R.id.yes) {
            mRequest.setHasPhysicalStore(1);
        }

        if (childId == R.id.no) {
            mRequest.setHasPhysicalStore(2);
        }
    }

    private void onCommitClicked(View view) {
        try {
            deployRequest();
        } catch (Exception e) {
            showToast(e.getMessage());
        }
    }

    private void deployRequest() throws RuntimeException {
        mRequest.setFirstName(getEtContent(mFirstNameEt, "请输入您的名字"));
        mRequest.setMiddleName(getEtContent(mMiddleNameEt));
        mRequest.setLastName(getEtContent(mLastNameEt, "请输入您的姓氏"));
        mRequest.setContactNumber(getEtContent(mPhoneEt, "请输入联系电话"));
        mRequest.setEmail(getEtContent(mEmailEt, "请输入电子邮件"));
        mRequest.setMerName(getEtContent(mMerchantNameEt, "请输入商家名称"));

        checkAttributeNotZero(mRequest.getMerTypeId(), "请选择商家类别");
        checkAttributeNotZero(mRequest.getAreaId(), "请选择商家所在城市");

        mRequest.setMerAddress(getEtContent(mMerchantAddressEt, "请输入商家地址"));
        mRequest.setMerTel(getEtContent(mMerchantPhoneEt, "请输入商家电话"));

        checkAttributeNotZero(mRequest.getHasPhysicalStore(), "请选择是否有实体店面");

        if (mAdapter.getItemCount() > 1) {
            // 先上传图片
            updatePictures();
        } else {
            throw new RuntimeException("请上传营业执照");
        }
    }

    private void updatePictures() {
        //先上传图片
        List<String> data = mAdapter.getData();
        int size = data.size();
        // 包含 上传图片 item，则待上传数量为 size - 1
        mToUploadCount = data.contains(ADD_PIC_ACTION) ? size - 1 : size;
        // 为了有序的保存上传链接
        mUploadLinks = new String[mToUploadCount];
        showLoading();
        for (int i = 0; i < size; i++) {
            String filePath = data.get(i);
            if (!filePath.equals(ADD_PIC_ACTION)) {
                mModel.postUploadFile(filePath, new BaseObserver<UploadFileResponse>() {
                    @Override
                    protected void onError(ErrorResponse errorResponse) {
                        stopLoading();
                        showNetError(errorResponse);
                    }

                    @Override
                    protected void onSuccess(UploadFileResponse response) {
                        mUploadCompleted++;
                        int index = mAdapter.getData().indexOf(filePath);
                        mUploadLinks[index] = response.link;
                        if (mUploadCompleted == mToUploadCount) {
                            mRequest.setBusinessLicense(GsonUtils.toJson(mUploadLinks));
                            // 都上传完了，提交评论
                            commitMerchant();
                        }
                    }
                });

            }
        }
    }

    private void commitMerchant() {
        Log.e("MerchantSettledActivity", "commitMerchant: " + "----");
        mModel.postMerchant(mRequest, new BaseObserver<SuccessResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                Log.e("MerchantSettledActivity", "onError: " + errorResponse.getErrMsg());
                showNetError(errorResponse);
            }

            @Override
            protected void onSuccess(SuccessResponse response) {
                stopLoading();
                showToast(getString(R.string.commit_success));
            }
        });
    }

    private void checkAttributeNotZero(int attribute, String errorPrompt) {
        if (attribute == 0) {
            throw new RuntimeException(errorPrompt);
        }
    }

    private String getEtContent(EditText et, String errorPrompt) {
        String content = et.getText().toString();
        if ("".equals(content)) {
            throw new RuntimeException(errorPrompt);
        }
        return content;
    }

    private String getEtContent(EditText et) {
        return et.getText().toString();
    }

    private void onToChooseCityView(View view) {
        Intent toChooseType = new Intent(this, CityPickerActivity.class);
        startActivityForResult(toChooseType, RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_CITY_PICKER);
    }

    private void onToChooseTypeView(View view) {
        Intent toChooseType = new Intent(this, SettledTypeActivity.class);
        startActivityForResult(toChooseType, RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_SETTLED_TYPE);
    }

    private void onDelPicClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        mAdapter.removeAt(position);
        if (!mAdapter.getData().contains(ADD_PIC_ACTION)) {
            mAdapter.addData(ADD_PIC_ACTION);
        }
    }

    private void onAddPicClicked(BaseQuickAdapter<?, ?> baseQuickAdapter, View view, int position) {
        String s = mAdapter.getData().get(position);
        if (s.equals(ADD_PIC_ACTION)) {
            Intent intent = new Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_NATIVE_PHOTOS);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        //在相册里面选择好相片之后调回到现在的这个activity中
        switch (requestCode) {
            case RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_NATIVE_PHOTOS:
                if (resultCode == RESULT_OK) {//resultcode是setResult里面设置的code值
                    try {
                        Uri selectedImage = data.getData(); //获取系统返回的照片的Uri
                        String[] filePathColumn = {MediaStore.Images.Media.DATA};
                        Cursor cursor = getContentResolver().query(selectedImage,
                                filePathColumn, null, null, null);//从系统表中查询指定Uri对应的照片
                        cursor.moveToFirst();
                        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                        String path = cursor.getString(columnIndex);  //获取照片路径
                        cursor.close();
                        int itemCount = mAdapter.getItemCount();
                        int index = itemCount - 1;
                        mAdapter.addData(index, path);
                        if (index == 1) {
                            mAdapter.removeAt(2);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
            case RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_SETTLED_TYPE:
                if (resultCode == RequestResultCode.RESULT_SETTLED_TYPE_TO_MERCHANT_SETTLED) {
                    String typeName = data.getStringExtra("typeName");
                    int typeId = data.getIntExtra("typeId", 0);
                    mRequest.setMerTypeId(typeId);
                    mMerchantTypeTv.setText(typeName);
                }
                break;
            case RequestResultCode.REQUEST_MERCHANT_SETTLED_TO_CITY_PICKER:
                if (resultCode == RESULT_OK) {
                    String cityName = data.getStringExtra(CityPickerActivity.EXTRA_CITY);
                    String cityId = data.getStringExtra(CityPickerActivity.EXTRA_CITYID);
                    mRequest.setAreaId(Integer.parseInt(cityId));
                    mMerchantCityTv.setText(cityName);
                }
                break;
        }
    }

    @Override
    protected boolean isStatusDark() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_merchant_settled;
    }

    static class AddPictureAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

        public AddPictureAdapter() {
            super(R.layout.item_merchant_settled_licence_pics);
        }

        @Override
        protected void convert(@NotNull BaseViewHolder baseViewHolder, String s) {
            if (s.equals("action")) {
                baseViewHolder.setImageResource(R.id.picIv, R.mipmap.ic_add_licence);
                baseViewHolder.setGone(R.id.delPicIv, true);
            } else {
                baseViewHolder.setGone(R.id.delPicIv, false);
                ImageView picIv = baseViewHolder.findView(R.id.picIv);
                GlideUtil.load(getContext(), s, picIv);
            }
        }
    }
}
