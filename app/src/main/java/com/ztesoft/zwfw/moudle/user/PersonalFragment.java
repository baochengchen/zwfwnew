package com.ztesoft.zwfw.moudle.user;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.ztesoft.zwfw.domain.Version;
import com.ztesoft.zwfw.domain.resp.HttpErrorResp;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.domain.HeaderPic;
import com.ztesoft.zwfw.domain.User;
import com.ztesoft.zwfw.moudle.LoginActivity;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.moudle.service.UpdateService;
import com.ztesoft.zwfw.utils.APPPreferenceManager;
import com.ztesoft.zwfw.utils.SessionUtils;
import com.ztesoft.zwfw.utils.http.RequestManager;
import com.ztesoft.zwfw.utils.http.RequestMap;
import com.ztesoft.zwfw.widget.CircleImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.List;

/**
 * Created by BaoChengchen on 2017/8/17.
 */

public class PersonalFragment extends BaseFragment implements View.OnClickListener {


    private static final int[] optionImg = {R.mipmap.icon_edit, R.mipmap.icon_lock, R.mipmap.icon_update, R.mipmap.icon_about};
    private static final String[] optionStr = {"编辑个人信息", "修改密码", "检查新版本", "关于我们"};

    private static final int REQUEST_TAKEPHOTO = 0x00;
    private static final int REQUEST_FROMFILE = 0x01;

    private static final int ACTION_ATTCHMENT = 0;
    private static final int ACTION_SETHEAD = 1;
    private static final int ACTION_GETHEAD = 2;
    private static final int ACTION_CHECKVERSION = 3;

    private String SDdir = Environment.getExternalStorageDirectory().toString();
    private String headImgPath =SDdir+"/ztesoft/zwfw/images/head.jpg";
    View mView;
    ListView mListView;
    CircleImageView mHeaderView;
    Button mLogoffBt;
    AlertDialog mHeaderSetDialog;
    TextView mNameTv, mPhoneTv, mEmailTv, mAddrTv;

