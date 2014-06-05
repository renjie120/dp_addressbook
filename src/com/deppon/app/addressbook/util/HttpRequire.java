package com.deppon.app.addressbook.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import com.alibaba.fastjson.JSON;
import com.deppon.app.addressbook.bean.JpushTokenResult;
import com.deppon.app.addressbook.bean.ServerResult;
import com.deppon.app.addressbook.bean.ServerResults;

public class HttpRequire {
	public static ServerResult getOrgByChildern(int root,String loginUser,String tk) throws Exception { 
		return request(Constant.DPM_HOST
				+ "/dpm/tongxunlu_getChildByOrg.action?id=" + root
				+ "&userId="+loginUser+"&token="+tk, null);
	}

	public static String getBase64(String val) throws NoSuchAlgorithmException {
		if (val == null)
			return null;
		byte[] result = Base64.encodeBase64(val.getBytes());
		return new String(result);
	}

	/**
	 * 按条件搜索.
	 * 
	 * @param isEmp
	 * @param content
	 * @param start
	 * @return
	 * @throws Exception
	 */
	public static ServerResults search(boolean isEmp, String content, int start,String loginUser,String tk)
			throws Exception { 
		return requestArr(
				Constant.DPM_HOST
						+ "/dpm/tongxunlu_search.action?searchName="
						+ content
						+ "&userId="+loginUser+"&token="+tk+"&searchType="
						+ (isEmp ? "1" : "2") + "&start=" + start
						+ "&pageSize=50", null);
	}

	/**
	 * 将手机注册到jpush服务器.
	 * 
	 * @param businoId
	 *            业务号
	 * @return
	 * @throws Exception
	 */
	public static String regiestJpush(String businoId,String loginUser,String tk) throws Exception { 
		JpushTokenResult result = requestToken(
				Constant.DPM_HOST
						+ "/dpm/jpush_setTagAndAlias.action?userId="+loginUser+"&token="+tk
						+ "&SysCode=dpm&deviceType=android&businoId="
						+ businoId, null);
		if (result.getErrorCode() >= 0) {
			return result.getData();
		} else
			return "-1";
	}

	/**
	 * 查询人员详情
	 * 
	 * @param empID
	 * @return
	 * @throws Exception
	 */
	public static ServerResult getEmpDetail(String empID,String loginUser,String tk) throws Exception { 
		return request(Constant.DPM_HOST + "/dpm/tongxunlu_getEmpDetail.action?id="
				+ empID
				+ "&userId="+loginUser+"&token="+tk, null);
	}

	private static JpushTokenResult requestToken(String url, String auth)
			throws Exception {
		// 得到url请求.
		System.out.println("请求的url" + url);

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httpost = new HttpPost(url);
			if (auth != null)
				httpost.addHeader("auth", auth);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), "GBK"));
			String str = br.readLine();
			// 如果没有登录成功，就弹出提示信息.
			JpushTokenResult result = (JpushTokenResult) JSON.parseObject(str,
					JpushTokenResult.class);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭连接.
			httpclient.getConnectionManager().shutdown();
		}
	}
	private static ServerResults requestArr(String url, String auth)
			throws Exception {
		System.out.println("请求的url" + url);
		// 得到url请求.
		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httpost = new HttpPost(url);
			if (auth != null)
				httpost.addHeader("auth", auth);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), "GBK"));
			// 如果没有登录成功，就弹出提示信息.
			ServerResults result = (ServerResults) JSON.parseObject(
					br.readLine(), ServerResults.class);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭连接.
			httpclient.getConnectionManager().shutdown();
		}
	}

	private static ServerResult request(String url, String auth)
			throws Exception {
		// 得到url请求.
		System.out.println("请求的url" + url);

		DefaultHttpClient httpclient = new DefaultHttpClient();
		try {
			HttpPost httpost = new HttpPost(url);
			if (auth != null)
				httpost.addHeader("auth", auth);
			HttpResponse response = httpclient.execute(httpost);
			HttpEntity entity = response.getEntity();
			BufferedReader br = new BufferedReader(new InputStreamReader(
					entity.getContent(), "GBK"));
			String str = br.readLine();
			// 如果没有登录成功，就弹出提示信息.
			ServerResult result = (ServerResult) JSON.parseObject(str,
					ServerResult.class);
			return result;
		} catch (Exception e) {
			throw e;
		} finally {
			// 关闭连接.
			httpclient.getConnectionManager().shutdown();
		}
	}
  

	public static String encryptBASE64(String key) throws Exception {
		// String base64String = "whuang123";
		byte[] result = Base64.encodeBase64(key.getBytes());
		return new String(result);
	}

	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5',
			'6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static String toHexString(byte[] b) { // String to byte
		StringBuilder sb = new StringBuilder(b.length * 2);
		for (int i = 0; i < b.length; i++) {
			sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
			sb.append(HEX_DIGITS[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	public static String getMD5(String s) {
		try {
			// Create MD5 Hash
			MessageDigest digest = java.security.MessageDigest
					.getInstance("MD5");
			digest.update(s.getBytes());
			byte messageDigest[] = digest.digest();

			return toHexString(messageDigest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return "";
	}
}
