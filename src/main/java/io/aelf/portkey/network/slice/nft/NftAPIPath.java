package io.aelf.portkey.network.slice.nft;

/**
 * Those APIs are not implemented yet, so they are not included in the interface.
 */
public interface NftAPIPath {
    String FETCH_ACCOUNT_TOKEN_LIST = "/api/app/user/assets/token";
    String GET_SYMBOL_IMAGE = "/api/app/user/assets/symbolImages";
    String FETCH_ACCOUNT_NFT_COLLECTION_LIST = "/api/app/user/assets/nftCollections";
    String FETCH_ACCOUNT_NFT_ITEM_LIST = "/api/app/user/assets/nftItems";
    String FETCH_TOKEN_PRICE = "/api/app/tokens/prices";
    String GET_USER_TOKEN_LIST = "/api/app/search/usertokenindex";
}
