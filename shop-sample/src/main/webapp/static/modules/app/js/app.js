// 组件
var Component = function(options) {
	
	// 基本设置(具体类型可以根据具体情况设置属性)
	var setting = {
		type : 'config',
		config: "",
		template : "",
		
		// 设置属性
		set : function(options) {
			$.extend(this, options);
		},
		
		// 有效性验证 -- 默认所有字段都要有值
		validate : function() {
			
			var error = false;
			
			// 可提交的属性
			var propertys = this.getProperty();
			
			// 遍历验证
			for(var key in propertys) {
				var e = propertys[key];
				// 数组的长度必须大于0
				if (e instanceof Array) {
					if (e.length == 0) {
						error = true;
						break;
					}
				}
				// 属性不能为空
				else if (!validator.methods.required(e)) {
					error = true;
					break;
				}
			}
			
			// 显示错误信息
			if (error) {
				this.show();
				this.showConfig();
				this.showError();
			}
			
			// 返回错误
			return error;
		},
		
		// 销毁页面
		destory : function() {
			
			// 取消此区域的所有事件
			$(AppDesign.regions.editRegion).off();
		},
		
		// 删除组件
		remove : function() {
			this.destory();
		},
		
		// 展示页面
		show : function() {
			// 有模板就展示
			if (!!this.template && this.template.length > 0) {
				var _self = this;
				var $self = $('#' + _self.id);
				if (!$self.get(0)) {
					var html = Public.runTemplate(this.getTemplate(this.template), _self);
					$(html).appendTo(AppDesign.regions.showRegion);
					$self = $('#' + _self.id);
				} else {
					var html = Public.runTemplate(this.template, _self);
					$self.html(html);
				}
				
				// 子模板(动态加载)
				this.showInnerTemplate($self);
			}
		},
		
		// 显示错误信息
		showError : function() {
			AppDesign.regions.editRegion.find('.errors').show();
		},
		
		// 加载模板
		showInnerTemplate : function($self) {
			
		},
		
		// 激活
	    active : function() {
	    	if (this.type == 'config') {
	    		AppDesign.regions.showRegion.find('.editing').removeClass('editing');
				$(AppDesign.regions.editRegion).parent().css({'margin-top': 0});
	    	} else {
	    		// 配置
		    	var curr = $(AppDesign.regions.showRegion).find('[data-field="'+this.id+'"]');
		    	var top = $(curr).offset().top - $(AppDesign.regions.configRegion).offset().top;
				$(AppDesign.regions.editRegion).parent().css({'margin-top': top});
		    	// 当前显示
		    	AppDesign.regions.showRegion.find('.editing').removeClass('editing');
		    	curr.addClass('editing');
	    	}
	    	
	    	// 显示配置
	    	this.showConfig();
	    	
	    	// 事件支持
	    	this.addEvent();
	    },
	    
	    // 展示配置
		showConfig : function() {
			var _self = this;
	    	var _config = Public.runTemplate(this.config, _self);
	    	
	    	// 显示配置
	    	AppDesign.regions.editRegion.html(_config)
		},
	    
	    // 添加事件
	    addEvent : function() {
	    	
	    },
		
		// 初始化绑定
	    init : function() {
	    	this.show(), this.active();
	    },
	    
	    // 得到组件的属性
	    getProperty : function() {
	    	var self = this;
	    	var object = new Object(); 
	    	for(key in self) {
	    		if (!(key.startWith('_') || typeof(self[key]) === 'function'
	    			 || key === 'config' || key === 'template')) {
	    			object[key] = self[key];
	    		}
	    	}
	    	return object;
	    }
	};
	
	// 保存属性
	return $.extend(true, this, setting, options);
};

// 组件基本属性
Component.prototype = {
	
	// 得到模板
    getTemplate : function(tem) {
        return '<div id="{{=id}}" class="app-field editing clearfix" data-field="{{=id}}">' + tem + '</div>';
    },
    
};

//配置
var config = new Component({
	type : 'config',
	config : $('#config-config').html(),
	
	// 配置属性 
	title : '',
	description : '',
	color : '#f9f9f9',
	footerAble : 0,
	
	// 得到组件的属性
    getProperty : function() {
    	var self = this;
    	var object = new Object(); 
    	for(key in self) {
    		if (!(key.startWith('_') || typeof(self[key]) === 'function' || key === 'type' || key === 'config' || key === 'template')) {
    			object[key] = self[key];
    		}
    	}
    	return object;
    },
	
	// 设置标题
	setTitle : function(title) {
		this.title = title;
		AppDesign.regions.configRegion.find('h1>span').text(title || '微页面标题');
	},
	
	// 设置描述
	setDescription : function(description) {
		this.description = description;
	},
	
	// 设置颜色
	setColor : function(color) {
		this.color = color;
	},
	
	// 设置底部
	setFooterAble : function(isChecked) {
		this.footerAble = isChecked?1:0;
	},
	
	// 添加事件
	addEvent : function() {
		var e = this;
		
		// 重置颜色
	    $(AppDesign.regions.editRegion).off('click.resetbg').on('click.resetbg', '.js-reset-bg', function() {
		    $(this).parent().find('input[type="color"]').val('');
		    e.setColor('');
	    });
	
	    // 颜色变化
	    $(AppDesign.regions.editRegion).off('input.color').on('input.color', 'input[type="color"]', function() {
		    var color = $(this).val();
		    e.setColor(color);
	    });
	    
	    // 标题
	    $(AppDesign.regions.editRegion).off('blur.title').on('blur.title', 'input[name="title"]', function() {
		    var title = $(this).val();
		    e.setTitle(title);
	    });
	    
	    // 描述
	    $(AppDesign.regions.editRegion).off('blur.description').on('blur.description', 'input[name="description"]', function() {
		    var description = $(this).val();
		    e.setDescription(description);
	    });
	    
	    // 底部导航
	    $(AppDesign.regions.editRegion).off('click.footerAble').on('click.footerAble', 'input[name="footerAble"]', function() {
		    var isChecked = $(this).is(':checked');
		    e.setFooterAble(isChecked);
	    });
	    
	    // 标题
	    AppDesign.regions.configRegion.find('h1>span').text(e.title || '微页面标题');
	}
});

// 富文本
var rich_text = new Component({
	type : 'rich_text',
	config : $('#rich_text-config').html(),
	template: $('#rich_text-template').html(),
    
	// 配置属性
	content: '',
	color: '#f9f9f9',
	fullscreen: 0,
	
	// 设置内容
	setContent : function(content) {
		this.content = content;
		this.show();
	},
	
	// 设置颜色
	setColor : function(color) {
		this.color = color;
		this.show();
	},
	
	// 设置是否全屏显示
	setFullscreen : function(fullscreen) {
		this.fullscreen = fullscreen || 0;
		this.show();
	},
	
	// 添加事件
	addEvent : function() {
		
	   var e = this;
	   
	   // 编辑器
	   var id = 'editor_' + Math.random().toString().substr(2);
	   $(".js-editor").attr('id', id);
       this._editor = UE.getEditor(id, {
    	   toolbars: [['fullscreen', 'source', '|', 'undo', 'redo', '|',
			            'bold', 'italic', 'underline', 'fontborder', 'strikethrough', 'forecolor', 'backcolor', '|',
			            'fontfamily', 'fontsize', '|',
			            'justifyleft', 'justifycenter', 'justifyright', 'justifyjustify', '|', 'insertimage'
		   ]],
		   autoClearinitialContent: !1,
		   autoFloatEnabled: !0,
		   wordCount: !1,
		   elementPathEnabled: !1,
		   initialFrameWidth: 396,
		   initialFrameHeight: 600,
		   focus: !1,
		   pasteplain: !1
	   });
       var t = this._editor;
	   t.addListener("blur", function() {
		   e.setContent(t.getContent());
	   }), t.addListener("contentChange", function() {
		   e.setContent(t.getContent());
	   }), t.ready(function() {
		   t.setContent(e.content);
	   });
	   
	   // 重置颜色
	   $(AppDesign.regions.editRegion).off('click.resetbg').on('click.resetbg', '.js-reset-bg', function() {
		   $(this).parent().find('input[type="color"]').val('');
		   e.setColor('');
	   });
	
	   // 颜色变化
	   $(AppDesign.regions.editRegion).off('input.color').on('input.color', 'input[type="color"]', function() {
		   var color = $(this).val();
		   e.setColor(color);
	   });
	   
	   // 全屏设置 
	   $(AppDesign.regions.editRegion).off('click.fullscreen').on('click.fullscreen', 'input[type="checkbox"]', function() {
		   var checked = $(this).is(':checked');
		   e.setFullscreen(checked?1:0);
	   });
	}
});

