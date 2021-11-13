package com.app.brandmania.Fragment.bottom;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.databinding.DataBindingUtil;

import com.app.brandmania.BuildConfig;
import com.app.brandmania.R;
import com.app.brandmania.utils.CodeReUse;
import com.app.brandmania.utils.Utility;
import com.app.brandmania.databinding.FragmentPickerBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class PickerFragment extends BottomSheetDialogFragment {
    private Activity act;
    private FragmentPickerBinding binding;
    private final boolean isVideoMode = false;

    private TiggerEventParents eventParents;

    private HandlerImageLoad imageLoad;
    private int actionId;
    private String cameraFilePath;
    public final static String FOLDER = Environment.getExternalStorageDirectory() + "/PDF";

    public PickerFragment(Activity act) {
        this.act = act;
    }

    //rotate 90 degree is any horizontal image
    private static Bitmap rotateBitmap(Bitmap bitmap, int orientation) {
        Matrix matrix = new Matrix();
        switch (orientation) {
            case ExifInterface.ORIENTATION_FLIP_HORIZONTAL:
                matrix.setScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                matrix.setRotate(180);
                break;
            case ExifInterface.ORIENTATION_FLIP_VERTICAL:
                matrix.setRotate(180);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_TRANSPOSE:
                matrix.setRotate(90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_90:
                matrix.setRotate(90);
                break;
            case ExifInterface.ORIENTATION_TRANSVERSE:
                matrix.setRotate(-90);
                matrix.postScale(-1, 1);
                break;
            case ExifInterface.ORIENTATION_ROTATE_270:
                matrix.setRotate(-90);
                break;
            default:
                return bitmap;
        }
        try {
            Bitmap bmRotated = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
         //   bitmap.recycle();
            return bmRotated;
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            return null;
        }
    }

    public void setSelectedBitmapForFullView(Bitmap selectedBitmapForFullView) {
    }



    public void setImageUrl(String imageUrl) {
    }

    public void setEnableViewMode(boolean enableViewMode) {
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setWhiteNavigationBar(dialog);
        }
        return dialog;
    }

    public void setCalledFragmentContext(int calledFragmentContext) {
    }

    public int getActionId() {
        return actionId;
    }

    @Override
    public View onCreateView( LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_picker, container, false);

        View v = binding.getRoot();

        act = getActivity();

        binding.chooseCameraIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideoMode) {
                    checkForPermissions(2);
                } else {
                    checkForPermissions(0);
                }
            }
        });
        binding.chooseGalleryIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isVideoMode) {
                    checkForPermissions(3);
                } else {
                    checkForPermissions(1);
                }
            }
        });

        binding.choosePdfIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                checkForPermissions(4);

            }
        });

