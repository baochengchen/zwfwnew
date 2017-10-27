package com.ztesoft.zwfw.moudle.todo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ztesoft.zwfw.R;
import com.ztesoft.zwfw.base.BaseFragment;
import com.ztesoft.zwfw.domain.ServiceEvaluate;

import java.io.Serializable;

/**
 * Created by BaoChengchen on 2017/10/25 0025.
 */

public class EvaluateDetailFragment extends BaseFragment {

    View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_evaluate_detail, container, false);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        ServiceEvaluate evaluate = (ServiceEvaluate) getArguments().getSerializable("evaluate");

        TextView evaluatorTv = (TextView) rootView.findViewById(R.id.evaluator_tv);
        TextView sourceTv = (TextView) rootView.findViewById(R.id.source_tv);
        TextView timeTv = (TextView) rootView.findViewById(R.id.time_tv);
        TextView contentTv = (TextView) rootView.findViewById(R.id.content_tv);
        TextView opinionTv = (TextView) rootView.findViewById(R.id.opinion_tv);

        evaluatorTv.setText(evaluate.getWebUserName());
        sourceTv.setText(evaluate.getSource().getTitle());
        timeTv.setText(evaluate.getCreateDate());
        contentTv.setText(evaluate.getResult().getTitle());
        opinionTv.setText(evaluate.getOpinion());
    }


    public static EvaluateDetailFragment newInstance(Object object) {

        Bundle args = new Bundle();
        args.putSerializable("evaluate", (Serializable) object);
        EvaluateDetailFragment fragment = new EvaluateDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }
}
