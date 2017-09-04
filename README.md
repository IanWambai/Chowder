# Chowder M-Pesa Checkout Android Library

This library, using the M-Pesa C2B APIs will allow you to prompt a user to make a payment from their M-Pesa account to a PayBill number without having to leave your app. 

For successful requests, Safaricom will send a push USSD to the user's mobile device and prompt them to enter their Bonga PIN. Funds will then be transferred from the user's M-Pesa account into your PayBill account after which you can provide the user the goods or services that they have purchased.

Get the sample apk from [here](https://github.com/IanWambai/Chowder/blob/master/sample/chowder_sample.apk?raw=true).

Find progress on updating Chowder to use new M-Pesa REST APIs [here](https://github.com/IanWambai/Chowder/issues/12).

## Screenshots

The screenshots below show how it works.

![](images/hints.png?raw=true)
![](images/details.png?raw=true)
![](images/payment_ready.png?raw=true)
![](images/transaction_in_progress.png?raw=true)
![](images/ussd_push.png?raw=true)
![](images/ussd_accept.png?raw=true)
![](images/transaction_done.png?raw=true)

## Use Cases

You can easily use Chowder in your Android app for the following cases:
* Having a user pay before accessing your app, or certain features in your app.
* Having a list of items, such as products, tickets, meals, books, music, images or other media, and having the user reliably pay to access them.
* In-app purchases in games.
* Having a user pay to access the premium/ad-free version of your app.
* Subscribing a user and having them pay again after a set period of time.
* Any form of payment you need a user to make for you to provide them goods/services via Android.

## Usage

Add this to the `build.gradle` file of your module:

    dependencies {
        compile 'com.toe.chowder:chowder:0.8.1'
    }

If you are using a version lower than `0.8.1` make sure you switch to this version because it has some [important security updates.](https://github.com/IanWambai/Chowder/issues/14)

*The minSdkVersion is 9*

## Parameters

You're going to need some parameters beforehand in order to successfully receive payments:

+ **Pay Bill number**: This is the PayBill number to which the user will be making payments. You get this from Safaricom.
+ **Passkey**: This is a string that you also get from Safarcom after they enable your PayBill account for online checkouts.
+ **Amount**: This is the amount that you would like to charge the user, or the cost of your product, feature or service.
+ **Phone number**: This is the Safaricom phone number of the person who is supposed to make the payment. They will have to confirm the payment using their Bonga PIN.
+ **Product Id**: This is the unique id of the product, feature or service that you are selling.

You must provide all of these parameters or else you will receive an error.

## Sample Code

#### Process payment:

This is how you initialize the Chowder object and process a payment.

Get the test `PAYBILL_NUMBER ` and `PASSKEY` from the sample project.
    
        //You can create a single global variable for Chowder like this
        Chowder chowder = new Chowder(YourActivity.this, PAYBILL_NUMBER, PASSKEY, this);

        //You can also add your callback URL using this constructor
        Chowder chowder = new Chowder(YourActivity.this, PAYBILL_NUMBER, callbackUrl ,PASSKEY, this);

        //You can then use processPayment() to process individual payments
        chowder.processPayment(amount, phoneNumber, productId);

Guess what? You're done! All you have to do is wait for the user to make the payment.

#### Confirm payment:

This is how you confirm whether a user has paid or not, so you can then take the necessary action.

    chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionId);
    //Use the transaction id provided by the PaymentListener

#### Interface

You use `PaymentListener` to know the results of the payment processes. There are three methods:

    @Override
    public void onPaymentReady(String returnCode, String processDescription, String merchantTransactionId, String transactionId) {
        //The user is now waiting to enter their PIN on the Safaricom push USSD
        //Show the user something cause it might be awkward just sitting there
        //You can use the transaction id provided to confirm payment to make sure you store the ids somewhere if you want the user to be able to check later
    }

    @Override
    public void onPaymentSuccess(String merchantId, String phoneNumber, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        //The payment was successful, and real money has moved from the user to the PayBill account
    }

    @Override
    public void onPaymentFailure(String merchantId, String phoneNumber, String amount, String transactionStatus, String processDescription) {
        //The payment failed. The user most probably cancelled the transaction. They can always try again.
    }

#### Subscriptions

You can subscribe the user for a product or service using Chowder. This means that after a certain period of time the subscription will become invalid and therfore you may charge the user again. Here's how that works:
    
    @Override
    public void onPaymentSuccess(String merchantId, String msisdn, String amount, String mpesaTransactionDate, String mpesaTransactionId, String transactionStatus, String returnCode, String processDescription, String merchantTransactionId, String encParams, String transactionId) {
        //When the payment is complete you have the option to subscribe the user
        //This means that after a set period of time you can prompt the user to make another payment if the subscription is invalid
        String productId = "1717171717171";
        chowder.subscribeForProduct(productId, Chowder.SUBSCRIBE_DAILY);

        //After a day, if you check the product's subscription it will be invalid, but before it will be valid
    }

After the user subscribes for a product you can periodically check whether the subscription is valid like this:

    //This is how you check whether a single product's subscription is valid
    String productId = "1717171717171";
    boolean isSubscribed = chowder.checkSubscription(productId);

    //This is how you retrieve all the product's subscriptions
    ArrayList<Subscription> validatedSubscriptions = chowder.checkAllSubscriptions();

    //This how you check a subscribed product's data from the list
    if (validatedSubscriptions.size() > 0) {
        Subscription subcribedProduct = validatedSubscriptions.get(0);
        String subscribedProductId = subcribedProduct.getProductId();
        boolean isSubscriptionValid = subcribedProduct.isSubscriptionValid();

        //You get the product Id and whether or not it's subscription is valid
    }

Here are the subscription periods:

    Chowder.SUBSCRIBE_DAILY;
    Chowder.SUBSCRIBE_WEEKLY;
    Chowder.SUBSCRIBE_MONTHLY;
    Chowder.SUBSCRIBE_YEARLY;

You can check whether the subscription is valid every time the user tries to access the product or service.

##### Disclaimer

The subscriptions are stored locally, therefore if a user clears the app's data or uninstalls it, the subscriptions will be lost. This will mean a user potentially won't get access to a product or service they paid for.

## Debugging

+ You can use the Log tag "M-PESA REQUEST" to view any errors or exceptions.
+ If you get errors, look up the response code (which will be toasted and also logged) in the Developers Guide under Reference Faults. Find it [here](https://github.com/IanWambai/Chowder/tree/master/files/m-pesa_developers_guide.doc).

## Other Platforms

#### PHP
If you would like to use the M-Pesa API for a PHP project, find a PHP implementation [here](https://github.com/icrackthecode/MPESA-API).


## Conclusion

And you are done! Get more code in the sample project.

If you have any feature suggestions or additions that you wish to make, please feel free. Please open issues if you come across anything weird.
