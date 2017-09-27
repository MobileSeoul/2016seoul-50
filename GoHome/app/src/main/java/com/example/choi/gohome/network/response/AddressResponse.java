package com.example.choi.gohome.network.response;

/**
 * Created by choi on 2016-08-30.
 */
public class AddressResponse {
    private String router;

    public AddressResponse(String router) {
        this.router = router;
    }

    public String getRouter() {
        return router;
    }

    public void setRouter(String router) {
        this.router = router;
    }
}
