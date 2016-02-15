package demo.linhao.com.springcart;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class MainActivity extends Activity {
    //ID号
    public static final String APP_ID = "wx46f7d09d59a7f552";
    private IWXAPI api;

    private CheckBox mShareFriends;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        api = WXAPIFactory.createWXAPI(this,APP_ID);
        //将APP_ID注册到微信中
        api.registerApp(APP_ID);
        mShareFriends = (CheckBox) findViewById(R.id.sure);
    }
    public void share(View view)
    {
        //第一步 创建WXWebpageObject对象
        WXWebpageObject webpage = new WXWebpageObject();
        webpage.webpageUrl = "http://1.monkeycardyear.applinzi.com";
        //第二步 创建一个WXMediaMessage对象
        WXMediaMessage msg = new WXMediaMessage(webpage);
        msg.title = "新年好";
        msg.description = "个人技术分享";
        //第三步 设置一个缩略图，获取二进制图片
        Bitmap thumb = BitmapFactory.decodeResource(getResources(),R.drawable.fu);
        msg.thumbData = bmpToByteArray(thumb, true);
        //第四步 创建一个对象
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = builtTransition("webpage");
        req.message = msg;
        req.scene = mShareFriends.isChecked()?SendMessageToWX.Req.WXSceneTimeline
                :SendMessageToWX.Req.WXSceneSession;
//        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        Toast.makeText(MainActivity.this, String.valueOf(api.sendReq(req)),Toast.LENGTH_SHORT).show();
        finish();
//        api.sendReq(req);

    }
    public void SendMessages()
    {
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        //分享到朋友或者朋友圈
        req.scene = mShareFriends.isChecked()?SendMessageToWX.Req.WXSceneTimeline
                :SendMessageToWX.Req.WXSceneSession;
       Toast.makeText(MainActivity.this, String.valueOf(api.sendReq(req)),Toast.LENGTH_SHORT).show();
    }
    //将bitmap转换成二进制数组
    private byte[] bmpToByteArray(final Bitmap bitmap,final boolean needRecycle)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100,out);
        if (needRecycle)
        {
            bitmap.recycle();
        }
        byte[] result = out.toByteArray();
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return  result;
    }
    //请求生成唯一标识
    private String builtTransition(final String type)
    {
        return (type==null)?String.valueOf(System.currentTimeMillis()):type+System.currentTimeMillis();
    }
    public void open(View view)
    {
        api.openWXApp();
    }
}
