package com.ztesoft.zwfw.moudle.todo;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.ztesoft.zwfw.domain.Exception;
import com.ztesoft.zwfw.domain.ServiceEvaluate;
import com.ztesoft.zwfw.moudle.Config;
import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.Consult;
import com.ztesoft.zwfw.domain.Supervise;
import com.ztesoft.zwfw.domain.Task;
import com.ztesoft.zwfw.domain.WebSiteInterAction;
import com.ztesoft.zwfw.domain.Work;
import com.ztesoft.zwfw.domain.resp.ApplyInfoResp;
import com.ztesoft.zwfw.utils.http.RequestManager;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by BaoChengchen on 2017/8/16.
 */

public class ApplyInfoFragment extends BaseFragment {

    View rootView;
    TextView mApplyType, mCertificateType, mCertificateNumber, mApplyer,
            mSex, mBirthday, mAddress, mContentDescribe;

    TextView mNameTv, mMobileTv, mEmailTv, mAddrTv, mTitleTv, mOrgNameTv, mInteractionTypeTv, mContentTv;

    LinearLayout mTaskLayout, mConsultLayout;


    private Task mTask;
    private Consult mConsult;
    private Supervise mSupervise;
    private Exception mException;
    private ServiceEvaluate mEvaluate;
    private String mType;

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

        rootView = inflater.inflate(R.layout.fragment_apply_info, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mTask = (Task) getArguments().getSerializable("task");
        mConsult = (Consult) getArguments().getSerializable("consult");
        mSupervise = (Supervise) getArguments().getSerializable("supervise");
        mException = (Exception) getArguments().getSerializable("exception");
        mEvaluate = (ServiceEvaluate) getArguments().getSerializable("evaluate");
        mType = getArguments().getString("type");
        mTaskLayout = (LinearLayout) rootView.findViewById(R.id.apply_info_task_layout);
        mConsultLayout = (LinearLayout) rootView.findViewById(R.id.apply_info_web_layout);
        if(mType.equals(TaskDetailActivity.TYPE_TASK)||mType.equals(TaskDetailActivity.TYPE_EXCEPTION)){
            initTaskView();
        }else if(mType.equals(TaskDetailActivity.TYPE_CONSULT)){
            initConsultView();
        }else if(mType.equals(TaskDetailActivity.TYPE_SUPERVISE)){
            initTaskView();
        }
        requestData();
    }

    private void initTaskView() {
        mConsultLayout.setVisibility(View.GONE);
        mApplyType = (TextView) rootView.findViewById(R.id.apply_type_tv);
        mCertificateType = (TextView) rootView.findViewById(R.id.certificate_type_tv);
        mCertificateNumber = (TextView) rootView.findViewById(R.id.certificatenumber_tv);
        mApplyer = (TextView) rootView.findViewById(R.id.applyer_tv);
        mSex = (TextView) rootView.findViewById(R.id.sex_tv);
        mBirthday = (TextView) rootView.findViewById(R.id.birthday_tv);
        mAddress = (TextView) rootView.findViewById(R.id.address_tv);
        mContentDescribe = (TextView) rootView.findViewById(R.id.content_tv);
    }

    private void initConsultView() {
        mTaskLayout.setVisibility(View.GONE);
        mNameTv = (TextView) rootView.findViewById(R.id.name_tv);
        mMobileTv= (TextView) rootView.findViewById(R.id.mobile_tv);
        mEmailTv = (TextView) rootView.findViewById(R.id.email_tv);
        mAddrTv = (TextView) rootView.findViewById(R.id.addr_tv);
        mTitleTv = (TextView) rootView.findViewById(R.id.title_tv);
        mOrgNameTv = (TextView) rootView.findViewById(R.id.orgname_tv);
        mInteractionTypeTv = (TextView) rootView.findViewById(R.id.interaction_type_tv);
        mContentTv = (TextView) rootView.findViewById(R.id.consult_content_tv);

    }


