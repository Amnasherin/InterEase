package com.example.InterEaseApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

public class FAQAdapter extends BaseExpandableListAdapter {

    private Context context;
    private List<String> faqList;
    private HashMap<String, List<String>> answerList;

    public FAQAdapter(Context context, List<String> faqList, HashMap<String, List<String>> answerList) {
        this.context = context;
        this.faqList = faqList;
        this.answerList = answerList;
    }

    @Override
    public int getGroupCount() {
        return faqList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return answerList.get(faqList.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return faqList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return answerList.get(faqList.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String faqQuestion = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_group, null);
        }
        TextView faqQuestionTextView = convertView.findViewById(R.id.faq_question);
        faqQuestionTextView.setText(faqQuestion);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String faqAnswer = (String) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.list_item, null);
        }
        TextView faqAnswerTextView = convertView.findViewById(R.id.faq_answer);
        faqAnswerTextView.setText(faqAnswer);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
