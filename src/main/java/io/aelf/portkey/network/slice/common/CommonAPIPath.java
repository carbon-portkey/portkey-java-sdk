package io.aelf.portkey.network.slice.common;

public interface CommonAPIPath {
    String FETCH_TRANSACTION_FEE = "/api/app/account/transactionFee";
    /**
     * Need to remember it's not Portkey's API.
     * <p>
     * Its base host is <a href="https://www.googleapis.com">https://www.googleapis.com</a>
     */
    String GET_GOOGLE_ACCESS_TOKEN = "/userinfo/v2/me";
}