// 商品组件
var goods = new Component({
	type : 'goods',
	config : ((function() {
		var html = $('#goods-config').html();
		var list = $('#goods_style-config').html();
		return html + '<div class="js-goods-style-region" style="margin-top: 10px;">' + list + '</div>';
	})()),
	template: $('#goods-template').html(),
	
    // 子目录
	showInnerTemplate : function(self) {
		var goods_style = $('#goods_style-template').html();
		var html = Public.runTemplate(goods_style, this);
		self.find('.js-collection-region').html(html);
	},
   
	// 配置属性
	goodss: [],
	size: 2, // 0 大图、 1 小图、 2 一大两小、3 详细列表
	size_type: 0, // 0 卡片样式、1 瀑布流、2 极简样式 、3 促销
	buy_btn: 1, // 1 显示购买按钮, 0 不显示
	buy_btn_type: 1, // 1, 2, 3, 4
	show_sub_title: 0, // 显示商品简介
	show_wish_btn: 0,
	title: 1, // 显示商品名称
	price: 1, // 显示商品价格
	
	// 设置商品
	setGoods : function(goods) {
		this.goodss.push(goods);
		this.show();
		this.showConfig();
	},
	
	// 删除第几个产品
	deleteGoods : function(index) {
		this.goodss.splice(index, 1);
		this.show();
		this.showConfig();
	},
	
	// 设置列表商品数量
	setSize : function(size) {
		this.size = size;
		this.show();
		this.showConfig();
	},
	
	// 设置列表展示样式
	setSize_type : function(size_type) {
		this.size_type = size_type;
		this.show();
		this.showConfig();
	},
	
	// 设置是否显示购买按钮
	setBuy_btn : function(buy_btn) {
		this.buy_btn = buy_btn;
		this.show();
		this.showConfig();
	},
	
	// 设置购买按钮样式
	setBuy_btn_type : function(buy_btn_type) {
		this.buy_btn_type = buy_btn_type;
		this.show();
	},
	
	// 设置是否显示商品名称
	setTitle : function(show_title) {
		this.title = show_title;
		this.show();
	},
	
	// 设置是否显示商品简述
	setShow_sub_title : function(show_sub_title) {
		this.show_sub_title = show_sub_title;
		this.show();
	},
	
	// 设置是否显示商品价格
	setPrice : function(show_price) {
		this.price = show_price;
		this.show();
	},
	
	// 添加自定义事件
	addCustomEvent : function() {
		
	},
	
	// 添加事件
	addEvent : function() {
		
		var e = this;
		
		// 列表样式
	    $(AppDesign.regions.editRegion).off('click.size').on('click.size', 'input[name="size"]', function() {
		    var size = $(this).val();
		    e.setSize(size);
	    });
	    
	    // 卡片样式
	    $(AppDesign.regions.editRegion).off('click.size_type').on('click.size_type', 'input[name="size_type"]', function() {
		    var size = $(this).val();
		    e.setSize_type(size);
	    });
	    
	    // 购买按钮
	    $(AppDesign.regions.editRegion).off('click.buy_btn').on('click.buy_btn', 'input[name="buy_btn"]', function() {
	    	var checked = $(this).is(':checked');
		    e.setBuy_btn(checked?1:0);
	    });
	    
	    // 购买按钮样式
	    $(AppDesign.regions.editRegion).off('click.buy_btn_type').on('click.buy_btn_type', 'input[name="buy_btn_type"]', function() {
	    	var size = $(this).val();
		    e.setBuy_btn_type(size);
	    });
	    
	    // 商品介绍
	    $(AppDesign.regions.editRegion).off('click.show_sub_title').on('click.show_sub_title', 'input[name="show_sub_title"]', function() {
	    	var checked = $(this).is(':checked');
		    e.setShow_sub_title(checked?1:0);
	    });
	    
	    // 商品价格
	    $(AppDesign.regions.editRegion).off('click.price').on('click.price', 'input[name="price"]', function() {
	    	var checked = $(this).is(':checked');
		    e.setPrice(checked?1:0);
	    });
	    
	    // 商品名称
	    $(AppDesign.regions.editRegion).off('click.title').on('click.title', 'input[name="title"]', function() {
	    	var checked = $(this).is(':checked');
		    e.setTitle(checked?1:0);
	    });
	    
	    // 选择商品
	    $(AppDesign.regions.editRegion).off('click.add-goods').on('click.add-goods', '.js-add-goods', function() {
	    	// 选择商品
	    	Shop.selectGoods(function(goods) {
	    		e.setGoods({
	    			id : goods.id,
	    			title : goods.name,
	    			sub_title : goods.fullName,
	    			price: goods.price,
	    			image: goods.image,
	    			url: goods.url
	    		});
	    	});
	    });
	    
	    // 删除商品
	    $(AppDesign.regions.editRegion).off('click.delete-goods').on('click.delete-goods', '.js-delete-goods', function() {
	    	var index = $(this).closest('li').data('index');
	    	e.deleteGoods(index);
	    });
	    
	    // 自定义事件
	    this.addCustomEvent();
	}
});

// 继承于 goods
var goods_list = $.extend(true, {}, goods, {
	type : 'goods_list',
	config : ((function() {
		var html = $('#goods_list-config').html();
		var list = $('#goods_style-config').html();
		return html + '<div class="js-goods-style-region" style="margin-top: 10px;">' + list + '</div>';
	})()),
	template: $('#goods-template').html(),
	
	goods: '',
	goodss: [{id:'-1', title:'此处显示商品名称', sub_title:'第一个商品', price:'99.00', image:'/static/modules/app/img/goods-one.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第二个商品', price:'99.00', image:'/static/modules/app/img/goods-two.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第三个商品', price:'99.00', image:'/static/modules/app/img/goods-three.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第n个商品', price:'99.00', image:'/static/modules/app/img/goods-n.jpg', url : ''}],
	goods_number_type: 0,
	size: 1, // 0 大图、 1 小图、 2 一大两小、3 详细列表
});

