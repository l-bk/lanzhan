KindEditor.plugin('netpaster', function(K)
{
	var editor = this, name = 'netpaster';
	// 点击图标时执行
	editor.clickToolbar(name, function()
	{
		pasterMgr.SetEditor(editor);
		pasterMgr.UploadNetImg();
	});
});
