#Chowder M-Pesa Checkout Android Libarary

This library, using the M-Pesa C2B APIs will allow you to prompt a user to make a payment from their M-Pesa account to a PayBill number without having to leave your app. 

For successful requests, Safaricom will send a push USSD to the user's mobile device and prompt them to enter their Bonga PIN. Funds will then be transferred from the user's M-Pesa account into your PayBill account after which you can provide the user the goods or services that they have purchased.

Get the sample apk from [here](https://github.com/IanWambai/Chowder/tree/master/sample/chowder_sample.apk), click on 'View the full file' to download it, or find it in the 'sample' folder:

##Screenshots

The screenshots below show how it works.

![](images/hints.png?raw=true)
![](images/details.png?raw=true)
![](images/payment_ready.png?raw=true)
![](images/transaction_in_progress.png?raw=true)
![](images/ussd_push.png?raw=true)
![](images/ussd_accept.png?raw=true)
![](images/transaction_done.png?raw=true)

##Use Cases

You can easily use Chowder in your Android app for the following cases:
* Having a user pay before accessing your app, or certain features in your app.
* Having a list of items, such as products, tickets, meals, books, music, images or other media, and having the user reliably pay to access them.
* In-app purchases in games.
* Having a user pay to access the premium/ad-free version of your app.
* Subscribing a user and having them pay again after a set period of time.
* Any form of payment you need a user to make for you to provide them goods/services via Android.

##Usage

Add this to the `build.gradle` file of your module

    dependencies {
        compile 'com.toe.chowder:chowder:0.6.5'
    }

*The minSdkVersion is 9*

##Parameters

You're going to need some parameters beforehand in order to successfully receive payments:

+ **Pay Bill number**: This is the PayBill number to which the user will be making payments. You get this from Safaricom.
+ **Passkey**: This is a string that you also get from Safarcom after they enable your PayBill account for online checkouts.
+ **Amount**: This is the amount that you would like to charge the user, or the cost of your product, feature or service.
+ **Phone number**: This is the Safaricom phone number of the person who is supposed to make the payment. They will have to confirm the payment using their Bonga PIN.
+ **Product Id**: This is the unique id of the product, feature or service that you are selling.

You must provide all of these parameters or else you will receive an error.

##Sample Code

####Process payment:

This is how you initialize the Chowder object and process a payment.

Get the test `PAYBILL_NUMBER ` and `PASSKEY` from the sample project.
    
        //You can create a single global variable for Chowder like this
        Chowder chowder = new Chowder(YourActivity.this, PAYBILL_NUMBER, PASSKEY, this);

        //You can then use processPayment() to process individual payments
        chowder.processPayment(amount, phoneNumber, productId);

Guess what? You're done! All you have to do is wait for the user to make the payment.

####Confirm payment:

This is how you confirm whether a user has paid or not, so you can then take the necessary action.

    chowder.checkTransactionStatus(PAYBILL_NUMBER, transactionId);
    //Use the transaction id provided by the PaymentListener

####Interface

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

##Debugging

+ You can use the tag "M-PESA REQUEST" to view requests and return codes.
+ If you get errors, look up the response code (which will be toasted and also logged) in the Developers Guide under Reference Faults. Find it [here](https://github.com/IanWambai/Chowder/tree/master/files/m-pesa_developers_guide.doc).

And you are done! Get more code in the sample project.

If you have any feature suggestions or additions that you wish to make, please feel free. Please open issues if you come across anything weird.