//图片广告
var image_ad = new Component({
	type : 'image_ad',
	config : $('#image_ad-config').html(),
	template: $('#image_ad-template').html(),
	
	show_method: 0, // 0 折叠轮播、 1 分开显示
	size: 0, // 0 大图、 1 小图
	sub_entry : [],
	
	// 重写配置组件
	showConfig : function() {
		var _self = this;
    	var _config = Public.runTemplate(this.config, _self);
    	
    	// 显示配置
    	AppDesign.regions.editRegion.html(_config)
    	
    	// 显示具体的图片配置
    	var _images = this.sub_entry; var image_template = $('#choice_image-config').html();
    	if (_images.length > 0) {
    		var htmls = []; htmls.push('<ul class="choices ui-sortable">')
    		for(var i = 0; i< _images.length; i++) {
    			var image = _images[i]; image.ci = i;
    			var _html = Public.runTemplate(image_template, image);
    			htmls.push('<li class="choice" data-id="'+image.image_id+'">');
    			htmls.push(_html);
    			htmls.push('</li>');
    		}
    		htmls.push('</ul>')
    		AppDesign.regions.editRegion.find('.js-choices-region').append(htmls.join(''));
    	}
	},
	
	// 设置显示方式
	setShow_method : function(show_method) {
		this.show_method = show_method;
		this.show();
		this.showConfig();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 设置图片大小
	setSize : function(size) {
		this.size = size;
		this.show();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 设置标题(指定了哪张图)
	setImage : function(index, image) {
		var index = this.getIndex(index);
		var _image = this.sub_entry[index];
		_image.image_url = image;
		this.show();
		this.showConfig();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 设置标题(指定了哪张图)
	setTitle : function(index, title) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.image_title = title;
		}
		this.show();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 添加图片
	addImage : function(image) {
		this.sub_entry.push({
			image_id : 'image_' + Math.random().toString().substr(2),
			image_url : image,
			image_title : '',
		});
		this.show();
		this.showConfig();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 复制图片
	copyImage : function(image) {
		var index = this.getIndex(image);
		var _image = this.sub_entry[index];
		var _copy = _.clone(_image);
		    _copy.image_id = 'image_' + Math.random().toString().substr(2);
		    this.sub_entry.splice(index, 0, _copy);
		this.show();
		this.showConfig();
		
		// 实例化
	    this.renderSwiper();
	},
	
	// 根据ID获取index
	getIndex : function(id) {
        for(var i = 0; i < this.sub_entry.length; i++) {
        	if (this.sub_entry[i].image_id === id) {
        		return i;
        	}
        }
        return -1;
	},
	
	// 删除
	delImage : function(image) {
		// 删除
		var index = this.getIndex(image);
		this.sub_entry.splice(index, 1);
		this.show();
		this.showConfig();
	    this.renderSwiper();
	},
	
	// 设置链接地址
	setLink : function(image, link) {
		var index = this.getIndex(image);
		var _image = this.sub_entry[index];
		_image.link_title = link.link_title;
		_image.link_type = link.link_type;
		_image.link_url = link.link_url;
		this.show();
		this.showConfig();
	    this.renderSwiper();
	},
	
	// 添加事件
	addEvent : function() {
		var e = this;
		
		// 显示方式
	    $(AppDesign.regions.editRegion).off('click.show_method').on('click.show_method', 'input[name="show_method"]', function() {
	    	var size = $(this).val();
		    e.setShow_method(size);
	    });
	    
	    // 图片大小
	    $(AppDesign.regions.editRegion).off('click.size').on('click.size', 'input[name="size"]', function() {
	    	var size = $(this).val();
		    e.setSize(size);
	    });
	    
	    // 添加图片
	    $(AppDesign.regions.editRegion).off('click.js-add-option').on('click.js-add-option', '.js-add-option', function() {
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.addImage(image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 替换图片
	    $(AppDesign.regions.editRegion).off('click.js-trigger-image').on('click.js-trigger-image', '.js-trigger-image', function() {
	    	var index = $(this).closest('.choice').data('id');
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.setImage(index, image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.title').on('blur.title', 'input[name^="title"]', function() {
	    	var title = $(this).val();
	    	var name = $(this).attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	e.setTitle(num, title);
	    });
	    
	    // 复制
	    $(AppDesign.regions.editRegion).off('click.action_add').on('click.action_add', '.action.add', function() {
	    	var index = $(this).closest('.choice').data('id');
	    	e.copyImage(index);
	    });
	    
	    // 删除
	    $(AppDesign.regions.editRegion).off('click.action_delete').on('click.action_delete', '.action.delete', function() {
	    	var index = $(this).closest('.choice').data('id');
	    	e.delImage(index);
	    });
	    
	    // 删除链接
	    $(AppDesign.regions.editRegion).off('click.js-delete-link').on('click.js-delete-link', '.js-delete-link', function() {
	    	var index = $(this).closest('.choice').data('id');
	    	e.setLink(index, {
	    		link_title : '',
	    		link_type : '',
	    		link_url : ''
	    	});
	    });
	    
	    // 设置链接
	    $(AppDesign.regions.editRegion).off('click.js-trigger-link').on('click.js-trigger-link', '.js-trigger-link', function() {
	    	var type = $(this).data('type');
	    	var index = $(this).closest('.choice').data('id');
	    	Business.selectLink(type, function(data) {
	    		data.link_type = type;
	    		e.setLink(index, data);
	    	});
	    });
	    
	    // 实例化
	    this.renderSwiper();
	},
	
	// 实例化swiper 组件
	renderSwiper : function() {
		
		!!this.swiper && this.swiper.destroy();
		
		if (this.show_method != 0 || this.sub_entry.length == 0) {return;}
		
		var id = this.id;
		$('#' + id).find('.swiper-container').each(function() {
			var _swiper = $(this); var _pagination = '#' + id +'-swiper-pagination';
			// 实例化组件
			var swiper = new Swiper(_swiper, {
		        pagination: _pagination,
		        slidesPerView: 'auto',
		        centeredSlides: true,
		        loop:true,
		        autoplay: 2000,
		    });
			
			this.swiper = swiper;
		});
	}
});

// 标题
var title = new Component({
	type : 'title',
	config : $('#title-config').html(),
	template: $('#title-template').html(),
	
	// 属性
	title_template : 0,
	show_method: 0,
	title: '',
	sub_title: '',
	color: '',
	wx_title_date: '',
	wx_title_author: '',
	wx_title_link: '',
	wx_title_link_type: '',
	sub_entry: [],
	
	// 重写配置
	showConfig : function() {
		var _self = this;
    	var _config = Public.runTemplate(this.config, _self);
    	
    	// 显示配置
    	AppDesign.regions.editRegion.html(_config)
    	
    	// 显示具体的图片配置
    	var _images = _self.sub_entry; var image_template = $('#title-item-config').html();
    	if (_self.title_template == 0 && _images.length > 0) {
    		var htmls = []; htmls.push('<ul class="choices ui-sortable">')
    		for(var i = 0; i< _images.length; i++) {
    			var image = _images[i]; image.ci = i;
    			var _html = Public.runTemplate(image_template, image);
    			htmls.push('<li class="choice" data-id="'+image.id+'">');
    			htmls.push(_html);
    			htmls.push('</li>');
    		}
    		htmls.push('</ul>')
    		AppDesign.regions.editRegion.find('.js-collection-region').append(htmls.join(''));
    	}
	},
	
	// 设置标题
	setTitle: function(title) {
		this.title = title;
		this.show();
	},
	
	// 副标题
	setSub_title : function(sub_title) {
		this.sub_title = sub_title;
		this.show();
	},
	
	// 设置显示
	setShow_method : function(show_method) {
		this.show_method = show_method;
		this.show();
	},
	
	// 设置颜色
	setColor : function(color) {
		this.color = color;
		this.show();
	},
	
	// 设置显示模式
	setTitle_template : function(title_template) {
		this.title_template = title_template;
		this.show();
		this.showConfig();
	},
	
	// 设置日期
	setWx_title_date : function(wx_title_date) {
		this.wx_title_date = wx_title_date;
		this.show();
	},
	
	// 设置作者
	setWx_title_author : function(wx_title_author) {
		this.wx_title_author = wx_title_author;
		this.show();
	},
	
	// 设置链接
	setWx_title_link : function(wx_title_link) {
		this.wx_title_link = wx_title_link;
		this.show();
	},
	
	// 设置链接类型
	setWx_title_link_type : function(wx_title_link_type) {
		this.wx_title_link_type = wx_title_link_type;
		this.show();
		this.showConfig();
	},
	
	// 添加图片
	addTitleNav : function() {
		this.sub_entry.push({
			id : 'nav_' + Math.random().toString().substr(2),
			url : '',
			title : '',
		});
		this.show();
		this.showConfig();
	},
	
	// 设置导航标题
	setNavTitle : function(index, title) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.title = title;
		}
		this.show();
	},
	
	// 设置链接
	setLink : function(link) {
		if (this.wx_title_link_type == 1) {
			this.wx_link_url = link.link_url;
			this.wx_title_link = link.link_title;
			this.wx_link_type = link.link_type;
		} else {
			var image = this.sub_entry[0];
			if (!!image) {
				image.link_title = link.link_title;
				image.link_url = link.link_url;
				image.link_type = link.link_type;
			}
		}
		this.show();
		this.showConfig();
	},
	
	// 添加相关事件
	addEvent: function() {
		
		var e = this;
		
		// 设置标题
	    $(AppDesign.regions.editRegion).off('blur.title').on('blur.title', 'input[name="title"]', function() {
	    	var title = $(this).val();
	    	e.setTitle(title);
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.sub_title').on('blur.sub_title', 'input[name="sub_title"]', function() {
	    	var title = $(this).val();
	    	e.setSub_title(title);
	    });
	    
	    // 设置显示模式
	    $(AppDesign.regions.editRegion).off('click.show_method').on('click.show_method', 'input[name="show_method"]', function() {
	    	var title = $(this).val();
	    	e.setShow_method(title);
	    });
	    
	    // 重置颜色
	    $(AppDesign.regions.editRegion).off('click.resetbg').on('click.resetbg', '.js-reset-bg', function() {
		    $(this).parent().find('input[type="color"]').val('');
		    e.setColor('');
	    });
	
	    // 颜色变化
	    $(AppDesign.regions.editRegion).off('input.color').on('input.color', 'input[type="color"]', function() {
		    var color = $(this).val();
		    e.setColor(color);
	    });
	    
	    // 切换显示模式
	    $(AppDesign.regions.editRegion).off('click.title_template').on('click.title_template', 'input[name="title_template"]', function() {
		    var title_template = $(this).val();
		    e.setTitle_template(title_template);
	    });
	    
	    // 设置日期
	    $(AppDesign.regions.editRegion).off('blur.wx_title_date').on('blur.wx_title_date', 'input[name="wx_title_date"]', function() {
	    	var title = $(this).val();
	    	e.setWx_title_date(title);
	    });
	    
	    // 设置作者
	    $(AppDesign.regions.editRegion).off('blur.wx_title_author').on('blur.wx_title_author', 'input[name="wx_title_author"]', function() {
	    	var title = $(this).val();
	    	e.setWx_title_author(title);
	    });
	    
	    // 链接标题
	    $(AppDesign.regions.editRegion).off('blur.wx_title_link').on('blur.wx_title_link', 'input[name="wx_title_link"]', function() {
	    	var title = $(this).val();
	    	e.setWx_title_link(title);
	    });
	    
	    // 链接类型
	    $(AppDesign.regions.editRegion).off('click.wx_title_link_type').on('click.wx_title_link_type', 'input[name="wx_title_link_type"]', function() {
	    	var title = $(this).val();
	    	e.setWx_title_link_type(title);
	    });
	    
	    // 添加导航
	    $(AppDesign.regions.editRegion).off('click.js-add-option').on('click.js-add-option', '.js-add-option', function() {
	    	e.addTitleNav();
	    });
	    
	    // 设置导航标题
	    $(AppDesign.regions.editRegion).off('blur.js-collection-region_title').on('blur.js-collection-region_title', '.js-collection-region input[name^="title"]', function() {
	    	var title = $(this).val();
	    	var name = $(this).attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	e.setNavTitle(num, title);
	    });
	    
	    // 设置链接
	    $(AppDesign.regions.editRegion).off('click.js-trigger-link').on('click.js-trigger-link', '.js-trigger-link', function() {
	    	var type = $(this).data('type');
	    	Business.selectLink(type, function(data) {
	    		data.link_type = type;
	    		e.setLink(data);
	    	});
	    });
	}
	
});

// 文本导航
var text_nav = new Component({
	type : 'text_nav',
	config : $('#text_nav-config').html(),
	template: $('#text_nav-template').html(),
	
	// 属性
	show_method : 0,
	sub_entry: [],
	
	// 设置显示模式
	setShow_method : function(show_method) {
		this.show_method = show_method;
		this.show();
	},
	
	// 添加图片
	addTextNav : function() {
		this.sub_entry.push({
			id : 'nav_' + Math.random().toString().substr(2),
			url : '',
			title : '',
		});
		this.show();
		this.showConfig();
	},
	
	// 设置导航标题
	setNavTitle : function(index, title) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.title = title;
		}
		this.show();
	},
	
	// 设置链接
	setLink : function(index, link) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.link_title = link.link_title;
			image.link_url = link.link_url;
			image.link_type = link.link_type;
		}
		this.show();
		this.showConfig();
	},
	
	// 重写配置
	showConfig : function() {
		var _self = this;
    	var _config = Public.runTemplate(this.config, _self);
    	
    	// 显示配置
    	AppDesign.regions.editRegion.html(_config)
    	
    	// 显示具体的图片配置
    	var _images = _self.sub_entry; var image_template = $('#text_nav-item-config').html();
    	if (_images.length > 0) {
    		var htmls = []; htmls.push('<ul class="choices ui-sortable">')
    		for(var i = 0; i< _images.length; i++) {
    			var image = _images[i]; image.ci = i;
    			var _html = Public.runTemplate(image_template, image);
    			htmls.push('<li class="choice" data-id="'+image.id+'">');
    			htmls.push(_html);
    			htmls.push('</li>');
    		}
    		htmls.push('</ul>')
    		AppDesign.regions.editRegion.find('.js-collection-region').append(htmls.join(''));
    	}
	},
	
	// 添加事件
	addEvent : function() {
		
		var e = this;
		
		// 添加导航
	    $(AppDesign.regions.editRegion).off('click.js-add-option').on('click.js-add-option', '.js-add-option', function() {
	    	e.addTextNav();
	    });
	    
	    // 设置导航标题
	    $(AppDesign.regions.editRegion).off('blur.js-collection-region_title').on('blur.js-collection-region_title', '.js-collection-region input[name^="title"]', function() {
	    	var title = $(this).val();
	    	var name = $(this).attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	e.setNavTitle(num, title);
	    });
	    
	    // 设置显示模式
	    $(AppDesign.regions.editRegion).off('click.show_method').on('click.show_method', 'input[name="show_method"]', function() {
	    	var value = $(this).val();
	    	e.setShow_method(value);
	    });
	    
	    // 设置链接
	    $(AppDesign.regions.editRegion).off('click.js-trigger-link').on('click.js-trigger-link', '.js-trigger-link', function() {
	    	var name = $(this).closest('.choice').find('input').attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	var type = $(this).data('type');
	    	Business.selectLink(type, function(data) {
	    		data.link_type = type;
	    		e.setLink(num, data);
	    	});
	    });
	}
	
});

//图片导航
var image_nav = new Component({
	type : 'image_nav',
	config : $('#image_nav-config').html(),
	template: $('#image_nav-template').html(),
	
	// 属性
	sub_entry: [{image_id:'',}, {image_id:'',}, {image_id:'',}, {image_id:'',}],
	
	// 重写配置
	showConfig : function() {
		var _self = this;
    	var _config = Public.runTemplate(this.config, _self);
    	
    	// 显示配置
    	AppDesign.regions.editRegion.html(_config)
    	
    	// 显示具体的图片配置
    	var _images = _self.sub_entry; var image_template = $('#image_nav-item-config').html();
    	if (_images.length > 0) {
    		var htmls = []; htmls.push('<ul class="choices ui-sortable">')
    		for(var i = 0; i< _images.length; i++) {
    			var image = _images[i]; image.ci = i;
    			var _html = Public.runTemplate(image_template, image);
    			htmls.push('<li class="choice" data-id="'+image.image_id+'">');
    			htmls.push(_html);
    			htmls.push('</li>');
    		}
    		htmls.push('</ul>')
    		AppDesign.regions.editRegion.find('.js-collection-region').append(htmls.join(''));
    	}
	},
	
	// 设置图片
	setImage : function(index, image) {
		var _image = this.sub_entry[index];
		if (!!_image) {
			_image.image_id = 'image_' + Math.random().toString().substr(2);
			_image.image_url = image;
		}
		this.show();
		this.showConfig();
	},
	
	// 设置标题
	setTitle : function(index, title) {
		var _image = this.sub_entry[index];
		if (!!_image) {
			_image.title = title;
		}
		this.show();
	},
	
	// 设置链接
	setLink : function(index, link) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.link_title = link.link_title;
			image.link_url = link.link_url;
			image.link_type = link.link_type;
		}
		this.show();
		this.showConfig();
	},
	
	// 添加事件
	addEvent : function() {
		
		var e = this;
		
		// 选择图片
		$(AppDesign.regions.editRegion).off('click.js-trigger-image').on('click.js-trigger-image', '.js-trigger-image', function() {
	    	var index = $(this).data('index');
			Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.setImage(index, image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.title').on('blur.title', 'input[name^="title"]', function() {
	    	var title = $(this).val();
	    	var name = $(this).attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	e.setTitle(num, title);
	    });
	    
	    // 设置链接
	    $(AppDesign.regions.editRegion).off('click.js-trigger-link').on('click.js-trigger-link', '.js-trigger-link', function() {
	    	var name = $(this).closest('.choice-content').find('input').attr('name');
	    	var num = /^title\[(\d+)\]$/.test(name);
	    	    num = RegExp.$1;
	    	var type = $(this).data('type');
	    	Business.selectLink(type, function(data) {
	    		data.link_type = type;
	    		e.setLink(num, data);
	    	});
	    });
	}
});

