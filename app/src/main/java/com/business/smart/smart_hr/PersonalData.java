package com.business.smart.smart_hr;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.business.smart.smart_hr.Connections.SqlConnection;
import com.business.smart.smart_hr.Global.GlobalClass;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by ESRAA on 2016-07-12.
 */
public class PersonalData extends Activity {
     GlobalClass globalClass;// = (GlobalClass) getApplicationContext();
    TextView tv_empCode, tv_empName, tv_empPhone, tv_empMobile, tv_empEmail, tv_empGender, tv_empBdate, tv_empSSn,
            tv_empAddress, tv_empSocialStatus, tv_empGraduateYear, tv_empQualification, tv_empJob, tv_empWorkPlace,
            tv_empWorkTime, tv_from, tv_to, tv_empWeekEnd, tv_empManagement;

    String str_empCode, str_empName, str_empPhone, str_empMobile, str_empEmail, str_empGender, str_empBdate, str_empSSn,
            str_empAddress, str_empSocialStatus, str_empGraduateYear, str_empQualifications, str_empJob, str_empWorkPlace,
            str_empWorkTime, str_from, str_to, str_empWeekEnd, str_empManagement;

    String Saturday, Sunday, Monday, Tuesday, Wensday, Thursday, Friday;
    int LSaturday, LSunday, LMonday, LTuesday, LWensday, LThursday, LFriday;

    int CheckGender;
    int CheckSocialStatus;

    String  strEmployeeID ;
    ImageView personal_image;
    private Uri fileUri;
    String mCurrentPhotoPath;
    SqlConnection sqlConnection;
    private static int RESULT_LOAD_IMAGE = 1;
    static final int REQUEST_IMAGE_CAPTURE = 0;
    public static final int MEDIA_TYPE_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);

        globalClass = (GlobalClass) getApplicationContext();

        setTitle("Personal Data");

        strEmployeeID= globalClass.getStrEmployeeID();

        sqlConnection = new SqlConnection();
