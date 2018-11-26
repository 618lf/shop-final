
/**
 *  固定的地址
 */
var Address = {
	
    // 地址信息 二位数组
	_address : {
		440300 : {
			440301 : '罗湖区',
			440302 : '福田区',
			440303 : '南山区',
			440304 : '宝安区',
			440305 : '龙岗区',
			440306 : '盐田区',
			440307 : '光明新区',
			440308 : '坪山新区',
			440309 : '大鹏新区',
			440310 : '龙华新区',
			440311 : '其它区',
		},
		440301: {
			44030101: '桂园街道',
			44030102: '黄贝街道',
			44030103: '东门街道',
			44030104: '翠竹街道',
			44030105: '南湖街道',
			44030106: '笋岗街道',
			44030107: '东湖街道',
			44030108: '莲塘街道',
			44030109: '东晓街道',
			44030110: '清水河街道',
			44030111: '其它',
		},
		440302: {
			44030201: '南园街道',
			44030202: '园岭街道',
			44030203: '福田街道',
			44030204: '沙头街道',
			44030205: '香蜜湖街道',
			44030206: '梅林街道',
			44030207: '莲花街道',
			44030208: '华富街道',
			44030209: '福保街道',
			44030210: '华强北街道',
			44030211: '福田保税区',
			44030212: '其它'
		},
		440303: {
			44030301: '南头街道',
			44030302: '南山街道',
			44030303: '沙河街道',
			44030304: '蛇口街道',
			44030305: '招商街道',
			44030306: '粤海街道',
			44030307: '桃源街道',
			44030308: '西丽街道',
			44030309: '其它'
		},
		440304: {
			44030401: '新安街道',
			44030402: '光明办事处',
			44030403: '西乡街道',
			44030404: '福永街道',
			44030405: '沙井街道',
			44030406: '松岗街道',
			44030407: '公明办事处',
			44030408: '石岩街道',
			44030409: '观澜街道',
			44030410: '大浪街道',
			44030411: '龙华街道',
			44030412: '民治街道',
			44030413: '宝安国际机场',
			44030414: '其它'
		},
		440305: {
			44030501: '平湖街道',
			44030502: '坪地街道',
			44030503: '坪山街道',
			44030504: '坑梓街道',
			44030505: '葵涌街道',
			44030506: '大鹏街道',
			44030507: '南澳街道',
			44030508: '南湾街道',
			44030509: '坂田街道',
			44030510: '布吉街道',
			44030511: '龙城街道',
			44030512: '龙岗街道',
			44030513: '横岗街道',
			44030514: '大工业区',
			44030515: '其它'
		},
		440306: {
			44030601: '梅沙街道',
			44030602: '盐田街道',
			44030603: '沙头角街道',
			44030604: '海山街道',
			44030605: '市保税区（沙头角）',
			44030606: '市保税区（盐田港）',
			44030607: '其它',
		}
	},
	
	// 是否初始化
	inited: false,
	
	// 初始化
	_init : function() {
		// 只初始化一次
		if(this.inited) {return;}
		
		this.inited = true;
		
		var $area = this.$area = $('#_area');
		var $street = this.$street = $('#_street');
		
		var areas = this._address['440300'];
		if (!!areas) {
			var _areas = [];
			for (var i in areas) {
				_areas.push('<a class="item" data-code="'+i+'">'+ areas[i] + '</a>')
			}
			$area.html(_areas.join(''));
		};
		
		// 添加事件
		this.addEvent();
		
		// 触发第一个选择
		$area.find('.item').eq(0).click();
	},
	
	// 添加相关事件
	addEvent : function() {
		var self = this;
		$('.iScroll').each(function(index, iScroll) {
			var _area_scroll = Public.newScroll(iScroll);
			if ($(this).hasClass('actionSheet-two-iScroll')) {
				self.street_scroll = _area_scroll;
			}
		});
		
		// 点击
		$(self.$area).on('click.area_item', '.item', function() {
			self.$area.find('.item').removeClass('cur');
			$(this).addClass('cur');
			var code = $(this).data('code');
			
			// 构造数据
			var _areas = [];
			var areas = self._address[code];
			var _doSelect = true;
			if (!!areas) {
				for(var i in areas) {
					_areas.push('<a class="item" data-code="'+i+'">'+ areas[i] + '</a>')
				}
				_doSelect = false;
			}
			self.$street.html(_areas.join(''));
			self.street_scroll.refresh();
			
			// 选择
			if (_doSelect) {
				self.select();
			}
		});
		
		// 街道选择
		$(self.$street).on('click.street_item', '.item', function() {
			self.$street.find('.item').removeClass('cur');
			$(this).addClass('cur');
			
			// 选择
			self.select();
		});
		
		// 取消选择
		$(document).on('click', '.weui_mask', function() {
			$('.actionSheet-wrap').hide();
		});
	},
	
	// 选择
	select : function() {
		var self = this;
		var address = self.$area.find('.item.cur').text() + self.$street.find('.item.cur').text();
		!!window.setAddress && window.setAddress(address, '');
	},
		
	// 当前地址
    doLocation : function() {
    	this._init();
    }
};