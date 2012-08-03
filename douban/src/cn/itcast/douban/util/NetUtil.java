package cn.itcast.douban.util;

import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import cn.itcast.douban.domain.BookItem;
import cn.itcast.douban.domain.Review;

import com.google.gdata.client.douban.DoubanService;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.Source;

public class NetUtil {
	/**
	 * 如果验证码id为空 就说明不需要验证码
	 * 
	 * @return 验证码的id
	 * @throws Exception
	 */
	public static String getCaptcha() throws Exception {
		String path = "http://www.douban.com/accounts/login";
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		Source source = new Source(conn);
		List<Element> elemets = source.getAllElements("input");
		for (Element element : elemets) {
			if ("captcha-id".equals(element.getAttributeValue("name"))) {
				System.out.println("需要输入验证码");
				return element.getAttributeValue("value");
			}
		}
		return null;
	}

	/**
	 * 
	 * @param email
	 *            用户邮箱
	 * @param pwd
	 *            用户密码
	 * @param captcha_solution
	 *            验证码的单词
	 * @param captcha_id
	 *            验证码的id
	 * @param needcaptcha
	 *            是否需要验证码
	 * @return 返回认证的token
	 * @throws Exception
	 */
	public static ArrayList<String> getAccess(String email, String pwd,
			String captcha_solution, String captcha_id, boolean needcaptcha)
			throws Exception {
		String loginpath = "http://www.douban.com/accounts/login";
		String apiKey = "0acd20946740ceb0206761bd35c26c10";
		String secret = "b7feacef6b845643";

		// 利用豆瓣api生成豆瓣的service
		DoubanService myService = new DoubanService("我的小豆豆", apiKey, secret);

		String path = myService.getAuthorizationUrl(null);
		System.out.println(path);

		// 通过代码 打开 path的 网页. 然后模拟用户点击post的操作
		// 第一步 模拟登陆豆瓣 网站 获取网站的cookie
		// DefaultHttpClient 对浏览器的简单封装
		DefaultHttpClient client1 = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(loginpath);
		// 创建一个集合, 里面 post信息的 name -value 字符串
		List<BasicNameValuePair> namevaluepair = new ArrayList<BasicNameValuePair>();
		// source=simple&redir=http%3A%2F%2Fwww.douban.com&form_email=itcastweibo@sina.cn&form_password=a11111&user_login=%E7%99%BB%E5%BD%95
		// source=simple&redir=http%3A%2F%2Fwww.douban.com&form_email=itcastweibo@sina.cn&form_password=a11111&captcha-solution=punctual&captcha-id=vAusJ3WQyNF3dDGq15XbKZfM&user_login=%E7%99%BB%E5%BD%95
		namevaluepair.add(new BasicNameValuePair("source", "simple"));
		namevaluepair.add(new BasicNameValuePair("redir",
				"http://www.douban.com"));
		namevaluepair.add(new BasicNameValuePair("form_email", email));
		namevaluepair.add(new BasicNameValuePair("form_password", pwd));
		if (needcaptcha) {
			namevaluepair.add(new BasicNameValuePair("captcha-solution",
					captcha_solution));
			namevaluepair.add(new BasicNameValuePair("captcha-id", captcha_id));
		}
		namevaluepair.add(new BasicNameValuePair("user_login", "登录"));
		// 设置发送数据的实体
		httppost.setEntity(new UrlEncodedFormEntity(namevaluepair, "UTF-8"));

		HttpResponse response = client1.execute(httppost);
		System.out.println(response.getStatusLine().getStatusCode());

		InputStream is = response.getEntity().getContent();

		// new 出来一个 jericho包里面的source对象
		Source source = new Source(is);
		System.out.println(source.toString());

		// 获取到登陆成功的cookie信息
		CookieStore cookie = client1.getCookieStore();

		DefaultHttpClient client2 = new DefaultHttpClient();
		// 带上带有用户信息的cookie
		client2.setCookieStore(cookie);
		HttpGet httpget = new HttpGet(path);
		HttpResponse response2 = client2.execute(httpget);
		Source source2 = new Source(response2.getEntity().getContent());

		System.out.println(source2.toString());
		// POST
		// /service/auth/authorize?oauth_token=3d0a95f06afb198cb71fc1d140f14ca4
		// ck=Ii7T&oauth_token=3d0a95f06afb198cb71fc1d140f14ca4&oauth_callback=&ssid=1bcb0a16&confirm=%E5%90%8C%E6%84%8f

		DefaultHttpClient client3 = new DefaultHttpClient();
		client3.setCookieStore(cookie);
		HttpPost httppost3 = new HttpPost(path);
		List<BasicNameValuePair> namevaluepair2 = new ArrayList<BasicNameValuePair>();
		namevaluepair2.add(new BasicNameValuePair("ck", "Ii7T"));
		namevaluepair2.add(new BasicNameValuePair("oauth_token", myService
				.getRequestToken()));
		namevaluepair2.add(new BasicNameValuePair("oauth_callback", ""));
		namevaluepair2.add(new BasicNameValuePair("ssid", "1bcb0a16"));
		namevaluepair2.add(new BasicNameValuePair("confirm", "同意"));

		// 设置发送数据的实体
		httppost3.setEntity(new UrlEncodedFormEntity(namevaluepair2, "UTF-8"));
		client3.execute(httppost3);

		ArrayList<String> lists = myService.getAccessToken();
		System.out.println("access token " + lists.get(0));
		System.out.println("token  secret " + lists.get(1));
		return lists;

	}

