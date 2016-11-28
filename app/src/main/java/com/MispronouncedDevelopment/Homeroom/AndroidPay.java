package com.MispronouncedDevelopment.Homeroom;

        import android.content.Intent;
        import android.os.Bundle;
        import android.support.v7.app.AppCompatActivity;
        import android.view.View;
        import android.widget.Toast;


        import com.google.android.gms.common.ConnectionResult;
        import com.google.android.gms.common.api.GoogleApiClient;
        import com.google.android.gms.wallet.Cart;
        import com.google.android.gms.wallet.FullWallet;
        import com.google.android.gms.wallet.FullWalletRequest;
        import com.google.android.gms.wallet.LineItem;
        import com.google.android.gms.wallet.MaskedWallet;
        import com.google.android.gms.wallet.MaskedWalletRequest;
        import com.google.android.gms.wallet.PaymentMethodTokenizationParameters;
        import com.google.android.gms.wallet.PaymentMethodTokenizationType;
        import com.google.android.gms.wallet.Wallet;
        import com.google.android.gms.wallet.WalletConstants;
        import com.google.android.gms.wallet.fragment.SupportWalletFragment;
        import com.google.android.gms.wallet.fragment.WalletFragmentInitParams;
        import com.google.android.gms.wallet.fragment.WalletFragmentMode;
        import com.google.android.gms.wallet.fragment.WalletFragmentOptions;
        import com.google.android.gms.wallet.fragment.WalletFragmentStyle;


/**
 * Created by Rick on 11/8/2016.
 */


public class AndroidPay extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private SupportWalletFragment mWalletFragment;
    private MaskedWallet mMaskedWallet;
    public static final int MASKED_WALLET_REQUEST_CODE = 888;
    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";
    private GoogleApiClient mGoogleApiClient;
    public static final int FULL_WALLET_REQUEST_CODE = 889;
    private FullWallet mFullWallet;

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // GoogleApiClient failed to connect, we should log the error and retry
    }

    public void requestFullWallet(View view) {
        if (mMaskedWallet == null) {
            Toast.makeText(this, "No masked wallet, can't confirm", Toast.LENGTH_SHORT).show();
            return;
        }
        Wallet.Payments.loadFullWallet(mGoogleApiClient,
                generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()),
                FULL_WALLET_REQUEST_CODE);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parent_payment_history);

        // Check if WalletFragment exists
        mWalletFragment = (SupportWalletFragment) getSupportFragmentManager()
                .findFragmentByTag(WALLET_FRAGMENT_ID);


        if (mWalletFragment == null) {
            // Wallet fragment style
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(WalletFragmentStyle.BuyButtonText.BUY_WITH)
                    .setBuyButtonWidth(WalletFragmentStyle.Dimension.MATCH_PARENT);

            // Wallet fragment options
            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_LIGHT)
                    .setMode(WalletFragmentMode.BUY_BUTTON)
                    .build();

            // Initialize the WalletFragment
            WalletFragmentInitParams.Builder startParamsBuilder =
                    WalletFragmentInitParams.newBuilder()
                            .setMaskedWalletRequest(generateMaskedWalletRequest())
                            .setMaskedWalletRequestCode(MASKED_WALLET_REQUEST_CODE)
                            .setAccountName("Google I/O Codelab");
            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);
            mWalletFragment.initialize(startParamsBuilder.build());


            // Add the WalletFragment to the UI
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID)
                    .commit();
        }
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addOnConnectionFailedListener(this)
                .enableAutoManage(this, 0, this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_TEST)
                        .setTheme(WalletConstants.THEME_LIGHT)
                        .build())
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case MASKED_WALLET_REQUEST_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        mMaskedWallet =  data
                                .getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                        Toast.makeText(this, "Got Masked Wallet", Toast.LENGTH_SHORT).show();
                        break;
                    case RESULT_CANCELED:
                        // The user canceled the operation
                        break;
                    case WalletConstants.RESULT_ERROR:
                        Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                        break;
                    case FULL_WALLET_REQUEST_CODE:
                        switch (resultCode) {
                            case RESULT_OK:
                                mFullWallet = data
                                        .getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                                // Show the credit card number
                                Toast.makeText(this,
                                        "Got Full Wallet, Done!",
                                        Toast.LENGTH_SHORT).show();
                                break;
                            case WalletConstants.RESULT_ERROR:
                                Toast.makeText(this, "An Error Occurred", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        break;
                }
                break;
        }
    }


    private MaskedWalletRequest generateMaskedWalletRequest() {
        // This is just an example publicKey for the purpose of this codelab.
        // To learn how to generate your own visit:
        // https://github.com/android-pay/androidpay-quickstart
        String publicKey = "BO39Rh43UGXMQy5PAWWe7UGWd2a9YRjNLPEEVe+zWIbdIgALcDcnYCuHbmrrzl7h8FZjl6RCzoi5/cDrqXNRVSo=";
        PaymentMethodTokenizationParameters parameters =
                PaymentMethodTokenizationParameters.newBuilder()
                        .setPaymentMethodTokenizationType(
                                PaymentMethodTokenizationType.NETWORK_TOKEN)
                        .addParameter("publicKey", publicKey)
                        .build();


        MaskedWalletRequest maskedWalletRequest =
                MaskedWalletRequest.newBuilder()
                        .setMerchantName("Google I/O Codelab")
                        .setPhoneNumberRequired(true)
                        .setShippingAddressRequired(true)
                        .setCurrencyCode("USD")
                        .setCart(Cart.newBuilder()
                                .setCurrencyCode("USD")
                                .setTotalPrice("10.00")
                                .addLineItem(LineItem.newBuilder()
                                        .setCurrencyCode("USD")
                                        .setDescription("Google I/O Sticker")
                                        .setQuantity("1")
                                        .setUnitPrice("10.00")
                                        .setTotalPrice("10.00")
                                        .build())
                                .build())
                        .setEstimatedTotalPrice("15.00")
                        .setPaymentMethodTokenizationParameters(parameters)
                        .build();
        return maskedWalletRequest;

    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionId) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("USD")
                        .setTotalPrice("10.10")
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Google I/O Sticker")
                                .setQuantity("1")
                                .setUnitPrice("10.00")
                                .setTotalPrice("10.00")
                                .build())
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("USD")
                                .setDescription("Tax")
                                .setRole(LineItem.Role.TAX)
                                .setTotalPrice(".10")
                                .build())
                        .build())
                .build();
        return fullWalletRequest;
    }
}


