package io.aelf.portkey.behaviour;

import io.aelf.portkey.behaviour.guardian.GuardianBehaviourEntity;
import io.aelf.portkey.internal.model.common.OperationScene;
import io.aelf.portkey.internal.model.guardian.GuardianDTO;
import io.aelf.portkey.utils.log.GLogger;
import io.aelf.utils.AElfException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GuardianTest {
    private GuardianBehaviourEntity guardianBehaviourEntity;

    @Before
    public void setUp() {
        guardianBehaviourEntity = new GuardianBehaviourEntity(new GuardianDTO(), OperationScene.register, (info, guardian) -> {
        });
    }

    @Test
    public void notFinishedTest() {
        Assert.assertFalse(guardianBehaviourEntity.isVerified());
    }

    @Test(expected = AElfException.class)
    public void callFinishWhenNotVerifiedTest() {
        Assert.assertFalse(guardianBehaviourEntity.isVerified());
        try {
            guardianBehaviourEntity.next();
        } catch (AElfException e) {
            GLogger.e("assert exception:", e);
            throw e;
        }
    }
}
