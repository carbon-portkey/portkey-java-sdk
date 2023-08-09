package io.aelf.portkey.network.slice.common;

import io.aelf.portkey.internal.model.common.ChainInfoDTO;
import io.aelf.schemas.ChainstatusDto;
import io.aelf.utils.AElfException;

import java.io.IOException;

public interface CommonOperationAPISlice {
    ChainInfoDTO getGlobalChainInfo() throws AElfException;

    ChainstatusDto getParticularChainInfo() throws IOException;
}
