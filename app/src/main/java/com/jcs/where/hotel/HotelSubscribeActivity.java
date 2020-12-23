package com.jcs.where.hotel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jcs.where.R;
import com.jcs.where.api.BaseObserver;
import com.jcs.where.api.ErrorResponse;
import com.jcs.where.api.request.HotelOrderRequest;
import com.jcs.where.api.response.HotelOrderResponse;
import com.jcs.where.base.BaseActivity;
import com.jcs.where.bean.SubscribeBean;
import com.jcs.where.codepicker.Country;
import com.jcs.where.codepicker.CountryPicker;
import com.jcs.where.codepicker.OnPick;
import com.jcs.where.home.dialog.AreaCodeListDialog;
import com.jcs.where.model.HotelSubscribeModel;

import androidx.appcompat.widget.Toolbar;
import io.reactivex.annotations.NonNull;

public class HotelSubscribeActivity extends BaseActivity {

    private static final String EXT_BEAN = "bean";
    private TextView hotelNameTv, roomNameTv, startDateTv, startWeekTv, endDateTv, endWeekTv, nightTv;
    private Toolbar toolbar;
    private SubscribeBean subscribeBean;
    private TextView bedTv, breakfastTv, windowTv, wifiTv, peopleTv, cancelTv;
    private TextView phoneTv, roomNumTv, priceTv;
    private TextView mAreaTv;
    private ImageView reduceIv, addIv;
    private EditText nameEt, phoneEt;
    private int night = 1;
    private AreaCodeListDialog mAreaCodeListDialog;
    private HotelSubscribeModel mModel;

