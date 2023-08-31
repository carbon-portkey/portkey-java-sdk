package io.aelf.portkey.behaviour;

import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.behaviour.guardian.state.VerifiedGuardianState;
import io.aelf.portkey.internal.model.common.AccountOriginalType;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.internal.model.verify.HeadVerifyCodeResultDTO;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuardianTest {
    private GuardianBehaviourEntity guardianBehaviourEntity;

    @Before
    public void setUp() {
        guardianBehaviourEntity = new GuardianBehaviourEntity(
                new GuardianDTO(),
                OperationScene.register,
                guardianWrapper -> {
                },
                AccountOriginalType.Email,
                false
        );
    }

    @Test
    public void notFinishedTest() {
        Assert.assertFalse(guardianBehaviourEntity.isVerified());
    }

    @Test
    public void callFinishTest() {
        Assert.assertFalse(guardianBehaviourEntity.isVerified());
        guardianBehaviourEntity.setNextState(new VerifiedGuardianState(guardianBehaviourEntity, new HeadVerifyCodeResultDTO()));
        assert guardianBehaviourEntity.isVerified();
    }
}
