package io.aelf.portkey.behaviour.login;

import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.behaviour.guardian.state.VerifiedGuardianState;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.guardian.GuardianWrapper;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginEntityTest {

    @Test
    public void guardianVerifyLimitTest1() {
        List<GuardianWrapper> guardians = new ArrayList<>();
        GuardianWrapper stub = new GuardianWrapper(new GuardianDTO());
        guardians.add(stub);
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 1;
        LoginBehaviourEntity loginStub = new LoginBehaviourEntity(guardians);
        stub.setVerifiedData(new HeadVerifyCodeResultDTO());
        loginStub.informGuardianReady(stub);
        assert loginStub.isFulfilled();
    }

    @Test
    public void guardianVerifyLimitTest2() {
        List<GuardianWrapper> guardians = new ArrayList<>();
        GuardianWrapper stub1 = new GuardianWrapper(new GuardianDTO()), stub2 = new GuardianWrapper(new GuardianDTO());
        guardians.add(stub1);
        guardians.add(stub2);
        // 2 * 0.6 + 1 = 1.2 + 1 = 2.2 -> 2
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 2;
        LoginBehaviourEntity loginStub = new LoginBehaviourEntity(guardians);
        stub1.setVerifiedData(new HeadVerifyCodeResultDTO());
        loginStub.informGuardianReady(stub1);
        assert !loginStub.isFulfilled();
        stub2.setVerifiedData(new HeadVerifyCodeResultDTO());
        loginStub.informGuardianReady(stub2);
        assert loginStub.isFulfilled();
    }

    @Test
    public void guardianVerifyLimitTest3() {
        List<GuardianWrapper> guardians = new ArrayList<>();
        int len = 10;
        for (int i = 0; i < len; i++) {
            guardians.add(new GuardianWrapper(new GuardianDTO()));
        }
        // 10 * 0.6 + 1 = 6 + 1 = 7 -> 7
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 7;
        guardians.add(new GuardianWrapper(new GuardianDTO()));
        // 11 * 0.6 + 1 = 6.6 + 1 = 7.6 -> 7
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 7;
        guardians.add(new GuardianWrapper(new GuardianDTO()));
        // 12 * 0.6 + 1 = 7.2 + 1 = 8.2 -> 8
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 8;
    }

    @Test
    public void callbackTest() {
        List<GuardianWrapper> guardians = new ArrayList<>();
        GuardianWrapper stub = new GuardianWrapper(new GuardianDTO());
        guardians.add(stub);
        assert LoginBehaviourEntity.getGuardianVerifyLimit(guardians) == 1;
        LoginBehaviourEntity entity = new LoginBehaviourEntity(guardians);
        Optional<GuardianBehaviourEntity> guardian = entity.nextWaitingGuardian();
        assert guardian.isPresent();
        guardian.get().setNextState(new VerifiedGuardianState(guardian.get(), new HeadVerifyCodeResultDTO()));
        assert guardian.get().isVerified();
        assert entity.isFulfilled();
    }

}
