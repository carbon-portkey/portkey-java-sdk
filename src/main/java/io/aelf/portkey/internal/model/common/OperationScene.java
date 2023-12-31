package io.aelf.portkey.internal.model.common;

public interface OperationScene {
    // unknown
    int unknown = 0;
    // register
    int register = 1;
    // account recovery
    int communityRecovery = 2;
    // add guardian
    int addGuardian = 3;
    // delete guardian
    int deleteGuardian = 4;
    // edit guardian
    int editGuardian = 5;
    // remove other managers
    int removeOtherManager = 6;
    // set the login account
    int setLoginAccount = 7;
}
