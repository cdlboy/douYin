package com.douyin.AppInterface;

import com.douyin.httpTool.MyHttp;

import org.json.JSONException;

import java.io.IOException;
import java.util.Map;

public class AppInterface {
	private static String urlHeadr = "http://bysj.douyin.xyz/index.php/Admin/";

	/**
	 * 该方法用于用户登录
	 * @param username 用户名
	 * @param passwd	密码
	 * @return	执行失败时：{"error":x,"msg":x} error： -1->用户信息错误 <BR/>
	 * 执行成功时：{"error":0,"id":x,"username":x,"password":x,"tele":x,"device_id":x,"addtime":x,"user_pic":x,"cookie":cookie}<BR/>
	 * 索引对应的内容：<BR/>
	 * id-用户的id,username-用户名,password-密码,tele-手机号码,device_id-设备号,addtime-注册时间,user_pic-头像地址,cookie-产生的cookie
	 * @throws IOException   IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String login(String username,String passwd) throws IOException, JSONException {
		String rsl = "";
		String url_post = urlHeadr+"AppUser/login";
		String str_param_body = "username="+username+"&password="+passwd;
		rsl = MyHttp.Post(url_post, str_param_body, "utf-8", false, "");
		return rsl;
	}



	/**
	 * 该方法用于用户注册
	 * @param username 用户名（必须）
	 * @param password 密码（必须）
	 * @param tele 手机号码（必须）
	 * @param device_id 设备号（必须）
	 * @return 暂无说明
	 * @throws IOException IO流异常
	 * @throws JSONException 非josn格式
	 */
	public static String register(String username,String password,String tele,String device_id) throws IOException, JSONException {
		String data = "username="+username+"&password="+password+"&tele="+tele+"&device_id="+device_id;
		return MyHttp.Post(urlHeadr+"AppUser/register",data, "utf-8",false, "");
	}

