package chenls.orderdishes;

import android.app.Application;
import android.test.ApplicationTestCase;
import android.widget.Toast;

/**
 * <a href="http://d.android.com/tools/testing/testing_android.html">Testing Fundamentals</a>
 */
public class ApplicationTest extends ApplicationTestCase<Application> {
    public ApplicationTest() {
        super(Application.class);
    }

    /**
     * 初始设置
     *
     * @throws Exception
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * 垃圾清理与资源回收
     *
     * @throws Exception
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void test1() {
        System.out.println("陈六生...");
        Toast.makeText(getContext(), "陈六生————", Toast.LENGTH_SHORT).show();
    }
}