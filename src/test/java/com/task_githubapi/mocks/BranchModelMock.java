package com.task_githubapi.mocks;

import com.task_githubapi.model.BranchModel;

import static com.task_githubapi.mocks.MockedValues.BRANCH_NAME;
import static com.task_githubapi.mocks.MockedValues.COMMIT_SHA;

public class BranchModelMock {

    public BranchModelMock() {
    }
    public static BranchModel getBranchModel() {
        BranchModel branchModel = new BranchModel();
        branchModel.setName(BRANCH_NAME);

        BranchModel.Commit commit = new BranchModel.Commit();
        commit.setSha(COMMIT_SHA);
        branchModel.setCommit(commit);

        return branchModel;
    }
}
