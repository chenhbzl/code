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
	 * �����֤��idΪ�� ��˵������Ҫ��֤��
	 * 
	 * @return ��֤���id
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
				System.out.println("��Ҫ������֤��");
				return element.getAttributeValue("value");
			}
		}
		return null;
	}

	/**
	 * 
	 * @param email
	 *            �û�����
	 * @param pwd
	 *            �û�����
	 * @param captcha_solution
	 *            ��֤��ĵ���
	 * @param captcha_id
	 *            ��֤���id
	 * @param needcaptcha
	 *            �Ƿ���Ҫ��֤��
	 * @return ������֤��token
	 * @throws Exception
	 */
	public static ArrayList<String> getAccess(String email, String pwd,
			String captcha_solution, String captcha_id, boolean needcaptcha)
			throws Exception {
		String loginpath = "http://www.douban.com/accounts/login";
		String apiKey = "0acd20946740ceb0206761bd35c26c10";
		String secret = "b7feacef6b845643";

		// ���ö���api���ɶ����service
		DoubanService myService = new DoubanService("�ҵ�С����", apiKey, secret);

		String path = myService.getAuthorizationUrl(null);
		System.out.println(path);

		// ͨ������ �� path�� ��ҳ. Ȼ��ģ���û����post�Ĳ���
		// ��һ�� ģ���½���� ��վ ��ȡ��վ��cookie
		// DefaultHttpClient ��������ļ򵥷�װ
		DefaultHttpClient client1 = new DefaultHttpClient();
		HttpPost httppost = new HttpPost(loginpath);
		// ����һ������, ���� post��Ϣ�� name -value �ַ���
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
		namevaluepair.add(new BasicNameValuePair("user_login", "��¼"));
		// ���÷������ݵ�ʵ��
		httppost.setEntity(new UrlEncodedFormEntity(namevaluepair, "UTF-8"));

		HttpResponse response = client1.execute(httppost);
		System.out.println(response.getStatusLine().getStatusCode());

		InputStream is = response.getEntity().getContent();

		// new ����һ�� jericho�������source����
		Source source = new Source(is);
		System.out.println(source.toString());

		// ��ȡ����½�ɹ���cookie��Ϣ
		CookieStore cookie = client1.getCookieStore();

		DefaultHttpClient client2 = new DefaultHttpClient();
		// ���ϴ����û���Ϣ��cookie
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
		namevaluepair2.add(new BasicNameValuePair("confirm", "ͬ��"));

		// ���÷������ݵ�ʵ��
		httppost3.setEntity(new UrlEncodedFormEntity(namevaluepair2, "UTF-8"));
		client3.execute(httppost3);

		ArrayList<String> lists = myService.getAccessToken();
		System.out.println("access token " + lists.get(0));
		System.out.println("token  secret " + lists.get(1));
		return lists;

	}

	/**
	 * ����ָ��path����Ӧ��ͼƬ
	 * 
	 * @param path
	 *            ͼƬ�ĵ�ַ
	 * @return ͼƬ����Ӧ��bitmap
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
			// ���˳����з�����Ŀ��Ϣ��li�Ľڵ�
			// �ж�li Ԫ���Ƿ��������Ӻ���
			if (li.getChildElements().size() == 2) {
				BookItem book = new BookItem();
				List<Element> childelements = li.getChildElements();
				Element elementdiv = childelements.get(0);
				List<Element> elementdivchildren = elementdiv
						.getChildElements();
				// ��ȡ�����title
				book.setName(elementdivchildren.get(0).getTextExtractor()
						.toString());
				// ��ȡ���������
				book.setDescription(elementdivchildren.get(1)
						.getTextExtractor().toString());
				// ��ȡ�����summary
				book.setSummary(elementdivchildren.get(2).getTextExtractor()
						.toString());
				Element elementa = childelements.get(1);
				// ��ȡͼƬ��ַ
				book.setIcon(elementa.getAllElements("img").get(0)
						.getAttributeValue("src"));
				books.add(book);
			}
		}
		return books;
	}

	/**
	 *  �������۵ļ�����Ϣ 
	 * @param startIndex  ���۵Ŀ�ʼid
	 * @return  ���۵ļ���
	 * @throws Exception
	 */
	public static List<Review> getReviews(int startIndex) throws Exception {
		String path = "http://book.douban.com/review/best/?start=" + startIndex;
		URL url = new URL(path);
		URLConnection conn = url.openConnection();
		Source source = new Source(conn); // ���Բ�ȥ���Ķ�Ӧ���ַ����뼯

		Element contentElement = source.getElementById("content");
		// ��ȡ contentElemnt ����� <ul class="tlst clearfix" �Ӻ���
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
				// ����Ƿ��ж�Ӧ��rating��class��Ϣ
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