    public static void goTo(Context context, SubscribeBean subscribeBean) {
        Intent intent = new Intent(context, HotelSubscribeActivity.class);
        intent.putExtra(EXT_BEAN, subscribeBean);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void initView() {
        toolbar = findViewById(R.id.toolbar);
        setMargins(toolbar, 0, getStatusBarHeight(), 0, 0);
        hotelNameTv = findViewById(R.id.tv_hotelname);
        roomNameTv = findViewById(R.id.tv_roomname);
        startDateTv = findViewById(R.id.tv_startdate);
        startWeekTv = findViewById(R.id.tv_startweek);
        endDateTv = findViewById(R.id.tv_enddate);
        endWeekTv = findViewById(R.id.tv_endweek);
        nightTv = findViewById(R.id.tv_night);
        bedTv = findViewById(R.id.tv_bed);
        breakfastTv = findViewById(R.id.tv_breakfast);
        windowTv = findViewById(R.id.tv_window);
        mAreaTv = findViewById(R.id.areaCodeTv);
        wifiTv = findViewById(R.id.tv_wifi);
        peopleTv = findViewById(R.id.tv_people);
        cancelTv = findViewById(R.id.tv_cancel);
        phoneTv = findViewById(R.id.tv_phone);
        roomNumTv = findViewById(R.id.tv_roomnum);
        reduceIv = findViewById(R.id.iv_roomreduce);
        addIv = findViewById(R.id.iv_roomadd);
        priceTv = findViewById(R.id.tv_price);
        nameEt = findViewById(R.id.et_name);
        phoneEt = findViewById(R.id.et_phone);

        mAreaCodeListDialog = new AreaCodeListDialog();
        mAreaCodeListDialog.injectCallback(this::select);
    }

    @Override
    protected void initData() {
        subscribeBean = getIntent().getParcelableExtra(EXT_BEAN);
        mModel = new HotelSubscribeModel();

        hotelNameTv.setText(subscribeBean.hotelName);
        roomNameTv.setText(subscribeBean.roomName);
        startDateTv.setText(subscribeBean.startDate);

        startWeekTv.setText(String.format(getString(R.string.parentheses_contain_string), subscribeBean.startWeek));
        endDateTv.setText(subscribeBean.endDate);
        endWeekTv.setText(String.format(getString(R.string.parentheses_contain_string), subscribeBean.endWeek));
        nightTv.setText(subscribeBean.night);
        bedTv.setText(subscribeBean.bed);
        breakfastTv.setText(subscribeBean.breakfast);
        if (subscribeBean.window == 1) {
            windowTv.setText(R.string.with_window);
        } else {
            windowTv.setText(R.string.no_window);
        }
        if (subscribeBean.wifi == 1) {
            wifiTv.setText(R.string.with_wifi);
        } else {
            wifiTv.setText(R.string.no_wifi);
        }
        peopleTv.setText(String.format(getString(R.string.stay_people_number), subscribeBean.people));

        //cancel 为 1 表示 可取消 不为 1 表示不可取消
        if (subscribeBean.cancel == 1) {
            cancelTv.setText(R.string.cancelable);
        } else {
            cancelTv.setText(R.string.not_cancelable);
        }
        roomNumTv.setText(subscribeBean.roomNumber);
        night = Integer.parseInt(subscribeBean.night.replace(getString(R.string.price_unit), "").replace(getString(R.string.total), "").replace(getString(R.string.night), ""));
        // 订单总价
        float priceFloat = Integer.parseInt(roomNumTv.getText().toString()) * subscribeBean.roomPrice * night;
        String priceText = String.format(getString(R.string.show_price_with_forward_unit_f), priceFloat);
        priceTv.setText(priceText);
    }

    @Override
    protected void bindListener() {
        reduceIv.setOnClickListener(this::onRoomReduceClicked);
        addIv.setOnClickListener(this::onRoomAddClicked);
        mAreaTv.setOnClickListener(this::onAreaTvClicked);
        findViewById(R.id.tv_submit).setOnClickListener(this::onSubmitTvClicked);
        findViewById(R.id.rl_choosephone).setOnClickListener(this::onChoosePhoneClicked);

        toolbar.setNavigationOnClickListener(this::onBackIconClicked);
    }

    public void onBackIconClicked(View view) {
        finish();
    }

    public void onRoomReduceClicked(View view) {
        int roomNum = Integer.parseInt(roomNumTv.getText().toString());
        if (roomNum == 1) {
            showToast(getString(R.string.can_not_reduce));
        } else {
            roomNum--;
            roomNumTv.setText(String.valueOf(roomNum));
            // 订单总价
            float priceFloat = roomNum * subscribeBean.roomPrice * night;
            String price = String.format(getString(R.string.show_price_with_forward_unit_f), priceFloat);
            priceTv.setText(price);
        }
    }

    public void onRoomAddClicked(View view) {
        int roomNum = Integer.parseInt(roomNumTv.getText().toString());
        roomNum++;
        roomNumTv.setText(String.valueOf(roomNum));
        // 订单总价
        float priceFloat = roomNum * subscribeBean.roomPrice * night;
        String price = String.format(getString(R.string.show_price_with_forward_unit_f), priceFloat);
        priceTv.setText(price);
    }

    public void onAreaTvClicked(View view) {
        mAreaCodeListDialog.show(getSupportFragmentManager());
    }

    public void onSubmitTvClicked(View view) {
        if (TextUtils.isEmpty(nameEt.getText().toString())) {
            showToast(getString(R.string.please_input_name));
            return;
        }
        if (TextUtils.isEmpty(phoneEt.getText().toString().trim())) {
            showToast(getString(R.string.please_input_phone_number));
            return;
        }
        submit();
    }

    public void onChoosePhoneClicked(View view) {
        CountryPicker.newInstance(null, new OnPick() {
            @Override
            public void onPick(Country country) {
                phoneTv.setText(String.valueOf(country.code));
            }
        }).show(getSupportFragmentManager(), "country");
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_hotel_subscribe;
    }

    private void submit() {
        showLoading();
        HotelOrderRequest request = new HotelOrderRequest();
        String month = getString(R.string.month);
        String day = getString(R.string.day);
        String symbolShortLink = getString(R.string.symbol_short_link);
        request.setHotel_room_id(String.valueOf(subscribeBean.roomId));
        request.setPrice(priceTv.getText().toString().replace(getString(R.string.price_unit), ""));
        request.setPhone(phoneEt.getText().toString());
        request.setUsername(nameEt.getText().toString());
        request.setStart_date(subscribeBean.startYear + symbolShortLink + subscribeBean.startDate.replace(month, symbolShortLink).replace(day, ""));
        request.setEnd_date(subscribeBean.endYear + symbolShortLink + subscribeBean.endDate.replace(month, symbolShortLink).replace(day, ""));
        request.setRoom_num(roomNumTv.getText().toString());
        mModel.postHotelOrder(request, new BaseObserver<HotelOrderResponse>() {
            @Override
            protected void onError(ErrorResponse errorResponse) {
                stopLoading();
                showNetError(errorResponse);
            }

            @Override
            public void onNext(@NonNull HotelOrderResponse hotelOrderResponse) {
                stopLoading();
                showToast(getString(R.string.subscribe_success));
                HotelPayActivity.goTo(HotelSubscribeActivity.this, hotelOrderResponse);
            }
        });
    }

    public void select(String area) {
        mAreaTv.setText(area);
    }
}
