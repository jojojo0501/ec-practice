$(function() {
	$('.paging-containter').paginathing({//親要素のclassを記述
		perPage: 3,//1ページあたりの表示件数
		prevText:'前へ',//1つ前のページへ移動するボタンのテキスト
		nextText:'次へ',//1つ次のページへ移動するボタンのテキスト
		firstText:'最初',
		lastText:'最後',
		activeClass: 'navi-active',//現在のページ番号に任意のclassを付与できます
	})
});