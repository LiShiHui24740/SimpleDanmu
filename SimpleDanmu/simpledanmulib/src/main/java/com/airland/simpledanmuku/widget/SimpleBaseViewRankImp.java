package com.airland.simpledanmuku.widget;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public final class SimpleBaseViewRankImp implements ISimpleBaseViewRank<SimpleItemBaseView> {
    private int rowCount;
    private SparseArray<LinkedList<SimpleItemBaseView>> simpleBaseViewSparseArray;
    private Random random = new Random();

    SimpleBaseViewRankImp(int rowCount) {
        this.rowCount = rowCount;
        if (this.rowCount <= 0) {
            this.rowCount = 1;
        }
        simpleBaseViewSparseArray = new SparseArray<>();
        for (int i = 1; i <= rowCount; i++) {
            simpleBaseViewSparseArray.put(i, new LinkedList<SimpleItemBaseView>());
        }
    }


    @Override
    public void insertRowItem(SimpleItemBaseView simpleItemBaseView) {
        if (simpleBaseViewSparseArray.get(simpleItemBaseView.rowNumber) != null) {
            simpleBaseViewSparseArray.get(simpleItemBaseView.rowNumber).add(simpleItemBaseView);
        }
    }

    @Override
    public boolean removeRowItem(SimpleItemBaseView simpleItemBaseView) {
        if (simpleBaseViewSparseArray.get(simpleItemBaseView.rowNumber) != null && simpleBaseViewSparseArray.get(simpleItemBaseView.rowNumber).size() > 0) {
            return simpleBaseViewSparseArray.get(simpleItemBaseView.rowNumber).remove(simpleItemBaseView);
        }
        return false;
    }

    /**
     * 计算弹幕应该处在第几行（哪一行弹幕少，分配给哪一行，最少弹幕行数如果有多行相同，则随机分配）
     *
     * @return 返回行号
     */
    @Override
    public int caculateRow() {
        if (rowCount == 1) {
            return 1;
        } else {
            int minSize = simpleBaseViewSparseArray.get(1).size();
            List<Integer> integerArrayList = new ArrayList<>();
            for (int i = 1; i <= rowCount; i++) {
                int size = simpleBaseViewSparseArray.get(i).size();
                if (size < minSize) {
                    minSize = size;
                    integerArrayList.clear();
                    integerArrayList.add(i);
                } else if (size == minSize) {
                    integerArrayList.add(i);
                }
            }
            if (integerArrayList.size() == 1) {
                return integerArrayList.get(0);
            } else {
                return integerArrayList.get(random.nextInt(integerArrayList.size()));
            }
        }

    }
}
