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

