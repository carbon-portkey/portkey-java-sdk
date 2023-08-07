package io.aelf.portkey.network.connecter;

import io.aelf.portkey.network.slice.account.AccountOperationAPISlice;
import io.aelf.portkey.network.slice.common.GoogleOperationAPISlice;

/**
 * This interface is just a representation of all the API interfaces.
 * <p>
 * See those interfaces that this class has extended for more details.
 */
public interface GlobalOperationInterface extends AccountOperationAPISlice,
        GoogleOperationAPISlice {
}
