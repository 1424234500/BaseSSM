package com.walker.core.service.rmi;

import java.rmi.Naming;

import org.junit.Test;

import com.walker.common.util.Tools;
import com.walker.core.cache.CacheMgr;
import com.walker.core.service.service.ServiceClass;


/**
 * Consumer
 *
 */
public class Consumer {
	
	@Test
	public void test(){
		int port = CacheMgr.getInstance().get("port_rmi", 8091);

        try {
        	ServiceClass hello = (ServiceClass) Naming.lookup("rmi://localhost:"+ port + "/ServiceClass");
            Tools.out(hello.test("hello rmi"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}