#Chowder M-Pesa Checkout Android Libarary

This libarary, using the M-Pesa C2B APIs will allow you to prompt a user to make a payment from their M-Pesa accounts to a PayBill number without having to leave your app. 

For successful requests, Safaricom will send push USSD to the user's mobile device and prompt them to enter their Bonga PIN.

The screenshots below show how it works.

![](images/hints.png?raw=true)
![](images/details.png?raw=true)
![](images/payment_ready.png?raw=true)
![](images/transaction_in_progress.png?raw=true)
![](images/ussd_push.png?raw=true)
![](images/ussd_accept.png?raw=true)
![](images/transaction_done.png?raw=true)

#Usage

Add this to the build.gradle file of your module

```repositories {
    maven {
        url 'https://dl.bintray.com/ianwambai/maven/'
    }
}

dependencies {
    compile 'com.toe.chowder:chowder:0.5.0'
}
```

#Sample Code

        Chowder chowder = new Chowder(MainActivity.this, MERCHANT_ID, PASSKEY, amount, phoneNumber.replaceAll("\\+", ""), productId);
        chowder.processPayment();
        chowder.paymentCompleteDialog = new AlertDialog.Builder(MainActivity.this)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        //Check user's SMS inbox for confirmation text
                        //You can also use a callback URL to confirm the transaction, but I'll add that soon
                    }
                });

And you are done!