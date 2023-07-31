package component;

import io.aelf.portkey.assertion.AssertChecker;
import io.aelf.utils.AElfException;
import org.junit.Test;

import com.google.common.collect.Lists;

import java.util.List;

public class AssertTest {

    @Test
    public void testAssertNotBlank() {
        AssertChecker.assertNotBlank("test");
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotBlankWithException() {
        AssertChecker.assertNotBlank("");
    }

    @Test
    public void testAssertNotNull() {
        AssertChecker.assertNotNull("test");
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotNullWithException() {
        AssertChecker.assertNotNull(null);
    }

    @Test
    public void testAssertNotEquals() {
        AssertChecker.assertNotEquals("test", "test1");
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotEqualsWithException() {
        AssertChecker.assertNotEquals("test", "test");
    }

    @Test
    public void testAssertNotEmptyList() {
        AssertChecker.assertNotEmptyList(List.of(new String[] { "test" }));
    }

    @Test
    public void testAssertNotEmptyList2() {
        AssertChecker.assertNotEmptyList(Lists.asList("test", new String[] {}));
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotEmptyListWithException() {
        AssertChecker.assertNotEmptyList(List.of(new String[] {}));
    }

    @Test
    public void testAssertNotThrow() {
        AssertChecker.assertNotThrow(() -> true);
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotThrowWithException() {
        AssertChecker.assertNotThrow(() -> {
            throw new Exception();
        });
    }

    @Test(expected = RuntimeException.class)
    public void testAssertNotThrowWithException2() {
        AssertChecker.assertNotThrow(() -> {
            throw new Exception();
        }, new AElfException());
    }

}
