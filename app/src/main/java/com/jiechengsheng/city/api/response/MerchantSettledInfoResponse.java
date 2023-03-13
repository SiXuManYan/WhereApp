package com.jiechengsheng.city.api.response;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2021/2/26 10:25 下午
 */
public class MerchantSettledInfoResponse {

    /**
     * id : 15
     * first_name : james
     * middle_name :
     * last_name : lebron
     * contact_number : 12345678910
     * email : 12345678910@163.com
     * mer_name : Test Name
     * mer_type : 动物&宠物
     * area : 阿布凯
     * mer_address : Test Address
     * mer_tel : 963852741
     * mer_extension_tel :
     * mer_site :
     * mer_facebook :
     * has_physical_store : 1
     * business_license : [" https://whereoss.oss-cn-beijing.aliyuncs.com/images/ebd227ed-2dc4-4038-b57a-28afad07770c.jpg"]
     * is_verify : 1
     * created_at : 2021-02-26 20:41:21
     * notes :
     */

    @SerializedName("id")
    private Integer id;
    @SerializedName("first_name")
    private String firstName;
    @SerializedName("middle_name")
    private String middleName;
    @SerializedName("last_name")
    private String lastName;
    @SerializedName("contact_number")
    private String contactNumber;
    @SerializedName("email")
    private String email;
    @SerializedName("mer_name")
    private String merName;
    @SerializedName("mer_type")
    private String merType;
    @SerializedName("area")
    private String area;
    @SerializedName("mer_address")
    private String merAddress;
    @SerializedName("mer_tel")
    private String merTel;
    @SerializedName("mer_extension_tel")
    private String merExtensionTel;
    @SerializedName("mer_site")
    private String merSite;
    @SerializedName("mer_facebook")
    private String merFacebook;
    @SerializedName("has_physical_store")
    private Integer hasPhysicalStore;
    @SerializedName("is_verify")
    private Integer isVerify;
    @SerializedName("created_at")
    private String createdAt;
    @SerializedName("notes")
    private String notes;
    @SerializedName("business_license")
    private List<String> businessLicense;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMerName() {
        return merName;
    }

    public void setMerName(String merName) {
        this.merName = merName;
    }

    public String getMerType() {
        return merType;
    }

    public void setMerType(String merType) {
        this.merType = merType;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getMerAddress() {
        return merAddress;
    }

    public void setMerAddress(String merAddress) {
        this.merAddress = merAddress;
    }

    public String getMerTel() {
        return merTel;
    }

    public void setMerTel(String merTel) {
        this.merTel = merTel;
    }

    public String getMerExtensionTel() {
        return merExtensionTel;
    }

    public void setMerExtensionTel(String merExtensionTel) {
        this.merExtensionTel = merExtensionTel;
    }

    public String getMerSite() {
        return merSite;
    }

    public void setMerSite(String merSite) {
        this.merSite = merSite;
    }

    public String getMerFacebook() {
        return merFacebook;
    }

    public void setMerFacebook(String merFacebook) {
        this.merFacebook = merFacebook;
    }

    public Integer getHasPhysicalStore() {
        return hasPhysicalStore;
    }

    public void setHasPhysicalStore(Integer hasPhysicalStore) {
        this.hasPhysicalStore = hasPhysicalStore;
    }

    public Integer getIsVerify() {
        return isVerify;
    }

    public void setIsVerify(Integer isVerify) {
        this.isVerify = isVerify;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public List<String> getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(List<String> businessLicense) {
        this.businessLicense = businessLicense;
    }
}
