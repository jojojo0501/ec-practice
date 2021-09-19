package jp.co.example.service;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import jp.co.example.domain.CreditCardPaymenResponse;
import jp.co.example.domain.CreditCardPaymentRequest;
import jp.co.example.form.OrderForm;

@Service
public class CreditCardPaymentService {
	
	@Bean
	public RestTemplate setUpRestTemplate() {
		return new RestTemplate();
	}

	@Autowired
	private RestTemplate restTemplate;
	
	// 外部サーバのWEB-APIのURL
		private static final String URL = "http://153.127.48.168:8080/sample-credit-card-web-api/credit-card/payment"; 

		/**
		 * カード決済のWebAPIを呼び出し、結果を含んだオブジェクトを返す.
		 * @param form クレジットカード情報
		 * @return WebAPIのレスポンス情報が入ったドメイン
		 */
		public CreditCardPaymenResponse paymentApiService(OrderForm form) {
			CreditCardPaymentRequest creditCardPaymentRequest = new CreditCardPaymentRequest();
			BeanUtils.copyProperties(form, creditCardPaymentRequest);
			return restTemplate.postForObject(URL, form, CreditCardPaymenResponse.class);
		}
}
