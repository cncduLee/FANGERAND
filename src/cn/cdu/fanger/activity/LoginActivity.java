package cn.cdu.fanger.activity;


import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import cn.cdu.fanger.ac.view.AbstractAsyncActivity;
import cn.cdu.fanger.constant.ServerUrl;

public class LoginActivity extends AbstractAsyncActivity {
	private EditText username;
	private EditText userpwd;
	private Button loginbtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		bindComponents();
		addListener();
	}

	private void bindComponents() {
		username = (EditText) findViewById(R.id.login_username);
		userpwd = (EditText) findViewById(R.id.login_password);
		loginbtn = (Button) findViewById(R.id.login_submit);
	}

	private void addListener() {
		loginbtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				new LoginPostMessageTask().execute();
				//showResult("---");
			}
		});
	}

	private class LoginPostMessageTask extends AsyncTask<Void, Void, String> {
		
		private MultiValueMap<String, String> message;
		
		@Override
		protected void onPreExecute() {
			showLoadingProgressDialog();
			// assemble the map
			message = new LinkedMultiValueMap<String, String>();
			message.add("name", username.getText().toString());
			message.add("pwd", userpwd.getText().toString());
		}
		
		@Override
		protected String doInBackground(Void... arg0) {
			try{
				// Create a new RestTemplate instance
				RestTemplate restTemplate = new RestTemplate(true);
				//restTemplate.getMessageConverters().add(new MappingJacksonHttpMessageConverter());
				// Make the network request, posting the message, expecting a String in response from the server
				ResponseEntity<String> response = restTemplate.postForEntity(ServerUrl.userLogin, message, String.class);
				// Return the response body to display to the user
				return response.getBody();
			}catch (Exception e) {
				Log.e(TAG, e.getMessage(),e);
			}
			return null;
		}

		@Override
		protected void onPostExecute(String result) {
			dismissProgressDialog();
			showResult(result);
		}
	}

	private void showResult(String result) {
		// && result.trim().equals("SUCCESS")
		if (result != null) {
			// display a notification to the user with the response message
			Toast.makeText(this, result, Toast.LENGTH_LONG).show();
			// go to main page
			startActivity(new Intent(LoginActivity.this, HttpGetJsonActivity.class));
		} else {
			Toast.makeText(this, "I got null, something happened!", Toast.LENGTH_LONG).show();
		}	
	}
}