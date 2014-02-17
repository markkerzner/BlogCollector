/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.shmsoft.blogcollector.selenium;

import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author mark
 */
public class TiCollectorTest {
    
    public TiCollectorTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of run method, of class TiCollector.
     */
    @Test
    public void testRun() {
        System.out.println("run");
        TiCollector instance = new TiCollector();
        instance.run();
    }

    /**
     * Test of findPosts method, of class TiCollector.
     */
    @Test
    public void testFindPosts() {
        System.out.println("findPosts");
        String html = "";
        TiCollector instance = new TiCollector();
        List expResult = null;
        List result = instance.findPosts(html);
        assertEquals(expResult, result);
    }

}