package com.example.choi.gohome.network.response;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by choi on 2016-10-11.
 */
public class WardListResponse {
    private boolean result;
    private List<Users> users = new ArrayList<>();

    public WardListResponse(boolean result, List<Users> users) {
        this.result = result;
        this.users = users;
    }

    public boolean isResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }

    public List<Users> getUsers() {
        return users;
    }

    public void setUsers(List<Users> users) {
        this.users = users;
    }
}