//商品搜索
var goods_search = new Component({
	type : 'goods_search',
	config : $('#goods_search-config').html(),
	template: $('#goods_search-template').html(),
	
	color: '',
	
	setColor : function(color) {
		this.color = color;
		this.show();
	},
	
    addEvent : function() {
		
		var e = this;
		
		// 颜色变化
	    $(AppDesign.regions.editRegion).off('input.color').on('input.color', 'input[type="color"]', function() {
		    var color = $(this).val();
		    e.setColor(color);
	    });
	    
	    // 重置颜色
	    $(AppDesign.regions.editRegion).off('click.resetbg').on('click.resetbg', '.js-reset-bg', function() {
		    $(this).parent().find('input[type="color"]').val('');
		    e.setColor('#e5e5e5');
	    });
	}
});

//线
var line = new Component({
	type : 'line',
	config : $('#line-config').html(),
	template: $('#line-template').html(),
	
	color: '#e5e5e5',
	hasPadding: false,
	lineType: 'solid',
	
	setColor : function(color) {
		this.color = color;
		this.show();
	},
	
	setHasPadding : function(hasPadding) {
		this.hasPadding = hasPadding;
		this.show();
	},
	
	setLineType : function(lineType) {
		this.lineType = lineType;
		this.show();
	},
	
	addEvent : function() {
		
		var e = this;
		
		// 颜色变化
	    $(AppDesign.regions.editRegion).off('input.color').on('input.color', 'input[type="color"]', function() {
		    var color = $(this).val();
		    e.setColor(color);
	    });
	    
	    // 重置颜色
	    $(AppDesign.regions.editRegion).off('click.resetbg').on('click.resetbg', '.js-reset-bg', function() {
		    $(this).parent().find('input[type="color"]').val('');
		    e.setColor('#e5e5e5');
	    });
	    
	    $(AppDesign.regions.editRegion).off('click.hasPadding').on('click.hasPadding', 'input[name="hasPadding"]', function() {
		    var checked = $(this).is(':checked');
		    e.setHasPadding(checked?1:0);
	    });
	    
	    $(AppDesign.regions.editRegion).off('click.lineType').on('click.lineType', 'input[name="lineType"]', function() {
		    var lineType = $(this).val();
		    e.setLineType(lineType);
	    });
	}
});

