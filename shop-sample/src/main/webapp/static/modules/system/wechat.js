var Wechat = Wechat || {};

/**
 * 显示 qqEmo 表情
 */
Wechat.qqEmo = function(target) {
	var emo_click = 'click.emo'
	// 模板
	var template = '<div class="popover popover-bar-pop right" style="display: none;"><div class="arrow" style="top: 61.016949152542374%;"></div><h3 class="popover-title">插入表情</h3><div class="popover-content"><table class="qq-emo-table" style="background-color:#f6fbfe;border-color:#d3e3f0"><tbody><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[微笑]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/0.gif" title="[微笑]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[撇嘴]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/1.gif" title="[撇嘴]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[色]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/2.gif" title="[色]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[发呆]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/3.gif" title="[发呆]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[得意]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/4.gif" title="[得意]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[流泪]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/5.gif" title="[流泪]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[害羞]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/6.gif" title="[害羞]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[闭嘴]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/7.gif" title="[闭嘴]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[睡]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/8.gif" title="[睡]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[大哭]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/9.gif" title="[大哭]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[尴尬]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/10.gif" title="[尴尬]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[发怒]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/11.gif" title="[发怒]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[调皮]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/12.gif" title="[调皮]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[呲牙]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/13.gif" title="[呲牙]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[惊讶]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/14.gif" title="[惊讶]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[难过]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/15.gif" title="[难过]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[酷]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/16.gif" title="[酷]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[冷汗]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/17.gif" title="[冷汗]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[抓狂]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/35.gif" title="[抓狂]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[吐]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/19.gif" title="[吐]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[偷笑]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/20.gif" title="[偷笑]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[愉快]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/21.gif" title="[愉快]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[白眼]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/22.gif" title="[白眼]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[傲慢]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/23.gif" title="[傲慢]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[饥饿]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/24.gif" title="[饥饿]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[困]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/25.gif" title="[困]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[惊恐]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/26.gif" title="[惊恐]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[流汗]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/27.gif" title="[流汗]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[憨笑]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/28.gif" title="[憨笑]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[悠闲]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/29.gif" title="[悠闲]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[奋斗]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/30.gif" title="[奋斗]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[咒骂]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/31.gif" title="[咒骂]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[疑问]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/32.gif" title="[疑问]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[嘘]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/33.gif" title="[嘘]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[晕]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/34.gif" title="[晕]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[衰]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/36.gif" title="[衰]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[骷髅]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/37.gif" title="[骷髅]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[敲打]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/38.gif" title="[敲打]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[再见]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/39.gif" title="[再见]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[擦汗]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/40.gif" title="[擦汗]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[抠鼻]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/41.gif" title="[抠鼻]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[鼓掌]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/42.gif" title="[鼓掌]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[糗大了]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/43.gif" title="[糗大了]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[坏笑]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/44.gif" title="[坏笑]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[左哼哼]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/45.gif" title="[左哼哼]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[右哼哼]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/46.gif" title="[右哼哼]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[哈欠]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/47.gif" title="[哈欠]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[鄙视]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/48.gif" title="[鄙视]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[委屈]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/49.gif" title="[委屈]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[快哭了]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/50.gif" title="[快哭了]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[阴险]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/51.gif" title="[阴险]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[亲亲]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/52.gif" title="[亲亲]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[吓]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/53.gif" title="[吓]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[可怜]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/54.gif" title="[可怜]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[菜刀]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/55.gif" title="[菜刀]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[西瓜]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/56.gif" title="[西瓜]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[啤酒]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/57.gif" title="[啤酒]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[篮球]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/58.gif" title="[篮球]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[乒乓]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/59.gif" title="[乒乓]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[咖啡]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/60.gif" title="[咖啡]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[饭]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/61.gif" title="[饭]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[猪头]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/62.gif" title="[猪头]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[玫瑰]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/63.gif" title="[玫瑰]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[凋谢]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/64.gif" title="[凋谢]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[嘴唇]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/65.gif" title="[嘴唇]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[爱心]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/66.gif" title="[爱心]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[心碎]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/67.gif" title="[心碎]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[蛋糕]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/68.gif" title="[蛋糕]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[闪电]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/69.gif" title="[闪电]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[炸弹]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/70.gif" title="[炸弹]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[刀]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/71.gif" title="[刀]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[足球]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/72.gif" title="[足球]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[瓢虫]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/73.gif" title="[瓢虫]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[便便]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/74.gif" title="[便便]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[月亮]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/75.gif" title="[月亮]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[太阳]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/76.gif" title="[太阳]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[礼物]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/77.gif" title="[礼物]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[拥抱]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/78.gif" title="[拥抱]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[强]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/79.gif" title="[强]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[弱]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/80.gif" title="[弱]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[握手]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/81.gif" title="[握手]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[胜利]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/82.gif" title="[胜利]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[抱拳]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/83.gif" title="[抱拳]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[勾引]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/84.gif" title="[勾引]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[拳头]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/85.gif" title="[拳头]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[差劲]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/86.gif" title="[差劲]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[爱你]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/87.gif" title="[爱你]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[NO]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/88.gif" title="[NO]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[OK]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/89.gif" title="[OK]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[爱情]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/90.gif" title="[爱情]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[飞吻]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/91.gif" title="[飞吻]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[跳跳]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/92.gif" title="[跳跳]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[发抖]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/93.gif" title="[发抖]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[怄火]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/94.gif" title="[怄火]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[转圈]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/95.gif" title="[转圈]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[磕头]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/96.gif" title="[磕头]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[回头]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/97.gif" title="[回头]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[跳绳]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/100.gif" title="[跳绳]"></a></td></tr><tr></tr><tr></tr><tr><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[投降]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/99.gif" title="[投降]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[激动]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/100.gif" title="[激动]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[街舞]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/101.gif" title="[街舞]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[献吻]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/102.gif" title="[献吻]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[左太极]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/103.gif" title="[左太极]"></a></td><td style="cursor:pointer;border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;" data-text="[右太极]"><a><img src="//dn-qncdn1.qbox.me/wxcrm/qqemo/104.gif" title="[右太极]"></a></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td><td style="border:1px solid #d3e3f0;width:32px;height:28px;line-height:28px;text-align:center;"></td></tr></tbody></table></div></div>';
	
	// 显示
	var fadeIn = function() {
		$(template).appendTo('body');
		var height = $('.popover-bar-pop').height();
		var offset = target.offset;
		$('.popover-bar-pop').show().css({top: offset.top - height/2 - 20, left: offset.left + 25}).fadeIn();
	};
	fadeIn();
	
	// 删除
	var fadeOut = function() {
		$('.popover-bar-pop').fadeOut(function() {
			$(this).remove();
			$(document).off(emo_click);
		});
	};
	
	// 点击事件
	$(document).off(emo_click).on(emo_click, function(e) {
		var _target  = e.target || e.srcElement; 
		if ($(_target).closest('.popover-bar-pop').length == 0) {
			fadeOut();
		}
	});
	
    // 具体的表情
	$(document).on(emo_click,  '.popover-bar-pop td[data-text]', function(e) {
		var text = $(this).data('text');
		target.setEmo(text);
		fadeOut();
	});
};

