package app.preplotus.activities;

import static app.preplotus.utilities.Constants.REFERRAL_CODE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ShareCompat;

import app.preplotus.R;
import app.preplotus.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReferEarnActivity extends AppCompatActivity {

    @BindView(R.id.tvCode)
    AppCompatTextView tvCode;
    private Context mContext;
    private String shareText = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_earn);
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = ReferEarnActivity.this;
        tvCode.setText(Utils.getPrefData(REFERRAL_CODE, mContext));
        String str = getPackageName();
        shareText = "Hey there i am using Preplotus for easy learning\n\nCode: "+Utils.getPrefData(REFERRAL_CODE, mContext) + "\n\nUse my code to get 300 coins.\n\nDownload App from below link:\nhttps://play.google.com/store/apps/details?id=" +str+"&referrer=" + tvCode.getText().toString();
    }


    @OnClick(R.id.iconBack)
    public void onBack() {
        onBackPressed();
    }

    @OnClick(R.id.tvCopyCode)
    public void onCopyCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", tvCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Utils.showToast(mContext, "Code copied!");
    }

    @OnClick(R.id.imgWhatsApp)
    public void onWhatsApp() {
        Intent i = ShareCompat.IntentBuilder.from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share Code...")
                .getIntent().setPackage("com.whatsapp");

        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mContext, "Whatsapp have not been installed.");
        }
    }

    @OnClick(R.id.tvFacebook)
    public void onFacebook() {
        Intent i = ShareCompat.IntentBuilder.from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share Code...")
                .getIntent().setPackage("com.facebook.katana");

        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mContext, "Facebook have not been installed.");
        }
    }

    @OnClick(R.id.tvTelegram)
    public void onTelegram() {
        Intent i = ShareCompat.IntentBuilder.from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share Code...")
                .getIntent().setPackage("org.telegram.messenger");

        try {
            startActivity(i);
        } catch (android.content.ActivityNotFoundException ex) {
            Utils.showToast(mContext, "Telegram have not been installed.");
        }
    }

    @OnClick(R.id.tvOther)
    public void onOther() {
        ShareCompat.IntentBuilder.from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share Code...")
                .startChooser();
    }


}