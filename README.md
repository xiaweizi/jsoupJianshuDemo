先演示一波动态图：

![整体效果.gif](http://upload-images.jianshu.io/upload_images/4043475-1015dbce5faa6a90.gif?imageMogr2/auto-orient/strip)

> 话说这个布局就花了我半个小时...

> 一些基础的我就不说了，就简单说明一下我的数据是如何爬到的。可以直接去看一下我的源码，写的比较仓促，一些细节没有处理好，多多指教。

# 1. 准备 #
#### 1. 相关资料 ####

> [官方文档](https://jsoup.org/cookbook/)
> 
> [中文文档](http://www.open-open.com/jsoup/)

#### 2. 添加依赖 ####

	compile 'org.jsoup:jsoup:1.9.2'
#### 3. 打开简书首页 ####
![first.gif](http://upload-images.jianshu.io/upload_images/4043475-47c5c0014f33995c.gif?imageMogr2/auto-orient/strip)


在单个部分上 右击，然后点击**检查**选项(我用的是QQ浏览器，其他未尝试)，底部就会跳出网页的源码，并且会跟踪到这个item对应的源码。

从上图可以大概了解到每个`<li></li>`标签里的内容就是我们每个item的信息。
# 2. 爬数据  #
#### 1. 获取 `Document` 对象 ####

		Document document = Jsoup.connect("http://www.jianshu.com/")
		                         .timeout(10000)
		                         .get();
>这里通过建立与服务器的链接，并设置10s的超时连接来获取 `Document` 对象

#### 2. 获取跟标签的 `Elements` 对象 ####

![first.PNG](http://upload-images.jianshu.io/upload_images/4043475-3c651e3f8adb56fc.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

    Elements noteList = document.select("ul.note-list");
    Elements li = noteList.select("li");
>找到文章列表模块，发现 `<ul class="note-list></ul>` 是我们需要信息的跟标签。通过 `select` 方法查询节点所有信息。

	for (Element element : li) {
		...
	}

>下面全部都是 `li`标签的列表，里面的内容大致相似，我们就可以通过循环来遍历里面的信息。

#### 3. 获取每个部分所有信息 ####

因为信息比较多，我就选择比较有代表性的来将一下。

有个非常简单的方式：直接在你需要获取内容的部分右击，点击 **检查**，就可以直接追踪到要查询的位置。

![second.gif](http://upload-images.jianshu.io/upload_images/4043475-97a92643e9d4212e.gif?imageMogr2/auto-orient/strip)

#### 1. 标题 ####
> 就拿标题而言，直接在标题右击-->检查，即可，一目了然。然后我把数据截图一下。

![second.PNG](http://upload-images.jianshu.io/upload_images/4043475-79b584e8b487c788.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

	String title = element.select("a.title").text()

> 通过 `select` 查询节点信息，然后` .text` 获取里面文本内容。


#### 2. 头像： ####
![third.PNG](http://upload-images.jianshu.io/upload_images/4043475-9a1309b80f3eca30.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

	String avatar = element.select("a.avatar").select("img").attr("src")
> 这个就是先找到头像 节点，然后图片节点，最后通过 `attr` 获取图片 `url`

#### 3. 首页链接 ####

![forth.PNG](http://upload-images.jianshu.io/upload_images/4043475-f74c2389c73ebcc6.PNG?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

	String authorLink = element.select("a.blue-link").attr("abs:href")
> 这里注意 `href` 元素，他存放的就是跳转链接，不过是相对路径，这个时候就需要通过 `attr("abs:href")` 获取绝对路径。

其他 就 大同小异，其实我知道也就这么多，也是不断尝试通过打印得出来的，还是比较心酸的，比较没学过 `js`，不过对 `js` 挺有兴趣的。
# 3. 封装 #
剩下的就是将获取到的数据加载到bean对象中 

#### 1. 创建 bean 对象 ####
	public class JianshuBean {
	    private String authorName;          // 作者
	    private String authorLink;          // 作者链接
	    private String time;                // 更新时间
	    private String primaryImg;          // 主图
	    private String avatarImg;           // 头像
	
	    private String title;               // 标题
	    private String titleLink;           // 标题链接
	    private String content;             // 内容
	    private String collectionTagLink;   // 专题链接
	    private String readNum;             // 阅读量
	
	    private String talkNum;             // 评论
	    private String likeNum;             // 喜欢
	    private String collectionTag;       // 专题
	
		// ... get set
	}
#### 2. 将获取到的数据添加到集合中 ####

	for (Element element : li) {
        JianshuBean bean = new JianshuBean();
        bean.setAuthorName(element.select("div.name").text()); // 作者姓名
        bean.setAuthorLink(element.select("a.blue-link").attr("abs:href")); // 作者首页链接
        bean.setTime(element.select("span.time").attr("data-shared-at"));   // 发表时间
        bean.setPrimaryImg(element.select("img").attr("src"));  // 主图
        bean.setAvatarImg(element.select("a.avatar").select("img").attr("src")); // 头像

        bean.setTitle(element.select("a.title").text());    // 标题
        bean.setTitleLink(element.select("a.title").attr("abs:href")); // 标题链接

        bean.setContent(element.select("p.abstract").text());       // 内容
        bean.setCollectionTagLink(element.select("a.collection-tag").attr("abs:href")); // 专题链接

        String[] text = element.select("div.meta").text().split(" ");
        if (text[0].matches("[0-9]+")) {
            bean.setReadNum(text[0]);
            bean.setTalkNum(text[1]);
            bean.setLikeNum(text[2]);
        } else {
            bean.setCollectionTag(text[0]);
            bean.setReadNum(text[1]);
            bean.setTalkNum(text[2]);
            bean.setLikeNum(text[3]);

        }
        mBeans.add(bean);
    }

再来看一下效果：

![整体效果.gif](http://upload-images.jianshu.io/upload_images/4043475-1015dbce5faa6a90.gif?imageMogr2/auto-orient/strip)
- 点击头像查看作者信息
- 点击图片或文字查看文章内容
- 点击专题查看专题内容
- 下拉刷新获取最新内容

