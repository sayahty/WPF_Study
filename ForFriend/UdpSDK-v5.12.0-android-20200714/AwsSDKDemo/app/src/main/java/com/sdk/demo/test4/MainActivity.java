package com.sdk.demo.test4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crashlytics.android.Crashlytics;
import com.games.android.test1.R;
import com.games.sdk.CurrencyCode;
import com.games.sdk.SdkCallback;
import com.games.sdk.SdkKtplayListener;
import com.games.sdk.SdkPaymentInterface;
import com.games.sdk.SdkPlatform;
import com.games.sdk.SdkPlatformConstant;
import com.games.sdk.SdkPlatformConstant.Language;
import com.games.sdk.SdkPlatformInterface;
import com.games.sdk.vo.FBPageInfo;
import com.games.sdk.vo.FriendInfo;
import com.games.sdk.vo.ProductInfo;
import com.games.sdk.vo.UserInfo;

import java.lang.ref.WeakReference;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainActivity extends Activity {
	private static final String TAG = "SdkDemo";

	private final String FUNCTION_SDK_CHANGELANGUAGE = "切换语言";
	private final String FUNCTION_SDK_INDEXING = "强化搜索";
	private final String FUNCTION_SDK_REPORTCRASH = "上报崩溃";
	private final String FUNCTION_SDK_PURCHASE = "支付";
	private final String FUNCTION_SDK_SWITCHUSER = "切换用户";
//	private final String FUNCTION_SDK_CHANGESERVERINFO = "切换服务器或角色";
	private final String FUNCTION_SDK_CHANGESERVERINFORESET = "设置新的服务器或角色";
	private final String FUNCTION_SDK_OGSHOW = "显示OG";
	private final String FUNCTION_SDK_OGHIDE = "隐藏OG";
	private final String FUNCTION_SDK_CUSTOMER = "联系客服";
    private final String FUNCTION_SDK_REPORTADJUST = "Adjust上报示例";
    private final String FUNCTION_SDK_REPORTMDATA = "Mdata上报示例";
    private final String FUNCTION_SDK_REPORTFIREBASE = "Firebase上报示例";
    private final String FUNCTION_SDK_SUGGEST = "反馈建议";
    private final String FUNCTION_SDK_BBS = "论坛";
    private final String FUNCTION_SDK_PERSON_CENTER = "个人中心";
	private final String FUNCTION_SDK_FBCONNECT = "FB关联";
	private final String FUNCTION_SDK_FBSHARELINK = "FB分享链接";
	private final String FUNCTION_SDK_FBSHAREPIC = "FB分享图片";
	private final String FUNCTION_SDK_FBGETFREIEND = "获取FB好友";
	private final String FUNCTION_SDK_FBGETABLEFREIEND = "获取可邀请FB好友";
	private final String FUNCTION_SDK_FBIVITEFREIEND = "邀请FB好友";
	private final String FUNCTION_SDK_FBSEND = "赠送体力";
	private final String FUNCTION_SDK_FBASK = "索要体力";
	private final String FUNCTION_SDK_GAMEAREA = "设置游戏大区";
	private final String FUNCTION_SDK_OPENLINE = "打开LINE";
	private final String FUNCTION_SDK_OPENVK = "打开VK";
	private final String FUNCTION_SDK_SHARE_TWITTER = "Twitter分享";

    AtomicBoolean isSandbox = new AtomicBoolean(false);

	String[] country = new String[]{"英语","德语","希腊语","西班牙语","法语","意大利语",
			"韩语","荷兰语","波兰语","葡萄牙语","俄语","瑞典语","土耳其语","越南语","泰语","日语","中文繁体","中文简体","阿拉伯语"};
	final Language[] languages = new SdkPlatformConstant.Language[]{Language.EN,	Language.DE, Language.EL, Language.ES, Language.FR, Language.IT,
			Language.KO, Language.NL, Language.PL, Language.PT, Language.RU, Language.SV, Language.TR,
			Language.VI, Language.TH, Language.JA, Language.ZH_TW, Language.ZH, Language.AR};
    String[] permissions = new String[]{
//			android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
//			android.Manifest.permission.READ_EXTERNAL_STORAGE,
    };
    String[] permissionNotices = new String[]{
//			"WRITE_EXTERNAL_STORAGE",
//			"READ_EXTERNAL_STORAGE"
    };

    String[][] functionArray = null;
    LinearLayout function_container;
    Language currentGameLanguage = Language.ZH_TW;
	// 声明1个Handler对象
	public MyHandler myHandler = null;
	public MainActivity activity;
	String uri;

    private EditText product_id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//2019-3-12为了适配全面屏，刘海屏而加入以下的设置
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,  WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //添加全面屏支持
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //2019-3-11 开启页面使用刘海区域，为适配刘海屏幕 start
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            lp.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
        }
        getWindow().setAttributes(lp);
        //2019-3-11 开启页面使用刘海区域，为适配刘海屏幕 end
		setContentView(R.layout.main2);
		activity = this;
		myHandler = new MyHandler(activity);
        Log.w(TAG, "当前SDK版本号："+SdkPlatform.getSdkVersionInfo());

        if(getIntent() != null){
            uri = getIntent().getDataString();
            Log.e("MainCompatActivity", "警告：来源于FB的地址："+getIntent().getDataString());// 当使用Facebook发送请求、索要礼物等情况时，需要处理从facebook跳转过来的URI，
//		https://m.facebook.com/appcenter/heroes_pt?fbs=1001&request_ids=1410279699278863&ref=notif&app_request_type=user_to_user&content=send%3A632964910166909

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                Log.e("MainActivity", "title = "+bundle.getString("title") + "；" + "body = "+bundle.getString("body") );
                Log.e("MainActivity", "uid = "+bundle.getString("uid") + "；" );
            }
        }

        if (Build.VERSION.SDK_INT >= 23) {// android 6.0及以上设备需要检查权限
            String lastLanguage = getLanguage();
            Log.w(TAG, "游戏启动所需权限验证：\n调用函数SdkPlatform.checkPermissions;当前语言："+getLanguageName());
            SdkPlatform.checkPermissions(this, SdkPlatformConstant.REQUEST_CODE_CHECKPERMISSIONS_REQUEST, permissions, permissionNotices, Language.valueOf(lastLanguage));
        }else {
            isSandBoxByUser();
        }

	}
    private void initSDK(){
        //初始化
        Log.w(TAG, "初始化-init：\n调用函数SdkPlatform.init;");
        SdkPlatform.init(this, isSandbox.get(), Language.valueOf(getLanguage()));

        //实例化接口，为登录成功后的操作作准备
        Log.w(TAG, "初始化-实例化接口：\n调用函数SdkPlatform.setSdkPlatformInterfaceImpl;");
        SdkPlatform.setSdkPlatformInterfaceImpl(new InterfaceImpl());
        SdkPlatform.setSdkPaymentInterfaceImpl(new SdkPaymentInterface() {
            @Override
            public void paymentCallback(String s, int paymentCode, String errorMessage) {
                // 如果调用了SdkPlatform.setSdkPlatformInterfaceImplForPayment()，SdkPlatformInterfaceImpl中的paymentCallback、getExtendValue将不会被回调。
                switch (paymentCode) {
                    case SdkPlatformConstant.RESULT_SUCCESS:
                        showNotice("游戏提示：充值成功");
                        break;
                    case SdkPlatformConstant.RESULT_CANCEL:
                    case SdkPlatformConstant.RESULT_CANCEL_USER:
                    case SdkPlatformConstant.RESULT_FAIL:
                    case SdkPlatformConstant.RESULT_EXCEPTION:
                    case SdkPlatformConstant.RESULT_EXCEPTION_RETRYPAY_EXCEPTION:
                        showNotice("游戏提示："+errorMessage);
                    default:
                        break;
                }
            }

            @Override
            public void getExtendValue(String s, String s1, String s2, SdkCallback SdkCallback) {
                // 如果调用了SdkPlatform.setSdkPlatformInterfaceImplForPayment()，SdkPlatformInterfaceImpl中的paymentCallback、getExtendValue将不会被回调。
            // 生成支付时的透传参数 ext，功能等同于SdkPlatform.toGoogleBillPayPage时的ext参数
                String extValue  = "";

                // 此方法中必须回调success或error函数，
                SdkCallback.success(extValue);
            //			SdkCallback.error("error");
            }

            @Override
            public void decideProductsVisibility(Map<String, Boolean> product_ids, SdkCallback callback) {
                // 游戏将运营提供的商品ID（按每一档的显示条件）逐一进行检查，判断product_ids中每一档商品，是否展示给玩家；如果展示给玩家，代表该档位的商品可以购买；
                // 参数说明：
                // 		product_ids		SDK当前所有的档位(采用key-value的形式保存)
                // 						key形如 "xxx.mtester.100." 、"prop.mtester.200."等；同档位可能包含多个支付渠道，所以采用商品ID的前三段表示。
                //						value 默认都是true; 某些限时、限购的商品或礼包，游戏可根据条件将value设置为false。
                //		callback		将处理后的product_ids通过success回调给SDK；如无需处理，可调用error回调给SDK。
                // 注意：1、如果不需要特殊处理，可使用callback.success(product_ids)直接回调SDK；
                // 		2、任何情况下都必须回调给SDK结果（无论callback.success或 callback.error），否则SDK将持续等待



                /**以下为测试代码**/
                // 已有productId=xxx.mtester.100.23036;xxx.mtester.200.23035;prop.mtester.200.25150;mcard.mtester.500.25149
                // 测试：100档游戏币商品不展示
//                if (product_ids != null && product_ids.containsKey("xxx.mtester.100.")) {
//                    product_ids.put("xxx.mtester.100.", false);
//                }
//
//                // 测试：道具、礼包、月卡商品不展示（即prop.mtester.200.25150和mcard.mtester.500.25149不展示）
//                if (product_ids != null && product_ids.containsKey("prop.mtester.200.")) {
//                    product_ids.put("prop.mtester.200.", false);
//                }
//                if (product_ids != null && product_ids.containsKey("mcard.mtester.500.")) {
//                    product_ids.put("mcard.mtester.500.", false);
//                }
//
//                // 测试：200档所有商品都不展示
//                for (Map.Entry entry:product_ids.entrySet()) {
//                    if (((String) entry.getKey()).endsWith(".200.")) // 例如：200档位的所有商品不展示给玩家（包含普通商品、道具、月卡等）
//                        product_ids.put((String)entry.getKey(),false);
//                }

                // 打印日志：处理后各档位的显示情况
                for (Map.Entry entry:product_ids.entrySet()) {
                    Log.e(TAG, "处理后：档位="+entry.getKey()+"  是否展示："+entry.getValue().toString());
                }
			    callback.success(product_ids);
            }
        });

        //跟踪
        Log.w(TAG, "初始化-生命周期-trackOnCreate：\n调用函数SdkPlatform.trackOnCreate;");
        SdkPlatform.trackOnCreate(this);

    }
    private void isSandBoxByUser(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示")
                .setMessage("如果上次是沙箱模式本次也选择沙箱模式则为自动登录，如果选择正式模式则为匿名登录。反之同理。")
                .setCancelable(false);
        builder.setPositiveButton("正式模式", new AlertDialog.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSandbox.set(false);
                // 开始初始化SDK
                initSDK();
                //初始化demo UI
                initDemoUI();
            }
        });
        builder.setNegativeButton("沙箱模式", new AlertDialog.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialog, int which) {
                isSandbox.set(true);
                // 开始初始化SDK
                initSDK();
                //初始化demo UI
                initDemoUI();
            }
        });
        builder.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.w(TAG, "初始化-生命周期-trackOnResume：\n调用函数SdkPlatform.trackOnResume;");
        SdkPlatform.trackOnResume(this);
    }
    @Override
    protected void onStart() {
        super.onStart();
        Log.w(TAG, "初始化-生命周期-trackOnStart：\n调用函数SdkPlatform.trackOnStart;");
        SdkPlatform.trackOnStart(this);
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.w(TAG, "初始化-生命周期-trackOnRestart：\n调用函数SdkPlatform.trackOnRestart;");
        SdkPlatform.trackOnRestart(this);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.w(TAG, "初始化-生命周期-trackOnPause：\n调用函数SdkPlatform.trackOnPause;");
        SdkPlatform.trackOnPause(this);
    }
    @Override
    protected void onStop() {
        Log.w(TAG, "初始化-生命周期-trackOnStop：\n调用函数SdkPlatform.trackOnStop;");
        SdkPlatform.trackOnStop(this);
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        Log.w(TAG, "初始化-生命周期-trackOnDestroy：\n调用函数SdkPlatform.trackOnDestroy;");

        SdkPlatform.trackOnDestroy(this);
        //清除数据
        Log.w(TAG, "初始化-生命周期-destroy：\n调用函数SdkPlatform.destroy;");
        SdkPlatform.destroy(this);
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == SdkPlatformConstant.REQUEST_CODE_CHECKPERMISSIONS_REQUEST){
            if(resultCode == SdkPlatformConstant.RESULT_SUCCESS){
                isSandBoxByUser();
            }else{
                Log.d(TAG, "游戏方重新请求授权，避免某权限未授权成功");
                // 重新尝试授权
                Log.w(TAG, "游戏启动所需权限再次验证：\n调用函数SdkPlatform.checkPermissions;当前语言："+currentGameLanguage.name());
                SdkPlatform.checkPermissions(this, SdkPlatformConstant.REQUEST_CODE_CHECKPERMISSIONS_REQUEST, permissions, permissionNotices, currentGameLanguage);
            }

        }
    }

    private class InterfaceImpl implements SdkPlatformInterface {
        @Override
        public void quitApplication(int type, String message) {
            // 触发条件：
            // 根据SDK的逻辑，当发生以下情况时，exitGame将被调用，此时必须退出整个APP
            // 1、登录过程中因网络问题无法登录成功，玩家在界面中点击了"退出游戏"按钮
            // 2、登录的账号被封禁时，玩家在界面中点击了"确认"按钮 或 返回键

            // 参数说明：
            // type 	退出游戏的类型：1-自动登录失败；2-账号或设备被禁
            // message 	描述信息




            /**-------以下为游戏方具体的退出整个APP的操作--------**/
            // 游戏根据自身情况处理，目的是退出整个APP
            Toast.makeText(MainActivity.this, "游戏提示：SDK通知退出游戏", Toast.LENGTH_LONG).show();
            activity.finish();//执行完生命周期的整个流程
        }

        @Override
        public void reloadGame(UserInfo userInfo) {
            if(userInfo == null){
                Toast.makeText(MainActivity.this, "登录失败！用户取消操作", Toast.LENGTH_LONG).show();
                return;
            }
            findViewById(R.id.sdk_login).setVisibility(View.GONE);
//			listView.setVisibility(View.VISIBLE);
            function_container.setVisibility(View.VISIBLE);
            product_id.setVisibility(View.VISIBLE);
            // 选服或选角色后，同步设置用户信息
            Log.w(TAG, "设置角色信息：\n调用函数SdkPlatform.setUserInfo;服ID："+"102001；"+ "服名称：Ölüm Perisi Sulağı，"+"服类型：all，"+"角色名：中文角色名称，"+ "角色ID：102178196 ；此接口参数涉及游戏内发钻，请确保参数正确性");
//            SdkPlatform.setUserInfo("102001", "Ölüm Perisi Sulağı", "all", "中文角色名称", "102178196");

            StringBuilder showNotice = new StringBuilder();
            showNotice.append( "当前用户是通过 ").append((1==userInfo.operation?"用户登录":"切换账号")).append(" 完成本次登录");
            showNotice.append( "\n当前登录账户player_id:").append(userInfo.uid);
            showNotice.append( "\n当前账户类型:").append(1==userInfo.type?"游客账号":"正式账号").append("[").append(userInfo.platform).append("]");

            showNotice.append( "\n当前语言:").append(getLanguageName());
            showNotice.append( "\nSDK Version:").append(SdkPlatform.getSdkVersionInfo());

            TextView text_notice = findViewById(R.id.sdk_notice);
            text_notice.setText(showNotice.toString());

            myHandler.sendEmptyMessage(1000);


        }

        @Override
        public void fbRequestCallback(int requestAction, int resultCode, String fbRequestId) {
            Log.d("MainCompatActivity", "动作："+requestAction +"   resultCode："+resultCode);
            if(resultCode != SdkPlatformConstant.RESULT_SUCCESS){// 操作失败 或 操作取消
                switch (requestAction) {
                    case 1:
                        // invite 邀请
                        break;
                    case 2:
                        // send 发送、赠送

                        break;
                    case 3:
                        // askfor 索要
                        break;

                    default:

                        break;
                }
                return;
            }
            switch (requestAction) {
                case 1:
                    // invite 邀请
                    break;
                case 2:
                    // send 发送、赠送

                    break;
                case 3:
                    // askfor 索要
                    break;

                default:

                    break;
            }
        }

        @Override
        public void fbFriendsListCallback(int type, int resultCode, FBPageInfo info) {
            if(resultCode != SdkPlatformConstant.RESULT_SUCCESS || info == null)
                return;
            for (FriendInfo user : info.data) {
                System.out.println("id="+user.id+"  name="+user.name +" \n "+user.picture);
            }
            Intent in = new Intent();
            in.putExtra("fbpageinfo", info);
            if(type == SdkPlatformConstant.REQUEST_CODE_FACEBOOK_GETFRIENDS){
                // info 为正在玩该游戏的好友

                in.putExtra("friendType", 1);
            }
            else if(type == SdkPlatformConstant.REQUEST_CODE_FACEBOOK_GETINVITABLEFRIENDS){
                // info 为 可邀请的好友

                in.putExtra("friendType", 2);
            }
            in.setClass(activity, FriendsListActivity.class);
            startActivity(in);
        }

        @Override
        /**
         * 游戏中发起账号关联请求，SDK通过本接口返回相应的结果
         * @param platform 		平台类型
         * @param code 			结果码
         * @param bind_entrance 绑定入口
         *                      "api" 表示从借口处调用绑定
         *                      "sdk" 表示从个人中心页面调用绑定
         * @param userInfo		绑定后的用户信息,如果绑定失败为空
         * @param message 		描述
         *2018-10-29 v4.14.0 接口名称由 connectCallbak 更新为 connectCallback
         *2019-04-19 v4.15.2 新增参数bind_entrance、userInfo
         *
         */
        public void connectCallback(String platform, int code,String bind_entrance,UserInfo userInfo, String message) {
            if (code == SdkPlatformConstant.RESULT_SUCCESS){
                Toast.makeText(MainActivity.this, platform+" connect is success!" , Toast.LENGTH_LONG).show();
            }else
                Toast.makeText(MainActivity.this, platform+" connect is fail!\n"+message , Toast.LENGTH_LONG).show();
        }

        @Override
        public void deviceTokenCallback(String deviceToken) {
            Log.e(TAG, "接收到deviceToken："+deviceToken+"  可根据实际情况建立与角色的对应关系，以达到精准推送的目的");
        }

        @Override
        public void vkFriendsListCallback(int i, String s, String s1, String s2) {

        }

        @Override
        public void shareCallback(String shareType, int resultCode, String id, String errorInfo) {
            String showNotice;
            if (resultCode == SdkPlatformConstant.RESULT_SUCCESS){
                showNotice = shareType + "分享成功("+id+")";
            }else if (resultCode == SdkPlatformConstant.RESULT_CANCEL_USER){
                showNotice = "用户取消分享";
            }else{
                showNotice = shareType + "分享失败，"+errorInfo;
            }
            Toast.makeText(MainActivity.this, showNotice, Toast.LENGTH_LONG).show();
        }

        @Override
        public void adCallback(int resultCode, String message) {

//			-1:广告播放成功
//			0:加载广告或播放广告失败或其他错误
//			1:未登录或未创角
//			2:缺少必要参数：adUnitKey
//			3:未接入广告SDK
//			4:SDK服务端未配置adUnitKey或广告单元ID
//			5:不支持该广告类型
//			6:SDK服务端，广告开关未开
//			7:未调用SdkPlatform.setSdkPaymentInterfaceImpl接口
//			8:getExtendValue函数返回失败
            Toast.makeText(MainActivity.this, "游戏方提示："+ message, Toast.LENGTH_LONG).show();
        }
    }


    /** ---------------------------------------以上代码为CP接入代码---------------------------------------------- **/


    /** ---------------------------------------此线以下代码为SDK Demo代码---------------------------------------------- **/


	private void turnPage(String text){
		if(FUNCTION_SDK_CHANGELANGUAGE.equals(text)){
			showChangeDialog();
		}else if (FUNCTION_SDK_REPORTCRASH.equals(text)){
			try {
				int i = 10/0;
			}catch (Exception e){
				Exception exception = e;
				if (SdkPlatform.getUserInfo() != null && !TextUtils.isEmpty(SdkPlatform.getUserInfo().uid))
					exception = new Exception(SdkPlatform.getUserInfo().uid, e);
                Crashlytics.logException(exception);

				Toast.makeText(this, "Firebase Crash上报操作成功", Toast.LENGTH_LONG).show();
			}
		}else if (FUNCTION_SDK_SWITCHUSER.equals(text)){
			// 切换用户
            Log.w(TAG, "切换用户）：\n调用函数SdkPlatform.switchUser：");
			SdkPlatform.switchUser(this);
		}else if (FUNCTION_SDK_PURCHASE.equals(text)){// 调用Google支付页面
			String productID = product_id.getText().toString();// xxx.mtester.200.23035
            Log.w(TAG, "购买商品：\n调用函数SdkPlatform.purchase;商品ID："+productID);
			SdkPlatform.purchase(this, productID, 0.99, 100, CurrencyCode.USD);
		}else if (FUNCTION_SDK_CHANGESERVERINFORESET.equals(text)){
			// 注销用户后，重新选服或选角色时 设置当前用户信息
            Log.w(TAG, "设置角色信息：\n调用函数SdkPlatform.setUserInfo;服ID："+"102001；"+ "服名称：Server1，"+"服类型：android，"+"角色名：中文角色名称，"+ "角色ID：102178196 ；此接口参数涉及游戏内发钻，请确保参数正确性");
//			SdkPlatform.setUserInfo("102001", "Server1", "", "中文角色名称", "102178196");
		}else if (FUNCTION_SDK_OGSHOW.equals(text)){
			// 显示ＯＧ助手
			showOGMenu(true);
		}else if (FUNCTION_SDK_OGHIDE.equals(text)){
			// 隐藏ＯＧ助手
			showOGMenu(false);
		}else if (FUNCTION_SDK_FBGETFREIEND.equals(text)){
			SdkPlatform.getFriends(this, 10, true, true);
		}else if (FUNCTION_SDK_FBGETABLEFREIEND.equals(text)){
			SdkPlatform.getInvitableFriends(this, 5, true, true);
		}else if (FUNCTION_SDK_FBIVITEFREIEND.equals(text)){
			SdkPlatform.setAppRequest(activity, 1, "", null, "测试邀请好友");
		}else if (FUNCTION_SDK_FBSEND.equals(text)){
			SdkPlatform.setAppRequest(activity, 2, "632964910166909", null, "向好友赠送体力");// 632964910166909    430168697148051
		}else if (FUNCTION_SDK_FBASK.equals(text)){
			SdkPlatform.setAppRequest(activity, 3, "430168697148051", null, "向好友索要体力");
		}else if (FUNCTION_SDK_FBSHAREPIC.equals(text)){
			String path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/IMG_20160308_185628.jpg";//"/DCIM/Screenshots/11111.png";//
            Log.w(TAG, "Facebook分享图片：\n调用函数SdkPlatform.uploadImage;图片地址："+path);
            SdkPlatform.share(this, "It's good idea。", path, "https://play.google.com/store/apps/details?id=com.facebook.katana");
		}else if (FUNCTION_SDK_FBSHARELINK.equals(text)){
	    	String link = "https://developers.facebook.com/android";
            Log.w(TAG, "Facebook分享链接：\n调用函数SdkPlatform.shareByFacebook;");
            SdkPlatform.share(this, "It's good idea。", "", "https://play.google.com/store/apps/details?id=com.facebook.katana");
		}else if (FUNCTION_SDK_FBCONNECT.equals(text)){
			SdkPlatform.connectFacebook(this);
		}else if (FUNCTION_SDK_CUSTOMER.equals(text)){
			SdkPlatform.contactCustomerService(this);
		}else if(FUNCTION_SDK_REPORTADJUST.equals(text)){
            //上报adjust
            //跟踪事件1
//		HashMap<String, String> map_value = new HashMap<String, String>();
//		map_value.put("key1", "value1");
//		SdkPlatform.trackEvent(this, SdkPlatformConstant.REPORT_TYPE_ADJUST, "LV5", map_value, null); // 当map_value不为空时，需要市场人员在Adjust后台配置相应的回调地址，如果没有配，请直接采用跟踪事件2.
            //跟踪事件2
            Log.w(TAG, "Adjust事件上报：\n调用函数SdkPlatform.trackEvent;传递事件名：adjust_LV3");
            SdkPlatform.logEvent("adjust_LV3", null, SdkPlatformConstant.REPORT_TYPE_ADJUST);
            showNotice("Adjust事件已经上报");
//		SdkPlatform.trackRevenue(this, "", 9.99, "RMB", null);
//			SdkPlatform.trackEvent(this, SdkPlatformConstant.REPORT_TYPE_ALL, "LV3", null, null);
        }else if(FUNCTION_SDK_REPORTMDATA.equals(text)){
            //上报mdata
            Log.w(TAG, "Mdata事件上报：\n调用函数SdkPlatform.trackEvent;传递事件名：mdata_LV3（事件名称由运营提供）");
            SdkPlatform.logEvent("mdata_LV3", null, SdkPlatformConstant.REPORT_TYPE_MDATA);
            showNotice("Mdata事件已经上报");
        }else if(FUNCTION_SDK_REPORTFIREBASE.equals(text)){
            //上报Firebase
            Log.w(TAG, "Firebase事件上报：\n调用函数SdkPlatform.trackEvent;传递事件名：firebase_LV3（事件名称由运营提供）");
            SdkPlatform.logEvent("firebase_LV3", null, SdkPlatformConstant.REPORT_TYPE_FIREBASE);
            showNotice("Firebase事件已经上报");
        }else if(FUNCTION_SDK_BBS.equals(text)){
            SdkPlatform.showBBS(this);

            SdkPlatform.setOnStatusChangeListener(new SdkKtplayListener.OnStatusChangedListener() {
                @Override
                public void onStatusChangedListener(boolean hasNews) {
                    Log.e("MainCompatActivity", "avg = "+hasNews);
                }
            });
        }else if(FUNCTION_SDK_SUGGEST.equals(text)){
            SdkPlatform.showFeedback(this);
        }else if(FUNCTION_SDK_PERSON_CENTER.equals(text)){
            SdkPlatform.showPersonalCenter(this);
        }else if(FUNCTION_SDK_OPENLINE.equals(text)){
            SdkPlatform.openLoginStyleForLine(this, true);
            showNotice("Line已开启，请进入SDK界面查看并使用");
        }else if(FUNCTION_SDK_OPENVK.equals(text)){
            SdkPlatform.openLoginStyleForVK(this, true);
            showNotice("VK已开启，请进入SDK界面查看并使用");
        }else if(FUNCTION_SDK_SHARE_TWITTER.equals(text)){
            String path = Environment.getExternalStorageDirectory().getPath()+"/DCIM/Camera/IMG_20160308_185628.jpg";
            SdkPlatform.share(this, "It's good idea。", path, "https://play.google.com/store/apps/details?id=com.facebook.katana");
            Log.w(TAG, "Twitter分享：\n调用函数SdkPlatform.share;图片地址："+path);
        }
	}

	public void  onBtnClick(View v){
		switch (v.getId()) {
		case R.id.sdk_login:
			// 调用登录页面--自动登录
            Log.w(TAG, "自动登录：\n调用函数SdkPlatform.login");
            SdkPlatform.login(this, -1);
			break;
		default:
			break;
		}
	}


	private static class MyHandler extends Handler {

		// WeakReference to the outer class's instance.
		private WeakReference<MainActivity> mOuter;

        MyHandler(MainActivity activity) {
			mOuter = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg) {
			MainActivity outer = mOuter.get();
			switch (msg.what) {
			case 1000:

				outer.showOGMenu(true);
				break;
			case 100:
				Toast.makeText(outer, "FB请求操作成功", Toast.LENGTH_LONG).show();
			default:
				break;
			}
		}

	}

	public void showOGMenu(boolean showFlag){
		// 显示 OG
        Log.w(TAG, (showFlag?"展示":"隐藏")+"OG助手：\n调用函数SdkPlatform.showMenu");
		SdkPlatform.showMenu(activity, 1, showFlag);

		// 处理来自于FB的跳转
		if(uri != null && !TextUtils.isEmpty(uri)){//
//			uri = "https://m.facebook.com/appcenter/heroes_pt?fbs=1001&request_ids=1410279699278863&ref=notif&app_request_type=user_to_user&content=send%3A632964910166909";
			Uri u = Uri.parse(uri);
			String requestIDs = u.getQueryParameter("request_ids");
			if(TextUtils.isEmpty(requestIDs))
				return;

			// 以下内容为测试内容，请接入方自行实现
			activity.test(requestIDs);
		}
	}



	private void showNotice(String s){
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	private void test(final String requestIds){
		SdkPlatform.test(requestIds, new SdkCallback() {
			@Override
			public void success(Object s) {
				activity.myHandler.sendEmptyMessage(100);
			}

			@Override
			public void error(String s) {

			}
		});
	}

	/**
	 * 获取FB的key hash
     * Debug模式下生成的key hash只在Debug包中有效；
     * 如需release的key hash，请在正式签名的apk中执行该方法
	 */
	private void getFBKeyHash(){

		//获取当前应用的 key hash（分为签名与非签名两种）
		try {
			PackageInfo info = getPackageManager().getPackageInfo("替换为package name", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				String sign = Base64
						.encodeToString(md.digest(), Base64.DEFAULT);
				Log.e("MY KEY HASH:", sign);
			}
		} catch (NameNotFoundException|NoSuchAlgorithmException e) {
            e.printStackTrace();
		}
	}

    /**
     * 获取VK的Fingerprints
     * Debug模式下生成的fingerprints只在Debug包中有效；
     * 如需release的fingerprints，请在正式签名的apk中执行该方法
     */
    private void getVKFingerprints(){

//        String[] fingerprints = VKUtil.getCertificateFingerprint(this, "替换为package name");
//        if (fingerprints != null)
//            Log.e("VK fingerprints:", fingerprints[0]);
    }
	private String getLanguage(){
        SharedPreferences sharedPreferences = getSharedPreferences("DEMO", Context.MODE_PRIVATE);
        String languageCode = sharedPreferences.getString("lastLanguage", "");
        if (TextUtils.isEmpty(languageCode))
            languageCode = currentGameLanguage.name();

        return languageCode;

	}
	private String getLanguageName(){
        String languageName = "";
        String language = getLanguage();
		for(int i =0;i<languages.length;i++){
			if(language.equalsIgnoreCase(languages[i].toString())) {
                languageName = country[i];
                break;
            }
		}
		return languageName;
	}
	private void showChangeDialog(){
		new AlertDialog.Builder(this)
				.setItems(country, new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialogInterface, int i) {
                        Log.w(TAG, "切换游戏语言：\n调用函数SdkPlatform.setLanguage;语言即将切换为："+languages[i].name());

                        saveLanguage(languages[i].name());
					}
				}).show();
	}

    /**
     * 保存选中的语言，并重新启动应用
     * @param language  语言代码
     */
    private void saveLanguage(String language) {
        SharedPreferences sharedPreferences = getSharedPreferences("DEMO", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastLanguage", language.toUpperCase());
        editor.apply();
        editor.commit();

        restartApp();
    }
    private void restartApp() {
        Intent intent = new Intent(activity, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        activity.startActivity(intent);
        // 杀掉进程
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    private void initDemoUI() {
        Button loginButton = findViewById(R.id.sdk_login);
        if (isSandbox.get())
            loginButton.setText("沙箱登录");
        else
            loginButton.setText("登录");
        getFBKeyHash();
        getVKFingerprints();


        product_id = findViewById(R.id.game_product_id);
        function_container = findViewById(R.id.sdk_main);
        //初始化sdk demo界面
        functionArray = new String[][]{
                {FUNCTION_SDK_SWITCHUSER,FUNCTION_SDK_PURCHASE},
                {FUNCTION_SDK_OGSHOW,FUNCTION_SDK_OGHIDE},
                {FUNCTION_SDK_CHANGELANGUAGE,FUNCTION_SDK_PERSON_CENTER,FUNCTION_SDK_CUSTOMER,FUNCTION_SDK_BBS,FUNCTION_SDK_SUGGEST},
                {FUNCTION_SDK_FBCONNECT, FUNCTION_SDK_FBSHARELINK, FUNCTION_SDK_FBSHAREPIC, FUNCTION_SDK_FBGETFREIEND},
                {FUNCTION_SDK_REPORTADJUST,FUNCTION_SDK_REPORTMDATA,FUNCTION_SDK_REPORTFIREBASE,FUNCTION_SDK_REPORTCRASH},
//                {FUNCTION_SDK_OPENLINE,FUNCTION_SDK_OPENVK},
                {FUNCTION_SDK_SHARE_TWITTER}
        };

        for (String[] singleArea:functionArray) {
            View view_parent = getLayoutInflater().inflate(R.layout.main_function_array, null);
            LinearLayout view_content = view_parent.findViewById(R.id.sdk_main_function_array);
            for (final String function:singleArea) {
                View view = LayoutInflater.from(this).inflate(R.layout.main_function_item2,null);
                ((TextView)view.findViewById(R.id.func_item_name)).setText(function);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        turnPage(function);
                    }
                });
                view_content.addView(view);
            }
            function_container.addView(view_parent);

            TextView divisionView = new TextView(this);
            LinearLayout.LayoutParams parasDv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,30);
            divisionView.setLayoutParams(parasDv);
            function_container.addView(divisionView);
        }
        List<String> moreSkus = new ArrayList<>();
        moreSkus.add("xxx.mtester.100.23036");
        moreSkus.add("xxx.mtester.200.23035");
        moreSkus.add("mcard.mtester.500.25149");
        moreSkus.add("prop.mtester.200.25150");

        moreSkus.add("prop.mtester.100.3778");
        moreSkus.add("xxx.mtester.200.3771");
        SdkPlatform.getProductData(this, moreSkus, new SdkCallback() {
            @Override
            public void success(Object result) {
                Map<String,ProductInfo> resultMap= (Map<String, ProductInfo>) result;
                for (Map.Entry entry:resultMap.entrySet()){
                    ProductInfo info = (ProductInfo) entry.getValue();
                    Log.e(TAG,"getProductData:productDetail:"+info.toString());
                }
            }

            @Override
            public void error(String result) {
                Log.e("MainActivity", result);
            }
        });
    }
}
