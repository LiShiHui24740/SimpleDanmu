package com.airland.simpledanmuku.judge;

import android.util.SparseArray;


public class DefautJudgeManager implements IStateJudge {
    private int rowCount;
    private SparseArray<Boolean> sparseArray;

    public DefautJudgeManager(int rowCount) {
        this.rowCount = rowCount;
        sparseArray = new SparseArray<>();
        resetState(true);
    }

    @Override
    public void resetState(boolean initState) {
        sparseArray.clear();
        for (int i = 1; i <= rowCount; i++) {
            sparseArray.put(i, initState);
        }
    }

    @Override
    public boolean getIndicator(int row) {
        if (row <= rowCount) {
            return sparseArray.get(row);
        } else {
            throw new IllegalArgumentException("row > rowCount?");
        }
    }

    @Override
    public void setIndicator(int row, boolean state) {
        if (row <= rowCount) {
            sparseArray.put(row, state);
        } else {
            throw new IllegalArgumentException("row > rowCount?");
        }
    }
}
