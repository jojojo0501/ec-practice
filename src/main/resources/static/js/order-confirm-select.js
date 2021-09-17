'use strict';
$(function () {
	
	$('.payment-select-cash').on('change',function(){
		selectChange();
	});
	$('.payment-select-credit').on('change',function(){
		selectChange();
	});
	
	function selectChange(){
		let paymentStatus = $('input[name="paymentMethod"]:checked').val();
		if(paymentStatus == "2"){
			$('.credit-form').slideDown();
		}
		if(paymentStatus == "1"){
			$('.credit-form').hide();
		}
	};
});