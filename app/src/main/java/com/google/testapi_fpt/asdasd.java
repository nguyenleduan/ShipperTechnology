//package vn.com.fptshop.FPTShop.Uservice;
//
//import androidx.annotation.RequiresApi;
//import androidx.appcompat.app.ActionBar;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.app.ActivityCompat;
//import androidx.core.content.ContextCompat;
//import id.zelory.compressor.Compressor;
//import vn.com.fptshop.FPTShop.Uservice.Model.ChucNangModel;
//import vn.com.fptshop.FPTShop.Uservice.Model.ImageModel;
//import vn.com.fptshop.FPTShop.Uservice.Model.SupmitModel;
//import vn.com.fptshop.FPTShop.Uservice.Model.UpFileModel;
//import vn.com.fptshop.FPTShop.Uservice.Service.AIPService;
//import vn.com.fptshop.FPTShop.lib.Authentication;
//import vn.com.fptshop.FPTShop.lib.ChangeCustom;
//import vn.com.fptshop.FPTShop.lib.ImageHelper;
//import vn.com.fptshop.FPTShop.lib.UIHelper;
//import vn.com.fptshop.R;
//import vn.com.fptshop.mPos.BanHang1.ProductSearchActivity;
//import vn.com.fptshop.mPos.BanHang1.ViewSOActivity;
//import vn.com.fptshop.mPos.BanHang1.model.TonKhoShopGanNhat;
//import vn.com.fptshop.mPos.TuVanPK.TuVanPKActivity;
//import vn.com.fptshop.mPos.mPos.service.MPOSService;
//
//import android.Manifest;
//import android.app.AlertDialog;
//import android.app.Dialog;
//import android.app.ProgressDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.content.pm.PackageManager;
//import android.graphics.Bitmap;
//import android.graphics.BitmapFactory;
//import android.graphics.drawable.ColorDrawable;
//import android.net.Uri;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Environment;
//import android.os.StrictMode;
//import android.provider.MediaStore;
//import android.text.Html;
//import android.util.Base64;
//import android.util.Log;
//import android.view.KeyEvent;
//import android.view.MenuItem;
//import android.view.View;
//import android.widget.AdapterView;
//import android.widget.ArrayAdapter;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.kosalgeek.android.photoutil.CameraPhoto;
//import com.kosalgeek.android.photoutil.GalleryPhoto;
//
//import java.io.ByteArrayOutputStream;
//import java.io.File;
//import java.io.FileNotFoundException;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.lang.reflect.Method;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//import java.util.Random;
//
//public class UserviceActivity extends AppCompatActivity {
//    Spinner spNhomChucNangUserviceActivity, spChucNangUserviceActivity;
//    Context context = this;
//    TextView tvUpThemFileUserviceActivity;
//    ImageView img2UServiceActivity, imgUploadCallogUserviceActivity, img3UServiceActivity, img4UServiceActivity, img5UServiceActivity, img1UServiceActivity;
//    static ArrayList<ImageModel> mListBitMap = new ArrayList<>();
//    ArrayList<ChucNangModel> arrChucNangModel = new ArrayList<>();
//    static ArrayList<String> arrNhomChucNang = new ArrayList<>();
//    static ArrayList<String> arrChucNang = new ArrayList<>();
//    EditText edtContentUserviceActivity;
//    CameraPhoto cameraPhoto;
//    Button btTieptucUservicrActivity;
//    GalleryPhoto galleryPhoto;
//    static String base64ImageCamera = "", base64ImageGallery = "";
//    File compressedImage;
//    static boolean checkapi = false;
//    final int CAMERA = 1;
//    final int GALLERY = 2;
//    String imgName = "_android.jpg";
//    String imgNameAlbum = "_android.jpg";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_uservice);
//        input();
//
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Hỗ trợ khách hàng 87333");
//        actionBar.setHomeButtonEnabled(true);
//        actionBar.setDisplayHomeAsUpEnabled(true);
//        /** init Permission */
//        initPermission();
//        if (Build.VERSION.SDK_INT >= 24) {
//            try {
//                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
//                m.invoke(null);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//
//        cameraPhoto = new CameraPhoto(getApplicationContext());
//        galleryPhoto = new GalleryPhoto(getApplicationContext());
//        Onclick();
//        loadTonKhoShopKhac();
//    }
//
//    public void initPermission() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            if (checkSelfPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//
//                //Permisson don't granted
//                if (shouldShowRequestPermissionRationale(
//                        android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
//                    Toast.makeText(context, "Permission isn't granted ", Toast.LENGTH_SHORT).show();
//                }
//                // Permisson don't granted and dont show dialog again.
//                else {
//                    Toast.makeText(context, "Permisson don't granted and dont show dialog again ", Toast.LENGTH_SHORT).show();
//                }
//                //Register permission
//                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//
//            }
//        }
//    }
//
//    private void LoaDingIMG() {
//        img1UServiceActivity.setVisibility(View.GONE);
//        img2UServiceActivity.setVisibility(View.GONE);
//        img3UServiceActivity.setVisibility(View.GONE);
//        img4UServiceActivity.setVisibility(View.GONE);
//        img5UServiceActivity.setVisibility(View.GONE);
//        for (int ii = 0; ii < mListBitMap.size(); ii++) {
//            if (mListBitMap.get(ii) != null) {
//                if (ii == 0) {
//                    img1UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                    img1UServiceActivity.setVisibility(View.VISIBLE);
//
//                }
//                if (ii == 1) {
//                    img2UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                    img2UServiceActivity.setVisibility(View.VISIBLE);
//
//                }
//                if (ii == 2) {
//                    img3UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                    img3UServiceActivity.setVisibility(View.VISIBLE);
//                }
//                if (ii == 3) {
//                    img4UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                    img4UServiceActivity.setVisibility(View.VISIBLE);
//                }
//                if (ii == 4) {
//                    img5UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                    img5UServiceActivity.setVisibility(View.VISIBLE);
//                }
//
//
//            }
//        }
//
//        if (mListBitMap.size() >= 5) {
//            tvUpThemFileUserviceActivity.setVisibility(View.GONE);
//            imgUploadCallogUserviceActivity.setVisibility(View.GONE);
//        } else {
//            tvUpThemFileUserviceActivity.setVisibility(View.VISIBLE);
//            imgUploadCallogUserviceActivity.setVisibility(View.VISIBLE);
//        }
//    }
//
//    private void input() {
//        ArrayList<String> mListSpiner = new ArrayList<>();
//        img1UServiceActivity = findViewById(R.id.img1UServiceActivity);
//        tvUpThemFileUserviceActivity = findViewById(R.id.tvUpThemFileUserviceActivity);
//        edtContentUserviceActivity = findViewById(R.id.edtContentUserviceActivity);
//        btTieptucUservicrActivity = findViewById(R.id.btTieptucUservicrActivity);
//        img2UServiceActivity = findViewById(R.id.img2UServiceActivity);
//        img5UServiceActivity = findViewById(R.id.img5UServiceActivity);
//        img4UServiceActivity = findViewById(R.id.img4UServiceActivity);
//        img3UServiceActivity = findViewById(R.id.img3UServiceActivity);
//        spNhomChucNangUserviceActivity = findViewById(R.id.spNhomChucNangUserviceActivity);
//        spChucNangUserviceActivity = findViewById(R.id.spChucNangUserviceActivity);
//        imgUploadCallogUserviceActivity = findViewById(R.id.imgUploadCallogUserviceActivity);
//        mListSpiner.add("Chưa có dữ liệu");
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, mListSpiner);
//        spNhomChucNangUserviceActivity.setAdapter(arrayAdapter);
//        spChucNangUserviceActivity.setAdapter(arrayAdapter);
//        tvUpThemFileUserviceActivity.setText(Html.fromHtml("<u>" + "Upload thêm file" + "</u>"));
//    }
//
//    private void SetIMG(int i, String id, Bitmap bitmap) {
//
//        img1UServiceActivity.setVisibility(View.GONE);
//        img2UServiceActivity.setVisibility(View.GONE);
//        img3UServiceActivity.setVisibility(View.GONE);
//        img4UServiceActivity.setVisibility(View.GONE);
//        img5UServiceActivity.setVisibility(View.GONE);
//        try {
//            if (i == -1) {
//                if (mListBitMap.size() < 6) {
//                    Log.e("LOGE", "!!  " + id);
//                    ImageModel model = new ImageModel(mListBitMap.size() - 1, id, bitmap);
//                    mListBitMap.add(model);
//                    for (int ii = 0; ii < mListBitMap.size(); ii++) {
//                        if (ii == 0) {
//                            img1UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                            img1UServiceActivity.setVisibility(View.VISIBLE);
//                        }
//                        if (ii == 1) {
//                            img2UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                            img2UServiceActivity.setVisibility(View.VISIBLE);
//                        }
//                        if (ii == 2) {
//                            img3UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                            img3UServiceActivity.setVisibility(View.VISIBLE);
//                        }
//                        if (ii == 3) {
//                            img4UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                            img4UServiceActivity.setVisibility(View.VISIBLE);
//                        }
//                        if (ii == 4) {
//                            img5UServiceActivity.setImageBitmap(mListBitMap.get(ii).getBitmap());
//                            img5UServiceActivity.setVisibility(View.VISIBLE);
//                        }
//                    }
//                }
//            }
//            if (mListBitMap.size() >= 5) {
//                tvUpThemFileUserviceActivity.setVisibility(View.GONE);
//                imgUploadCallogUserviceActivity.setVisibility(View.GONE);
//            } else {
//                tvUpThemFileUserviceActivity.setVisibility(View.VISIBLE);
//                imgUploadCallogUserviceActivity.setVisibility(View.VISIBLE);
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    private void DialogTiepTuc(int status, String content) {
//        TextView tvThongBaoDialogUservice, tvDialogUserviceActivity, tvContentDialogUserviceActivity;
//        Button btDialogUserviceActivity;
//        ImageView imgCloseDialogUservieActivity;
//        Dialog dialog = new Dialog(this);
//        dialog.setContentView(R.layout.dialog_uservice);
//        tvDialogUserviceActivity = dialog.findViewById(R.id.tvDialogUserviceActivity);
//        imgCloseDialogUservieActivity = dialog.findViewById(R.id.imgCloseDialogUservieActivity);
//        btDialogUserviceActivity = dialog.findViewById(R.id.btDialogUserviceActivity);
//        tvThongBaoDialogUservice = dialog.findViewById(R.id.tvThongBaoDialogUservice);
//        tvContentDialogUserviceActivity = dialog.findViewById(R.id.tvContentDialogUserviceActivity);
//        dialog.show();
//        if (status == 1) {
//            tvThongBaoDialogUservice.setText("Đã tạo yêu cầu thành công.");
//            tvContentDialogUserviceActivity.setText(content);
//        } else if (status == 0) {
//            tvThongBaoDialogUservice.setText("Tạo yêu cầu thất bại. Vui lòng thao tác lại");
//            tvContentDialogUserviceActivity.setVisibility(View.GONE);
//            tvDialogUserviceActivity.setVisibility(View.GONE);
//        }
//        imgCloseDialogUservieActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//        btDialogUserviceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                dialog.dismiss();
//            }
//        });
//    }
//
//    private void Onclick() {
//        btTieptucUservicrActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                DialogTiepTuc(1,"NULL");
//                if (checkapi == true) {
//                    if (!edtContentUserviceActivity.getText().toString().equals("")) {
//                        String s = Authentication.getEmail(context);
//                        s = s.substring(0, s.indexOf("@"));
//                        CreateUservice(mListBitMap, s, ChangeCustom.getGateWayToken(context)
//                                , s, "Uservice 87333", s, s, "HoTroNguoiDung87333"
//                                , edtContentUserviceActivity.getText().toString(), "Phòng CallLog", Authentication.getUserCode(context), spNhomChucNangUserviceActivity.getSelectedItem().toString()
//                                , spChucNangUserviceActivity.getSelectedItem().toString(), Authentication.getJobTitle(context), "012345678");
//
//                    } else
//                        Toast.makeText(context, "Chưa nhập nội dung", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(context, "Nhóm chức năng chưa có dữ liệu", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        tvUpThemFileUserviceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogCamera();
//            }
//        });
//        imgUploadCallogUserviceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogCamera();
//            }
//        });
//        img1UServiceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogIMG(1, img1UServiceActivity);
//            }
//        });
//        img2UServiceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogIMG(2, img2UServiceActivity);
//            }
//        });
//        img3UServiceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogIMG(3, img3UServiceActivity);
//            }
//        });
//        img4UServiceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogIMG(4, img4UServiceActivity);
//            }
//        });
//        img5UServiceActivity.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                DialogIMG(5, img5UServiceActivity);
//            }
//        });
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        finish();
//        return super.onKeyDown(keyCode, event);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == android.R.id.home) {
//            finish();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    private void DialogIMG(int i, ImageView img) {
//        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        b.setTitle("Thông báo");
//        b.setMessage("Bạn muốn thay đổi hay xóa ?");
//        b.setPositiveButton("Thay đổi", new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            public void onClick(DialogInterface dialog, int id) {
//                DialogCamera();
//                mListBitMap.remove(i - 1);
//                img.setVisibility(View.GONE);
//                dialog.cancel();
//            }
//        });
//        b.setNegativeButton("Xóa", new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            public void onClick(DialogInterface dialog, int id) {
//                mListBitMap.remove(i - 1);
//                img.setVisibility(View.GONE);
//                LoaDingIMG();
//                dialog.cancel();
//            }
//        });
//        AlertDialog al = b.create();
//        al.show();
//    }
//
//    private void DialogCamera() {
//        AlertDialog.Builder b = new AlertDialog.Builder(this);
//        b.setTitle("Thông báo");
//        b.setMessage("Bạn muốn chọn Camera hay Album ?");
//        b.setPositiveButton("Camera", new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            public void onClick(DialogInterface dialog, int id) {
//                showDialogUploadImg(1);
//                dialog.cancel();
//            }
//        });
//        b.setNegativeButton("Album", new DialogInterface.OnClickListener() {
//            @RequiresApi(api = Build.VERSION_CODES.O)
//            public void onClick(DialogInterface dialog, int id) {
//                showDialogUploadImg(2);
//                dialog.cancel();
//            }
//        });
//        AlertDialog al = b.create();
//        al.show();
//    }
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    public void showDialogUploadImg(final int keyID) {
//        try {
//            if (keyID == 1) {
//                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (intent.resolveActivity(getPackageManager()) != null) {
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(getTempFile(context)));
//                    startActivityForResult(intent, CAMERA_MTCMND);
//                }
//            } else if (keyID == 2) {
//                startActivityForResult(galleryPhoto.openGalleryIntent(), GALLERY);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//    final int CAMERA_MTCMND = 1;
//
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == RESULT_OK) {
//            if (requestCode == GALLERY) {
//                Uri uri = data.getData();
//                galleryPhoto.setPhotoUri(uri);
//                String photoPath = galleryPhoto.getPath();
//                Bitmap bitmap1 = BitmapFactory.decodeFile(photoPath);
//                if (bitmap1.getWidth() > 1024) {
//                    bitmap1 = resize(photoPath);
//                }
//                try {
//                    FileOutputStream fos = new FileOutputStream(photoPath);
//                    bitmap1.compress(Bitmap.CompressFormat.PNG, 7, fos);
//                    base64ImageGallery = ImageHelper.bitmapToStringFF(bitmap1);
//                    fos.flush();
//                    fos.close();
////                     SetIMG(-1,"model.getFileInfo_fileID()",bitmap1);
//                    Log.e("LOG64", base64ImageGallery);
//                    UpFile(base64ImageGallery, bitmap1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//
//            } else if (requestCode == CAMERA_MTCMND) {
//                try {
//                    final File file = getTempFile(this);
//                    compressedImage = new Compressor(this).setQuality(70).compressToFile(file);
//                    String photoPath = String.valueOf(compressedImage);
//                    Bitmap bitmap1 = BitmapFactory.decodeFile(photoPath);
//                    FileOutputStream fos = new FileOutputStream(photoPath);
//                    bitmap1.compress(Bitmap.CompressFormat.PNG, 7, fos);
//                    base64ImageCamera = ImageHelper.bitmapToStringFF(bitmap1);
//                    Log.e("LOG64", base64ImageCamera);
//                    fos.flush();
//                    fos.close();
////                     SetIMG(-1,"model.getFileInfo_fileID()",bitmap1);
//                    UpFile(base64ImageCamera, bitmap1);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    Log.e("LOGE", e + "");
//                }
//
//            }
//        }
//    }
//
//    public Bitmap decodeFile(String filePath) {
//        Bitmap result = null;
//        Bitmap bmpImage;
//        // Decode image size
//        BitmapFactory.Options o = new BitmapFactory.Options();
//        o.inJustDecodeBounds = true;
//        BitmapFactory.decodeFile(filePath, o);
//
//        // The new size we want to scale to
//        // Find the correct scale value. It should be the power of 2.
//        int width_tmp = o.outWidth, height_tmp = o.outHeight;
//
//        float scale = 800 / (float) width_tmp;
//        float newHeight = height_tmp * scale;
//        int height = (int) newHeight;
//
//
//        // Decode with inSampleSize
//        BitmapFactory.Options o2 = new BitmapFactory.Options();
//        bmpImage = BitmapFactory.decodeFile(filePath, o2);
//        result = Bitmap.createScaledBitmap(bmpImage, 800, height, true);
//
//        return result;
//    }
//
//    public Bitmap resize(String filePath) {
//
//        Bitmap result = null;
//        try {
//            if (filePath != null) {
//                result = decodeFile(filePath);
//            }
//        } catch (Exception e) {
//            Toast.makeText(getApplicationContext(), "Internal error",
//                    Toast.LENGTH_LONG).show();
//            Log.e(e.getClass().getName(), e.getMessage(), e);
//        }
//        return result;
//    }
//
//    private File getTempFile(Context context) {
//        //it will return /sdcard/image.tmp
//        final File path = new File(Environment.getExternalStorageDirectory(), context.getPackageName());
//        if (!path.exists()) {
//            path.mkdir();
//        }
//        return new File(path, imgName);
//    }
//
//    public static String bitmapToStringFF(Bitmap bmp) {
//        try {
//            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            bmp.compress(Bitmap.CompressFormat.PNG, 7, byteArrayOutputStream);
//            byte[] byteArray = byteArrayOutputStream.toByteArray();
//            String encoded = Base64.encodeToString(byteArray, Base64.NO_WRAP);
//            return encoded;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    private void setSpinner(ArrayList<ChucNangModel> arr) {
//        String s = "";
//        for (int i = 0; i < arr.size(); i++) {
//            if (!arr.get(i).getIdNhomChucNang().equals(s)) {
//                arrNhomChucNang.add(arr.get(i).getTenNhomChucNang());
//                s = arr.get(i).getIdNhomChucNang();
//            }
//        }
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, arrNhomChucNang);
//        spNhomChucNangUserviceActivity.setAdapter(arrayAdapter);
//        spNhomChucNangUserviceActivity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                arrChucNang.clear();
//                for (int ii = 0; ii < arrChucNangModel.size(); ii++) {
//                    if (arrChucNangModel.get(ii).getTenNhomChucNang().equals(arrNhomChucNang.get(i))) {
//                        arrChucNang.add(arrChucNangModel.get(ii).getChucNang());
//                        ArrayAdapter arrayAdapterChucNang = new ArrayAdapter(UserviceActivity.this, R.layout.support_simple_spinner_dropdown_item, arrChucNang);
//                        spChucNangUserviceActivity.setAdapter(arrayAdapterChucNang);
//                    }
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
//    }
//
//    protected void loadTonKhoShopKhac() {
//        final ProgressDialog progressDialog = ProgressDialog.show(context, "Xin chờ", "Đang tìm chức năng...", true, false);
//        new AsyncTask<Void, Void, ArrayList<ChucNangModel>>() {
//            @Override
//            protected ArrayList<ChucNangModel> doInBackground(Void... params) {
//                try {
//                    ArrayList<ChucNangModel> result = AIPService.getChucNang(context);
//                    return result;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(ArrayList<ChucNangModel> lst) {
//                progressDialog.dismiss();
//                if (lst != null) {
//                    if (lst.size() > 0) {
//                        arrChucNangModel.addAll(lst);
//                        setSpinner(lst);
//                        checkapi = true;
//                    }
//                } else {
//                    UIHelper.showAlertDialog(context, "Thông báo", "Nạp danh sách chức năng thất bại !!!", R.drawable.ic_error);
//                }
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//    protected void UpFile(final String b64, Bitmap bitmap) {
//        final ProgressDialog progressDialog = ProgressDialog.show(context, "Xin chờ", "Đang UpFile...", true, false);
//        new AsyncTask<Void, Void, UpFileModel>() {
//            @Override
//            protected UpFileModel doInBackground(Void... params) {
//                try {
//                    String s = Authentication.getEmail(context);
//                    s = s.substring(0, s.indexOf("@"));
//                    String m = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()) + "_USERVICE.jpg";
//                    UpFileModel result = AIPService.getUpFile(context, s, b64, m);
//                    return result;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(UpFileModel model) {
//                progressDialog.dismiss();
//                if (model != null) {
//                    SetIMG(-1, model.getFileInfo_fileID(), bitmap);
//                    Log.e("LOGID", model.getFileInfo_fileID() + "");
//                    Toast.makeText(context, "Up thành công", Toast.LENGTH_SHORT).show();
//                    //
//                } else {
//                    UIHelper.showAlertDialog(context, "Thông báo", "Up file thất bại !!!", R.drawable.ic_error);
//                }
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//    //Authentication.getToken(context)
//    protected void CreateUservice(final ArrayList<ImageModel> arrIMG, final String email, final String token
//            , final String ticketOwner, final String tile, final String sender, final String informedUsers,
//                                  final String ProcessKey, final String content, final String phongban, final String mInsede,
//                                  final String nhomchucnang, final String chucnang, final String chucdanh, final String sdt) {
//        final ProgressDialog progressDialog = ProgressDialog.show(context, "Xin chờ", "Đang tạo Ticket...", true, false);
//        new AsyncTask<Void, Void, SupmitModel>() {
//            @Override
//            protected SupmitModel doInBackground(Void... params) {
//                try {
//                    SupmitModel result = AIPService.SupmitUservice(context, arrIMG, email, token, ticketOwner
//                            , tile, sender, informedUsers, ProcessKey, content, phongban, mInsede, nhomchucnang, chucnang, chucdanh, sdt);
//                    return result;
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//
//            @Override
//            protected void onPostExecute(SupmitModel model) {
//                progressDialog.dismiss();
//                if (model != null) {
//                    DialogTiepTuc(1, model.getData_id() + "");
//                    restart();
//                } else {
//                    DialogTiepTuc(0, "NULL");
//                }
//            }
//        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
//    }
//
//    private void restart() {
//        edtContentUserviceActivity.setText(null);
//        mListBitMap.clear();
//        LoaDingIMG();
//    }
//
//}