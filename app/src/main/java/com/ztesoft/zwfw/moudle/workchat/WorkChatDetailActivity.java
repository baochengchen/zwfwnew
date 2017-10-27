package com.ztesoft.zwfw.moudle.workchat;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.adapter.ImagePickerAdapter;
import com.ztesoft.zwfw.base.BaseActivity;
import com.ztesoft.zwfw.domain.Chat;
import com.ztesoft.zwfw.domain.Comment;
import com.ztesoft.zwfw.domain.HeaderPic;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.utils.SoftKeyBoardListener;
import com.ztesoft.zwfw.utils.UnicodeUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.utils.http.RequestMap;
import com.ztesoft.zwfw.widget.NoScrollGridView;
import com.ztesoft.zwfw.widget.NoScrollListView;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WorkChatDetailActivity extends BaseActivity implements View.OnClickListener, ImagePickerAdapter.OnRecyclerViewItemClickListener {


    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;


    public static final int ACTION_ADD_COMMENT = 0;
    public static final int ACTION_GET_COMMENT = 1;
    public static final int ACTION_UPDATECHAT = 2;
    public static final int ACTION_ATTCHMENT = 3;

    private Chat mChat;
    private int mType;
    LinearLayout mReplyLayout;
    EditText mReplyEdt;
    Button mSendBt;
    private CommentAdapter mCommentAdapter;
    private List<Comment> mComments = new ArrayList<>();
    private Comment mComment;

    private ImagePickerAdapter mImgadapter;
    private ArrayList<ImageItem> mSelImageList; //当前选择的所有图片
    private int maxImgCount = 4;               //允许选择图片最大数


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_work_chat_detail);
        TextView csTitile = (TextView) findViewById(R.id.cs_title);
        csTitile.setText("问题详情");
        findViewById(R.id.cs_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageView rightView = (ImageView) findViewById(R.id.cs_right_view);
        rightView.setVisibility(View.VISIBLE);
        rightView.setImageResource(R.mipmap.ic_edit_reply);
        rightView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReplyEdt();
            }
        });

        TextView titleTv = (TextView) findViewById(R.id.title_tv);
        TextView contentTv = (TextView) findViewById(R.id.content_tv);
        TextView creatorTv = (TextView) findViewById(R.id.creator_tv);
        TextView creatTimeTv = (TextView) findViewById(R.id.time_tv);
        GridView imgGv = (GridView) findViewById(R.id.img_gv);
        NoScrollListView commentLv = (NoScrollListView) findViewById(R.id.commentLv);
        mSendBt = (Button) findViewById(R.id.send_bt);
        mSendBt.setOnClickListener(this);


        mReplyLayout = (LinearLayout) findViewById(R.id.reply_layout);
        mReplyEdt = (EditText) findViewById(R.id.reply_edt);
        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                mReplyLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void keyBoardHide(int height) {
                mReplyLayout.setVisibility(View.GONE);
            }
        });

        mReplyEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (EditorInfo.IME_ACTION_SEND == actionId) {
                    return true;
                }
                return false;
            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mSelImageList = new ArrayList<>();
        mImgadapter = new ImagePickerAdapter(this, mSelImageList, maxImgCount);
        mImgadapter.setOnItemClickListener(this);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 4));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(mImgadapter);


        mChat = (Chat) getIntent().getSerializableExtra("chat");
        mType = getIntent().getIntExtra("type", 0);

        if (mType == Config.TYPE_PUBLIC)
            rightView.setVisibility(View.GONE);

        titleTv.setText(mChat.getTitle());
        contentTv.setText(UnicodeUtils.unicode2String(mChat.getContent()));
        creatorTv.setText("来自 " + mChat.getByUserName());
        creatTimeTv.setText(mChat.getCreateDate());
        String attachments = mChat.getAttachments();
        if (!TextUtils.isEmpty(attachments)) {
            final String[] imgIds = attachments.split(",");
            imgGv.setAdapter(new ImageAdapter(imgIds));
            imgGv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = new Intent(WorkChatDetailActivity.this, PhotoViewActivity.class);
                    intent.putExtra("imgs", imgIds);
                    intent.putExtra("position", position);
                    startActivity(intent);
                }
            });
        }

        mCommentAdapter = new CommentAdapter();
        commentLv.setAdapter(mCommentAdapter);

        updateChat();
        requestComment();


    }

    private void updateChat() {
        Map<String, String> map = new HashMap<>();
        map.put("id", mChat.getId());
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_UPDATECHAT, JSON.toJSONString(map), mListener, ACTION_UPDATECHAT);
    }


    private void requestComment() {
        Map<String, String> map = new HashMap<>();
        map.put("chatId", mChat.getId());
        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_GETCOMMENTS, JSON.toJSONString(map), mListener, ACTION_GET_COMMENT);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.send_bt:
                sendReply();
                break;
        }
    }

    private void sendReply() {
        String replyStr = mReplyEdt.getText().toString().trim();
        if (TextUtils.isEmpty(replyStr)) {
            Toast.makeText(mContext, "请输入回复内容", Toast.LENGTH_SHORT).show();
            return;
        }

        //upload attchments
        if (0 < mSelImageList.size()) {
            RequestMap map = new RequestMap();
            for (int i = 0; i < mSelImageList.size(); i++) {
                map.put("commentAttach_Img_" + i, new File(mSelImageList.get(i).path));
            }
            RequestManager.getInstance().upload(Config.BASE_URL + Config.URL_ATTACHMENT, map, mListener, ACTION_ATTCHMENT);
        } else {
            addComment("");
        }
    }

    RequestManager.RequestListener mListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest(String url, int actionId) {
            switch (actionId){
                case ACTION_ADD_COMMENT:
                case ACTION_ATTCHMENT:
                    showProgressDialog("回复中..");
                    break;
            }
        }

        @Override
        public void onSuccess(String response, String url, int actionId) {
            hideProgressDialog();
            switch (actionId) {
                case ACTION_ADD_COMMENT:
                    mComments.add(mComment);
                    mCommentAdapter.notifyDataSetChanged();
                    break;
                case ACTION_GET_COMMENT:
                    mComments = JSON.parseArray(response, Comment.class);
                    mCommentAdapter.notifyDataSetChanged();
                    break;
                case ACTION_ATTCHMENT:
                    List<HeaderPic> headerPics = JSONArray.parseArray(response, HeaderPic.class);
                    StringBuffer sb = new StringBuffer();
                    for (HeaderPic headerPic : headerPics) {
                        sb.append(headerPic.getId()).append(",");
                    }
                    if (!TextUtils.isEmpty(sb)) {
                        String attchemnts = sb.substring(0, sb.length() - 1);
                        addComment(attchemnts);
                    }

                    break;
            }
        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            hideProgressDialog();
            switch (actionId) {
                case ACTION_ADD_COMMENT:
                    Toast.makeText(mContext, "回复失败", Toast.LENGTH_SHORT).show();
                    break;

                case ACTION_GET_COMMENT:
                    break;

                case ACTION_UPDATECHAT:

                    break;

            }

        }
    };


    private void addComment(String attachments) {

        mComment = new Comment();
        mComment.setChatId(Long.parseLong(mChat.getId()));
        mComment.setByUserId(Long.parseLong(getmUser().getUserId()));
        mComment.setToUserId(mType == Config.TYPE_MINE ? mChat.getToUserId() : mChat.getByUserId());
        mComment.setByUserName(getmUser().getUserName());
        mComment.setToUserName(mType == Config.TYPE_MINE ? mChat.getToUserName() : mChat.getByUserName());
        mComment.setAttachments(attachments);
        String replyStr = mReplyEdt.getText().toString().trim();
        mComment.setContent(UnicodeUtils.string2Unicode(replyStr));

        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_TALK_ADDCOMMENT, JSON.toJSONString(mComment), mListener, ACTION_ADD_COMMENT);

        hideReplyEdt();
    }


    private Handler mHandler = new Handler();

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                showReplyEdt();
            }
        }, 500);

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
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemClick(View view, int position) {
        switch (position) {
            case IMAGE_ITEM_ADD:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(maxImgCount - mSelImageList.size());
                Intent intent1 = new Intent(WorkChatDetailActivity.this, ImageGridActivity.class);
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


    class ImageAdapter extends BaseAdapter {

        private String[] imgIds;

        public ImageAdapter(String[] imgIds) {
            this.imgIds = imgIds;
        }

        @Override
        public int getCount() {
            return imgIds == null ? 0 : imgIds.length;
        }

        @Override
        public Object getItem(int position) {
            return imgIds == null ? null : imgIds[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View view, ViewGroup parent) {
            if (view == null) {
                view = LayoutInflater.from(mContext).inflate(R.layout.list_item_image, parent, false);
            }
            ImageView iv_img = (ImageView) view.findViewById(R.id.iv_img);
            //ImagePicker.getInstance().getImageLoader().displayImage((Activity) mContext, mChat.imgs.get(position), iv_img, 0, 0);
            ImageLoader.getInstance().displayImage(Config.BASE_URL + Config.URL_ATTACHMENT + "/" + imgIds[position], iv_img);

            return view;
        }


    }


    class CommentAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mComments.size();
        }

        @Override
        public Object getItem(int position) {
            return mComments.size() == 0 ? null : mComments.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Comment comment = mComments.get(position);
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.comment_item, parent, false);
            }
            TextView userTv = (TextView) convertView.findViewById(R.id.user_tv);
            TextView commentTv = (TextView) convertView.findViewById(R.id.comment_tv);
            NoScrollGridView gridView = (NoScrollGridView) convertView.findViewById(R.id.comment_img_gv);
            String byUserName = comment.getByUserName();
            SpannableStringBuilder ssb = new SpannableStringBuilder(byUserName + "：");
            ssb.setSpan(new ForegroundColorSpan(Color.parseColor("#708090")), 0, byUserName.length() + 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            userTv.setText(ssb);
            commentTv.setText(UnicodeUtils.unicode2String(comment.getContent()));
            if (null != comment.getAttachments()){
                gridView.setVisibility(View.VISIBLE);
                final String[] imgIds = comment.getAttachments().split(",");
                gridView.setAdapter(new ImageAdapter(imgIds));
                gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(WorkChatDetailActivity.this, PhotoViewActivity.class);
                        intent.putExtra("imgs", imgIds);
                        intent.putExtra("position", position);
                        startActivity(intent);
                    }
                });
            }else{
                gridView.setVisibility(View.GONE);
            }

            return convertView;
        }
    }


    private void showReplyEdt() {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        mReplyEdt.setFocusable(true);
        mReplyEdt.setFocusableInTouchMode(true);
        mReplyEdt.requestFocus();
        imm.showSoftInput(mReplyEdt, 0);

    }


    private void hideReplyEdt() {
        mReplyEdt.setText("");
        mSelImageList.clear();
        mImgadapter.setImages(mSelImageList);
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mReplyEdt.getWindowToken(), 0);

    }
}
