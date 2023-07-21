package com.example.http_test;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends Activity {
	Button but1, but2, but3, but4;
	TextView tvw;

	private final static String BASE_URL = "http://192.168.1.100:8080//beautifulzzzz//servlet//hello";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		but1 = (Button) this.findViewById(R.id.button1);
		but2 = (Button) this.findViewById(R.id.button2);
		but3 = (Button) this.findViewById(R.id.button3);
		but4 = (Button) this.findViewById(R.id.button4);
		tvw = (TextView) this.findViewById(R.id.textView1);
		but1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Quest(1).start();
			}
		});
		but2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Quest(2).start();
			}
		});
		but3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Quest(3).start();
			}
		});
		but4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				new Quest(4).start();
			}
		});
	}

	/***
	 * �ռ�Message��Ϣ���ڸ���TextView,��Ϊlayout���²��ܷ��߳��У�����Ҫ�����ó���
	 */
	Handler registerHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			if (msg.what == -1) {
				return;
			}
			if (msg.what == 1) {
				tvw.setText(msg.getData().getSerializable("get").toString());
				return;
			}
		}
	};

	/***
	 * 
	 * @author LiTao ��ΪhttpҪ��������һ���߳����Ȼ�ᵼ��������������4�����󷽷���������kind��ʾ��ͬ�������߳�
	 *         Ȼ��ֱ���ò�ͬ��������Fun������������
	 *         ������Quest��Ҳ����ʵ��TextView���£�����Ҫ��Message����õ�getStr����
	 */
	public class Quest extends Thread {
		private int kind = 0;

		Quest(int kind) {
			this.kind = kind;
		}

		public void run() {
			String getStr = "";
			Message message = new Message();
			try {
				switch (kind) {
				case 1:
					getStr = Func1();
					message.what = 1;
					break;
				case 2:
					getStr = Func2();
					message.what = 1;
					break;
				case 3:
					getStr = Func3();
					message.what = 1;
					break;
				case 4:
					getStr = Func4();
					message.what = 1;
					break;
				default:
					break;
				}
			} catch (Exception e) {
				e.printStackTrace();
				message.what = -1;
			}
			Bundle bundle = new Bundle();
			bundle.putSerializable("get", getStr);
			message.setData(bundle);
			registerHandler.sendMessage(message);
		}
	}
	
	/***
	 * ��HttpURLConnection����Get���󣬷��������ַ�
	 * @return
	 * @throws IOException
	 */
	public String Func1() throws IOException{
		// ƴ��get�����URL�ִ���ʹ��URLEncoder.encode������Ͳ��ɼ��ַ����б���
		String MyURL=BASE_URL+ "?name=" + URLEncoder.encode("beautifulzzzz", "utf-8")
        		+"&password=12345678";//(�����������Ĳ���)
		URL getUrl = new URL(MyURL);
		// ����ƴ�յ�URL�������ӣ�URL.openConnection���������URL�����ͣ�
        // ���ز�ͬ��URLConnection����Ķ�������URL��һ��http�����ʵ�ʷ��ص���HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) getUrl.openConnection();
        
		// ������������
		conn.setConnectTimeout(30000);// �������ӳ�ʱʱ������λ����
              
        // �������ӣ�����ʵ����get requestҪ����һ���connection.getInputStream()�����вŻ���������������
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                conn.getInputStream()));// ȡ������������ʹ��Reader��ȡ
        String result = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			result = result + line+"\n";
		}
		System.out.println(result);
		reader.close();
		conn.disconnect();
		return result;
	}

	/***
	 * ��HttpURLConnection����post���󣬷��������ַ�
	 * @return
	 * @throws IOException
	 */
 	public String Func2() throws IOException {
		URL url = new URL(BASE_URL);
		// �˴���urlConnection����ʵ�����Ǹ���URL��
		// ����Э��(�˴���http)���ɵ�URLConnection��
		// ������HttpURLConnection,�ʴ˴���ý���ת��
		// ΪHttpURLConnection���͵Ķ���,�Ա��õ�
		// HttpURLConnection�����API.����:
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();

		// ������������
		conn.setDoOutput(true);// ʹ�� URL ���ӽ������
		conn.setDoInput(true);// ʹ�� URL ���ӽ�������
		conn.setUseCaches(false);// POST�������û���
		conn.setConnectTimeout(30000);// �������ӳ�ʱʱ������λ����
        conn.setInstanceFollowRedirects(true);// URLConnection.setInstanceFollowRedirects�ǳ�Ա�������������ڵ�ǰ����
        // ���ñ������ӵ�Content-type������Ϊapplication/x-www-form-urlencoded��
        // ��˼��������urlencoded�������form�������������ǿ��Կ������Ƕ���������ʹ��URLEncoder.encode
        // ���б���
        conn.setRequestProperty("Content-Type",
                "application/x-www-form-urlencoded");
		conn.setRequestMethod("POST");// ��������ʽ��POST or
										// GET��ע�⣺��������ַΪһ��servlet��ַ�Ļ��������ó�POST��ʽ

		OutputStream outStrm = conn.getOutputStream();// �˴�getOutputStream�������Ľ���connect
		DataOutputStream out = new DataOutputStream(outStrm);
		 // ���ģ�����������ʵ��get��URL��'?'��Ĳ����ַ���һ��
        String content = "name=" + URLEncoder.encode("��ĳ��", "utf-8")
        		+"&password="+ URLEncoder.encode("12345678", "utf-8");
        // DataOutputStream.writeBytes���ַ����е�16λ��unicode�ַ���8λ���ַ���ʽд��������
        out.writeBytes(content); 
        out.flush();
        out.close(); // flush and close
        
		// ����HttpURLConnection���Ӷ����getInputStream()����,
		// ���ڴ滺�����з�װ�õ�������HTTP������ķ��͵�����ˡ�
		InputStream inStrm = conn.getInputStream(); // <===ע�⣬ʵ�ʷ�������Ĵ���ξ�������
		// �ϱߵ�httpConn.getInputStream()�����ѵ���,����HTTP�����ѽ���,������������������������壬
		// ��ʹ���������û�е���close()�������±ߵĲ���Ҳ��������������д���κ�����.
		// ��ˣ�Ҫ���·�������ʱ��Ҫ���´������ӡ���������������´�������������д���ݡ�
		// ���·�������(�����Ƿ���������Щ������Ҫ���о�)
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(inStrm));
		String result = "";
		String line = "";
		while ((line = reader.readLine()) != null) {
			result = result + line+"\n";
		}
		System.out.println(result);
		reader.close();
	    conn.disconnect();
		return result;
	}

	/***
	 * ʹ��Http��GET���󷵻ط��������ؽ���ַ���
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String Func3() throws ClientProtocolException, IOException {
		HttpGet httpGet = new HttpGet(BASE_URL + "?name=beautifulzzzz"
				+ "&password=1234");
		// ��ȡHttpClient����
		HttpClient httpClient = new DefaultHttpClient();
		// ���ӳ�ʱ
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		// ����ʱ
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);
		HttpResponse httpResp = httpClient.execute(httpGet);
		String response = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
		System.out.println(response);
		if (response == null)
			response = "";
		return response;
	}

	/***
	 * ʹ��Http��POST���󷵻ط��������ؽ���ַ���
	 * 
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public String Func4() throws ClientProtocolException, IOException {
		// ���Ñ������ܴa��imei���b��list�У���http�l��postՈ��o������
		NameValuePair pair1 = new BasicNameValuePair("user_name", "��");
		NameValuePair pair2 = new BasicNameValuePair("user_password",
				"Deddd344");
		List<NameValuePair> pairList = new ArrayList<NameValuePair>();
		pairList.add(pair1);
		pairList.add(pair2);
		HttpPost httpPost = new HttpPost(BASE_URL);
		HttpEntity requestHttpEntity = new UrlEncodedFormEntity(pairList,
				HTTP.UTF_8);
		// �����������ݼ���������
		httpPost.setEntity(requestHttpEntity);
		// ��ȡHttpClient����
		HttpClient httpClient = new DefaultHttpClient();
		// ���ӳ�ʱ
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 30000);
		// ����ʱ
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT,
				30000);

		HttpResponse httpResp = httpClient.execute(httpPost);
		String response = EntityUtils.toString(httpResp.getEntity(), "UTF-8");
		System.out.println(response);
		if (response == null)
			response = "";
		return response;
	}
}
