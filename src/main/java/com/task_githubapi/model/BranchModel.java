package com.task_githubapi.model;

import jakarta.websocket.OnOpen;
import lombok.*;

@NoArgsConstructor
@Data
public class BranchModel {
    private String name;
    private Commit commit;

    @Data
    public static class Commit {
        private String sha;
    }
}
