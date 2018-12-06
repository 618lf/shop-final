/**
 *  动态的地址
 */
var CITY = {
	
    // 地址信息 二位数组
	_address : (function(){
		var _a = ChineseDistricts;
		_a['86'] = {
			11 : '北京市',
			12 : '天津市',
			13 : '河北省',
			14 : '山西省',
			15 : '内蒙古自治区',
			21 : '辽宁省',
			22 : '吉林省',
			23 : '黑龙江省',
			31 : '上海市',
			32 : '江苏省',
			33 : '浙江省',
			34 : '安徽省',
			35 : '福建省',
			36 : '江西省',
			37 : '山东省',
			41 : '河南省',
			42 : '湖北省',
			43 : '湖南省',
			44 : '广东省',
			45 : '广西壮族自治区',
			46 : '海南省',
			50 : '重庆市',
			51 : '四川省',
			52 : '贵州省',
			53 : '云南省',
			54 : '西藏自治区',
			61 : '陕西省',
			62 : '甘肃省',
			64 : '宁夏回族自治区',
			63 : '青海省',
			65 : '新疆维吾尔自治区',
		}
		return _a;
	}()),
	
	// 是否初始化
	inited: false,
	
	// 初始化
	_init : function() {
		// 只初始化一次
		if(this.inited) {return;}
		
		this.inited = true;
		
		var $p = this.$p = $('#_p-area');
		var $c = this.$c = $('#_c-area');
		
		var areas = this._address['86'];
		if (!!areas) {
			var _areas = [];
			for (var i in areas) {
				_areas.push('<a class="item" data-code="'+i+'">'+ areas[i] + '</a>')
			}
			$p.html(_areas.join(''));
		};
		
		// 添加事件
		this.addEvent();
		
		// 触发第一个选择
		$p.find('.item').eq(0).click();
	},
	
	// 添加相关事件
	addEvent : function() {
		var self = this;
		$('#city_picker .iScroll').each(function(index, iScroll) {
			var _area_scroll = Public.newScroll(iScroll);
			if ($(this).hasClass('actionSheet-two-iScroll')) {
				self.$c_scroll = _area_scroll;
			}
		});
		
		// 点击
		$(self.$p).on('click.area_item', '.item', function() {
			self.$p.find('.item').removeClass('cur');
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
			self.$c.html(_areas.join(''));
			self.$c_scroll.refresh();
			
			// 选择
			if (_doSelect) {
				self.select();
			}
		});
		
		// 街道选择
		$(self.$c).on('click.street_item', '.item', function() {
			self.$c.find('.item').removeClass('cur');
			$(this).addClass('cur');
			
			// 选择
			self.select();
		});
	},
	
	// 选择
	select : function() {
		var self = this;
		var id = self.$p.find('.item.cur').data('code') + '/' + self.$c.find('.item.cur').data('code');
		var name = self.$p.find('.item.cur').text() + self.$c.find('.item.cur').text();
		!!window.setCity && window.setCity(id, name);
	},
		
	// 当前地址
    doLocation : function() {
    	this._init();
    }
};

/**
 *  动态的地址
 */
var STREET = {
	
    // 地址信息 二位数组
	_address : (function(){
		return ChineseDistricts;
	}()),
	
	// 是否初始化
	inited: false,
	
	// 初始化
	_init : function() {

		// 只初始化一次
		if (!this.inited) {
			this.inited = true;
			this.$d = $('#_d-area');
			this.$s = $('#_s-area');
			// 添加事件
			this.addEvent();
		}
		
		// 每次都会执行
		var areaId = $('#areaId').val();
		if (!areaId) {
			Public.toast('请先选择城市！');
			return;
		}
		var cityCode = areaId.split('/')[1];
		if (!cityCode) {
			Public.toast('请先选择城市！');
			return;
		}
		
		// 保存为当前的区域
		this.areaId = areaId.split('/')[0] + '/' + cityCode;
		
		// 找到数据
		var areas = this._address[cityCode];
		if (!!areas) {
			var _areas = [];
			for (var i in areas) {
				_areas.push('<a class="item" data-code="'+i+'">'+ areas[i] + '</a>')
			}
			this.$d.html(_areas.join(''));
		};
		this.$d_scroll.refresh();
		
		// 触发第一个选择
		this.touchFirst();
	},
	
	// 触发第一个
	touchFirst : function() {
		var self = this; var $item = self.$d.find('.item').eq(0);
		self.$d.find('.item').removeClass('cur');
		$item.addClass('cur');
		var code = $item.data('code');
		var _areas = [];
		var areas = self._address[code];
		if (!!areas) {
			for(var i in areas) {
				_areas.push('<a class="item" data-code="'+i+'">'+ areas[i] + '</a>')
			}
		}
		self.$s.html(_areas.join(''));
		self.$s_scroll.refresh();
	},
	
	// 添加相关事件
	addEvent : function() {
		var self = this;
		$('#street_picker .iScroll').each(function(index, iScroll) {
			var _area_scroll = Public.newScroll(iScroll);
			if ($(this).hasClass('actionSheet-two-iScroll')) {
				self.$s_scroll = _area_scroll;
			} else {
				self.$d_scroll = _area_scroll;
			}
		});
		
		// 点击
		$(self.$d).on('click.area_item', '.item', function() {
			self.$d.find('.item').removeClass('cur');
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
			self.$s.html(_areas.join(''));
			self.$s_scroll.refresh();
			
			// 选择
			if (_doSelect) {
				self.select();
			}
		});
		
		// 街道选择
		$(self.$s).on('click.street_item', '.item', function() {
			self.$s.find('.item').removeClass('cur');
			$(this).addClass('cur');
			
			// 选择
			self.select();
		});
	},
	
	// 选择
	select : function() {
		var self = this;
		var id = self.areaId + '/' + self.$d.find('.item.cur').data('code');
		    id = id + (!!((self.$s.find('.item.cur').data('code'))||'') ? ('/' + self.$s.find('.item.cur').data('code')) : '');
		var name = self.$d.find('.item.cur').text() + self.$s.find('.item.cur').text();
		!!window.setStreet && window.setStreet(id, name);
	},
		
	// 当前地址
    doLocation : function() {
    	this._init();
    }
};