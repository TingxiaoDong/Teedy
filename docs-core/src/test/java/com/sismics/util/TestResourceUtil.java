package com.sismics.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

/**
 * Test of the resource utils.
 *
 * @author jtremeaux
 */
public class TestResourceUtil {

    @Test
    public void listFilesTest() throws Exception {
        List<String> fileList = ResourceUtil.list(Test.class, "/junit/framework");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "/junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "junit/framework/");
        Assert.assertTrue(fileList.contains("Test.class"));

        fileList = ResourceUtil.list(Test.class, "");
        Assert.assertEquals(0, fileList.size());

        fileList = ResourceUtil.list(Test.class, "/nonexistent/path");
        Assert.assertEquals(0, fileList.size());
    }
    // 测试ResourceUtil.list方法传入非法参数时是否抛出异常
    @Test(expected = IllegalArgumentException.class)
    public void listFilesWithIllegalArgumentTest() throws Exception {
        ResourceUtil.list(null, "/junit/framework");
    }

    // 假设ResourceUtil有构造函数，测试构造函数
    @Test
    public void resourceUtilConstructorTest() {
        ResourceUtil resourceUtil = new ResourceUtil();
        Assert.assertNotNull(resourceUtil);
    }
}
