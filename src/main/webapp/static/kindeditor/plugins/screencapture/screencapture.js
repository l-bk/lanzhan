KindEditor.plugin('screencapture', function(K)
{
	var editor = this, name = 'screencapture';
	// 点击图标时执行
	editor.clickToolbar(name, function()
	{
		scpMgr.Capture();
	});
});
