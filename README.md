## 关于代码中的警告（黄色的warning）
尽最大能力消除！

## 布局文件
1，根布局使用 ConstraintLayout  
2，列表使用 RecyclerView + BaseQuickAdapter  
3，标题使用 JcsTitle  
### 命名规则
   1，activity或fragment：activity_xx_xx  fragment_xx_xx  
   2，列表的item：item_xx_xx  
   3，弹窗：dialog_xx_xx  
   4，自定义组件：widget_xx_xx  
   5，布局中的组件id，小驼峰即可  

## java文件
1，Activity 继承 BaseActivity  
2，Fragment 继承 BaseFragment（若需要全屏，则继承 BaseFullFragment）  
3，初始化组件代码写在 initView() 中，设置事件监听写在 bindListener() 中，获取数据写在 initData() 中  
4，若页面有标题，则在文档注释中标明页面标题，便于搜索  
### 命名规则
   1，activity或fragment：XxxYyyActivity  XxxYyyFragment  
   2，成员变量：mXxxYyy  
   
## 网络请求相关
1，使用 GsonFormatPlus 生成数据类  
2，request 命名为：XxxYyyRequest 放在 /api/request 下  
3，response 命名为：XxxYyyResponse 放在 /api/response 下  
4，在 RetrofitApi 中定义相关方法即可  
5，定义 XxxModel 继承 BaseModel 即可在需要的地方，初始化，使用  

## shape和selector
1，shape和selector命名中要含有shape的所有子元素，例如一个shape颜色为红，圆角为8  
    命名为：shape_red_radius_8  
    
## 图片
1，.png 的图片放在对应的 mipmap 下，已有的暂不修改


## Google play 相关
GooglePlay 发布成功后，在Android以及Pc搜索不到应用解决方案
1.检查发布的国家和地区
2.google play 控制台 - 商店发布 - 商店设置
  检查是否有和应用类别相关的标签

  eg：应用类别：生活时尚
      标签选择生活时尚



## v17.0.2.4( 2021-10-28)
#### 全新的设计风格和项目重构
##### 酒店：首页、详情、房间详情、酒店预订、地图
##### 旅游：首页、详情、地图
##### 注：v15 v16版本内容一致，v17修正已存在问题


## v18.0.2.5
UI更和项目重构
【美食首页】
    列表和地图整合在同一页，样式更新为瀑布流，增加购物车入口
【店铺详情】
    1.更新滑动交互方式，菜品列表和评论列表更新
    2.进入堂食详情前，新增修改菜品数量交互
【店铺评论】
    更新UI以及筛选方式
【堂食详情】
    1.更新UI和滑动交互方式，
    2.新增修改菜品数量交互
【美食购物车】
    1.内部逻辑重构，兼容提交订单页商品列表
    2.更新UI和滑动交互方式，
【美食订单提交】
    1.支持分店铺展示不通菜品
    2.更新UI
【外卖详情】
    1.更新UI和滑动交互方式，
【外卖订单提交】
    1.更新UI
    2.更新地和送达时间交互
【地址】
    1.更新收地址列表和编辑

    1 上午修正

我的卡券
12

卡券商品
4

领券中心