//空白
var space = new Component({
	type : 'space',
	config : $('#space-config').html(),
	template: $('#space-template').html(),
	
	height: 8,
	
	setHeight: function(height) {
		this.height = height;
		this.show();
	},
	
	// 设置数据
	renderSlider : function() {
		
		var e = this;
		var $js_height = AppDesign.regions.editRegion.find('.js-height');
		AppDesign.regions.editRegion.find('.js-slider').slider({
			step: 2,
			min: 8,
			max: 100,
			value: e.height,
			formatter: function(value) {
				return '像素: ' + value;
			}
		}).on('slide', function(slideEvt) {
			$js_height.text(slideEvt.value);
			e.setHeight(slideEvt.value);
		});
	},
	
	// 清除组件
	destory: function() {
		// 取消此区域的所有事件
		$(AppDesign.regions.editRegion).off();
		AppDesign.regions.editRegion.find('.js-slider').slider('destroy');
	},
	
	// 添加事件
	addEvent : function() {
		this.renderSlider();
	}
});

//橱窗
var showcase = new Component({
	type : 'showcase',
	config : $('#showcase-config').html(),
	template: $('#showcase-template').html(),
	
	title: '',
	mode:'0',
	without_space: '0',
	body_title: '',
	body_desc: '',
	
	// 属性
	sub_entry: [{image_id:'',}, {image_id:'',}, {image_id:'',}],
	
	// 重写配置
	showConfig : function() {
		var _self = this;
    	var _config = Public.runTemplate(this.config, _self);
    	
    	// 显示配置
    	AppDesign.regions.editRegion.html(_config)
    	
    	// 显示具体的图片配置
    	var _images = _self.sub_entry; var image_template = $('#showcase-item-config').html();
    	if (_images.length > 0) {
    		var htmls = []; htmls.push('<ul class="choices ui-sortable">')
    		for(var i = 0; i< _images.length; i++) {
    			var image = _images[i]; image.ci = i;
    			var _html = Public.runTemplate(image_template, image);
    			htmls.push('<li class="choice" data-id="'+image.image_id+'" data-index="'+i+'">');
    			htmls.push(_html);
    			htmls.push('</li>');
    		}
    		htmls.push('</ul>')
    		AppDesign.regions.editRegion.find('.js-collection-region').append(htmls.join(''));
    	}
	},
	
	// 设置图片
	setImage : function(index, image) {
		var _image = this.sub_entry[index];
		if (!!_image) {
			_image.image_id = 'image_' + Math.random().toString().substr(2);
			_image.image_url = image;
		}
		this.show();
		this.showConfig();
	},
	
	// 设置标题
	setTitle : function(title) {
		this.title = title;
		this.show();
	},
	
	// 设置标题
	setMode : function(mode) {
		this.mode = mode;
		this.show();
	},
	
	// 设置链接
	setLink : function(index, link) {
		var image = this.sub_entry[index];
		if (!!image) {
			image.link_title = link.link_title;
			image.link_url = link.link_url;
			image.link_type = link.link_type;
		}
		this.show();
		this.showConfig();
	},
	
	// 设置空白
	setWithout_space : function(without_space) {
		this.without_space = without_space;
		this.show();
	},
	
	// 设置标题
	setBody_title : function(body_title) {
		this.body_title = body_title;
		this.show();
	},
	
	// 设置描述
	setBody_desc : function(body_desc) {
		this.body_desc = body_desc;
		this.show();
	},
	
	// 添加事件
	addEvent : function() {
		
		var e = this;
		
		// 选择图片
		$(AppDesign.regions.editRegion).off('click.js-trigger-image').on('click.js-trigger-image', '.js-trigger-image', function() {
	    	var index = $(this).data('index');
			Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.setImage(index, image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.title').on('blur.title', 'input[name="title"]', function() {
	    	var title = $(this).val();
	    	e.setTitle(title);
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('click.mode').on('click.mode', 'input[name="mode"]', function() {
	    	var mode = $(this).val();
	    	e.setMode(mode);
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('click.without_space').on('click.without_space', 'input[name="without_space"]', function() {
	    	var without_space = $(this).val();
	    	e.setWithout_space(without_space);
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.body_title').on('blur.body_title', 'input[name="body_title"]', function() {
	    	var title = $(this).val();
	    	e.setBody_title(title);
	    });
	    
	    // 设置标题
	    $(AppDesign.regions.editRegion).off('blur.body_desc').on('blur.body_desc', 'textarea[name="body_desc"]', function() {
	    	var title = $(this).val();
	    	e.setBody_desc(title);
	    });
	    
	    // 设置链接
	    $(AppDesign.regions.editRegion).off('click.js-trigger-link').on('click.js-trigger-link', '.js-trigger-link', function() {
	    	var index = $(this).closest('.choice').data('index');
	    	var type = $(this).data('type');
	    	Business.selectLink(type, function(data) {
	    		data.link_type = type;
	    		e.setLink(index, data);
	    	});
	    });
	}
	
});

//继承于 goods
var tag_list = $.extend(true, {}, goods, {
	type : 'tag_list',
	config : ((function() {
		var html = $('#tag_list-config').html();
		var list = $('#goods_style-config').html();
		return html + '<div class="js-goods-style-region" style="margin-top: 10px; {{=tag_group=="tag_list"?"display:none":""}}">' + list + '</div>';
	})()),
	template: $('#tag_list-template').html(),
	
	goodss: [{id:'-1', title:'此处显示商品名称', sub_title:'第一个商品', price:'99.00', image:'/static/modules/app/img/goods-one.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第二个商品', price:'99.00', image:'/static/modules/app/img/goods-two.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第三个商品', price:'99.00', image:'/static/modules/app/img/goods-three.jpg', url : ''}, {id:'-1', title:'此处显示商品名称', sub_title:'第n个商品', price:'99.00', image:'/static/modules/app/img/goods-n.jpg', url : ''}],
	sub_entrys:[],
	tag_group: 'tag_list',
	size: 1, // 0 大图、 1 小图、 2 一大两小、3 详细列表
	
	setTag_group : function(tag_group) {
		this.tag_group = tag_group;
		this.show();
		this.showConfig();
	},
	
	// 添加事件
	addCustomEvent : function() {
		var e = this;
		
		// 设置标题
	    $(AppDesign.regions.editRegion).off('click.tag_group').on('click.tag_group', 'input[name="tag_group"]', function() {
	    	var title = $(this).val();
	    	e.setTag_group(title);
	    });
	},
});

//魔方
var cube = new Component({
	type : 'cube',
	config : $('#cube-config').html(),
	template: $('#cube-template').html(),
	
	// 默认配置
	defaults: function() {
		return {
			type: "cube",
			layout_width: 4,
			layout_height: 4,
		}
	},
	
	// 配置内容
	layout_width: 4,
	layout_height: 4,
	tableContent: "",
	sub_entry: [],
	layoutMap : [],
	currentSelection : -1,
	previousSelection : -1,
	
	// 设置初始属性值
	set : function(options) {
		
		var self = this;
		
		// sub_entry 中存储的是自定义对象
		var _sub_entry = options.sub_entry;
		delete options.sub_entry
		$.extend(this, options);
		
		self.sub_entry = [];
		
		// 构造对象
		if (!!_sub_entry && _sub_entry.length > 0) {
			$.each(_sub_entry, function(n, e) {
				e.parent = self;
				var p = new self.models_item(e);
				self.sub_entry.push(p);
			});
		}
		
		// 计算tableContent
		this.denerateTableContent();
	},
	
	// 得到组件的属性
    getProperty : function() {
    	var self = this;
    	var object = new Object(); 
    	for(key in self) {
    		if (!(key.startWith('_') || typeof(self[key]) === 'function'
    			 || key === 'config' || key === 'template' || key === 'tableContent'
    			 || key === 'layoutMap' || key === 'currentSelection' || key === 'previousSelection' || key === 'index')) {
    			if (key === 'sub_entry') {
    				var _key = key;
    				var sub_entry = self[key];
    				var _sub_entry = [];
    				$.each(sub_entry, function(n, e) {
    					_sub_entry.push(e.getProperty())
    				})
    				object[_key] = _sub_entry;
    			} else {
    				object[key] = self[key];
    			}
    		}
    	}
    	return object;
    },
    
	// 内容
	setTableContent : function(tableContent) {
		this.tableContent = tableContent;
	},
	
	// 表格
	tdTemplate : function(obj) {
		var html = $('#cube-td-config').html();
		return Public.runTemplate(html, obj);
	},
	
	// 展示
	denerateLayoutMap : function() {
		var self = this;
		var e = self;
			n = self.sub_entry;
			i = new Array(e.layout_width),
			s = self.layoutMap = [];
		return _.times(e.layout_height, function() {
			s.push(_.clone(i))
		}), _.each(_.clone(n), function(e, t) {
			e.index = t;
			for (var n = e.y; n < e.height + e.y; n++) for (var i = e.x; i < e.width + e.x; i++) s[n][i] = e
		}), s
	},
	
	// 表格内容
	denerateTableContent : function() {
		for (var e = this, n = this.denerateLayoutMap(), i = [], s = 0; s < e.layout_height; s++) {
			i.push("<tr>");
			for (var a = 0; a < e.layout_width; a++) {
				var l = n[s][a];
				if (l) {
					if (s === l.y && a === l.x) {
						var c = l.image_url;
						    l.image_url = c, l.image_thumb_url = c, i.push(this.tdTemplate(l))
					}
				} else i.push('<td class="empty" data-x="' + a + '" data-y="' + s + '"></td>')
			}
			i.push("</tr>")
		}
		this.setTableContent(i.join(""));
	},
	
	// 显示提示信息
	suggestion : function() {
		AppDesign.regions.editRegion.find(".not-empty").each(function() {
			var $this = $(this);
			if (!$this.find('img').attr('src')) {
				var t = 160 * + $this.attr("colspan"), i = 160 * + $this.attr("rowspan");
				$this.append("<span>" + t + "x" + i + "</span>")
			}
		});
	},
	
	// 显示选择的数据
	renderSelection : function() {
		
		// 初始化操作
		var self = this, choices = AppDesign.regions.editRegion.find(".choices");
		choices.html("");
		
		// 实例化选择的数据
		var t = self.sub_entry;
		
		$.each(t, function(n, item) {
			// 实例化
			var i = new self.edit_item({
				model: item,
				parent: self
			});
			i.render(), n !== self.previousSelection && i.$el.hide(), choices.append(i.$el);
		}), this._slideSelection()
	},
	
	// 显示配置项
	_slideSelection : function() {
		
		var layout_map = AppDesign.regions.editRegion.find(".layout-map");
		var choices = AppDesign.regions.editRegion.find(".choices");
		
		if (layout_map.find("td").removeClass("current"), layout_map.find("td[data-index=" + this.currentSelection + "]").addClass("current"), this.previousSelection !== this.currentSelection) {
			var e = choices.find(".choice");
			e.eq(this.previousSelection).slideUp(), e.eq(this.currentSelection).slideDown(), this.previousSelection = this.currentSelection
		}
	},
	
	// 实例化
	render : function() {
		
		// 构建表格内容
		this.denerateTableContent(), this.show(), this.showConfig();
		
		// 提示显隐控制
		0 === AppDesign.regions.editRegion.find(".layout-map .empty").size() && AppDesign.regions.editRegion.find(".layout-map .help-desc").hide();
		
		// 显示
		this.suggestion(), this.renderSelection()
	},
	
	// 添加内容
	add_sub_entry : function(target) {
		var e = this;
		var s = e.sub_entry, a = $(target), c = + a.data("x"), r = + a.data("y"),
	    d = function(m) {
			p.set(m), s.push(p), e.currentSelection = s.length - 1, e.render()
	    },
	    p = new e.models_item({ x: c, y: r, parent: e}),
	    m = p.layout(e.layoutMap);
		return 1 === m.length ? void d({width: 1,height: 1}) : void new e.layout_modal({
		    layout: m,
			callback: d
		});
	},
	
	// 编辑内容
	edit_sub_entry : function(e) {
		var t = $(e), i = + t.data("index");
	    i !== this.currentSelection && (this.currentSelection = i, this._slideSelection())
	},
	
	// 删除内容
	delete_sub_entry: function(e) {
		this.sub_entry.splice($.inArray(e, this.sub_entry), 1);
		this.currentSelection = this.sub_entry.length > 0?0:-1, this.previousSelection = -1, this.render();
	},
	
	// 添加事件
	addEvent : function() {
		
		// 当前对象
		var e = this;
		
		// 实例化
		e.render();
		
		// 空内容点击
		$(AppDesign.regions.editRegion).off('click.empty').on('click.empty', '.empty', function() {
			e.add_sub_entry($(this));
	    });
		
		// 空内容点击
		$(AppDesign.regions.editRegion).off('click.not-empty').on('click.not-empty', '.not-empty', function() {
			e.edit_sub_entry($(this));
	    });
	},
	
	// 相关对象
	// 编辑数据
	edit_item : function(options) {
		
		// 默认
		this.defaults = {
				
			 // 设置属性
			 set : function(options) {
				 var n = this.model;
				 $.extend(n, options);
				 
				 // 实例化显示
				 this.parent.render();
			 },
			 
			 // 设置模板
			 template : function(context) {
				 var html = $('#cube-item-config').html();
				 return Public.runTemplate(html, context);
			 },
			 
			 // 实例化
			 render	: function() {
				 this.$el = $('<li class="choice"></li>');
				 var e = this.cid, n = _.clone(this.model);
				 $.extend(n, {
					 cid: e,
					 layout: this.model.layout(this.parent.layoutMap)
				 });
				 return this.$el.html(this.template(n)), this.addEvent(), this;
			 },
			 
			 // 添加事件
			 addEvent: function() {
				 // click .js-trigger-image
				 var self = this, wrap = self.$el;
				 
				 // 取消所有事件
				 $(wrap).off();
				 
				 // 定义选择图片
				 $(wrap).on('click', '.js-trigger-image', function(e) {
					 self.chooseImage(e);
				 });
				 
				 // 定义选择链接
				 $(wrap).on('click', '.js-trigger-link', function(e) {
					 var type = $(this).data('type');
			    	 Business.selectLink(type, function(data) {
			    		data.link_type = type;
			    		self.setLink(data);
			    	 });
				 });
				 
				 // 定义选择事件
				 $(wrap).on('click', '.js-image-layout', function(e) {
					 self.updateLayout(e);
				 });
				 
				 // 定义删除事件
				 $(wrap).on('click', '.delete', function(e) {
					 self.remove(e);
				 });
				 
				 // 下拉显示
				 $(wrap).on('click', '[data-toggle="dropdown"]', function() {
					 var $this = $(this), $parent = $this.closest('.btn-group');
					 var isActive = $parent.hasClass('open');
					 if (!isActive) {
						 $parent.toggleClass('open')
					 }
				 })
				 
				 // 关闭下拉框
				 $(wrap).on('click', function(e) {
					 var $target  = $(e.target || e.srcElement);
					 wrap.find('.btn-group').each(function(){
						var menu = $(this);
						if ($target.closest(menu).length == 0){
							menu.removeClass('open');
						};
					 });
				 });
			 },
			 
			 // 选择图片
			 chooseImage: function() {
				 var e = this;
				 Attachment.selectAttachments(function(files) {
		    		 if (!!files && files.length != 0) {
		    			 var image = files[0].src
		    			 e.set({
		    				 image_id: 'image_' + Math.random().toString().substr(2),
		    				 image_url: image
		    			 });
		    		 }
		    		 return true;
		    	 });
			 },
			 
			 // 设置链接
			 setLink : function(link) {
				 var e = this;
				 e.set({
    				 link_type : link.link_type,
    				 link_title : link.link_title,
    				 link_url : link.link_url
    			 });
			 },
			 
			 // 修改布局
			 updateLayout : function(e) {
				var t = $(e.target);
				this.set({
					width: +t.data("width"),
					height: +t.data("height")
				}), this.parent.render()
			 },
			 
			 // 删除
			 remove : function() {
				 this.model.destroy();
			 }
		};
		
		return $.extend({}, this.defaults, options);
	},
	
	// 模型数据
	models_item : function(options) {
		
		// 默认数据
		this.defaults =  function() {
			return {
				type: "cube_selection",
				x: 0,
				y: 0,
				width: 1,
				height: 1,
				title: "",
				image_id: "",
				image_url: "",
				image_thumb_url: "",
				image_width: 0,
				image_height: 0,
				link_id: "",
				link_type: "",
				link_title: "",
				link_url: "",
				show_method: "1"
			}
		};
		
		// 返回对象
		return $.extend({}, this.defaults(), {
			
			// 属性设置
			set : function(options) {
				return $.extend(this,options);
			},
			
			// 获取属性
			getProperty : function() {
		    	var self = this;
		    	var object = new Object(); 
		    	for(key in self) {
		    		if (!(key.startWith('_') || typeof(self[key]) === 'function'
		    			 || key === 'parent')) {
		    			object[key] = self[key];
		    		}
		    	}
		    	return object;
		    },
			
			// 布局方法
			layout : function(e) {
				for (var n = _.clone(this), i = [], s = [], o = n.y; o < e.length; o++) {
					for (var a = n.x; a < e[o].length; a++) {
						var l = e[o][a];
						if (l && (n.x !== l.x || n.y !== l.y)) break;
						if (o - n.y > 0) {
							var c = _.find(i, function(e) {
								return e.rows === o - n.y && e.cols === a - n.x + 1
							});
							if (!c) break
						}
						s.push({
							cols: a - n.x + 1,
							rows: o - n.y + 1
						})
					}
					if (!s.length) break;
					i = i.concat(s), s = []
				}
				return i
			},
			
			// 销毁
			destroy : function() {
				this.parent.delete_sub_entry(this);
			}
		}, options);
	},
	
	// 显示选择界面
	layout_modal : function(options) {
		
		// 默认
		this.defaults = {
			 template: $('#cube-layout-config').html(),
			 
			 // 实例化
			 render : function() {
				 var self = this;
				 var html = Public.runTemplate(self.template, {layout : self.layout});
				 Public.openWindow('设置布局', html, 650, 420, null, null, function() {
					 
					 // 添加事件
					 self.addEvent();
					 
				 }, self.close);
			 },
			 
			 // 添加事件
			 addEvent : function() {
				 
				 var self = this;
				 
				 // 定义点击事件
				 $('.layout-table').off('click').on('click', '.layout-cols li', function(e) {
					 self.chooseLayout(e);
				 });
				 
				 // 鼠标事件
				 $('.layout-table').off('mouseenter').on('mouseenter', '.layout-cols li', function(e) {
					 var t = $(e.target),
						 i = + t.data("cols"),
						 s = + t.data("rows"),
						 o = $(".layout-table li").removeClass("selected");
					 o.filter(function(e, t) {
						return t = $(t), + t.data("cols") <= i && + t.data("rows") <= s
					 }).addClass("selected");
				 })
			 },
			 
			 // 关闭
			 close : function() {
				 $('.layout-table').off();
			 },
			 
			 // 选择
			 chooseLayout : function(e) {
				 var t = $(e.target),
					 i = +t.data("cols"),
					 s = +t.data("rows");
				 
				 this.callback({
					 width: i,
					 height: s
				 });
				 Public.close();
			 },
			 
			 // 回调 -- 需要重写
			 callback : function(e){}
		};
		
		// 操作对象
		var _t = $.extend({}, this.defaults, options);
		return  _t.render(), _t;
	},
	
});

// 全画幅场景
var full_scene = new Component({
	type : 'full_scene',
	config : $('#full_scene-config').html(),
	template : $('#full_scene-template').html(),
	
	homepage_icon : 1,
	music_icon: 1,
	music_title: '',
	music_url: '',
	loop: 1,
	autoplay: 0,
	flipway: 'horizontal',
	first_page_image_url:'',
	last_page_image_url:'',
	sub_entry : [],
	
	// 添加首图
	setFirst_image : function(first_page_image_url) {
		this.first_page_image_url = first_page_image_url;
	},
	
	// 结尾图
	setLast_image : function(last_page_image_url) {
		this.last_page_image_url = last_page_image_url;
	},
	
	// 添加图片
	addImage : function(type, image, index) {
		if (type == 'first') {
			this.setFirst_image(image);
		} else if(type == 'last') {
			this.setLast_image(image);
		} else {
			this.sub_entry[index].image_url = image;
		}
		this.show();
		this.showConfig();
		this.renderSwiper();
	},
	
	// 设置轮波方式
	setFlipway : function(flipway) {
		this.flipway = flipway;
		this.show();
		this.renderSwiper();
	},
	
	// 设置循环
	setLoop : function(loop) {
		this.loop = loop;
		this.show();
		this.renderSwiper();
	},
	
	// 自动切换时长
	setAutoplay : function(autoplay) {
		this.autoplay = parseInt(autoplay);
		this.show();
		this.renderSwiper();
	},
	
	// 设置是否显示首页的链接
	setHomepage_icon : function(homepage_icon) {
		this.homepage_icon = homepage_icon?1:0;
		this.show();
		this.renderSwiper();
	},
	
	// 添加页面
	addPage : function(image) {
		this.sub_entry.push({
			image_url : image, 
			image_id : 'image_' + Math.random().toString().substr(2),
		});
		this.show();
		this.showConfig();
		this.renderSwiper();
	},
	
	// 删除一个页面
	delPage : function(index) {
		this.sub_entry.splice(index, 1);
		this.show();
		this.showConfig();
		this.renderSwiper();
	},
	
	addEvent : function() {
		
		var e = this;
		
		// 添加图片
	    $(AppDesign.regions.editRegion).on('click.js-triger-image', '.js-triger-image', function() {
	    	var type = $(this).closest('.js-first-page-region').length > 0 ? 'first': 'last';
	    	var index = 0;
	    	if ($(this).closest('.js-pages-region').length > 0) {
	    		type = 'pages';
	    		index = $(this).closest('.image-box').data('index');
	    	}
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.addImage(type, image, index);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 设置切换方式
	    $(AppDesign.regions.editRegion).on('change.flipway', 'select[name="flipway"]', function() {
	    	var flipway = $(this).val();
	    	e.setFlipway(flipway);
	    });
	    
	    // 设置切换方式
	    $(AppDesign.regions.editRegion).on('click.loop', 'input[name="loop"]', function() {
	    	var loop = $(this).is(':checked');
	    	e.setLoop(loop);
	    });
	    
	    // 设置切换时长
	    $(AppDesign.regions.editRegion).on('blur.autoplay', 'input[name="autoplay"]', function() {
	    	var autoplay = $(this).val();
	    	e.setAutoplay(autoplay);
	    });
	    
	    // homepage_icon
	    $(AppDesign.regions.editRegion).on('click.homepage_icon', 'input[name="homepage_icon"]', function() {
	    	var loop = $(this).is(':checked');
	    	e.setHomepage_icon(loop);
	    });
	    
	    // 添加页面
	    $(AppDesign.regions.editRegion).on('click.js-add-page', '.js-add-page', function() {
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.addPage(image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 删除页面
	    $(AppDesign.regions.editRegion).on('click.action_delete', '.action.delete', function() {
	    	var index = $(this).closest('.image-box').data('index');
	    	e.delPage(index);
	    });
	    
	    // 初始化
	    this.renderSwiper();
	},
	
	// 实例化swiper 组件
	renderSwiper : function() {
		var self = this;
		
		!!this.swiper && this.swiper.destroy();
		
		var id = this.id;
		
		if (!this.first_page_image_url && !this.last_page_image_url && this.sub_entry.length == 0) {return;}
		
		$('#' + id).find('.swiper-container').each(function() {
			var _swiper = $(this);
			// 实例化组件
			var swiper = new Swiper(_swiper, {
		        slidesPerView: 'auto',
		        centeredSlides: true,
		        loop: !!self.loop,
		        direction: self.flipway,
		        autoplay: self.autoplay * 1000,
		    });
			
			this.swiper = swiper;
		});
	}
});

// 新人礼包
var xr_gift = new Component({
	type : 'xr_gift',
	config : $('#xr_gift-config').html(),
	template : $('#xr_gift-template').html(),
	
	image_url : '',
	show_type: 0,
	
	// 设置显示的图片
	setImage : function(image) {
		this.image_url = image;
		this.show();
		this.showConfig();
	},
	
	// 设置显示模式
	setShow_type : function(show_type) {
		this.show_type = show_type;
	},
	
	// 添加事件
	addEvent : function() {
		
		var e = this;
		
		// 添加图片
	    $(AppDesign.regions.editRegion).off('click.js-triger-image').on('click.js-triger-image', '.js-triger-image', function() {
	    	Attachment.selectAttachments(function(files) {
	    		if(!!files && files.length != 0) {
	    			var image = files[0].src
	    			e.setImage(image);
	    		}
	    		return true;
	    	});
	    });
	    
	    // 显示模式 
	    $(AppDesign.regions.editRegion).off('click.show_type').on('click.show_type', 'input[name="show_type"]', function() {
	    	var type = $(this).val();
	    	e.setShow_type(type);
	    });
	}
});

//添加组件
var addFields = new Component({
	type : 'add_fields',
	config : '',
	init : function() {},
});

/**
 * 设计器
 */
var AppDesign = {
	
    // 所有组件
	components : [],
	
	// 字段
	fields : [],
	
	// 区域
	regions : {
		configRegion: $(".js-config-region"),
		showRegion: $(".js-fields-region"),
		editRegion: $(".js-sidebar-region"),
		addRegion: $(".js-add-region")
	},
	
	// 自定义函数
	initFunc : function() {
		// 实例化选择链接
		var _renderLink = function(link_url, link_title, link_type) {
			var context = {source_type : link_type, source_url : link_url, source_title: link_title};
			var html = Public.runTemplate($('#link-item-config2').html(), context);
			return html;
		};
		Public.registerTemplateFunction('renderLink', _renderLink);
	},
	
    // 设计器初始化
	init : function() {
		
		// 自定义函数
		this.initFunc();
		
		// 添加可以操作的组件
		this.addComponent(config);
		this.addComponent(rich_text);
		this.addComponent(goods);
		this.addComponent(goods_list);
		this.addComponent(image_ad);
		this.addComponent(title);
		this.addComponent(text_nav);
		this.addComponent(image_nav);
		this.addComponent(goods_search);
		this.addComponent(showcase);
		this.addComponent(line);
		this.addComponent(space);
		this.addComponent(tag_list);
		this.addComponent(cube);
		this.addComponent(full_scene);
		this.addComponent(xr_gift);
		this.addComponent(addFields);
		
		// 初始化配置
		addFields.init();
		
		// 添加事件
		this.addEvent();
		
		// 加载配置
		this.loadFields();
		
		// 可排序
		this.sortAble();
		
		// 默认显示配置项
		this.regions.configRegion.click();
	},
	
	// 可排序
	sortAble : function() {
		var self = this;
		var $sort = Public.sortable('.sortable', {
			draggable: '.app-field',
			filter: '.app-field-fixed',
		}, function($item, $list, datas) {
			var id = $item.data('field');
			var field = self.getField(id);
			!!field && field.active();
		});
	},
	
	// 添加事件
	addEvent : function() {
		var self = this;
		
		// 配置区域事件
		$(self.regions.configRegion).off('click.config').on('click.config', function() {
			var config = self.getComponent('config');
			(!!self.currField && self.currField.destory()), self.currField = config, config.init();
		});
		
		// 展示区域事件
		$(self.regions.showRegion).off('click.show').on('click.show', '.app-field',function() {
			var target = $(this).data('field');
			var field = AppDesign.getField(target);
			!!field && ((!!self.currField && self.currField.destory()), self.currField = field, field.active());
		});
		
		// 添加组件事件
		$(self.regions.addRegion).off('click.addRegion').on('click.addRegion', '.js-new-field', function(e) {
			var target = $(this);
			var type = $(target).data('field-type');
			
			var _proxy = AppDesign.getComponent(type), _copy = null;
			!!_proxy && ((_copy = $.extend(true, {}, _proxy)), AppDesign.addField(_copy), (!!self.currField && self.currField.destory()), self.currField = _copy, _copy.init());
		});
		
		// 组件相关事件
		$(self.regions.showRegion).off('click.action').on('click.action', '.actions-wrap',function(e) {
			e.stopPropagation();e.preventDefault(); 
			var target = $(e.target);
			if (target.hasClass('edit')) {
				$(target).closest('.app-field').click();
			} else if(target.hasClass('delete')) {
				Public.confirmx('确认删除？', function() {
					var id = $(target).closest('.app-field').data('field');
					var field = AppDesign.getField(id);
					!!field && (field.remove(), $(target).closest('.app-field').remove(), self.removeField(field));
				});
			}
		});
	},
	
	// 添加组件
	addComponent : function(component) {
		
		// 添加引用，方便查找
		this.components[component.type] = component;
	},
	
	// 通过类型获取组件
	getComponent : function(type) {
		return this.components[type] || null;
	},
	
	// 添加字段
	addField : function(field) {
		if (!field.id) {
			field.id = this.generateId();
		}
		this.fields[field.id] = field;
	},
	
	// 删除字段
	removeField : function(field) {
		this.getConfig().active();
		delete this.fields[field.id];
	},
	
	// 配置组件
	getConfig : function() {
		return this.getComponent('config');
	},
	
	// 得到字段
	getField : function(field) {
		return this.fields[field]
	},
	
	// 随机主键
	generateId : function() {
		return 'field_' + Math.random().toString().substr(2);
	},
	
	// 加载组件
	loadFields : function() {
		var self = this;
		if (Mpage.id != '-1') {
			// 配置组件
			var config = self.getConfig();
			config.set(Mpage.getConfig());
			self.initFields(Mpage.fields);
		}
	},
	
	// 初始化组件
	initFields : function(fields) {
		var self = this;
		$.each(fields, function(index, item) {
			var field = $.parseJSON(item.config);
			
			var _proxy = AppDesign.getComponent(field.type), _copy = null;
			!!_proxy && ((_copy = $.extend(true, {}, _proxy)), _copy.set(field), AppDesign.addField(_copy),  _copy.show());
		});
		
		// 清除激活
		AppDesign.regions.showRegion.find('.editing').removeClass('editing');
		
	},
	
	// 保存页面设计
	getPage: function() {
		
		var self = this;
		
		// 所有的组件
		var postData = [];
		var fields = this.fields;
		
		// 显示的
		AppDesign.regions.showRegion.find('.app-field').each(function(n, e) {
			var id = $(e).data('field');
			var item = fields[id];
		        item = item.getProperty();
		        item = $.extend(true, {}, item);
		        item = JSON.stringify(item);
		    postData.push(item);
		});
		
		// 页面数据
		var page = $.extend({}, Mpage, self.getConfig().getProperty());
		    page.fields = postData;
		
		// 页面数据
		return page;
	}
};