//        getActionBar().setDisplayHomeAsUpEnabled(true);

        personal_image = (ImageView) findViewById(R.id.personal_image);
        tv_empCode = (TextView) findViewById(R.id.tv_Code);
        tv_empName = (TextView) findViewById(R.id.tv_Name);
        tv_empPhone = (TextView) findViewById(R.id.tv_phone);
        tv_empMobile = (TextView) findViewById(R.id.tv_mobile);
        tv_empEmail = (TextView) findViewById(R.id.tv_email);
        tv_empGender = (TextView) findViewById(R.id.tv_gender);
        tv_empBdate = (TextView) findViewById(R.id.tv_Bdate);
        tv_empSSn = (TextView) findViewById(R.id.tv_ssn);
        tv_empAddress = (TextView) findViewById(R.id.tv_address);
        tv_empSocialStatus = (TextView) findViewById(R.id.tv_socialStatus);
        tv_empGraduateYear = (TextView) findViewById(R.id.tv_graduation);
        tv_empQualification = (TextView) findViewById(R.id.tv_qualifications);
        tv_empJob = (TextView) findViewById(R.id.tv_Job);
        tv_empWorkPlace = (TextView) findViewById(R.id.tv_workPlace);
        tv_empWorkTime = (TextView) findViewById(R.id.tv_emp_timeOfWork);
        tv_from = (TextView) findViewById(R.id.tv_From);
        tv_to = (TextView) findViewById(R.id.tv_To);
        tv_empWeekEnd = (TextView) findViewById(R.id.tv_weekend);
        tv_empManagement = (TextView) findViewById(R.id.tv_managment);


        personal_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when you click on personal image >> go to selectImage method
                selectImage();
            }
        });

        ImageButton imageButton = (ImageButton) findViewById(R.id.backbutton);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PersonalData.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        ImageButton homeButton = (ImageButton) findViewById(R.id.homebtn);
        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent intent = new Intent(PersonalData.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });
        ConnectDB connectDB = new ConnectDB();
        connectDB.execute();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK /*&& null != data*/) {
            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            // String picturePath contains the path of selected Image
            personal_image = (ImageView) findViewById(R.id.personal_image);
            personal_image.setImageBitmap(BitmapFactory.decodeFile(picturePath));
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            personal_image.setImageBitmap(imageBitmap);
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    private void selectImage() {

//        Set Dialogbox Items
        final CharSequence[] items = {"Take Photo", "Choose from Library",
                "Cancel"};
        //dialog to select choice from it
        AlertDialog.Builder builder = new AlertDialog.Builder(PersonalData.this);
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                //Requesting Permission at Runtime
                boolean result = Utility.checkPermission(PersonalData.this);

                //Check for Camera Request
                if (items[item].equals("Take Photo")) {
//                    userChoosenTask = "Take Photo";
                    //this option  opens camera to take photo and load it automatically
                    Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name

                    startActivityForResult(intent, 0);
                    if (result)
//                        cameraIntent();
                        dispatchTakePictureIntent();
                    // choose from gallery
                } else if (items[item].equals("Choose from Library")) {
//                    userChoosenTask = "Choose from Library";
                    Intent i = new Intent(
                            Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(i, RESULT_LOAD_IMAGE);
                    if (result)
                        galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    /**
     * Create a file Uri for saving an image or video
     */
    private static Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MyCameraApp");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_" + timeStamp + ".jpg");
        } /*else if(type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");
        }*/ else {
            return null;
        }

        return mediaFile;
    }

    public static class Utility {
        public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;

        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        public static boolean checkPermission(final Context context) {
            int currentAPIVersion = Build.VERSION.SDK_INT;
            if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                        alertBuilder.setCancelable(true);
                        alertBuilder.setTitle("Permission necessary");
                        alertBuilder.setMessage("External storage permission is necessary");
                        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                            }
                        });
                        AlertDialog alert = alertBuilder.create();
                        alert.show();
                    } else {
                        ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                    }
                    return false;
                } else {
                    return true;
                }
            } else {
                return true;
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void galleryIntent() {

        //Tip:  if you want to get images as well as videos, you can use following code
//        intent.setType("image/* video/*");
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
//        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }


    public class ConnectDB extends AsyncTask<String, String, String> {
        String z = "";

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onPostExecute(String r) {
            tv_empCode.setText(str_empCode);
            tv_empName.setText( str_empName);
            tv_empPhone.setText(str_empPhone);
            tv_empEmail.setText(  str_empEmail);
            tv_empGender.setText(str_empGender);
            tv_empBdate.setText( str_empBdate);
            tv_empSSn.setText(str_empSSn);
            tv_empAddress.setText( str_empAddress);
            tv_empSocialStatus.setText( str_empSocialStatus);
            tv_empGraduateYear.setText( str_empGraduateYear);
            tv_empQualification.setText( str_empQualifications);
            tv_empJob.setText( str_empJob);
            tv_empWorkPlace.setText(str_empWorkPlace);
            tv_from.setText("من: " + str_from);
            tv_to.setText("إلى : " + str_to);
            tv_empWeekEnd.setText( Saturday + Sunday + Monday + Tuesday + Wensday + Thursday + Friday);
            tv_empManagement.setText( str_empManagement);
        }

        @Override
        protected String doInBackground(String... params) {
            Connection con = sqlConnection.CONN();
            try {
                if (con == null) {
                    z = "Error in connection with Database server";
                } else {
                    String query = "select EmployeeName,EmployeeCode,EmployeePhone,EmployeeEmail,Gender,\n" +
                            "BirthDate,EmployeeIDCard,SocialStatus,GraduationDate,EmployeeCertificate,\n" +
                            "EFrom,ETo,LSaturday,LSunday,LMonday,LTuesday,LWedensday,LThursday,LFriday,\n" +
                            "(select JobName from SMRT_Job where JobID=SMRT_Employee.JobID)as jobname,\n" +
                            "(select LocationName from SMRT_Location where LocationID=SMRT_Employee.LocationID)as LocName,\n" +
                            "(select DepartmentName from SMRT_Department where DepartmentID=SMRT_Employee.DepartmentID)as DeptName\n" +
                            "from SMRT_Employee where EmployeeID=" + strEmployeeID;
                    Statement stmt = con.createStatement();
                    ResultSet rs = stmt.executeQuery(query);


                    while (rs.next()) {
                        str_empCode = rs.getString("EmployeeCode");
                        str_empName = rs.getString("EmployeeName");
                        str_empPhone = rs.getString("EmployeePhone");
                        str_empMobile = rs.getString("EmployeeName");
                        str_empEmail = rs.getString("EmployeeEmail");
//                        str_empGender = rs.getString("Gender");
                        str_empBdate = rs.getString("BirthDate");
                        str_empSSn = rs.getString("EmployeeIDCard");
                        str_empAddress = rs.getString("EmployeeName");
                        str_empGraduateYear = rs.getString("GraduationDate");
                        str_empQualifications = rs.getString("EmployeeCertificate");
                        str_empJob = rs.getString("jobname");
                        str_empWorkPlace = rs.getString("LocName");
                        str_from = rs.getString("EFrom");
                        str_to = rs.getString("ETo");
                        str_empWeekEnd = rs.getString("EmployeeName");
                        str_empManagement = rs.getString("DeptName");

                        CheckSocialStatus = Integer.parseInt(rs.getString("SocialStatus"));

                        if(CheckSocialStatus==0){
                            str_empSocialStatus="أعزب";
                        }else if (CheckSocialStatus==1){
                            str_empSocialStatus="متزوج";
                        }else if (CheckSocialStatus==2){
                            str_empSocialStatus="متزوج و يعول";
                        }else if (CheckSocialStatus==3){
                            str_empSocialStatus="أرمل";
                        }else if (CheckSocialStatus==4){
                            str_empSocialStatus="مطلق";
                        }

                        CheckGender = Integer.parseInt(rs.getString("Gender"));

                        if (CheckGender == 0) {
                            str_empGender = "ذكر";
                        } else {
                            str_empGender = "أنثى ";
                        }
                        LSaturday = Integer.parseInt(rs.getString("LSaturday"));
                        LSunday = Integer.parseInt(rs.getString("LSunday"));
                        LMonday = Integer.parseInt(rs.getString("LMonday"));
                        LTuesday = Integer.parseInt(rs.getString("LTuesday"));
                        LWensday = Integer.parseInt(rs.getString("LWedensday"));
                        LThursday = Integer.parseInt(rs.getString("LThursday"));
                        LFriday = Integer.parseInt(rs.getString("LFriday"));

//

                        if (LSaturday == 0) {
                            Saturday = "السبت" + "-";
                        } else {
                            Saturday = "";
                        }
                        if (LSunday == 0) {
                            Sunday = "الأحد";
                        } else {
                            Sunday = "";
                        }
                        if (LMonday == 0) {
                            Monday = "الاثنين";
                        } else {
                            Monday = "";
                        }
                        if (LTuesday == 0) {
                            Tuesday = "الثلاثاء" + "-";
                        } else {
                            Tuesday = "";
                        }
                        if (LWensday == 0) {
                            Wensday = "الأربعاء";
                        } else {
                            Wensday = "";
                        }
                        if (LThursday == 0) {
                            Thursday = "الخميس";
                        } else {
                            Thursday = "";
                        }
                        if (LFriday == 0) {
                            Friday = "الجمعة";
                        } else {
                            Friday = "";
                        }

                    }
                    z = "Success";
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return z;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