/**
 *  设置链接
 */
Wechat.setLink = function(target) {
	var link_click = 'click.link'
	// 模板
	var template = '<div class="popover popover-bar-pop right" role="tooltip" style="display: none;"><div class="arrow" style="top: 50%;"></div><h3 class="popover-title">插入链接</h3><div class="popover-content"><div><input type="text" class="form-control" style="width:350px;" placeholder="输入链接以http或https开头"><div style="text-align:right;margin-top:9px"><button type="button" class="btn btn-link">取消</button><button type="button" class="btn btn-primary -confirm">确定</button></div></div></div></div>';
	
	// 显示
	var fadeIn = function() {
		$(template).appendTo('body');
		var height = $('.popover-bar-pop').height();
		var offset = target.offset;
		$('.popover-bar-pop').show().css({top: offset.top - height/2 + 5, left: offset.left + 25}).fadeIn();
	};
	fadeIn();
	
	// 删除
	var fadeOut = function() {
		$('.popover-bar-pop').fadeOut(function() {
			$(this).remove();
			$(document).off(link_click);
		});
	};
	
	// 点击事件
	$(document).off(link_click).on(link_click, function(e) {
		var _target  = e.target || e.srcElement; 
		if ($(_target).closest('.popover-bar-pop').length == 0
				|| $(_target).hasClass('btn-link')) {
			fadeOut();
		}
	});
	
    // 具体的表情
	$(document).on(link_click,  '.popover-bar-pop .-confirm', function(e) {
		var text = $(this).closest('.popover-bar-pop').find('.form-control').val();
		if (/^(https?|s?ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i
				.test(text)) {
			target.setEmo('<a href="'+ text + '"></a>');
			fadeOut();
		} else {
			Public.toast('链接格式不正确!');
		}
	});
};


/**
 * 实例化
 */
var Meta_Rich = {

	render : function(id) {
		
		var that = this;
		
		var multi_meta_template =  '<a class="item top-item {{=(meta.relas.length == 0?"single-item":"multi-item")}}" data-id="{{=meta.id}}">' +
		        '<span class="title"><span class="bg"></span><span class="-title">{{=meta.title}}</span></span>' +
		        '<span class="date">{{=meta.createDate}}</span>' +
		        '<span class="image-wrap"><img alt="" src="{{=meta.image}}"></span>' +
			    '<span class="remarks">{{=meta.remarks}}</span>' +
			    '<input type="hidden" class="-config" value="{{=meta.id}}">' +
			 '</a>' +
			 '{{ for(var i= 0; i < meta.relas.length; i++) {var rela = meta.relas[i]; }}' +
			 '<a class="item">' +
			    '<span class="image-wrap"><img alt="" src="{{=rela.relaImage}}"></span>'+
			    '<span class="title">{{=rela.relaName}}</span>' +
			 '</a>' +
			 '{{ } }}';
		
		var html = '';
		Public.getAjax(webRoot + '/admin/wechat/meta/rich/get/' + id, {}, function(data) {
			html = Public.runTemplate(multi_meta_template, {meta: data.obj});
		}, false);
		return html;
	},
	
	// 页面实现
	callback : function(meta) {}
}

/**
 *  初始化设置
 */
$(function() {
	
	// 封装Emo操作对象
	var EmoTarget = function(target, offset) {
		this.offset = offset || {top:0, left: 0};
		this.target = target || {};
		this.setEmo = function(emo) {
			var _text = this.target.val();
			this.target.val(_text + emo);
			this.target.trigger('input');
			this.target.focus();
		};
	};
	
	// 插入表情
	$(document).on('click', '.-qq-emo', function() {
		var offset = $(this).offset();
		var textarea = $(this).closest('.config-content').find('.config-content-textarea').eq(0);
		var target = new EmoTarget(textarea, offset);
		Wechat.qqEmo(target);
	});
	
	// 插入链接
	$(document).on('click', '.-set-link', function() {
		var offset = $(this).offset();
		var textarea = $(this).closest('.config-content').find('.config-content-textarea').eq(0);
		var target = new EmoTarget(textarea, offset);
		Wechat.setLink(target);
	});
	
	// 选择图文(只能选择自己公众号的)
	$(document).on('click', '.meta-select', function() {
		var metas = $(this).closest('.multi-meta').find('.metas');
		Public.tableSelect(webRoot + '/admin/wechat/meta/rich/tableSelect', '选择图文', 800, 500, function(iframe, ids, names) {
    		var id = ids[0];
    		if (!!id) {
    			var html = Meta_Rich.render(id);
    			metas.html(html);
    			Meta_Rich.callback(id);
    		}
    	});
	});
	
	// 查询关键字(只能选择自己公众号的)
	var searchState = false;
	var searchKeyword = function(dom) {
		
		// 保持查询状态 - 延迟查询
		if(searchState) {return;}
		
		// 保证一段时间内只执行一次
		searchState = true;
		
		Public.delayPerform(function() {
			// 延迟获取数据
			var query = $(dom).val();
			if (!!query) {
				var keys = $(dom).closest('.keyword-wrap').find('.keyword-dropdown');
				var appId = $('.apps > .active > a').data('app');
				Public.postAjax(webRoot + '/admin/wechat/meta/keyword/top', {appId: appId, query: query}, function(data) {
					var html = [];
					var _keys = data.obj;
					$.each(_keys, function(index, item) {
						html.push('<a class="keyword" data-keyword="'+item.keyword+'">' + item.hkeyword + '</a>')
					});
					if (html.length != 0) {
						keys.show().html(html.join(''));
					} else {
						keys.show().html('<a class="keyword keyword-null">请在<b>素材管理</b>中定义关键词</a>');
					}
				}, false);
			}
			searchState = false;
		}, 100);
	};
	
	// 查询关键字
	$('.keyword-input').on('input', function() {
		var dom = $(this);
		
		// 查询关键字
		searchKeyword(dom);
	});
	
	// 选择关键字
	$(document).on('click', '.keyword-wrap .keyword', function() {
		var keyword = $(this).data('keyword');
		$(this).closest('.keyword-wrap').find('.-config').val(keyword);
		$(this).closest('.keyword-wrap').find('.keyword-dropdown').hide();
	});
	
	// 选择图片
	$(document).on('click', '.config-pic-select', function() {
		var that = $(this); var p = $(this).closest('.config-pic');
		Attachment.selectAttachments(function(files) {
    		var file = files[0].src;
    		if (!!file) {
    			p.find('img').attr('src', file);
    		}
    	});
	});
	
	// 上传图片
	$(document).on('click', '.config-pic-upload', function() {
		var that = $(this); var p = $(this).closest('.config-pic');
		var img = $(p).find('img').attr('src');
		if (!img) {
			Public.toast('请选择图片');
			return;
		}
		var appId = $('.apps > .active > a').data('app');
		Public.loading('图片上传中...')
		Public.postAjax(webRoot + '/admin/wechat/meta/image/upload', {appId: appId, img: img}, function(data) {
			if (data.success) {
				Public.close();
				Public.success('图片上传成功！');
				$(p).find('.-config').val(data.obj); 
				$(p).find('.-config').trigger('input');
			} else {
				Public.error(data.msg);
			}
		});
	});
	
	// 输入限制
	$('.iInputLimit').inputLimit();
});