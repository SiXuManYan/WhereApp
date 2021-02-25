package com.jcs.where.mine.activity.merchant_settled;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.jcs.where.R;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.utils.CacheUtil;
import com.jcs.where.utils.GlideUtil;
import com.jcs.where.utils.RequestResultCode;

import org.jetbrains.annotations.NotNull;

import androidx.recyclerview.widget.GridLayoutManager;
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
    private View mToChooseTypeView;
    private TextView mMerchantTypeTv;

    @Override
    protected void initView() {
        defaultSoftInputHidden();
        mLicenceRecycler = findViewById(R.id.licenceRecycler);
        mAdapter = new AddPictureAdapter();
        mLicenceRecycler.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        mLicenceRecycler.setAdapter(mAdapter);

        mToChooseTypeView = findViewById(R.id.toChoseTypeView);
        mMerchantTypeTv = findViewById(R.id.merchantTypeTv);
    }

    @Override
    protected void initData() {
        mAdapter.getData().clear();
        mAdapter.addData(ADD_PIC_ACTION);
    }

    @Override
    protected void bindListener() {
        mAdapter.setOnItemClickListener(this::onAddPicClicked);
        mAdapter.addChildClickViewIds(R.id.delPicIv);
        mAdapter.setOnItemChildClickListener(this::onDelPicClicked);
        mToChooseTypeView.setOnClickListener(this::onToChooseTypeView);
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
                    String typeId = data.getStringExtra("typeId");
                    mMerchantTypeTv.setText(typeName);
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
