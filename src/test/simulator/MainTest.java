package test.simulator;

import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import simulator.*;

import java.util.Objects;

import static org.junit.Assert.*;

/* Main Tester.
        *
        * @author <Authors name>
* @since <pre>mars 5, 2022</pre>
        * @version 1.0
        */
public class MainTest {


    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

/*
* Method: init()
*
*/
@Test
public void testInit() throws Exception {
//TODO: Test goes here...
        instance.init();
        assertFalse(instance.frame == null);
        Simulator sim = new Simulator(instance);
        instance.simulator = sim;

    }

/*
        * Method: start()
*
        */
    @Test
    public void testStart() throws Exception {
//TODO: Test goes here...
    }

/**
 * @author Babacar
* Method: main(String[] args)
*
*/
@Test
public void testMain() throws Exception {
//TODO: Test goes here...
        testInit();
        System.out.println("main");
        String[] args = null;

        Code code = new Code(2);
        code.add("Level sword 0");
        code.add("add Level, 5, 6");
        Simulator.code = code;
        RunNBC run = new RunNBC(instance);
        Simulator.run = run;
        run.executeLine("Level sword 0",1);
        assertTrue(run.v.has("Level"));

        run.executeLine("add Level, 5, 6",2);
        String result = Variable.getValue("Level").replace(")","");
        assertEquals(11, Integer.parseInt(result));

        Main.main(args);



    }
@Test
public void testMain2() throws Exception {
//TODO: Test goes here...
        testInit();
        System.out.println("main");
        String[] args = null;

        Code code = new Code(2);
        code.add("Level sword 0");
        code.add("sub Level, 10, 5");
        Simulator.code = code;
        RunNBC run = new RunNBC(instance);
        Simulator.run = run;
        run.executeLine("Level sword 0",1);
        assertTrue(run.v.has("Level"));

        run.executeLine("add Level, 10, 5",2);
        String result = Variable.getValue("Level").replace(")","");
        assertEquals(5, Integer.parseInt(result));

        Main.main(args);
    }
    @Test
    public void testMain3() throws Exception {
//TODO: Test goes here...
        testInit();
        System.out.println("main");
        String[] args = null;

        Code code = new Code(2);
        code.add("Level sword 0");
        code.add("div Level, 10, 5");
        Simulator.code = code;
        RunNBC run = new RunNBC(instance);
        Simulator.run = run;
        run.executeLine("Level sword 0",1);
        assertTrue(run.v.has("Level"));

        run.executeLine("add Level, 10, 5",2);
        String result = Variable.getValue("Level").replace(")","");
        assertEquals(2, Integer.parseInt(result));

        Main.main(args);
    }


    Main instance = new Main();

}

