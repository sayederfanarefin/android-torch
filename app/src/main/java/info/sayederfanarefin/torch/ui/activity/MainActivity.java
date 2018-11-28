/*
 *     Copyright (C) 2016  Merbin J Anselm <merbinjanselm@gmail.com>
 *
 *     This program is free software; you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation; either version 2 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License along
 *     with this program; if not, write to the Free Software Foundation, Inc.,
 *     51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package info.sayederfanarefin.torch.ui.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import info.sayederfanarefin.torch.R;
import info.sayederfanarefin.torch.main.TorchieManagerListener;
import info.sayederfanarefin.torch.main.manager.TorchManager;
import info.sayederfanarefin.torch.main.manager.device.output.OutputDeviceListener;
import info.sayederfanarefin.torch.service.TorchieQuick;
import info.sayederfanarefin.torch.ui.fragment.dialog.AboutDialog;
import info.sayederfanarefin.torch.ui.fragment.dialog.PermissionDialog;
import info.sayederfanarefin.torch.ui.fragment.dialog.WelcomeDialog;
import info.sayederfanarefin.torch.ui.helper.DonateDialogListener;
import info.sayederfanarefin.torch.utils.Constants;
import info.sayederfanarefin.torch.utils.SettingsUtils;

public class MainActivity extends AppCompatActivity implements TorchieManagerListener, DonateDialogListener {
    ImageView but_flash;
  //  SwitchCompat sw_func_toggle;
//
//    TransitionDrawable transAnimButFlash;


    boolean flashButtonStatus = false;
    int flashButAnimTime = 200;

    ImageView smallButtonService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");



        but_flash = (ImageView) findViewById(R.id.but_flash_pto);
        smallButtonService = (ImageView) findViewById(R.id.enable_background_service);
      //  sw_func_toggle = (SwitchCompat) findViewById(R.id.sw_func_toggle);

//        transAnimButFlash = (TransitionDrawable) but_flash.getBackground();
//        transAnimButFlash.resetTransition();

        if (SettingsUtils.isFirstTime(this)) {
           // this.showDialogWelcome();
        }

        MobileAds.initialize(this, getString(R.string.APPLICATION_ID));
        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest request = new AdRequest.Builder()
                .addTestDevice("B7ED290654B835116908C4A987760A3E")
                .build();
        mAdView.loadAd(request);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //transAnimButFlash.resetTransition();
        if (isTorchieQuickServiceRunning()) {
            TorchieQuick.getInstance().registerTorchieManagerListener(this);
            if (this.isTorchOn()) {
                this.setFlashButtonStatus(this.isTorchOn());
            }
        }
        //sw_func_toggle.setChecked(isTorchieQuickServiceRunning());
        if(isTorchieQuickServiceRunning()){
            smallButtonService.setImageResource(R.mipmap.btn_on);
        }else{
            smallButtonService.setImageResource(R.mipmap.btn_off);
        }
    }

    @Override
    protected void onPause() {
        if (this.isTorchieQuickServiceRunning()) {
            TorchieQuick.getInstance().unregisterTorchieManagerListener();
        } else if (this.isTorchOn()) {
            this.toggleTorch(null);
        }
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_donate:
//                showDialogDonate(null);
//                break;
            case R.id.menu_tell_friend:
                Intent int_tell = new Intent(Intent.ACTION_SEND);
                int_tell.setType("text/plain");
                int_tell.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_info) + Constants.PLAY_URI);
                startActivity(Intent.createChooser(int_tell, getResources().getString(R.string.share_via)));
                break;
            case R.id.menu_rate:
                Intent int_rate = new Intent(Intent.ACTION_VIEW);
                int_rate.setData(Uri.parse(Constants.PLAY_URI));
                startActivity(int_rate);
                break;
//            case R.id.menu_about:
//                showDialogAbout();
//                break;
            case R.id.menu_settings:
                Intent int_settings = new Intent(this, SettingsActivity.class);
                startActivity(int_settings);
                break;
            case R.id.menu_help_feedback:

                final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

                /* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"boson42apps@gmail.com"});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "From Torch");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "I have been using your app, Torch");

                /* Send it off to the Activity-Chooser */
                startActivity(Intent.createChooser(emailIntent, "Send mail..."));

//                Intent int_help = new Intent(Intent.ACTION_VIEW);
//                int_help.setData(Uri.parse(Constants.WEB_URI + "/contact"));
//                startActivity(int_help);
                break;
        }
        return true;
    }

    @Override
    public void onError(String error) {
        Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTorchStatusChanged(final boolean status) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setFlashButtonStatus(status);
            }
        });
    }

    @Override
    public void onDonateDialogResult(boolean isSuccess) {
        if (isSuccess) {

        } else {

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    public void openAccessibilitySettings(View v) {

        if(isTorchieQuickServiceRunning()){
            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }else{
            this.showDialogPermission();
        }


//        if (sw_func_toggle.isChecked()) {
//            Intent intent = new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
//            startActivity(intent);
//        } else {
//            this.showDialogPermission();
//        }
    }

    private void showDialogWelcome() {
        WelcomeDialog welcomeDialog = new WelcomeDialog();
        welcomeDialog.show(getFragmentManager(), "Welcome Dialog");
    }

    private void showDialogPermission() {
        PermissionDialog permissionDialog = new PermissionDialog();
        permissionDialog.show(getFragmentManager(), "Permission Dialog");
    }




    private void showDialogAbout() {
        AboutDialog aboutDialog = new AboutDialog();
        aboutDialog.show(getFragmentManager(), "About Dialog");
    }

    private void setFlashButtonStatus(boolean enabled) {
        flashButtonStatus = enabled;
        if (flashButtonStatus) {
            //transAnimButFlash.startTransition(flashButAnimTime);
            //Log.v("----------xxx--------", "if");
            but_flash.setImageResource(R.mipmap.btn_on);

        } else {

            but_flash.setImageResource(R.mipmap.btn_off);

            //Log.v("----------xxx--------", "else"); //transAnimButFlash.reverseTransition(flashButAnimTime);
        }
    }

    private boolean isTorchieQuickServiceRunning() {
        return TorchieQuick.getInstance() != null;
    }

    public void toggleTorch(View v) {
        if (isTorchieQuickServiceRunning()) {
            TorchieQuick.getInstance().toggleTorch();
        } else {
            TorchManager.getInstance(SettingsUtils.getTorchSource(this), true).setListener(new OutputDeviceListener() {
                @Override
                public void onStatusChanged(String deviceType, final boolean status) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setFlashButtonStatus(status);
                        }
                    });
                }

                @Override
                public void onError(String error) {
                    Toast.makeText(MainActivity.this, error, Toast.LENGTH_SHORT).show();
                }
            });
            TorchManager.getInstance(SettingsUtils.getTorchSource(this), true).toggle(this);
        }
    }

    private boolean isTorchOn() {
        if (isTorchieQuickServiceRunning()) {
            return TorchieQuick.getInstance().getTorchStatus();
        } else {
            return TorchManager.getInstance(SettingsUtils.getTorchSource(this), true).getStatus();
        }
    }
}
