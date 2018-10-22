package com.pinpinbox.android.pinpinbox2_0_0.fragment;

import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.pinpinbox.android.R;
import com.pinpinbox.android.Utility.BitmapUtility;
import com.pinpinbox.android.Utility.FileUtility;
import com.pinpinbox.android.Utility.HttpUtility;
import com.pinpinbox.android.Utility.TextUtility;
import com.pinpinbox.android.pinpinbox2_0_0.activity.Creation2Activity;
import com.pinpinbox.android.pinpinbox2_0_0.adapter.RecyclerPdfFileAdapter;
import com.pinpinbox.android.pinpinbox2_0_0.custom.LoadingAnimation;
import com.pinpinbox.android.pinpinbox2_0_0.custom.PPBApplication;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DialogStyleClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.stringClass.DirClass;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.Key;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.MyLog;
import com.pinpinbox.android.pinpinbox2_0_0.custom.widget.PinPinToast;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.CheckExecute;
import com.pinpinbox.android.pinpinbox2_0_0.dialog.DialogV2Custom;
import com.pinpinbox.android.pinpinbox2_0_0.model.Protocol58_InsertPhotoOfDiy;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class FragmentSelectPDF2 extends Fragment implements View.OnClickListener {

    private LoaderManager loaderManager;

    private LoadingAnimation loading;

    private DialogV2Custom dlgUploading;

    private Protocol58_InsertPhotoOfDiy protocol58;

    private RecyclerPdfFileAdapter adapter;

    private List<HashMap<String, Object>> filePathList;

    private RecyclerView rvPdfFile;

    private PDFView pdfView;

    private RelativeLayout rBottom;

    private TextView tvStartUpLoad;

    private String album_id;

    private int page = 0;

    private boolean isUploading = false;


    private LoaderManager.LoaderCallbacks<Cursor> loaderCallbacks = new LoaderManager.LoaderCallbacks<Cursor>() {

        private CursorLoader cursorLoader;

        private String[] projection = {
                MediaStore.Files.FileColumns.DATA,
                MediaStore.Files.FileColumns.MIME_TYPE,
                MediaStore.Files.FileColumns.TITLE

        };


        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            MyLog.Set("d", FragmentSelectPDF2.class, "loader.getId => " + id);


            cursorLoader = new CursorLoader(getActivity(), MediaStore.Files.getContentUri("external"),
                    projection,
                    null,
                    null,
                    null);

            return cursorLoader;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, final Cursor cursor) {
            MyLog.Set("d", FragmentSelectPDF2.class, "onLoadFinished");

            getPdfContent(cursor);

        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            MyLog.Set("d", FragmentSelectPDF2.class, "onLoaderReset");
        }


        private void getPdfContent(Cursor cursor) {

            loading.show();

            cursor.moveToLast();

            do {

                String path = cursor.getString(cursor.getColumnIndex(projection[0]));
                File file = new File(path);
                if (file.exists() && FileUtility.isPdf_(file.getName())) {

                    HashMap<String, Object> map = new HashMap<>();
                    map.put("name", file.getName());
                    map.put("path", path);


                    MyLog.Set("e", FragmentSelectPDF2.class, "path => " + path);
                    MyLog.Set("d", FragmentSelectPDF2.class, "---------------------------------------------------------------------");


                    filePathList.add(map);

                }

            } while (cursor.moveToPrevious());

            adapter.notifyDataSetChanged();

            loading.dismiss();

        }

    };


    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {

                case 0:

                    Bitmap bitmap = BitmapUtility.createViewBitmapByLight(BitmapUtility.createViewBitmap(pdfView), 0.85f);

                    BitmapUtility.saveToLocal(bitmap, DirClass.getPdfDir(getActivity(), PPBApplication.getInstance().getId()) + page + ".jpg", 100);

                    if (bitmap != null && !bitmap.isRecycled()) {
                        bitmap.recycle();
                    }
                    bitmap = null;

                    page = page + 1;

                    pdfView.jumpTo(page);

                    if (page < pdfView.getPageCount()) {

                        Message mmm = new Message();
                        mmm.what = 0;
                        mHandler.sendMessageDelayed(mmm, 500);

                    } else {

                        loading.dismiss();

                        doUpload();

                    }

                    break;

            }

        }

    };


    /*
     * onCreate
     * onCreateView
     * onActivityCreated
     */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_2_0_0_select_pdf, container, false);

        rvPdfFile = (RecyclerView) v.findViewById(R.id.rvPdfFile);

        rBottom = (RelativeLayout) v.findViewById(R.id.rBottom);

        tvStartUpLoad = (TextView) v.findViewById(R.id.tvStartUpLoad);

        pdfView = (PDFView) v.findViewById(R.id.pdfView);

        (v.findViewById(R.id.backImg)).setOnClickListener(this);

        TextUtility.setBold(tvStartUpLoad, true);

        return v;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getBundle();

        init();

        setRecycler();

        createDir();

        loaderManager.initLoader(3, null, loaderCallbacks);

    }

    private void getBundle() {
        album_id = getArguments().getString(Key.album_id, "");
    }


    private void init() {

        loading = new LoadingAnimation(getActivity());
        loading.dialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                checkStopUpload();
                return true;
            }
        });

        loaderManager = getLoaderManager();

        filePathList = new ArrayList<>();

    }

    private void setRecycler() {

        adapter = new RecyclerPdfFileAdapter(getActivity(), filePathList);

        rvPdfFile.setAdapter(adapter);

        LinearLayoutManager manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvPdfFile.setLayoutManager(manager);


        adapter.setOnRecyclerViewListener(new RecyclerPdfFileAdapter.OnRecyclerViewListener() {
            @Override
            public void onItemClick(int position, View v) {

//                pdfView.fromFile(new File((String)filePathList.get(position).get("path")))   //设置pdf文件地址
//                        .swipeHorizontal(true)
//                        .pageSnap(true)
//                        .autoSpacing(true)
//                        .pageFling(true)
//                        .onLoad(new OnLoadCompleteListener() {
//                            @Override
//                            public void loadComplete(int nbPages) {
//
//
//
//                            }
//                        })
//
//                        .load();


            }

            @Override
            public boolean onItemLongClick(int position, View v) {
                return false;
            }
        });


        adapter.setOnUploadListener(new RecyclerPdfFileAdapter.OnUploadListener() {
            @Override
            public void onClick(int position) {

                loading.show();

                PinPinToast.ShowToast(getActivity(), R.string.pinpinbox_2_0_0_toast_message_converting_files);

                changeToImage(position);


            }
        });


    }

    private void createDir() {

        FileUtility.createPdfFile(getActivity(), PPBApplication.getInstance().getId());

    }

    private void changeToImage(int position) {

        pdfView.fromFile(new File((String) filePathList.get(position).get("path")))   //设置pdf文件地址
                .swipeHorizontal(true)
                .pageSnap(true)
                .autoSpacing(true)
                .pageFling(true)
                .onLoad(new OnLoadCompleteListener() {
                    @Override
                    public void loadComplete(int nbPages) {
                    }
                })
                .load();

        Message msg = new Message();
        msg.what = 0;
        mHandler.sendMessageDelayed(msg, 1000);

    }

    private void hideFragment() {

        getActivity().getSupportFragmentManager().beginTransaction().hide(this).commit();

    }

    private void checkStopUpload() {

        if (isUploading) {

            if (dlgUploading == null) {
                dlgUploading = new DialogV2Custom(getActivity());
                dlgUploading.setStyle(DialogStyleClass.CHECK);
                dlgUploading.setMessage(R.string.pinpinbox_2_0_0_dialog_message_stop_to_upload);
                dlgUploading.getTvRightOrBottom().setText(R.string.pinpinbox_2_0_0_button_stop);
                dlgUploading.setCheckExecute(new CheckExecute() {
                    @Override
                    public void DoCheck() {


                    }
                });
            }
            dlgUploading.show();

        }

    }

    private void upLoadStart() {

        loading.show();
        isUploading = true;

    }

    private void upLoadStop() {
        loading.dismiss();
        isUploading = false;
    }

    private void doUpload() {

        if (!HttpUtility.isConnect(getActivity())) {
            ((Creation2Activity) getActivity()).setNoConnect();
            return;
        }

        rBottom.setVisibility(View.VISIBLE);
        tvStartUpLoad.setText(R.string.pinpinbox_2_0_0_button_uploading);

        protocol58 = new Protocol58_InsertPhotoOfDiy(
                getActivity(),
                album_id,
                FileUtility.getPictureByDir(DirClass.getPdfDir(getActivity(), PPBApplication.getInstance().getId())),
                new Protocol58_InsertPhotoOfDiy.TaskCallBack() {
                    @Override
                    public void Prepare() {
                        upLoadStart();
                    }

                    @Override
                    public void UploadSuccessOnUiThread(int uploadedCount) {
                        tvStartUpLoad.setText(getResources().getString(R.string.pinpinbox_2_0_0_button_uploaded) + uploadedCount);
                    }

                    @Override
                    public void Post() {
                        upLoadStop();

                        rBottom.setVisibility(View.GONE);

                        FileUtility.delAllFile(DirClass.getPdfDir(getActivity(), PPBApplication.getInstance().getId()));
                    }

                    @Override
                    public void Success() {

                        ((Creation2Activity) getActivity()).reFreshBottomPhoto();
                        hideFragment();

                    }

                    @Override
                    public void TimeOut() {

                    }
                }

        );


    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.backImg:

                hideFragment();

                break;

        }

    }

    @Override
    public void onDestroy() {

        FileUtility.delAllFile(DirClass.getPdfDir(getActivity(), PPBApplication.getInstance().getId()));

        pdfView.recycle();

        pdfView = null;

        super.onDestroy();
    }

}
