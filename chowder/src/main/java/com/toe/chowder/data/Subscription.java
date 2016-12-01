package com.toe.chowder.data;

/**
 * Created by ian on 01/12/2016.
 */

public class Subscription {

    private String productId;
    private long subscriptionPeriod;
    private boolean isSubscriptionValid;

    public Subscription(String productId, long subscriptionPeriod, boolean isSubscriptionValid) {
        this.productId = productId;
        this.subscriptionPeriod = subscriptionPeriod;
        this.isSubscriptionValid = isSubscriptionValid;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public long getSubscriptionPeriod() {
        return subscriptionPeriod;
    }

    public void setSubscriptionPeriod(long subscriptionPeriod) {
        this.subscriptionPeriod = subscriptionPeriod;
    }

    public boolean isSubscriptionValid() {
        return isSubscriptionValid;
    }

    public void setSubscriptionValid(boolean subscriptionValid) {
        isSubscriptionValid = subscriptionValid;
    }
}
