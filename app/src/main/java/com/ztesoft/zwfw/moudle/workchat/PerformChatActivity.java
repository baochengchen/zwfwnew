package com.ztesoft.zwfw.moudle.workchat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.ImagePickerAdapter;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.HeaderPic;
import com.ztesoft.zwfw.domain.QueryUser;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.UnicodeUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.utils.http.RequestMap;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PerformChatActivity extends BaseActivity implements ImagePickerAdapter.OnRecyclerViewItemClickListener {

    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    public static final int REQUEST_CODE_USER = 102;

    public static final int ACTION_ATTCHMENT = 0;
    public static final int ACTION_CONTENT = 1;

    public static final int RESULT_SEND = 1000;


    private ImagePickerAdapter mImgadapter;
    private ArrayList<ImageItem> mSelImageList; //当前选择的所有图片
    private int maxImgCount = 8;               //允许选择图片最大数


    TextView mSearchUsersTv;
    EditText mChatTitleEdt, mChatContentEdt;
    CheckBox mPublicChex;

    private QueryUser mQueryUser;
    private String mTitle;
    private String mContent;
    private boolean isPulic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perform_chat);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.perform_chat));
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        mSearchUsersTv = (TextView) findViewById(R.id.search_users_tv);
        mChatTitleEdt = (EditText) findViewById(R.id.chat_title_edt);
        mChatContentEdt = (EditText) findViewById(R.id.chat_content_edt);
        mPublicChex = (CheckBox) findViewById(R.id.public_check);
        mPublicChex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isPulic = isChecked;
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSelImageList = new ArrayList<>();
        mImgadapter = new ImagePickerAdapter(this, mSelImageList, maxImgCount);
        mImgadapter.setOnItemClickListener(this);

        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mImgadapter);

    }


    public void searchUsers(View view) {
        startActivityForResult(new Intent(mContext, SearchUsersActivity.class), REQUEST_CODE_USER);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                List<ImageItem> items = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (items != null) {
                    mSelImageList.addAll(items);
                    mImgadapter.setImages(mSelImageList);
                }

            }

        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                List<ImageItem> items = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                if (items != null) {
                    mSelImageList.clear();
                    mSelImageList.addAll(items);
                    mImgadapter.setImages(mSelImageList);
                }
            }
        } else if (resultCode == SearchUsersActivity.RESULT_CODE_SELECT) {
            //选择提问人返回
            if (data != null && requestCode == REQUEST_CODE_USER) {
                mQueryUser = (QueryUser) data.getSerializableExtra("queryUser");
                mSearchUsersTv.setText(mQueryUser.getUserCode());
            }
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(View view, int position) {

        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - mSelImageList.size());
                Intent intent1 = new Intent(PerformChatActivity.this, ImageGridActivity.class);
                                /* 如果需要进入选择的时候显示已经选中的图片，
                                 * 详情请查看ImagePickerActivity
                                 * */
//                                intent1.putExtra(ImageGridActivity.EXTRAS_IMAGES,images);
                startActivityForResult(intent1, REQUEST_CODE_SELECT);
                break;
            default:
                //打开预览
                Intent intentPreview = new Intent(this, ImagePreviewDelActivity.class);
                intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) mImgadapter.getImages());
                intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                break;
        }
    }

    RequestManager.RequestListener mListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest(String url, int actionId) {
            showProgressDialog("发送中...");
        }

        @Override
        public void onSuccess(String response, String url, int actionId) {
            hideProgressDialog();
            switch (actionId) {
                case ACTION_ATTCHMENT:
                    List<HeaderPic> headerPics = JSONArray.parseArray(response, HeaderPic.class);
                    StringBuffer sb = new StringBuffer();
                    for (HeaderPic headerPic : headerPics) {
                        sb.append(headerPic.getId()).append(",");
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        String attchemnts = sb.substring(0, sb.length() - 1);
                        uploadContent(attchemnts);
                    }

                    break;
                case ACTION_CONTENT:
                    if (TextUtils.equals(response, "true")) {
                        Toast.makeText(mContext, "发送成功", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_SEND);
                        finish();
                    } else {
                        Toast.makeText(mContext, "发送失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            hideProgressDialog();
            Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    };


    private void uploadContent(String attchemnts) {

        Chat chat = new Chat();
        //chat.setTitle(mTitle);
        chat.setContent(UnicodeUtils.string2Unicode(mContent));
        chat.setAttachments(attchemnts);
        if (null != mQueryUser)
            chat.setToUserId(Long.parseLong(mQueryUser.getUserId()));
        chat.setByUserId(Long.parseLong(getmUser().getUserId()));
        chat.setIsPublic(mPublicChex.isChecked() ? "Y" : "N");
        chat.setState("A");
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_ADDCHAT, JSON.toJSONString(chat), mListener, ACTION_CONTENT);
    }

    public void send(View view) {

        if (null == mQueryUser && !isPulic) {
            Toast.makeText(mContext, "请选择想要提问的人", Toast.LENGTH_SHORT).show();
            return;
        }

    /*    mTitle = mChatTitleEdt.getText().toString().trim();
        if (TextUtils.isEmpty(mTitle)){
            Toast.makeText(mContext, "请输入标题", Toast.LENGTH_SHORT).show();
            return;
        }*/

        mContent = mChatContentEdt.getText().toString().trim();
        if (TextUtils.isEmpty(mContent)) {
            Toast.makeText(mContext, "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }

        //upload attchments
        if (0 < mSelImageList.size()) {
            RequestMap map = new RequestMap();
            for (int i = 0; i < mSelImageList.size(); i++) {
                map.put("chatAttach_Img_" + i, new File(mSelImageList.get(i).path));
            }
            RequestManager.getInstance().upload(Config.BASE_URL + Config.URL_ATTACHMENT, map, mListener, ACTION_ATTCHMENT);
        } else {
            uploadContent("");
        }

    }

}
