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
### 主要应用详情
#### 应用名称：Where
#### 简短说明：Local Information, Convenient Service, News, Product Introduction, Personal recommendation
#### 完整说明：
    【Local Information】
    Government agencies, educational institutions, health care, etc. master first-hand corporate yellow pages information.

    【Convenient Service】
    Consolidate all information about food, clothing, accommodation, travel, education, entertainment, and business directory.
    There are all things to eat, drink and play: hotel reservations, gourmet selections, restaurant takeaways, arts and entertainment, event venues, sports and leisure, etc.

    【News】
    Real-time agricultural news, business news, education news, environmental news, health news, infrastructure, peace and order, social welfare and other news.

    【Product Introduction】
    Government agencies, corporate yellow pages, tourist accommodation, convenience services, financial services, educational institutions, medical and health, housekeeping services, news and other functions are designed to provide users with good services!

    [Personal recommendation]
    Subscribe to your favorite content with one click and customize your own information platform.

    【本地信息】
    政府机关、教育机构、健康医疗等掌握一手企业黄页信息。

    【便捷服务】
    整合所有关于食物、衣服、住宿、出行、教育、娱乐以及工商目录信息。
    吃喝玩乐全都有：酒店预订、 美食精选、餐厅外卖、艺术娱乐、活动场地、运动休闲等。

    【新闻资讯】
    实时的农业新闻、商业新闻、教育新闻、环境新闻、健康新闻、基础设施、和平与秩序、社会福利等新闻。

    【产品简介】
    政府机构、企业黄页、旅游住宿、便民服务、金融服务、教育机构、医疗健康、家政服务、新闻资讯等功能，旨在为用户提供好的服务！

    【个性推荐】
    喜欢的内容一键订阅，定制属于自己的资讯平台。







