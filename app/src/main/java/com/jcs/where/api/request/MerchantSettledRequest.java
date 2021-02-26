package com.jcs.where.api.request;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * create by zyf on 2021/2/26 8:44 下午
 */
public class MerchantSettledRequest {

    /**
     * first_name : james
     * middle_name :
     * last_name : lebron
     * contact_number : 12345678910
     * email : 12345678910@163.com
     * mer_name : Test Name
     * mer_type_id : 3
     * area_id : 1
     * mer_address : Test Address
     * mer_tel : 963852741
     * mer_extension_tel :
     * mer_site :
     * mer_facebook :
     * mer_email :
     * has_physical_store : 1
     * business_license : ["https://whereoss.oss-cn-beijing.aliyuncs.com/images/ebd227ed-2dc4-4038-b57a-28afad07770c.jpg"]
     */

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
    @SerializedName("mer_type_id")
    private Integer merTypeId;
    @SerializedName("area_id")
    private Integer areaId;
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
    @SerializedName("mer_email")
    private String merEmail;
    @SerializedName("has_physical_store")
    private Integer hasPhysicalStore;
    @SerializedName("business_license")
    private String businessLicense;

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

    public Integer getMerTypeId() {
        return merTypeId;
    }

    public void setMerTypeId(Integer merTypeId) {
        this.merTypeId = merTypeId;
    }

    public Integer getAreaId() {
        return areaId;
    }

    public void setAreaId(Integer areaId) {
        this.areaId = areaId;
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

    public String getMerEmail() {
        return merEmail;
    }

    public void setMerEmail(String merEmail) {
        this.merEmail = merEmail;
    }

    public Integer getHasPhysicalStore() {
        return hasPhysicalStore;
    }

    public void setHasPhysicalStore(Integer hasPhysicalStore) {
        this.hasPhysicalStore = hasPhysicalStore;
    }

    public String getBusinessLicense() {
        return businessLicense;
    }

    public void setBusinessLicense(String businessLicense) {
        this.businessLicense = businessLicense;
    }
}