	/**
	 * 返回指定path所对应的图片
	 * 
	 * @param path
	 *            图片的地址
	 * @return 图片所对应的bitmap
	 */
	public static Bitmap getBitmapImage(String path) throws Exception {
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		InputStream is = conn.getInputStream();
		return BitmapFactory.decodeStream(is);
	}

	public static List<BookItem> getNewBooks() throws Exception {
		String path = "http://book.douban.com/latest";
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		Source source = new Source(conn);
		List<Element> lis = source.getAllElements("li");
		List<BookItem> books = new ArrayList<BookItem>();
		for (Element li : lis) {
			// 过滤出所有符合书目信息的li的节点
			// 判断li 元素是否有两个子孩子
			if (li.getChildElements().size() == 2) {
				BookItem book = new BookItem();
				List<Element> childelements = li.getChildElements();
				Element elementdiv = childelements.get(0);
				List<Element> elementdivchildren = elementdiv
						.getChildElements();
				// 获取到书的title
				book.setName(elementdivchildren.get(0).getTextExtractor()
						.toString());
				// 获取到书的描述
				book.setDescription(elementdivchildren.get(1)
						.getTextExtractor().toString());
				// 获取到书的summary
				book.setSummary(elementdivchildren.get(2).getTextExtractor()
						.toString());
				Element elementa = childelements.get(1);
				// 获取图片地址
				book.setIcon(elementa.getAllElements("img").get(0)
						.getAttributeValue("src"));
				books.add(book);
			}
		}
		return books;
	}

	/**
	 *  返回评论的集合信息 
	 * @param startIndex  评论的开始id
	 * @return  评论的集合
	 * @throws Exception
	 */
	public static List<Review> getReviews(int startIndex) throws Exception {
		String path = "http://book.douban.com/review/best/?start=" + startIndex;
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		Source source = new Source(conn); // 可以不去关心对应的字符编码集

		Element contentElement = source.getElementById("content");
		// 获取 contentElemnt 里面的 <ul class="tlst clearfix" 子孩子
		List<Review> reviews = new ArrayList<Review>();
		List<Element> subjectElements = contentElement
				.getAllElementsByClass("tlst clearfix");
		for (Element subjectElement : subjectElements) {
			Review review = new Review();
			String title = subjectElement.getFirstElement("a")
					.getTextExtractor().toString();
			review.setTitle(title);
			String reviewpath = subjectElement.getFirstElement("a")
					.getAttributeValue("href");
			review.setReviewpath(reviewpath);
			String iconpath = subjectElement.getFirstElementByClass("ilst")
					.getFirstElement("img").getAttributeValue("src");
			review.setIconpath(iconpath);
			String reviewauthor = subjectElement.getFirstElementByClass("clst")
					.getFirstElementByClass("starb").getChildElements().get(0)
					.getTextExtractor().toString();
			review.setReviewauthor(reviewauthor);

			String summary = subjectElement.getFirstElementByClass("clst")
					.getFirstElement("div").getTextExtractor().toString();
			review.setSummary(summary);
			int rating = 0;
			for (int i = 0; i <= 5; i++) {
				String rating_class = "stars" + i + " stars";
				// 检查是否有对应的rating的class信息
				if (subjectElement.getFirstElementByClass("clst")
						.getAllElementsByClass(rating_class).size() > 0) {
					rating = i;
					break;
				}
			}
			review.setRating(rating);
			reviews.add(review);
		}
		return  reviews;
	}
}
