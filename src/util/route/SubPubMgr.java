package util.route;

import util.Call;

/**
 * 发布订阅控制器 
 *
 */
public class SubPubMgr implements Call{

	private SubPubMgr() {}
 

	public static <T> SubPub<T> getSubPub(Integer threadCoreSize) {
		SubPub<T> subPub =  new SubPubMapImpl<>();
		subPub.init(threadCoreSize);
		return subPub;
	}
	
	
	public void call(){
		
	}

	


}
