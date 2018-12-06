<%@ page contentType="text/html;charset=UTF-8" session="false" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/include/pageHead.jsp"%>
<!DOCTYPE html>
<html>
<head>
<title><sys:site/>管理后台</title>
<%@ include file="/WEB-INF/views/include/header.jsp"%>
<link href="${ctxStatic}/common/homePage.css?v=${version}" rel="stylesheet"/>
</head>
<body>
<div class="report-wrap">
   <div class="report-main">
      <div class="-title"><i class="iconfont icon-menu"></i>待处理订单</div>
      <div class="order-stat">
          <div class="-stat-wrap"><div class="order-stat_sh -stat">待审核<span class="-num">0</span></div></div>
          <div class="-stat-wrap"><div class="order-stat_fk -stat">待付款<span class="-num">0</span></div></div>
          <div class="-stat-wrap"><div class="order-stat_fh -stat">制作中<span class="-num">0</span></div></div>
      </div>
      <div class="-title"><i class="iconfont icon-menu"></i>销售情况概况</div>
      <div class="order-stat-big">
          <div class="-stat-wrap">
             <div class="order-stat_sale -stat" id="sales">
               <div class="-stat-title">销售额统计</div>
               <div class="-stat-title-small">最后更新时间：10分钟之前</div>
               <div id="sale_chart" class="-stat-chat"></div>
               <ul class="-stat-info">
	               <li><span class="-p">￥0</span><span class="-t">本月</span></li>
	               <li><span class="-p">￥0</span><span class="-t">本周</span></li>
	               <li><span class="-p">￥0</span><span class="-t">今天</span></li>
	           </ul>
             </div>
          </div>
          <div class="-stat-wrap">
             <div class="order-stat_book -stat" id="orders">
               <div class="-stat-title">订单数统计</div>
               <div class="-stat-title-small">最后更新时间：10分钟之前</div>
               <div id="order_chart" class="-stat-chat"></div>
               <ul class="-stat-info">
	               <li><span class="-p">0</span><span class="-t">本月</span></li>
	               <li><span class="-p">0</span><span class="-t">本周</span></li>
	               <li><span class="-p">0</span><span class="-t">今天</span></li>
	           </ul>
             </div>
          </div>
      </div>
      <div class="-title"><i class="iconfont icon-menu"></i>用户注册/登录统计</div>
      <div class="order-stat-big">
          <div class="-stat-wrap">
             <div class="order-stat_sale -stat" id="userz">
               <div class="-stat-title">新用户注册统计</div>
               <div class="-stat-title-small">最后更新时间：10分钟之前</div>
               <div id="newUser_chart" class="-stat-chat"></div>
               <ul class="-stat-info">
	               <li><span class="-p">0</span><span class="-t">本月</span></li>
	               <li><span class="-p">0</span><span class="-t">本周</span></li>
	               <li><span class="-p">0</span><span class="-t">今天</span></li>
	           </ul>
             </div>
          </div>
          <div class="-stat-wrap">
             <div class="order-stat_book -stat" id="usera">
               <div class="-stat-title">活跃用户统计</div>
               <div class="-stat-title-small">最后更新时间：10分钟之前</div>
               <div id="activeUser_chart" class="-stat-chat"></div>
               <ul class="-stat-info">
	               <li><span class="-p">0</span><span class="-t">本月</span></li>
	               <li><span class="-p">0</span><span class="-t">本周</span></li>
	               <li><span class="-p">0</span><span class="-t">今天</span></li>
	           </ul>
             </div>
          </div>
      </div>
      <div class="-status">
          <div class="-title">店铺及商品提示</div>
          <div class="-main">
              <ul class="-status-info" id="product_stat">
	               <li><span class="-p">0</span><span class="-t">出售中</span></li>
	               <li><span class="-p">0</span><span class="-t">未上线</span></li>
	               <li><span class="-p">0</span><span class="-t">库存&lt;100</span></li>
	               <li><span class="-p">0</span><span class="-t">库存&lt;10</span></li>
	           </ul>
          </div>
      </div>
      <div class="-status">
          <div class="-title">交易提示</div>
          <div class="-main">
              <ul class="-status-info" id="order_stats">
	               <li><span class="-p">0</span><span class="-t">待审核</span></li>
	               <li><span class="-p">0</span><span class="-t">待付款</span></li>
	               <li><span class="-p">0</span><span class="-t">待提交需求</span></li>
	               <li><span class="-p">0</span><span class="-t">待确认需求</span></li>
	               <li><span class="-p">0</span><span class="-t">待邮寄产品</span></li>
	               <li><span class="-p">0</span><span class="-t">待签收产品</span></li>
	               <li><span class="-p">0</span><span class="-t">制作中</span></li>
	               <li><span class="-p">0</span><span class="-t">制作完成</span></li>
	               <li><span class="-p">0</span><span class="-t">申请退款</span></li>
	               <li><span class="-p">0</span><span class="-t">已退款</span></li>
	               <li><span class="-p">0</span><span class="-t">已完成</span></li>
	           </ul>
          </div>
      </div>
      <div class="-status">
          <div class="-title">热品销售排名</div>
          <div class="-main">
               <table class="table simple-table">
                  <tr>
                     <td class="tc" width="80">排名</td>
                     <td>商品信息</td>
                     <td class="tc" width="100">销量</td>
                  </tr>
                  <tbody id="products">
                  <tr><td class="tc" colspan="3">暂无排名</td></tr>
                  </tbody>
               </table>
          </div>
      </div>
      <div class="-status">
          <div class="-title">会员下单排名</div>
          <div class="-main">
               <table class="table simple-table">
                  <tr>
                     <td class="tc" width="80">排名</td>
                     <td>会员信息</td>
                     <td class="tc" width="100">下单量</td>
                     <td class="tc" width="100">总金额</td>
                  </tr>
                  <tbody id="user_orders">
                  <tr><td class="tc" colspan="4">暂无排名</td></tr>
                  </tbody>
               </table>
          </div>
      </div>
   </div>
   <div class="report-site">
      <div class="-title"><i class="iconfont icon-menu"></i>操作日志</div>
      <div class="-main"></div>
   </div>
</div>
<%@ include file="/WEB-INF/views/include/form-footer.jsp"%>
<script src="${ctxStatic}/echarts/dist/echarts.min.js" type="text/javascript"></script>
<script src="${ctxStatic}/echarts/theme/macarons.js" type="text/javascript"></script>
<script src="${ctxStatic}/common/common-charts.js" type="text/javascript"></script>
<script src="${ctxModules}/system/homepage.js?v=${version}" type="text/javascript"></script>
<script src="${ctxModules}/system/report.js?v=${version}" type="text/javascript"></script>
<script type="text/javascript">
$(function() {
    // 链接服务器
    WS.connect();
    
    // 加载代办
    TODO.load();
    
    // 用户统计
    Report.userStat();
    
    // 销售额统计
    Report.saleStat();
    
    // 订单统计
    Report.orderStat();
    
    // 商品统计
    Report.productStat();
});
</script>
</body>
</html>