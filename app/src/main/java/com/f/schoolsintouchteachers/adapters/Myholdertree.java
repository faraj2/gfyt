package com.f.schoolsintouchteachers.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.f.schoolsintouchteachers.R;
import com.unnamed.b.atv.model.TreeNode;

import java.util.ArrayList;

/**
 * Created by admin on 3/6/2018.
 */

public class Myholdertree extends  TreeNode.BaseNodeViewHolder<IconTreeItem> {
    Context context;
    private CheckBox nodeSelector;

    public Myholdertree(Context context) {
        super(context);
        this.context=context;

    }

    @Override
    public View createNodeView(final TreeNode node, com.f.schoolsintouchteachers.adapters.IconTreeItem value) {
        View view=LayoutInflater.from(context).inflate(R.layout.test,null,false);
        TextView mytextt=view.findViewById(R.id.textx);
        mytextt.setText(value.mytext);
        nodeSelector = (CheckBox) view.findViewById(R.id.node_selector);
        nodeSelector.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setSelected(isChecked);
                for (TreeNode n : node.getChildren()) {
                    getTreeView().selectNode(n, isChecked);
                }
            }
        });
        nodeSelector.setChecked(node.isSelected());

        if (node.isLastChild()) {
            view.findViewById(R.id.bot_line).setVisibility(View.INVISIBLE);

        }

        return view;
    }


//    @Override
//    public View createNodeView(TreeNode node, IconTreeItem value) {
//        View view=LayoutInflater.from(context).inflate(R.layout.newlayout,null,false);
//        TextView mytextt=view.findViewById(R.id.textx);
//        mytextt.setText(value.mytext);
//
//        return view;
//    }


    @Override
    public void toggleSelectionMode(boolean editModeEnabled) {
        super.toggleSelectionMode(editModeEnabled);
nodeSelector.setChecked(mNode.isSelected());

    }
}
