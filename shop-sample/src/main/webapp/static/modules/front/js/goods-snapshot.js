$(function(){
   var swiper = new Swiper('.swiper-container', {
       pagination: '.swiper-pagination',
       slidesPerView: 'auto',
       centeredSlides: true,
   });
   
   // 异步加载图片
   $(".goods-big-images img").scrollLoading();
});