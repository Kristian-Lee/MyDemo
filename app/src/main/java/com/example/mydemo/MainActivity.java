package com.example.mydemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.core.os.EnvironmentCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.example.mydemo.activity.AActivity;
import com.example.mydemo.databinding.ActivityMainBinding;
import com.example.mydemo.fragment.FragmentActivity;
import com.example.mydemo.listview.ListViewActivity;
import com.example.mydemo.navigation.NavigationActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;
//import com.zhihu.matisse.Matisse;
//import com.zhihu.matisse.MimeType;
//import com.zhihu.matisse.engine.impl.GlideEngine;
//import com.zhihu.matisse.engine.impl.PicassoEngine;
//import com.zhihu.matisse.filter.Filter;
//import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.example.mydemo.recycleview.RecycleViewActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PHOTO_PICK_REQUEST_CODE = 1002;
    private static final int RECYCLE_IMAGE_CHOOSE_CODE = 1001;
    private static final int CAMERA_REQUEST_CODE = 2;
    private static final String ID = "0001";
    private static final String NAME = "MyChannel";

    private TimePickerView pvTime;
    private ActivityMainBinding binding;
    private NotificationChannel mChannel;

    //???????????????????????????uri
    private Uri mCameraUri;

    // ????????????????????????????????????Android 10????????????????????????????????????
    private String mCameraImagePath;

    // ?????????Android 10????????????
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        binding.tv1.setSelected(true);
        binding.tv1.setText(Html.fromHtml("<u>Hello World!</u>"));

        setListeners();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PHOTO_PICK_REQUEST_CODE);
        }

        ImmersionBar.with(this)
                .titleBar(R.id.toolbar)
                .init();
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onResume() {
        getNotificationChannel().setShowBadge(false);
        super.onResume();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public NotificationChannel getNotificationChannel() {
        if (mChannel == null) {
            mChannel = new NotificationChannel(ID, NAME, NotificationManager.IMPORTANCE_LOW);
        }
        return mChannel;
    }

    public void call() {
        try {
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:10010"));
            startActivity(intent);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    private void camera() {
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // ?????????????????????
        if (captureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            Uri photoUri = null;

            if (isAndroidQ) {
                // ??????android 10
                photoUri = createImageUri();
            } else {
                try {
                    photoFile = createImageFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (photoFile != null) {
                    mCameraImagePath = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //??????Android 7.0?????????????????????FileProvider????????????content?????????Uri
                        photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", photoFile);
                    } else {
                        photoUri = Uri.fromFile(photoFile);
                    }
                }
            }
            mCameraUri = photoUri;
            if (photoUri != null) {
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                captureIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                startActivityForResult(captureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }

    /**
     * ??????????????????uri,?????????????????????????????? Android 10????????????????????????
     */
    private Uri createImageUri() {
        String status = Environment.getExternalStorageState();
        // ???????????????SD???,????????????SD?????????,?????????SD????????????????????????
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * ???????????????????????????
     */
    private File createImageFile() throws IOException {
        String imageName = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    call();
                } else {
                    Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera();
                } else {
                    Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
            case PHOTO_PICK_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    camera();
                } else {
                    Toast.makeText(this, "??????????????????????????????????????????", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        call();
    }

    private void initTimePicker() {//Dialog ???????????????????????????
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                Toast.makeText(MainActivity.this, getTime(date), Toast.LENGTH_SHORT).show();
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{false, true, true, false, false, false})
                .isDialog(true) //????????????false ??????????????????DecorView ????????????????????????
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(5) //?????????????????????????????????1???????????????6???????????????????????????7???
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .setTitleText("????????????")
                .isCyclic(true)
                .build();

        pvTime.show();
        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//??????????????????
                dialogWindow.setGravity(Gravity.BOTTOM);//??????Bottom,????????????
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }
    private String getTime(Date date) {//???????????????????????????????????????
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("MM???dd???");
        return format.format(date);
    }

    private void setListeners() {
        onClick onClick = new onClick();
        binding.btnLv.setOnClickListener(onClick);
        binding.btnWv.setOnClickListener(onClick);
        binding.btnActivity.setOnClickListener(onClick);
        binding.btnFragment.setOnClickListener(onClick);
        binding.btnContacts.setOnClickListener(onClick);
        binding.btnDownload.setOnClickListener(onClick);
        binding.btnNavigation.setOnClickListener(onClick);
        binding.btnSearchView.setOnClickListener(onClick);
        binding.btnRecycleView.setOnClickListener(onClick);
        binding.btnNotify.setOnClickListener(v -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                Intent intent = new Intent(MainActivity.this, MainActivity.class);
                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
                NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                //??????????????????

                getNotificationChannel().setShowBadge(true);
                manager.createNotificationChannel(getNotificationChannel());
                Notification notification = new NotificationCompat.Builder(MainActivity.this)
                        .setContentTitle("???????????????")
                        .setContentText("Happy Niu Year!")
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("??????????????? Google ??? Android O ????????????????????????" +
                                "??????????????????????????????????????????????????????????????????????????????????????????" +
                                "?????????????????????????????????????????????????????????????????????????????????" +
                                "??????????????????????????????????????????????????????????????????????????????????????????" +
                                "??? Android O ???????????????????????????????????????????????????????????????????????????????????????????????????"))
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .setChannelId(ID)
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setPriority(Notification.PRIORITY_MAX)
                        .build();
                manager.notify(1, notification);
            }
        });
        binding.ivSquareImage.setOnClickListener(v -> {
            PictureSelector.create(MainActivity.this)
                    .openGallery(PictureMimeType.ofImage())
                    .imageEngine(GlideEngine.createGlideEngine())
                    .maxSelectNum(3)
                    .isEnableCrop(true)//??????????????????
                    .withAspectRatio(1, 1)//????????????
                    .rotateEnabled(true)//???????????????????????????
                    .scaleEnabled(true)//?????????????????????????????????
                    .showCropFrame(true)// ?????????????????????????????? ???????????????????????????false
                    .showCropGrid(true)//?????????????????????????????? ???????????????????????????false
                    .isMaxSelectEnabledMask(true)
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        });
        binding.ivCircleImage.setOnClickListener(v -> PictureSelector.create(MainActivity.this)
                .openGallery(PictureMimeType.ofImage())
                .imageEngine(GlideEngine.createGlideEngine())
                .isEnableCrop(true)
                .selectionMode(PictureConfig.SINGLE)
                .circleDimmedLayer(true)// ????????????????????????
                .withAspectRatio(1, 1)//????????????
                .scaleEnabled(true)//?????????????????????????????????
                .forResult(RECYCLE_IMAGE_CHOOSE_CODE));
        binding.et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        binding.rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = group.findViewById(checkedId);
                Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
            }
        });
        binding.cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Toast.makeText(MainActivity.this, isChecked?"?????????":"????????????", Toast.LENGTH_SHORT).show();
            }
        });
        binding.btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE}, 1);
                } else {
                    call();
                }
            }

        });
        binding.btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
                } else {
                    camera();
                }
            }
        });
        binding.btnBottomSheetDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
                bottomSheetDialog.setContentView(R.layout.dialog_bottom_sheet);
                bottomSheetDialog.show();
            }
        });
        binding.btnDatePicker.setOnClickListener(v -> initTimePicker());
    }
    private class onClick implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = null;
            switch (v.getId()) {
                case R.id.btnLv:
                    intent = new Intent(MainActivity.this, ListViewActivity.class);
                    break;

                case R.id.btnWv:
                    intent = new Intent(MainActivity.this, WebViewActivity.class);
                    break;
                case R.id.btnActivity:
                    intent = new Intent(MainActivity.this, AActivity.class);
                    break;
                case R.id.btnFragment:
                    intent = new Intent(MainActivity.this, FragmentActivity.class);
                    break;
                case R.id.btnContacts:
                    intent = new Intent(MainActivity.this, ContactsActivity.class);
                    break;
                case R.id.btnDownload:
                    intent = new Intent(MainActivity.this, DownloadActivity.class);
                    break;
                case R.id.btnNavigation:
                    intent = new Intent(MainActivity.this, NavigationActivity.class);
                    break;
                case R.id.btnSearchView:
                    intent = new Intent(MainActivity.this, SearchViewActivity.class);
                    break;
                case R.id.btnRecycleView:
                    intent = new Intent(MainActivity.this, RecycleViewActivity.class);
                    break;
            }
            startActivity(intent);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                if (isAndroidQ) {
                    // Android 10 ????????????uri??????
                    binding.ivPhoto.setImageURI(mCameraUri);
                } else {
                    // ????????????????????????
                    binding.ivPhoto.setImageBitmap(BitmapFactory.decodeFile(mCameraImagePath));
                }
            } else {
                Toast.makeText(this,"??????",Toast.LENGTH_LONG).show();
            }
        }
        else if (requestCode == PictureConfig.CHOOSE_REQUEST && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            Glide.with(this).load(selectList.get(0).getCutPath()).into(binding.ivSquareImage);
        }
        else if (requestCode == RECYCLE_IMAGE_CHOOSE_CODE && resultCode == RESULT_OK) {
            List<LocalMedia> selectList = PictureSelector.obtainMultipleResult(data);
            Glide.with(this).load(selectList.get(0).getCutPath()).into(binding.ivCircleImage);
        }
    }
}