package com.zwh.utils;


import com.aliyun.auth.credentials.Credential;
import com.aliyun.auth.credentials.provider.StaticCredentialProvider;
import com.aliyun.sdk.service.dysmsapi20170525.AsyncClient;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.sdk.service.dysmsapi20170525.models.SendSmsResponse;
import com.google.gson.Gson;
import darabonba.core.client.ClientOverrideConfiguration;

import java.util.concurrent.CompletableFuture;


public class SMSUtils {
	public static void senMsg(String phoneNumbers,String param) throws Exception {

		// HttpClient Configuration
        /*HttpClient httpClient = new ApacheAsyncHttpClientBuilder()
                .connectionTimeout(Duration.ofSeconds(10)) // Set the connection timeout time, the default is 10 seconds
                .responseTimeout(Duration.ofSeconds(10)) // Set the response timeout time, the default is 20 seconds
                .maxConnections(128) // Set the connection pool size
                .maxIdleTimeOut(Duration.ofSeconds(50)) // Set the connection pool timeout, the default is 30 seconds
                // Configure the proxy
                .proxy(new ProxyOptions(ProxyOptions.Type.HTTP, new InetSocketAddress("<your-proxy-hostname>", 9001))
                        .setCredentials("<your-proxy-username>", "<your-proxy-password>"))
                // If it is an https connection, you need to configure the certificate, or ignore the certificate(.ignoreSSL(true))
                .x509TrustManagers(new X509TrustManager[]{})
                .keyManagers(new KeyManager[]{})
                .ignoreSSL(false)
                .build();*/

		// Configure Credentials authentication information, including ak, secret, token
		StaticCredentialProvider provider = StaticCredentialProvider.create(Credential.builder()
				.accessKeyId("")
				.accessKeySecret("")
				//.securityToken("<your-token>") // use STS token
				.build());

		// Configure the Client
		AsyncClient client = AsyncClient.builder()
				.region("cn-hangzhou") // Region ID
				//.httpClient(httpClient) // Use the configured HttpClient, otherwise use the default HttpClient (Apache HttpClient)
				.credentialsProvider(provider)
				//.serviceConfiguration(Configuration.create()) // Service-level configuration
				// Client-level configuration rewrite, can set Endpoint, Http request parameters, etc.
				.overrideConfiguration(
						ClientOverrideConfiguration.create()
								.setEndpointOverride("dysmsapi.aliyuncs.com")
						//.setConnectTimeout(Duration.ofSeconds(30))
				)
				.build();

		// Parameter settings for API request
		SendSmsRequest sendSmsRequest = SendSmsRequest.builder()
				.signName("庄炜浩个人博客")
				.templateCode("SMS_268546045")
				.phoneNumbers(phoneNumbers)
				.templateParam("{\"code\":\""+param+"\"}")
				// Request-level configuration rewrite, can set Http request parameters, etc.
				// .requestConfiguration(RequestConfiguration.create().setHttpHeaders(new HttpHeaders()))
				.build();

		// Asynchronously get the return value of the API request
		CompletableFuture<SendSmsResponse> response = client.sendSms(sendSmsRequest);
		// Synchronously get the return value of the API request
		SendSmsResponse resp = response.get();
		System.out.println(new Gson().toJson(resp));
		// Asynchronous processing of return values
        /*response.thenAccept(resp -> {
            System.out.println(new Gson().toJson(resp));
        }).exceptionally(throwable -> { // Handling exceptions
            System.out.println(throwable.getMessage());
            return null;
        });*/

		// Finally, close the client
		client.close();
	}

}