    private User mUser;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_layout_personal, container, false);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mView.findViewById(R.id.tt_layout).setBackgroundResource(R.mipmap.sy_top);
        TextView csTitile = (TextView) mView.findViewById(R.id.cs_title);
        csTitile.setText(getString(R.string.person_center));
        csTitile.setTextColor(getResources().getColorStateList(R.color.white));
        mView.findViewById(R.id.cs_back).setVisibility(View.GONE);


        mNameTv = (TextView) mView.findViewById(R.id.username_tv);
        mPhoneTv = (TextView) mView.findViewById(R.id.phone_tv);
        mEmailTv = (TextView) mView.findViewById(R.id.email_tv);
        mAddrTv = (TextView) mView.findViewById(R.id.addr_tv);

        mHeaderView = (CircleImageView) mView.findViewById(R.id.header_img);
        mHeaderView.setOnClickListener(this);


        mLogoffBt = (Button) mView.findViewById(R.id.logoff_btn);
        mLogoffBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_LOGOUT, "{}", new RequestManager.RequestListener() {
                    @Override
                    public void onRequest(String url, int actionId) {

                    }

                    @Override
                    public void onSuccess(String response, String url, int actionId) {

                        APPPreferenceManager.getInstance().saveObject(getActivity(), Config.IS_LOGIN, false);
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                        getActivity().finish();
                    }

                    @Override
                    public void onError(String errorMsg, String url, int actionId) {
                        if(SessionUtils.invalid(errorMsg)){
                            Toast.makeText(getActivity(),"会话超时",Toast.LENGTH_SHORT).show();
                            APPPreferenceManager.getInstance().saveObject(getActivity(), Config.IS_LOGIN, false);
                            startActivity(new Intent(getActivity(),LoginActivity.class));
                            getActivity().finish();
                        }else{
                            Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_SHORT).show();
                        }

                    }
                },0);
            }
        });

        mListView = (ListView) mView.findViewById(R.id.list_view);
        mListView.setAdapter(new MyAdapter());
        setListViewHeightBasedOnChildren(mListView);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Intent intentMeInfo = new Intent(getActivity(),MeInfoActivity.class);
                        startActivity(intentMeInfo);
                        break;
                    case 1:
                        Intent intentResetPwd = new Intent(getActivity(),ResetPwdActivity.class);
                        startActivity(intentResetPwd);
                        break;
                    case 2:
                        checkVersion();
                        break;
                    case 3:
                        Intent intentAbout = new Intent(getActivity(),AboutActivity.class);
                        startActivity(intentAbout);
                        break;
                }
            }
        });


        mUser = JSON.parseObject(APPPreferenceManager.getInstance().getString(getActivity(),Config.USERINFO), User.class);
        if(TextUtils.isEmpty(APPPreferenceManager.getInstance().getString(getActivity(),"headerURL"))){
            //调用头像接口
            requestHead();
        }else{
            Glide.with(getActivity()).load(APPPreferenceManager.getInstance().getString(getActivity(),"headerURL")).error(R.mipmap.icon_default_header_img).diskCacheStrategy(DiskCacheStrategy.ALL).into(mHeaderView);
           // ImageLoader.getInstance().displayImage(APPPreferenceManager.getInstance().getString(getActivity(),"headerURL"),mHeaderView);
        }

    }


    private void requestHead() {
        RequestManager.getInstance().getHeader(Config.BASE_URL+Config.URL_USERS+"/"+mUser.getUserId()+"/headshot",mListener,ACTION_GETHEAD,new HashMap<String, String>());
    }


    @Override
    public void onResume() {
        super.onResume();

        mUser = JSON.parseObject(APPPreferenceManager.getInstance().getString(getActivity(),Config.USERINFO), User.class);
        mNameTv.setText(mUser.getUserName());
        mPhoneTv.setText(mUser.getPhone());
        mEmailTv.setText(mUser.getEmail());
        mAddrTv.setText(mUser.getAddress());


    }

    public static PersonalFragment newInstance() {

        Bundle args = new Bundle();
        PersonalFragment fragment = new PersonalFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.header_img:
                setHeader();
                break;
            case R.id.takePhoto:
                mHeaderSetDialog.dismiss();
                Intent intent1 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                getOutputFileUri();
                intent1.putExtra(MediaStore.EXTRA_OUTPUT, getOutputFileUri());
                startActivityForResult(intent1, REQUEST_TAKEPHOTO);
                break;

            case R.id.fromFile:
                mHeaderSetDialog.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_GET_CONTENT);
                intent2.addCategory(Intent.CATEGORY_OPENABLE);
                intent2.setType("image/*");
                startActivityForResult(intent2, REQUEST_FROMFILE);
                break;
        }

    }

    private Uri getOutputFileUri() {
        String SDstate = Environment.getExternalStorageState();
        if (!SDstate.equals(Environment.MEDIA_MOUNTED)) {
            Toast.makeText(getActivity(), "SD卡不可读", Toast.LENGTH_SHORT).show();
        } else {

            // /data/data/com.example.fileoperation/files
            //Log.d("bcc", "" + getActivity().getApplicationContext().getFilesDir().getAbsolutePath());
          /*  File fileDir = new File(getActivity().getApplicationContext().getFilesDir().
                    getAbsolutePath()+"/images/");*/
            File fileDir = new File(SDdir + "/ztesoft/zwfw/images/");
            if (!fileDir.exists())
                fileDir.mkdirs();
            File fileName = new File(headImgPath);
            return Uri.fromFile(fileName);
        }
        return null;
    }

    private void setHeader() {
        if (null == mHeaderSetDialog)
            mHeaderSetDialog = new AlertDialog.Builder(getActivity()).create();
        mHeaderSetDialog.show();
        Window window = mHeaderSetDialog.getWindow();
        window.setContentView(R.layout.headset_dialog);
        Button takePhoto = (Button) window.findViewById(R.id.takePhoto);
        takePhoto.setOnClickListener(this);
        Button fromFile = (Button) window.findViewById(R.id.fromFile);
        fromFile.setOnClickListener(this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                // 如果是直接从相册获取
                case REQUEST_FROMFILE:
                    startPhotoZoom(data.getData());
                    break;
                // 如果是调用相机拍照时
                case REQUEST_TAKEPHOTO:
                    File temp = new File(headImgPath);
                    startPhotoZoom(Uri.fromFile(temp));
                    break;
                // 取得裁剪后的图片
                case 3:
                    if (data != null) {
                        setPicToView(data);
                    }
                    break;
                default:
                    break;
            }
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    private void setPicToView(Intent picdata) {
        Bundle extras = picdata.getExtras();
        if (extras != null) {
            Bitmap bitmap = extras.getParcelable("data");
            FileOutputStream b = null;
            try {
                b = new FileOutputStream(headImgPath);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                savePictoServer();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            } finally {
                try {
                    b.flush();
                    b.close();
                } catch (Exception e) {
                    // TODO: handle exception
                    e.printStackTrace();
                }
                mHeaderView.setImageBitmap(bitmap);
            }
        }
    }

    private void savePictoServer() {
        RequestMap map = new RequestMap();
        map.put("FILE",new File(headImgPath));
        RequestManager.getInstance().upload(Config.BASE_URL+Config.URL_ATTACHMENT, map,mListener,ACTION_ATTCHMENT);
    }


    RequestManager.RequestListener mListener = new RequestManager.RequestListener() {
        @Override
        public void onRequest(String url, int actionId) {

        }

        @Override
        public void onSuccess(String response, String url, int actionId) {
            switch (actionId){
                case ACTION_ATTCHMENT:
                    List<HeaderPic> headerPics = JSONArray.parseArray(response,HeaderPic.class);
                    if(headerPics!=null&&headerPics.size()>0){
                        RequestManager.getInstance().getHeader(Config.BASE_URL+Config.URL_USERS+"/"+mUser.getUserId()+"/headshot/"+headerPics.get(0).getId(),mListener,ACTION_SETHEAD,new HashMap<String, String>());
                        APPPreferenceManager.getInstance().saveObject(getActivity(),"headerURL",Config.BASE_URL+Config.URL_ATTACHMENT+"/"+headerPics.get(0).getId());
                    }else{
                        Toast.makeText(getActivity(),"上传头像失败",Toast.LENGTH_SHORT).show();
                    }
                    break;

                case ACTION_SETHEAD:
                    Toast.makeText(getActivity(),"头像设置成功",Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_GETHEAD:
                    Glide.with(getActivity()).load(Config.BASE_URL+Config.URL_ATTACHMENT+"/"+response.substring(1,response.length()-1)).error(R.mipmap.icon_default_header_img).diskCacheStrategy(DiskCacheStrategy.ALL).into(mHeaderView);
                    break;
                case ACTION_CHECKVERSION:
                    try {
                        String localVersionName = getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(),0).versionName;
                        Version version = JSON.parseObject(response,Version.class);
                        if(TextUtils.equals(localVersionName,version.getValue())){
                            new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("当前已是最新版本").setPositiveButton("确定",null).show();
                        }else{
                            new AlertDialog.Builder(getActivity()).setTitle("提示").setMessage("检测到新版本\n"+version.getValue()).setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //download
                                    Intent intent = new Intent(getActivity(), UpdateService.class);
                                    intent.putExtra("downloadUrl",Config.BASE_URL+Config.URL_DOWNLOADFILE+"?fileName=zwfw.apk&path=/");
                                    getActivity().startService(intent);
                                }
                            }).setNegativeButton("以后更新",null).show();
                        }
                    } catch (PackageManager.NameNotFoundException e) {
                        e.printStackTrace();
                    }

                    break;
            }

        }

        @Override
        public void onError(String errorMsg, String url, int actionId) {
            switch (actionId){
                case ACTION_ATTCHMENT:
                    Toast.makeText(getActivity(),"上传头像失败",Toast.LENGTH_SHORT).show();
                    break;
                case ACTION_SETHEAD:
                    Toast.makeText(getActivity(),"设置头像失败",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public void startPhotoZoom(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 3);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return optionStr.length;
        }

        @Override
        public Object getItem(int i) {
            return optionStr[i];
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            view = LayoutInflater.from(getActivity()).inflate(R.layout.item_my_option, null);
            LinearLayout mask = (LinearLayout) view.findViewById(R.id.mask);
            ImageView optImg = (ImageView) view.findViewById(R.id.option_img);
            TextView optTv = (TextView) view.findViewById(R.id.option_tv);
            optImg.setImageResource(optionImg[i]);
            optTv.setText(optionStr[i]);
            return view;
        }
    }

    public void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void checkVersion() {
        RequestManager.getInstance().getHeader(Config.BASE_URL+Config.URL_CHECKVERSION,mListener,ACTION_CHECKVERSION,new HashMap<String, String>());
    }
}
