'use strict';
$(function () {
	calc_price();
	$('.size').on('change', function () {
		calc_price();
	});

	$('.checkbox').on('change', function () {
		calc_price();
	});

	$('#pizza-num').on('change', function () {
		calc_price();
	});

	// 値段の計算をして変更する関数
	function calc_price() {
		let size = $('.size:checked').val();
		let topping_count = $('#topping input:checkbox:checked').length;
		let pizza_num = $('#pizza-num option:selected').val();
		let size_price = 0;
		let topping_price = 0;
		if (size == 'M') {
			size_price = Number(document.getElementById('priceM').value);
			topping_price = 200 * topping_count;
		} else {
			size_price = Number(document.getElementById('priceL').value);
			topping_price = 300 * topping_count;
		}
		let price = (size_price + topping_price) * pizza_num;
		$('#totalPrice').text(price.toLocaleString());
	}
	;
});