    private void requestData() {
        Map<String, String> map = new HashMap<>();

        if(mType.equals(TaskDetailActivity.TYPE_TASK)){
            map.put("keyId", mTask.getId());
            map.put("servicePath", "WorkProcessService");
        }else if(mType.equals(TaskDetailActivity.TYPE_CONSULT)){
            map.put("keyId", mConsult.getId());
            map.put("stateCode", "IA-01");
        }else if(mType.equals(TaskDetailActivity.TYPE_SUPERVISE)){
            map.put("keyId", mSupervise.getBizId());
            map.put("servicePath", "WorkProcessService");
        }if(mType.equals(TaskDetailActivity.TYPE_EXCEPTION)){
            map.put("keyId", mException.getExceptionId());
            map.put("servicePath", "WorkProcessService");
        }


        RequestManager.getInstance().postHeader(Config.BASE_URL + Config.URL_SEARCHFRONTSTARTINFO, JSON.toJSONString(map), new RequestManager.RequestListener() {
            @Override
            public void onRequest(String url, int actionId) {

            }

            @Override
            public void onSuccess(String response, String url, int actionId) {

                ApplyInfoResp applyInfoResp = JSON.parseObject(response, ApplyInfoResp.class);
                if (applyInfoResp != null) {
                    if(mType.equals(TaskDetailActivity.TYPE_TASK)||mType.equals(TaskDetailActivity.TYPE_EXCEPTION)){
                        updateTaskView(applyInfoResp);
                    }else if(mType.equals(TaskDetailActivity.TYPE_CONSULT)){
                        updateConsultView(applyInfoResp);
                    }else if(mType.equals(TaskDetailActivity.TYPE_SUPERVISE)){
                        updateTaskView(applyInfoResp);
                    }

                }else{
                    Toast.makeText(getActivity(),"获取数据失败",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String errorMsg, String url, int actionId) {
                Toast.makeText(getActivity(),errorMsg,Toast.LENGTH_SHORT).show();
            }
        }, 0);

    }

    private void updateConsultView(ApplyInfoResp applyInfoResp) {

        WebSiteInterAction webSiteInterAction = applyInfoResp.getDynamicData().getWebSiteInterAction();
        mNameTv.setText(webSiteInterAction.getWebUserName());
        mMobileTv.setText(webSiteInterAction.getPhone());
        mEmailTv.setText(webSiteInterAction.getEmail());
        mAddrTv.setText(webSiteInterAction.getAddress());
        mTitleTv.setText(webSiteInterAction.getTitle());
        mOrgNameTv.setText(webSiteInterAction.getOrgName());
        mInteractionTypeTv.setText(webSiteInterAction.getInteractionType().getTitle());
        mContentTv.setText(webSiteInterAction.getContent());

    }

    private void updateTaskView(ApplyInfoResp applyInfoResp) {
        Work work = applyInfoResp.getDynamicData().getWork();
        mApplyType.setText(work.getApplicantTypeName());
        mCertificateType.setText(work.getApplicantCertTypeName());
        mCertificateNumber.setText(work.getApplicantCertNo());
        mApplyer.setText(work.getApplicantName());
        mSex.setText(TextUtils.equals(work.getGender(),"M")?"男":"女");
        mBirthday.setText(work.getBirthDate());
        mAddress.setText(work.getAddress());
        mContentDescribe.setText(work.getComments());

    }



    public static ApplyInfoFragment newInstance(Object object) {

        Bundle args = new Bundle();
        if(object instanceof Task){
            args.putSerializable("task", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_TASK);
        }else if(object instanceof Consult){
            args.putSerializable("consult", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_CONSULT);
        }else if(object instanceof Supervise){
            args.putSerializable("supervise", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_SUPERVISE);
        }else if(object instanceof Exception){
            args.putSerializable("exception", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_EXCEPTION);
        }else if(object instanceof ServiceEvaluate){
            args.putSerializable("evaluate", (Serializable) object);
            args.putString("type",TaskDetailActivity.TYPE_EVALUATE);
        }
        ApplyInfoFragment fragment = new ApplyInfoFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
