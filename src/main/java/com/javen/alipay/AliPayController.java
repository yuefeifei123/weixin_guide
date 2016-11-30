package com.javen.alipay;

import java.util.Map;

import com.javen.vo.AjaxResult;
import com.jfinal.core.Controller;
import com.jfinal.kit.Prop;
import com.jfinal.kit.PropKit;
import com.jfinal.log.Log;

public class AliPayController extends Controller {
	private static final Prop prop = PropKit.use("alipay.properties");
	private Log log=Log.getLog(AliPayController.class);
	private AjaxResult result = new AjaxResult();
	private boolean isDebug = true;
	
	public void index(){
		
		String orderInfo;
		try {
			
			String body="我是测试数据";
			String passback_params="custom json";
			String subject="1";
			String total_amount="0.01";
			
			String appId;
			String rsa_private;
			if (isDebug) {
				appId=prop.get("test_appId");
				rsa_private=prop.get("test_rsa_private");
System.out.println("test。。。。");
			}else {
				appId=prop.get("appId");
				rsa_private=prop.get("rsa_private");
			}
			
			BizContent content = new BizContent();
			content.setBody(body);
			content.setOut_trade_no(OrderInfoUtil2_0.getOutTradeNo());;
			
			content.setPassback_params(passback_params);
			
			content.setSubject(subject);
			
			content.setTotal_amount(total_amount);
			content.setProduct_code("QUICK_MSECURITY_PAY");
			
			
			Map<String, String> params = OrderInfoUtil2_0.buildOrderParamMap(appId,content);
			String orderParam = OrderInfoUtil2_0.buildOrderParam(params);
			
			String sign = OrderInfoUtil2_0.getSign(params, rsa_private);
			orderInfo = orderParam + "&" + sign;
			log.info("orderInfo>"+orderInfo);
			result.success(orderInfo);
			renderJson(result);
			
		} catch (Exception e) {
			e.printStackTrace();
			result.addError("system error");
		}
	}
	/**
	 * 支付回调通知
	 * https://doc.open.alipay.com/docs/doc.htm?spm=a219a.7629140.0.0.TxBJbS&treeId=193&articleId=105301&docType=1#s3
	 */
	public void pay_notify(){
		String queryString = getRequest().getQueryString();
		log.debug("支付宝回调参数："+queryString);
		
		
		String notify_time = getPara("notify_time");
		String notify_type = getPara("notify_type");
		String notify_id = getPara("notify_id");
		String app_id = getPara("app_id");
		String charset = getPara("charset");
		String version = getPara("version");
		String sign_type = getPara("sign_type");
		String sign = getPara("sign");
		String trade_no = getPara("trade_no");
		String out_trade_no = getPara("out_trade_no");
		
		////////////////一下是可选参数//////////////////////////
		
		String buyer_id = getPara("buyer_id");
		String buyer_logon_id  = getPara("buyer_logon_id");
		String trade_status  = getPara("trade_status");
		String total_amount  = getPara("total_amount");
		String receipt_amount  = getPara("receipt_amount");
		
		
		String passback_params = getPara("passback_params");//附加参数
		
	}
}
