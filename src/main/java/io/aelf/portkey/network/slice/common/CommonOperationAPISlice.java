package io.aelf.portkey.network.slice.common;

import io.aelf.portkey.internal.model.common.ChainInfoDTO;
import io.aelf.utils.AElfException;

public interface CommonOperationAPISlice {
    ChainInfoDTO getGlobalChainInfo() throws AElfException;
}
