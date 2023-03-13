package com.jiechengsheng.city.features.message.custom;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by Wangsw  2022/2/21 16:53.
 * <p>
 * estore商城 商品消息（融云自定义消息）
 * https://doc.rongcloud.cn/im/Android/5.X/noui/message/custom
 */
@MessageTag(value = "eStoreGoodsMessage", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class CustomMessage extends MessageContent {



    public int goodsID = 0;

    /**
     * 商品图片
     */
    public String goodsImage = "";

    /**
     * 商品名称
     */
    public String goodsName = "";

    /**
     * 商品价格
     */
    public String goodsPrice = "";


    public CustomMessage(int goodsID, String goodsImage, String goodsName, String goodsPrice) {
        this.goodsID = goodsID;
        this.goodsImage = goodsImage;
        this.goodsName = goodsName;
        this.goodsPrice = goodsPrice;
    }

    /**
     * 解析消息内容.
     */
    public CustomMessage(byte[] data) {
        if (data == null) {
            return;
        }

        String jsonStr = null;
        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        if (jsonStr == null) {
            return;
        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            // 消息携带用户信息时, 自定义消息需添加下面代码
            if (jsonObj.has("user")) {
                setUserInfo(parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

            // 用于群组聊天, 消息携带 @ 人信息时, 自定义消息需添加下面代码
            if (jsonObj.has("mentionedInfo")) {
                setMentionedInfo(parseJsonToMentionInfo(jsonObj.getJSONObject("mentionedInfo")));
            }

            // 自定义消息, 定义的字段
            if (jsonObj.has("goodsID")) {
                goodsID = jsonObj.getInt("goodsID");
            }
            if (jsonObj.has("goodsImage")) {
                goodsImage = jsonObj.getString("goodsImage");
            }
            if (jsonObj.has("goodsName")) {
                goodsName = jsonObj.getString("goodsName");
            }
            if (jsonObj.has("goodsPrice")) {
                goodsPrice = jsonObj.getString("goodsPrice");
            }


        } catch (JSONException e) {

        }
        return;

    }

    /**
     * 将本地消息对象序列化为消息数据。
     *
     * @return 消息数据。
     */
    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();
        try {

            // 消息携带用户信息时, 自定义消息需添加下面代码
            if (getJSONUserInfo() != null) {
                jsonObj.putOpt("user", getJSONUserInfo());
            }

            // 用于群组聊天, 消息携带 @ 人信息时, 自定义消息需添加下面代码
            if (getJsonMentionInfo() != null) {
                jsonObj.putOpt("mentionedInfo", getJsonMentionInfo());
            }


            // 自定义消息, 定义的字段.
            jsonObj.put("goodsID", goodsID);
            jsonObj.put("goodsImage", goodsImage);
            jsonObj.put("goodsName", goodsName);
            jsonObj.put("goodsPrice", goodsPrice);


        } catch (JSONException e) {

        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {

        }
        return null;
    }


    public CustomMessage(Parcel parcel) {
        // 读取消息属性
        setExtra(ParcelUtils.readFromParcel(parcel));
        setUserInfo(ParcelUtils.readFromParcel(parcel, UserInfo.class));
        goodsID = parcel.readInt();
        goodsImage = parcel.readString();
        goodsName = parcel.readString();
        goodsPrice = parcel.readString();
    }


    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        // 对消息属性进行序列化，将类的数据写入外部提供的 Parcel 中
        ParcelUtils.writeToParcel(parcel, getExtra());
        ParcelUtils.writeToParcel(parcel, getUserInfo());
        parcel.writeInt(goodsID);
        parcel.writeString(goodsImage);
        parcel.writeString(goodsName);
        parcel.writeString(goodsPrice);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CustomMessage> CREATOR = new Creator<CustomMessage>() {

        @Override
        public CustomMessage createFromParcel(Parcel source) {
            return new CustomMessage(source);
        }

        @Override
        public CustomMessage[] newArray(int size) {
            return new CustomMessage[0];
        }
    };


}