//        if (isEnableViewMode) {
//            binding.viewImageIntent.setVisibility(View.VISIBLE);
//            binding.viewImageIntent.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Utility.OnImageViewDismiss viewDismiss = new Utility.OnImageViewDismiss() {
//                        @Override
//                        public void onPhotoDialogDismiss() {
//                            hideView();
//                        }
//                    };
//                    if (actionId != Constant.PICKER_PROFILE) {
//                        if (selectedBitmapForFullView != null)
//                            Utility.fullScreenImageViewerBitmap(act, selectedBitmapForFullView, viewDismiss);
//                        else
//                            Utility.fullScreenImageViewer(act, imageUrl);
//                    } else {
//                        if (selectedBitmapForFullView != null)
//                            Utility.viewProfileFullMode(act, "", selectedBitmapForFullView);
//                        else
//                            Utility.viewProfileFullMode(act, imageUrl, null);
//                    }
//                }
//            });
//        }
//        if (calledFragmentContext == Constant.ADD_EVENT_ACTIVITY) {
//            binding.choosePdfIntent.setVisibility(View.VISIBLE);
//        }
        return v;

    }

    private void checkForPermissions(int flagForCall) {
        if (ContextCompat.checkSelfPermission(act, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(act, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(act, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            Utility.showAlert(act, "Please Enable Permission for accessing photos and camera");
        } else {
            if (flagForCall == 0) {
                captureFromCamera();
            } else if (flagForCall == 1) {
                selectImageFromGallery();
            } else if (flagForCall == 2) {
                captureVideoWithCamera();
            } else if (flagForCall == 3) {
                selectVideoFromGallery();
            } else if (flagForCall == 4) {
                selectPdf();
            }

        }
    }

    private void captureVideoWithCamera() {
        Intent videoCaptureIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
        if (videoCaptureIntent.resolveActivity(act.getPackageManager()) != null) {
            startActivityForResult(videoCaptureIntent, CodeReUse.SELECT_VIDEO_CAMERA);
        }
    }

    private void selectVideoFromGallery() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), CodeReUse.SELECT_VIDEO_GALLERY);
    }

    public void setActionId(int actionId) {
        this.actionId = actionId;
    }

    private void setSelectedImage(Uri selectedImage) {
        String selectedImagePath = getRealPathFromURIPath(selectedImage, act);
        Utility.loadImageOnURI(act, binding.selectedImage, selectedImage);
        Bitmap bitmap;
        try {

            bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), selectedImage);

            imageLoad.onGalleryResult(actionId, bitmap);
            this.dismiss();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void selectPdf() {


        Intent intent = new Intent();
        //sets the select file to all types of files
        intent.setType("application/pdf");
        //allows to select data and return it
        intent.setAction(Intent.ACTION_GET_CONTENT);
        //starts new activity to select file and return data
        startActivityForResult(Intent.createChooser(intent, "Select Pdf"), CodeReUse.PDF_INTENT);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
                CropImage.ActivityResult result = CropImage.getActivityResult(data);
                if (resultCode == RESULT_OK) {
                    Uri resultUri = result.getUri();
                    setSelectedImage(resultUri);
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    Exception error = result.getError();
                    error.printStackTrace();
                }
                this.dismiss();
            } else if (requestCode == CodeReUse.GALLERY_INTENT) {


                if (data != null) {
                    Uri selectedImage = data.getData();
                    CropImage.activity(selectedImage)
                            .setGuidelines(CropImageView.Guidelines.ON)
                            .setMultiTouchEnabled(true)
                            .setOutputCompressFormat(Bitmap.CompressFormat.PNG)
                            .start(getContext(), this);

//                    String selectedImagePath = getRealPathFromURIPath(selectedImage, act);
//                    if (eventParents != null)
//                        eventParents.handleEventOnImageSelection(1);
//                    Utility.loadImageOnURI(act, binding.selectedImage, selectedImage);
//                    Bitmap bitmap;
//                    try {
//                        bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), selectedImage);
//                        Bitmap imageRotate;
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                            ExifInterface exifObject = new ExifInterface(selectedImagePath);
//                            int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
//                            imageRotate = rotateBitmap(bitmap, orientation);
//                        } else {
//                            imageRotate = bitmap;
//                        }
//
//                        if (imageRotate == null || imageRotate.getByteCount() == 0) {
//                            imageRotate = bitmap;
//                        }
//
//                        if (actionId == Constant.PICKER_PROFILE) {
//                            CropImage.activity(selectedImage)
//                                    .setGuidelines(CropImageView.Guidelines.ON)
//                                    .setAspectRatio(1, 1)
//                                    .setOutputCompressQuality(100)
//                                    .setRequestedSize(700, 700)
//                                    .start(getContext(), this);
//                        } else {
//
//                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
//                            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, stream);
//
//                            byte[] byteArray = stream.toByteArray();
//                            Bitmap compressedBitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
//
//                            imageLoad.onGalleryResult(actionId, compressedBitmap);
//                            this.dismiss();
//                        }
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }

            } else if (requestCode == CodeReUse.CAMERA_INTENT && resultCode == RESULT_OK) {

                String selectedImagePath = getRealPathFromURIPath(Uri.parse(cameraFilePath), act);

                Utility.loadImageOnURI(act, binding.selectedImage, Uri.parse(cameraFilePath));
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(act.getContentResolver(), Uri.parse(cameraFilePath));
                    Bitmap imageRotate;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        ExifInterface exifObject = new ExifInterface(selectedImagePath);
                        int orientation = exifObject.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                        imageRotate = rotateBitmap(bitmap, orientation);
                    } else {
                        imageRotate = bitmap;
                    }

                    if (imageRotate == null || imageRotate.getByteCount() == 0) {
                        imageRotate = bitmap;
                    }
                    imageLoad.onGalleryResult(actionId, bitmap);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                this.dismiss();
            } else if (requestCode == CodeReUse.SELECT_VIDEO_GALLERY) {
                if (data != null) {
                    Uri selectedImageUri = data.getData();
                    String selectedImagePath = getPath(selectedImageUri);

                }
                this.dismiss();
            } else if (requestCode == CodeReUse.SELECT_VIDEO_CAMERA) {
                if (data != null) {
                    Uri uri = data.getData();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (act.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                            String pathToStoredVideo = getRealPathFromURIPath(uri, act);

                        } else {
                            Utility.showAlert(act, "Please Enable Permission for accessing photos and camera");
                        }
                    }
                }
                this.dismiss();
            } else if (requestCode == CodeReUse.PDF_INTENT) {
                if (data != null) {
                    Uri filePath = data.getData();
                   // generateImageFromPdf(filePath);
                } else {
                    this.dismiss();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
            this.dismiss();
        }

    }

