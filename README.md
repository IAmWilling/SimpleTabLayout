# SimpleTabLayout
简单的tablayout开源库 多多学习

|  属性   | 默认值 | 含义  |
|  ----  | ---- | ----  |
| default_text_color  | 0xff8A8A8A | 默认文本颜色 |
| selected_text_color  | 14 | 选中的文本颜色 |
| default_text_size  | 0xff03a9f4 | 默认文本大小（sp） |
| indicator_color  | 0xffff33f5 | 指示器颜色 |
| is_selected_text_bold  | false | 选中文本是否加粗 |
| indicator_height  | 3dp | 指示器高度 |
| simple_scale  | 1.2 | 渐变文本大小（选中后） |
| indicator_alignment_text  | false | 指示器是否对齐文字 |
| text_and_indicator_distance  | 3dp | 指示器到文字底部距离（dp） |
| is_show_indicator  | true | 是否显示指示器（默认true） |

#引入
root build
```
allprojects {
	repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
app build
```
	implementation 'com.github.IAmWilling:SimpleTabLayout:1.0.0'
```

#使用
> XML布局文件中使用
```xml
    <com.zhy.simple.TabLayout
        app:default_text_size="14sp"
        app:simple_scale="1.2"
        app:selected_text_color="#f96d0a"
        app:indicator_color="#f96d0a"
        app:is_show_indicator="false"
        app:text_and_indicator_distance="3dp"
        app:indicator_height="2dp"
        android:id="@+id/myTabLayout"
        android:layout_margin="0dp"
        android:layout_width="match_parent"
        android:layout_height="40dp" />
```
---
> 代码
设置Tab
此Tab只需要一个String数组即可
绑定ViewPager2 （目前只支持了ViewPager2）
然后设置adapter在进行绑定
```
myTabLayout.addTab(tabs);
viewPager2.setAdapter(new TestFragmentAdapter(this));
myTabLayout.setWithViewPager2(viewPager2);
```
`addTab(String[] strings)` 先绑定viewpager2

`setWithViewPager2(viewPager2)` 先绑定viewpager2

`CurrentViewPagerChangeListener` 设置当前绑定的viewpager滑动监听器
`setOnPageChangeListener` 设置 单一的状态改变监听 只返回选中的position
`setOnSelectedTabListener` 选中的tab的监听

