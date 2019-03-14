package util.route;

import util.Call;

/**
 * 发布订阅控制器 
 *
 */
public class SubPubMgr implements Call{

	private SubPubMgr() {}
 

	public static SubPub<String> getSubPub(Integer threadCoreSize) {
		SubPub<String> subPub =  new SubPubMapImpl<>();
		subPub.init(threadCoreSize);
		return subPub;
	}
	
	
	public void call(){
		
	}

	


}