	/**
	 * 该方法用于获取用户的信息
	 * @param cookie 必须提交的cookie
	 * @return 执行失败：{"error":xx,"msg":xx}error: -1-cookie已过期 ,-101-系统错误<BR/>
	 * 执行成功：{"error":0,"id":x,"username":x,"password":x,"tele":x,"device_id":x,"addtime":x,"user_pic":x}<BR/>
	 * 索引对应的内容：<BR/>
	 * 	id-用户的id,username-用户名,password-密码,tele-手机号码,device_id-设备号,addtime-注册时间,user_pic-头像地址
	 * @throws IOException	 IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String getUserInfo(String cookie) throws IOException, JSONException {
		return MyHttp.Post(urlHeadr+"AppUser/getUserInfo","", "utf-8",false, cookie);
	}

	/**
	 * 该方法用于获取设备的信息
	 * @param cookie 必须提交的cookie
	 * @return 执行失败：{"error":xx,"msg":xx} error: -1-cookie已过期, -101-系统错误<BR/>
	 * 执行成功：{"error":0,"id":x,"device_id":x,"addtime":x,"status":x}<BR/>
	 * 索引对应的内容：<BR/>
	 * id-设备id,device_id-设备号,addtime-出厂时间,status-激活状态
	 * @throws IOException  IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String getDeviceInfo(String cookie) throws IOException, JSONException {
		return MyHttp.Post(urlHeadr+"AppDevice/getDeviceInfo","", "utf-8",false, cookie);
	}

	/**
	 * 该方法用于设置温度
	 * @param weather 温度开关量 1-开,0-关
	 * @param minWeather 下限温度
	 * @param maxWeather  上限温度
	 * @param cookie 必须提交的cookie
	 * @return 执行失败：{"error":xx,"msg":xx}error: -1-cookie已过期,-2-改方式不是POST,-101-系统错误,0-温度设定成功
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String setWeather(int weather,int minWeather,int maxWeather,String cookie) throws IOException, JSONException {
		String data = "weather="+weather+"&minWeather="+minWeather+"&maxWeather="+maxWeather;
		return MyHttp.Post(urlHeadr+"AppDevice/setWeather",data, "utf-8",false, cookie);
	}

	/**
	 * 该方法用于设置氧气的开关
	 * @param oxygen 打氧开关量 1-开,0-关
	 * @param oxygenTime 打氧时长 (分钟)
	 * @param cookie 必须提交的cookie
	 * @return {"error":xx,"msg":xx}error: -1-cookie已过期,-2-改方式不是POST,-101-系统错误,0-打氧设定成功
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String oxygenOn(int oxygen,int oxygenTime,String cookie) throws IOException, JSONException {
		String data = "oxygen="+oxygen+"&oxygenTime="+oxygenTime;
		return MyHttp.Post(urlHeadr+"AppDevice/oxygenOn",data, "utf-8",false, cookie);
	}

	/**
	 * 该方法用于设置滤水的开关
	 * @param flashWater 滤水该开关量 1-开,0-关
	 * @param flashTime  滤水时长 （分钟）
	 * @param cookie     必须提交的cookie
	 * @return {"error":xx,"msg":xx} error: -1-cookie已过期,-2-改方式不是POST,-101-系统错误,0-滤水设定成功
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String flashWaterOn(int flashWater,int flashTime,String cookie) throws IOException, JSONException {
		String data = "flashWater="+flashWater+"&flashTime="+flashTime;
		return MyHttp.Post(urlHeadr+"AppDevice/flashWaterOn",data, "utf-8",false, cookie);
	}

	/**
	 * 该方法用于喂食的设置
	 * @param feed 投喂量 -1-少量,0-中量,1-大量
	 * @param cookie 必须提交的cookie
	 * @return {"error":xx,"msg":xx} error: -1-cookie过期,-2-该方式不是POST,-101-系统错误,0-投喂成功
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String feed(int feed,String cookie) throws IOException, JSONException {
		String data = "feed="+feed;
		return MyHttp.Post(urlHeadr+"AppDevice/feed",data, "utf-8",false, cookie);
	}

	/**
	 * 该方法用于找回密码第一步的用户信息确认
	 * @param username(用户名)
	 * @param verify(验证码)
	 * @return	{"error":xxx,"msg":"xxx",cookie:cookie}error: 101-验证码错误,102-用户名不能为空,103-用户名不存在,0-验证正确
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String check(String username,String verify) throws IOException, JSONException {
		String data = "username="+username+"&verify="+verify;
		return MyHttp.Post(urlHeadr+"AppUser/check",data, "utf-8",false, "");
	}

	public static String getActionInfo(String cookie) throws IOException, JSONException {
		return MyHttp.Post(urlHeadr+"AppDevice/getActionInfo","", "utf-8",false, cookie);
	}

	public static String getLastTemp(String cookie) throws IOException, JSONException {
		return MyHttp.Post(urlHeadr+"AppDevice/showLastTemp","", "utf-8",false, cookie);
	}

	/**
	 * 该方法用于设定托管
	 * @param cookie(登录获得的cookie)
	 * @param map (map变量，存放的是各个参数)
	 * map需存取的键(字段):
	 * tg_oxygen，tg_oxygenSettime，tg_oxygenTime 打氧选项的三个键
	 * tg_flash，tg_flashSettime，tg_flashTime 滤水选项的三个键
	 * tg_feed，tg_feedCount，tg_feedSettime 喂养选项的三个键
	 * @return json字符串
	 * {"error":0}为成功 {"error":-101}系统错误 {"error":-1}cookie无效
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String setTg(String cookie,Map<String,String> map) throws IOException, JSONException {
		String tg_oxygen = (map.get("tg_oxygen")!=null) ? map.get("tg_oxygen") : "0";
		String tg_oxygenSettime = (map.get("tg_oxygenSettime")!=null) ? map.get("tg_oxygenSettime") : "0";
		String tg_oxygenTime = (map.get("tg_oxygenTime")!=null) ? map.get("tg_oxygenTime") : "0";
		String tg_flash = (map.get("tg_flash")!=null) ? map.get("tg_flash") : "0";
		String tg_flashSettime = (map.get("tg_flashSettime")!=null) ? map.get("tg_flashSettime") : "0";
		String tg_flashTime = (map.get("tg_flashTime")!=null) ? map.get("tg_flashTime") : "0";
		String tg_feed = (map.get("tg_feed")!=null) ? map.get("tg_feed") : "0";
		String tg_feedCount = (map.get("tg_feedCount")!=null) ? map.get("tg_feedCount") : "0";
		String tg_feedSettime = (map.get("tg_feedSettime")!=null) ? map.get("tg_feedSettime") : "0";
		String data = "tg_oxygen="+tg_oxygen+"&tg_oxygenSettime="+tg_oxygenSettime;
		data = data +"&tg_oxygenTime="+tg_oxygenTime+"&tg_flash="+tg_flash;
		data = data +"&tg_flashSettime="+tg_flashSettime+"&tg_flashTime="+tg_flashTime;
		data = data +"&tg_feed="+tg_feed+"&tg_feedCount="+tg_feedCount;
		data = data +"&tg_feedSettime="+tg_feedSettime;

		return MyHttp.Post(urlHeadr+"AppDevice/setTg",data, "utf-8",false, cookie);
	}

	/**
	 * @param cookie
	 * @return json字符串
	 * {"feed":{"feed_next":"7分54秒","isFeed":"0","tg_feed":"1"},"error":0,"oxygen":{"isOxygen":"0","oxygen_next":"29分54秒","tg_oxygen":"1"},"flash":{"tg_flash":"1","flash_next":"0分54秒","isFlash":"0"}}
	 * isFeed 只有一个状态需要判断，isFeed为0则说明喂食时间未到，feed_next":"9分9秒" 表明距离下一次自动喂食为9分9秒
	 * oxygen跟flash一样，我就讲一个
	 * 先判断isOxygen 如果为1说明已经在打氧，然后oxygen这个json的子集 会有oxygen_surplus这个键，对应的内容为打氧的剩余时间
	 * 如果isOxygen为0 说明还未打氧，"oxygen_next":"29分6秒" 表示距离下一次打氧为"29分6秒"
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getTgInfo(String cookie) throws IOException, JSONException {

		return MyHttp.Post(urlHeadr+"AppDevice/getTgInfo","", "utf-8",false, cookie);
	}

	/**
	 * 该方法用于找回密码第一步的用户信息确认
	 * @param username(用户名)
	 * @return	{"error":xxx,"msg":"xxx",cookie:cookie，tele:用户手机}error: 106-非post,100-用户名不能为空,101-用户名不存在,0-验证正确
	 * @throws IOException IO流异常
	 * @throws JSONException 非json格式
	 */
	public static String checkUser(String username) throws IOException, JSONException {

		return MyHttp.Post(urlHeadr+"AppUser/check","username="+username, "utf-8",false, "");
	}

