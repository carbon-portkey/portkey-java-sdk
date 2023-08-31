package io.aelf.portkey.behaviour;

import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.StringValue;
import io.aelf.utils.ByteArrayHelper;
import org.apache.http.util.TextUtils;
import org.junit.Assert;
import org.junit.Test;

public class ContractContentChecker {
    private final String transactionResult = "0ab1010a220a208b32a14d28d7093b14e40d09eb0065b68a7d0c0462bc19ac88c519beb39884f31207506f72746b65791a4368747470733a2f2f706f72746b65792d6469642e73332e61702d6e6f727468656173742d312e616d617a6f6e6177732e636f6d2f696d672f506f72746b65792e706e672219687474703a2f2f3137322e33312e33322e3230373a383035302a220a20e8068f3e64503c3d41a7a5cd8bd7d9a4e25c0caf7fea1e3699f08579ad626b030ab1010a220a20aa2ff076f87e061785f9c073735c135e081e175331532422650c07e31fd2f1dc12074d696e657276611a4368747470733a2f2f706f72746b65792d6469642e73332e61702d6e6f727468656173742d312e616d617a6f6e6177732e636f6d2f696d672f4d696e657276612e706e672219687474703a2f2f3137322e33312e33322e3230373a383033302a220a200684b7c48f47ce3e513fc2fa48e44cd5582e5a525d9e257fd908c265ff2fac400abb010a220a2066a8d7ff588f802490e31035aec19b44049990e506b4d6604a7bdf8fa4a1ccd6120c446f6b65774361706974616c1a4868747470733a2f2f706f72746b65792d6469642e73332e61702d6e6f727468656173742d312e616d617a6f6e6177732e636f6d2f696d672f446f6b65774361706974616c2e706e672219687474703a2f2f3137322e33312e33322e3230373a383032302a220a20e73d31be0072d6895475a70b5a384d7428e7700ff50274b4d3343faca738824c0aad010a220a20e719fb52490871eb043b2b4cb3c9903ccfa492a49bfbbd3db32c964b0a1d78e0120547617573731a4168747470733a2f2f706f72746b65792d6469642e73332e61702d6e6f727468656173742d312e616d617a6f6e6177732e636f6d2f696d672f47617573732e706e672219687474703a2f2f3137322e33312e33322e3230373a383031302a220a2009de663a130e78cbe47b052ae186ebb0eecca1d7dc858b71702391d3095fedc10abf010a220a20beb7627bd7f51836726cbc191aad3d2d22eeab41d15a4b59b0c237cf6dd93bf3120e43727970746f477561726469616e1a4a68747470733a2f2f706f72746b65792d6469642e73332e61702d6e6f727468656173742d312e616d617a6f6e6177732e636f6d2f696d672f43727970746f477561726469616e2e706e672219687474703a2f2f3137322e33312e33322e3230373a383034302a220a20d211cf723f17396f7cf282dc03a26368e82a026d56989303f35665ecb43c7086";

    @Test
    public void test() throws InvalidProtocolBufferException {
        String convertedResult = StringValue.parseFrom(ByteArrayHelper.hexToByteArray(transactionResult)).getValue();
        Assert.assertFalse(TextUtils.isEmpty(convertedResult));
        System.out.println(convertedResult);
    }
}
