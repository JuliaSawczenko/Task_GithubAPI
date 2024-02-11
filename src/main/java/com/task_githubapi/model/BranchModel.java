package com.task_githubapi.model;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BranchModel {
    private String name;
    private Commit commit;

    @Data
    public static class Commit {
        private String sha;
    }
}