	/**
	 * 该方法用于找回密码第二步的获取短信验证码
	 * @param checkCookie 第一步获取到的cookie
	 * @return	error 102-cookie无效（身份无效） 105-系统错误  0-成功获取验证码
	 * @throws IOException
	 * @throws JSONException
	 */
	public static String getPhoneVerify(String checkCookie) throws IOException, JSONException {

		return MyHttp.Post(urlHeadr+"AppUser/getPhoneVerify","", "utf-8",false, checkCookie);
	}

	/**
	 * 该方法用于找回密码第二步的验证短信验证码
	 * @param checkCookie 第一步获取到的cookie
	 * @param code	短信验证码
	 * @return	error 102-cookie无效（身份无效） 101-验证码错误  106-非post  0-验证码正确
	 * @throws IOException
	 * @throws JSONException
	 */

	public static String checkTeleCode(String checkCookie,String code) throws IOException, JSONException {

		return MyHttp.Post(urlHeadr+"AppUser/checkTeleCode","teleCode="+code, "utf-8",false, checkCookie);
	}

	/**
	 * 该方法用于找回密码第三步的键入新密码
	 * @param checkCookie 第一步获取到的cookie
	 * @param newPsw 新密码
	 * @return error 102-cookie无效（身份无效） 0-修改密码成功 105-系统错误
	 * @throws IOException
	 * @throws JSONException
	 */



	public static String newPass(String checkCookie,String newPsw) throws IOException, JSONException {

		return MyHttp.Post(urlHeadr+"AppUser/newPass","password="+newPsw, "utf-8",false, checkCookie);
	}

}
