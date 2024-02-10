package com.task_githubapi.mocks;

import com.task_githubapi.dto.BranchDTO;

import static com.task_githubapi.mocks.MockedValues.BRANCH_NAME;
import static com.task_githubapi.mocks.MockedValues.COMMIT_SHA;

public class BranchDTOMock {

    public BranchDTOMock() {
    }

    public static BranchDTO getBranchDTO() {
        return new BranchDTO(BRANCH_NAME, COMMIT_SHA);
    }
}
