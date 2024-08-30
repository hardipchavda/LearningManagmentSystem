package app.preplotus.activities;

import static app.preplotus.utilities.Constants.REFERRAL_CODE;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.app.ShareCompat;

import app.preplotus.R;
import app.preplotus.databinding.ActivityNotesBinding;
import app.preplotus.databinding.ActivityReferEarnBinding;
import app.preplotus.utilities.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ReferEarnActivity extends AppCompatActivity {

    private Context mContext;
    private String shareText = "";

    private ActivityReferEarnBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReferEarnBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ButterKnife.bind(this);
        init();
    }

    private void init() {

        mContext = ReferEarnActivity.this;
        binding.tvCode.setText(Utils.getPrefData(REFERRAL_CODE, mContext));
        String str = getPackageName();
        shareText = "Hey there i am using Preplotus for easy learning\n\nCode: "+Utils.getPrefData(REFERRAL_CODE, mContext) + "\n\nUse my code to get 300 coins.\n\nDownload App from below link:\nhttps://play.google.com/store/apps/details?id=" +str+"&referrer=" + binding.tvCode.getText().toString();

        binding.iconBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        binding.tvCopyCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCopyCode();
            }
        });

        binding.imgWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onWhatsApp();
            }
        });

        binding.tvFacebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onFacebook();
            }
        });

        binding.tvTelegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTelegram();
            }
        });

        binding.tvOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onOther();
            }
        });

    }




    public void onCopyCode() {
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("label", binding.tvCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        Utils.showToast(mContext, "Code copied!");
    }


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


    public void onOther() {
        ShareCompat.IntentBuilder.from(this)
                .setText(shareText)
                .setType("text/plain")
                .setChooserTitle("Share Code...")
                .startChooser();
    }


}