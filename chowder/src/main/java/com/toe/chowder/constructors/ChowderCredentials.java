package com.toe.chowder.constructors;

public class ChowderCredentials {

    private String mpesaKey;
    private String mpesaSecret;
    private String initiatorPassword;
    private String shortCode1;
    private String initiatorName;
    private String securityCredential;
    private String shortCode2;
    private String msisdn;
    private String expiryDate;
    private String lipaNaMpesaShortCode;
    private String lipaNaMpesaPasskey;
    private String generatedInitiatorSecurityCredential;

//    For M-Pesa APIs that have ‘PartyA’ in their request parameters, use Shortcode 1; Shortcode 2 will be used for ‘PartyB’.

    public ChowderCredentials(String mpesaKey, String mpesaSecret, String initiatorPassword, String shortCode1, String initiatorName, String securityCredential, String shortCode2, String msisdn, String expiryDate, String lipaNaMpesaShortCode, String lipaNaMpesaPasskey, String generatedInitiatorSecurityCredential) {
        this.mpesaKey = mpesaKey;
        this.mpesaSecret = mpesaSecret;
        this.initiatorPassword = initiatorPassword;
        this.shortCode1 = shortCode1;
        this.initiatorName = initiatorName;
        this.securityCredential = securityCredential;
        this.shortCode2 = shortCode2;
        this.msisdn = msisdn;
        this.expiryDate = expiryDate;
        this.lipaNaMpesaShortCode = lipaNaMpesaShortCode;
        this.lipaNaMpesaPasskey = lipaNaMpesaPasskey;
        this.generatedInitiatorSecurityCredential = generatedInitiatorSecurityCredential;
    }

    public String getMpesaKey() {
        return mpesaKey;
    }

    public void setMpesaKey(String mpesaKey) {
        this.mpesaKey = mpesaKey;
    }

    public String getMpesaSecret() {
        return mpesaSecret;
    }

    public void setMpesaSecret(String mpesaSecret) {
        this.mpesaSecret = mpesaSecret;
    }

    public String getInitiatorPassword() {
        return initiatorPassword;
    }

    public void setInitiatorPassword(String initiatorPassword) {
        this.initiatorPassword = initiatorPassword;
    }

    public String getShortCode1() {
        return shortCode1;
    }

    public void setShortCode1(String shortCode1) {
        this.shortCode1 = shortCode1;
    }

    public String getInitiatorName() {
        return initiatorName;
    }

    public void setInitiatorName(String initiatorName) {
        this.initiatorName = initiatorName;
    }

    public String getSecurityCredential() {
        return securityCredential;
    }

    public void setSecurityCredential(String securityCredential) {
        this.securityCredential = securityCredential;
    }

    public String getShortCode2() {
        return shortCode2;
    }

    public void setShortCode2(String shortCode2) {
        this.shortCode2 = shortCode2;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getLipaNaMpesaShortCode() {
        return lipaNaMpesaShortCode;
    }

    public void setLipaNaMpesaShortCode(String lipaNaMpesaShortCode) {
        this.lipaNaMpesaShortCode = lipaNaMpesaShortCode;
    }

    public String getLipaNaMpesaPasskey() {
        return lipaNaMpesaPasskey;
    }

    public void setLipaNaMpesaPasskey(String lipaNaMpesaPasskey) {
        this.lipaNaMpesaPasskey = lipaNaMpesaPasskey;
    }

    public String getGeneratedInitiatorSecurityCredential() {
        return generatedInitiatorSecurityCredential;
    }

    public void setGeneratedInitiatorSecurityCredential(String generatedInitiatorSecurityCredential) {
        this.generatedInitiatorSecurityCredential = generatedInitiatorSecurityCredential;
    }
}