//    void generateImageFromPdf(Uri pdfUri) {
//        int pageNumber = 0;
//        PdfiumCore pdfiumCore = new PdfiumCore(act);
//        try {
//            //http://www.programcreek.com/java-api-examples/index.php?api=android.os.ParcelFileDescriptor
//            ParcelFileDescriptor fd = act.getContentResolver().openFileDescriptor(pdfUri, "r");
//            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
//            pdfiumCore.openPage(pdfDocument, pageNumber);
//            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
//            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
//            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
//            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
//            imageLoad.onGalleryResult(actionId, bmp);
//
//            pdfiumCore.closeDocument(pdfDocument);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        this.dismiss();
//    }

    private void saveImage(Bitmap bmp) {
        FileOutputStream out = null;
        try {
            File folder = new File(FOLDER);
            if (!folder.exists())
                folder.mkdirs();
            File file = new File(folder, "PDF.png");
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 100, out); // bmp is your Bitmap instance
        } catch (Exception e) {
            //todo with exception
        } finally {
            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {
                //todo with exception
            }
        }
    }

    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                act.getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (thumbnail.getWidth() < 600) {
            Bitmap b2 = Bitmap.createScaledBitmap(thumbnail, 1000, 1000, false);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            b2.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
            imageLoad.onGalleryResult(actionId, b2);
            this.dismiss();
        } else {
            imageLoad.onGalleryResult(actionId, thumbnail);
            this.dismiss();
        }
    }

    /**
     * Capture image from camera
     */
    private void captureFromCamera() {
        try {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".fileProvider", createImageFile()));
            startActivityForResult(intent, CodeReUse.CAMERA_INTENT);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        //This is the directory in which the file will be created. This is the default location of Camera photos
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DCIM), "Camera");
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for using again
        cameraFilePath = "file://" + image.getAbsolutePath();
        return image;
    }

    private void selectImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png"};
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);
        startActivityForResult(intent, CodeReUse.GALLERY_INTENT);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void setWhiteNavigationBar(@NonNull Dialog dialog) {
        Window window = dialog.getWindow();
        if (window != null) {
            DisplayMetrics metrics = new DisplayMetrics();
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);

            GradientDrawable dimDrawable = new GradientDrawable();
            // ...customize your dim effect here

            GradientDrawable navigationBarDrawable = new GradientDrawable();
            navigationBarDrawable.setShape(GradientDrawable.RECTANGLE);
            navigationBarDrawable.setColor(Color.WHITE);

            Drawable[] layers = {dimDrawable, navigationBarDrawable};

            LayerDrawable windowBackground = new LayerDrawable(layers);
            windowBackground.setLayerInsetTop(1, metrics.heightPixels);

            window.setBackgroundDrawable(windowBackground);
        }
    }

    private String getPath(Uri uri) {
        Cursor cursor = act.getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = act.getContentResolver().query(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA));
        cursor.close();
        return path;
    }

    public void setImageLoad(HandlerImageLoad imageLoad) {
        this.imageLoad = imageLoad;
    }


    private void hideView() {
        this.dismiss();
    }

    public interface HandlerImageLoad {
        void onGalleryResult(int flag, Bitmap bitmap);
    }

    interface TiggerEventParents {
        void handleEventOnImageSelection(int flag);
    }

    private String getRealPathFromURIPath(Uri contentURI, Activity activity) {
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            return contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }
    }

}
