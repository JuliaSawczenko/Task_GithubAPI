package com.task_githubapi.mocks;

import com.task_githubapi.model.BranchModel;
import com.task_githubapi.model.RepositoryModel;

import java.util.ArrayList;
import java.util.List;

import static com.task_githubapi.mocks.MockedValues.REPO_NAME;
import static com.task_githubapi.mocks.MockedValues.USERNAME;

public class RepositoryModelMock {

    public RepositoryModelMock() {
    }

    public static RepositoryModel getRepositoryModel(boolean isFork) {
        List<BranchModel> branches = new ArrayList<>();
        branches.add(BranchModelMock.getBranchModel());

        RepositoryModel repositoryModel = new RepositoryModel();
        repositoryModel.setBranches(branches);
        repositoryModel.setOwner(new RepositoryModel.Owner());
        repositoryModel.getOwner().setLogin(USERNAME);
        repositoryModel.setName(REPO_NAME);
        repositoryModel.setFork(isFork);

        return repositoryModel;
    }

}
